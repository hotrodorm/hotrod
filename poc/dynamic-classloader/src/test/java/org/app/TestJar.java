package org.app;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import dynamic.classloader.ClassLoaderFactory;
import dynamic.classloader.ClassLoaderFactory.InvalidClassPathException;

public class TestJar {

  public static void test()
      throws InvalidClassPathException, ClassNotFoundException, InstantiationException, IllegalAccessException,
      NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {

    // 1. Create a class loader

    List<File> entries = new ArrayList<File>();
    entries.add(new File("dist/compressed.jar"));
    ClassLoader cl = ClassLoaderFactory.createClassLoader(entries);

    // 2. Use the class

    Caller.callJar(cl);

  }

}
