package org.hotrod.dynamicsql.tuples;

public class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {

  protected D d;

  public final D getD() {
    return d;
  }

  public final void setD(D d) {
    this.d = d;
  }

}
