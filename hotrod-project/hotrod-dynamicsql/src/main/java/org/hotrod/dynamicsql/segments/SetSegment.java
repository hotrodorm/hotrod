package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class SetSegment extends ControlSegment {

  private static final Logger log = Logger.getLogger(SetSegment.class.getName());

  private String column;
  private ParameterNullableSegment value;
  @SuppressWarnings("unused")
  private DynamicExpressionFactory factory;

  public SetSegment(String column, ParameterNullableSegment value, DynamicExpressionFactory factory) {
    log.finer("init");
    this.column = column;
    this.value = value;
    this.factory = factory;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    sc.consume(this.column + " = ");
    this.value.prepare(sc, context, loopNestingLevel);
    return true;
  }

}
