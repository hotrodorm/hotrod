package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.BindExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

@XmlRootElement(name = "bind")
public class BindTag extends DynamicSQLPart {

  // Constants

  private static final String NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  // Properties

  private String nameText = null;
  private String valueText = null;

  private LiteralTextPart name = null;
  private ParameterisableTextPart value = null;

  // Constructor

  public BindTag() {
    super("bind");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(String name) {
    this.nameText = name;
  }

  @XmlAttribute
  public void setValue(String value) {
    this.valueText = value;
  }

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    if (this.nameText == null) {
      throw new InvalidConfigurationFileException("Invalid <bind> tag in the body of the tag " + tagIdentification
          + ". The 'name' attribute in the <bind> tag must be specified.");
    }
    if (!this.nameText.matches(NAME_PATTERN)) {
      throw new InvalidConfigurationFileException("Invalid <bind> tag in the body of the tag " + tagIdentification
          + ". The name must start with a letter and continue with alphanumeric caracters and/or underscores (_) but found '"
          + this.nameText + "'");
    }

    this.name = this.nameText == null ? null : new LiteralTextPart(this.nameText);
    this.value = this.valueText == null ? null
        : new ParameterisableTextPart(this.valueText, tagIdentification, parameterDefinitions);

    SQLParameter p = new SQLParameter(this.nameText, tagIdentification);
    parameterDefinitions.add(p);

  }

  @Override
  protected void specificBodyValidation(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
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

    // try {
    //
    // String n = this.name.renderXML(parameterRenderer);
    // String v = this.value.renderXML(parameterRenderer);
    //
    // return new BindExpression(n, v);
    //
    // } catch (RuntimeException e) {
    // throw new InvalidJavaExpressionException(this.getSourceLocation(),
    // "Could not produce Java expression for tag <bind> on file '" +
    // this.getSourceLocation().getFile().getPath()
    // + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
    // + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    // }

  }

}