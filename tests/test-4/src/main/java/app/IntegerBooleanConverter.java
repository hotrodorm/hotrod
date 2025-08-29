package app;

import java.sql.Connection;
import java.sql.SQLException;

import org.hotrod.runtime.converter.TypeConverter;

public class IntegerBooleanConverter implements TypeConverter<Integer, Boolean> {

  @Override
  public Boolean decode(Integer raw, Connection conn) throws SQLException {
    if (raw == null) {
      return null;
    }
    return !raw.equals(0);
  }

  @Override
  public Integer encode(Boolean domain, Connection conn) throws SQLException {
    return domain ? 1 : 0;
  }

}
