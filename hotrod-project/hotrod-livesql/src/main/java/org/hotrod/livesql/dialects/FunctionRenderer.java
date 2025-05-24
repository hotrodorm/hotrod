package org.hotrod.livesql.dialects;

import java.util.List;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Helper;
import org.hotrod.livesql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.ordering.OHelper;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.utils.Separator;

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

  public void groupConcat(final QueryWriter w, final boolean distinct, final GeneralStringExpression value,
      final List<OrderingTerm> ordering, final GeneralStringExpression separator) {
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

  public void power(final QueryWriter w, final GeneralNumberExpression x, final GeneralNumberExpression exponent) {
    this.write(w, "power", x, exponent);
  }

  public void logarithm(final QueryWriter w, final GeneralNumberExpression x, final GeneralNumberExpression base) {
    if (base == null) {
      this.write(w, "log", x);
    } else {
      this.write(w, "log", base, x);
    }
  }

  public void remainder(final QueryWriter w, final GeneralNumberExpression a, final GeneralNumberExpression b) {
    this.write(w, "%", a, b);
  }

  public void round(final QueryWriter w, final GeneralNumberExpression x, final GeneralNumberExpression places) {
    if (places == null) {
      this.write(w, "round", x);
    } else {
      this.write(w, "round", x, places);
    }
  }

  public void trunc(final QueryWriter w, final GeneralNumberExpression x, final GeneralNumberExpression places) {
    if (places == null) {
      this.write(w, "trunc", x);
    } else {
      this.write(w, "trunc", x, places);
    }
  }

  public void abs(final QueryWriter w, final GeneralNumberExpression x) {
    this.write(w, "abs", x);
  }

  public void signum(final QueryWriter w, final GeneralNumberExpression x) {
    this.write(w, "sign", x);
  }

  public void neg(final QueryWriter w, final GeneralNumberExpression x) {
    this.write(w, "-", x);
  }

  // String functions

  public void concat(final QueryWriter w, final List<GeneralStringExpression> strings) {
    this.write(w, "concat", strings.toArray(new GeneralStringExpression[0]));
  }

  public void length(final QueryWriter w, final GeneralStringExpression string) {
    this.write(w, "length", string);
  }

  public void lower(final QueryWriter w, final GeneralStringExpression string) {
    this.write(w, "lower", string);
  }

  public void upper(final QueryWriter w, final GeneralStringExpression string) {
    this.write(w, "upper", string);
  }

  public void locate(final QueryWriter w, final GeneralStringExpression substring, final GeneralStringExpression string,
      final GeneralNumberExpression from) {
    if (from == null) {
      this.write(w, "locate", substring, string);
    } else {
      this.write(w, "locate", substring, string, from);
    }
  }

  public void substr(final QueryWriter w, final GeneralStringExpression string, final GeneralNumberExpression from,
      final GeneralNumberExpression length) {
    if (length == null) {
      this.write(w, "substr", string, from);
    } else {
      this.write(w, "substr", string, from, length);
    }
  }

  public void trim(final QueryWriter w, final GeneralStringExpression string) {
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

  public void date(final QueryWriter w, final GeneralDateTimeExpression datetime) {
    this.write(w, "date", datetime);
  }

  public void time(final QueryWriter w, final GeneralDateTimeExpression datetime) {
    this.write(w, "time", datetime);
  }

  public void dateTime(final QueryWriter w, final GeneralDateTimeExpression date, final GeneralDateTimeExpression time) {
    this.write(w, "timestamp", date, time);
  }

  public void extract(final QueryWriter w, final GeneralDateTimeExpression datetime, final DateTimeFieldExpression field) {
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
