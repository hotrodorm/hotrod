package org.hotrod.dynamic.segments;

public abstract class ParameterSegment extends StaticSegment {

  public abstract String getName();

  public abstract int getSQLType();

  public abstract Object getValue();

}
