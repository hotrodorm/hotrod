package org.hotrod.dynamic.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.segments.ParameterSegment;

public class PreparedInsertIdentityInlineKeyResultSetQuery extends InsertExecutor {

  private static final Logger log = Logger.getLogger(PreparedInsertIdentityInlineKeyResultSetQuery.class.getName());

  @Override
  public Long execute(Connection conn, String sql, List<ParameterSegment> parameters, String sequencePreFetchSQL,
      String primaryKeyParameterName, String[] generatedKeysNames) throws SQLException, DynamicExpressionException {
    if (generatedKeysNames == null || generatedKeysNames.length == 0) {
      log.info(">>> Statement.RETURN_GENERATED_KEYS");
      try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        return execute(parameters, ps);
      }
    } else {
      log.info(">>> generatedKeysNames");
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
      log.info("rm.getColumnTypeName(1)=" + rm.getColumnTypeName(1));
      log.info("rm.getColumnClassName(1)=" + rm.getColumnClassName(1));
      log.info("rm.getColumnType(1)=" + rm.getColumnType(1));
      if (rs.next()) {
        return rs.getLong(1);
      }
      return null;
    }
  }

}
