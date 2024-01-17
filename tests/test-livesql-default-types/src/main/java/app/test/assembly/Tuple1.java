package app.test.assembly;

public class Tuple1<A> {
  private A a;

  protected Tuple1(A a) {
    this.a = a;
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

  public UnboundColumns getUnboundColumns() {
    return null;
  }

}