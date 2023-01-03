package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "exclude")
public class ExcludeTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Properties

  private String catalog = null;
  private String schema = null;
  private String name = null;

  private ObjectId objectId;

  // Constructor

  public ExcludeTag() {
    super("exclude");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute
  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  @XmlAttribute
  public void setSchema(final String schema) {
    this.schema = schema;
  }

  // Behavior

  public void validate(final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'name' cannot be empty", //
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify a database table name.");
    }

    // catalog

    Id catalogId;
    try {
      catalogId = this.catalog == null ? Id.fromTypedSQL(currentCS.getCatalog(), adapter)
          : Id.fromTypedSQL(this.catalog, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid catalog name '" + this.catalog + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    // schema

    Id schemaId;
    try {
      schemaId = this.schema == null ? Id.fromTypedSQL(currentCS.getSchema(), adapter)
          : Id.fromTypedSQL(this.schema, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid schema name '" + this.schema + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    // Assemble object id

    Id nameId;
    try {
      nameId = Id.fromTypedSQL(this.name, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid table name '" + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    try {
      this.objectId = new ObjectId(catalogId, schemaId, nameId, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid table object name: " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

  }

  // Getters

  public ObjectId getObjectId() {
    return objectId;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    return true;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
