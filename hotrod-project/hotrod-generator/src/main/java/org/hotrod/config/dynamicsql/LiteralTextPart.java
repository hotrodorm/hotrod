package org.hotrod.config.dynamicsql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrodorm.hotrod.utils.SUtil;

public class LiteralTextPart extends DynamicSQLPart implements SQLSegment {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(LiteralTextPart.class);

  // Properties

  private SourceLocation location;
  private String text;

  // Constructor

  public LiteralTextPart(final SourceLocation location, final String text) {
    super("not-a-tag-but-literal-text");
    log.debug("init");
    this.location = location;
    this.text = text;
  }

  // Behavior

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No extra validation on the body
  }

  public LiteralTextPart concat(final LiteralTextPart other) {
    return new LiteralTextPart(this.location, this.text + other.text);
  }

  // Rendering

  @Override
  protected void validateAttributes(ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // Nothing to validate. Not a tag.
  }

  @Override
  protected boolean shouldRenderTag() {
    return false;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    return null;
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return this.text;
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    return SUtil.escapeXmlBody(this.text);
  }

  // Java Expression

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new LiteralExpression(this.text);

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for literal text on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

  @Override
  public boolean isEmpty() {
    return this.text == null || this.text.trim().isEmpty();
  }

}
