package app.converters;

import org.hotrod.runtime.converter.TypeConverter;

public class ShortBooleanConverter implements TypeConverter<Short, Boolean> {

  private static final Short FALSE = 0;
  private static final Short TRUE = 1;

  @Override
  public Boolean decode(final Short value) {
    if (value == null) {
      return null;
    }
    return !FALSE.equals(value);
  }

  @Override
  public Short encode(final Boolean value) {
    if (value == null) {
      return null;
    }
    return value ? TRUE : FALSE;
  }

}
