package org.hotrod.dynamicsql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.parameters.ParameterOccurrence;

public class PreparedInsertSequenceInlineKeysResultsetExecutor extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertSequenceInlineKeysResultsetExecutor.class.getName());

  @Override
  public Long execute(Connection conn, String sql, List<ParameterOccurrence> parameters, String sequencePreFetchSQL,
      String primaryKeyParameterName, String[] generatedKeysNames) throws SQLException, DynamicExpressionException {
    if (generatedKeysNames == null || generatedKeysNames.length == 0) {
      try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        return execute(parameters, ps, conn);
      }
    } else {
      try (PreparedStatement ps = conn.prepareStatement(sql, generatedKeysNames)) {
        return execute(parameters, ps, conn);
      }
    }
  }

  private Long execute(List<ParameterOccurrence> parameters, PreparedStatement ps, Connection conn) throws SQLException {
    super.applyParameters(parameters, ps, conn);
    ps.executeUpdate();
    try (ResultSet rs = ps.getGeneratedKeys()) {
      if (rs.next()) {
        long k = rs.getLong(1);
        return k;
      }
      return null;
    }
  }

}
