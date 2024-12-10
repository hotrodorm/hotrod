package org.hotrod.dynamic.segments;

import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class ParameterSegment extends StaticSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private Object value;
  private int sqlType;

  private DynamicExpression nameExpression;

  public ParameterSegment(DynamicExpressionFactory factory, String name, int sqlType) {
    this.factory = factory;
    this.name = name;
    this.sqlType = sqlType;

    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public void prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    this.value = this.nameExpression.evaluate(context, Object.class);
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

}
