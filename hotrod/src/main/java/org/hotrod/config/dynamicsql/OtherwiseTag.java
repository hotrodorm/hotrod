package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.OtherwiseExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

@XmlRootElement(name = "otherwise")
public class OtherwiseTag extends DynamicSQLPart {

  // Constructor

  public OtherwiseTag() {
    super("otherwise");
  }

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No extra validation on the body
  }

  // Rendering

  @Override
  protected boolean shouldRenderTag() {
    return true;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new OtherwiseExpression(toArray(this.parts, parameterRenderer));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for tag <otherwise> on file '"
              + this.getSourceLocation().getFile().getPath() + "' at line " + this.getSourceLocation().getLineNumber()
              + ", col " + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

  // Merging logic

  @Override
  protected boolean sameProperties(final DynamicSQLPart fresh) {
    return true;
  }

}