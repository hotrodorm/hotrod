package org.hotrod.dynamicsql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.segments.ParameterSegment;

public class PreparedInsertIdentityInlineKeyResultSetQuery extends InsertExecutor {

  private static final Logger log = Logger.getLogger(PreparedInsertIdentityInlineKeyResultSetQuery.class.getName());

  @Override
  public Long execute(Connection conn, String sql, List<ParameterSegment> parameters, String sequencePreFetchSQL,
      String primaryKeyParameterName, String[] generatedKeysNames) throws SQLException, DynamicExpressionException {
    if (generatedKeysNames == null || generatedKeysNames.length == 0) {
      try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        return execute(parameters, ps);
      }
    } else {
      try (PreparedStatement ps = conn.prepareStatement(sql, generatedKeysNames)) {
        return execute(parameters, ps);
      }
    }
  }

  private Long execute(List<ParameterSegment> parameters, PreparedStatement ps) throws SQLException {
    super.applyParameters(parameters, ps);
    ps.executeUpdate();
    try (ResultSet rs = ps.getGeneratedKeys()) {
      ResultSetMetaData rm = rs.getMetaData();
      if (rs.next()) {
        return rs.getLong(1);
      }
      return null;
    }
  }

}
