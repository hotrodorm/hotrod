package app.converters;

import org.hotrod.runtime.converter.TypeConverter;

public class StringBooleanConverter implements TypeConverter<String, Boolean> {

  private static final String FALSE = "";
  private static final String TRUE = "x";

  @Override
  public Boolean decode(final String value) {
    if (value == null) {
      return null;
    }
    return !FALSE.equals(value);
  }

  @Override
  public String encode(final Boolean value) {
    if (value == null) {
      return null;
    }
    return value ? TRUE : FALSE;
  }

}
