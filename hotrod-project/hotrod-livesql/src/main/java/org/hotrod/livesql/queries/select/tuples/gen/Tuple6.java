package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;
import org.hotrod.livesql.queries.select.tuples.AbstractTuple;

public class Tuple6<A, B, C, D, E, F> extends AbstractTuple {

  private A a;
  private B b;
  private C c;
  private D d;
  private E e;
  private F f;

  @SuppressWarnings("unused")
  private Tuple6() {
    super();
  }


  public Tuple6(A a, B b, C c, D d, E e, F f, Map<String, Object> unbound) {
    super(unbound);
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
    this.f = f;
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

}
