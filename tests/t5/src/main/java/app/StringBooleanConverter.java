package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;

public class StringBooleanConverter implements TypeConverter<String, Boolean> {

  private static String TRUE_DB = "S";
  private static String FALSE_DB = "N";

  @Override
  public Boolean decode(String raw, Connection conn) {
    if (raw == null || raw.equals(FALSE_DB)) {
      return false;
    }
    if (raw.equals(TRUE_DB)) {
      return true;
    }
    throw new RuntimeException("Cannot convert raw-value '" + raw + "' to any Boolean enum");
  }

  @Override
  public String encode(Boolean value, Connection conn) {
    if (value == null) {
      throw new RuntimeException("Value cannot be null");
    }
    if (value) {
      return TRUE_DB;
    }
    return FALSE_DB;
  }

}