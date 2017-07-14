package org.hotrod.config.dynamicsql;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

@XmlRootElement(name = "where")
public class WhereTag extends DynamicSQLPart {

  // Constructor

  public WhereTag() {
    super("where");
  }

  // Properties

  // Getters & Setters

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
    return super.renderTag(parameterRenderer);
  }

}