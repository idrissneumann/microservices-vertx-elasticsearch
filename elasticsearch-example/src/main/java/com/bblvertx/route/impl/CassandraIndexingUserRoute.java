package com.bblvertx.route.impl;

import com.bblvertx.indexation.adapter.impl.CassandraUserIndexationDeltaAdapter;
import com.bblvertx.pojo.vo.UserVO;
import com.bblvertx.route.AbstractIndexingDeltaRoute;
import com.bblvertx.utils.singleton.impl.RouteContext;

import io.vertx.ext.web.Router;

/**
 * Route to index all users of cassandra connection.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class CassandraIndexingUserRoute extends AbstractIndexingDeltaRoute<UserVO> {
  /**
   * Constructor.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public CassandraIndexingUserRoute(String url,
      String contentType,
      Router router,
      RouteContext ctx) {
    super(url, contentType, router, ctx);
    this.adapter = new CassandraUserIndexationDeltaAdapter(ctx);
  }
}
