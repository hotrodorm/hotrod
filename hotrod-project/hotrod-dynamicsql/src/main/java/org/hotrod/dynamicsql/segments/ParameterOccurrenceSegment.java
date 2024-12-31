package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.ParameterContext;

public class ParameterOccurrenceSegment extends QuerySegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterOccurrenceSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
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

}
