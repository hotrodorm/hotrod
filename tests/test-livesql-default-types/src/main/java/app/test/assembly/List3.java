package app.test.assembly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.test.assembly.List3.Tuple3;

public class List3<A, B, C> implements Iterable<Tuple3<A, B, C>> {

  private List<Tuple3<A, B, C>> list = new ArrayList<>();

  public void add(A a, B b, C c) {
    this.list.add(new Tuple3<>(a, b, c));
  }

  @Override
  public Iterator<Tuple3<A, B, C>> iterator() {
    return this.list.iterator();
  }

  public static class Tuple3<A, B, C> {
    private A a;
    private B b;
    private C c;

    protected Tuple3(A a, B b, C z) {
      this.a = a;
      this.b = b;
      this.c = z;
    }

    public A get1() {
      return a;
    }

    public B get2() {
      return b;
    }

    public C get3() {
      return c;
    }

  }

}
