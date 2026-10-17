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
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public class RowCursor<T> implements Cursor<T> {

  private static final Logger log = Logger.getLogger(RowCursor.class.getName());

  private LiveSQLContext context;
  private Connection conn;
  private PreparedStatement ps;
  private ResultSet rs;
  private RowReader<T> rowReader;

  public RowCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q, final LiveSQLLogging loggingAdapter,
      final RowReader<T> rowReader, final Integer fetchSize) throws SQLException {

    this.context = context;
    this.conn = null;
    this.ps = null;
    this.rs = null;

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
      // close resources if there is an exception during initialization
      this.close();
      throw e;
    }

  }

  @Override
  public Iterator<T> iterator() {
    return new CursorIterator<>(this);
  }

  @Override
  public void close() {
    try {
      try {
        if (this.rs != null) {
          this.rs.close();
          this.rs = null;
        }
      } catch (SQLException e) {
        log.log(Level.SEVERE, "Could not close the database result set", e);
      }
    } finally {
      try {
        if (this.ps != null) {
          this.ps.close();
          this.ps = null;
        }
      } catch (SQLException e) {
        log.log(Level.SEVERE, "Could not close the database prepared statement", e);
      } finally {
        try {
          if (this.conn != null) {
            this.context.releaseConnection(this.conn);
            this.conn = null;
          }
        } catch (Exception e) {
          log.log(Level.SEVERE, "Could not release the database connection", e);
        }
      }
    }
  }

  private static class CursorIterator<T> implements Iterator<T> {

    private final RowCursor<T> parent;
    private boolean endReached;
    private boolean cachedFetched;
    private Boolean cachedHasNext; // null => not fetched

    public CursorIterator(RowCursor<T> parent) {
      this.parent = parent;
      this.endReached = false;
      this.cachedFetched = false;
      this.cachedHasNext = null;
    }

    private boolean fetchNextIfNeeded() {
      if (this.cachedHasNext != null) {
        return this.cachedHasNext;
      }
      if (this.endReached) {
        this.cachedHasNext = Boolean.FALSE;
        return false;
      }
      try {
        boolean next = this.parent.rs.next();
        if (!next) {
          this.endReached = true;
          this.cachedHasNext = Boolean.FALSE;
          return false;
        } else {
          this.cachedHasNext = Boolean.TRUE;
          return true;
        }
      } catch (SQLException e) {
        throw new LiveSQLException("Could not move to the next row of the result set", e);
      }
    }

    @Override
    public boolean hasNext() {
      return fetchNextIfNeeded();
    }

    @Override
    public T next() {
      if (!fetchNextIfNeeded()) {
        throw new NoSuchElementException("There are no more rows in the result set");
      }
      try {
        // We already advanced rs in fetchNextIfNeeded(); read the current row
        T row = this.parent.rowReader.readRowFrom(this.parent.rs, this.parent.conn);
        // clear cachedHasNext so subsequent hasNext() will advance further
        this.cachedHasNext = null;
        return row;
      } catch (SQLException e) {
        throw new LiveSQLException("Could not read the result set row", e);
      }
    }
  }

}
