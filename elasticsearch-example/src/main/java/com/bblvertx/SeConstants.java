package com.bblvertx;

/**
 * Constants interface.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public interface SeConstants {
  String KEY_PORT = "se.port";
  String CLASS_ROUTE_PATTERN = "com.bblvertx.route.impl.%s";
  String PREFIX_URL = "/bblvertx/api%s";

  String KEY_TPL_ROUTE_URL = "se.route.%s.url";
  String KEY_TPL_ROUTE_CONTENT_TYPE = "se.route.%s.contenttype";
  String KEY_TPL_ROUTE_CLASS = "se.route.%s.class";

  /**
   * Properties files.
   */
  String APP_CONFIG_FILE = "application.properties";
  String ROUTE_CONFIG_FILE = "routes.properties";
  String SQL_CONFIG_FILE = "sqlqueries.properties";
  String CQL_CONFIG_FILE = "cqlqueries.properties";

  /**
   * SQL queries.
   */
  String SELECT_CHECK = "db.check.connection";
  String SELECT_USER = "db.user.rssearch";
  String SELECT_SINGLE_USER = "db.user.single";
  String SELECT_USER_FLAG = "db.user.flagidx";
  String DELETE_RSSEARCH = "db.delete.rssearch";
  String UPDATE_RSSEARCH = "db.update.rssearch";

  /**
   * JDBC database
   */
  String DB_KEY_DRIVER = "db.driver";
  String DB_KEY_URL = "db.url";
  String DB_KEY_USERNAME = "db.username";
  String DB_KEY_PASSWD = "db.password";
  String DB_KEY_SCHEMA = "db.schema";
  String DB_KEY_MIN_POOL_SIZE = "db.min_pool_size";
  String DB_KEY_MAX_POOL_SIZE = "db.min_pool_size";
  String DB_KEY_MAX_STATEMENTS = "db.max_statements";
  String DB_KEY_PAGINATION = "db.pagination";

  /**
   * Cassandra database
   */
  String CASS_HOST = "cassandra.host";
  String CASS_PORT = "cassandra.port";
  String CASS_USERNAME = "cassandra.username";
  String CASS_PASSWD = "cassandra.password";
  String CASS_KEYSPACE = "cassandra.keyspace";
  String CASS_BASE_DELAY_MS = "cassandra.base.delay.ms";
  String CASS_MAX_DELAY_MS = "cassandra.max.delay.ms";

  /**
   * Elastic Search
   */
  String ES_KEY_HOST = "es.host";
  String ES_KEY_PORT = "es.port";
  String ES_KEY_CLUSTER = "es.cluster";

  /**
   * Indexes
   */
  String ES_INDEX_USER = "user";

  /**
   * Flag RS_SEARCH values.
   */
  Integer RS_TO_DELETE = -1;
  Integer RS_TO_UPDATE = 1;
  Integer RS_TO_STAY = 0;

  /**
   * Error messages.
   */
  String MSG_BAD_REQUEST = "The parameter %s is mandatory !";
  String MSG_BAD_REQUEST_MUST_BE_EMPTY = "The parameter %s must be null !";
  String MSG_BAD_REQUEST_MUST_BE_NUMERIC = "The parameter %s must be numeric !";
  String MSG_BAD_REQUEST_MUST_BE_CALENDAR = "The parameter %s must be a date !";

  /**
   * Others.
   */
  String EMPTY_STRING = "";
  String EMPTY_JSON = "[]";
  String RESPONSE_HTML_TEMPLATE = "<html><body><div>%s</div></body></html>";
  String NAMESPACE_URI = "http://bblvertx.com";
  String SEPARATOR_DEFAULT = ";";
}
