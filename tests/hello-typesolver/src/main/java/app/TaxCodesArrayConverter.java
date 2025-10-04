package app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.converter.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public class TaxCodesArrayConverter implements TypeConverter<java.sql.Array, String[]> {

  @Override
  public String[] decode(java.sql.Array raw, Connection conn) {
    if (raw == null)
      return null;
    try {
      Object[] o = (Object[]) raw.getArray();
      String[] value = Arrays.stream(o).toArray(String[]::new);
      return value;
    } catch (SQLException e) {
      throw new RuntimeException("Could not convert a database VARCHAR ARRAY into a String[].", e);
    }
  }

  @Override
  public java.sql.Array encode(String[] value, Connection conn) {
    if (value == null)
      return null;
    try {
      return conn.createArrayOf("CHARACTER VARYING ARRAY", value);
    } catch (SQLException e) {
      throw new RuntimeException("Could not convert String[" + value.length + "] to a VARCHAR ARRAY. The values are: "
          + Arrays.stream(value).collect(Collectors.joining(", ")));
    }
  }

}
