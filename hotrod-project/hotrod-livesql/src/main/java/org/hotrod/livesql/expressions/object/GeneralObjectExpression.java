package org.hotrod.livesql.expressions.object;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.predicates.Between;
import org.hotrod.livesql.expressions.predicates.Equal;
import org.hotrod.livesql.expressions.predicates.GreaterThan;
import org.hotrod.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.predicates.InList;
import org.hotrod.livesql.expressions.predicates.LessThan;
import org.hotrod.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.predicates.NotBetween;
import org.hotrod.livesql.expressions.predicates.NotEqual;
import org.hotrod.livesql.expressions.predicates.NotInList;
import org.hotrod.livesql.expressions.predicates.Predicate;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryObjectColumn;
import org.hotrod.livesql.util.BoxUtil;

public abstract class GeneralObjectExpression extends ComparableExpression {

  protected GeneralObjectExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new SubqueryObjectColumn(subquery, alias);
  }

  // Coalesce

  public ObjectExpression coalesce(final GeneralObjectExpression a) {
    return new ObjectCoalesce(this, a);
  }

  public ObjectExpression coalesce(final Object a) {
    return new ObjectCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public ObjectExpression nullIf(final GeneralObjectExpression a) {
    return new ObjectNullIf(this, a);
  }

  public ObjectExpression nullIf(final Object a) {
    return new ObjectNullIf(this, BoxUtil.box(a));
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralObjectExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final Object value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralObjectExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final Object value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralObjectExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final Object value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralObjectExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final Object value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralObjectExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final Object value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralObjectExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final Object value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralObjectExpression from, final GeneralObjectExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralObjectExpression from, final Object to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final Object from, final GeneralObjectExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final Object from, final Object to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralObjectExpression from, final GeneralObjectExpression to) {
    return new NotBetween<GeneralObjectExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralObjectExpression from, final Object to) {
    return new NotBetween<GeneralObjectExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final Object from, final GeneralObjectExpression to) {
    return new NotBetween<GeneralObjectExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final Object from, Object to) {
    return new NotBetween<GeneralObjectExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralObjectExpression... values) {
    return new InList<GeneralObjectExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final Object... values) {
    return new InList<GeneralObjectExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralObjectExpression... values) {
    return new NotInList<GeneralObjectExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final Object... values) {
    return new NotInList<GeneralObjectExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
