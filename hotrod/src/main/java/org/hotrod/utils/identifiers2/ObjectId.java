package org.hotrod.utils.identifiers2;

import java.util.List;

import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.utils.identifiers2.Id.NamePart;

public class ObjectId implements Comparable<ObjectId> {

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

  public String getCanonicalSQLName() {
    return this.object.getCanonicalSQLName();
  }

  public String getRenderedSQLName() {
    return this.object.getRenderedSQLName();
  }

  public boolean wasJavaNameSpecified() {
    return this.object.wasJavaNameSpecified();
  }

  public String getJavaClassName() {
    return this.object.getJavaClassName();
  }

  public String getJavaMemberName() {
    return this.object.getJavaMemberName();
  }

  public String getJavaConstantName() {
    return this.object.getJavaConstantName();
  }

  public String getDashedName() {
    return this.object.getDashedName();
  }

  public String getJavaGetter() {
    return this.object.getJavaGetter();
  }

  public String getJavaSetter() {
    return this.object.getJavaSetter();
  }

  public List<NamePart> getCanonicalParts() {
    return this.object.getCanonicalParts();
  }

  // Comparable

  @Override
  public int compareTo(final ObjectId o) {

    int comp = compareIds(this.catalog, o.catalog);
    if (comp != 0) {
      return comp;
    }

    comp = compareIds(this.schema, o.schema);
    if (comp != 0) {
      return comp;
    }

    return compareIds(this.object, o.object);

  }

  private int compareIds(final Id a, final Id b) {
    if (a == null) {
      return b == null ? 0 : -1;
    }
    if (b == null) {
      return 1;
    }
    return a.compareTo(b);
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
