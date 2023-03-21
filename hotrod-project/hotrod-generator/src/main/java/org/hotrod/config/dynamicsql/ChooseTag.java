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
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "choose")
public class ChooseTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

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
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
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
          try {
            ParameterisableTextPart text = (ParameterisableTextPart) p;
            if (!text.isEmpty()) {
              throw new InvalidConfigurationFileException(this,
                  "Invalid <choose> tag. " + "A <choose> tag can only include <when> and <otherwise> tags in its body, "
                      + "but found additional text in it.");
            }
          } catch (ClassCastException e3) {
            throw new InvalidConfigurationFileException(this,
                "Invalid <choose> tag. "
                    + "A <choose> tag can only include <when> and <otherwise> tags, but found an object of class '"
                    + p.getClass().getName() + "'.");
          }
        }
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
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      List<WhenExpression> whenExps = new ArrayList<WhenExpression>();
      for (WhenTag w : this.whens) {
        WhenExpression we = (WhenExpression) w.getJavaExpression(parameterRenderer);
        whenExps.add(we);
      }

      OtherwiseExpression o = this.otherwise == null ? null
          : (OtherwiseExpression) this.otherwise.getJavaExpression(parameterRenderer);
      return new ChooseExpression(o, whenExps.toArray(new WhenExpression[0]));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for tag <choose> on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

  // Merging logic

  @Override
  protected boolean sameProperties(final DynamicSQLPart fresh) {
    try {
      ChooseTag f = (ChooseTag) fresh;
      return //
      Compare.same(this.otherwise, f.otherwise) && //
          Compare.same(this.whens, f.whens);
    } catch (ClassCastException e) {
      return false;
    }
  }

}