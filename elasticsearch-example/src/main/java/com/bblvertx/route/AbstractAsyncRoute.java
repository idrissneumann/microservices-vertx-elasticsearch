package com.bblvertx.route;

import static com.bblvertx.SeConstants.PREFIX_URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bblvertx.utils.singleton.impl.RouteContext;

import io.vertx.ext.web.Router;

/**
 * Abstract async route.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public abstract class AbstractAsyncRoute implements AsyncRoute {
  private static final Logger LOGGER = LogManager.getLogger(AbstractAsyncRoute.class);

  protected RouteContext ctx;

  /**
   * Init the async route in the router of verticle.
   * 
   * @param url
   * @param contentType
   * @param router
   */
  public AbstractAsyncRoute(String url, String contentType, Router router, RouteContext ctx) {
    this.ctx = ctx;
    router.get(String.format(PREFIX_URL, url)).handler(req -> {
      LOGGER.info("Lauching route " + url);
      req.response().putHeader("content-type", contentType)
          .end(this.proceed(req.request(), req.response()));

      new Thread(() -> {
        LOGGER.info("Lauching route " + url + " async action...");
        proceedAsync(req.request(), req.response());
      }).start();
    });
  }
}
