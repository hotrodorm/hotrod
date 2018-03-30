package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.WhenExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "when")
public class WhenTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(WhenTag.class);

  // Constructor

  public WhenTag() {
    super("when");
  }

  // Properties

  private String test = null;

  // JAXB Setters

  @XmlAttribute
  public void setTest(final String test) {
    log.debug("init");
    this.test = test;
  }

  // Behavior

  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // Nothing to do
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
    TagAttribute[] atts = { //
        new TagAttribute("test", this.test) //
    };
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new WhenExpression(this.test, toArray(this.parts, parameterRenderer));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for tag <when> on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }
  }

  // Merging logic

  @Override
  protected boolean sameProperties(final DynamicSQLPart fresh) {
    try {
      WhenTag f = (WhenTag) fresh;
      return Compare.same(this.test, f.test);
    } catch (ClassCastException e) {
      return false;
    }
  }

}