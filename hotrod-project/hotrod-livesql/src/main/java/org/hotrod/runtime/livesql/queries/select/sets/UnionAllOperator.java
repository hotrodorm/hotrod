package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.Select;

public class UnionAllOperator<R> extends SetOperator<R> {

  public UnionAllOperator(Select<R> a) {
    super(a);
  }

}
