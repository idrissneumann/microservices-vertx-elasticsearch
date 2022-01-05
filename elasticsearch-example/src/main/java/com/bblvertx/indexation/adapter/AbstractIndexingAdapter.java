package com.bblvertx.indexation.adapter;

import java.io.Serializable;

import com.bblvertx.persistence.RowMapper;
import com.bblvertx.utils.singleton.impl.RouteContext;

/**
 * Indexing abstract adapter.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 * @param <T>
 */
public abstract class AbstractIndexingAdapter<T extends Serializable>
    implements IndexingAdapter<T> {
  protected RouteContext ctx;

  protected RowMapper<T> rowMapper;

  /**
   * Constructor.
   * 
   * @param ctx
   */
  public AbstractIndexingAdapter(RouteContext ctx) {
    this.ctx = ctx;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RouteContext getRouteContext() {
    return ctx;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RowMapper<T> getMapper() {
    return rowMapper;
  }
}
