package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.metadata.Metadata;

@XmlRootElement(name = "if")
public class IfTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

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
      throw new InvalidConfigurationFileException(this, "Invalid <if> tag. The 'test' attribute must be specified.");
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

  // Getters

  public String getTest() {
    return test;
  }

}