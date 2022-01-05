package com.bblvertx.utils.singleton.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Route singleton context.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@Singleton
public class RouteContext {
  @Inject
  private JdbcDataSource jdbcDataSource;

  @Inject
  private CassandraDataSource cassandraDataSource;

  @Inject
  private PropertyReader prop;

  @Inject
  private ESClient esClient;

  /**
   * @return the dataSource
   */
  public JdbcDataSource getJdbcDataSource() {
    return jdbcDataSource;
  }

  /**
   * @param dataSource the dataSource to set
   */
  public void setJdbcDataSource(JdbcDataSource dataSource) {
    this.jdbcDataSource = dataSource;
  }

  /**
   * @return the prop
   */
  public PropertyReader getProp() {
    return prop;
  }

  /**
   * @param prop the prop to set
   */
  public void setProp(PropertyReader prop) {
    this.prop = prop;
  }

  /**
   * @return the esClient
   */
  public ESClient getEsClient() {
    return esClient;
  }

  /**
   * @param esClient the esClient to set
   */
  public void setEsClient(ESClient esClient) {
    this.esClient = esClient;
  }

  /**
   * @return the cassandraDataSource
   */
  public CassandraDataSource getCassandraDataSource() {
    return cassandraDataSource;
  }

  /**
   * @param cassandraDataSource the cassandraDataSource to set
   */
  public void setCassandraDataSource(CassandraDataSource cassandraDataSource) {
    this.cassandraDataSource = cassandraDataSource;
  }
}
