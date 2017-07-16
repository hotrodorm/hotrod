package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.CollectionExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;

@XmlRootElement(name = "not-a-tag")
public class CollectionTag extends DynamicSQLPart {

  // Constructor

  public CollectionTag(final List<DynamicSQLPart> parts) {
    super("not-a-tag");
    super.parts = parts;
  }

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

    return new CollectionExpression(exps.toArray(new DynamicExpression[0]));
  }

}