package org.hotrod.dynamic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.hotrod.dynamic.segments.ParameterSegment;

public class PreparedInsertQuery extends PreparedQuery {

  public enum PrimaryKeyRetrievalMode {
    NO_RETRIEVAL, //
    SEQUENCE_PREFETCH, //
    SEQUENCE_INLINE_STANDARD_RESULTSET, //
    SEQUENCE_INLINE_KEYS_RESULTSET, //
    SEQUENCE_POSTFETCH, //
    IDENTITY_INLINE_STANDARD_RESULTSET, //
    IDENTITY_INLINE_KEYS_RESULTSET, //
    IDENTITY_POSTFETCH;
  }

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertQuery.class.getName());

  public Long execute(Connection conn) throws SQLException, DynamicExpressionException {
    return executeIdentityInlineKeysResultSet(conn);
  }

  private Long executeIdentityInlineKeysResultSet(Connection conn) throws SQLException {
    try (PreparedStatement ps = conn.prepareStatement(this.sb.toString(), Statement.RETURN_GENERATED_KEYS)) {
      int ordinal = 1;
      for (ParameterSegment p : this.parameters) {
        if (p.getValue() != null) {
          ps.setObject(ordinal++, p.getValue());
        } else {
          ps.setNull(ordinal++, p.getSQLType());
        }
      }
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          return rs.getLong(1);
        }
        return null;
      }
    }
  }

}
