package org.hotrod.dynamic.segments;

import java.util.ArrayList;
import java.util.List;

public class QuerySegments {

  private List<QuerySegment> parts = new ArrayList<>();

  public QuerySegments(List<QuerySegment> parts) {
    this.parts = parts;
  }

  public List<QuerySegment> getParts() {
    return parts;
  }
  

}
