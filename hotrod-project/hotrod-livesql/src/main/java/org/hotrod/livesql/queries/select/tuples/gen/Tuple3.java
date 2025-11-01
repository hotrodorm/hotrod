package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;
import org.hotrod.livesql.queries.select.tuples.AbstractTuple;

public class Tuple3<A, B, C> extends AbstractTuple {

  private A a;
  private B b;
  private C c;

  public Tuple3(A a, B b, C c, Map<String, Object> unbound) {
    super(unbound);
    this.a = a;
    this.b = b;
    this.c = c;
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

}
