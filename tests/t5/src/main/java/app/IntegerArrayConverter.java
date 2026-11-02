package app;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import org.hotrod.converter.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public class IntegerArrayConverter implements TypeConverter<java.sql.Array, Integer[]> {

  @Override
  public Integer[] decode(Array raw, Connection conn) {
    if (conn == null) {
      throw new RuntimeException("could not decode; connection is null");
    }
    if (raw == null)
      return null;
    try {
      return Arrays.stream((Object[]) raw.getArray()).map(o -> (Integer) o).toArray(Integer[]::new);
    } catch (SQLException e) {
      throw new RuntimeException("Could not decode integer array; error while reading array from database", e);
    }
  }

  @Override
  public Array encode(Integer[] domain, Connection conn) {
    if (conn == null) {
      throw new RuntimeException("Could not encode; connection is null");
    }
    if (domain == null)
      return null;
    try {
      return conn.createArrayOf("integer", domain);
    } catch (SQLException e) {
      throw new RuntimeException("Could not encode integer array; error while creating array for database", e);
    }
  }

}
