package com.bblvertx.utils.singleton;

import com.bblvertx.persistence.QueryParam;
import com.bblvertx.persistence.RowMapper;

import java.io.Serializable;
import java.util.List;

/**
 * Datasource interface.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public interface SeDataSource {
  /**
   * Initializing connection with the database.
   */
  void init();

  /**
   * Fetching the result of a SQL query with a row mapper.
   * 
   * @param query
   * @return List<T>
   */
  <T extends Serializable> List<T> execute(String query, RowMapper<T> rowMapper);


  /**
   * Fetching the result of a query with parameters and a row mapper.
   * 
   * @param query
   * @return List<T>
   */
  <T extends Serializable> List<T> execute(String query, List<QueryParam> params,
      RowMapper<T> rowMapper);

  /**
   * executing SQL query and return the number of updates.
   * 
   * @param query
   * @return Integer
   */
  Integer executeUpdate(String query);

  /**
   * executing SQL query with params and return the number of updates.
   * 
   * @param query
   * @param params
   * @return Integer
   */
  public Integer executeUpdate(String query, List<QueryParam> params);

  /**
   * Launching stored proc.
   * 
   * @param name
   */
  public void executeStoredProcedure(String name);
}
