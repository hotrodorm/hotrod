package org.hotrod.dynamicsql.tuples;

public class DTuple3<A, B, C> extends DTuple2<A, B> {

  protected C c;

  public final C getC() {
    return c;
  }

  public final void setC(C c) {
    this.c = c;
  }

}
