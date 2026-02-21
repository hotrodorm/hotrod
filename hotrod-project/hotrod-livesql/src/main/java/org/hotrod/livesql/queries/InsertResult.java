package org.hotrod.livesql.queries;

import java.util.List;
import java.util.stream.Collectors;

public class InsertResult<T> {

  private int count;

  private List<T> keys;

  public InsertResult(int count, List<T> keys) {
    this.count = count;
    this.keys = keys;
  }

  public final int getCount() {
    return count;
  }

  public final List<T> getKeys() {
    return keys;
  }

  @Override
  public String toString() {
    return "InsertResult [count=" + count + ", " + (keys == null ? "keys[null]"
        : "keys[" + keys.size() + "]=" + keys.stream().map(k -> "" + k).collect(Collectors.joining(","))) + "]";
  }

}
