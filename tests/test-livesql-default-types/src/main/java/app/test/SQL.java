package app.test;

public class SQL {

  // Select1<AutoTable<Auto>, Auto> s = new Select1<>(a, new Auto());

  public <T extends Table<VO>, V extends VO> void select(T a) {
//    Select1<AutoTable<Auto>, Auto> s =
//        new Select1<T, V>(a, a.getVO());
//  return null;  
  }

}
