package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.BindExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;

@XmlRootElement(name = "bind")
public class BindTag extends DynamicSQLPart {

  // Constructor

  public BindTag() {
    super("bind");
  }

  // Properties

  private String nameText = null;
  private String valueText = null;

  private ParameterisableTextPart name = null;
  private ParameterisableTextPart value = null;

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
  protected void validateAttributes(final String tagIdentification) throws InvalidConfigurationFileException {
    this.name = this.nameText == null ? null : new ParameterisableTextPart(this.nameText, tagIdentification);
    this.value = this.valueText == null ? null : new ParameterisableTextPart(this.valueText, tagIdentification);
  }

  // Rendering

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
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {

    String n = this.name.renderTag(parameterRenderer);
    String v = this.value.renderTag(parameterRenderer);

    return new BindExpression(n, v);
  }

}