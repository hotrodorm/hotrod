package org.hotrod.config.dynamicsql;

import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

public interface SQLSegment {

  public abstract boolean isEmpty();

  public abstract String renderStatic(ParameterRenderer parameterRenderer);

  public abstract String renderXML(ParameterRenderer parameterRenderer);

  public abstract DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException;

}
