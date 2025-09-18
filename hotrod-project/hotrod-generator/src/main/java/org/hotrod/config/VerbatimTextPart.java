package org.hotrod.config;

import org.hotrod.config.dynamicsql.SQLSegment;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.SourceLocation;

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
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    return this.content;
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return this.content;
  }

  public String getContent() {
    return content;
  }

}
