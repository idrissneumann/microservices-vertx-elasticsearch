package com.bblvertx.tests.verticle;

import static com.bblvertx.SeConstants.APP_CONFIG_FILE;
import static com.bblvertx.SeConstants.KEY_PORT;
import static com.bblvertx.SeConstants.KEY_TPL_ROUTE_URL;
import static com.bblvertx.SeConstants.RESPONSE_HTML_TEMPLATE;
import static com.bblvertx.SeConstants.ROUTE_CONFIG_FILE;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.bblvertx.SearchEngineServer;
import com.bblvertx.tests.AbstractTest;
import com.bblvertx.utils.FileUtils;
import com.bblvertx.utils.singleton.impl.PropertyReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

/**
 * Test sur le verticle SearchEngineServer.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@Ignore
@RunWith(VertxUnitRunner.class)
public class SearchEngineServerTest extends AbstractTest {
  private Vertx vertx;
  private PropertyReader propReader;

  /**
   * Pré-conditions : initialisation du verticle avec injection de dépendance via HK2 / CDI.
   * 
   * @param context
   */
  @Before
  public void before(final TestContext context) {
    vertx = Vertx.vertx();
    DeploymentOptions opt = new DeploymentOptions()
        .setConfig(new JsonObject(FileUtils.file2stringQuietly(getDataDir() + "config.json")));
    vertx.deployVerticle("java-hk2:" + SearchEngineServer.class.getName(), opt,
        context.asyncAssertSuccess());

    propReader = new PropertyReader();
  }

  /**
   * Posts-conditions
   * 
   * @param context
   */
  @After
  public void after(final TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  /**
   * Test nominal : on vérifie l'adhérence du serveur avec les routes configurées dans
   * routes.properties.
   * 
   * @param context
   */
  @Test
  public void testHelloWorld(final TestContext context) {
    final Async async = context.async();

    Integer port = propReader.getIntQuietly(APP_CONFIG_FILE, KEY_PORT);
    String host = "localhost";
    String requestURI = "/bblvertx/api/"
        + propReader.getQuietly(ROUTE_CONFIG_FILE, String.format(KEY_TPL_ROUTE_URL, "1"));
    String expectedResponse = String.format(RESPONSE_HTML_TEMPLATE, "Hello world");

    vertx.createHttpClient().getNow(port, host, requestURI, response -> {
      response.handler(body -> {
        context.assertTrue(body.toString().contains(expectedResponse));
        async.complete();
      });
    });
  }

  /**
   * Test nominal : on vérifie l'adhérence avec Elastic Search.
   * 
   * @param context
   */
  @Test
  public void testSearchElasticSearch(final TestContext context) {
    final Async async = context.async();

    Integer port = propReader.getIntQuietly(APP_CONFIG_FILE, KEY_PORT);
    String host = "localhost";
    String requestURI = "/bblvertx/api/"
        + propReader.getQuietly(ROUTE_CONFIG_FILE, String.format(KEY_TPL_ROUTE_URL, "3"))
        + "?startIndex=0&maxResults=100";

    vertx.createHttpClient().getNow(port, host, requestURI, response -> {
      response.handler(body -> {
        context.assertTrue(isNotEmpty(body.toString()));
        async.complete();
      });
    });
  }
}
