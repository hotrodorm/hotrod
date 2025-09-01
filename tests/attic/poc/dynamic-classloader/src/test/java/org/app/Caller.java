package org.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Caller {

  public static void callJar(final ClassLoader cl) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    // 1. Instantiate the class

    Class<?> catClass = Class.forName("compressed.Cat", true, cl);
    Object c = catClass.newInstance();

    // 2. Use the class

    Method m = catClass.getMethod("walk", Integer.TYPE, Integer.TYPE);
    Object r = m.invoke(c, 10, 5);

    System.out.println("[ Test JAR - result=" + r + " ]");
    // int total = new Cat().walk(10, 5);
  }

  public static void callDir(final ClassLoader cl) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    // 1. Instantiate the class

    Class<?> elephantClass = Class.forName("exploded.Elephant", true, cl);
    Object e = elephantClass.newInstance();

    // 2. Use the class

    Method m = elephantClass.getMethod("play", Integer.TYPE);
    Object r = m.invoke(e, 7);

    System.out.println("[ Test DIR - result=" + r + " ]");
    // int total = new Elephant().play(7);
  }

}
