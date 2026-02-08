package org.hotrod.getters;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringGetter implements ResultSetGetter {

  @Override
  public Object get(ResultSet rs, int ordinal) throws SQLException {
    Object v = rs.getString(ordinal);
    return rs.wasNull() ? null : v;
  }

}
