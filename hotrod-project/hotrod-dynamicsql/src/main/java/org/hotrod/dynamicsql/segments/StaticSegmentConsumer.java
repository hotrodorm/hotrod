package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.parameters.ParameterInstance;

public interface StaticSegmentConsumer {

  void startNextEntry();

  void consume(String literal);

  void consume(ParameterInstance s);

}
