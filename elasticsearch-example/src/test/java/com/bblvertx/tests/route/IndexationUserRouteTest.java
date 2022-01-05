package com.bblvertx.tests.route;

import static com.bblvertx.SeConstants.APP_CONFIG_FILE;
import static com.bblvertx.SeConstants.DB_KEY_PAGINATION;
import static com.bblvertx.SeConstants.RESPONSE_HTML_TEMPLATE;
import static com.bblvertx.SeConstants.SQL_CONFIG_FILE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bblvertx.persistence.QueryParam;
import com.bblvertx.persistence.mapper.JdbcUserMapper;
import com.bblvertx.pojo.vo.UserVO;
import com.bblvertx.route.impl.JdbcIndexingUserRoute;
import com.bblvertx.tests.AbstractTest;
import com.bblvertx.utils.singleton.impl.ESClient;
import com.bblvertx.utils.singleton.impl.JdbcDataSource;
import com.bblvertx.utils.singleton.impl.PropertyReader;
import com.bblvertx.utils.singleton.impl.RouteContext;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * Tests sur la route IndexationChatUserRoute.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class IndexationUserRouteTest extends AbstractTest {
  private JdbcIndexingUserRoute route;

  @Mock
  private Route routeMock;

  @Mock
  private RouteContext ctx;

  @Mock
  private JdbcDataSource dataSource;

  @Mock
  private PropertyReader prop;

  @Mock
  private ESClient esClient;

  @Mock
  private Router router;

  @Mock
  private TransportClient tClient;

  @Mock
  private IndexRequestBuilder indexRequestBuilder;

  @Mock
  private HttpServerRequest requestMock;

  @Mock
  private HttpServerResponse responseMock;

  @SuppressWarnings("rawtypes")
  @Mock
  private ListenableActionFuture listenable;

  private List<UserVO> lstResults = null;
  private UserVO vo = null;

  /**
   * Initialisation des mocks et injection de dépendance.
   */
  @SuppressWarnings("unchecked")
  @Before
  public final void initIocAndData() {
    when(ctx.getJdbcDataSource()).thenReturn(dataSource);
    when(ctx.getEsClient()).thenReturn(esClient);
    when(ctx.getProp()).thenReturn(prop);
    when(router.get(anyString())).thenReturn(routeMock);
    when(esClient.getClient()).thenReturn(tClient);
    when(tClient.prepareIndex(anyString(), anyString(), anyString()))
        .thenReturn(indexRequestBuilder);
    when(indexRequestBuilder.setSource(anyString())).thenReturn(indexRequestBuilder);
    when(indexRequestBuilder.execute()).thenReturn(listenable);

    try {
      when(prop.get(APP_CONFIG_FILE, DB_KEY_PAGINATION)).thenReturn("1");
      when(prop.get(eq(SQL_CONFIG_FILE), anyString())).thenReturn("select blabla");
    } catch (IOException e) {
      failWithException(e);
    }

    lstResults = new ArrayList<UserVO>();
    vo = new UserVO();
    lstResults.add(vo);

    when(dataSource.execute(anyString(), anyListOf(QueryParam.class), any(JdbcUserMapper.class)))
        .thenReturn(lstResults, new ArrayList<UserVO>());

    route = new JdbcIndexingUserRoute("/route", "/text/html", router, ctx);
  }

  /**
   * Test de la méthode proceed.
   */
  @Test
  public final void testProceed() {
    String response = route.proceed(requestMock, responseMock);
    assertEquals(String.format(RESPONSE_HTML_TEMPLATE, "Indexation en cours..."), response);
  }

  /**
   * Test de la méthode proceedAsync
   */
  @Test
  public final void testProceedAsync() {
    route.proceedAsync(requestMock, responseMock);

    // assertions sur les mocks
    verify(dataSource, times(3)).execute(anyString(), anyListOf(QueryParam.class), any());

    verify(tClient).prepareIndex(anyString(), anyString(), anyString());
  }
}
