package app.test;

public class AutoTable<T extends VO> extends Table<VO> {

  private T t;

  @Override
  public T getVO() {
    return t;
  }

}
