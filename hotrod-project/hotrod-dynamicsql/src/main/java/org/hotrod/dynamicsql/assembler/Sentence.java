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

//  public T if_(String test, NestedSentence sentence) {
//    this.segments.add(new IfSegment(test, sentence, this.factory));
//    return me.cast(this);
//  }

  public IfSentence<M> if_(String test) {
    IfSentence<M> s = new IfSentence(this.factory, this, test);
    return s;
  }

  public ChooseSentence<M> choose() {
    ChooseSentence<M> s = new ChooseSentence(this.factory, this);
    return s;
  }

//  public GenericSentence set(IfSequence ifSentence) {
//    this.segments.add(new SettersSegment(ifSentence, this.factory));
//    return this;
//  }
//
//  public GenericSentence set(IfSequence ifSentence, String headerPrefix, String headerSuffix, String separatorPrefix,
//      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
//    this.segments.add(new SettersSegment(ifSentence, this.factory, headerPrefix, headerSuffix, separatorPrefix,
//        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }
//
//  public GenericSentence where(String separator, IfSequence ifSentence) {
//    this.segments.add(new WhereSegment(separator, ifSentence, this.factory));
//    return this;
//  }
//
//  public GenericSentence where(String separator, IfSequence ifSentence, String headerPrefix, String headerSuffix,
//      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
//    this.segments.add(new WhereSegment(separator, ifSentence, this.factory, headerPrefix, headerSuffix, separatorPrefix,
//        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }

  public TrimSentence<M> trim(String header, String separator, String tail) {
    TrimSentence<M> s = new TrimSentence(this.factory, this, header, separator, tail);
    return s;
  }

//  public GenericSentence trim(String header, String separator, String tail, IfSequence ifSentence, String headerPrefix,
//      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
//      String... removePrefixes) {
//    this.segments.add(new TrimSegment(header, separator, tail, ifSentence, this.factory, headerPrefix, headerSuffix,
//        separatorPrefix, separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }
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
