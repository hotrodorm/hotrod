package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.segments.ParameterSegment;

public class PreparedSelectQuery<R> extends PreparedQuery {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedSelectQuery.class.getName());

  public PreparedSelectQuery(SimpleStaticSegmentConsumer sc) {
    super(sc.getSQL(), sc.getParameters());
  }

  public List<R> execute(Connection conn, RowReader<R> rowReader) throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(super.sql)) {
      int ordinal = 1;
      for (ParameterSegment p : super.parameters) {
        p.applyTo(ps, ordinal++);
      }
      try (ResultSet rs = ps.executeQuery()) {
        List<R> rows = new ArrayList<>();
        while (rs.next()) {
          R r = rowReader.readRowFrom(rs, conn);
          rows.add(r);
        }
        return rows;
      }
    }
  }

  public static interface RowReader<R> {

    R readRowFrom(ResultSet rs, Connection conn) throws SQLException;

  }

}
