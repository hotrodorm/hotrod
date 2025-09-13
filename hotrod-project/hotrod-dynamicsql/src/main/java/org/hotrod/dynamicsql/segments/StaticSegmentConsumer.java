package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.parameters.ParameterOccurrence;

public interface StaticSegmentConsumer {

  void startNextEntry();

  void consume(String literal);

  void consume(ParameterOccurrence s);

}
