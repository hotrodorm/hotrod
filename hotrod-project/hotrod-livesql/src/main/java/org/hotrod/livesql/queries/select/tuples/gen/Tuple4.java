package org.hotrod.livesql.queries.select.tuples.gen;

import java.util.Map;
import org.hotrod.livesql.queries.select.tuples.AbstractTuple;

public class Tuple4<A, B, C, D> extends AbstractTuple {

  private A a;
  private B b;
  private C c;
  private D d;

  public Tuple4(A a, B b, C c, D d, Map<String, Object> unbound) {
    super(unbound);
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
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

}
