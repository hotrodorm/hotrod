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

public class PreparedInsertSequenceInlineKeysResultsetQuery extends InsertExecutor {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedInsertSequenceInlineKeysResultsetQuery.class.getName());

  public Long execute(Connection conn, String sql, List<ParameterSegment> parameters, InsertProperties insertProperties)
      throws SQLException, DynamicExpressionException {
    String[] generatedKeysNames = insertProperties.getGeneratedKeysNames();
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
//    log.info(">> inserted");
    try (ResultSet rs = ps.getGeneratedKeys()) {
//      log.info(">> rs=" + rs);
      if (rs.next()) {
//        log.info(">> keys found!");
        return rs.getLong(1);
      }
//      log.info(">> no keys found.");
      return null;
    }
  }

}
