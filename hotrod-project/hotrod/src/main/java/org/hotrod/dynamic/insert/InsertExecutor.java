package org.hotrod.dynamic.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.segments.ParameterSegment;

public abstract class InsertExecutor {

  public abstract Long execute(Connection conn, String sql, List<ParameterSegment> parameters,
      String sequencePreFetchSQL, String primaryKeyParameterName, String[] generatedKeysNames)
      throws SQLException, DynamicExpressionException;

  protected void applyParameters(List<ParameterSegment> parameters, PreparedStatement ps) throws SQLException {
    int ordinal = 1;
    for (ParameterSegment p : parameters) {
      if (p.getValue() != null) {
        ps.setObject(ordinal++, p.getValue());
      } else {
        ps.setNull(ordinal++, p.getSQLType());
      }
    }
  }

}
