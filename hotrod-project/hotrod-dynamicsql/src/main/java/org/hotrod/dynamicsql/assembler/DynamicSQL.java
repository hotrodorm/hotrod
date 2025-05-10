package org.hotrod.dynamicsql.assembler;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.Parameters;

public class DynamicSQL {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(DynamicSQL.class.getName());

  private DynamicExpressionFactory factory;

  public DynamicSQL() {
    this.factory = DynamicExpressionFactoryConfig.getFactory();
  }

  public DynamicSQL(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  public Parameters newParameters() {
    return this.factory.newParameterContext();
  }

  public IfSentence ifs(String test, Sentence sentence) {
    IfSentence is = new IfSentence(this.factory);
    IfSentence if_ = is.if_(test, sentence);
    return if_;
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

  public Sentence set(IfSentence ifSentence) {
    Sentence q = new Sentence(this.factory);
    return q.set(ifSentence);
  }

  public Sentence set(IfSentence ifSentence, String headerPrefix, String headerSuffix, String separatorPrefix,
      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    Sentence q = new Sentence(this.factory);
    return q.set(ifSentence, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix, tailPrefix, tailSuffix,
        removePrefixes);
  }

  public Sentence where(String separator, IfSentence ifSentence) {
    Sentence q = new Sentence(this.factory);
    return q.where(separator, ifSentence);
  }

  public Sentence where(String separator, IfSentence ifSentence, String headerPrefix, String headerSuffix,
      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    Sentence q = new Sentence(this.factory);
    return q.where(separator, ifSentence, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix, tailPrefix,
        tailSuffix, removePrefixes);
  }

  public Sentence trim(String header, String separator, String tail, IfSentence ifSentence) {
    Sentence q = new Sentence(this.factory);
    return q.trim(header, separator, tail, ifSentence);
  }

  public Sentence trim(String header, String separator, String tail, IfSentence ifSentence, String headerPrefix,
      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
      String... removePrefixes) {
    Sentence q = new Sentence(this.factory);
    return q.trim(header, separator, tail, ifSentence, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix,
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
