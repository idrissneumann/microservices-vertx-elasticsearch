package com.bblvertx.route;

import static com.bblvertx.SeConstants.PREFIX_URL;

import com.bblvertx.utils.singleton.impl.RouteContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.ext.web.Router;

/**
 * Abstract class for all routes (job).
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public abstract class AbstractRoute implements Route {
  private static final Logger LOGGER = LogManager.getLogger(AbstractRoute.class);

  protected RouteContext ctx;

  /**
   * Initializing route in the router of verticle.
   * 
   * @param url
   * @param contentType
   * @param router
   */
  public AbstractRoute(String url, String contentType, Router router, RouteContext ctx) {
    this.ctx = ctx;
    router.get(String.format(PREFIX_URL, url)).handler(req -> {
      LOGGER.info("Lauching route " + url);
      req.response().putHeader("content-type", contentType)
          .end(this.proceed(req.request(), req.response()));
    });
  }
}
