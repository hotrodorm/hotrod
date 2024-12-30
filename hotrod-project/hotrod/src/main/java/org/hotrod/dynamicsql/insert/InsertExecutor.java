package org.hotrod.dynamicsql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.segments.ParameterSegment;

public abstract class InsertExecutor {

  public abstract Long execute(Connection conn, String sql, List<ParameterSegment> parameters,
      String sequencePreFetchSQL, String primaryKeyParameterName, String[] generatedKeysNames)
      throws SQLException, DynamicExpressionException;

  protected void applyParameters(List<ParameterSegment> parameters, PreparedStatement ps) throws SQLException {
    int ordinal = 1;
    for (ParameterSegment p : parameters) {
      p.applyTo(ps, ordinal);
    }
  }

}
