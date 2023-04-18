package app.converters;

import java.sql.SQLException;

import org.hotrod.runtime.converter.TypeConverter;
import org.postgresql.jdbc.PgArray;

public class ArrayConverter implements TypeConverter<java.sql.Array, Integer[]> {

  @Override
  public Integer[] decode(java.sql.Array raw) {
    if (raw == null)
      return null;
    System.out.println("<<< raw=" + raw + (raw == null ? "" : " (" + raw.getClass().getName() + ")"));
    PgArray a = (PgArray) raw;
    try {
      Integer[] ia = (Integer[]) a.getArray();
      return ia;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public java.sql.Array encode(Integer[] appValue) {
    System.out.println(">>> appValue=" + appValue);
    return null;
  }

}
