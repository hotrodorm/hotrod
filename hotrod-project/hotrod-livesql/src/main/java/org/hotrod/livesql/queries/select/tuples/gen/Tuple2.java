package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;
import org.hotrod.livesql.queries.select.tuples.AbstractTuple;

public class Tuple2<A, B> extends AbstractTuple {

  private A a;
  private B b;

  public Tuple2(A a, B b, Map<String, Object> unbound) {
    super(unbound);
    this.a = a;
    this.b = b;
  }

  public final A getA() {
    return a;
  }

  public final B getB() {
    return b;
  }

}
