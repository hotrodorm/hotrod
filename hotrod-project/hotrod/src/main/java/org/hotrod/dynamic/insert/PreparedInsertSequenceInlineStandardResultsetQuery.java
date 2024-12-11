package org.hotrod.dynamic.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.segments.ParameterSegment;

public class PreparedInsertSequenceInlineStandardResultsetQuery extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger
      .getLogger(PreparedInsertSequenceInlineStandardResultsetQuery.class.getName());

  public Long execute(Connection conn, String sql, List<ParameterSegment> parameters, InsertProperties insertProperties)
      throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      super.applyParameters(parameters, ps);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getLong(1);
        }
        return null;
      }
    }
  }

}
