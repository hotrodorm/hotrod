package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public class DynamicModificationQuery extends DynamicQuery {

  public DynamicModificationQuery(List<QuerySegment> parts) {
    super(parts);
  }

}
