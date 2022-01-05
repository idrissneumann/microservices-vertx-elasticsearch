package com.bblvertx.route;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

/**
 * Asynchronous route interface.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public interface AsyncRoute extends Route {
  /**
   * Launching async action.
   * 
   * @param request
   * @param response
   */
  void proceedAsync(HttpServerRequest request, HttpServerResponse response);
}
