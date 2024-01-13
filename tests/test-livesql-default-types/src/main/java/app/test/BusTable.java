package app.test;

public class BusTable<T> extends Table<T> {

  private T t;

  @Override
  public T getVO() {
    return t;
  }

}
