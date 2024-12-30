package org.hotrod.dynamicsql.segments;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.ParameterContext;

public class ParameterInstanceValueSegment extends TypedParameterSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterInstanceValueSegment.class.getName());

  private Object value;
  private int sqlType;
  private String originalParameterName;

  public ParameterInstanceValueSegment(Object value, int sqlType, String originalParameterName) {
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
    return "<instance value for '" + this.originalParameterName + "'>";
  }

  @Override
  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal) throws SQLException {
    if (this.value != null) {
      ps.setObject(ordinal++, this.value);
    } else {
      ps.setNull(ordinal++, this.sqlType);
    }
  }

}
