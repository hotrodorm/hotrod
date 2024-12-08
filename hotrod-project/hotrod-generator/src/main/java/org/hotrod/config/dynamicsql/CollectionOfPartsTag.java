package org.hotrod.config.dynamicsql;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.dynamicsql.existing.expressions.CollectionExpression;
import org.hotrod.dynamicsql.existing.expressions.OldDynamicExpression;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidJavaExpressionException;
import org.hotrod.generator.ParameterRenderer;

@XmlRootElement(name = "not-a-tag")
public class CollectionOfPartsTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

  // Constructor

  public CollectionOfPartsTag(final List<DynamicSQLPart> parts) {
    super("not-a-tag");
    super.parts = parts;
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
    return false;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

  // Java Expression

  @Override
  public OldDynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new CollectionExpression(toArray(this.parts, parameterRenderer));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for XML tag on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

}