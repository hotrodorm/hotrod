package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;

public class Tuple10<A, B, C, D, E, F, G, H, I, J> {

  private A a;
  private B b;
  private C c;
  private D d;
  private E e;
  private F f;
  private G g;
  private H h;
  private I i;
  private J j;
  private Map<String, Object> unbound;

  @SuppressWarnings("unused")
  private Tuple10() {
  }

  public Tuple10(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, Map<String, Object> unbound) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
    this.f = f;
    this.g = g;
    this.h = h;
    this.i = i;
    this.j = j;
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

  public final G getG() {
    return g;
  }

  public final H getH() {
    return h;
  }

  public final I getI() {
    return i;
  }

  public final J getJ() {
    return j;
  }

  public final Map<String, Object> getUnbound() {
    return unbound;
  }

}
