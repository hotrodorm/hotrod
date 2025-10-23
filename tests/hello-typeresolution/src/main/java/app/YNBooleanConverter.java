package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public class YNBooleanConverter implements TypeConverter<String, Boolean> {

  private static String TRUE = "Y";
  private static String FALSE = "N";

  @Override
  public Boolean decode(String raw, Connection conn) {
    if (raw == null)
      return null;
    if (FALSE.equals(raw))
      return false;
    if (TRUE.equals(raw))
      return true;
    throw new RuntimeException("Could not convert raw-value '" + raw + "' to a Boolean value");
  }

  @Override
  public String encode(Boolean value, Connection conn) {
    if (value == null)
      return null;
    return value ? TRUE : FALSE;
  }

}
