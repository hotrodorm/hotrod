package org.app;

import java.lang.reflect.InvocationTargetException;

public class TestNone {

  public static void test()
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    try {
      Caller.callJar(TestNone.class.getClassLoader());
      throw new RuntimeException("Should not succeed whe classes are not in the class loader");
    } catch (ClassNotFoundException e) {
      // OK
      System.out.println("[ Test None - OK ]");

    }
  }

}
