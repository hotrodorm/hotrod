package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowReader<T> {

  // Optional method to discover columns in a dynamic ResultSet before reading
  // rows.
  default void discoverDynamicColumns(ResultSet rs) throws SQLException {
    // Default implementation does nothing. Subclasses can override if needed.
  }

  T readRowFrom(ResultSet rs, Connection conn) throws SQLException;

}