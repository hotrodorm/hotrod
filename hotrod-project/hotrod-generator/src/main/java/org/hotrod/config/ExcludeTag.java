package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;

@XmlRootElement(name = "exclude")
public class ExcludeTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Properties

  private String name = null;
  private String canonicalName = null;

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
    if (this.name == null) {
      throw new InvalidConfigurationFileException(this, "The attribute 'name' of the tag <exclude> must be specified. "
          + "It will exclude the table or view with that name.");
    } else {
      try {
        this.canonicalName = Id.fromTypedSQL(this.name, adapter).getCanonicalSQLName();
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid database object name '" + this.name + "' in tag <" + super.getTagName() + ">: "
            + e.getMessage();
        throw new InvalidConfigurationFileException(this, msg);
      }
    }
  }

  // Getters

  public String getTypedName() {
    return this.name;
  }

  public String getCanonicalName() {
    return canonicalName;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
