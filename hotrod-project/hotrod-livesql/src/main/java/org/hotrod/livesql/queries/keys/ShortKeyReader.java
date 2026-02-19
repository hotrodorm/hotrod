package org.hotrod.livesql.queries.keys;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortKeyReader implements KeyReader<Short> {

  @Override
  public Short read(ResultSet rs, int ordinal) throws SQLException {
    short k = rs.getShort(ordinal);
    return rs.wasNull() ? null : k;
  }

}
