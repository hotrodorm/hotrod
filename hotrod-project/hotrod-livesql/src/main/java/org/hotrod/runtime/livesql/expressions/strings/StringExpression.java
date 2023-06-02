package org.hotrod.runtime.livesql.expressions.strings;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberConstant;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Between;
import org.hotrod.runtime.livesql.expressions.predicates.Equal;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThan;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.InList;
import org.hotrod.runtime.livesql.expressions.predicates.LessThan;
import org.hotrod.runtime.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.Like;
import org.hotrod.runtime.livesql.expressions.predicates.NotBetween;
import org.hotrod.runtime.livesql.expressions.predicates.NotEqual;
import org.hotrod.runtime.livesql.expressions.predicates.NotInList;
import org.hotrod.runtime.livesql.expressions.predicates.NotLike;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.postgresql.Ascii;
import org.hotrod.runtime.livesql.util.BoxUtil;

public abstract class StringExpression extends Expression {

  protected StringExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public StringExpression coalesce(final StringExpression a) {
    return new StringCoalesce(this, a);
  }

  public StringExpression coalesce(final String a) {
    return new StringCoalesce(this, new StringConstant(a));
  }

  // Locate

  public NumberExpression locate(final StringExpression substring, final NumberExpression from) {
    return new Locate(substring, this, from);
  }

  public NumberExpression locate(final StringExpression substring, final Number from) {
    return new Locate(substring, this, new NumberConstant(from));
  }

  public NumberExpression locate(final String substring, final NumberExpression from) {
    return new Locate(new StringConstant(substring), this, from);
  }

  public NumberExpression locate(final String substring, final Number from) {
    return new Locate(new StringConstant(substring), this, new NumberConstant(from));
  }

  public NumberExpression locate(final StringExpression substring) {
    return new Locate(substring, this, new NumberConstant(0));
  }

  public NumberExpression locate(final String substring) {
    return new Locate(new StringConstant(substring), this, new NumberConstant(0));
  }

  // Substr

  public StringExpression substr(final NumberExpression from, final NumberExpression length) {
    return new Substring(this, from, length);
  }

  public StringExpression substr(final NumberExpression from, final Number length) {
    return new Substring(this, from, new NumberConstant(length));
  }

  public StringExpression substr(Number from, final NumberExpression length) {
    return new Substring(this, new NumberConstant(from), length);
  }

  public StringExpression substr(Number from, final Number length) {
    return new Substring(this, new NumberConstant(from), new NumberConstant(length));
  }

  public StringExpression substr(final NumberExpression from) {
    return new Substring(this, from);
  }

  public StringExpression substr(final Number from) {
    return new Substring(this, new NumberConstant(from));
  }

  // General functions

  public StringExpression concat(final StringExpression e) {
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

  public Predicate like(final StringExpression e) {
    return new Like(this, e);
  }

  public Predicate like(final String value) {
    return new Like(this, new StringConstant(value));
  }

  // Like escape

  public Predicate like(final StringExpression e, final StringExpression escape) {
    return new Like(this, e, escape);
  }

  public Predicate like(final StringExpression e, final String escape) {
    return new Like(this, e, new StringConstant(escape));
  }

  public Predicate like(final String e, final StringExpression escape) {
    return new Like(this, new StringConstant(e), escape);
  }

  public Predicate like(final String e, final String escape) {
    return new Like(this, new StringConstant(e), new StringConstant(escape));
  }

  // Not Like

  public Predicate notLike(final StringExpression e) {
    return new NotLike(this, e);
  }

  public Predicate notLike(final String e) {
    return new NotLike(this, new StringConstant(e));
  }

  // Not like escape

  public Predicate notLike(final StringExpression e, final StringExpression escape) {
    return new NotLike(this, e, escape);
  }

  public Predicate notLike(final StringExpression e, final String escape) {
    return new NotLike(this, e, new StringConstant(escape));
  }

  public Predicate notLike(final String e, final StringExpression escape) {
    return new NotLike(this, new StringConstant(e), escape);
  }

  public Predicate notLike(final String e, final String escape) {
    return new NotLike(this, new StringConstant(e), new StringConstant(escape));
  }

  // TODO: implement in subclasses

  // Scalar comparisons

  // Equal

  public Predicate eq(final StringExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final String value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final StringExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final String value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final StringExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final String value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final StringExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final String value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final StringExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final String value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final StringExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final String value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final StringExpression from, final StringExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final StringExpression from, final String to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final String from, final StringExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final String from, final String to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final StringExpression from, final StringExpression to) {
    return new NotBetween<StringExpression>(this, from, to);
  }

  public Predicate notBetween(final StringExpression from, final String to) {
    return new NotBetween<StringExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final String from, final StringExpression to) {
    return new NotBetween<StringExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final String from, String to) {
    return new NotBetween<StringExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final StringExpression... values) {
    return new InList<StringExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final String... values) {
    return new InList<StringExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final StringExpression... values) {
    return new NotInList<StringExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final String... values) {
    return new NotInList<StringExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

  // Specialized Functions

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public final NumberExpression ascii() {
    return new Ascii(this);
  }

}
