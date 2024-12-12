package org.hotrod.dynamic.segments;

import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.ParameterContext;

public class IterationValueParameterSegment extends ParameterSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(IterationValueParameterSegment.class.getName());

  private Object value;
  private int sqlType;
  private String originalParameterName;

  public IterationValueParameterSegment(Object value, int sqlType, String originalParameterName) {
    this.value = value;
    this.sqlType = sqlType;
    this.originalParameterName = originalParameterName;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {
    sc.consume("?");
    sc.consume(this);
    return true;
  }

  public int getSQLType() {
    return sqlType;
  }

  public Object getValue() {
    return this.value;
  }

  @Override
  public String getName() {
    return "<iteration value for '" + this.originalParameterName + "'>";
  }

}
