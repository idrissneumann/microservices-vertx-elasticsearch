package com.bblvertx;

import static com.bblvertx.SeConstants.APP_CONFIG_FILE;
import static com.bblvertx.SeConstants.CLASS_ROUTE_PATTERN;
import static com.bblvertx.SeConstants.KEY_PORT;
import static com.bblvertx.SeConstants.KEY_TPL_ROUTE_CLASS;
import static com.bblvertx.SeConstants.KEY_TPL_ROUTE_CONTENT_TYPE;
import static com.bblvertx.SeConstants.KEY_TPL_ROUTE_URL;
import static com.bblvertx.SeConstants.ROUTE_CONFIG_FILE;

import com.bblvertx.ioc.SeBinder;
import com.bblvertx.utils.singleton.IPropertyReader;

import com.bblvertx.utils.singleton.IRouteContext;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;

import javax.inject.Inject;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * Server engine : main verticle node.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class SearchEngineServer extends AbstractVerticle {
  private static final Logger LOGGER = LogManager.getLogger(SearchEngineServer.class);

  private SeBinder seBinder;

  @Inject
  private IPropertyReader reader;

  @Inject
  private IRouteContext routeCtx;

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() throws Exception {
    LOGGER.info("Launching server...");
    System.setProperty("es.set.netty.runtime.available.processors", "false");

    seBinder = new SeBinder(vertx);
    Injector injector = Guice.createInjector(seBinder);
    injector.injectMembers(this);

    Integer port = reader.getInt(APP_CONFIG_FILE, KEY_PORT);

    final Router router = Router.router(vertx);

    // Automatically connect all routes
    Integer i = 1;
    while (true) {
      try {
        String routeClass = reader.get(ROUTE_CONFIG_FILE, String.format(KEY_TPL_ROUTE_CLASS, i));
        String routeUrl = reader.get(ROUTE_CONFIG_FILE, String.format(KEY_TPL_ROUTE_URL, i));
        String routeContentType =
            reader.get(ROUTE_CONFIG_FILE, String.format(KEY_TPL_ROUTE_CONTENT_TYPE, i));

        Class<?> clazz = Class.forName(String.format(CLASS_ROUTE_PATTERN, routeClass));
        Constructor<?> ctor =
            clazz.getConstructor(String.class, String.class, Router.class, IRouteContext.class);
        ctor.newInstance(new Object[] {routeUrl, routeContentType, router, routeCtx});
        i++;
      } catch (Exception e) {
        break;
      }
    }

    vertx.createHttpServer().requestHandler(router::handle).listen(port);
  }
}
