package org.hotrod.dynamic.insert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.PreparedQuery;

public class PreparedInsertQuery extends PreparedQuery {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertQuery.class.getName());

  private InsertExecutor executor;
  private InsertProperties insertProperties;

  public PreparedInsertQuery(InsertExecutor executor, InsertProperties insertProperties) {
    super();
    this.executor = executor;
    this.insertProperties = insertProperties;
  }

  public Long execute(Connection conn) throws SQLException, DynamicExpressionException {
    return this.executor.execute(conn, this.sb.toString(), this.parameters, this.insertProperties);
  }

}
