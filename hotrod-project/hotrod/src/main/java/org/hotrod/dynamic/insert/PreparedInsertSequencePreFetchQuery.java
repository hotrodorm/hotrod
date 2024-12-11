package org.hotrod.dynamic.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.segments.ParameterSegment;

public class PreparedInsertSequencePreFetchQuery extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertSequencePreFetchQuery.class.getName());

  public Long execute(Connection conn, String sql, List<ParameterSegment> parameters, InsertProperties insertProperties)
      throws SQLException, DynamicExpressionException {

    String prefetch = insertProperties.getSequencePreFetchSQL();
    Long seq = null;
    try (PreparedStatement ps = conn.prepareStatement(prefetch)) {
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          seq = rs.getLong(1);
        }
      }
    }
    if (seq == null) {
      throw new SQLException("Could not retrieve sequence for INSERT using: " + prefetch);
    }

    boolean wasSet = this.setParameter(parameters, insertProperties.getPrimaryKeyName(), seq);
    if (!wasSet) {
      throw new SQLException("Failed to INSERT: could not set value for primary key column parameter: "
          + insertProperties.getPrimaryKeyName());
    }

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      super.applyParameters(parameters, ps);
      ps.executeUpdate();
      return seq;
    }
  }

  private boolean setParameter(List<ParameterSegment> parameters, String name, Long value) {
    for (ParameterSegment s : parameters) {
      if (s.getName().equals(name)) {
        s.setValue(value);
        return true;
      }
    }
    return false;
  }

}
