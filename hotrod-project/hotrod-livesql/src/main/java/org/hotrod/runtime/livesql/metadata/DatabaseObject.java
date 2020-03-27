package org.hotrod.runtime.livesql.metadata;

public class DatabaseObject {

  // Properties

  private String catalog;
  private String schema;
  private String name;
  private String type;

  // Constructor

  public DatabaseObject(final String catalog, final String schema, final String name, final String type) {
    this.catalog = catalog;
    this.schema = schema;
    this.name = name;
    this.type = type;
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

  public String getType() {
    return type;
  }

  public String renderUnescapedName() {
    StringBuilder sb = new StringBuilder();
    if (this.catalog != null) {
      sb.append(this.catalog);
      sb.append(".");
    }
    if (this.schema != null) {
      sb.append(this.schema);
    }
    if (this.catalog != null || this.schema != null) {
      sb.append(".");
    }
    sb.append(this.name);
    return sb.toString();
  }

  // --- Indexable methods (hashCode & equals) ---
  // DO NOT implement these methods, since the code relies on the default
  // [shallow] JVM implementation.
  // ---------------------------------------------

}
