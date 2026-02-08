package org.hotrod.getters;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteGetter implements ResultSetGetter {

  @Override
  public Object get(ResultSet rs, int ordinal) throws SQLException {
    Object v = rs.getByte(ordinal);
    return rs.wasNull() ? null : v;
  }

}
