CREATE TABLE T_USER (
	id SERIAL,
	name VARCHAR(254), 
	firstname VARCHAR(254), 
	email VARCHAR(254) UNIQUE,
	skill VARCHAR(254),
	date_connect DATE,
	PRIMARY KEY (id)
);

CREATE TABLE T_USER_IDX (
	t_user_id INT NOT NULL,
	rs_search INT NOT NULL,
	date_update DATE
);

CREATE OR REPLACE FUNCTION index_all () RETURNS VOID AS '
    DECLARE
    BEGIN 
        TRUNCATE t_user_idx;
        INSERT INTO t_user_idx (t_user_id, rs_search, date_update) SELECT id, 1 AS rs_search, now() AS date_update FROM t_user;
    END; 
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete_user_idx () RETURNS TRIGGER AS '
    DECLARE 
        nb INT; 
    BEGIN 
        SELECT INTO nb COUNT(*) FROM T_USER_IDX WHERE T_USER_ID = OLD.ID; 
        IF (nb >= 1) THEN 
            UPDATE T_USER_IDX SET RS_SEARCH = -1, DATE_UPDATE = now() WHERE T_USER_ID = OLD.ID;
        ELSE
            INSERT INTO T_USER_IDX (T_USER_ID, RS_SEARCH, DATE_UPDATE) VALUES (OLD.ID, -1, now());
        END IF; 
        RETURN OLD; 
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_user_idx () RETURNS TRIGGER AS '
    DECLARE 
        nb INT; 
    BEGIN 
        SELECT INTO nb COUNT(*) FROM T_USER_IDX WHERE T_USER_ID = NEW.ID; 
        IF (nb >= 1) THEN 
            UPDATE T_USER_IDX SET RS_SEARCH = 1, DATE_UPDATE = now() WHERE T_USER_ID = NEW.ID;
        ELSE
            INSERT INTO T_USER_IDX (T_USER_ID, RS_SEARCH, DATE_UPDATE) VALUES (NEW.ID, 1, now());
        END IF; 
        RETURN NEW; 
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER t_delete_user_idx AFTER DELETE
    ON T_USER FOR EACH ROW
    EXECUTE PROCEDURE delete_user_idx();
    
CREATE TRIGGER t_update_user_idx_insert BEFORE INSERT
    ON T_USER FOR EACH ROW
    EXECUTE PROCEDURE update_user_idx();

CREATE TRIGGER t_update_user_idx_update BEFORE UPDATE
    ON T_USER FOR EACH ROW
    EXECUTE PROCEDURE update_user_idx();