package org.hotrod.dynamic.builder;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicInsertQuery;
import org.hotrod.dynamic.DynamicModificationQuery;
import org.hotrod.dynamic.DynamicSelectQuery;
import org.hotrod.dynamic.insert.InsertProperties;
import org.hotrod.dynamic.insert.PrimaryKeyRetrievalMode;
import org.hotrod.dynamic.segments.BindSegment;
import org.hotrod.dynamic.segments.ChooseSegment;
import org.hotrod.dynamic.segments.ForEachSegment;
import org.hotrod.dynamic.segments.IfSegment;
import org.hotrod.dynamic.segments.LiteralSegment;
import org.hotrod.dynamic.segments.ParameterDefinitionSegment;
import org.hotrod.dynamic.segments.QuerySegment;
import org.hotrod.dynamic.segments.SegmentList;
import org.hotrod.dynamic.segments.SettersSegment;
import org.hotrod.dynamic.segments.TrimSegment;
import org.hotrod.dynamic.segments.WhereSegment;

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

  public PartialQuery parameter(String name, int sqlType) {
    this.segments.add(new ParameterDefinitionSegment(this.factory, name, sqlType));
    return this;
  }

  public PartialQuery ifPart(String test, SegmentList querySegments) {
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
      SegmentList segmentList) throws DynamicExpressionException {
    this.segments.add(new ForEachSegment(item, collection, open, separator, close, segmentList, this.factory));
    return this;
  }

  public PartialQuery bind(String name, String value) throws DynamicExpressionException {
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
    return new DynamicInsertQuery(this.segments, primaryKeyRetrievalMode, new InsertProperties());
  }

  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode,
      InsertProperties insertProperties) {
    return new DynamicInsertQuery(this.segments, primaryKeyRetrievalMode, insertProperties);
  }

}
