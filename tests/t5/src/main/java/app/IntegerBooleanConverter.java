package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public class IntegerBooleanConverter implements TypeConverter<Integer, Boolean> {

  @Override
  public Boolean decode(Integer raw, Connection conn) {
    if (raw == null) {
      return null;
    }
    return !raw.equals(0);
  }

  @Override
  public Integer encode(Boolean domain, Connection conn) {
    return domain ? 1 : 0;
  }

}
