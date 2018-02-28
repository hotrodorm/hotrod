package org.hotrod.config.dynamicsql;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.WhereExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

@XmlRootElement(name = "where")
public class WhereTag extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(WhereTag.class);

  // Constructor

  public WhereTag() {
    super("where");
  }

  // Properties

  // JAXB Setters

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    log.debug("extra validation");

    for (Iterator<DynamicSQLPart> it = super.parts.iterator(); it.hasNext();) {
      DynamicSQLPart p = it.next();
      try {
        ParameterisableTextPart text = (ParameterisableTextPart) p;
        if (!text.isEmpty()) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(),
              "Invalid <where> tag. " + "A <where> tag can only include other tags, but no free text content in it.");
        }
        // it.remove();
      } catch (ClassCastException e3) {
        // Nothing to do
      }
    }

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

      return new WhereExpression(toArray(this.parts, parameterRenderer));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for tag <where> on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

  // Merging logic

  @Override
  protected boolean sameProperties(final DynamicSQLPart fresh) {
    return true;
  }

}