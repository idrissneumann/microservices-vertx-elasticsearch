package com.bblvertx.indexation.adapter;

import com.bblvertx.persistence.RowMapper;

import java.io.Serializable;

/**
 * Indexing delta job adapter.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 * 
 * @param <T> value object's type.
 *
 */
public interface IndexingDeltaAdapter<T extends Serializable> extends IndexingAdapter<T> {
  /**
   * Getting the "select flag" SQL/CQL query.
   * 
   * @return the query
   */
  String getDbSelectFlagIdx();

  /**
   * Getting the "delete rs search" query.
   * 
   * @return the query
   */
  String getDbDeleteRsSearch();

  /**
   * Getting a mapper for the id.
   * 
   * @return RowMapper<Serializable>
   */
  RowMapper<Serializable> getIdMapper();
}
