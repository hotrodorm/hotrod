package org.hotrod.getters;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLXMLGetter implements ResultSetGetter {

  @Override
  public Object get(ResultSet rs, int ordinal) throws SQLException {
    Object v = rs.getSQLXML(ordinal);
    return rs.wasNull() ? null : v;
  }

}
