package org.hotrod.livesql.queries.select.sets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public class RowCursor<T> implements Cursor<T> {

  private static final Logger log = Logger.getLogger(RowCursor.class.getName());

  private Connection conn;
  private PreparedStatement ps;
  private ResultSet rs;
  private RowReader<T> rowReader;

  public RowCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q, final LiveSQLLogging loggingAdapter,
      final RowReader<T> rowReader, final Integer fetchSize) throws SQLException {

    this.conn = null;
    this.ps = null;

    try {

      this.conn = context.getConnection();
      this.ps = this.conn.prepareStatement(q.getSQL());

      context.getLiveSQLDialect().enableSelectStreaming(this.ps, fetchSize);

      // 1. Apply parameters

      int n = 1;
      for (Object obj : q.getParameters().values()) {
        int i = n++;
        this.ps.setObject(i, obj);
      }

      this.rs = this.ps.executeQuery();

      if (rowReader == null) {
        this.rowReader = new FlatRowReader<>(context, q, this.rs);
      } else {
        this.rowReader = rowReader;
      }

      if (loggingAdapter != null && loggingAdapter.fullEnabled()) {
        loggingAdapter.fullLog(q.getPreview(true));
      } else if (loggingAdapter != null && loggingAdapter.basicEnabled()) {
        loggingAdapter.basicLog(q.getPreview(false));
      }

    } catch (SQLException e) {
      this.close();
      throw e;
    } finally {
      context.releaseConnection(this.conn);
    }

  }

  @Override
  public Iterator<T> iterator() {
    return new CursorIterator<>(this.conn, this.rs, this.rowReader);
  }

  @Override
  public void close() {
    try {
      try {
        if (this.rs != null) {
          this.rs.close();
        }
      } catch (SQLException e) {
        log.log(Level.SEVERE, "Could not close the database result set", e);
      }
    } finally {
      try {
        if (this.ps != null) {
          this.ps.close();
        }
      } catch (SQLException e) {
        log.log(Level.SEVERE, "Could not close the database prepared statement", e);
      } finally {
        try {
          if (this.conn != null) {
            this.conn.close();
          }
        } catch (SQLException e) {
          log.log(Level.SEVERE, "Could not close the database connection", e);
        }
      }
    }
  }

  public static class CursorIterator<T> implements Iterator<T> {

    private Connection conn;
    private ResultSet rs;
    private RowReader<T> rowReader;
    private boolean endReached;

    public CursorIterator(Connection conn, ResultSet rs, RowReader<T> rowReader) {
      this.conn = conn;
      this.rs = rs;
      this.rowReader = rowReader;
      this.endReached = false;
    }

    @Override
    public boolean hasNext() {
      if (this.endReached) {
        throw new NoSuchElementException("There are no more rows in the result set");
      }
      try {
        boolean next = this.rs.next();
        if (!next) {
          this.endReached = true;
        }
        return next;
      } catch (SQLException e) {
        throw new RuntimeException("Could not move to the next row of the result set", e);
      }
    }

    @Override
    public T next() {
      if (this.endReached) {
        throw new NoSuchElementException("There are no more rows in the result set");
      }
      try {
        return this.rowReader.readRowFrom(this.rs, this.conn);
      } catch (SQLException e) {
        throw new RuntimeException("Could not read the result set row", e);
      }
    }

  }

}
