package org.hotrod.dynamicsql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.segments.ParameterSegment;
import org.hotrod.dynamicsql.segments.TypedParameterSegment;

public class PreparedInsertSequencePreFetchQuery extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertSequencePreFetchQuery.class.getName());

  @Override
  public Long execute(Connection conn, String sql, List<ParameterSegment> parameters, String sequencePreFetchSQL,
      String primaryKeyParameterName, String[] generatedKeysNames) throws SQLException, DynamicExpressionException {
    Long seq = null;
    try (PreparedStatement ps = conn.prepareStatement(sequencePreFetchSQL)) {
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          seq = rs.getLong(1);
        }
      }
    }
    if (seq == null) {
      throw new SQLException("Could not retrieve sequence for INSERT using: " + sequencePreFetchSQL);
    }

    boolean wasSet = this.setParameter(parameters, primaryKeyParameterName, seq);
    if (!wasSet) {
      throw new SQLException(
          "Failed to INSERT: could not set value for primary key column parameter: " + primaryKeyParameterName);
    }

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      super.applyParameters(parameters, ps);
      ps.executeUpdate();
      return seq;
    }
  }

  private boolean setParameter(List<ParameterSegment> parameters, String name, Long value) {
    for (ParameterSegment s : parameters) {
      if (s instanceof TypedParameterSegment) {
        if (s.getName().equals(name)) {
          TypedParameterSegment ts = (TypedParameterSegment) s;
          ts.setValue(value);
          return true;
        }
      }
    }
    return false;
  }

}
