package com.bblvertx.persistence;

import java.io.Serializable;

/**
 * Mapping between database and VO.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 * @param <T>
 */
public interface RowMapper<T extends Serializable> {
  /**
   * Mapping a row in resultset.
   * 
   * @param rs
   * @return T
   */
  T map(Object rs);
}
