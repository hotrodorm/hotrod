package app.test.assembly;

import java.util.Map;

public class XTuple1<A> {
  private A a;

  protected XTuple1(A a) {
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

  public Map<String, Object> getUnboundColumns() {
    return null;
  }

}