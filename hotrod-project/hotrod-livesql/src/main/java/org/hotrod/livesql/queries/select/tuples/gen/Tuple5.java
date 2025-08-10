package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;

public class Tuple5<A, B, C, D, E> {

  private A a;
  private B b;
  private C c;
  private D d;
  private E e;
  private Map<String, Object> unbound;

  @SuppressWarnings("unused")
  private Tuple5() {
  }

  public Tuple5(A a, B b, C c, D d, E e, Map<String, Object> unbound) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
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

  public final E getE() {
    return e;
  }

  public final Map<String, Object> getUnbound() {
    return unbound;
  }

}
