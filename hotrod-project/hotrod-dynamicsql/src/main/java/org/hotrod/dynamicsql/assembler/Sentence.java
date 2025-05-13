package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.ParameterInjectionSegment;
import org.hotrod.dynamicsql.segments.ParameterNotNullableSegment;
import org.hotrod.dynamicsql.segments.ParameterNullableSegment;
import org.hotrod.dynamicsql.segments.StaticContentSegment;
import org.hotrod.dynamicsql.segments.VariableSegment;

//public class IfSentence<P extends Sentence<?, ?>> extends Sentence<IfSentence<P>, P> {

public abstract class Sentence<M extends Sentence<?, ?>, P> extends AbstractSentence<M, P> {

  public Sentence(DynamicExpressionFactory factory, M me, P parent) {
    super(factory, me, parent);
  }

  // Segments

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
    this.segments.add(new ParameterNotNullableSegment(this.factory, name));
    return this.me;
  }

  public M parameterNullable(String name, int sqlType) {
    this.segments.add(new ParameterNullableSegment(this.factory, name, sqlType));
    return this.me;
  }

  public M parameterInjection(String name) {
    this.segments.add(new ParameterInjectionSegment(this.factory, name));
    return this.me;
  }

  public M variable(String name) {
    this.segments.add(new VariableSegment(this.factory, name));
    return this.me;
  }

  public IfSentence<M> if_(String test) {
    IfSentence<M> s = new IfSentence(this.factory, this, test);
    return s;
  }

  public ChooseSentence<M> choose() {
    ChooseSentence<M> s = new ChooseSentence(this.factory, this);
    return s;
  }

  public WhereSentence<M> where(String separator) {
    WhereSentence<M> s = new WhereSentence(this.factory, this, separator);
    return s;
  }

  public SetSentence<M> set() {
    SetSentence<M> s = new SetSentence(this.factory, this);
    return s;
  }

  public TrimSentence<M> trim(String header, String separator, String tail) {
    TrimSentence<M> s = new TrimSentence(this.factory, this, header, separator, tail);
    return s;
  }

  public TrimSentence<M> trim(String header, String separator, String tail, String headerPrefix, String headerSuffix,
      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    TrimSentence<M> s = new TrimSentence(this.factory, this, header, separator, tail, headerPrefix, headerSuffix,
        separatorPrefix, separatorSuffix, tailPrefix, tailSuffix, removePrefixes);
    return s;
  }
//
//  public GenericSentence foreach(String item, String collection, String open, String separator, String close,
//      GenericSentence content) {
//    this.segments.add(new ForEachSegment(item, collection, open, separator, close, content.segments, this.factory));
//    return this;
//  }
//
//  public GenericSentence bind(String name, String value) {
//    this.segments.add(new BindSegment(name, value, this.factory));
//    return this;
//  }

}
