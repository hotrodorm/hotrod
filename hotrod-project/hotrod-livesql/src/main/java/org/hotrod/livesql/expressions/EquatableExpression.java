package org.hotrod.livesql.expressions;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.asymmetric.EqAll;
import org.hotrod.livesql.expressions.asymmetric.EqAny;
import org.hotrod.livesql.expressions.asymmetric.InSubquery;
import org.hotrod.livesql.expressions.asymmetric.NeAll;
import org.hotrod.livesql.expressions.asymmetric.NeAny;
import org.hotrod.livesql.expressions.asymmetric.NotInSubquery;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.queries.select.Select;

public abstract class EquatableExpression extends SortableExpression {

  // Constructor

  protected EquatableExpression(final int precedence) {
    super(precedence);
  }

  // In subquery

  public BooleanSyntaxExpression in(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new InSubquery(this, subquery);
  }

  public BooleanSyntaxExpression notIn(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NotInSubquery(this, subquery);
  }

  // Any

  public BooleanSyntaxExpression eqAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new EqAny(this, subquery);
  }

  public BooleanSyntaxExpression neAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NeAny(this, subquery);
  }

  // All

  public BooleanSyntaxExpression eqAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new EqAll(this, subquery);
  }

  public BooleanSyntaxExpression neAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NeAll(this, subquery);
  }

}
