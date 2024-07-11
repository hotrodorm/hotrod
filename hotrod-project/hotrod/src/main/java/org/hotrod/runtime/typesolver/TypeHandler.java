package org.hotrod.runtime.typesolver;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.logging.Logger;

import org.hotrod.runtime.converter.TypeConverter;

public class TypeHandler {

  private static final Logger log = Logger.getLogger(TypeHandler.class.getName());

  public static final TypeHandler STRING_TYPE_HANDLER = new TypeHandler(String.class, null, null);

  private Class<?> javaClass;
  private Class<?> rawClass;
  private TypeConverter<?, ?> converter;

  private TypeHandler(final Class<?> javaClass, final Class<?> rawClass, final TypeConverter<?, ?> converter) {
    this.javaClass = javaClass;
    this.rawClass = rawClass;
    this.converter = converter;
  }

  public static TypeHandler of(final Class<?> javaClass) {
    return new TypeHandler(javaClass, null, null);
  }

  public static TypeHandler of(final Class<?> javaClass, final Class<?> rawClass, final TypeConverter<?, ?> converter) {
    return new TypeHandler(javaClass, rawClass, converter);
  }

  public static TypeHandler of(final TypeConverter<?, ?> converter) {
    Class<?> raw = null;
    Class<?> domain = null;
    Method[] methods = converter.getClass().getDeclaredMethods();
    for (Method m : methods) {
      if ("decode".equals(m.getName()) && (domain == null || domain.equals(Object.class))) {
        domain = m.getReturnType();
      }
      if ("encode".equals(m.getName()) && (raw == null || raw.equals(Object.class))) {
        raw = m.getReturnType();
      }
    }
    return new TypeHandler(domain, raw, converter);
  }

  public Class<?> getJavaClass() {
    return javaClass;
  }

  public Class<?> getRawClass() {
    return rawClass;
  }

  public TypeConverter<?, ?> getConverter() {
    return converter;
  }

  protected String render() {
    return this.converter == null ? this.javaClass.getName()
        : "[" + this.rawClass.getName() + " -> " + this.converter.getClass().getName() + " -> "
            + this.javaClass.getName() + "]";
  }

}
