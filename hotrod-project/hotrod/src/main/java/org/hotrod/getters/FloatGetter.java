package org.hotrod.getters;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatGetter implements ResultSetGetter {

  @Override
  public Object get(ResultSet rs, int ordinal) throws SQLException {
    Object v = rs.getFloat(ordinal);
    return rs.wasNull() ? null : v;
  }

}
