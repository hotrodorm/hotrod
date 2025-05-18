package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowReader<T> {

  default void discoverColumns(ResultSet rs) throws SQLException {
    // nothing to do; all columns selected by default
  }

  T readRowFrom(ResultSet rs, Connection conn) throws SQLException;

}