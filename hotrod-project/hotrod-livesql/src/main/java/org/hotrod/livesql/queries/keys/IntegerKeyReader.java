package org.hotrod.livesql.queries.keys;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerKeyReader implements KeyReader<Integer> {

  @Override
  public Integer read(ResultSet rs, int ordinal) throws SQLException {
    int k = rs.getInt(ordinal);
    return rs.wasNull() ? null : k;
  }

}
