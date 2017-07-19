package org.hotrod.config.dynamicsql;

import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

public interface SQLChunk {

  boolean isEmpty();

  String renderStatic(ParameterRenderer parameterRenderer);

  String renderXML(ParameterRenderer parameterRenderer);

  DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer) throws InvalidJavaExpressionException;

}
