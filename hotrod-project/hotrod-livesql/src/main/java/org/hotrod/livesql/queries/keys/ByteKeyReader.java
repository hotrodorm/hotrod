package org.hotrod.livesql.queries.keys;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteKeyReader implements KeyReader<Byte> {

  @Override
  public Byte read(ResultSet rs, int ordinal) throws SQLException {
    byte k = rs.getByte(ordinal);
    return rs.wasNull() ? null : k;
  }

}
