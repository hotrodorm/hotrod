package org.hotrod.runtime.sql.dialects;

import java.util.Date;
import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.datetime.DateTimeFieldExpression;

import sql.util.Separator;

public abstract class FunctionRenderer {

  // General purpose functions

  public <T> void coalesce(final QueryWriter w, final List<Expression<T>> values) {
    this.write(w, "COALESCE", values);
  }

  // Math functions

  public void power(final QueryWriter w, final Expression<Number> x, final Expression<Number> exponent) {
    this.write(w, "POWER", x, exponent);
  }

  public void logarithm(final QueryWriter w, final Expression<Number> x, final Expression<Number> base) {
    if (base == null) {
      this.write(w, "LOG", x);
    } else {
      this.write(w, "LOG", base, x);
    }
  }

  public void round(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
    this.write(w, "ROUND", x, places);
  }

  public void trunc(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
    this.write(w, "TRUNC", x, places);
  }

  public void abs(final QueryWriter w, final Expression<Number> x) {
    this.write(w, "ABC", x);
  }

  public void signum(final QueryWriter w, final Expression<Number> x) {
    this.write(w, "SIGN", x);
  }

  public void neg(final QueryWriter w, final Expression<Number> x) {
    this.write(w, "-", x);
  }

  // String functions

  public void concat(final QueryWriter w, final List<Expression<String>> strings) {
    this.write(w, "CONCAT", strings);
  }

  public void length(final QueryWriter w, final Expression<String> string) {
    this.write(w, "LENGTH", string);
  }

  public void lower(final QueryWriter w, final Expression<String> string) {
    this.write(w, "LOWER", string);
  }

  public void upper(final QueryWriter w, final Expression<String> string) {
    this.write(w, "UPPER", string);
  }

  public void locate(final QueryWriter w, final Expression<String> substring, final Expression<String> string,
      final Expression<Number> from) {
    if (from == null) {
      this.write(w, "LOCATE", substring, string);
    } else {
      this.write(w, "LOCATE", substring, string, from);
    }
  }

  public void substr(final QueryWriter w, final Expression<String> string, final Expression<Number> from,
      final Expression<Number> length) {
    if (length == null) {
      this.write(w, "SUBSTR", string, from);
    } else {
      this.write(w, "SUBSTR", string, from, length);
    }
  }

  public void trim(final QueryWriter w, final Expression<String> string) {
    this.write(w, "TRIM", string);
  }

  // Date/Time functions

  public void currentDate(final QueryWriter w) {
    w.write("CURRENT_DATE()");
  }

  public void currentTime(final QueryWriter w) {
    w.write("CURRENT_TIME()");
  }

  public void currentDateTime(final QueryWriter w) {
    w.write("CURRENT_TIMESTAMP()");
  }

  public void date(final QueryWriter w, final Expression<Date> datetime) {
    this.write(w, "DATE", datetime);
  }

  public void time(final QueryWriter w, final Expression<Date> datetime) {
    this.write(w, "TIME", datetime);
  }

  public void dateTime(final QueryWriter w, final Expression<Date> date, final Expression<Date> time) {
    this.write(w, "TIMESTAMP", date, time);
  }

  public void extract(final QueryWriter w, final Expression<Date> datetime, final DateTimeFieldExpression field) {
    w.write("EXTRACT(");
    field.renderTo(w);
    w.write(" FROM ");
    datetime.renderTo(w);
    w.write(")");
  }

  // Write utilities

  protected <T> void write(final QueryWriter w, final String function, final Expression<T> x) {
    w.write(function);
    w.write("(");
    x.renderTo(w);
    w.write(")");
  }

  protected <T, U> void write(final QueryWriter w, final String function, final Expression<T> x,
      final Expression<U> y) {
    w.write(function);
    w.write("(");
    x.renderTo(w);
    w.write(", ");
    y.renderTo(w);
    w.write(")");
  }

  protected <T, U, V> void write(final QueryWriter w, final String function, final Expression<T> x,
      final Expression<U> y, final Expression<V> z) {
    w.write(function);
    w.write("(");
    x.renderTo(w);
    w.write(", ");
    y.renderTo(w);
    w.write(", ");
    z.renderTo(w);
    w.write(")");
  }

  protected <T> void write(final QueryWriter w, final String function, final List<Expression<T>> x) {
    this.write(w, function, x, ", ");
  }

  protected <T> void write(final QueryWriter w, final String function, final List<Expression<T>> x,
      final String separator) {
    w.write(function);
    w.write("(");
    Separator sep = new Separator(separator);
    for (Expression<?> expr : x) {
      w.write(sep.render());
      expr.renderTo(w);
    }
    w.write(")");
  }

}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
