package org.hotrod.dynamic.insert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.PreparedQuery;

public class PreparedInsertQuery extends PreparedQuery {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertQuery.class.getName());

  private PrimaryKeyRetrievalMode primaryKeyRetrievalMode;
  private String sequencePreFetchSQL;
  private String primaryKeyParameterName;
  private String[] generatedKeysNames;

  public PreparedInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode, String sequencePreFetchSQL,
      String primaryKeyParameterName, String[] generatedKeysNames) {
    super();
    this.primaryKeyRetrievalMode = primaryKeyRetrievalMode;
    this.sequencePreFetchSQL = sequencePreFetchSQL;
    this.primaryKeyParameterName = primaryKeyParameterName;
    this.generatedKeysNames = generatedKeysNames;
  }

  public Long execute(Connection conn) throws SQLException, DynamicExpressionException {
    return this.primaryKeyRetrievalMode.getInsertExecutor().execute(conn, this.sb.toString(), this.parameters,
        this.sequencePreFetchSQL, this.primaryKeyParameterName, this.generatedKeysNames);
  }

}
