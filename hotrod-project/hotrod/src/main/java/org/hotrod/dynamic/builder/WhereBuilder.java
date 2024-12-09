package org.hotrod.dynamic.builder;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.segments.IfSegment;
import org.hotrod.dynamic.segments.QuerySegment;

public class WhereBuilder {

  public static PartialWhere create(DynamicExpressionFactory factory) {
    return new PartialWhere(null, factory);
  }

  public static PartialWhere create(String delimiter, DynamicExpressionFactory factory) {
    return new PartialWhere(delimiter, factory);
  }

  public static class PartialWhere {

    private String delimiter;
    private DynamicExpressionFactory factory;
    private List<QuerySegment> segments = new ArrayList<>();

    public PartialWhere(String delimiter, DynamicExpressionFactory factory) {
      this.delimiter = delimiter;
      this.factory = factory;
    }

    public PartialWhere add(QuerySegment s) {
      this.segments.add(s);
      return this;
    }

    public IfSegment end() {
      return new IfSegment(this.delimiter, this.segments, this.factory);
    }

  }

}
