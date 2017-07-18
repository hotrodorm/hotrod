package org.hotrod.config.dynamicsql;

import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;

public interface SQLChunk {

  boolean isEmpty();

  String renderStatic(ParameterRenderer parameterRenderer);

  String renderXML(ParameterRenderer parameterRenderer);

  DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer);

}
