package org.app;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import dynamic.classloader.ClassLoaderFactory.InvalidClassPathException;

public class MyApp {

  public static void main(final String[] args)
      throws InvalidClassPathException, ClassNotFoundException, InstantiationException, IllegalAccessException,
      NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, SQLException {

    // TestNone.test();
    // TestJar.test();
    // TestDir.test();
    // TestDirJar.test();
    TestJDBCJar.test();

  }

}
