package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;
import org.hotrod.livesql.queries.select.tuples.AbstractTuple;

public class Tuple9<A, B, C, D, E, F, G, H, I> extends AbstractTuple {

  private A a;
  private B b;
  private C c;
  private D d;
  private E e;
  private F f;
  private G g;
  private H h;
  private I i;

  @SuppressWarnings("unused")
  private Tuple9() {
    super();
  }


  public Tuple9(A a, B b, C c, D d, E e, F f, G g, H h, I i, Map<String, Object> unbound) {
    super(unbound);
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
    this.f = f;
    this.g = g;
    this.h = h;
    this.i = i;
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

}
