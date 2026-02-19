package org.hotrod.livesql.queries.keys;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongKeyReader implements KeyReader<Long> {

  @Override
  public Long read(ResultSet rs, int ordinal) throws SQLException {
    long k = rs.getLong(ordinal);
    return rs.wasNull() ? null : k;
  }

}
