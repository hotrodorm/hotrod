package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "discover")
public class DiscoverTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Properties

  private SchemaTag currentSchema = null;
  private List<SchemaTag> schemas = new ArrayList<>();

  // Constructor

  public DiscoverTag() {
    super("discover");
  }

  // JAXB Setters

  @XmlElement(name = "current-schema")
  public void setCurrentSchema(final SchemaTag currentSchema) {
    this.currentSchema = currentSchema;
  }

  @XmlElement(name = "schema")
  public void setSchema(final SchemaTag schema) {
    this.schemas.add(schema);
  }

  // Behavior

  public void validate(final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {

    // Set defaults

    if (this.currentSchema == null) {
      this.currentSchema = new SchemaTag();
    }
    this.currentSchema.setCurrent();

    // 1. Validate the current schema

    this.currentSchema.validate(adapter, currentCS);

    // 2. Validate other schemas

    for (SchemaTag s : this.schemas) {
      s.validate(adapter, currentCS);
    }

  }

  // Getters

  public List<SchemaTag> getAllSchemaTags() {
    List<SchemaTag> all = new ArrayList<>();
    if (this.currentSchema != null) {
      all.add(this.currentSchema);
    }
    all.addAll(this.schemas);
    return all;
  }

  public SchemaTag getCurrentSchema() {
    return null;
  }

  public SchemaTag getSchema() {
    return null;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
