package org.hotrod.dynamic.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.segments.ParameterSegment;

public class PreparedInsertIdentityInlineKeyResultSetQuery extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertIdentityInlineKeyResultSetQuery.class.getName());

  public Long execute(Connection conn, String sql, List<ParameterSegment> parameters, InsertProperties insertProperties)
      throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      super.applyParameters(parameters, ps);
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
