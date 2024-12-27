package org.hotrod.dynamic.segments;

import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class ParameterOccurrenceSegment extends QuerySegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterOccurrenceSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private Object value;
  private int sqlType;

  private DynamicExpression nameExpression;

  public ParameterOccurrenceSegment(DynamicExpressionFactory factory, String name, int sqlType) {
    this.factory = factory;
    this.name = name;
    this.sqlType = sqlType;
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {
      Object v = this.nameExpression.evaluate(context, Object.class);
      ParameterInstanceValueSegment is = new ParameterInstanceValueSegment(v, this.sqlType, this.name);
      sc.consume("?");
      sc.consume(is);
      return true;
  }

//  public String getName() {
//    return name;
//  }
//
//  public int getSQLType() {
//    return sqlType;
//  }
//
//  public Object getValue() {
//    return value;
//  }
//
//  public void setValue(Object value) {
//    this.value = value;
//  }

}
