package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class DynamicReader {

  private ResultSet rs;
  private Connection conn;
  private Set<String> columnLabels;

  public DynamicReader(ResultSet rs, Connection conn) throws SQLException {
    this.rs = rs;
    this.conn = conn;
    ResultSetMetaData rm = rs.getMetaData();
    this.columnLabels = new LinkedHashSet<>();
    int cols = rm.getColumnCount();
    for (int i = 1; i < cols; i++) {
      this.columnLabels.add(rm.getColumnLabel(i));
    }
  }

  Row read() throws SQLException {
    Row r = new Row();
    for (String label : this.columnLabels) {
      r.put(label, rs.getObject(label));
    }
    return r;
  }

}
