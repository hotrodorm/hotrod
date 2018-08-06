package org.hotrod.utils.identifiers2;

public class ObjectId {

  // Properties

  private Id catalog;
  private Id schema;
  private Id object;

  // Constructor

  public ObjectId(final Id catalog, final Id schema, final Id object) {
    this.catalog = catalog;
    this.schema = schema;
    this.object = object;
  }

  // Getters

  public Id getCatalog() {
    return catalog;
  }

  public Id getSchema() {
    return schema;
  }

  public Id getObject() {
    return object;
  }

  // Equals

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ObjectId other = (ObjectId) obj;
    if (catalog == null) {
      if (other.catalog != null)
        return false;
    } else if (!catalog.equals(other.catalog))
      return false;
    if (object == null) {
      if (other.object != null)
        return false;
    } else if (!object.equals(other.object))
      return false;
    if (schema == null) {
      if (other.schema != null)
        return false;
    } else if (!schema.equals(other.schema))
      return false;
    return true;
  }

}
