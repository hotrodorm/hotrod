package org.hotrod.runtime.livesql.expressions.general;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class Constant<T> extends Expression<T> {

  private static final int MAX_LITERAL_STRING_LENGTH = 250;

  // Control characters (x01 - x1f)
  // Single quotes
  // Delete (x7f)
  // Control characters (x80 - x9f)
  // Double quotes
  private static final String SQL_INJECTION_PATTERN = ".*[\\x01-\\x1f'\\x7f-\\x9f\"].*";

  // Properties

  private T value;
  private boolean parameterize;

  // Constructor

  public Constant(final T value) {
    super(Expression.PRECEDENCE_LITERAL);
    if (this.isString(value)) {
      String s = (String) value;
      if (s.length() > MAX_LITERAL_STRING_LENGTH) {
        this.parameterize = true;
      } else {
        this.parameterize = s.matches(SQL_INJECTION_PATTERN);
      }
    } else if (this.isNumber(value)) {
      this.parameterize = this.isFloat(value) || this.isDouble(value);
    } else if (this.isDateTime(value)) {
      this.parameterize = true;
    } else if (this.isBoolean(value)) {
      this.parameterize = false;
    } else if (this.isByteArray(value)) {
      this.parameterize = true;
    } else { // Object
      this.parameterize = true;
    }
    this.value = value;
  }

  // Utilities

  private boolean isString(final T n) {
    try {
      String.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  private boolean isNumber(final T n) {
    try {
      Number.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

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

  private boolean isDateTime(final T n) {
    try {
      Date.class.cast(n);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  private boolean isBoolean(final T n) {
    try {
      Boolean.class.cast(n);
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

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.parameterize) {
      String name = w.registerParameter(this.value);
      w.write("#{" + name);
      w.write("}");
    } else {
      if (this.value instanceof String) {
        w.write("'" + this.value + "'");
      } else {
        w.write("" + this.value);
      }
    }
  }

  // Validation

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    // nothing to do
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    // nothing to do
  }

}
