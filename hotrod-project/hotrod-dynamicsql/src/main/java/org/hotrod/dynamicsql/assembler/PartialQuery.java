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
import org.hotrod.dynamicsql.segments.SegmentList;
import org.hotrod.dynamicsql.segments.SettersSegment;
import org.hotrod.dynamicsql.segments.StaticContentSegment;
import org.hotrod.dynamicsql.segments.TrimSegment;
import org.hotrod.dynamicsql.segments.VariableSegment;
import org.hotrod.dynamicsql.segments.WhereSegment;

public class PartialQuery {

  private DynamicExpressionFactory factory;
  List<QuerySegment> segments = new ArrayList<>();

  public PartialQuery(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // Segments

  public PartialQuery literal(String text) {
    this.segments.add(new StaticContentSegment(text));
    return this;
  }

  public PartialQuery literaln() {
    return this.literal("\n");
  }

  public PartialQuery literaln(String text) {
    return this.literal(text).literaln();
  }

  public PartialQuery parameter(String name) {
    this.segments.add(new ParameterNotNullableSegment(this.factory, name));
    return this;
  }

  public PartialQuery parameterNullable(String name, int sqlType) {
    this.segments.add(new ParameterNullableSegment(this.factory, name, sqlType));
    return this;
  }

  public PartialQuery parameterInjection(String name) {
    this.segments.add(new ParameterInjectionSegment(this.factory, name));
    return this;
  }

  public PartialQuery variable(String name) {
    this.segments.add(new VariableSegment(this.factory, name));
    return this;
  }

  public PartialQuery if_(String test, SegmentList content) {
    this.segments.add(new IfSegment(test, content.getSegments(), this.factory));
    return this;
  }

  public PartialQuery set(List<IfSegment> content) {
    this.segments.add(new SettersSegment(content, this.factory));
    return this;
  }

  public PartialQuery set(List<IfSegment> content, String headerPrefix, String headerSuffix, String separatorPrefix,
      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    this.segments.add(new SettersSegment(content, this.factory, headerPrefix, headerSuffix, separatorPrefix,
        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public PartialQuery where(String separator, List<IfSegment> content) {
    this.segments.add(new WhereSegment(separator, content, this.factory));
    return this;
  }

  public PartialQuery where(String separator, List<IfSegment> content, String headerPrefix, String headerSuffix,
      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    this.segments.add(new WhereSegment(separator, content, this.factory, headerPrefix, headerSuffix, separatorPrefix,
        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public ChooseAssembler choose() {
    return new ChooseAssembler(this.factory, this);
  }

  public PartialQuery trim(String header, String separator, String tail, List<IfSegment> content) {
    this.segments.add(new TrimSegment(header, separator, tail, content, this.factory));
    return this;
  }

  public PartialQuery trim(String header, String separator, String tail, List<IfSegment> content, String headerPrefix,
      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
      String... removePrefixes) {
    this.segments.add(new TrimSegment(header, separator, tail, content, this.factory, headerPrefix, headerSuffix,
        separatorPrefix, separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public PartialQuery foreach(String item, String collection, String open, String separator, String close,
      SegmentList content) {
    this.segments.add(new ForEachSegment(item, collection, open, separator, close, content, this.factory));
    return this;
  }

  public PartialQuery bind(String name, String value) {
    this.segments.add(new BindSegment(name, value, this.factory));
    return this;
  }

  // end

  public SegmentList end() {
    return new SegmentList(this.segments);
  }

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
