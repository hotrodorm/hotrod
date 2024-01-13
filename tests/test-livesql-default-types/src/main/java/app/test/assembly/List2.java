package app.test.assembly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.test.assembly.List2.Tuple2;

public class List2<A, B> implements Iterable<Tuple2<A, B>> {

  private List<Tuple2<A, B>> list = new ArrayList<>();

  public void add(A a, B b) {
    this.list.add(new Tuple2<>(a, b));
  }

  @Override
  public Iterator<Tuple2<A, B>> iterator() {
    return this.list.iterator();
  }

  public static class Tuple2<A, B> {
    private A a;
    private B b;

    protected Tuple2(A a, B b) {
      this.a = a;
      this.b = b;
    }

    public A get1() {
      return a;
    }

    public B get2() {
      return b;
    }

  }

}
