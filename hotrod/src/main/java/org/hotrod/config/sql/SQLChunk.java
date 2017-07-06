package org.hotrod.config.sql;

import org.hotrod.generator.ParameterRenderer;

public interface SQLChunk {

  String renderSQLSentence(ParameterRenderer parameterRenderer);

  String renderAugmentedSQL();

}
