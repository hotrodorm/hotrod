package org.hotrod.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.generator.FileGenerator.TextWriter;

public class ClassWriter {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ClassWriter.class.getName());

  private String[] headerLines;
  private ClassPackage classPackage;

  private List<String> segments;
  private LinkedHashMap<String, String> importCode; // import name / code name (full/simple)
  private Map<String, String> simpleImport; // simple name / import name

  public ClassWriter(final ClassPackage classPackage, final String... headerLines) {
    this.headerLines = headerLines;
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
    ParsedClass pc = ParsedClass.of(c);
    registerClass(pc);
  }

  public void registerClass(String fullClassName) {
    ParsedClass pc = ParsedClass.of(fullClassName);
    registerClass(pc);
  }

  public void printClass(String fullClassName) {
    ParsedClass pc = ParsedClass.of(fullClassName);
    print(registerClass(pc));
  }

  public void print(Class<?> c) {
    ParsedClass pc = ParsedClass.of(c);
    print(registerClass(pc));
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

  public void writeTo(final TextWriter w) throws IOException {

    if (this.headerLines != null) {
      for (String h : this.headerLines) {
        w.write(h + "\n");
      }
    }

    w.write("package " + this.classPackage.getPackage() + ";\n\n");

    for (String i : this.importCode.keySet()) {
//      log.info("### " + i);
      w.write("import " + i + ";\n");
    }
    w.write("\n");

    for (String s : this.segments) {
      w.write(s);
    }

  }

  // Utils

  private String registerClass(final ParsedClass pc) {
    if (pc.getImportName().startsWith("java.lang.") || isPrimitive(pc.getBaseClass())) {
      // skip registering
//      log.info("-- skip registering: " + pc.getOriginal());
      return pc.getShortCodeName();
    }

    String registeredCodeName = this.importCode.get(pc.getImportName());
    if (registeredCodeName != null) {
      // already registered
//      log.info("-- already registered: " + pc.getOriginal());
      return registeredCodeName;
    }

    // Not registered

    String otherSimpleName = this.simpleImport.get(pc.getBaseClass());
    if (otherSimpleName == null) {
      this.importCode.put(pc.getImportName(), pc.getBaseClass());
      this.simpleImport.put(pc.getBaseClass(), pc.getImportName());
//      log.info("-- register: " + pc.getShortCodeName() + " (import: " + pc.getImportName() + ")");
      return pc.getShortCodeName();
    } else {
      this.importCode.put(pc.getImportName(), pc.getImportName());
//      log.info("-- register: " + pc.getLongCodeName() + " (import: " + pc.getImportName() + ")");
      return pc.getLongCodeName();
    }
  }

  private boolean isPrimitive(String c) {
    return "byte".equals(c) //
        || "short".equals(c) //
        || "int".equals(c) //
        || "long".equals(c) //
        || "float".equals(c) //
        || "double".equals(c) //
        || "boolean".equals(c) //
        || "char".equals(c);
  }

  public static class ParsedClass {

    private String original;
    private String generics;
    private String arrays;
    private String importName;
    private String baseClass;
    private String shortCodeName;
    private String longCodeName;

    public static ParsedClass of(String c) {
      return new ParsedClass(c);
    }

    public static ParsedClass of(Class<?> c) {
      return new ParsedClass(c.getCanonicalName());
    }

    private ParsedClass(String c) {
      this.original = c;

      String gfree;
      int l = c.indexOf("<");
      int g = c.lastIndexOf(">");
      if (l != -1 && g != -1) {
        this.generics = c.substring(l, g + 1);
        gfree = c.substring(0, l) + c.substring(g + 1);
      } else {
        this.generics = null;
        gfree = c;
      }

      int b = gfree.indexOf("[");
      if (b != -1) {
        this.arrays = gfree.substring(b);
        this.importName = gfree.substring(0, b);
      } else {
        this.arrays = null;
        this.importName = gfree;
      }

      int dot = importName.lastIndexOf('.');
      this.baseClass = dot == -1 ? importName : importName.substring(dot + 1);
      this.shortCodeName = this.baseClass + coalesce(this.generics, "") + coalesce(this.arrays, "");
      this.longCodeName = this.importName + coalesce(this.generics, "") + coalesce(this.arrays, "");

    }

    private String coalesce(String a, String b) {
      return a != null ? a : b;
    }

    public String toString() {
      return "orig: " + this.original + " - generics: " + this.generics + " - arrays: " + this.arrays
          + " - importName: " + this.importName + " - baseClass: " + this.baseClass + " - shortCodeName: "
          + this.shortCodeName;
    }

    public String getOriginal() {
      return original;
    }

    public String getGenerics() {
      return generics;
    }

    public String getArrays() {
      return arrays;
    }

    public String getImportName() {
      return importName;
    }

    public String getBaseClass() {
      return baseClass;
    }

    public String getShortCodeName() {
      return this.shortCodeName;
    }

    public String getLongCodeName() {
      return longCodeName;
    }

  }

}
