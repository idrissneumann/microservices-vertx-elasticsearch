package com.bblvertx.route;

import static com.bblvertx.SeConstants.APP_CONFIG_FILE;
import static com.bblvertx.SeConstants.DB_KEY_PAGINATION;
import static com.bblvertx.SeConstants.RESPONSE_HTML_TEMPLATE;
import static com.bblvertx.SeConstants.RS_TO_DELETE;
import static com.bblvertx.SeConstants.RS_TO_STAY;
import static com.bblvertx.SeConstants.RS_TO_UPDATE;
import static com.bblvertx.utils.JSONUtils.objectTojsonQuietly;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.indexation.adapter.IndexingDeltaAdapter;
import com.bblvertx.persistence.QueryParam;
import com.bblvertx.persistence.QueryParamBuilder;
import com.bblvertx.persistence.RowMapper;
import com.bblvertx.utils.singleton.impl.RouteContext;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Generic impl of delta indexing route.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 * @param <T> value object's type
 */
public abstract class AbstractIndexingDeltaRoute<T extends Serializable>
    extends AbstractIndexingRoute {

  /**
   * Adapter for the specificity of each object which will be indexed.
   */
  protected IndexingDeltaAdapter<T> adapter;

  /**
   * Constructor.
   * 
   * @param url
   * @param contentType
   * @param router
   * @param ctx
   */
  public AbstractIndexingDeltaRoute(String url,
      String contentType,
      Router router,
      RouteContext ctx) {
    super(url, contentType, router, ctx);
  }

  /**
   * Deleting old data (rs search = -1).
   */
  private void deleteOldData() {
    try {
      String sql = adapter.getDbSelectFlagIdx();
      String sqlDelete = adapter.getDbDeleteRsSearch();
      Integer limit = Integer.valueOf(ctx.getProp().get(APP_CONFIG_FILE, DB_KEY_PAGINATION));
      Integer offset = 0;
      List<Serializable> lstResults = null;

      QueryParam pRsSearch = new QueryParamBuilder() //
          .add("order", 1, Integer.class) //
          .add("value", RS_TO_DELETE, Object.class) //
          .add("clazz", Integer.class, Class.class) //
          .getParam();

      QueryParam pLimit = new QueryParamBuilder() //
          .add("order", adapter.getOrderLimit(), Integer.class) //
          .add("value", limit, Object.class) //
          .add("clazz", Integer.class, Class.class) //
          .getParam();

      QueryParam pOffset = null;

      StringJoiner idElems = new StringJoiner(",");
      do {
        pOffset = new QueryParamBuilder() //
            .add("order", adapter.getOrderOffset(), Integer.class) //
            .add("value", offset, Object.class) //
            .add("clazz", Integer.class, Class.class) //
            .getParam();

        lstResults = adapter.getDataSource().execute(sql,
            pRsSearch.asList(pLimit.asList(pOffset.asList())), adapter.getIdMapper());

        if (isNotEmpty(lstResults)) {
          for (Serializable idElem : lstResults) {
            ctx.getEsClient() //
                .getClient() //
                .prepareDelete(adapter.getIndexName(), adapter.getIndexType(),
                    String.valueOf(idElem)) //
                .execute().actionGet();

            idElems.add(idElem.toString());
          }
        }

        offset += limit;
      } while (isNotEmpty(lstResults));

      if (idElems.length() > 0) {
        adapter.getDataSource().executeUpdate(String.format(sqlDelete, idElems.toString()));
      }
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * Indexing new data (rs search = 1)
   */
  private void indexingNewData() {
    try {
      String sql = adapter.getDbSelectValueObject();
      String sqlUpdate = adapter.getDbUpdateRsSearch();
      Integer limit = Integer.valueOf(ctx.getProp().get(APP_CONFIG_FILE, DB_KEY_PAGINATION));
      Integer offset = 0;
      List<T> lstResults = null;
      RowMapper<T> mapper = adapter.getMapper();

      QueryParam pRsSearch = new QueryParamBuilder() //
          .add("order", 1, Integer.class) //
          .add("value", RS_TO_UPDATE, Object.class) //
          .add("clazz", Integer.class, Class.class) //
          .getParam();

      QueryParam pLimit = new QueryParamBuilder() //
          .add("order", adapter.getOrderLimit(), Integer.class) //
          .add("value", limit, Object.class) //
          .add("clazz", Integer.class, Class.class) //
          .getParam();

      QueryParam pOffset = null;

      StringJoiner idElems = new StringJoiner(",");
      do {
        pOffset = new QueryParamBuilder() //
            .add("order", adapter.getOrderOffset(), Integer.class) //
            .add("value", offset, Object.class) //
            .add("clazz", Integer.class, Class.class) //
            .getParam();

        lstResults = adapter.getDataSource().execute(sql,
            pRsSearch.asList(pLimit.asList(pOffset.asList())), mapper);

        if (isNotEmpty(lstResults)) {
          for (T object : lstResults) {
            ctx.getEsClient() //
                .getClient() //
                .prepareIndex(adapter.getIndexName(), adapter.getIndexType(), adapter.getId(object)) //
                .setSource(objectTojsonQuietly(object, adapter.getValueObjectClass())) //
                .execute() //
                .actionGet();

            idElems.add(adapter.getId(object));
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
   * {@inheritDoc}
   */
  @Override
  public void proceedAsync(HttpServerRequest request, HttpServerResponse response) {
    indexingNewData();
    deleteOldData();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String proceed(HttpServerRequest request, HttpServerResponse response) {
    return String.format(RESPONSE_HTML_TEMPLATE, "Indexation en cours...");
  }

  /**
   * @return the adapter
   */
  public IndexingDeltaAdapter<T> getAdapter() {
    return adapter;
  }

  /**
   * @param adapter the adapter to set
   */
  public void setAdapter(IndexingDeltaAdapter<T> adapter) {
    this.adapter = adapter;
  }
}
