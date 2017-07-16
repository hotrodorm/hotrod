package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.WhereExpression;

@XmlRootElement(name = "where")
public class WhereTag extends DynamicSQLPart {

  // Constructor

  public WhereTag() {
    super("where");
  }

  // Properties

  // JAXB Setters

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification) throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  // Rendering

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {

    List<DynamicExpression> exps = new ArrayList<DynamicExpression>();
    for (DynamicSQLPart p : super.parts) {
      exps.add(p.getJavaExpression(parameterRenderer));
    }

    return new WhereExpression(exps.toArray(new DynamicExpression[0]));
  }

}