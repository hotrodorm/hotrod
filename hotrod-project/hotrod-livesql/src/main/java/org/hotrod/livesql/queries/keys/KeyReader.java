package org.hotrod.livesql.queries.keys;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface KeyReader<T> {

  public static KeyReader<Byte> BYTE_KEY_READER = new ByteKeyReader();
  public static KeyReader<Short> SHORT_KEY_READER = new ShortKeyReader();
  public static KeyReader<Integer> INTEGER_KEY_READER = new IntegerKeyReader();
  public static KeyReader<Long> LONG_KEY_READER = new LongKeyReader();

  T read(ResultSet rs, int ordinal) throws SQLException;

}
