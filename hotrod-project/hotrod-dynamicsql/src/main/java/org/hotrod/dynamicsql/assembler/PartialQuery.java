package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;
import org.hotrod.dynamicsql.segments.BindSegment;
import org.hotrod.dynamicsql.segments.ChooseSegment;
import org.hotrod.dynamicsql.segments.ForEachSegment;
import org.hotrod.dynamicsql.segments.IfSegment;
import org.hotrod.dynamicsql.segments.LiteralSegment;
import org.hotrod.dynamicsql.segments.ParameterInjectionSegment;
import org.hotrod.dynamicsql.segments.ParameterOccurrenceSegment;
import org.hotrod.dynamicsql.segments.QuerySegment;
import org.hotrod.dynamicsql.segments.SegmentList;
import org.hotrod.dynamicsql.segments.SettersSegment;
import org.hotrod.dynamicsql.segments.TrimSegment;
import org.hotrod.dynamicsql.segments.VariableOccurrenceSegment;
import org.hotrod.dynamicsql.segments.WhereSegment;

public class PartialQuery {

  private DynamicExpressionFactory factory;
  private List<QuerySegment> segments = new ArrayList<>();

  public PartialQuery(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // Segments

  public PartialQuery literal(String txt) {
    this.segments.add(new LiteralSegment(txt));
    return this;
  }

  public PartialQuery literaln() {
    return this.literal("\n");
  }

  public PartialQuery literaln(String txt) {
    return this.literal(txt).literaln();
  }

  public PartialQuery parameter(String name, int sqlType) {
    this.segments.add(new ParameterOccurrenceSegment(this.factory, name, sqlType));
    return this;
  }

  public PartialQuery parameterInjection(String name) {
    this.segments.add(new ParameterInjectionSegment(this.factory, name));
    return this;
  }

  public PartialQuery variable(String name) {
    this.segments.add(new VariableOccurrenceSegment(this.factory, name));
    return this;
  }

  public PartialQuery if_(String test, SegmentList querySegments) {
    this.segments.add(new IfSegment(test, querySegments.getSegments(), this.factory));
    return this;
  }

  public PartialQuery set(List<IfSegment> ifSegments) {
    this.segments.add(new SettersSegment(ifSegments, this.factory));
    return this;
  }

  public PartialQuery set(List<IfSegment> ifSegments, String headerPrefix, String headerSuffix, String separatorPrefix,
      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    this.segments.add(new SettersSegment(ifSegments, this.factory, headerPrefix, headerSuffix, separatorPrefix,
        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public PartialQuery where(String separator, List<IfSegment> ifSegments) {
    this.segments.add(new WhereSegment(separator, ifSegments, this.factory));
    return this;
  }

  public PartialQuery where(String separator, List<IfSegment> ifSegments, String headerPrefix, String headerSuffix,
      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    this.segments.add(new WhereSegment(separator, ifSegments, this.factory, headerPrefix, headerSuffix, separatorPrefix,
        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public PartialQuery choose(ChooseSegment choose) {
    this.segments.add(choose);
    return this;
  }

  public PartialQuery trim(String header, String separator, String tail, List<IfSegment> ifSegments) {
    this.segments.add(new TrimSegment(header, separator, tail, ifSegments, this.factory));
    return this;
  }

  public PartialQuery trim(String header, String separator, String tail, List<IfSegment> ifSegments,
      String headerPrefix, String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix,
      String tailSuffix, String... removePrefixes) {
    this.segments.add(new TrimSegment(header, separator, tail, ifSegments, this.factory, headerPrefix, headerSuffix,
        separatorPrefix, separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
    return this;
  }

  public PartialQuery foreach(String item, String collection, String open, String separator, String close,
      SegmentList segmentList) {
    this.segments.add(new ForEachSegment(item, collection, open, separator, close, segmentList, this.factory));
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
