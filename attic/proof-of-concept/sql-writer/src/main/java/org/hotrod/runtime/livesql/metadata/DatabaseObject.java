package org.hotrod.runtime.livesql.metadata;

public class DatabaseObject {

  // Properties

  private String catalog;
  private String schema;
  private String name;

  // Constructor

  public DatabaseObject(final String catalog, final String schema, final String name) {
    this.catalog = catalog;
    this.schema = schema;
    this.name = name;
  }

  // Getters

  public final String getCatalog() {
    return catalog;
  }

  public final String getSchema() {
    return schema;
  }

  public final String getName() {
    return name;
  }

}
