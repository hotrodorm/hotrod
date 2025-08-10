package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;

public class Tuple6<A, B, C, D, E, F> {

  private A a;
  private B b;
  private C c;
  private D d;
  private E e;
  private F f;
  private Map<String, Object> unbound;

  @SuppressWarnings("unused")
  private Tuple6() {
  }

  public Tuple6(A a, B b, C c, D d, E e, F f, Map<String, Object> unbound) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
    this.f = f;
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

  public final F getF() {
    return f;
  }

  public final Map<String, Object> getUnbound() {
    return unbound;
  }

}
