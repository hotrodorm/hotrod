package org.hotrod.dynamicsql.assembler;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;

public class DynamicSQL extends Sentence<DynamicSQL, DynamicSQL> {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(DynamicSQL.class.getName());

  public DynamicSQL() {
    super(DynamicExpressionFactoryConfig.getFactory(), null, null);
    super.setMe(this);
  }

  public DynamicSQL(DynamicExpressionFactory factory) {
    super(factory, null, null);
    super.setMe(this);
  }

  public Parameters newParameters() {
    return this.factory.newParameterContext();
  }

//  public IfSequence ifs(String test, Sentence sentence) {
//    IfSequence is = new IfSequence(this.factory);
//    IfSequence if_ = is.if_(test, sentence);
//    return if_;
//  }
//
//  public ChooseSentence choose() {
//    MainSentence s = new MainSentence(this.factory);
//    return new ChooseSentence(s);
//  }
//
//  public MainSentence literal(String txt) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.literal(txt);
//  }
//
//  public MainSentence literaln() {
//    MainSentence q = new MainSentence(this.factory);
//    return q.literaln();
//  }
//
//  public MainSentence literaln(String txt) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.literaln(txt);
//  }
//
//  public MainSentence parameter(String name) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.parameter(name);
//  }
//
//  public MainSentence parameterNullable(String name, int sqlType) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.parameterNullable(name, sqlType);
//  }
//
//  public MainSentence parameterInjection(String name) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.parameterInjection(name);
//  }
//
//  public MainSentence variable(String name) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.variable(name);
//  }

//  public Sentence if_(String test, Sentence sentence) {
//    Sentence q = new Sentence(this.factory);
//    return q.if_(test, sentence);
//  }

//  public MainSentence set(IfSequence ifSentence) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.set(ifSentence);
//  }
//
//  public MainSentence set(IfSequence ifSentence, String headerPrefix, String headerSuffix, String separatorPrefix,
//      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.set(ifSentence, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix, tailPrefix, tailSuffix,
//        removePrefixes);
//  }
//
//  public MainSentence where(String separator, IfSequence ifSentence) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.where(separator, ifSentence);
//  }
//
//  public MainSentence where(String separator, IfSequence ifSentence, String headerPrefix, String headerSuffix,
//      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.where(separator, ifSentence, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix, tailPrefix,
//        tailSuffix, removePrefixes);
//  }
//
//  public MainSentence trim(String header, String separator, String tail, IfSequence ifSentence) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.trim(header, separator, tail, ifSentence);
//  }
//
//  public MainSentence trim(String header, String separator, String tail, IfSequence ifSentence, String headerPrefix,
//      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
//      String... removePrefixes) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.trim(header, separator, tail, ifSentence, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix,
//        tailPrefix, tailSuffix, removePrefixes);
//  }
//
//  public MainSentence foreach(String item, String collection, String open, String separator, String close,
//      MainSentence sentence) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.foreach(item, collection, open, separator, close, sentence);
//  }
//
//  public MainSentence bind(String name, String value) {
//    MainSentence q = new MainSentence(this.factory);
//    return q.bind(name, value);
//  }

  public DynamicModificationQuery endModificationQuery() {
    return new DynamicModificationQuery(this.segments);
  }

  public DynamicSelectQuery endSelectQuery() {
    return new DynamicSelectQuery(this.segments);
  }

  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode) {
    return new DynamicInsertQuery(this.segments, primaryKeyRetrievalMode, null, null, null);
  }

  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode, String sequencePreFetchSQL,
      String primaryKeyParameterName, String... generatedKeysNames) {
    return new DynamicInsertQuery(this.segments, primaryKeyRetrievalMode, sequencePreFetchSQL, primaryKeyParameterName,
        generatedKeysNames);
  }


  
}
