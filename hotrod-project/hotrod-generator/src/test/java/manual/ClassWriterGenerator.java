package manual;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.hotrod.utils.AbstractClassWriter.ExternalClass;

public class ClassWriterGenerator {

  public static void main(String[] args) throws IOException {
    File basedir = new File("src/main/java/org/hotrod/utils");
    try (BufferedWriter w = new BufferedWriter(new FileWriter(new File(basedir, "ClassWriter.java")))) {
      w.write("package org.hotrod.utils;\n\n");
      w.write("public class ClassWriter extends AbstractClassWriter {\n\n");

      w.write("  public ClassWriter(ClassPackage classPackage, String... headerLines) {\n");
      w.write("    super(classPackage, headerLines);\n");
      w.write("  }\n");

      writeLevels(w, 5);

      w.write("\n}\n");
    }
    System.out.println("done.");
  }

  private static Segment[] SEGMENTS = { new StringSegment(null), new ClassSegment(null),
      new ExternalClassSegment(null) };

  public static void writeLevels(final BufferedWriter w, int maxLevel) throws IOException {
    for (int i = 0; i < maxLevel; i++) {
      writeLevel(w, i);
    }
  }

  public static void writeLevel(final BufferedWriter w, int level) throws IOException {
    w.write("\n  // Using " + (level + 1) + " parameter(s)\n");
    nestWrite(w, new Stack<Segment>(), 0, level + 1);
  }

  private static void nestWrite(final BufferedWriter w, Stack<Segment> segments, int currentNesting, int maxNesting)
      throws IOException {
    if (currentNesting < maxNesting) {
      for (Segment blueprint : SEGMENTS) {
        Segment s = blueprint.instantiate(currentNesting);
        segments.push(s);
        nestWrite(w, segments, currentNesting + 1, maxNesting);
        segments.pop();
      }
    } else {
      List<Segment> params = segments.stream().collect(Collectors.toList());
      w.write(renderPrint(params, false));
      w.write(renderPrint(params, true));
    }

  }

  public static String renderPrint(final List<Segment> segments, boolean ln) {
    return "\n  public void print" + (ln ? "ln" : "") + "("
        + segments.stream().map(s -> s.parameterClass() + " " + s.name).collect(Collectors.joining(", ")) + ") {\n" //
        + segments.stream().map(s -> s.renderLines()).collect(Collectors.joining("\n", "", "\n")) //
        + (ln ? "    this.println();\n" : "") //
        + "  }\n";
  }

  public static abstract class Segment {

    protected String name;

    public Segment(String name) {
      this.name = name;
    }

    public abstract Segment instantiate(int ordinal);

    public abstract String parameterClass();

    public abstract String renderLines();

  }

  public static class StringSegment extends Segment {

    public StringSegment(String name) {
      super(name);
    }

    @Override
    public Segment instantiate(int ordinal) {
      return new StringSegment("s" + ordinal);
    }

    @Override
    public String parameterClass() {
      return "String";
    }

    @Override
    public String renderLines() {
      return "    this.segments.add(" + this.name + ");";
    }

  }

  public static class ClassSegment extends Segment {

    public ClassSegment(String name) {
      super(name);
    }

    @Override
    public Segment instantiate(int ordinal) {
      return new ClassSegment("c" + ordinal);
    }

    @Override
    public String parameterClass() {
      return "Class<?>";
    }

    @Override
    public String renderLines() {
      return "    this.segments.add(registerClass(ParsedClass.of(" + name + ")));";
    }

  }

  public static class ExternalClassSegment extends Segment {

    public ExternalClassSegment(String name) {
      super(name);
    }

    @Override
    public Segment instantiate(int ordinal) {
      return new ExternalClassSegment("e" + ordinal);
    }

    @Override
    public String parameterClass() {
      return ExternalClass.class.getSimpleName();
    }

    @Override
    public String renderLines() {
      return "    this.segments.add(registerClass(ParsedClass.of(" + name + ".getCanonicalName())));";
    }

  }

}
