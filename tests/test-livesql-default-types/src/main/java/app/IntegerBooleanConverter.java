package app;

import java.sql.Connection;

import org.hotrod.runtime.converter.TypeConverter;

public class IntegerBooleanConverter implements TypeConverter<Integer, Boolean> {

  private static final Integer FALSE = 0;
  private static final Integer TRUE = 1;

  // Decoding is used when reading from the database

  @Override
  public Boolean decode(Integer raw, Connection conn) {
    if (raw == null) {
      return null;
    }
    return !FALSE.equals(raw); // Anything read that is different from zero is considered true
  }

  // Encoding is used when writing to the database

  @Override
  public Integer encode(Boolean value, Connection conn) {
    if (value == null) {
      return null;
    }
    return value ? TRUE : FALSE;
  }

}