package org.hotrod.runtime.livesql.expressions.strings;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberConstant;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFreeExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Between;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFreeExpression;
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

public abstract class StringExpression extends ComparableExpression {

  protected StringExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public StringFreeExpression coalesce(final StringExpression a) {
    return new StringCoalesce(this, a);
  }

  public StringFreeExpression coalesce(final String a) {
    return new StringCoalesce(this, new StringConstant(a));
  }

  // NullIf

  public StringFreeExpression nullIf(final StringExpression a) {
    return new StringNullIf(this, a);
  }

  public StringFreeExpression nullIf(final String a) {
    return new StringNullIf(this, new StringConstant(a));
  }

  // Locate

  public NumberFreeExpression locate(final StringExpression substring, final NumberExpression from) {
    return new Locate(substring, this, from);
  }

  public NumberFreeExpression locate(final StringExpression substring, final Number from) {
    return new Locate(substring, this, new NumberConstant(from));
  }

  public NumberFreeExpression locate(final String substring, final NumberExpression from) {
    return new Locate(new StringConstant(substring), this, from);
  }

  public NumberFreeExpression locate(final String substring, final Number from) {
    return new Locate(new StringConstant(substring), this, new NumberConstant(from));
  }

  public NumberFreeExpression locate(final StringExpression substring) {
    return new Locate(substring, this, new NumberConstant(0));
  }

  public NumberFreeExpression locate(final String substring) {
    return new Locate(new StringConstant(substring), this, new NumberConstant(0));
  }

  // Substr

  public StringFreeExpression substr(final NumberExpression from, final NumberExpression length) {
    return new Substring(this, from, length);
  }

  public StringFreeExpression substr(final NumberExpression from, final Number length) {
    return new Substring(this, from, new NumberConstant(length));
  }

  public StringFreeExpression substr(Number from, final NumberExpression length) {
    return new Substring(this, new NumberConstant(from), length);
  }

  public StringFreeExpression substr(Number from, final Number length) {
    return new Substring(this, new NumberConstant(from), new NumberConstant(length));
  }

  public StringFreeExpression substr(final NumberExpression from) {
    return new Substring(this, from);
  }

  public StringFreeExpression substr(final Number from) {
    return new Substring(this, new NumberConstant(from));
  }

  // General functions

  public StringFreeExpression concat(final StringExpression e) {
    Concat concat = new Concat(this, e);
    return concat;
  }

  public StringFreeExpression concat(final String e) {
    Concat concat = new Concat(this, new StringConstant(e));
    return concat;
  }

  public NumberFreeExpression length() {
    return new Length(this);
  }

  public StringFreeExpression lower() {
    return new Lower(this);
  }

  public StringFreeExpression upper() {
    return new Upper(this);
  }

  public StringFreeExpression trim() {
    return new Trim(this);
  }

  // Like

  public BooleanFreeExpression like(final StringExpression e) {
    return new Like(this, e);
  }

  public BooleanFreeExpression like(final String value) {
    return new Like(this, new StringConstant(value));
  }

  // Like escape

  public BooleanFreeExpression like(final StringExpression e, final StringExpression escape) {
    return new Like(this, e, escape);
  }

  public BooleanFreeExpression like(final StringExpression e, final String escape) {
    return new Like(this, e, new StringConstant(escape));
  }

  public BooleanFreeExpression like(final String e, final StringExpression escape) {
    return new Like(this, new StringConstant(e), escape);
  }

  public BooleanFreeExpression like(final String e, final String escape) {
    return new Like(this, new StringConstant(e), new StringConstant(escape));
  }

  // Not Like

  public BooleanFreeExpression notLike(final StringExpression e) {
    return new NotLike(this, e);
  }

  public BooleanFreeExpression notLike(final String e) {
    return new NotLike(this, new StringConstant(e));
  }

  // Not like escape

  public BooleanFreeExpression notLike(final StringExpression e, final StringExpression escape) {
    return new NotLike(this, e, escape);
  }

  public BooleanFreeExpression notLike(final StringExpression e, final String escape) {
    return new NotLike(this, e, new StringConstant(escape));
  }

  public BooleanFreeExpression notLike(final String e, final StringExpression escape) {
    return new NotLike(this, new StringConstant(e), escape);
  }

  public BooleanFreeExpression notLike(final String e, final String escape) {
    return new NotLike(this, new StringConstant(e), new StringConstant(escape));
  }

  // TODO: implement in subclasses

  // Scalar comparisons

  // Equal

  public BooleanFreeExpression eq(final StringExpression e) {
    return new Equal(this, e);
  }

  public BooleanFreeExpression eq(final String value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanFreeExpression ne(final StringExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanFreeExpression ne(final String value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanFreeExpression gt(final StringExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanFreeExpression gt(final String value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanFreeExpression ge(final StringExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression ge(final String value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanFreeExpression lt(final StringExpression e) {
    return new LessThan(this, e);
  }

  public BooleanFreeExpression lt(final String value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanFreeExpression le(final StringExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression le(final String value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanFreeExpression between(final StringExpression from, final StringExpression to) {
    return new Between(this, from, to);
  }

  public BooleanFreeExpression between(final StringExpression from, final String to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression between(final String from, final StringExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression between(final String from, final String to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanFreeExpression notBetween(final StringExpression from, final StringExpression to) {
    return new NotBetween<StringExpression>(this, from, to);
  }

  public BooleanFreeExpression notBetween(final StringExpression from, final String to) {
    return new NotBetween<StringExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression notBetween(final String from, final StringExpression to) {
    return new NotBetween<StringExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression notBetween(final String from, String to) {
    return new NotBetween<StringExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanFreeExpression in(final StringExpression... values) {
    return new InList<StringExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression in(final String... values) {
    return new InList<StringExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanFreeExpression notIn(final StringExpression... values) {
    return new NotInList<StringExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression notIn(final String... values) {
    return new NotInList<StringExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

  // Specialized Functions

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public final NumberFreeExpression ascii() {
    return new Ascii(this);
  }

}
