package com.bblvertx.route.impl;

import static com.bblvertx.SeConstants.ES_INDEX_USER;
import static com.bblvertx.SeConstants.MSG_BAD_REQUEST;
import static com.bblvertx.SeConstants.MSG_BAD_REQUEST_MUST_BE_CALENDAR;
import static com.bblvertx.SeConstants.MSG_BAD_REQUEST_MUST_BE_NUMERIC;
import static com.bblvertx.utils.CommonUtils.assertParamNotCalendar;
import static com.bblvertx.utils.CommonUtils.assertParamNotEmpty;
import static com.bblvertx.utils.CommonUtils.assertParamNumeric;
import static com.bblvertx.utils.CommonUtils.initSearchResult;
import static com.bblvertx.utils.JSONUtils.objectTojsonQuietly;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.pojo.SearchResult;
import com.bblvertx.pojo.vo.UserVO;
import com.bblvertx.route.AbstractSearchIndexRoute;
import com.bblvertx.utils.singleton.impl.RouteContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Route to search user by criterias.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class SearchUserRoute extends AbstractSearchIndexRoute {
  private static final Logger LOGGER = LogManager.getLogger(SearchUserRoute.class);

  /**
   * Constructeur.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public SearchUserRoute(String url, String contentType, Router router, RouteContext ctx) {
    super(url, contentType, router, ctx);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String proceed(HttpServerRequest request, HttpServerResponse response) {
    // Contrôles des paramètres
    Integer startIndex = assertParamNumeric(request.getParam("startIndex"),
        String.format(MSG_BAD_REQUEST_MUST_BE_NUMERIC, "startIndex"));
    Integer maxResults = assertParamNumeric(request.getParam("maxResults"),
        String.format(MSG_BAD_REQUEST_MUST_BE_NUMERIC, "maxResults"));

    assertParamNotEmpty(startIndex, String.format(MSG_BAD_REQUEST, "startIndex"));
    assertParamNotEmpty(maxResults, String.format(MSG_BAD_REQUEST, "maxResults"));

    List<String> searchCriteres = request.params().getAll("term");
    String nom = request.getParam("name");
    String prenom = request.getParam("firstname");
    String email = request.getParam("email");
    String id = request.getParam("id");
    String lng = request.getParam("lng");
    Calendar dateConnect = assertParamNotCalendar(request.getParam("dateConnect"),
        String.format(MSG_BAD_REQUEST_MUST_BE_CALENDAR, "dateConnect"));

    BoolQueryBuilder qb = boolQuery();
    qb.minimumNumberShouldMatch(1);

    if (isNotEmpty(nom)) {
      qb.must(match("name", nom));
    }

    if (isNotEmpty(prenom)) {
      qb.must(match("firstname", prenom));
    }

    if (isNotEmpty(email)) {
      qb.must(match("email", email));
    }

    if (null != dateConnect) {
      qb.must(termQuery("dateConnect", dateConnect.getTimeInMillis()));
    }

    if (null != lng) {
      qb.must(matchQuery("lng", lng));
    }

    if (null != id) {
      qb.must(match("id", id));
    }

    if (isNotEmpty(searchCriteres)) {
      for (String c : searchCriteres) {
        qb.should(regexpQuery("name", c.toLowerCase() + ".*"));
        qb.should(regexpQuery("firstname", c.toLowerCase() + ".*"));
        qb.should(regexpQuery("email", c.toLowerCase() + ".*"));
        qb.should(matchQuery("id", c.toLowerCase()));
      }
    }

    SearchResponse r = null;
    SearchResult<UserVO> result =
        initSearchResult(Long.valueOf(startIndex), Long.valueOf(maxResults));
    try {
      r = ctx.getEsClient() //
          .getClient() //
          .prepareSearch(ES_INDEX_USER) //
          .setQuery(qb) //
          .setFrom(startIndex * maxResults) //
          .setSize(maxResults).execute() //
          .actionGet();
    } catch (Exception e) {
      LOGGER.warn(e);
      return objectTojsonQuietly(result, SearchResult.class);
    }

    result.setTotalResults(r.getHits().getTotalHits());

    List<UserVO> lstResult = new ArrayList<UserVO>();

    // Unmarshalling
    ObjectMapper mapper = new ObjectMapper();
    if (r.getHits().getHits().length > 0) {
      for (SearchHit hit : r.getHits().getHits()) {
        try {
          UserVO vo = mapper.readValue(hit.getSourceAsString(), new TypeReference<UserVO>() {});

          lstResult.add(vo);
        } catch (IOException e) {
          throw new TechnicalException(e);
        }
      }
    }

    result.setResults(lstResult);

    return objectTojsonQuietly(result, SearchResult.class);
  }
}
