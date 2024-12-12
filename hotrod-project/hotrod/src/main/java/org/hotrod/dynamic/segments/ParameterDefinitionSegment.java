package org.hotrod.dynamic.segments;

import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class ParameterDefinitionSegment extends ParameterSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterDefinitionSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private Object value;
  private int sqlType;

  private DynamicExpression nameExpression;

  public ParameterDefinitionSegment(DynamicExpressionFactory factory, String name, int sqlType) {
    this.factory = factory;
    this.name = name;
    this.sqlType = sqlType;

    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {
    if (loopNestingLevel > 0) {
      Object v = this.nameExpression.evaluate(context, Object.class);
      IterationValueParameterSegment is = new IterationValueParameterSegment(v, this.sqlType, this.name);
      sc.consume("?");
      sc.consume(is);
      return true;
    } else {
      this.value = this.nameExpression.evaluate(context, Object.class);
      sc.consume("?");
      sc.consume(this);
      return true;
    }
  }

  public String getName() {
    return name;
  }

  public int getSQLType() {
    return sqlType;
  }

  public Object getValue() {
    log.info("GET VALUE");
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

}
