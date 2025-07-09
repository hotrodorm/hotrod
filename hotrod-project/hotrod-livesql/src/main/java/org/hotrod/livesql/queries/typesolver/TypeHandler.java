package org.hotrod.livesql.queries.typesolver;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;

public class TypeHandler<R, D> {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TypeHandler.class.getName());

  public static final TypeHandler<String, String> STRING_ENTITY_COLUMN = TypeHandler.forClass(String.class,
      TypeSource.RUNTIME_DIALECT_RULE);

  private Class<D> javaClass;
  private Class<R> rawClass;
  private TypeConverter<R, D> converter;

  private TypeSource typeSource;
  private Integer ruleNumber;

  private TypeHandler(final Class<D> javaClass, final Class<R> rawClass, final TypeConverter<R, D> converter,
      final TypeSource typeSource, final Integer ruleNumber) {
    this.javaClass = javaClass;
    this.rawClass = rawClass;
    this.converter = converter;
    this.typeSource = typeSource;
    this.ruleNumber = ruleNumber;
  }

  public static <D> TypeHandler<D, D> forClass(final Class<D> javaClass, final TypeSource typeSource) {
    return new TypeHandler<D, D>(javaClass, null, null, typeSource, null);
  }

  @SuppressWarnings("unchecked")
  public static <R, D> TypeHandler<R, D> forConverter(final TypeConverter<R, D> converter,
      final TypeSource typeSource) {
    Class<R> raw = null;
    Class<D> domain = null;
    Method[] methods = converter.getClass().getDeclaredMethods();
    for (Method m : methods) {
      if ("decode".equals(m.getName()) && (domain == null || domain.equals(Object.class))) {
        domain = (Class<D>) m.getReturnType();
      }
      if ("encode".equals(m.getName()) && (raw == null || raw.equals(Object.class))) {
        raw = (Class<R>) m.getReturnType();
      }
    }
    TypeHandler<R, D> th = new TypeHandler<R, D>(domain, raw, converter, typeSource, null);
    return th;
  }

  public Class<D> getJavaClass() {
    return javaClass;
  }

  public Class<R> getRawClass() {
    return rawClass;
  }

  public TypeConverter<R, D> getConverter() {
    return converter;
  }

  public TypeSource getTypeSource() {
    return typeSource;
  }

  public Integer getRuleNumber() {
    return ruleNumber;
  }

  protected String render() {
    return (this.converter == null ? "" + this.javaClass
        : "[" + this.rawClass + " -> " + this.converter.getClass() + " -> " + this.javaClass + "]") + ", source: "
        + this.typeSource + (this.ruleNumber == null ? "" : ":" + this.ruleNumber);
  }

  public String toString() {
    return this.render();
  }

}
