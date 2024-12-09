package org.hotrod.dynamic.builder;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicModificationQuery;
import org.hotrod.dynamic.DynamicSelectQuery;
import org.hotrod.dynamic.segments.IfSegment;
import org.hotrod.dynamic.segments.LiteralSegment;
import org.hotrod.dynamic.segments.ParameterSegment;
import org.hotrod.dynamic.segments.QuerySegment;
import org.hotrod.dynamic.segments.QuerySegments;

public class PartialQuery {

  private DynamicExpressionFactory factory;
  private List<QuerySegment> parts = new ArrayList<>();

  public PartialQuery(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // Segments

  public PartialQuery literal(String txt) {
    this.parts.add(new LiteralSegment(txt));
    return this;
  }

  public PartialQuery parameter(String name, int sqlType) {
    this.parts.add(new ParameterSegment(name, sqlType));
    return this;
  }

  public PartialQuery ifSegment(String test, QuerySegments querySegments) {
    this.parts.add(new IfSegment(test, querySegments.getParts(), this.factory));
    return this;
  }

  // end

  public QuerySegments end() {
    return new QuerySegments(this.parts);
  }

  public DynamicModificationQuery endModificationQuery() {
    return new DynamicModificationQuery(this.parts);
  }

  public DynamicSelectQuery endSelectQuery() {
    return new DynamicSelectQuery(this.parts);
  }

}
