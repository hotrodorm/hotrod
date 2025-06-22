package org.hotrod.livesql.expressions.strings;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.Available;
import org.hotrod.livesql.dialects.Const;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.numbers.NumberConstant;
import org.hotrod.livesql.expressions.numbers.NumberExpression;
import org.hotrod.livesql.expressions.predicates.Between;
import org.hotrod.livesql.expressions.predicates.Equal;
import org.hotrod.livesql.expressions.predicates.GreaterThan;
import org.hotrod.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.predicates.InList;
import org.hotrod.livesql.expressions.predicates.LessThan;
import org.hotrod.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.predicates.Like;
import org.hotrod.livesql.expressions.predicates.NotBetween;
import org.hotrod.livesql.expressions.predicates.NotEqual;
import org.hotrod.livesql.expressions.predicates.NotInList;
import org.hotrod.livesql.expressions.predicates.NotLike;
import org.hotrod.livesql.expressions.predicates.Predicate;
import org.hotrod.livesql.expressions.strings.postgresql.Ascii;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryStringColumn;
import org.hotrod.livesql.util.BoxUtil;

public abstract class GeneralStringExpression extends ComparableExpression {

  protected GeneralStringExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new SubqueryStringColumn(subquery, alias);
  }

  // Coalesce

  public StringExpression coalesce(final GeneralStringExpression a) {
    return new StringCoalesce(this, a);
  }

  public StringExpression coalesce(final String a) {
    return new StringCoalesce(this, new StringConstant(a));
  }

  // NullIf

  public StringExpression nullIf(final GeneralStringExpression a) {
    return new StringNullIf(this, a);
  }

  public StringExpression nullIf(final String a) {
    return new StringNullIf(this, new StringConstant(a));
  }

  // Locate

  public NumberExpression locate(final GeneralStringExpression substring, final GeneralNumberExpression from) {
    return new Locate(substring, this, from);
  }

  public NumberExpression locate(final GeneralStringExpression substring, final Number from) {
    return new Locate(substring, this, new NumberConstant(from));
  }

  public NumberExpression locate(final String substring, final GeneralNumberExpression from) {
    return new Locate(new StringConstant(substring), this, from);
  }

  public NumberExpression locate(final String substring, final Number from) {
    return new Locate(new StringConstant(substring), this, new NumberConstant(from));
  }

  public NumberExpression locate(final GeneralStringExpression substring) {
    return new Locate(substring, this, new NumberConstant(0));
  }

  public NumberExpression locate(final String substring) {
    return new Locate(new StringConstant(substring), this, new NumberConstant(0));
  }

  // Substr

  public StringExpression substr(final GeneralNumberExpression from, final GeneralNumberExpression length) {
    return new Substring(this, from, length);
  }

  public StringExpression substr(final GeneralNumberExpression from, final Number length) {
    return new Substring(this, from, new NumberConstant(length));
  }

  public StringExpression substr(Number from, final GeneralNumberExpression length) {
    return new Substring(this, new NumberConstant(from), length);
  }

  public StringExpression substr(Number from, final Number length) {
    return new Substring(this, new NumberConstant(from), new NumberConstant(length));
  }

  public StringExpression substr(final GeneralNumberExpression from) {
    return new Substring(this, from);
  }

  public StringExpression substr(final Number from) {
    return new Substring(this, new NumberConstant(from));
  }

  // General functions

  public StringExpression concat(final GeneralStringExpression e) {
    Concat concat = new Concat(this, e);
    return concat;
  }

  public StringExpression concat(final String e) {
    Concat concat = new Concat(this, new StringConstant(e));
    return concat;
  }

  public NumberExpression length() {
    return new Length(this);
  }

  public StringExpression lower() {
    return new Lower(this);
  }

  public StringExpression upper() {
    return new Upper(this);
  }

  public StringExpression trim() {
    return new Trim(this);
  }

  // Like

  public Predicate like(final GeneralStringExpression e) {
    return new Like(this, e);
  }

  public Predicate like(final String value) {
    return new Like(this, new StringConstant(value));
  }

  // Like escape

  public Predicate like(final GeneralStringExpression e, final GeneralStringExpression escape) {
    return new Like(this, e, escape);
  }

  public Predicate like(final GeneralStringExpression e, final String escape) {
    return new Like(this, e, new StringConstant(escape));
  }

  public Predicate like(final String e, final GeneralStringExpression escape) {
    return new Like(this, new StringConstant(e), escape);
  }

  public Predicate like(final String e, final String escape) {
    return new Like(this, new StringConstant(e), new StringConstant(escape));
  }

  // Not Like

  public Predicate notLike(final GeneralStringExpression e) {
    return new NotLike(this, e);
  }

  public Predicate notLike(final String e) {
    return new NotLike(this, new StringConstant(e));
  }

  // Not like escape

  public Predicate notLike(final GeneralStringExpression e, final GeneralStringExpression escape) {
    return new NotLike(this, e, escape);
  }

  public Predicate notLike(final GeneralStringExpression e, final String escape) {
    return new NotLike(this, e, new StringConstant(escape));
  }

  public Predicate notLike(final String e, final GeneralStringExpression escape) {
    return new NotLike(this, new StringConstant(e), escape);
  }

  public Predicate notLike(final String e, final String escape) {
    return new NotLike(this, new StringConstant(e), new StringConstant(escape));
  }

  // TODO: implement in subclasses

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralStringExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final String value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralStringExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final String value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralStringExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final String value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralStringExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final String value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralStringExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final String value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralStringExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final String value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralStringExpression from, final GeneralStringExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralStringExpression from, final String to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final String from, final GeneralStringExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final String from, final String to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralStringExpression from, final GeneralStringExpression to) {
    return new NotBetween<GeneralStringExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralStringExpression from, final String to) {
    return new NotBetween<GeneralStringExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final String from, final GeneralStringExpression to) {
    return new NotBetween<GeneralStringExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final String from, String to) {
    return new NotBetween<GeneralStringExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralStringExpression... values) {
    return new InList<GeneralStringExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final String... values) {
    return new InList<GeneralStringExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralStringExpression... values) {
    return new NotInList<GeneralStringExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final String... values) {
    return new NotInList<GeneralStringExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

  // Specialized Functions

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public final GeneralNumberExpression ascii() {
    return new Ascii(this);
  }

}
