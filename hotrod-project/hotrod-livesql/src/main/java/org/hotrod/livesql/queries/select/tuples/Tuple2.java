package org.hotrod.livesql.queries.select.tuples;

import java.util.Map;

public class Tuple2<A, B> {

  private A a;
  private B b;
  private Map<String, Object> unbound;

  @SuppressWarnings("unused")
  private Tuple2() {
  }

  public Tuple2(A a, B b, Map<String, Object> unbound) {
    this.a = a;
    this.b = b;
    this.unbound = unbound;
  }

  public final A getA() {
    return a;
  }

  public final B getB() {
    return b;
  }

  public final Map<String, Object> getUnbound() {
    return unbound;
  }

}
