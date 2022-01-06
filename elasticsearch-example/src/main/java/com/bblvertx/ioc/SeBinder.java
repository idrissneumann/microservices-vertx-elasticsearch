package com.bblvertx.ioc;

import com.bblvertx.utils.singleton.ICassandraDataSource;
import com.bblvertx.utils.singleton.IESClient;
import com.bblvertx.utils.singleton.IJdbcDataSource;
import com.bblvertx.utils.singleton.IPropertyReader;
import com.bblvertx.utils.singleton.IRouteContext;
import com.bblvertx.utils.singleton.impl.CassandraDataSourceImpl;
import com.bblvertx.utils.singleton.impl.ESClientImpl;
import com.bblvertx.utils.singleton.impl.JdbcDataSourceImpl;
import com.bblvertx.utils.singleton.impl.PropertyReaderImpl;

import com.bblvertx.utils.singleton.impl.RouteContextImpl;
import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;

import javax.inject.Singleton;

/**
 * Init of lifecycle beans context.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class SeBinder extends AbstractModule {
  private Vertx vertx;

  public SeBinder(Vertx vertx) {
    this.vertx = vertx;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void configure() {
    bind(Vertx.class).toInstance(vertx);
    IPropertyReader reader = PropertyReaderImpl.newInstance();
    bind(IPropertyReader.class).toInstance(reader);
    bind(IJdbcDataSource.class).to(JdbcDataSourceImpl.class).in(Singleton.class);
    bind(ICassandraDataSource.class).to(CassandraDataSourceImpl.class).in(Singleton.class);
    bind(IESClient.class).to(ESClientImpl.class).in(Singleton.class);
    bind(IRouteContext.class).to(RouteContextImpl.class).in(Singleton.class);
  }
}
