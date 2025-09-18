package org.hotrod.config.dynamicsql;

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "when")
public class WhenTag extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(WhenTag.class.getName());

  // Constructor

  public WhenTag() {
    super("when");
  }

  // Properties

  private String test = null;

  // JAXB Setters

  @XmlAttribute
  public void setTest(final String test) {
    log.fine("init");
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

  // Getters

  public String getTest() {
    return test;
  }

}