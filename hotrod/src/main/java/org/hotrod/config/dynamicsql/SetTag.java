package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.SetExpression;

@XmlRootElement(name = "set")
public class SetTag extends DynamicSQLPart {

  // Constructor

  public SetTag() {
    super("set");
  }

  // Properties

  // Getters & Setters

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  @Override
  protected void specificBodyValidation(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    for (Iterator<DynamicSQLPart> it = super.parts.iterator(); it.hasNext();) {
      DynamicSQLPart p = it.next();
      try {
        ParameterisableTextPart text = (ParameterisableTextPart) p;
        if (!text.isEmpty()) {
          throw new InvalidConfigurationFileException("Invalid <set> tag included in the tag " + tagIdentification
              + ". A <set> tag can only include other tags, but no free text content in it.");
        }
        it.remove();
      } catch (ClassCastException e3) {
        // Nothing to do
      }
    }

  }

  // Rendering

  @Override
  protected boolean shouldRenderTag() {
    return true;
  }

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

    return new SetExpression(exps.toArray(new DynamicExpression[0]));
  }

}