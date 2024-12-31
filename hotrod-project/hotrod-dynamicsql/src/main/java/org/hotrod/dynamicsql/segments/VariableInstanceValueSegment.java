package org.hotrod.dynamicsql.segments;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.ParameterContext;

public class VariableInstanceValueSegment extends ParameterSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(VariableInstanceValueSegment.class.getName());

  private Object value;
  private String originalParameterName;

  public VariableInstanceValueSegment(Object value, String originalParameterName) {
    this.value = value;
    this.originalParameterName = originalParameterName;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {
    sc.consume("?");
    sc.consume(this);
    return true;
  }

  public Object getValue() {
    return this.value;
  }

  @Override
  public String getName() {
    return this.originalParameterName;
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal) throws SQLException {
    ps.setObject(ordinal, this.value);
  }

}
