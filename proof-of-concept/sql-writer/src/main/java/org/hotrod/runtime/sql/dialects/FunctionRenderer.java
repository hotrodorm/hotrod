package org.hotrod.runtime.sql.dialects;

import java.util.Date;
import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.sql.expressions.strings.StringExpression;

import sql.util.Separator;

public abstract class FunctionRenderer {

  // General purpose

  public <T> void coalesce(final QueryWriter w, final List<Expression<T>> values) {
    w.write("coalesce(");
    Separator sep = new Separator();
    for (Expression<T> expr : values) {
      w.write(sep.render());
      expr.renderTo(w);
    }
    w.write(")");
  }

  // Math functions

  public void logarithm(final QueryWriter w, final Expression<Number> number, final Expression<Number> base) {
    w.write("log(");
    number.renderTo(w);
    w.write(", ");
    base.renderTo(w);
    w.write(")");
  }

  public void power(final QueryWriter w, final Expression<Number> number, final Expression<Number> exponent) {
    w.write("power(");
    number.renderTo(w);
    w.write(", ");
    exponent.renderTo(w);
    w.write(")");
  }

  public void round(final QueryWriter w, final Expression<Number> number, final Expression<Number> places) {
    w.write("round(");
    number.renderTo(w);
    w.write(", ");
    places.renderTo(w);
    w.write(")");
  }

  public void signum(final QueryWriter w, final Expression<Number> number) {
    w.write("sign(");
    number.renderTo(w);
    w.write(")");
  }

  public void abs(final QueryWriter w, final Expression<Number> number) {
    w.write("abs(");
    number.renderTo(w);
    w.write(")");
  }

  // String functions

  public void concat(final QueryWriter w, final List<StringExpression> strings) {
    Separator sep = new Separator(" || ");
    for (StringExpression s : strings) {
      w.write(sep.render());
      s.renderTo(w);
    }
  }

  public void length(final QueryWriter w, final StringExpression string) {
    w.write("length(");
    string.renderTo(w);
    w.write(")");
  }

  public void lower(final QueryWriter w, final StringExpression string) {
    w.write("lower(");
    string.renderTo(w);
    w.write(")");
  }

  public void upper(final QueryWriter w, final StringExpression string) {
    w.write("upper(");
    string.renderTo(w);
    w.write(")");
  }

  public void trim(final QueryWriter w, final StringExpression string) {
    w.write("trim(");
    string.renderTo(w);
    w.write(")");
  }

  public void position(final QueryWriter w, final StringExpression substring, final StringExpression string,
      final NumberExpression from) {
    w.write("position(");
    substring.renderTo(w);
    w.write(", ");
    string.renderTo(w);
    if (from != null) {
      w.write(", ");
      from.renderTo(w);
    }
    w.write(")");
  }

  public void substring(final QueryWriter w, final StringExpression string, final NumberExpression from,
      final NumberExpression length) {
    w.write("substring(");
    string.renderTo(w);
    w.write(", ");
    from.renderTo(w);
    w.write(", ");
    length.renderTo(w);
    w.write(")");
  }

  // Date/Time functions

  public void currentDate(final QueryWriter w) {
    w.write("current_date()");
  }

  public void currentTime(final QueryWriter w) {
    w.write("current_time()");
  }

  public void currentDateTime(final QueryWriter w) {
    w.write("current_datetime()");
  }

  public void date(final QueryWriter w, final Expression<Date> datetime) {
    w.write("date(");
    datetime.renderTo(w);
    w.write(")");
  }

  public void time(final QueryWriter w, final Expression<Date> datetime) {
    w.write("time(");
    datetime.renderTo(w);
    w.write(")");
  }

  public void dateTime(final QueryWriter w, final Expression<Date> date, final Expression<Date> time) {
    w.write("datetime(");
    date.renderTo(w);
    w.write(", ");
    time.renderTo(w);
    w.write(")");
  }

  public void extract(final QueryWriter w, final Expression<Date> datetime, final Expression<String> field) {
    w.write("extract(");
    field.renderTo(w);
    w.write(" from ");
    datetime.renderTo(w);
    w.write(")");
  }

  public void addInterval(final QueryWriter w, final Expression<Date> date, final Expression<Number> amount,
      final Expression<String> unit) {
    date.renderTo(w);
    w.write(" + interval ");
    amount.renderTo(w);
    w.write(" ");
    unit.renderTo(w);
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
