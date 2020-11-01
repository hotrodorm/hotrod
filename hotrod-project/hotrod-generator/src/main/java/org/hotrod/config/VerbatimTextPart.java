package org.hotrod.config;

import org.hotrod.config.dynamicsql.SQLSegment;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrodorm.hotrod.utils.SUtil;

public class VerbatimTextPart implements SQLSegment {

  private static final long serialVersionUID = 1L;

  private SourceLocation location;
  private String content;

  public VerbatimTextPart(final SourceLocation location, final String content) {
    this.location = location;
    this.content = content;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  public SourceLocation getLocation() {
    return location;
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return this.content;
  }

  @Override
  public String renderXML(ParameterRenderer parameterRenderer) {
    return SUtil.escapeXmlBody(this.content);
  }

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    return null;
  }

}
