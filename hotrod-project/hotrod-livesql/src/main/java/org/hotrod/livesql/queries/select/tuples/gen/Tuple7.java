package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;
import org.hotrod.livesql.queries.select.tuples.AbstractTuple;

public class Tuple7<A, B, C, D, E, F, G> extends AbstractTuple {

  private A a;
  private B b;
  private C c;
  private D d;
  private E e;
  private F f;
  private G g;

  public Tuple7(A a, B b, C c, D d, E e, F f, G g, Map<String, Object> unbound) {
    super(unbound);
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
    this.f = f;
    this.g = g;
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

}
