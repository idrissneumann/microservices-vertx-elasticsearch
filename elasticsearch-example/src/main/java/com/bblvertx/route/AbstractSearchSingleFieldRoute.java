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

import com.bblvertx.utils.singleton.IRouteContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import org.elasticsearch.search.builder.SearchSourceBuilder;

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
      IRouteContext ctx) {
    super(url, contentType, router, ctx);
  }

  public String proceed(HttpServerRequest request, HttpServerResponse response, String indexName, String fieldsName) {
    return proceed(request, response, indexName, Arrays.asList(fieldsName));
  }

  /**
   * Search with pagination.
   * 
   * @param request
   * @param response
   * @param indexName
   * @param fieldsNames
   * @return String : réponse sérialisée en JSON
   */
  private String proceed(HttpServerRequest request, HttpServerResponse response, String indexName, List<String> fieldsNames) {

    // Checking parameters
    Integer startIndex = assertParamNumeric(request.getParam("startIndex"),
        String.format(MSG_BAD_REQUEST_MUST_BE_NUMERIC, "startIndex"));
    Integer maxResults = assertParamNumeric(request.getParam("maxResults"),
        String.format(MSG_BAD_REQUEST_MUST_BE_NUMERIC, "maxResults"));

    assertParamNotEmpty(startIndex, String.format(MSG_BAD_REQUEST, "startIndex"));
    assertParamNotEmpty(maxResults, String.format(MSG_BAD_REQUEST, "maxResults"));

    List<String> searchCriteres = request.params().getAll("term");

    BoolQueryBuilder qb = boolQuery();
    qb.minimumShouldMatch(1);

    SearchResult<String> result =
        initSearchResult(Long.valueOf(startIndex), Long.valueOf(maxResults));
    if (isEmpty(searchCriteres)) {
      return objectTojsonQuietly(result, SearchResult.class);
    }

    for (String c : searchCriteres) {
      for (String fieldName : fieldsNames) {
        qb.should(regexpQuery(fieldName, c.toLowerCase() + ".*"));
      }
    }

    SearchResponse r = null;
    try {
      r = ctx.getEsClient().getClient().search(new SearchRequest(indexName).source(new SearchSourceBuilder().query(qb).fetchSource(fieldsNames.toArray(new String[fieldsNames.size()]), null).from(startIndex * maxResults).size(maxResults)), RequestOptions.DEFAULT);
    } catch (Exception e) {
      LOGGER.warn(e);
      return objectTojsonQuietly(result, SearchResult.class);
    }

    result.setTotalResults(r.getHits().getTotalHits().value);

    List<String> lstResult = new ArrayList<>();

    if (r.getHits().getHits().length > 0) {
      Arrays.stream(r.getHits().getHits())
              .forEach(hit -> processHit(fieldsNames, searchCriteres, lstResult, hit));
    }

    result.setResults(lstResult);

    return objectTojsonQuietly(result, SearchResult.class);
  }

  /**
   * Processing of hits.
   *
   * @param fieldsNames
   * @param searchCriteres
   * @param lstResult
   * @param hit
   */
  private void processHit(List<String> fieldsNames,
                          List<String> searchCriteres,
                          List<String> lstResult,
                          SearchHit hit) {
    List<Object> values =
            fieldsNames.stream().map(n -> hit.getSourceAsMap().get(n)).collect(Collectors.toList());

    if (isEmpty(values)) {
      return;
    }

    for (Object v : values) {
      if (null == v) {
        continue;
      }

      String value = String.valueOf(v).toLowerCase();
      if (value.length() >= 3) {
        value = value.substring(1, value.length() - 1);
      }

      for (String c : searchCriteres) {
        if (value.contains(c.toLowerCase()) && !lstResult.contains(value)) {
          lstResult.add(value);
          break;
        }
      }
    }
  }
}
