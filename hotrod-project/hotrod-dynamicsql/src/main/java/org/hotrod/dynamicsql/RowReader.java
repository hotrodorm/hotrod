package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowReader<T> {

  T readRowFrom(ResultSet rs, Connection conn) throws SQLException;

}