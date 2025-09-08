package org.hotrod.dynamicsql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.parameters.ParameterInstance;

public class PreparedInsertSequenceInlineStandardResultsetQuery extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger
      .getLogger(PreparedInsertSequenceInlineStandardResultsetQuery.class.getName());

  @Override
  public Long execute(Connection conn, String sql, List<ParameterInstance> parameters, String sequencePreFetchSQL,
      String primaryKeyParameterName, String[] generatedKeysNames) throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      super.applyParameters(parameters, ps, conn);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getLong(1);
        }
        return null;
      }
    }
  }

}
