package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;

public class Tuple4<A, B, C, D> {

  private A a;
  private B b;
  private C c;
  private D d;
  private Map<String, Object> unbound;

  @SuppressWarnings("unused")
  private Tuple4() {
  }

  public Tuple4(A a, B b, C c, D d, Map<String, Object> unbound) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.unbound = unbound;
  }

  public final A getA() {
    return a;
  }

  public final B getB() {
    return b;
  }

  public final C getC() {
    return c;
  }

  public final D getD() {
    return d;
  }

  public final Map<String, Object> getUnbound() {
    return unbound;
  }

}
