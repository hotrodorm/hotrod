package org.hotrod.getters;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DateGetter implements ResultSetGetter {

  @Override
  public Object get(ResultSet rs, int ordinal) throws SQLException {
    Object v = rs.getDate(ordinal);
    return rs.wasNull() ? null : v;
  }

}
