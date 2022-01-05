package com.bblvertx.persistence.mapper;

import static com.bblvertx.utils.JSONUtils.ERROR_PARSING_MSG;
import static com.bblvertx.utils.singleton.impl.CassandraDataSource.getIntFromRS;
import static com.bblvertx.utils.singleton.impl.CassandraDataSource.getStringFromRS;
import static com.bblvertx.utils.singleton.impl.CassandraDataSource.getUuidFromRS;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.bblvertx.persistence.RowMapper;
import com.bblvertx.pojo.vo.UserVO;
import com.datastax.driver.core.Row;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Row mapper for users in cassandra.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class CassandraUserMapper implements RowMapper<UserVO> {
  private static final Logger LOGGER = LogManager.getLogger(CassandraUserMapper.class);

  /**
   * Convert String JSON in a object.
   * 
   * @param json
   * @return the map
   */
  public static UserVO json2UserQuietly(String json) {
    UserVO rtn = null;

    if (isNotEmpty(json)) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        rtn = mapper.readValue(json, new TypeReference<UserVO>() {});
      } catch (Exception e) {
        LOGGER.error(ERROR_PARSING_MSG, e);
      }
    }

    return rtn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserVO map(Object rs) {
    Row cassandraRs = (Row) rs;
    UserVO rtn = json2UserQuietly(getStringFromRS(cassandraRs, "data"));
    rtn.setRsSearch(getIntFromRS(cassandraRs, "rs_search"));
    rtn.setId(getUuidFromRS(cassandraRs, "uid"));
    return rtn;
  }
}
