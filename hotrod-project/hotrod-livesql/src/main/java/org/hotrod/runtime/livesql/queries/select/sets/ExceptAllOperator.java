package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.Select;

public class ExceptAllOperator<R> extends SetOperator<R> {

  public ExceptAllOperator(Select<R> a) {
    super(a);
  }

}
