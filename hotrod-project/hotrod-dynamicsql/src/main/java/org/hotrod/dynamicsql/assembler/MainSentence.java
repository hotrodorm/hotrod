package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;
import org.hotrod.dynamicsql.segments.BindSegment;
import org.hotrod.dynamicsql.segments.ForEachSegment;
import org.hotrod.dynamicsql.segments.QuerySegment;
import org.hotrod.dynamicsql.segments.SettersSegment;
import org.hotrod.dynamicsql.segments.TrimSegment;
import org.hotrod.dynamicsql.segments.WhereSegment;

@Deprecated
public class MainSentence extends Sentence<MainSentence, MainSentence> {

  DynamicExpressionFactory factory;
  List<QuerySegment> segments = new ArrayList<>();

  public MainSentence(DynamicExpressionFactory factory) {
    super(factory, null, null);
    super.setMe(this);
  }

  // Segments

//  public Sentence literal(String text) {
//    this.segments.add(new StaticContentSegment(text));
//    return this;
//  }
//
//  public Sentence literaln() {
//    return this.literal("\n");
//  }
//
//  public Sentence literaln(String text) {
//    return this.literal(text).literaln();
//  }
//
//  public Sentence parameter(String name) {
//    this.segments.add(new ParameterNotNullableSegment(this.factory, name));
//    return this;
//  }
//
//  public Sentence parameterNullable(String name, int sqlType) {
//    this.segments.add(new ParameterNullableSegment(this.factory, name, sqlType));
//    return this;
//  }
//
//  public Sentence parameterInjection(String name) {
//    this.segments.add(new ParameterInjectionSegment(this.factory, name));
//    return this;
//  }
//
//  public Sentence variable(String name) {
//    this.segments.add(new VariableSegment(this.factory, name));
//    return this;
//  }

//  public Sentence if_(String test, Sentence sentence) {
//    this.segments.add(new IfSegment(test, sentence, this.factory));
//    return this;
//  }

//  public MainSentence set(IfSequence ifSentence) {
//    this.segments.add(new SettersSegment(ifSentence, this.factory));
//    return this;
//  }
//
//  public MainSentence set(IfSequence ifSentence, String headerPrefix, String headerSuffix, String separatorPrefix,
//      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
//    this.segments.add(new SettersSegment(ifSentence, this.factory, headerPrefix, headerSuffix, separatorPrefix,
//        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }
//
//  public MainSentence where(String separator, IfSequence ifSentence) {
//    this.segments.add(new WhereSegment(separator, ifSentence, this.factory));
//    return this;
//  }
//
//  public MainSentence where(String separator, IfSequence ifSentence, String headerPrefix, String headerSuffix,
//      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
//    this.segments.add(new WhereSegment(separator, ifSentence, this.factory, headerPrefix, headerSuffix, separatorPrefix,
//        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }
//
//  public ChooseSentence choose() {
//    return new ChooseSentence(this);
//  }
//
//  public MainSentence trim(String header, String separator, String tail, IfSequence ifSentence) {
//    this.segments.add(new TrimSegment(header, separator, tail, ifSentence, this.factory));
//    return this;
//  }
//
//  public MainSentence trim(String header, String separator, String tail, IfSequence ifSentence, String headerPrefix,
//      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
//      String... removePrefixes) {
//    this.segments.add(new TrimSegment(header, separator, tail, ifSentence, this.factory, headerPrefix, headerSuffix,
//        separatorPrefix, separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }
//
//  public MainSentence foreach(String item, String collection, String open, String separator, String close,
//      MainSentence content) {
//    this.segments.add(new ForEachSegment(item, collection, open, separator, close, content.segments, this.factory));
//    return this;
//  }
//
//  public MainSentence bind(String name, String value) {
//    this.segments.add(new BindSegment(name, value, this.factory));
//    return this;
//  }
//
//  // end
//
//  public DynamicModificationQuery endModificationQuery() {
//    return new DynamicModificationQuery(this.segments);
//  }
//
//  public DynamicSelectQuery endSelectQuery() {
//    return new DynamicSelectQuery(this.segments);
//  }
//
//  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode) {
//    return new DynamicInsertQuery(this.segments, primaryKeyRetrievalMode, null, null, null);
//  }
//
//  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode, String sequencePreFetchSQL,
//      String primaryKeyParameterName, String... generatedKeysNames) {
//    return new DynamicInsertQuery(this.segments, primaryKeyRetrievalMode, sequencePreFetchSQL, primaryKeyParameterName,
//        generatedKeysNames);
//  }

}
