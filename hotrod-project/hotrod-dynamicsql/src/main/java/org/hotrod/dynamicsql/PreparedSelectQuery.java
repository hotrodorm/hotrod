package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.parameters.ParameterOccurrence;

public class PreparedSelectQuery<R> extends PreparedQuery {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedSelectQuery.class.getName());

  private RowReader<R> rr;

  public PreparedSelectQuery(SimpleStaticSegmentConsumer sc, RowReader<R> rr) {
    super(sc.getSQL(), sc.getParameters());
    this.rr = rr;
  }

  public List<R> execute(Connection conn) throws SQLException {
    try (PreparedStatement ps = prepareStatement(conn)) {
      applyParameters(ps, conn);
      try (ResultSet rs = ps.executeQuery()) {
        this.rr.discoverColumns(rs);
        List<R> rows = new ArrayList<>();
        while (rs.next()) {
          R r = this.rr.readRowFrom(rs, conn);
          rows.add(r);
        }
        return rows;
      }
    }
  }

  public R executeOne(Connection conn) throws SQLException {
    try (PreparedStatement ps = prepareStatement(conn)) {
      applyParameters(ps, conn);
      try (ResultSet rs = ps.executeQuery()) {
        this.rr.discoverColumns(rs);
        if (rs.next()) {
          R r = this.rr.readRowFrom(rs, conn);
          if (rs.next()) {
            throw new RuntimeException(".executeOne() expects a single row at the most, "
                + "but this SELECT query produced more than one row");
          }
          return r;
        }
        return null;
      }
    }
  }

  PreparedStatement prepareStatement(Connection conn) throws SQLException {
    return conn.prepareStatement(super.sql);
  }

  void applyParameters(PreparedStatement ps, Connection conn) throws SQLException {
    int ordinal = 1;
    for (ParameterOccurrence p : super.parameters) {
      p.applyTo(ps, ordinal++, conn);
    }
  }

  public Cursor<R> executeCursor(Connection conn) throws SQLException {
    return new DynamicCursor<>(conn, this, this.rr, null);
  }

  public Cursor<R> executeCursor(Connection conn, Integer fetchSize) throws SQLException {
    return new DynamicCursor<>(conn, this, this.rr, fetchSize);
  }

}
