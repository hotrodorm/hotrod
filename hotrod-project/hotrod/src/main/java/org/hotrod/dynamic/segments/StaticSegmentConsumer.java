package org.hotrod.dynamic.segments;

public interface StaticSegmentConsumer {

  void startNextEntry();

  void consume(String literal);

  void consume(ParameterSegment s);

}
