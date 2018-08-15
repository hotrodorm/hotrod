package org.hotrod.utils.identifiers;

import java.util.List;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.utils.identifiers.Id.NamePart;

public class ObjectId implements Comparable<ObjectId> {

  // Properties

  private Id catalog;
  private Id schema;
  private Id object;
  private DatabaseAdapter adapter;

  // Constructor

  public ObjectId(final Id catalog, final Id schema, final Id object, final DatabaseAdapter adapter)
      throws InvalidIdentifierException {
    if (object == null) {
      throw new InvalidIdentifierException("'object' cannot be null");
    }
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null");
    }
    this.catalog = catalog;
    this.schema = schema;
    this.object = object;
    this.adapter = adapter;
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
    StringBuilder sb = new StringBuilder();
    if (this.catalog != null) {
      sb.append(this.catalog.getRenderedSQLName());
      sb.append(".");
      if (this.schema != null) {
        sb.append(this.schema.getRenderedSQLName());
        sb.append(".");
      } else {
        if (this.adapter.supportsSchema()) {
          sb.append(".");
        }
      }
    } else {
      if (this.schema != null) {
        sb.append(this.schema.getRenderedSQLName());
        sb.append(".");
      }
    }
    sb.append(this.object.getRenderedSQLName());
    return sb.toString();
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

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
    result = prime * result + ((object == null) ? 0 : object.hashCode());
    result = prime * result + ((schema == null) ? 0 : schema.hashCode());
    return result;
  }

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
    } else if (!object.equals(other.object)) {
      return false;
    }
    if (schema == null) {
      if (other.schema != null)
        return false;
    } else if (!schema.equals(other.schema))
      return false;
    return true;
  }

  // Helpers

  public String toString() {
    return this.getRenderedSQLName();
  }

  private void log(final String txt) {
    // System.out.println("[log] " + txt);
  }

}
