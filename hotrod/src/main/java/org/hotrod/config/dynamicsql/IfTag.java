package org.hotrod.config.dynamicsql;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

@XmlRootElement(name = "if")
public class IfTag extends DynamicSQLPart {

  // Constructor

  public IfTag() {
    super("if");
  }

  // Properties

  private String test = null;

  // Getters & Setters

  public String getTest() {
    return test;
  }

  @XmlAttribute
  public void setTest(final String test) {
    this.test = test;
  }

  // Behavior

  @Override
  public void validate(final String tagIdentification) throws InvalidConfigurationFileException {
    // No validation necessary
  }

  @Override
  public List<SQLParameter> getParameters() {
    return null;
  }

  // Rendering

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return super.renderTag(parameterRenderer, //
        new TagAttribute("test", this.test) //
    );
  }

}