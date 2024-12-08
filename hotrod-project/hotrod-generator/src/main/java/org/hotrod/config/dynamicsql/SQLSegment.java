package org.hotrod.config.dynamicsql;

import java.io.Serializable;

import org.hotrod.dynamicsql.existing.expressions.OldDynamicExpression;
import org.hotrod.exceptions.InvalidJavaExpressionException;
import org.hotrod.generator.ParameterRenderer;

public interface SQLSegment extends Serializable {

  public abstract boolean isEmpty();

  public abstract String renderStatic(ParameterRenderer parameterRenderer);

  public abstract String renderXML(ParameterRenderer parameterRenderer);

  public abstract OldDynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException;

}
