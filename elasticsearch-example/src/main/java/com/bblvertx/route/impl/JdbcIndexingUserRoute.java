package com.bblvertx.route.impl;

import com.bblvertx.indexation.adapter.impl.JdbcUserIndexationDeltaAdapter;
import com.bblvertx.pojo.vo.UserVO;
import com.bblvertx.route.AbstractIndexingDeltaRoute;
import com.bblvertx.utils.singleton.impl.RouteContext;

import io.vertx.ext.web.Router;

/**
 * Route to index all users of jdbc connection.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class JdbcIndexingUserRoute extends AbstractIndexingDeltaRoute<UserVO> {
  /**
   * Constructor.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public JdbcIndexingUserRoute(String url, String contentType, Router router, RouteContext ctx) {
    super(url, contentType, router, ctx);
    this.adapter = new JdbcUserIndexationDeltaAdapter(ctx);
  }
}
