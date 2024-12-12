package org.hotrod.dynamic.builder;

import java.util.List;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.segments.ChooseSegment;
import org.hotrod.dynamic.segments.IfSegment;
import org.hotrod.dynamic.segments.OtherwiseSegment;
import org.hotrod.dynamic.segments.SegmentList;
import org.hotrod.dynamic.segments.WhenSegment;

public class QueryBuilder {

  private DynamicExpressionFactory factory;

  public QueryBuilder(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

//  public PartialQuery create() {
//    return new PartialQuery(this.factory);
//  }

  public IfsBuilder ifs() {
    return new IfsBuilder(this.factory);
  }

  public ChooseBuilder choose() {
    return new ChooseBuilder(this.factory);
  }

  public WhenSegment when(String test, SegmentList segmentList) {
    return new WhenSegment(test, segmentList.getSegments(), this.factory);
  }

  public OtherwiseSegment otherwise(SegmentList segmentList) {
    return new OtherwiseSegment(segmentList.getSegments(), this.factory);
  }

  // Handy methods instead of create()

  public PartialQuery literal(String txt) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.literal(txt);
  }

  public PartialQuery parameter(String name, int sqlType) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.parameter(name, sqlType);
  }

  public PartialQuery ifPart(String test, SegmentList querySegments) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.ifPart(test, querySegments);
  }

  public PartialQuery set(List<IfSegment> ifSegments) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.set(ifSegments);
  }

  public PartialQuery set(List<IfSegment> ifSegments, String headerPrefix, String headerSuffix, String separatorPrefix,
      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.set(ifSegments, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix, tailPrefix, tailSuffix,
        removePrefixes);
  }

  public PartialQuery where(String separator, List<IfSegment> ifSegments) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.where(separator, ifSegments);
  }

  public PartialQuery where(String separator, List<IfSegment> ifSegments, String headerPrefix, String headerSuffix,
      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.where(separator, ifSegments, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix, tailPrefix,
        tailSuffix, removePrefixes);
  }

  public PartialQuery choose(ChooseSegment choose) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.choose(choose);
  }

  public PartialQuery trim(String header, String separator, String tail, List<IfSegment> ifSegments) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.trim(header, separator, tail, ifSegments);
  }

  public PartialQuery trim(String header, String separator, String tail, List<IfSegment> ifSegments,
      String headerPrefix, String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix,
      String tailSuffix, String... removePrefixes) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.trim(header, separator, tail, ifSegments, headerPrefix, headerSuffix, separatorPrefix, separatorSuffix,
        tailPrefix, tailSuffix, removePrefixes);
  }

  public PartialQuery foreach(String item, String collection, String open, String separator, String close,
      SegmentList segmentList) throws DynamicExpressionException {
    PartialQuery q = new PartialQuery(this.factory);
    return q.foreach(item, collection, open, separator, close, segmentList);
  }

  public PartialQuery bind(String name, String value) throws DynamicExpressionException {
    PartialQuery q = new PartialQuery(this.factory);
    return q.bind(name, value);
  }

}
