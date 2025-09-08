package org.hotrod.dynamicsql.assembler;

import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.Parameters;

public class DynamicSQL {

  private static final Logger log = Logger.getLogger(DynamicSQL.class.getName());

  private DynamicExpressionFactory factory = null;

  public DynamicSQL() {
    log.fine("init");
    this.factory = DynamicExpressionFactoryConfig.getFactory();
  }

  public DynamicSQL(DynamicExpressionFactory factory) {
    log.fine("init");
    this.factory = factory;
  }

  public Parameters newParameters() {
    return this.factory.newParameterContext();
  }

  public Sequence literal(String text) {
    Sequence s = new Sequence(this.factory);
    return s.literal(text);
  }

  public Sequence literaln() {
    Sequence s = new Sequence(this.factory);
    return s.literal("\n");
  }

  public Sequence literaln(String text) {
    Sequence s = new Sequence(this.factory);
    return s.literal(text + "\n");
  }

  public Sequence parameter(String name) {
    Sequence s = new Sequence(this.factory);
    return s.parameter(name);
  }

  public Sequence parameter(String name, TypeConverter<?, ?> converter) {
    Sequence s = new Sequence(this.factory);
    return s.parameter(name, converter);
  }

  public Sequence parameterNullable(String name, int sqlType) {
    Sequence s = new Sequence(this.factory);
    return s.parameterNullable(name, sqlType);
  }

  public Sequence parameterNullable(String name, int sqlType, TypeConverter<?, ?> converter) {
    Sequence s = new Sequence(this.factory);
    return s.parameterNullable(name, sqlType, converter);
  }

  public Sequence parameterInjection(String name) {
    Sequence s = new Sequence(this.factory);
    return s.parameterInjection(name);
  }

  public Sequence variable(String name) {
    Sequence s = new Sequence(this.factory);
    return s.variable(name);
  }

  public Sequence variable(String name, TypeConverter<?, ?> converter) {
    Sequence s = new Sequence(this.factory);
    return s.variable(name, converter);
  }

  public If<Sequence> if_(String test) {
    Sequence s = new Sequence(this.factory);
    return s.if_(test);
  }

  public Choose<Sequence> choose() {
    Sequence s = new Sequence(this.factory);
    return s.choose();
  }

  public Where<Sequence> where(String separator) {
    Sequence s = new Sequence(this.factory);
    return s.where(separator);
  }

  public Set<Sequence> set() {
    Sequence s = new Sequence(this.factory);
    return s.set();
  }

  public Trim<Sequence> trim(String header, String separator, String tail) {
    Sequence s = new Sequence(this.factory);
    return s.trim(header, separator, tail);
  }

  public ForEach<Sequence> foreach(String item, String collection, String open, String separator, String close) {
    Sequence s = new Sequence(this.factory);
    return s.foreach(item, collection, open, separator, close);
  }

  public Sequence bind(String name, String value) {
    Sequence s = new Sequence(this.factory);
    return s.bind(name, value);
  }

}
