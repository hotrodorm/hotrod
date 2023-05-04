package org.hotrod.runtime.livesql;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Row implements Map<String, Object> {

  private Map<String, Object> properties = new HashMap<>();

  @Override
  public int size() {
    return this.properties.size();
  }

  @Override
  public boolean isEmpty() {
    return this.properties.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return this.properties.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return this.properties.containsValue(value);
  }

  @Override
  public Object get(Object key) {
    return this.properties.get(key);
  }

  @Override
  public Object put(String key, Object value) {
    return this.properties.put(key, value);
  }

  @Override
  public Object remove(Object key) {
    return this.remove(key);
  }

  @Override
  public void putAll(Map<? extends String, ? extends Object> m) {
    this.properties.putAll(m);
  }

  @Override
  public void clear() {
    this.properties.clear();
  }

  @Override
  public Set<String> keySet() {
    return this.properties.keySet();
  }

  @Override
  public Collection<Object> values() {
    return this.properties.values();
  }

  @Override
  public Set<Entry<String, Object>> entrySet() {
    return this.properties.entrySet();
  }

  public String toString() {
    return this.properties.toString();
  }

}
