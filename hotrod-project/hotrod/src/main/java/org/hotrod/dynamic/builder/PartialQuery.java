package org.hotrod.dynamic.builder;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicInsertQuery;
import org.hotrod.dynamic.DynamicModificationQuery;
import org.hotrod.dynamic.DynamicSelectQuery;
import org.hotrod.dynamic.segments.IfSegment;
import org.hotrod.dynamic.segments.LiteralSegment;
import org.hotrod.dynamic.segments.ParameterSegment;
import org.hotrod.dynamic.segments.QuerySegment;
import org.hotrod.dynamic.segments.SegmentList;
import org.hotrod.dynamic.segments.SettersSegment;
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
    this.segments.add(new ParameterSegment(this.factory, name, sqlType));
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

  public DynamicInsertQuery endInsertQuery() {
    return new DynamicInsertQuery(this.segments);
  }

}
