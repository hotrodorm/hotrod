package app.test.assembly;

import java.util.Map;

public class XTuple2<A, B> {
  private A a;
  private B b;

  protected XTuple2(A a, B b) {
    this.a = a;
    this.b = b;
  }

  public A get1() {
    return a;
  }

  public B get2() {
    return b;
  }

  public <T> T get(Class<T> t) {
    return get(t, 1);
  }

  public <T> T get(Class<T> t, int index) {
    return null;
  }

  public Map<String, Object> getUnboundColumns() {
    return null;
  }

}