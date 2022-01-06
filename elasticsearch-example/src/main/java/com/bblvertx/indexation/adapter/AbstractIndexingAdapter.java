package com.bblvertx.indexation.adapter;

import java.io.Serializable;

import com.bblvertx.persistence.RowMapper;
import com.bblvertx.utils.singleton.IRouteContext;

/**
 * Indexing abstract adapter.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 * @param <T>
 */
public abstract class AbstractIndexingAdapter<T extends Serializable>
    implements IndexingAdapter<T> {
  protected IRouteContext ctx;

  protected RowMapper<T> rowMapper;

  /**
   * Constructor.
   * 
   * @param ctx
   */
  public AbstractIndexingAdapter(IRouteContext ctx) {
    this.ctx = ctx;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRouteContext getRouteContext() {
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
