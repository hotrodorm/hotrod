package org.hotrod.config.dynamicsql;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;

public class LiteralTextPart extends DynamicSQLPart implements SQLChunk {

  // Constants

  private static final Logger log = Logger.getLogger(LiteralTextPart.class);

  // Properties

  private String text;

  // Constructor

  public LiteralTextPart(final String text) {
    super("not-a-tag-but-sql-content");
    log.info("init");
    this.text = text;
  }

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  @Override
  protected void specificBodyValidation(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No extra validation on the body
  }

  @Override
  public boolean isEmpty() {
    return this.text == null || this.text.trim().isEmpty();
  }

  public LiteralTextPart concat(final LiteralTextPart other) {
    return new LiteralTextPart(this.text + other.text);
  }

  // Rendering

  @Override
  protected boolean shouldRenderTag() {
    return false;
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    // log.info("[renderStatic] this.text=" + this.text);
    return this.text;
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    return this.text;
  }

  // Java Expression

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {
    return new LiteralExpression(this.text);
  }

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

}
