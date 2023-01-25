package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "schema")
public class SchemaTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Properties

  private boolean isCurrentCS = false;
  private String name = null;
  private String catalog = null;
  private List<ExcludeTag> excludes = new ArrayList<>();

  private CatalogSchema cs;

  // Constructor

  public SchemaTag() {
    super("schema");
  }

  // JAXB Setters

  @XmlAttribute(name = "name")
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute(name = "catalog")
  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  @XmlElement(name = "exclude")
  public void addExclude(final ExcludeTag exclude) {
    this.excludes.add(exclude);
  }

  public void setCurrent() {
    this.isCurrentCS = true;
  }

  // Behavior

  public void validate(final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {

    if (this.isCurrentCS) {

      this.cs = currentCS;

    } else {

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
        schemaId = this.name == null ? Id.fromTypedSQL(currentCS.getSchema(), adapter)
            : Id.fromTypedSQL(this.name, adapter);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid schema name '" + this.name + "' on tag <" + super.getTagName() + "> for the table '"
            + this.name + "': " + e.getMessage();
        throw new InvalidConfigurationFileException(this, msg, msg);
      }

      this.cs = new CatalogSchema(this.catalog, this.name);
    }

    // excludes

    for (ExcludeTag e : this.excludes) {
      e.validate(adapter, currentCS);
    }

  }

  // Getters

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
    return this.getTagName();
  }

}
