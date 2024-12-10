package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public abstract class DynamicQuery {

  protected List<QuerySegment> segments;

  public DynamicQuery(List<QuerySegment> segments) {
    this.segments = segments;
  }

}
