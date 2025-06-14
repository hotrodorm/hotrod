package org.hotrod.dynamicsql.tuples;

public class DTuple4<A, B, C, D> extends DTuple3<A, B, C> {

  protected D d;

  public final D getD() {
    return d;
  }

  public final void setD(D d) {
    this.d = d;
  }

}
