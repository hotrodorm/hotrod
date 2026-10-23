package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DynamicCursor<R> implements Cursor<R> {

  private static final Logger log = Logger.getLogger(DynamicCursor.class.getName());

  private Connection conn;
  private PreparedStatement ps;
  private ResultSet rs;
  private RowReader<R> rowReader;
  private CursorIterator<R> iterator;

  public DynamicCursor(DataSource dataSource, PreparedSelectQuery<R> q, RowReader<R> rowReader, final Integer fetchSize)
      throws SQLException {

    this.conn = null;

    try {
      this.conn = dataSource.getConnection();

      this.ps = q.prepareStatement(conn);
      if (fetchSize != null) {
        this.ps.setFetchSize(fetchSize);
      }

      q.applyParameters(this.ps, conn);

      this.rs = this.ps.executeQuery();

      if (rowReader == null) {
        this.rowReader = new DynRowReader();
      } else {
        this.rowReader = rowReader;
      }
      this.rowReader.discoverColumns(this.rs);

    } catch (SQLException e) {
      this.close();
      throw e;
    }

  }

  @Override
  public synchronized Iterator<R> iterator() {
    if (this.iterator == null) {
      this.iterator = new CursorIterator<>(this.conn, this.rs, this.rowReader);
    }
    return this.iterator();
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

    private boolean nextAttempted;
    private boolean hasNext;
    private boolean endReached;

    public CursorIterator(Connection conn, ResultSet rs, RowReader<T> rowReader) {
      this.conn = conn;
      this.rs = rs;
      this.rowReader = rowReader;
      this.nextAttempted = false;
      this.hasNext = false;
      this.endReached = false;
    }

    @Override
    public boolean hasNext() {
      if (this.endReached) {
        return false;
      }
      try {
        nextIfNotAttempted();
        return this.hasNext;
      } catch (SQLException e) {
        throw new DynamicSQLException("Could not advance to the next row of the result set", e);
      }
    }

    private void nextIfNotAttempted() throws SQLException {
      if (!this.nextAttempted) {
        this.hasNext = this.rs.next();
        this.endReached = !this.hasNext;
        this.nextAttempted = true;
      }
    }

    @Override
    public T next() {
      try {
        nextIfNotAttempted();
        if (this.endReached) {
          throw new NoSuchElementException("There are no more rows in the result set");
        }
        T row = this.rowReader.readRowFrom(this.rs, this.conn);
        this.nextAttempted = false;
        return row;
      } catch (SQLException e) {
        throw new DynamicSQLException("Could not read the result set row", e);
      }
    }

  }

  class DynRowReader implements RowReader<R> {

    private List<String> columns = null;

    @Override
    public void discoverColumns(ResultSet rs) throws SQLException {
      ResultSetMetaData rm = rs.getMetaData();
      int columnCount = rm.getColumnCount();
      this.columns = new ArrayList<>();
      for (int i = 1; i <= columnCount; i++) {
        columns.add(rm.getColumnName(i));
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public R readRowFrom(ResultSet rs, Connection conn) throws SQLException {
      Row row = new Row();
      int i = 1;
      for (String column : this.columns) {
        Object value = rs.getObject(i);
        row.put(column, value);
        i++;
      }
      return (R) row;
    }

  }

}
