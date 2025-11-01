package org.hotrod.livesql.queries.select.tuples;

import java.util.HashMap;
import java.util.Map;

public class AbstractTuple {

  private Map<String, Object> unbound;

  public AbstractTuple(Map<String, Object> unbound) {
    this.unbound = unbound == null ? new HashMap<>() : unbound;
  }

  public final Map<String, Object> getUnbound() {
    return unbound;
  }

  public final Object get(String name) {
    return this.unbound.get(name);
  }

  public final <T> T get(String name, Class<T> c) {
    return c.cast(this.unbound.get(name));
  }

}
