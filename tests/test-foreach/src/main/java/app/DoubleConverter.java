package app;

import java.sql.Connection;

import org.hotrod.runtime.converter.TypeConverter;

public class DoubleConverter implements TypeConverter<Integer, Double> {

  @Override
  public Double decode(Integer raw, Connection conn) {
    System.out.println("Connection=" + conn);
    return raw == null ? null : 1.0 * raw;
  }

  @Override
  public Integer encode(Double value, Connection conn) {
    return value == null ? null : value.intValue();
  }

}
