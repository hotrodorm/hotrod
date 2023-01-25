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
  private List<SchemaTag> schemas = new ArrayList<SchemaTag>();

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
  public void addSchema(final SchemaTag schema) {
    this.schemas.add(schema);
  }

  // Behavior

  public void validate(final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {

    // Set defaults

    if (this.currentSchema != null) {
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
