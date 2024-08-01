package app;

import java.sql.Connection;
import java.sql.SQLException;

import org.hotrod.runtime.converter.TypeConverter;

public class StringIntegerConverter implements TypeConverter<String, Integer> {

  @Override
  public Integer decode(String raw, Connection conn) throws SQLException {
    return raw == null ? -1 : raw.length();
  }

  @Override
  public String encode(Integer value, Connection conn) throws SQLException {
    return "" + value;
  }

}