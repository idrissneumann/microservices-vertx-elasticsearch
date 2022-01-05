package com.bblvertx.indexation.adapter;

import com.bblvertx.persistence.RowMapper;
import com.bblvertx.utils.singleton.impl.RouteContext;

import java.io.Serializable;

/**
 * Abstract delta indexing adapter.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 * 
 * @param <T> value object type
 *
 */
public abstract class AbstractIndexingDeltaAdapter<T extends Serializable>
    extends AbstractIndexingAdapter<T> implements IndexingDeltaAdapter<T> {
  protected RowMapper<Serializable> rowMapperId;

  /**
   * Constructor.
   * 
   * @param ctx
   */
  public AbstractIndexingDeltaAdapter(RouteContext ctx) {
    super(ctx);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RowMapper<Serializable> getIdMapper() {
    return rowMapperId;
  }
}
