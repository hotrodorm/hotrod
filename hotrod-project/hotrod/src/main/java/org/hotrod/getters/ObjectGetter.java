package org.hotrod.getters;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectGetter implements ResultSetGetter {

  private Class<?> type;

  public ObjectGetter(Class<?> type) {
    this.type = type;
  }

  @Override
  public Object get(ResultSet rs, int ordinal) throws SQLException {
    Object v = rs.getObject(ordinal, type);
    return rs.wasNull() ? null : v;
  }

}
