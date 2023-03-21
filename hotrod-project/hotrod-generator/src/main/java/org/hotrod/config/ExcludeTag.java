package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "exclude")
public class ExcludeTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Properties

  private String name = null;

  // Constructor

  public ExcludeTag() {
    super("exclude");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  // Behavior

  public void validate(final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "The attribute 'name' of the tag <exclude> must be specified");
    }
  }

  // Getters

  public String getName() {
    return this.name;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
