package org.hotrod.runtime.livesql.expressions.strings;

import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberConstant;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Like;
import org.hotrod.runtime.livesql.expressions.predicates.NotLike;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public abstract class StringExpression extends Expression<String> {

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

  public NumberExpression locate(final Expression<String> substring, final Expression<Number> from) {
    return new Locate(substring, this, from);
  }

  public NumberExpression locate(final Expression<String> substring, final Number from) {
    return new Locate(substring, this, new NumberConstant(from));
  }

  public NumberExpression locate(final String substring, final Expression<Number> from) {
    return new Locate(new StringConstant(substring), this, from);
  }

  public NumberExpression locate(final String substring, final Number from) {
    return new Locate(new StringConstant(substring), this, new NumberConstant(from));
  }

  public NumberExpression locate(final Expression<String> substring) {
    return new Locate(substring, this, new NumberConstant(0));
  }

  public NumberExpression locate(final String substring) {
    return new Locate(new StringConstant(substring), this, new NumberConstant(0));
  }

  // Substr

  public StringExpression substr(final Expression<Number> from, final Expression<Number> length) {
    return new Substring(this, from, length);
  }

  public StringExpression substr(final Expression<Number> from, final Number length) {
    return new Substring(this, from, new NumberConstant(length));
  }

  public StringExpression substr(Number from, final Expression<Number> length) {
    return new Substring(this, new NumberConstant(from), length);
  }

  public StringExpression substr(Number from, final Number length) {
    return new Substring(this, new NumberConstant(from), new NumberConstant(length));
  }

  public StringExpression substr(final Expression<Number> from) {
    return new Substring(this, from);
  }

  public StringExpression substr(final Number from) {
    return new Substring(this, new NumberConstant(from));
  }

  // General functions

  public StringExpression concat(final Expression<String> e) {
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

  public Predicate like(final Expression<String> e) {
    return new Like(this, e);
  }

  public Predicate like(final String value) {
    return new Like(this, new StringConstant(value));
  }

  // Like escape

  public Predicate like(final Expression<String> e, final Expression<String> escape) {
    return new Like(this, e, escape);
  }

  public Predicate like(final Expression<String> e, final String escape) {
    return new Like(this, e, new StringConstant(escape));
  }

  public Predicate like(final String e, final Expression<String> escape) {
    return new Like(this, new StringConstant(e), escape);
  }

  public Predicate like(final String e, final String escape) {
    return new Like(this, new StringConstant(e), new StringConstant(escape));
  }

  // Not Like

  public Predicate notLike(final Expression<String> e) {
    return new NotLike(this, e);
  }

  public Predicate notLike(final String e) {
    return new NotLike(this, new StringConstant(e));
  }

  // Not like escape

  public Predicate notLike(final Expression<String> e, final Expression<String> escape) {
    return new NotLike(this, e, escape);
  }

  public Predicate notLike(final Expression<String> e, final String escape) {
    return new NotLike(this, e, new StringConstant(escape));
  }

  public Predicate notLike(final String e, final Expression<String> escape) {
    return new NotLike(this, new StringConstant(e), escape);
  }

  public Predicate notLike(final String e, final String escape) {
    return new NotLike(this, new StringConstant(e), new StringConstant(escape));
  }

}
