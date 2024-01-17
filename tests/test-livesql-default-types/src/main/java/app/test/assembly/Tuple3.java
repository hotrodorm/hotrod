package app.test.assembly;

public class Tuple3<A, B, C> {
  private A a;
  private B b;
  private C c;

  public Tuple3(A a, B b, C z) {
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