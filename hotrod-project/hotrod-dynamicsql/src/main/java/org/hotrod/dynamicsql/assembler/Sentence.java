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
import org.hotrod.dynamicsql.segments.IfSegment;
import org.hotrod.dynamicsql.segments.ParameterInjectionSegment;
import org.hotrod.dynamicsql.segments.ParameterNotNullableSegment;
import org.hotrod.dynamicsql.segments.ParameterNullableSegment;
import org.hotrod.dynamicsql.segments.QuerySegment;
import org.hotrod.dynamicsql.segments.SettersSegment;
import org.hotrod.dynamicsql.segments.StaticContentSegment;
import org.hotrod.dynamicsql.segments.TrimSegment;
import org.hotrod.dynamicsql.segments.VariableSegment;
import org.hotrod.dynamicsql.segments.WhereSegment;

public class Sentence {

  DynamicExpressionFactory factory;
  List<QuerySegment> segments = new ArrayList<>();

  public Sentence(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // Segments

  public Sentence literal(String text) {
    this.segments.add(new StaticContentSegment(text));
    return this;
  }

  public Sentence literaln() {
    return this.literal("\n");
  }

  public Sentence literaln(String text) {
    return this.literal(text).literaln();
  }

  public Sentence parameter(String name) {
    this.segments.add(new ParameterNotNullableSegment(this.factory, name));
    return this;
  }

  public Sentence parameterNullable(String name, int sqlType) {
    this.segments.add(new ParameterNullableSegment(this.factory, name, sqlType));
    return this;
  }

  public Sentence parameterInjection(String name) {
    this.segments.add(new ParameterInjectionSegment(this.factory, name));
    return this;
  }

  public Sentence variable(String name) {
    this.segments.add(new VariableSegment(this.factory, name));
    return this;
  }

  public Sentence if_(String test, Sentence sentence) {
    this.segments.add(new IfSegment(test, sentence, this.factory));
    return this;
  }

  public Sentence set(IfSentence ifSentence) {
    this.segments.add(new SettersSegment(ifSentence, this.factory));
    return this;
  }

  public Sentence set(IfSentence ifSentence, String headerPrefix, String headerSuffix, String separatorPrefix,
      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    this.segments.add(new SettersSegment(ifSentence, this.factory, headerPrefix, headerSuffix, separatorPrefix,
        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public Sentence where(String separator, IfSentence ifSentence) {
    this.segments.add(new WhereSegment(separator, ifSentence, this.factory));
    return this;
  }

  public Sentence where(String separator, IfSentence ifSentence, String headerPrefix, String headerSuffix,
      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    this.segments.add(new WhereSegment(separator, ifSentence, this.factory, headerPrefix, headerSuffix, separatorPrefix,
        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public ChooseAssembler choose() {
    return new ChooseAssembler(this);
  }

  public Sentence trim(String header, String separator, String tail, IfSentence ifSentence) {
    this.segments.add(new TrimSegment(header, separator, tail, ifSentence, this.factory));
    return this;
  }

  public Sentence trim(String header, String separator, String tail, IfSentence ifSentence, String headerPrefix,
      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
      String... removePrefixes) {
    this.segments.add(new TrimSegment(header, separator, tail, ifSentence, this.factory, headerPrefix, headerSuffix,
        separatorPrefix, separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public Sentence foreach(String item, String collection, String open, String separator, String close,
      Sentence content) {
    this.segments.add(new ForEachSegment(item, collection, open, separator, close, content.segments, this.factory));
    return this;
  }

  public Sentence bind(String name, String value) {
    this.segments.add(new BindSegment(name, value, this.factory));
    return this;
  }

  // end

//  public SegmentList end() {
//    return new SegmentList(this.segments);
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
