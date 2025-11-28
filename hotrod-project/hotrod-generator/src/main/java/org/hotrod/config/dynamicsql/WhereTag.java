package org.hotrod.config.dynamicsql;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

@XmlRootElement(name = "where")
public class WhereTag extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(WhereTag.class.getName());

  // Constructor

  public WhereTag() {
    super("where");
  }

  // Properties

  private String separator = null;

  // JAXB Setters

  @XmlAttribute
  public void setSeparator(final String separator) {
    this.separator = separator;
  }

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    // separator

    if (this.separator == null) {
      this.separator = "AND";
    }

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

  @Override
  public String renderSQLFoundation(final ParameterRenderer parameterRenderer) {
    return "";
  }

  // Rendering

  @Override
  protected boolean shouldRenderTag() {
    return true;
  }

  public String getSeparator() {
    return separator;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

}