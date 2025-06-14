package org.hotrod.dynamicsql.tuples;

public class DTuple5<A, B, C, D, E> extends DTuple4<A, B, C, D> {

  protected E e;

  public final E getE() {
    return e;
  }

  public final void setE(E e) {
    this.e = e;
  }

}
