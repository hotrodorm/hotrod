package org.hotrod.dynamicsql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicSQLException;
import org.hotrod.dynamicsql.parameters.ParameterOccurrence;

public class PreparedInsertNoRetrievalExecutor extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertNoRetrievalExecutor.class.getName());

  @Override
  public Long execute(Connection conn, String sql, List<ParameterOccurrence> parameters, String sequencePreFetchSQL,
      String primaryKeyParameterName, String[] generatedKeysNames) throws DynamicSQLException {
    try {
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        super.applyParameters(parameters, ps, conn);
        ps.executeUpdate();
        return null;
      }
    } catch (SQLException e) {
      throw new DynamicSQLException("Error executing SQL INSERT query: " + sql, e);
    }
  }

}
