package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRowReader implements RowReader<Map<String, Object>> {

  private List<String> columns = null;

  @Override
  public Map<String, Object> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
    if (this.columns == null) {
      this.initialize(rs.getMetaData());
    }
    Map<String, Object> m = new HashMap<>();
    int i = 1;
    for (String column : this.columns) {
      Object value = rs.getObject(i);
      m.put(column, value);
      i++;
    }
    return m;
  }

  private void initialize(ResultSetMetaData rm) throws SQLException {
    int columnCount = rm.getColumnCount();
    this.columns = new ArrayList<>();
    for (int i = 1; i <= columnCount; i++) {
      columns.add(rm.getColumnName(i));
    }
  }

}
