package com.bblvertx.persistence.mapper;

import static com.bblvertx.utils.singleton.impl.CassandraDataSource.getIntFromRS;

import com.bblvertx.persistence.RowMapper;
import com.datastax.driver.core.Row;

/**
 * COUNT mapper in cassandra.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class CassandraCountMapper implements RowMapper<Integer> {
  /**
   * {@inheritDoc}
   */
  @Override
  public Integer map(Object rs) {
    return getIntFromRS((Row) rs, "count");
  }
}
