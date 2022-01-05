package com.bblvertx.utils.singleton.impl;

import static com.bblvertx.SeConstants.APP_CONFIG_FILE;
import static com.bblvertx.SeConstants.ES_KEY_CLUSTER;
import static com.bblvertx.SeConstants.ES_KEY_HOST;
import static com.bblvertx.SeConstants.ES_KEY_PORT;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Elastic Search client singleton.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@Singleton
public class ESClient {
  @Inject
  private PropertyReader prop;

  private static final String CLUSTER_NAME = "cluster.name";

  private TransportClient client;

  /**
   * Init of singleton.
   * 
   * @throws IOException
   */
  @PostConstruct
  public void init() throws IOException {
    String host = prop.get(APP_CONFIG_FILE, ES_KEY_HOST);
    Integer port = prop.getInt(APP_CONFIG_FILE, ES_KEY_PORT);
    String cluster = prop.get(APP_CONFIG_FILE, ES_KEY_CLUSTER);

    Settings settings = ImmutableSettings.settingsBuilder().put(CLUSTER_NAME, cluster).build();

    client = new TransportClient(settings);
    client.addTransportAddress(new InetSocketTransportAddress(host, port));
  }

  /**
   * @return the client
   */
  public TransportClient getClient() {
    return client;
  }
}
