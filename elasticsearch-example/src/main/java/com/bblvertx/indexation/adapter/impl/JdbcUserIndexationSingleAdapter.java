package com.bblvertx.indexation.adapter.impl;

import static com.bblvertx.SeConstants.ES_INDEX_USER;
import static com.bblvertx.SeConstants.SELECT_SINGLE_USER;
import static com.bblvertx.SeConstants.SQL_CONFIG_FILE;
import static com.bblvertx.SeConstants.UPDATE_RSSEARCH;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.indexation.adapter.AbstractIndexingAdapter;
import com.bblvertx.persistence.mapper.JdbcUserMapper;
import com.bblvertx.pojo.vo.UserVO;
import com.bblvertx.utils.singleton.IRouteContext;
import com.bblvertx.utils.singleton.SeDataSource;

import java.io.IOException;

/**
 * Adapter for user.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class JdbcUserIndexationSingleAdapter extends AbstractIndexingAdapter<UserVO> {
  /**
   * Constructor.
   * 
   * @param ctx
   */
  public JdbcUserIndexationSingleAdapter(IRouteContext ctx) {
    super(ctx);
    rowMapper = new JdbcUserMapper();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDbUpdateRsSearch() {
    try {
      return ctx.getProp().get(SQL_CONFIG_FILE, UPDATE_RSSEARCH);
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
      return ctx.getProp().get(SQL_CONFIG_FILE, SELECT_SINGLE_USER);
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
    return getRouteContext().getJdbcDataSource();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getOrderLimit() {
    return 2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getOrderOffset() {
    return 3;
  }
}
