package org.hotrod.config;

import org.hotrod.generator.ParameterRenderer;

public interface SQLChunk {

  String renderSQLSentence(ParameterRenderer parameterRenderer);

  String renderAugmentedSQL();

}
