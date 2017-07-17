package org.hotrod.config.dynamicsql;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.VerbatimExpression;

public class LiteralTextPart extends DynamicSQLPart implements SQLChunk {

  private String text;

  public LiteralTextPart(final String text) {
    super("not-a-tag-but-sql-content");
    this.text = text;
  }

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification) throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  // Rendering

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return this.text;
  }

  @Override
  public String renderTag(final ParameterRenderer parameterRenderer) {
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
