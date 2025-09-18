package org.hotrod.config.dynamicsql;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "where")
public class WhereTag extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(WhereTag.class.getName());

  // Constructor

  public WhereTag() {
    super("where");
  }

  // Properties

  // JAXB Setters

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    log.fine("extra validation");

    for (Iterator<DynamicSQLPart> it = super.parts.iterator(); it.hasNext();) {
      DynamicSQLPart p = it.next();
      try {
        ParameterisableTextPart text = (ParameterisableTextPart) p;
        if (!text.isEmpty()) {
          throw new InvalidConfigurationFileException(this,
              "Invalid <where> tag. " + "A <where> tag can only include other tags, but no free text content in it.");
        }
        // it.remove();
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

}