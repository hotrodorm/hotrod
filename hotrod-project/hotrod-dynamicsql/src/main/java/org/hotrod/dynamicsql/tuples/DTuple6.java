package org.hotrod.dynamicsql.tuples;

public class DTuple6<A, B, C, D, E, F> extends DTuple5<A, B, C, D, E> {

  protected F f;

  public final F getF() {
    return f;
  }

  public final void setF(F f) {
    this.f = f;
  }

}
