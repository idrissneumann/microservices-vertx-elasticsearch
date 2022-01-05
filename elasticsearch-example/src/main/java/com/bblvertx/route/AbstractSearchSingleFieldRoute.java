package com.bblvertx.route;

import static com.bblvertx.SeConstants.MSG_BAD_REQUEST;
import static com.bblvertx.SeConstants.MSG_BAD_REQUEST_MUST_BE_NUMERIC;
import static com.bblvertx.utils.CommonUtils.assertParamNotEmpty;
import static com.bblvertx.utils.CommonUtils.assertParamNumeric;
import static com.bblvertx.utils.CommonUtils.initSearchResult;
import static com.bblvertx.utils.JSONUtils.objectTojsonQuietly;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;

import com.bblvertx.pojo.SearchResult;
import com.bblvertx.utils.singleton.impl.RouteContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Abstract searching single field route.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public abstract class AbstractSearchSingleFieldRoute extends AbstractSearchIndexRoute {
  private static final Logger LOGGER = LogManager.getLogger(AbstractSearchSingleFieldRoute.class);

  /**
   * Constructor.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public AbstractSearchSingleFieldRoute(String url,
      String contentType,
      Router router,
      RouteContext ctx) {
    super(url, contentType, router, ctx);
  }

  /**
   * Search with pagination.
   * 
   * @param request
   * @param response
   * @param indexName
   * @param fieldName
   * @return String : réponse sérialisée en JSON
   */
  public String proceed(HttpServerRequest request, HttpServerResponse response, String indexName,
      String fieldName) {

    // Checking parameters
    Integer startIndex = assertParamNumeric(request.getParam("startIndex"),
        String.format(MSG_BAD_REQUEST_MUST_BE_NUMERIC, "startIndex"));
    Integer maxResults = assertParamNumeric(request.getParam("maxResults"),
        String.format(MSG_BAD_REQUEST_MUST_BE_NUMERIC, "maxResults"));

    assertParamNotEmpty(startIndex, String.format(MSG_BAD_REQUEST, "startIndex"));
    assertParamNotEmpty(maxResults, String.format(MSG_BAD_REQUEST, "maxResults"));

    List<String> searchCriteres = request.params().getAll("term");

    BoolQueryBuilder qb = boolQuery();
    qb.minimumNumberShouldMatch(1);

    SearchResult<String> result =
        initSearchResult(Long.valueOf(startIndex), Long.valueOf(maxResults));
    if (isEmpty(searchCriteres)) {
      return objectTojsonQuietly(result, SearchResult.class);
    }

    for (String c : searchCriteres) {
      qb.should(regexpQuery(fieldName, c.toLowerCase() + ".*"));
    }

    SearchResponse r = null;
    try {
      r = ctx.getEsClient() //
          .getClient() //
          .prepareSearch(indexName) //
          .setQuery(qb) //
          .addFields(fieldName) //
          .setFrom(startIndex * maxResults) //
          .setSize(maxResults).execute() //
          .actionGet();
    } catch (Exception e) {
      LOGGER.warn(e);
      return objectTojsonQuietly(result, SearchResult.class);
    }

    result.setTotalResults(r.getHits().getTotalHits());

    List<String> lstResult = new ArrayList<String>();

    if (r.getHits().getHits().length > 0) {
      for (SearchHit hit : r.getHits().getHits()) {
        List<Object> values = hit.getFields().get(fieldName).getValues();
        if (isEmpty(values)) {
          continue;
        }

        for (Object v : values) {
          if (null == v) {
            continue;
          }

          String value = String.valueOf(v).toLowerCase();
          for (String c : searchCriteres) {
            if (value.contains(c.toLowerCase()) && !lstResult.contains(value)) {
              lstResult.add(value);
              break;
            }
          }
        }
      }
    }

    result.setResults(lstResult);

    return objectTojsonQuietly(result, SearchResult.class);
  }
}
