package org.hotrodorm.hotrod.poc.poc_partialcompiler;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class T1 {

  public static void main(final String[] args) {

    System.out.println("T1");

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

    // ====

    List<File> ls1 = Arrays.asList(new File("external-classes1/com/myapp/data/Car.java"));
    List<String> javacOptions = Arrays.asList("-d", "extout1");

    Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(ls1);
    Boolean b1 = compiler.getTask(null, null, null, javacOptions, null, compilationUnits1).call();

    System.out.println("T2 b1=" + b1);

    // ====

    List<File> ls2 = Arrays.asList(new File("external-classes2/com/myapp/client/Mechanic.java"));
    List<String> javacOptions2 = Arrays.asList("-cp", "extout1", "-d", "extout2");

    Iterable<? extends JavaFileObject> compilationUnits2 = fileManager.getJavaFileObjectsFromFiles(ls2);

    // reuse the same file manager to allow caching of jar files
    Boolean b2 = compiler.getTask(null, null, null, javacOptions2, null, compilationUnits2).call();

    System.out.println("T3 b2=" + b2);

  }

}
