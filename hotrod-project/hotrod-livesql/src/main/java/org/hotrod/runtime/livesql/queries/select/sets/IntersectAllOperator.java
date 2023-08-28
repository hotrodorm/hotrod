package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.Select;

public class IntersectAllOperator<R> extends SetOperator<R> {

  public IntersectAllOperator(Select<R> a) {
    super(a);
  }

}
