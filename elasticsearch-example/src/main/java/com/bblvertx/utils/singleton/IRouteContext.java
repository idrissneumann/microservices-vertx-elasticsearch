package com.bblvertx.utils.singleton;

public interface IRouteContext {
    IPropertyReader getProp();

    IESClient getEsClient();

    ICassandraDataSource getCassandraDataSource();

    IJdbcDataSource getJdbcDataSource();
}
