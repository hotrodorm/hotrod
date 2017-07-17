package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.WhenExpression;

@XmlRootElement(name = "when")
public class WhenTag extends DynamicSQLPart {

  // Constructor

  public WhenTag() {
    super("when");
  }

  // Properties

  private String testText = null;
  private ParameterisableTextPart test = null;

  // JAXB Setters

  @XmlAttribute
  public void setTest(final String test) {
    this.testText = test;
  }

  // Behavior

  protected void validateAttributes(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    if (this.testText == null) {
      throw new InvalidConfigurationFileException(
          "Invalid <when> tag included in the tag " + tagIdentification + ". The 'test' attribute must be specified.");
    }
    this.test = new ParameterisableTextPart(this.testText, tagIdentification, parameterDefinitions);
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
        new TagAttribute("test", this.test) //
    };
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {
    String condition = this.test.renderXML(parameterRenderer);

    List<DynamicExpression> exps = new ArrayList<DynamicExpression>();
    for (DynamicSQLPart p : super.parts) {
      exps.add(p.getJavaExpression(parameterRenderer));
    }

    return new WhenExpression(condition, exps.toArray(new DynamicExpression[0]));
  }

}