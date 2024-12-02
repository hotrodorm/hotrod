package org.hotrod.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.generator.FileGenerator.TextWriter;

public class AbstractClassWriter {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(AbstractClassWriter.class.getName());

  private String[] headerLines;
  private ClassPackage classPackage;

  protected List<String> segments;
  private LinkedHashMap<String, String> importCode; // import name / code name (full/simple)
  private Map<String, String> simpleImport; // simple name / import name

  public AbstractClassWriter(final ClassPackage classPackage, final String... headerLines) {
    this.headerLines = headerLines;
    this.classPackage = classPackage;
    this.importCode = new LinkedHashMap<>();
    this.simpleImport = new LinkedHashMap<>();
    this.segments = new ArrayList<>();
  }

  public void println() {
    this.segments.add("\n");
  }

  public void writeTo(final TextWriter w) throws IOException {

    if (this.headerLines != null) {
      for (String h : this.headerLines) {
        w.write(h + "\n");
      }
    }

    w.write("package " + this.classPackage.getPackage() + ";\n\n");

    for (String i : this.importCode.keySet()) {
      String code = this.importCode.get(i);
      if (!i.equals(code)) {
        w.write("import " + i + ";\n");
      }
    }
    w.write("\n");

    for (String s : this.segments) {
      w.write(s);
    }

  }

  protected String registerClass(final ParsedClass pc) {
    if (pc.getImportName().startsWith("java.lang.") || isPrimitive(pc.getBaseClass())) {
      // skip registering
      return pc.getShortCodeName();
    }

    String registeredCodeName = this.importCode.get(pc.getImportName());
    if (registeredCodeName != null) {
      // already registered
      return registeredCodeName;
    }

    // Not registered

    String otherSimpleName = this.simpleImport.get(pc.getBaseClass());
    if (otherSimpleName == null) {
      this.importCode.put(pc.getImportName(), pc.getBaseClass());
      this.simpleImport.put(pc.getBaseClass(), pc.getImportName());
      return pc.getShortCodeName();
    } else {
      this.importCode.put(pc.getImportName(), pc.getImportName());
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

  public static class ExternalClass {

    private String canonicalName;

    private ExternalClass(String canonicalName) {
      this.canonicalName = canonicalName;
    }

    public static ExternalClass of(String canonicalName) {
      return new ExternalClass(canonicalName);
    }

    public String getCanonicalName() {
      return canonicalName;
    }

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
