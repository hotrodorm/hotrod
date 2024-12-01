package org.hotrod.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.generator.FileGenerator.TextWriter;

public class ClassWriter {

  private static final Logger log = Logger.getLogger(ClassWriter.class.getName());

  private ClassPackage classPackage;

  private List<String> segments;
  private LinkedHashMap<String, String> importCode; // import name / code name (full/simple)
  private Map<String, String> simpleImport; // simple name / import name

  public ClassWriter(final ClassPackage classPackage) {
    this.classPackage = classPackage;
    this.importCode = new LinkedHashMap<>();
    this.simpleImport = new LinkedHashMap<>();
    this.segments = new ArrayList<>();
  }

  // printers

  public void println() {
    this.segments.add("\n");
  }

  // start with s

  public void print(String s) {
    this.segments.add(s);
  }

  public void println(String s) {
    this.segments.add(s);
    this.println();
  }

  public void print(String s, Class<?> c) {
    this.print(s);
    this.print(c);
  }

  public void println(String s, Class<?> c) {
    this.print(s, c);
    this.println();
  }

  public void print(String s, Class<?> c, String s2) {
    this.print(s, c);
    this.print(s2);
  }

  public void println(String s, Class<?> c, String s2) {
    this.print(s, c, s2);
    this.println();
  }

  public void print(String s, Class<?> c, String s2, Class<?> c2) {
    this.print(s, c, s2);
    this.print(c2);
  }

  public void println(String s, Class<?> c, String s2, Class<?> c2) {
    this.print(s, c, s2, c2);
    this.println();
  }

  public void print(String s, Class<?> c, String s2, Class<?> c2, String s3) {
    this.print(s, c, s2, c2);
    this.print(s3);
  }

  public void println(String s, Class<?> c, String s2, Class<?> c2, String s3) {
    this.print(s, c, s2, c2, s3);
    this.println();
  }

  public void print(String s, Class<?> c, String s2, Class<?> c2, String s3, Class<?> c3) {
    this.print(s, c, s2, c2, s3);
    this.print(c3);
  }

  public void println(String s, Class<?> c, String s2, Class<?> c2, String s3, Class<?> c3) {
    this.print(s, c, s2, c2, s3, c3);
    this.println();
  }

  // start with c

  public void registerClass(Class<?> c) {
    getCodeName(c);
  }

  public void registerClass(String importName) {
    int dot = importName.lastIndexOf('.');
    String simpleName = dot == -1 ? importName : importName.substring(dot + 1);
    registerClass(importName, simpleName);
  }

  public void print(Class<?> c) {
    print(getCodeName(c));
  }

  public void println(Class<?> c) {
    this.print(c);
    this.println();
  }

  public void print(Class<?> c, String s) {
    print(c);
    print(s);
  }

  public void println(Class<?> c, String s) {
    this.print(c, s);
    this.println();
  }

  public void print(Class<?> c, String s, Class<?> c2) {
    print(c, s);
    print(c2);
  }

  public void println(Class<?> c, String s, Class<?> c2) {
    this.print(c, s, c2);
    this.println();
  }

  public void print(Class<?> c, String s, Class<?> c2, String s2) {
    print(c, s, c2);
    print(s2);
  }

  public void println(Class<?> c, String s, Class<?> c2, String s2) {
    this.print(c, s, c2, s2);
    this.println();
  }

  public void print(Class<?> c, String s, Class<?> c2, String s2, Class<?> c3) {
    print(c, s, c2, s2);
    print(c3);
  }

  public void println(Class<?> c, String s, Class<?> c2, String s2, Class<?> c3) {
    this.print(c, s, c2, s2, c3);
    this.println();
  }

  public void print(Class<?> c, String s, Class<?> c2, String s2, Class<?> c3, String s3) {
    print(c, s, c2, s2, c3);
    print(s3);
  }

  public void println(Class<?> c, String s, Class<?> c2, String s2, Class<?> c3, String s3) {
    this.print(c, s, c2, s2, c3, s3);
    this.println();
  }

  // writer

  public void write(final TextWriter w) throws IOException {

    w.write("package " + this.classPackage.getPackage() + ";\n\n");

    for (String i : this.importCode.keySet()) {
      w.write("import " + i + ";\n");
    }
    w.write("\n");

    for (String s : this.segments) {
      w.write(s);
    }

  }

  // Utils

  private String getCodeName(final Class<?> c) {
    String importName = c.getCanonicalName();
    String simpleName = c.getSimpleName();
    return registerClass(importName, simpleName);
  }

  private String registerClass(String importName, String simpleName) {
    if (importName.startsWith("java.lang.")) {
      return simpleName;
    }

    String registeredCodeName = this.importCode.get(importName);
    if (registeredCodeName != null) {
      return registeredCodeName;
    }

    // Not registered

    String otherSimpleName = this.simpleImport.get(simpleName);
    if (otherSimpleName == null) {
      this.importCode.put(importName, simpleName);
      this.simpleImport.put(simpleName, importName);
      return simpleName;
    } else {
      this.importCode.put(importName, importName);
      return importName;
    }
  }

}
