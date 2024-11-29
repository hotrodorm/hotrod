package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.asymmetric.EqAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.EqAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.GeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.GeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.GtAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.GtAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.InSubquery;
import org.hotrod.runtime.livesql.expressions.asymmetric.LeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.LeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.LtAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.LtAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.NeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.NeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.NotInSubquery;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFreeExpression;
import org.hotrod.runtime.livesql.ordering.OrderByDirectionPhase;
import org.hotrod.runtime.livesql.queries.select.Select;

public abstract class ComparableExpression extends GenericExpression {

  // Constructor

  protected ComparableExpression(final int precedence) {
    super(precedence);
  }

  // Column ordering

  public final OrderByDirectionPhase asc() {
    return new OrderByDirectionPhase(this, true);
  }

  public final OrderByDirectionPhase desc() {
    return new OrderByDirectionPhase(this, false);
  }

  // In subquery

  public BooleanFreeExpression in(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new InSubquery(this, subquery);
  }

  public BooleanFreeExpression notIn(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NotInSubquery(this, subquery);
  }

  // Any

  public BooleanFreeExpression eqAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new EqAny(this, subquery);
  }

  public BooleanFreeExpression neAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NeAny(this, subquery);
  }

  public BooleanFreeExpression ltAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LtAny(this, subquery);
  }

  public BooleanFreeExpression leAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LeAny(this, subquery);
  }

  public BooleanFreeExpression gtAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GtAny(this, subquery);
  }

  public BooleanFreeExpression geAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GeAny(this, subquery);
  }

  // All

  public BooleanFreeExpression eqAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new EqAll(this, subquery);
  }

  public BooleanFreeExpression neAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NeAll(this, subquery);
  }

  public BooleanFreeExpression ltAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LtAll(this, subquery);
  }

  public BooleanFreeExpression leAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LeAll(this, subquery);
  }

  public BooleanFreeExpression gtAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GtAll(this, subquery);
  }

  public BooleanFreeExpression geAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GeAll(this, subquery);
  }

}
