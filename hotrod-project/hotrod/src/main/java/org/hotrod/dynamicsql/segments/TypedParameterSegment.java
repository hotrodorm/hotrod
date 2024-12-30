package org.hotrod.dynamicsql.segments;

public abstract class TypedParameterSegment extends ParameterSegment {

  public abstract int getSQLType();

  public abstract void setValue(Object value);

}
