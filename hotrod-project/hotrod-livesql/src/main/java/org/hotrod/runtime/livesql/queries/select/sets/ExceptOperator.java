package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.Select;

public class ExceptOperator<R> extends SetOperator<R> {

  public ExceptOperator(Select<R> a) {
    super(a);
  }

}
