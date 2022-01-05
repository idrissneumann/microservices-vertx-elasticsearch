package com.bblvertx.persistence.mapper;

import static com.bblvertx.utils.singleton.impl.JdbcDataSource.getIntFromRS;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.persistence.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User id mapper in jdbc compliant database.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class JdbcUserIdMapper implements RowMapper<Serializable> {
  /**
   * {@inheritDoc}
   */
  @Override
  public Serializable map(Object rs) {
    try {
      return getIntFromRS((ResultSet) rs, "t_user_id");
    } catch (SQLException e) {
      throw new TechnicalException(e);
    }
  }
}
