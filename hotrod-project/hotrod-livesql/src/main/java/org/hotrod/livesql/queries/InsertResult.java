package org.hotrod.livesql.queries;

import java.util.List;

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

}
