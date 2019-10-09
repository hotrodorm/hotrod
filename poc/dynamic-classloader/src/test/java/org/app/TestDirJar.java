package org.app;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import dynamic.classloader.ClassLoaderFactory;
import dynamic.classloader.ClassLoaderFactory.InvalidClassPathException;

public class TestDirJar {

  public static void test() throws InvalidClassPathException, ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    // 1. Create a class loader

    List<File> entries = new ArrayList<File>();
    entries.add(new File("dist/compressed.jar"));
    entries.add(new File("build/exploded"));
    ClassLoader cl = ClassLoaderFactory.newClassLoader(entries);

    // 2. Use the class

    Caller.callDir(cl);
    Caller.callJar(cl);

  }

}
