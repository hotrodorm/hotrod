package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.IfExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "if")
public class IfTag extends DynamicSQLPart {

  // Constructor

  public IfTag() {
    super("if");
  }

  // Properties

  private String test = null;

  // JAXB Setters

  @XmlAttribute
  public void setTest(final String test) {
    this.test = test;
  }

  // Behavior

  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    if (this.test == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid <if> tag. The 'test' attribute must be specified.");
    }
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
    TagAttribute[] atts = { new TagAttribute("test", this.test) };
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new IfExpression(this.test, toArray(this.parts, parameterRenderer));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for <if> tag on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

  // Merging logic

  @Override
  protected boolean sameProperties(final DynamicSQLPart fresh) {
    try {
      IfTag f = (IfTag) fresh;
      return Compare.same(this.test, f.test);
    } catch (ClassCastException e) {
      return false;
    }
  }

}