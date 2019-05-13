package org.hotrod.runtime.livesql.expressions;

import java.util.Date;

import org.hotrod.runtime.livesql.QueryWriter;

public class Constant<T> extends Expression<T> {

  private static final int MAX_LITERAL_STRING_LENGTH = 250;

  private static final int PRECEDENCE = 1;

  // Control characters (x01 - x1f)
  // Single quotes
  // Delete (x7f)
  // Control characters (x80 - x9f)
  // Double quotes
  private static final String SQL_INJECTION_PATTERN = ".*[\\x01-\\x1f'\\x7f-\\x9f\"].*";

  // Properties

  private T value;
  private JDBCType type;

  private boolean parameterize;

  // Constructor

  public Constant(final T value, final JDBCType type) {
    super(PRECEDENCE);
    if (type == null) {
      throw new IllegalArgumentException("Specified type cannot be null");
    }
    if (this.isFloat(value) || this.isDouble(value) || this.isByteArray(value)) {
      this.parameterize = true;
    } else if (this.isString(value)) {
      String s = (String) value;
      if (s.length() > MAX_LITERAL_STRING_LENGTH) {
        this.parameterize = true;
      } else {
        this.parameterize = s.matches(SQL_INJECTION_PATTERN);
      }
    } else {
      this.parameterize = false;
    }
    this.type = type;
    this.value = value;
  }

  // boxing

  public static Constant<String> from(final String v) {
    return new Constant<String>(v, JDBCType.VARCHAR);
  }

  public static Constant<String> from(final Character v) {
    return new Constant<String>("" + v, JDBCType.VARCHAR);
  }

  public static Constant<Number> from(final Number v) {
    return new Constant<Number>(v, JDBCType.NUMERIC);
  }

  public static Constant<Boolean> from(final Boolean v) {
    return new Constant<Boolean>(v, JDBCType.BOOLEAN);
  }

  public static Constant<Date> from(final Date v) {
    return new Constant<Date>(v, JDBCType.TIMESTAMP);
  }

  // Utilities

  private boolean isFloat(final T n) {
    try {
      Float.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  private boolean isDouble(final T n) {
    try {
      Double.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  private boolean isByteArray(final T n) {
    try {
      byte[].class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  private boolean isString(final T n) {
    try {
      String.class.cast(n);
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
