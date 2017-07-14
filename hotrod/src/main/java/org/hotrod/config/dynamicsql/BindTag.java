package org.hotrod.config.dynamicsql;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

@XmlRootElement(name = "bind")
public class BindTag extends DynamicSQLPart {

  // Constructor

  public BindTag() {
    super("bind");
  }

  // Properties

  private String name = null;
  private String value = null;

  // Getters & Setters

  public String getName() {
    return name;
  }

  @XmlAttribute
  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  @XmlAttribute
  public void setValue(String value) {
    this.value = value;
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
    return super.renderEmptyTag( //
        new TagAttribute("name", this.name), //
        new TagAttribute("value", this.value) //
    );
  }

}