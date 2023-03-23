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

  private String canonicalCatalog = null;
  private String canonicalSchema = null;

  @SuppressWarnings("unused")
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
  public void setExclude(final ExcludeTag exclude) {
    this.excludes.add(exclude);
  }

  public ExcludeTag getExclude() {
    return null;
  }

  public void setCurrent() {
    this.isCurrentCS = true;
  }

  // Behavior

  public void validate(final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {

    if (this.isCurrentCS) {

      this.cs = currentCS;
      this.catalog = currentCS.getCatalog();
      this.name = currentCS.getSchema();

      // catalog

      if (adapter.supportsCatalog()) {
        if (this.catalog != null) {
          try {
            this.canonicalCatalog = Id.fromTypedSQL(this.catalog, adapter).getCanonicalSQLName();
          } catch (InvalidIdentifierException e) {
            String msg = "Invalid current catalog name '" + this.catalog + "' specified in the runtime configuration: "
                + e.getMessage();
            throw new InvalidConfigurationFileException(this, msg);
          }
        }
      } else {
        if (this.catalog != null) {
          throw new InvalidConfigurationFileException(this,
              "Invalid current catalog name '" + this.catalog
                  + "' specified in the runtime configuration. This database does not support catalogs. "
                  + "Please remove it from the runtime configuration.");
        }
      }

      // schema

      if (adapter.supportsSchema()) {
        if (this.name != null) {
          try {
            this.canonicalSchema = Id.fromTypedSQL(this.name, adapter).getCanonicalSQLName();
          } catch (InvalidIdentifierException e) {
            String msg = "Invalid current schema '" + this.name + "' specified in the runtime configuration: "
                + e.getMessage();
            throw new InvalidConfigurationFileException(this, msg);
          }
        }
      } else {
        if (this.name != null) {
          throw new InvalidConfigurationFileException(this,
              "Invalid current schema name '" + this.name
                  + "' specified in the runtime configuration. This database does not support schemas. "
                  + "Please remove it from the runtime configuration.");
        }
      }

    } else {

      // catalog

      if (adapter.supportsCatalog()) {
        if (this.catalog != null) {
          try {
            this.canonicalCatalog = Id.fromTypedSQL(this.catalog, adapter).getCanonicalSQLName();
          } catch (InvalidIdentifierException e) {
            String msg = "Invalid catalog name '" + this.catalog + "' in tag <" + super.getTagName() + ">: "
                + e.getMessage();
            throw new InvalidConfigurationFileException(this, msg);
          }
        }
      } else {
        if (this.catalog != null) {
          throw new InvalidConfigurationFileException(this, "The <schema> tag specifies the catalog '" + this.catalog
              + "', but this database does not support catalogs. Please remove it.");
        }
      }

      // schema

      if (adapter.supportsSchema()) {
        if (this.name != null) {
          try {
            this.canonicalSchema = Id.fromTypedSQL(this.name, adapter).getCanonicalSQLName();
          } catch (InvalidIdentifierException e) {
            String msg = "Invalid schema name '" + this.name + "' in tag <" + super.getTagName() + ">: "
                + e.getMessage();
            throw new InvalidConfigurationFileException(this, msg);
          }
        }
      } else {
        if (this.name != null) {
          throw new InvalidConfigurationFileException(this,
              "The <schema> tag includes the attribute 'name' with value '" + this.catalog
                  + "' to indicate a schema, but this database does not support schemas. Please remove it.");
        }
      }

      this.cs = new CatalogSchema(this.canonicalCatalog, this.canonicalSchema);

    }

    // excludes

    for (ExcludeTag e : this.excludes) {
      e.validate(adapter);
    }

  }

  // Getters

  public List<ExcludeTag> getExcludeList() {
    return excludes;
  }

  public String getCanonicalCatalog() {
    return this.canonicalCatalog;
  }

  public String getCanonicalSchema() {
    return this.canonicalSchema;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
