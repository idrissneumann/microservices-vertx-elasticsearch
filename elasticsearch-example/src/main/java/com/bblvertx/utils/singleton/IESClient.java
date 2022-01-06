package com.bblvertx.utils.singleton;

public interface IESClient {
    /**
     * @return the client
     */
    IRestHighLevelClient getClient();
}
