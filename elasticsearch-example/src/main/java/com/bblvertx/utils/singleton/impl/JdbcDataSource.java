package com.bblvertx.utils.singleton.impl;

import static com.bblvertx.SeConstants.APP_CONFIG_FILE;
import static com.bblvertx.SeConstants.DB_KEY_DRIVER;
import static com.bblvertx.SeConstants.DB_KEY_MAX_POOL_SIZE;
import static com.bblvertx.SeConstants.DB_KEY_MIN_POOL_SIZE;
import static com.bblvertx.SeConstants.DB_KEY_PASSWD;
import static com.bblvertx.SeConstants.DB_KEY_URL;
import static com.bblvertx.SeConstants.DB_KEY_USERNAME;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.persistence.QueryParam;
import com.bblvertx.persistence.RowMapper;
import com.bblvertx.utils.singleton.SeDataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * Datasource for jdbc connection singleton.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@Singleton
public class JdbcDataSource implements SeDataSource {
  private static final String SELECT_TEST_CONNECTION = "SELECT 1 AS nb";
  private static final Logger LOGGER = LogManager.getLogger(JdbcDataSource.class);

  @Inject
  private PropertyReader prop;

  private BasicDataSource dataSource;

  /**
   * {@inheritDoc}
   */
  @Override
  @PostConstruct
  public void init() {
    try {
      dataSource = new BasicDataSource();
      dataSource.setDriverClassName(prop.get(APP_CONFIG_FILE, DB_KEY_DRIVER));
      dataSource.setUrl(prop.get(APP_CONFIG_FILE, DB_KEY_URL));
      dataSource.setUsername(prop.get(APP_CONFIG_FILE, DB_KEY_USERNAME));
      dataSource.setPassword(prop.get(APP_CONFIG_FILE, DB_KEY_PASSWD));
      dataSource.setMinIdle(prop.getInt(APP_CONFIG_FILE, DB_KEY_MIN_POOL_SIZE));
      dataSource.setMaxIdle(prop.getInt(APP_CONFIG_FILE, DB_KEY_MIN_POOL_SIZE));
      dataSource.setMaxTotal(prop.getInt(APP_CONFIG_FILE, DB_KEY_MAX_POOL_SIZE));
      dataSource.setPoolPreparedStatements(true);

      testConnection();
    } catch (Exception e) {
      LOGGER.error("Unable to connect to JDBC compliant database : {}", e.getMessage());
    }
  }

  /**
   * Testing the connection.
   */
  private void testConnection() {
    execute(SELECT_TEST_CONNECTION, new RowMapper<Integer>() {
      @Override
      public Integer map(Object rs) {
        try {
          return JdbcDataSource.getIntFromRS((ResultSet) rs, "nb");
        } catch (SQLException e) {
          throw new TechnicalException(e);
        }
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Serializable> List<T> execute(String query, RowMapper<T> rowMapper) {
    LOGGER.info("Launching " + query);
    try {
      Connection co = getDataSource().getConnection();
      Statement stmt = co.createStatement();
      ResultSet rs = stmt.executeQuery(query);

      List<T> rtn = new ArrayList<T>();

      while (rs.next()) {
        rtn.add(rowMapper.map(rs));
      }

      rs.close();
      stmt.close();
      co.close();

      LOGGER.info("End " + query);
      return rtn;
    } catch (SQLException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Serializable> List<T> execute(String query, List<QueryParam> params,
      RowMapper<T> rowMapper) {
    LOGGER.info("Launching " + query);
    try {
      Connection co = getDataSource().getConnection();

      PreparedStatement stmt = co.prepareStatement(query);
      if (isNotEmpty(params)) {
        Collections.sort(params);
        for (QueryParam p : params) {
          LOGGER.info(p.toString());
          stmt.setObject(p.getOrder(), p.getClazz().cast(p.getValue()));
        }
      }

      ResultSet rs = stmt.executeQuery();

      List<T> rtn = new ArrayList<T>();

      while (rs.next()) {
        rtn.add(rowMapper.map(rs));
      }

      rs.close();
      stmt.close();
      co.close();

      LOGGER.info("End " + query);
      return rtn;
    } catch (SQLException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer executeUpdate(String query) {
    LOGGER.info("Launching " + query);
    try {
      Connection co = getDataSource().getConnection();
      Statement stmt = co.createStatement();
      Integer rtn = stmt.executeUpdate(query);
      stmt.close();
      co.close();
      LOGGER.info("End " + query);
      return rtn;
    } catch (SQLException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * executing SQL query with params and return the number of updates.
   * 
   * @param query
   * @param params
   * @return
   * @throws SQLException
   */
  @Override
  public Integer executeUpdate(String query, List<QueryParam> params) {
    LOGGER.info("Launching " + query);
    try {
      Connection co = getDataSource().getConnection();
      PreparedStatement stmt = co.prepareStatement(query);
      if (isNotEmpty(params)) {
        Collections.sort(params);
        for (QueryParam p : params) {
          stmt.setObject(p.getOrder(), p.getClazz().cast(p.getValue()));
          LOGGER.info(p.toString());
        }
      }

      Integer rtn = stmt.executeUpdate();
      stmt.close();
      co.close();

      LOGGER.info("End " + query);
      return rtn;
    } catch (SQLException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeStoredProcedure(String name) {
    LOGGER.info("Launching stored proc " + name);
    try {
      Connection co = getDataSource().getConnection();
      CallableStatement proc = co.prepareCall("{ call " + name + "() }");
      proc.execute();
      proc.close();
      co.close();
      LOGGER.info("End stored proc " + name);
    } catch (SQLException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * Getting a rs value as int.
   * 
   * @param rs
   * @param name
   * @return Integer
   * @throws SQLException
   */
  public static Integer getIntFromRS(ResultSet rs, String name) throws SQLException {
    return rs.getInt(name);
  }

  /**
   * Getting a rs value as long.
   * 
   * @param rs
   * @param name
   * @return Integer
   * @throws SQLException
   */
  public static Long getLongFromRS(ResultSet rs, String name) throws SQLException {
    return rs.getLong(name);
  }

  /**
   * Getting a rs value as string.
   * 
   * @param rs
   * @param name
   * @return String
   * @throws SQLException
   */
  public static String getStringFromRS(ResultSet rs, String name) throws SQLException {
    return rs.getString(name);
  }

  /**
   * Getting a rs value as calendar.
   * 
   * @param rs
   * @param name
   * @return Calendar
   * @throws SQLException
   */
  public static Calendar getDateFromRS(ResultSet rs, String name) throws SQLException {
    Calendar cal = (Calendar) Calendar.getInstance().clone();
    cal.setTime(rs.getDate(name));
    return cal;
  }

  /**
   * Getting the datasource.
   * 
   * @return DataSource
   */
  public DataSource getDataSource() {
    return dataSource;
  }
}
