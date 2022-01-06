package com.bblvertx.route;

import com.bblvertx.utils.singleton.IRouteContext;
import io.vertx.ext.web.Router;

/**
 * Abstract indexing route.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public abstract class AbstractIndexingRoute extends AbstractAsyncRoute {
  public final static String[] CODE_LANGUAGES = new String[] {"fr", "en", "ar"};

  /**
   * Constructor.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public AbstractIndexingRoute(String url, String contentType, Router router, IRouteContext ctx) {
    super(url, contentType, router, ctx);
  }
}
