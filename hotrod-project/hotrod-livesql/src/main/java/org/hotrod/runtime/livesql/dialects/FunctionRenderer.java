package org.hotrod.runtime.livesql.dialects;

import java.util.Date;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrodorm.hotrod.utils.Separator;

/**
 * Generic rendering class. Methods can be overwritten by specific dialects.
 */
public abstract class FunctionRenderer {

  // General purpose functions

  public <T> void coalesce(final QueryWriter w, final List<Expression<T>> values) {
    this.write(w, "coalesce", values);
  }

  public void groupConcat(final QueryWriter w, final boolean distinct, final Expression<String> value,
      final List<OrderingTerm> ordering, final Expression<String> separator) {
    w.write("group_concat(");
    if (distinct) {
      w.write("distinct ");
    }
    value.renderTo(w);
    if (ordering != null) {
      w.write("ORDER BY ");
      Separator sep = new Separator();
      for (OrderingTerm t : ordering) {
        w.write(sep.render());
        t.renderTo(w);
      }
    }
    if (separator != null) {
      w.write("separator ");
      separator.renderTo(w);
    }
    w.write(")");
  }

  // Arithmetic functions

  public void power(final QueryWriter w, final Expression<Number> x, final Expression<Number> exponent) {
    this.write(w, "power", x, exponent);
  }

  public void logarithm(final QueryWriter w, final Expression<Number> x, final Expression<Number> base) {
    if (base == null) {
      this.write(w, "log", x);
    } else {
      this.write(w, "log", base, x);
    }
  }

  public void round(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
    if (places == null) {
      this.write(w, "round", x);
    } else {
      this.write(w, "round", x, places);
    }
  }

  public void trunc(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
    if (places == null) {
      this.write(w, "trunc", x);
    } else {
      this.write(w, "trunc", x, places);
    }
  }

  public void abs(final QueryWriter w, final Expression<Number> x) {
    this.write(w, "abs", x);
  }

  public void signum(final QueryWriter w, final Expression<Number> x) {
    this.write(w, "sign", x);
  }

  public void neg(final QueryWriter w, final Expression<Number> x) {
    this.write(w, "-", x);
  }

  // String functions

  public void concat(final QueryWriter w, final List<Expression<String>> strings) {
    this.write(w, "concat", strings);
  }

  public void length(final QueryWriter w, final Expression<String> string) {
    this.write(w, "length", string);
  }

  public void lower(final QueryWriter w, final Expression<String> string) {
    this.write(w, "lower", string);
  }

  public void upper(final QueryWriter w, final Expression<String> string) {
    this.write(w, "upper", string);
  }

  public void locate(final QueryWriter w, final Expression<String> substring, final Expression<String> string,
      final Expression<Number> from) {
    if (from == null) {
      this.write(w, "locate", substring, string);
    } else {
      this.write(w, "locate", substring, string, from);
    }
  }

  public void substr(final QueryWriter w, final Expression<String> string, final Expression<Number> from,
      final Expression<Number> length) {
    if (length == null) {
      this.write(w, "substr", string, from);
    } else {
      this.write(w, "substr", string, from, length);
    }
  }

  public void trim(final QueryWriter w, final Expression<String> string) {
    this.write(w, "trim", string);
  }

  // Date/Time functions

  public void currentDate(final QueryWriter w) {
    w.write("current_date()");
  }

  public void currentTime(final QueryWriter w) {
    w.write("current_time()");
  }

  public void currentDateTime(final QueryWriter w) {
    w.write("current_timestamp()");
  }

  public void date(final QueryWriter w, final Expression<Date> datetime) {
    this.write(w, "date", datetime);
  }

  public void time(final QueryWriter w, final Expression<Date> datetime) {
    this.write(w, "time", datetime);
  }

  public void dateTime(final QueryWriter w, final Expression<Date> date, final Expression<Date> time) {
    this.write(w, "timestamp", date, time);
  }

  public void extract(final QueryWriter w, final Expression<Date> datetime, final DateTimeFieldExpression field) {
    w.write("extract(");
    field.renderTo(w);
    w.write(" from ");
    datetime.renderTo(w);
    w.write(")");
  }

  // Write utilities

  protected void write(final QueryWriter w, final String function, final Expression<?>... expressions) {
    w.write(function);
    w.write("(");
    Separator sep = new Separator();
    for (Expression<?> expr : expressions) {
      w.write(sep.render());
      expr.renderTo(w);
    }
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
