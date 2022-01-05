package com.bblvertx.route.impl;

import static com.bblvertx.SeConstants.ES_INDEX_USER;

import com.bblvertx.route.AbstractSearchSingleFieldRoute;
import com.bblvertx.utils.singleton.impl.RouteContext;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class AutocompleteSkillRoute extends AbstractSearchSingleFieldRoute {
  private static final String SKILL_FIELD = "skill";

  /**
   * Default constructor.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public AutocompleteSkillRoute(String url, String contentType, Router router, RouteContext ctx) {
    super(url, contentType, router, ctx);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String proceed(HttpServerRequest request, HttpServerResponse response) {
    return super.proceed(request, response, ES_INDEX_USER, SKILL_FIELD);
  }

}
