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

  // Indexable methods (hashCode & equals)

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((schema == null) ? 0 : schema.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DatabaseObject other = (DatabaseObject) obj;
    if (catalog == null) {
      if (other.catalog != null)
        return false;
    } else if (!catalog.equals(other.catalog))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (schema == null) {
      if (other.schema != null)
        return false;
    } else if (!schema.equals(other.schema))
      return false;
    return true;
  }

}
