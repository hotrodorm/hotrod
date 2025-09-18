package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;

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

}