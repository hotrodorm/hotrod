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
  public void discoverDynamicColumns(ResultSet rs) throws SQLException {
    ResultSetMetaData rm = rs.getMetaData();
    int columnCount = rm.getColumnCount();
    this.columns = new ArrayList<>();
    for (int i = 1; i <= columnCount; i++) {
      columns.add(rm.getColumnName(i));
    }
  }

  @Override
  public Row readRowFrom(ResultSet rs, Connection conn) throws SQLException {
    Row row = new Row();
    int i = 1;
    for (String column : this.columns) {
      Object value = rs.getObject(i);
      row.put(column, value);
      i++;
    }
    return row;
  }

}
