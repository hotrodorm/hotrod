package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;

public class Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> {

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
  private K k;
  private L l;
  private Map<String, Object> unbound;

  @SuppressWarnings("unused")
  private Tuple12() {
  }

  public Tuple12(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, Map<String, Object> unbound) {
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
    this.k = k;
    this.l = l;
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

  public final K getK() {
    return k;
  }

  public final L getL() {
    return l;
  }

  public final Map<String, Object> getUnbound() {
    return unbound;
  }

}
