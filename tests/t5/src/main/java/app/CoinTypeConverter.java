package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;

public class CoinTypeConverter implements TypeConverter<String, Integer> {

  @Override
  public Integer decode(String raw, Connection conn) {
    if (raw == null) {
      return null;
    }
    return raw.charAt(0) - 'A';
  }

  @Override
  public String encode(Integer domain, Connection conn) {
    int x = 'A' + domain;
    return "" + (char) x;
  }

}
