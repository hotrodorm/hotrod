package app.test.assembly;

public class Tuple2<A, B> {
  private A a;
  private B b;

  protected Tuple2(A a, B b) {
    this.a = a;
    this.b = b;
  }

  public A get1() {
    return a;
  }

  public <T> T get(Class<T> t) {
    return get(t, 1);
  }

  public <T> T get(Class<T> t, int index) {
    return null;
  }

  public B get2() {
    return b;
  }

//  public B get(B b, int index) {
//    return a;
//  }

  public UnboundColumns getUnboundColumns() {
    return null;
  }

}