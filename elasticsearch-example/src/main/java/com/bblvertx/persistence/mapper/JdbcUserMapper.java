package com.bblvertx.persistence.mapper;

import static com.bblvertx.utils.singleton.impl.JdbcDataSource.getDateFromRS;
import static com.bblvertx.utils.singleton.impl.JdbcDataSource.getIntFromRS;
import static com.bblvertx.utils.singleton.impl.JdbcDataSource.getStringFromRS;

import com.bblvertx.exception.TechnicalException;
import com.bblvertx.persistence.RowMapper;
import com.bblvertx.pojo.vo.UserVO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row mapper for users in jdbc compliant database.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class JdbcUserMapper implements RowMapper<UserVO> {
  /**
   * {@inheritDoc}
   */
  @Override
  public UserVO map(Object rs) {
    try {
      UserVO rtn = new UserVO();
      ResultSet jdbcRs = (ResultSet) rs;
      rtn.setDateConnect(getDateFromRS(jdbcRs, "date_connect"));
      rtn.setDateUpdate(getDateFromRS(jdbcRs, "date_update"));
      rtn.setId(getStringFromRS(jdbcRs, "id"));
      rtn.setName(getStringFromRS(jdbcRs, "name"));
      rtn.setFirstname(getStringFromRS(jdbcRs, "firstname"));
      rtn.setEmail(getStringFromRS(jdbcRs, "email"));
      rtn.setSkill(getStringFromRS(jdbcRs, "skill"));
      rtn.setRsSearch(getIntFromRS(jdbcRs, "rs_search"));
      return rtn;
    } catch (SQLException e) {
      throw new TechnicalException(e);
    }
  }
}
