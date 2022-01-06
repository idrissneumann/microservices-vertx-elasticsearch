package com.bblvertx.utils.singleton.impl;

import com.bblvertx.utils.singleton.ICassandraDataSource;
import com.bblvertx.utils.singleton.IESClient;
import com.bblvertx.utils.singleton.IJdbcDataSource;
import com.bblvertx.utils.singleton.IPropertyReader;
import com.bblvertx.utils.singleton.IRouteContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Route singleton context.
 *
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@Singleton
public class RouteContextImpl implements IRouteContext {
    @Inject
    private IJdbcDataSource jdbcDataSource;

    @Inject
    private ICassandraDataSource cassandraDataSource;

    @Inject
    private IPropertyReader prop;

    @Inject
    private IESClient esClient;

    /**
     * @return the dataSource
     */
    @Override
    public IJdbcDataSource getJdbcDataSource() {
        return jdbcDataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setJdbcDataSource(IJdbcDataSource dataSource) {
        this.jdbcDataSource = dataSource;
    }

    /**
     * @return the prop
     */
    @Override
    public IPropertyReader getProp() {
        return prop;
    }

    /**
     * @param prop the prop to set
     */
    public void setProp(IPropertyReader prop) {
        this.prop = prop;
    }

    /**
     * @return the esClient
     */
    @Override
    public IESClient getEsClient() {
        return esClient;
    }

    /**
     * @param esClient the esClient to set
     */
    public void setEsClient(IESClient esClient) {
        this.esClient = esClient;
    }

    /**
     * @return the cassandraDataSource
     */
    @Override
    public ICassandraDataSource getCassandraDataSource() {
        return cassandraDataSource;
    }

    /**
     * @param cassandraDataSource the cassandraDataSource to set
     */
    public void setCassandraDataSource(ICassandraDataSource cassandraDataSource) {
        this.cassandraDataSource = cassandraDataSource;
    }
}
