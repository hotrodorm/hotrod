package org.hotrod.utils.identifiers2;

import org.hotrod.exceptions.InvalidIdentifierException;

public class ObjectId {

  // Properties

  private Id catalog;
  private Id schema;
  private Id object;

  // Constructor

  public ObjectId(final Id catalog, final Id schema, final Id object) throws InvalidIdentifierException {
    if (object == null) {
      throw new InvalidIdentifierException("'object' cannot be null");
    }
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
    log("ObjectID.equals() 1");
    if (this == obj)
      return true;
    log("ObjectID.equals() 2");
    if (obj == null)
      return false;
    log("ObjectID.equals() 3");
    if (getClass() != obj.getClass())
      return false;
    ObjectId other = (ObjectId) obj;
    log("ObjectID.equals() 4");
    if (catalog == null) {
      log("ObjectID.equals() 5");
      if (other.catalog != null)
        return false;
    } else if (!catalog.equals(other.catalog))
      return false;
    log("ObjectID.equals() 6");
    if (object == null) {
      log("ObjectID.equals() 7");
      if (other.object != null)
        return false;
    } else if (!object.equals(other.object)) {
      log("ObjectID.equals() 7.1");
      return false;
    }
    log("ObjectID.equals() 8");
    if (schema == null) {
      if (other.schema != null)
        return false;
    } else if (!schema.equals(other.schema))
      return false;
    log("ObjectID.equals() 9");
    return true;
  }

  private void log(final String txt) {
    // System.out.println("[log] " + txt);
  }

}
