package org.hotrod.dynamicsql.tuples;

public class Tuple5<A, B, C, D, E> extends Tuple4<A, B, C, D> {

  protected E e;

  public final E getE() {
    return e;
  }

  public final void setE(E e) {
    this.e = e;
  }

}
