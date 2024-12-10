package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public abstract class DynamicQuery {

  protected List<QuerySegment> parts;

  public DynamicQuery(List<QuerySegment> parts) {
    this.parts = parts;
  }

}
