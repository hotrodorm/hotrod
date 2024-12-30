package org.hotrod.config.dynamicsql;

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.ParameterTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "foreach")
public class ForEachTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(ForEachTag.class.getName());

  // Constructor

  public ForEachTag() {
    super("foreach");
  }

  // Properties

  private String item = null;
  private String index = null;
  private String collection = null;
  private String open = null;
  private String separator = null;
  private String close = null;

  private ParameterTag itemParameter = null;
  private ParameterTag indexParameter = null;

  // JAXB Setters

  @XmlAttribute
  public void setItem(final String item) {
    this.item = item;
  }

  @XmlAttribute
  public void setIndex(final String index) {
    this.index = index;
  }

  @XmlAttribute
  public void setCollection(final String collection) {
    this.collection = collection;
  }

  @XmlAttribute
  public void setOpen(final String open) {
    this.open = open;
  }

  @XmlAttribute
  public void setSeparator(final String separator) {
    this.separator = separator;
  }

  @XmlAttribute
  public void setClose(final String close) {
    this.close = close;
  }

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    log.fine("init");

    if (this.item != null) {
      if (parameterDefinitions.findParameter(this.item) != null) {
        throw new InvalidConfigurationFileException(this, "Invalid <foreach> tag. "
            + "The 'item' attribute specifies a parameter or variable that is already defined. Try using a different variable name.");
      }
      parameterDefinitions.addVariable(this.item, this);
    } else {
      this.itemParameter = null;
    }

    if (this.index != null) {
      if (parameterDefinitions.findParameter(this.index) != null) {
        throw new InvalidConfigurationFileException(this, "Invalid <foreach> tag. "
            + "The 'index' attribute specifies a parameter or variable that is already defined. Try using a different variable name.");
      }
    } else {
      this.indexParameter = null;
    }

  }

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // Nothing extra to validate
  }

  @Override
  protected void postTagValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    if (this.itemParameter != null) {
      parameterDefinitions.remove(this.itemParameter);
    }
    if (this.indexParameter != null) {
      parameterDefinitions.remove(this.indexParameter);
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
        new TagAttribute("item", this.item), //
        new TagAttribute("index", this.index), //
        new TagAttribute("collection", this.collection), //
        new TagAttribute("open", this.open), //
        new TagAttribute("separator", this.separator), //
        new TagAttribute("close", this.close) //
    };
    return atts;
  }

  public String getItem() {
    return item;
  }

  public String getCollection() {
    return collection;
  }

  public String getOpen() {
    return open;
  }

  public String getSeparator() {
    return separator;
  }

  public String getClose() {
    return close;
  }

}