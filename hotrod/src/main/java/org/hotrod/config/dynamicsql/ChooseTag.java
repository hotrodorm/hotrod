package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.ChooseExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.OtherwiseExpression;
import org.hotrod.runtime.dynamicsql.expressions.WhenExpression;

@XmlRootElement(name = "choose")
public class ChooseTag extends DynamicSQLPart {

  // Constructor

  public ChooseTag() {
    super("choose");
  }

  // Properties

  private OtherwiseTag otherwise;
  private List<WhenTag> whens;

  // Getters & Setters

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification) throws InvalidConfigurationFileException {
    // No attributes; nothing to do

    // whens & otherwise

    this.whens = new ArrayList<WhenTag>();
    this.otherwise = null;

    for (DynamicSQLPart p : super.parts) {
      try {
        WhenTag when = (WhenTag) p;
        this.whens.add(when);
      } catch (ClassCastException e1) {
        try {
          OtherwiseTag otherwise = (OtherwiseTag) p;
          this.otherwise = otherwise;
        } catch (ClassCastException e2) {
          throw new InvalidConfigurationFileException("Invalid <choose> tag included in the tag " + tagIdentification
              + ". A <choose> tag can only include <when> and <otherwise> tags, but found an object of class '"
              + p.getClass().getName() + "'.");
        }
      }
    }

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

    List<WhenExpression> whenExps = new ArrayList<WhenExpression>();
    for (WhenTag w : this.whens) {
      WhenExpression we = (WhenExpression) w.getJavaExpression(parameterRenderer);
      whenExps.add(we);
    }

    OtherwiseExpression o = this.otherwise == null ? null
        : (OtherwiseExpression) this.otherwise.getJavaExpression(parameterRenderer);

    return new ChooseExpression(o, whenExps.toArray(new WhenExpression[0]));
  }

}