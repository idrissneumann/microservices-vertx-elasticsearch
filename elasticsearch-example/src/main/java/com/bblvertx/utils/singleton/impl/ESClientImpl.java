package com.bblvertx.utils.singleton.impl;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.utils.singleton.IESClient;
import com.bblvertx.utils.singleton.IPropertyReader;
import com.bblvertx.utils.singleton.IRestHighLevelClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Objects;

import static com.bblvertx.SeConstants.APP_CONFIG_FILE;
import static com.bblvertx.SeConstants.ES_KEY_HOST;
import static com.bblvertx.SeConstants.ES_KEY_PASSWORD;
import static com.bblvertx.SeConstants.ES_KEY_PATH;
import static com.bblvertx.SeConstants.ES_KEY_PORT;
import static com.bblvertx.SeConstants.ES_KEY_SCHEME;
import static com.bblvertx.SeConstants.ES_KEY_USERNAME;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Singleton pour le client elastic search.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@Singleton
public class ESClientImpl implements IESClient {
  @Inject
  private IPropertyReader prop;

  private IRestHighLevelClient client;

  /**
   * {@inheritDoc}
   */
  @Override
  public IRestHighLevelClient getClient() {
    try {
      if (null == client) {
        String host = prop.get(APP_CONFIG_FILE, ES_KEY_HOST);
        Integer port = prop.getInt(APP_CONFIG_FILE, ES_KEY_PORT);
        String scheme = prop.get(APP_CONFIG_FILE, ES_KEY_SCHEME);
        String path = prop.get(APP_CONFIG_FILE, ES_KEY_PATH);
        String username = prop.get(APP_CONFIG_FILE, ES_KEY_USERNAME);
        String password = prop.get(APP_CONFIG_FILE, ES_KEY_PASSWORD);

        RestClientBuilder builder = RestClient
            .builder(new HttpHost(Objects.requireNonNull(host, "elastic.host must not be null"),
                Objects.requireNonNull(port, "elastic.port must not be null"),
                Objects.requireNonNull(scheme, "elastic.scheme must not be null")));

        if (isNotBlank(username) && isNotBlank(password)) {
          CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

          credentialsProvider.setCredentials(AuthScope.ANY,
              new UsernamePasswordCredentials(username, password));

          builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
              .setDefaultCredentialsProvider(credentialsProvider));
        }

        if (isNotBlank(path)) {
          builder.setPathPrefix(path);
        }

        this.client = new RestHighLevelClientImpl(new RestHighLevelClient(builder));
      }
    } catch (IOException e) {
      throw new TechnicalException("Unexpected IOException : " + e.getMessage(), e);
    }

    return client;
  }
}
