package org.hotrod.dynamic.segments;

public abstract class TypedParameterSegment extends ParameterSegment {

  public abstract int getSQLType();

  public abstract void setValue(Object value);

}
