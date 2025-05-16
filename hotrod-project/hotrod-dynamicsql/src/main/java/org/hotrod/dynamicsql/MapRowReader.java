package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapRowReader implements RowReader<Row> {

  private List<String> columns = null;

  @Override
  public Row readRowFrom(ResultSet rs, Connection conn) throws SQLException {
    if (this.columns == null) {
      this.initialize(rs.getMetaData());
    }
    Row row = new Row();
    int i = 1;
    for (String column : this.columns) {
      Object value = rs.getObject(i);
      row.put(column, value);
      i++;
    }
    return row;
  }

  private void initialize(ResultSetMetaData rm) throws SQLException {
    int columnCount = rm.getColumnCount();
    this.columns = new ArrayList<>();
    for (int i = 1; i <= columnCount; i++) {
      columns.add(rm.getColumnName(i));
    }
  }

}
