package org.hotrod.config.dynamicsql;

import org.apache.log4j.Logger;
import org.hotrod.config.SelectTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.VerbatimExpression;

public class LiteralTextPart extends DynamicSQLPart implements SQLChunk {

  // Constants

  private static final Logger log = Logger.getLogger(SelectTag.class);

  // Properties

  private String text;

  // Constructor

  public LiteralTextPart(final String text) {
    super("not-a-tag-but-sql-content");
    this.text = text;
    // log.info("[constructor] text=" + text);
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
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {
    return new VerbatimExpression(this.text);
  }

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

}
