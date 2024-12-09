package org.hotrod.dynamic.segments;

import java.util.ArrayList;
import java.util.List;

public class SegmentList {

  private List<QuerySegment> segments = new ArrayList<>();

  public SegmentList(List<QuerySegment> segments) {
    this.segments = segments;
  }

  public List<QuerySegment> getSegments() {
    return segments;
  }

}
