package org.hotrod.dynamicsql;

import java.util.List;

import org.hotrod.dynamicsql.segments.QuerySegment;

public abstract class DynamicQuery {

  protected List<QuerySegment> segments;

  public DynamicQuery(List<QuerySegment> segments) {
    this.segments = segments;
  }

}
