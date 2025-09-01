package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.SQL;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Like;
import org.hotrod.runtime.livesql.expressions.predicates.NotLike;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public abstract class StringExpression extends Expression<String> {

  protected StringExpression(final int precedence) {
    super(precedence);
  }

  // Like

  public Predicate like(final Expression<String> e) {
    return new Like(this, e);
  }

  public Predicate like(final String value) {
    return new Like(this, SQL.box(value));
  }

  // Like escape

  public Predicate like(final Expression<String> e, final Expression<String> escape) {
    return new Like(this, e, escape);
  }

  public Predicate like(final Expression<String> e, final String escape) {
    return new Like(this, e, SQL.box(escape));
  }

  public Predicate like(final String e, final Expression<String> escape) {
    return new Like(this, SQL.box(e), escape);
  }

  public Predicate like(final String e, final String escape) {
    return new Like(this, SQL.box(e), SQL.box(escape));
  }

  // Not Like

  public Predicate notLike(final Expression<String> e) {
    return new NotLike(this, e);
  }

  public Predicate notLike(final String e) {
    return new NotLike(this, SQL.box(e));
  }

  // Not like escape

  public Predicate notLike(final Expression<String> e, final Expression<String> escape) {
    return new NotLike(this, e, escape);
  }

  public Predicate notLike(final Expression<String> e, final String escape) {
    return new NotLike(this, e, SQL.box(escape));
  }

  public Predicate notLike(final String e, final Expression<String> escape) {
    return new NotLike(this, SQL.box(e), escape);
  }

  public Predicate notLike(final String e, final String escape) {
    return new NotLike(this, SQL.box(e), SQL.box(escape));
  }

}
