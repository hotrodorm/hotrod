package org.hotrod.dynamicsql.assembler;

import org.hotrod.converter.TypeConverter;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.BindSegment;
import org.hotrod.dynamicsql.segments.ParameterInjectionSegment;
import org.hotrod.dynamicsql.segments.ParameterNotNullableSegment;
import org.hotrod.dynamicsql.segments.ParameterNotNullableUpdatableSegment;
import org.hotrod.dynamicsql.segments.ParameterNullableSegment;
import org.hotrod.dynamicsql.segments.StaticContentSegment;
import org.hotrod.dynamicsql.segments.VariableSegment;

public abstract class Sentence<M extends Sentence<?, ?>, P> extends AbstractSentence<M, P> {

  protected Sentence(DynamicExpressionFactory factory, M me, P parent) {
    super(factory, me, parent);
  }

  public M literal(String text) {
    this.segments.add(new StaticContentSegment(text));
    return this.me;
  }

  public M literaln() {
    return this.literal("\n");
  }

  public M literaln(String text) {
    return this.literal(text + "\n");
  }

  public M parameter(String name) {
    this.segments.add(new ParameterNotNullableSegment(this.factory, name, null));
    return this.me;
  }

  public M parameter(String name, TypeConverter<?, ?> converter) {
    this.segments.add(new ParameterNotNullableSegment(this.factory, name, converter));
    return this.me;
  }

  public M parameterUpdatable(String name) {
    this.segments.add(new ParameterNotNullableUpdatableSegment(this.factory, name, null));
    return this.me;
  }

  public M parameterUpdatable(String name, TypeConverter<?, ?> converter) {
    this.segments.add(new ParameterNotNullableUpdatableSegment(this.factory, name, converter));
    return this.me;
  }

  public M parameterNullable(String name, int sqlType) {
    this.segments.add(new ParameterNullableSegment(this.factory, name, sqlType, null));
    return this.me;
  }

  public M parameterNullable(String name, int sqlType, TypeConverter<?, ?> converter) {
    this.segments.add(new ParameterNullableSegment(this.factory, name, sqlType, converter));
    return this.me;
  }

  public M parameterInjection(String name) {
    this.segments.add(new ParameterInjectionSegment(this.factory, name));
    return this.me;
  }

  public M variable(String name) {
    this.segments.add(new VariableSegment(this.factory, name, null));
    return this.me;
  }

  public M variable(String name, TypeConverter<?, ?> converter) {
    this.segments.add(new VariableSegment(this.factory, name, converter));
    return this.me;
  }

  @SuppressWarnings("unchecked")
  public Begin<M> begin() {
    @SuppressWarnings("rawtypes")
    Begin<M> s = new Begin(this.factory, this);
    return s;
  }

  @SuppressWarnings("unchecked")
  public If<M> if_(String test) {
    @SuppressWarnings("rawtypes")
    If<M> s = new If(this.factory, this, test);
    return s;
  }

  @SuppressWarnings("unchecked")
  public Choose<M> choose() {
    @SuppressWarnings("rawtypes")
    Choose<M> s = new Choose(this.factory, this);
    return s;
  }

  @SuppressWarnings("unchecked")
  public Where<M> where(String separator) {
    @SuppressWarnings("rawtypes")
    Where<M> s = new Where(this.factory, this, separator);
    return s;
  }

  @SuppressWarnings("unchecked")
  public Set<M> set() {
    @SuppressWarnings("rawtypes")
    Set<M> s = new Set(this.factory, this);
    return s;
  }

  @SuppressWarnings("unchecked")
  public Trim<M> trim(String header, String separator, String tail) {
    @SuppressWarnings("rawtypes")
    Trim<M> s = new Trim(this.factory, this, header, separator, tail);
    return s;
  }

  @SuppressWarnings("unchecked")
  public ForEach<M> foreach(String item, String collection, String open, String separator, String close) {
    @SuppressWarnings("rawtypes")
    ForEach<M> s = new ForEach(this.factory, this, item, collection, open, separator, close);
    return s;
  }

  public M bind(String name, String value) {
    BindSegment s = new BindSegment(name, value, this.factory);
    DynShield.addSegment(this, s);
    return this.me;
  }

}
