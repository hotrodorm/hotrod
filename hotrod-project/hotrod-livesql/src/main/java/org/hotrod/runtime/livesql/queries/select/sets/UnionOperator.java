package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.Select;

public class UnionOperator<R> extends SetOperator<R> {

  public UnionOperator(Select<R> a) {
    super(a);
  }

}
