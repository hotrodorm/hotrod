package org.hotrod.livesql.queries.select.tuples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.TableExpression;

public class TuplesRowReader<T> implements RowReader<T> {

  private static final Logger log = Logger.getLogger(TuplesRowReader.class.getName());

  protected TableExpression baseTableExpression = null;
  protected List<Join> joins = null;

  public TuplesRowReader(TableExpression baseTableExpression, List<Join> joins) {
    this.baseTableExpression = baseTableExpression;
    this.joins = joins;
  }

  @Override
  public T readRowFrom(ResultSet rs, Connection conn) throws SQLException {
    ResultSetMetaData rm = rs.getMetaData();
    int cols = rm.getColumnCount();
    log.info("=== Tuples Result Set (" + cols + " columns) ===");
    for (int i = 1; i <= cols; i++) {
      String name = rm.getColumnName(i);
      log.info("@ col #" + i + ": " + name);
    }

    return null;
  }

}
