package org.hotrod.runtime.typesolver;

import org.hotrod.runtime.converter.TypeConverter;

public class TypeHandler {

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
