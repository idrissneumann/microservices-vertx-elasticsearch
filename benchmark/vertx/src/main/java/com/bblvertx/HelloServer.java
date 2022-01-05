package com.bblvertx;

import io.vertx.core.AbstractVerticle;

/**
 * Simple vertilce.
 * 
 * @author ineumann
 *
 */
public class HelloServer extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    vertx.createHttpServer()
        .requestHandler(req -> req.response().putHeader("content-type", "text/html")
            .end("<html><body><h1>Great BBL !</h1></body></html>\n"))
        .listen(8111);
  }
}
