package org.hotrod.getters;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobGetter implements ResultSetGetter {

  @Override
  public Object get(ResultSet rs, int ordinal) throws SQLException {
    Object v = rs.getBlob(ordinal);
    return rs.wasNull() ? null : v;
  }

}
