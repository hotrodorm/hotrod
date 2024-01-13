package app.test.domain;

import app.test.base.Table;

public class AutoTable<T> extends Table<T> {

  private T t;

  @Override
  public T getVO() {
    return t;
  }

}
