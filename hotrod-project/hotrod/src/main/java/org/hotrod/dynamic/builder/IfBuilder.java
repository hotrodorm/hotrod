package org.hotrod.dynamic.builder;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.segments.IfSegment;
import org.hotrod.dynamic.segments.QuerySegment;

public class IfBuilder {

  public static PartialIf create(String test, DynamicExpressionFactory factory) {
    return new PartialIf(test, factory);
  }

  public static class PartialIf {

    private String test;
    private DynamicExpressionFactory factory;
    private List<QuerySegment> segments = new ArrayList<>();

    public PartialIf(String test, DynamicExpressionFactory factory) {
      this.test = test;
      this.factory = factory;
    }

    public PartialIf add(QuerySegment s) {
      this.segments.add(s);
      return this;
    }

    public IfSegment end() {
      return new IfSegment(this.test, this.segments, this.factory);
    }

  }

}
