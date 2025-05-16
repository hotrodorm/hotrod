package org.hotrod.dynamicsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DynCursor<R> implements Cursor<R> {

  private Connection conn;
  private PreparedStatement ps;
  private ResultSet rs;
  private RowReader<R> rowReader;

  public DynCursor(Connection conn, PreparedSelectQuery<R> q, RowReader<R> rowReader, final Integer fetchSize)
      throws SQLException {

    try {

      this.ps = q.prepareStatement(conn);
      if (fetchSize != null) {
        this.ps.setFetchSize(fetchSize);
      }

      q.applyParameters(this.ps);

      this.rs = this.ps.executeQuery();

      if (rowReader == null) {
        this.rowReader = new DynRowReader(rs.getMetaData());
      } else {
        this.rowReader = rowReader;
      }

    } catch (SQLException e) {
      try {
        this.close();
        throw e;
      } catch (IOException e1) {
        throw e;
      }
    }

  }

  @Override
  public Iterator<R> iterator() {
    return new CursorIterator<>(this.conn, this.rs, this.rowReader);
  }

  @Override
  public void close() throws IOException {
    try {
      try {
        if (this.rs != null) {
          this.rs.close();
        }
      } catch (SQLException e) {
        throw new IOException("Could not close the database result set", e);
      }
    } finally {
      try {
        if (this.ps != null) {
          this.ps.close();
        }
      } catch (SQLException e) {
        throw new IOException("Could not close the database prepared statement", e);
      } finally {
        try {
          if (this.conn != null) {
            this.conn.close();
          }
        } catch (SQLException e) {
          throw new IOException("Could not close the database connection", e);
        }
      }
    }
  }

  public static class CursorIterator<T> implements Iterator<T> {

    private Connection conn;
    private ResultSet rs;
    private RowReader<T> rowReader;

    public CursorIterator(Connection conn, ResultSet rs, RowReader<T> rowReader) {
      this.conn = conn;
      this.rs = rs;
      this.rowReader = rowReader;
    }

    @Override
    public boolean hasNext() {
      try {
        return this.rs.next();
      } catch (SQLException e) {
        throw new RuntimeException("Could not advance to the next row of the result set", e);
      }
    }

    @Override
    public T next() {
      try {
        return this.rowReader.readRowFrom(this.rs, this.conn);
      } catch (SQLException e) {
        throw new RuntimeException("Could not read the result set row", e);
      }
    }

  }

  class DynRowReader implements RowReader<R> {

    private List<String> columns = null;

    public DynRowReader(ResultSetMetaData rm) throws SQLException {
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
