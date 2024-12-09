package org.hotrod.dynamic.segments;

public interface StaticSegmentConsumer {

  void consume(String literal);

  void consume(ParameterSegment s);

}
