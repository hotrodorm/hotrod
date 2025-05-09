package org.hotrod.dynamicsql.assembler;

import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.ParameterContext;
import org.hotrod.dynamicsql.segments.IfSegment;

public class DynamicSQL {

  private DynamicExpressionFactory factory;

  public DynamicSQL() {
    this.factory = DynamicExpressionFactoryConfig.getFactory();
  }

  public DynamicSQL(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  public ParameterContext newParameterContext() {
    return this.factory.newParameterContext();
  }

  public IfsAssembler ifs() {
    return new IfsAssembler(this.factory);
  }

  public ChooseAssembler choose() {
    Sentence s = new Sentence(this.factory);
    return new ChooseAssembler(s);
  }

  public Sentence literal(String txt) {
    Sentence q = new Sentence(this.factory);
    return q.literal(txt);
  }

  public Sentence literaln() {
    Sentence q = new Sentence(this.factory);
    return q.literaln();
  }

  public Sentence literaln(String txt) {
    Sentence q = new Sentence(this.factory);
    return q.literaln(txt);
  }

  public Sentence parameter(String name) {
    Sentence q = new Sentence(this.factory);
    return q.parameter(name);
  }

  public Sentence parameterNullable(String name, int sqlType) {
    Sentence q = new Sentence(this.factory);
    return q.parameterNullable(name, sqlType);
  }

  public Sentence parameterInjection(String name) {
    Sentence q = new Sentence(this.factory);
    return q.parameterInjection(name);
  }

  public Sentence variable(String name) {
    Sentence q = new Sentence(this.factory);
    return q.variable(name);
  }

  public Sentence if_(String test, Sentence sentence) {
    Sentence q = new Sentence(this.factory);
    return q.if_(test, sentence);
  }

  public Sentence set(List<IfSegment> ifSegments) {
    Sentence q = new Sentence(this.factory);
    return q.set(ifSegments);
  }

  public Sentence set(List<IfSegment> ifSegments, String headerPrefix, String headerSuffix, String separatorPrefix,
      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    Sentence q = new Sentence(this.factory);
    return q.set(ifSegments, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix, tailPrefix, tailSuffix,
        removePrefixes);
  }

  public Sentence where(String separator, List<IfSegment> ifSegments) {
    Sentence q = new Sentence(this.factory);
    return q.where(separator, ifSegments);
  }

  public Sentence where(String separator, List<IfSegment> ifSegments, String headerPrefix, String headerSuffix,
      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    Sentence q = new Sentence(this.factory);
    return q.where(separator, ifSegments, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix, tailPrefix,
        tailSuffix, removePrefixes);
  }

  public Sentence trim(String header, String separator, String tail, List<IfSegment> ifSegments) {
    Sentence q = new Sentence(this.factory);
    return q.trim(header, separator, tail, ifSegments);
  }

  public Sentence trim(String header, String separator, String tail, List<IfSegment> ifSegments, String headerPrefix,
      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
      String... removePrefixes) {
    Sentence q = new Sentence(this.factory);
    return q.trim(header, separator, tail, ifSegments, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix,
        tailPrefix, tailSuffix, removePrefixes);
  }

  public Sentence foreach(String item, String collection, String open, String separator, String close,
      Sentence sentence) {
    Sentence q = new Sentence(this.factory);
    return q.foreach(item, collection, open, separator, close, sentence);
  }

  public Sentence bind(String name, String value) {
    Sentence q = new Sentence(this.factory);
    return q.bind(name, value);
  }

}
