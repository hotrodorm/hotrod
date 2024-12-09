package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedQuery;

public class ParameterSegment extends QuerySegment {

  private String name;
  private Object value;
  private int sqlType;

  public ParameterSegment(String name, int sqlType) {
    this.name = name;
    this.sqlType = sqlType;
  }

  @Override
  public void prepare(PreparedQuery pq, ParameterContext context) throws DynamicExpressionException {
    pq.addLiteral("?");
    pq.registerParameter(this);

    if (!context.hasParameter(this.name)) {
      throw new DynamicExpressionException(
          "Could not find parameter with name '" + this.name + "' in the parameter object");
    }
    this.value = context.getParameterValue(this.name);
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

}
