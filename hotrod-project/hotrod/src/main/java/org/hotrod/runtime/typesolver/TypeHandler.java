package org.hotrod.runtime.typesolver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.runtime.converter.TypeConverter;

public class TypeHandler {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TypeHandler.class.getName());

  public static final TypeHandler STRING_TYPE_HANDLER = new TypeHandler(String.class, null, null);

  private static Map<Class<?>, TypeHandler> KNOWN_HANDLERS = new HashMap<>();

  private Class<?> javaClass;
  private Class<?> rawClass;
  private TypeConverter<?, ?> converter;

  private TypeHandler(final Class<?> javaClass, final Class<?> rawClass, final TypeConverter<?, ?> converter) {
    this.javaClass = javaClass;
    this.rawClass = rawClass;
    this.converter = converter;
  }

  public static TypeHandler of(final Class<?> javaClass) {
    if (TypeConverter.class.isAssignableFrom(javaClass)) {
      @SuppressWarnings("unchecked")
      Class<TypeConverter<?, ?>> c = (Class<TypeConverter<?, ?>>) javaClass;
      return discoverHandler(c);
    } else {
      return new TypeHandler(javaClass, null, null);
    }
  }

  public static TypeHandler of(final Class<?> javaClass, final Class<?> rawClass, final TypeConverter<?, ?> converter) {
    return new TypeHandler(javaClass, rawClass, converter);
  }

  private static TypeHandler discoverHandler(final Class<TypeConverter<?, ?>> converter) {
    TypeHandler th = KNOWN_HANDLERS.get(converter);
    if (th == null) {
      Class<?> raw = null;
      Class<?> domain = null;
      Method[] methods = converter.getDeclaredMethods();
      for (Method m : methods) {
        if ("decode".equals(m.getName()) && (domain == null || domain.equals(Object.class))) {
          domain = m.getReturnType();
        }
        if ("encode".equals(m.getName()) && (raw == null || raw.equals(Object.class))) {
          raw = m.getReturnType();
        }
      }
      try {
        th = new TypeHandler(domain, raw, converter.newInstance());
      } catch (InstantiationException | IllegalAccessException e) {
        throw new RuntimeException(
            "Could not instantiate converter of class '" + converter + "' with default constructor", e);
      }
      KNOWN_HANDLERS.put(converter, th);
    }
    return th;
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
    return this.converter == null ? "" + this.javaClass
        : "[" + this.rawClass + " -> " + this.converter.getClass() + " -> " + this.javaClass + "]";
  }

}
