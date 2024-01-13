package app.test;

public class AutoTable<T> extends Table<T> {

  private T t;

  @Override
  public T getVO() {
    return t;
  }

}
