package org.hotrod.config.dynamicsql;

import org.hotrod.generator.ParameterRenderer;

public interface SQLChunk {

  boolean isEmpty();

  String renderStatic(ParameterRenderer parameterRenderer);

  String renderXML(ParameterRenderer parameterRenderer);

}
