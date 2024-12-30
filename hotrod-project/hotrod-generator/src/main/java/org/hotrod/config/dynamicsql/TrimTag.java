package org.hotrod.config.dynamicsql;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "trim")
public class TrimTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

  // Constructor

  public TrimTag() {
    super("trim");
  }

  // Properties

  private String prefix = null;
  private String separator = null;
//  private String prefixOverrides = null;
  private String suffix = null;
//  private String suffixOverrides = null;

  // JAXB Setters

  @XmlAttribute
  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  @XmlAttribute
  public void setSeparator(final String separator) {
    this.separator = separator;
  }

//  @XmlAttribute
//  public void setPrefixOverrides(final String prefixOverrides) {
//    this.prefixOverrides = prefixOverrides;
//  }

  @XmlAttribute
  public void setSuffix(final String suffix) {
    this.suffix = suffix;
  }

//  @XmlAttribute
//  public void setSuffixOverrides(final String suffixOverrides) {
//    this.suffixOverrides = suffixOverrides;
//  }

  // Getters

  public String getPrefix() {
    return prefix;
  }

  public String getSeparator() {
    return separator;
  }

  public String getSuffix() {
    return suffix;
  }

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // Nothing to validate
  }

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    for (Iterator<DynamicSQLPart> it = super.parts.iterator(); it.hasNext();) {
      DynamicSQLPart p = it.next();
      try {
        ParameterisableTextPart text = (ParameterisableTextPart) p;
        if (!text.isEmpty()) {
          throw new InvalidConfigurationFileException(this,
              "Invalid <trim> tag. " + "A <trim> tag can only include other tags, but no free text content in it.");
        }
        // it.remove();
      } catch (ClassCastException e3) {
        // Nothing to do for other kinds of part.
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
    TagAttribute[] atts = { //
        new TagAttribute("prefix", this.prefix), //
        new TagAttribute("separator", this.separator), //
        new TagAttribute("suffix", this.suffix) //
    };
    return atts;
  }

}