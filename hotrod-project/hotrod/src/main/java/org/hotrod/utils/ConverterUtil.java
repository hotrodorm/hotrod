package org.hotrod.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.hotrod.exceptions.PersistenceException;

public abstract class ConverterUtil {

  private ConverterUtil() {
  }

  public static Object encode(final Object domain, final TypeConverter<?, ?> converter, final Connection conn) {

    Method m;
    try {
      m = TypeConverter.class.getMethod("encode", Object.class, Connection.class);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new PersistenceException("Could not encode using converter", e);
    }

    Object raw;
    try {
      raw = m.invoke(converter, domain, conn);
    } catch (IllegalAccessException | IllegalArgumentException e) {
      throw new PersistenceException("Could not invoke converter's encode() method", e);
    } catch (InvocationTargetException e) {
      throw new PersistenceException("Converter's encode() method threw an exception", e);
    }

    return raw;

  }

}
