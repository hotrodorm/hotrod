package org.hotrod.runtime.sql.expressions;

import org.hotrod.runtime.sql.QueryWriter;

public class Constant extends Expression {

  private static final int PRECEDENCE = 1;

  // Control characters (x01 - x1f)
  // Single quotes
  // Delete (x7f)
  // Control characters (x80 - x9f)
  // Double quotes
  private static final String SQL_INJECTION_PATTERN = ".*[\\x01-\\x1f'\\x7f-\\x9f\"].*";

  // Properties

  private Object value;
  private JDBCType type;

  private boolean parameterize;

  // Constructors

  public Constant(final String value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.VARCHAR;
    this.parameterize = value.matches(SQL_INJECTION_PATTERN);
  }

  public Constant(final Character value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.VARCHAR;
    this.parameterize = true;
  }

  public Constant(final Number value) {
    super(PRECEDENCE);
    if (this.isFloat(value) || this.isDouble(value)) {
      this.parameterize = true;
      this.type = JDBCType.NUMERIC;
      this.value = value;
    } else {
      this.parameterize = false;
      this.type = JDBCType.NUMERIC;
      this.value = value;
    }
  }

  public Constant(final Boolean value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.BOOLEAN;
    this.parameterize = false;
  }

  public Constant(final java.util.Date value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.TIMESTAMP;
    this.parameterize = true;
  }

  public Constant(final Object value, final JDBCType type) {
    super(PRECEDENCE);
    this.value = value;
    if (type == null) {
      throw new IllegalArgumentException("Specified type cannot be null");
    }
    this.type = type;
    this.parameterize = true;
  }

  // Utilities

  private boolean isFloat(final Number n) {
    try {
      Float.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  private boolean isDouble(final Number n) {
    try {
      Double.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.parameterize) {
      String name = w.registerParameter(this.value);
      w.write("#{" + name);
      if (this.value == null) {
        w.write(",jdbcType=" + this.type);
      }
      w.write("}");
    } else {
      if (this.value instanceof String) {
        w.write("'" + this.value + "'");
      } else {
        w.write("" + this.value);
      }
    }
  }

}
