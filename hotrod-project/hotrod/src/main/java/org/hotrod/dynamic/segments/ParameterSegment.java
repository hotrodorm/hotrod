package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.ParameterContext;

public class ParameterSegment extends StaticSegment {

  private String name;
  private Object value;
  private int sqlType;

  public ParameterSegment(String name, int sqlType) {
    this.name = name;
    this.sqlType = sqlType;
  }

  @Override
  public void prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    if (!context.hasParameter(this.name)) {
      throw new DynamicExpressionException(
          "Could not find parameter with name '" + this.name + "' in the parameter object");
    }
    this.value = context.getParameterValue(this.name);
    sc.consume("?");
    sc.consume(this);
  }

  public String getName() {
    return name;
  }

  public int getSQLType() {
    return sqlType;
  }

  public Object getValue() {
    return value;
  }

//  // StaticSegment
//
//  @Override
//  public String getLiteral() {
//    return null;
//  }
//
//  @Override
//  public ParameterSegment getParameter() {
//    return this;
//  }

}
