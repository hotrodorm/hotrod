package org.hotrod.runtime.livesql.dialects;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.ordering.OHelper;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrodorm.hotrod.utils.Separator;

/**
 * Generic rendering class. Methods can be overwritten by specific dialects.
 */
public abstract class FunctionRenderer {

  // General purpose functions

  public <T extends ComparableExpression> void coalesce(final QueryWriter w, final List<T> values) {
    this.write(w, "coalesce", values);
  }

  public <T extends ComparableExpression> void nullif(final QueryWriter w, final T a, final T b) {
    this.write(w, "nullif", a, b);
  }

  public void groupConcat(final QueryWriter w, final boolean distinct, final StringExpression value,
      final List<OrderingTerm> ordering, final StringExpression separator) {
    w.write("group_concat(");
    if (distinct) {
      w.write("distinct ");
    }
    Helper.renderTo(value, w);
    if (ordering != null) {
      w.write("ORDER BY ");
      Separator sep = new Separator();
      for (OrderingTerm t : ordering) {
        w.write(sep.render());
        OHelper.renderTo(t, w);
      }
    }
    if (separator != null) {
      w.write("separator ");
      Helper.renderTo(separator, w);
    }
    w.write(")");
  }

  // Arithmetic functions

  public void power(final QueryWriter w, final NumberExpression x, final NumberExpression exponent) {
    this.write(w, "power", x, exponent);
  }

  public void logarithm(final QueryWriter w, final NumberExpression x, final NumberExpression base) {
    if (base == null) {
      this.write(w, "log", x);
    } else {
      this.write(w, "log", base, x);
    }
  }

  public void remainder(final QueryWriter w, final NumberExpression a, final NumberExpression b) {
    this.write(w, "%", a, b);
  }

  public void round(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
    if (places == null) {
      this.write(w, "round", x);
    } else {
      this.write(w, "round", x, places);
    }
  }

  public void trunc(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
    if (places == null) {
      this.write(w, "trunc", x);
    } else {
      this.write(w, "trunc", x, places);
    }
  }

  public void abs(final QueryWriter w, final NumberExpression x) {
    this.write(w, "abs", x);
  }

  public void signum(final QueryWriter w, final NumberExpression x) {
    this.write(w, "sign", x);
  }

  public void neg(final QueryWriter w, final NumberExpression x) {
    this.write(w, "-", x);
  }

  // String functions

  public void concat(final QueryWriter w, final List<StringExpression> strings) {
    this.write(w, "concat", strings.toArray(new StringExpression[0]));
  }

  public void length(final QueryWriter w, final StringExpression string) {
    this.write(w, "length", string);
  }

  public void lower(final QueryWriter w, final StringExpression string) {
    this.write(w, "lower", string);
  }

  public void upper(final QueryWriter w, final StringExpression string) {
    this.write(w, "upper", string);
  }

  public void locate(final QueryWriter w, final StringExpression substring, final StringExpression string,
      final NumberExpression from) {
    if (from == null) {
      this.write(w, "locate", substring, string);
    } else {
      this.write(w, "locate", substring, string, from);
    }
  }

  public void substr(final QueryWriter w, final StringExpression string, final NumberExpression from,
      final NumberExpression length) {
    if (length == null) {
      this.write(w, "substr", string, from);
    } else {
      this.write(w, "substr", string, from, length);
    }
  }

  public void trim(final QueryWriter w, final StringExpression string) {
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

  public void date(final QueryWriter w, final DateTimeExpression datetime) {
    this.write(w, "date", datetime);
  }

  public void time(final QueryWriter w, final DateTimeExpression datetime) {
    this.write(w, "time", datetime);
  }

  public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
    this.write(w, "timestamp", date, time);
  }

  public void extract(final QueryWriter w, final DateTimeExpression datetime, final DateTimeFieldExpression field) {
    w.write("extract(");
    Helper.renderTo(field, w);
    w.write(" from ");
    Helper.renderTo(datetime, w);
    w.write(")");
  }

  // Write utilities

  protected void write(final QueryWriter w, final String function, final ComparableExpression... expressions) {
    w.write(function);
    w.write("(");
    Separator sep = new Separator();
    for (ComparableExpression expr : expressions) {
      w.write(sep.render());
      Helper.renderTo(expr, w);
    }
    w.write(")");
  }

  protected <T extends ComparableExpression> void write(final QueryWriter w, final String function, final List<T> x) {
    this.write(w, function, x, ", ");
  }

  protected <T extends ComparableExpression> void write(final QueryWriter w, final String function, final List<T> x,
      final String separator) {
    w.write(function);
    w.write("(");
    Separator sep = new Separator(separator);
    for (ComparableExpression expr : x) {
      w.write(sep.render());
      Helper.renderTo(expr, w);
    }
    w.write(")");
  }

}
