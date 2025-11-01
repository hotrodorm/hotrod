package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;
import org.hotrod.livesql.queries.select.tuples.AbstractTuple;

public class Tuple1<A> extends AbstractTuple {

  private A a;

  public Tuple1(A a, Map<String, Object> unbound) {
    super(unbound);
    this.a = a;
  }

  public final A getA() {
    return a;
  }

}
