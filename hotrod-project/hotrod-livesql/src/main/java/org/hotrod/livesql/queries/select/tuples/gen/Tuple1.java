package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;

public class Tuple1<A> {

  private A a;
  private Map<String, Object> unbound;

  @SuppressWarnings("unused")
  private Tuple1() {
  }

  public Tuple1(A a, Map<String, Object> unbound) {
    this.a = a;
    this.unbound = unbound;
  }

  public final A getA() {
    return a;
  }

  public final Map<String, Object> getUnbound() {
    return unbound;
  }

}
