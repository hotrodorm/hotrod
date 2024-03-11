package org.hotrod.runtime.livesql.metadata;

public class DatabaseObject {

  // Properties

  private Name catalog;
  private Name schema;
  private Name name;
  private String type;

  // Constructor

  public DatabaseObject(final Name catalog, final Name schema, final Name name, final String type) {
    this.catalog = catalog;
    this.schema = schema;
    this.name = name;
    this.type = type;
  }

  // Getters

  public final Name getCatalog() {
    return catalog;
  }

  public final Name getSchema() {
    return schema;
  }

  public final Name getName() {
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
