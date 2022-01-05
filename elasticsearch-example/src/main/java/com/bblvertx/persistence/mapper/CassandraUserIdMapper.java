package com.bblvertx.persistence.mapper;

import static com.bblvertx.utils.singleton.impl.CassandraDataSource.getUuidFromRS;

import com.bblvertx.persistence.RowMapper;
import com.datastax.driver.core.Row;

import java.io.Serializable;

/**
 * User id mapper in cassandra.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class CassandraUserIdMapper implements RowMapper<Serializable> {
  /**
   * {@inheritDoc}
   */
  @Override
  public Serializable map(Object rs) {
    return getUuidFromRS((Row) rs, "uid");
  }
}
