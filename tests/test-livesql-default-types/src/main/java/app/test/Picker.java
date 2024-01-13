package app.test;

public class Picker<E extends VO> {

  private Table<E> list;

  public Picker(Table<E> list) {
    this.list = list;
  }

  public E first() {
    return null;
  }

}
