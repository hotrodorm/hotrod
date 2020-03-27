package org.hotrod.config.dynamicsql;

import java.io.Serializable;

import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

public interface SQLSegment extends Serializable {

  public abstract boolean isEmpty();

  public abstract String renderStatic(ParameterRenderer parameterRenderer);

  public abstract String renderXML(ParameterRenderer parameterRenderer);

  public abstract DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException;

}
