package com.bblvertx.route.impl;

import com.bblvertx.indexation.adapter.impl.JdbcUserIndexationSingleAdapter;
import com.bblvertx.pojo.vo.UserVO;
import com.bblvertx.route.AbstractIndexingSingleRoute;

import com.bblvertx.utils.singleton.IRouteContext;
import io.vertx.ext.web.Router;

/**
 * Route to index a single user of jdbc database connection.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class JdbcIndexingSingleUserRoute extends AbstractIndexingSingleRoute<UserVO> {
  /**
   * Constructor.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public JdbcIndexingSingleUserRoute(String url,
      String contentType,
      Router router,
      IRouteContext ctx) {
    super(url, contentType, router, ctx);
    this.adapter = new JdbcUserIndexationSingleAdapter(ctx);
  }
}
