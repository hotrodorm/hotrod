package org.hotrod.dynamicsql.segments;

public abstract class DynamicContentSegment extends ContentSegment {

  // Indexable to use it as the key on a Map

  @Override
  public int hashCode() {
    return System.identityHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj;
  }

}
