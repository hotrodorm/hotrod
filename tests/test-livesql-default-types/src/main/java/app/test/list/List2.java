package app.test.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.test.list.List2.Tuple2;

public class List2<A, B> implements Iterable<Tuple2<A, B>> {

  private List<Tuple2<A, B>> list = new ArrayList<>();

  public void add(A a, B b) {
    this.list.add(new Tuple2<>(a, b));
  }

  @Override
  public Iterator<Tuple2<A, B>> iterator() {
    return this.list.iterator();
  }

  public static class Tuple2<X, Y> {
    private X a;
    private Y b;

    protected Tuple2(X a, Y b) {
      this.a = a;
      this.b = b;
    }

    public X getA() {
      return a;
    }

    public Y getB() {
      return b;
    }

  }

}
