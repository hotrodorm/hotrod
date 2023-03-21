package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.ParameterTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "bind")
public class BindTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final String NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  // Properties

  private String name = null;
  private String value = null;

  // Constructor

  public BindTag() {
    super("bind");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute
  public void setValue(final String value) {
    this.value = value;
  }

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    if (this.name == null) {
      throw new InvalidConfigurationFileException(this, "Invalid <bind> tag. The 'name' attribute must be specified.");
    }
    if (!this.name.matches(NAME_PATTERN)) {
      throw new InvalidConfigurationFileException(this,
          "Invalid <bind> tag. The 'name' attribute must start with a letter "
              + "and continue with alphanumeric caracters and/or underscores but found '" + this.name + "'");
    }

    ParameterTag p = new ParameterTag();
    p.setName(this.name);
    parameterDefinitions.add(p);

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
        new TagAttribute("name", this.name), //
        new TagAttribute("value", this.value) //
    };
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    throw new UnsupportedOperationException("The <foreach> and <bind> tags are not yet supported.");

    /**
     * 
     * <pre>
     *
     * try {
     * 
     *   return new BindExpression(this.name, this.value);
     * 
     * } catch (RuntimeException e) {
     *   throw new InvalidJavaExpressionException(this.getSourceLocation(),
     *       "Could not produce Java expression for tag <bind>: " + e.getMessage());
     * }
     * 
     * </pre>
     * 
     */

  }

  // Merging logic

  @Override
  protected boolean sameProperties(final DynamicSQLPart fresh) {
    try {
      BindTag f = (BindTag) fresh;
      return //
      Compare.same(this.name, f.name) && //
          Compare.same(this.value, f.value);
    } catch (ClassCastException e) {
      return false;
    }
  }

}