package org.hotrod.dynamic.builder;

import java.util.List;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.segments.ChooseSegment;
import org.hotrod.dynamic.segments.IfSegment;
import org.hotrod.dynamic.segments.OtherwiseSegment;
import org.hotrod.dynamic.segments.SegmentList;
import org.hotrod.dynamic.segments.WhenSegment;

public class QueryAssembler {

  private DynamicExpressionFactory factory;

  public QueryAssembler(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  public IfsAssembler ifs() {
    return new IfsAssembler(this.factory);
  }

  public ChooseAssembler choose() {
    return new ChooseAssembler(this.factory);
  }

  public WhenSegment when(String test, SegmentList segmentList) {
    return new WhenSegment(test, segmentList.getSegments(), this.factory);
  }

  public OtherwiseSegment otherwise(SegmentList segmentList) {
    return new OtherwiseSegment(segmentList.getSegments(), this.factory);
  }

  public PartialQuery literal(String txt) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.literal(txt);
  }

  public PartialQuery literaln() {
    PartialQuery q = new PartialQuery(this.factory);
    return q.literaln();
  }

  public PartialQuery literaln(String txt) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.literaln(txt);
  }

  public PartialQuery parameter(String name, int sqlType) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.parameter(name, sqlType);
  }

  public PartialQuery parameterInjection(String name) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.parameterInjection(name);
  }

  public PartialQuery variable(String name) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.variable(name);
  }

  public PartialQuery if_(String test, SegmentList querySegments) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.if_(test, querySegments);
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
      SegmentList segmentList) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.foreach(item, collection, open, separator, close, segmentList);
  }

  public PartialQuery bind(String name, String value) {
    PartialQuery q = new PartialQuery(this.factory);
    return q.bind(name, value);
  }

}
