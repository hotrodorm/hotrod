package org.hotrod.dynamicsql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicSQLException;
import org.hotrod.dynamicsql.parameters.ParameterNotNullableUpdatableOccurrence;
import org.hotrod.dynamicsql.parameters.ParameterOccurrence;

public class PreparedInsertSequencePreFetchExecutor extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertSequencePreFetchExecutor.class.getName());

  @Override
  public Long execute(Connection conn, String sql, List<ParameterOccurrence> parameters, String sequencePreFetchSQL,
      String primaryKeyParameterName, String[] generatedKeysNames) throws DynamicSQLException {
    try {
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
        super.applyParameters(parameters, ps, conn);
        ps.executeUpdate();
        return seq;
      }
    } catch (SQLException e) {
      throw new DynamicSQLException("Error executing SQL INSERT query: " + sql, e);
    }

  }

  private boolean setParameter(List<ParameterOccurrence> parameters, String name, Long value) {
    for (ParameterOccurrence s : parameters) {
      if (s instanceof ParameterNotNullableUpdatableOccurrence) {
        ParameterNotNullableUpdatableOccurrence ts = (ParameterNotNullableUpdatableOccurrence) s;
        if (s.getName().equals(name)) {
          ts.setValue(value);
          return true;
        }
      }
    }
    return false;
  }

}
