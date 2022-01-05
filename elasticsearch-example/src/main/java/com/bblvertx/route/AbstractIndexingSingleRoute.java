package com.bblvertx.route;

import static com.bblvertx.SeConstants.APP_CONFIG_FILE;
import static com.bblvertx.SeConstants.DB_KEY_PAGINATION;
import static com.bblvertx.SeConstants.MSG_BAD_REQUEST;
import static com.bblvertx.SeConstants.RS_TO_STAY;
import static com.bblvertx.SeConstants.RS_TO_UPDATE;
import static com.bblvertx.utils.CommonUtils.assertParamNotEmpty;
import static com.bblvertx.utils.JSONUtils.objectTojsonQuietly;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.indexation.adapter.IndexingAdapter;
import com.bblvertx.persistence.QueryParam;
import com.bblvertx.persistence.QueryParamBuilder;
import com.bblvertx.persistence.RowMapper;
import com.bblvertx.utils.JSONUtils;
import com.bblvertx.utils.singleton.impl.RouteContext;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Generic impl of single mode indexing route.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 * 
 * @param <T> value object's type
 *
 */
public abstract class AbstractIndexingSingleRoute<T extends Serializable>
    extends AbstractIndexingRoute {
  /**
   * Adapter pour les spécificités de chaque indexation.
   */
  protected IndexingAdapter<T> adapter;

  /**
   * Constructor.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public AbstractIndexingSingleRoute(String url,
      String contentType,
      Router router,
      RouteContext ctx) {
    super(url, contentType, router, ctx);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void proceedAsync(HttpServerRequest request, HttpServerResponse response) {
    String strId = request.getParam("id");
    assertParamNotEmpty(strId, String.format(MSG_BAD_REQUEST, "id"));
    indexingNewData(strId);
  }

  /**
   * Indexing new data (id = ... and rs search = 1)
   */
  private void indexingNewData(String id) {
    try {
      String sql = adapter.getDbSelectValueObject();
      String sqlUpdate = adapter.getDbUpdateRsSearch();
      Integer limit = Integer.valueOf(ctx.getProp().get(APP_CONFIG_FILE, DB_KEY_PAGINATION));
      Integer offset = 0;
      List<T> lstResults = null;
      RowMapper<T> mapper = adapter.getMapper();

      QueryParam pId = generateQueryParamWithId(id);

      QueryParam pRsSearch = new QueryParamBuilder() //
          .add("order", 2, Integer.class) //
          .add("value", RS_TO_UPDATE, Object.class) //
          .add("clazz", Integer.class, Class.class) //
          .getParam();

      QueryParam pLimit = new QueryParamBuilder() //
          .add("order", adapter.getOrderLimit() + 1, Integer.class) //
          .add("value", limit, Object.class) //
          .add("clazz", Integer.class, Class.class) //
          .getParam();

      QueryParam pOffset = null;
      StringJoiner idElems = new StringJoiner(",");

      do {
        pOffset = new QueryParamBuilder() //
            .add("order", adapter.getOrderOffset() + 1, Integer.class) //
            .add("value", offset, Object.class) //
            .add("clazz", Integer.class, Class.class) //
            .getParam();

        lstResults = adapter.getDataSource().execute(sql,
            pId.asList(pRsSearch.asList(pLimit.asList(pOffset.asList()))), mapper);

        if (isNotEmpty(lstResults)) {
          for (T elem : lstResults) {
            ctx.getEsClient() //
                .getClient() //
                .prepareIndex(adapter.getIndexName(), adapter.getIndexType(), adapter.getId(elem)) //
                .setSource(objectTojsonQuietly(elem, adapter.getValueObjectClass())) //
                .execute() //
                .actionGet();

            idElems.add(adapter.getId(elem));
          }
        }

        offset += limit;
      } while (isNotEmpty(lstResults));

      if (idElems.length() > 0) {
        QueryParam pRsSearch2 = new QueryParamBuilder() //
            .add("order", 1, Integer.class) //
            .add("value", RS_TO_STAY, Object.class) //
            .add("clazz", Integer.class, Class.class) //
            .getParam();

        adapter.getDataSource().executeUpdate(String.format(sqlUpdate, idElems.toString()),
            pRsSearch2.asList());
      }
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * Transform the id in a query param.
   * 
   * @param id
   * @return QueryParam
   */
  private QueryParam generateQueryParamWithId(String id) {
    if (StringUtils.isNumeric(id)) {
      return new QueryParamBuilder() //
          .add("order", 1, Integer.class) //
          .add("value", Integer.valueOf(id), Object.class) //
          .add("clazz", Object.class, Class.class) //
          .getParam();
    }

    try {
      return new QueryParamBuilder() //
          .add("order", 1, Integer.class) //
          .add("value", UUID.fromString(id), Object.class) //
          .add("clazz", UUID.class, Class.class) //
          .getParam();
    } catch (IllegalArgumentException e) {
      return new QueryParamBuilder() //
          .add("order", 1, Integer.class) //
          .add("value", id, Object.class) //
          .add("clazz", String.class, Class.class) //
          .getParam();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String proceed(HttpServerRequest request, HttpServerResponse response) {
    return JSONUtils.objectTojsonQuietly("Indexation en cours...", String.class);
  }

  /**
   * @return the adapter
   */
  public IndexingAdapter<T> getAdapter() {
    return adapter;
  }

  /**
   * @param adapter the adapter to set
   */
  public void setAdapter(IndexingAdapter<T> adapter) {
    this.adapter = adapter;
  }
}
