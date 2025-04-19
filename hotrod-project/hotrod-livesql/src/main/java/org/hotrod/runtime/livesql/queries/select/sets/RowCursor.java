package org.hotrod.runtime.livesql.queries.select.sets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.hotrod.cursors.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;

public class RowCursor<T> implements Cursor<T> {

  private Connection conn;
  private PreparedStatement ps;
  private ResultSet rs;
  private RowReader<T> rowReader;

  public RowCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q, final RowReader<T> rowReader, final Integer fetchSize)
      throws SQLException {

    try {

      this.conn = context.getDataSource().getConnection();
      this.ps = conn.prepareStatement(q.getSQL());
      
      context.getLiveSQLDialect().enableSelectStreaming(this.ps, fetchSize);

      // 1. Apply parameters

      int n = 1;
      for (Object obj : q.getParameters().values()) {
        int i = n++;
        ps.setObject(i, obj);
      }

      this.rs = ps.executeQuery();

      if (rowReader == null) {
        this.rowReader = new GenericRowReader<>(context, q, rs);
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
  public Iterator<T> iterator() {
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
        throw new RuntimeException("Could not move to the next row of the result set", e);
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

}
