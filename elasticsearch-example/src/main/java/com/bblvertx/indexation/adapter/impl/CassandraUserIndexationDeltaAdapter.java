package com.bblvertx.indexation.adapter.impl;

import static com.bblvertx.SeConstants.CQL_CONFIG_FILE;
import static com.bblvertx.SeConstants.DELETE_RSSEARCH;
import static com.bblvertx.SeConstants.ES_INDEX_USER;
import static com.bblvertx.SeConstants.SELECT_USER;
import static com.bblvertx.SeConstants.SELECT_USER_FLAG;
import static com.bblvertx.SeConstants.UPDATE_RSSEARCH;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.indexation.adapter.AbstractIndexingDeltaAdapter;
import com.bblvertx.persistence.mapper.CassandraUserIdMapper;
import com.bblvertx.persistence.mapper.CassandraUserMapper;
import com.bblvertx.pojo.vo.UserVO;
import com.bblvertx.utils.singleton.SeDataSource;
import com.bblvertx.utils.singleton.impl.RouteContext;

import java.io.IOException;

/**
 * Adapter for user in cassandra.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class CassandraUserIndexationDeltaAdapter extends AbstractIndexingDeltaAdapter<UserVO> {
  /**
   * Constructor.
   * 
   * @param ctx
   */
  public CassandraUserIndexationDeltaAdapter(RouteContext ctx) {
    super(ctx);
    rowMapper = new CassandraUserMapper();
    rowMapperId = new CassandraUserIdMapper();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDbSelectFlagIdx() {
    try {
      return ctx.getProp().get(CQL_CONFIG_FILE, SELECT_USER_FLAG);
    } catch (IOException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDbDeleteRsSearch() {
    try {
      return ctx.getProp().get(CQL_CONFIG_FILE, DELETE_RSSEARCH);
    } catch (IOException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDbUpdateRsSearch() {
    try {
      return ctx.getProp().get(CQL_CONFIG_FILE, UPDATE_RSSEARCH);
    } catch (IOException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDbSelectValueObject() {
    try {
      return ctx.getProp().get(CQL_CONFIG_FILE, SELECT_USER);
    } catch (IOException e) {
      throw new TechnicalException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIndexName() {
    return ES_INDEX_USER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIndexType() {
    return UserVO.class.getSimpleName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getValueObjectClass() {
    return UserVO.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId(UserVO valueObject) {
    return valueObject.getId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SeDataSource getDataSource() {
    return getRouteContext().getCassandraDataSource();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getOrderLimit() {
    return 3;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getOrderOffset() {
    return 2;
  }
}
