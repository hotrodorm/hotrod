package org.hotrod.config.dynamicsql;

import org.hotrod.generator.ParameterRenderer;

public interface SQLChunk {

  String renderSQLSentence(ParameterRenderer parameterRenderer);

}
