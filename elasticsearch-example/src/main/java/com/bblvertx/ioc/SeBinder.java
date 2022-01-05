package com.bblvertx.ioc;

import com.bblvertx.utils.singleton.impl.CassandraDataSource;
import com.bblvertx.utils.singleton.impl.ESClient;
import com.bblvertx.utils.singleton.impl.JdbcDataSource;
import com.bblvertx.utils.singleton.impl.PropertyReader;
import com.bblvertx.utils.singleton.impl.RouteContext;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * Init of lifecycle beans context.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class SeBinder extends AbstractBinder {
  /**
   * {@inheritDoc}
   */
  @Override
  protected void configure() {
    bind(PropertyReader.class).to(PropertyReader.class).in(Singleton.class);

    bind(JdbcDataSource.class).to(JdbcDataSource.class).in(Singleton.class);

    bind(CassandraDataSource.class).to(CassandraDataSource.class).in(Singleton.class);

    bind(ESClient.class).to(ESClient.class).in(Singleton.class);

    bind(RouteContext.class).to(RouteContext.class).in(Singleton.class);
  }
}
