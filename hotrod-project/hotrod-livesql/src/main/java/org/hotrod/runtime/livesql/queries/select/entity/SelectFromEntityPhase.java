package org.hotrod.runtime.livesql.queries.select.entity;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;

public class SelectFromEntityPhase<A> extends LockableSelectEntityPhase<A> {

  private A a;

  // Constructor

  public SelectFromEntityPhase(final LiveSQLContext context, final CombinedSelectObject<A> combined,
      final TableExpression t, A a) {
    super(context, combined);
    this.a = a;
  }
  
  

}
