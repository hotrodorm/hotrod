package org.hotrod.livesql.queries.select;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Gen {

  private static String SRC_DIR = "src/test/java/org/hotrod/livesql/queries/select/src";
  private static String DEST_DIR = "src/main/java/org/hotrod/livesql/queries/select/tuples/gen";
  private static int MAX_SIZE = 26;

  public static void main(String[] args) throws IOException {
    System.out.println("Starting...");
    genSelectTuples();
    genTuple();
    genFactory();
    System.out.println("Completed.");
  }

  private static void genSelectTuples() throws IOException {
    String template = loadFileAsString(new File(SRC_DIR + "/TemplateSelectTuples.java.template"));
    String templateLast = loadFileAsString(new File(SRC_DIR + "/TemplateLastSelectTuples.java.template"));
    for (int i = 1; i <= MAX_SIZE; i++) {
      String tuplesc = stream(i).collect(Collectors.joining(", "));
      String tuplesn = stream(i + 1).collect(Collectors.joining(", "));
      String c = Character.toString((char) (int) ('A' + i));

      String classp = "SelectTuplesFrom" + i + "Phase";
      String classc = "SelectTuplesFrom" + i + "Phase<" + tuplesc + ">";
      String classn = "SelectTuplesFrom" + (i + 1) + "Phase<" + tuplesn + ">";
      String classcNew = "SelectTuplesFrom" + i + "Phase<>";
      String classnNew = "SelectTuplesFrom" + (i + 1) + "Phase<>";
      String tupleClassc = "Tuple" + i + "<" + tuplesc + ">";
//      String t = "<T extends TableOrView<" + c + ">, " + c + ">";
      String t = "<T0 extends TableOrView<" + c + ", ?, ?>, " + c + ">";

      String temp = i < MAX_SIZE ? template : templateLast;
      String ready = temp //
          .replace("@@classp@@", classp) //
          .replace("@@classc@@", classc) //
          .replace("@@classn@@", classn) //
          .replace("@@classcNew@@", classcNew) //
          .replace("@@classnNew@@", classnNew) //
          .replace("@@tupleClassc@@", tupleClassc) //
          .replace("@@t@@", t) //
      ;

      File dest = new File(DEST_DIR + "/" + classp + ".java");
      saveStringToFile(ready, dest);
    }
  }

  private static void genTuple() throws IOException {
    for (int i = 1; i <= MAX_SIZE; i++) {
      int size = i;
      String className = "Tuple" + i;
      String name = DEST_DIR + "/" + className + ".java";
      try (BufferedWriter w = new BufferedWriter(new FileWriter(new File(name)))) {
        w.write("package org.hotrod.livesql.queries.select.tuples.gen;\n\n");
        w.write("import java.util.Map;\n" + "import org.hotrod.livesql.queries.select.tuples.AbstractTuple;\n\n");
        w.write("public class ");
        w.write(
            className + "<" + stream(size).collect(Collectors.joining(", ")) + ">" + " extends AbstractTuple {\n\n");
        stream(size).forEach(x -> write(w, "  private " + x + " " + x.toLowerCase() + ";\n"));
//        w.write("  private Map<String, Object> unbound;\n");

        w.write("\n" + "  @SuppressWarnings(\"unused\")\n" + "  private " + className + "() {\n" + "    super();\n"
            + "  }\n" + "\n");

        w.write("\n  public " + className + "("
            + stream(size).map(x -> x + " " + x.toLowerCase()).collect(Collectors.joining(", "))
            + ", Map<String, Object> unbound) {\n" + "");
        w.write("    super(unbound);\n");
        stream(size).forEach(x -> write(w, "    this." + x.toLowerCase() + " = " + x.toLowerCase() + ";\n"));
        w.write("  }\n");
        stream(size).forEach(x -> write(w,
            "\n" + "  public final " + x + " get" + x + "() {\n" + "    return " + x.toLowerCase() + ";\n" + "  }\n"));
//        w.write("\n" + "  public final Map<String, Object> getUnbound() {\n" + "    return unbound;\n" + "  }\n" + "\n"
//            + "}\n");
        w.write("\n}\n");
      }
    }
  }

  private static void genFactory() throws IOException {
    String name = DEST_DIR + "/TupleClassFactory.java";
    try (BufferedWriter w = new BufferedWriter(new FileWriter(new File(name)))) {
      w.write("package org.hotrod.livesql.queries.select.tuples.gen;\n\n" //
          + "\nimport org.hotrod.livesql.exceptions.LiveSQLException;\n\n" //
          + "public class TupleClassFactory {\n" + "\n"
          + "  public static Class<?> getTuplesClass(int modelInstancesCount) {\n"
          + "    switch (modelInstancesCount) {\n");
      IntStream.rangeClosed(1, MAX_SIZE)
          .forEach(x -> write(w, "    case " + x + ":\n" + "      return Tuple" + x + ".class;\n"));
      w.write("    default:\n"
          + "      throw new LiveSQLException(\"Cannot find class for tuples: invalid instance count of \" + modelInstancesCount);\n"
          + "    }\n" + "  }\n" + "\n" + "}\n");
    }
  }

  // Utils

  static void write(Writer w, String s) {
    try {
      w.write(s);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static String generateList(int size) {
    return stream(size).collect(Collectors.joining(", "));
  }

  static Stream<String> stream(int size) {
    return Stream.iterate((char) 'A', x -> Character.valueOf((char) (x + 1))).map(y -> "" + y).limit(size);
  }

  static void saveStringToFile(final String txt, final File f) throws IOException {
    try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
      w.write(txt);
    }
  }

  static String loadFileAsString(final File f) throws IOException {
    return loadFileAsString(f, null);
  }

  static String loadFileAsString(final File f, final String readerEncoding) throws IOException {
    if (readerEncoding != null) {
      try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), readerEncoding))) {
        return loadReaderIntoString(r);
      }
    } else { // Use UTF-8 as default encoding
      try (BufferedReader r = new BufferedReader(
          new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
        return loadReaderIntoString(r);
      }
    }
  }

  static String loadReaderIntoString(BufferedReader r) throws IOException {
    String line;
    StringBuilder sb = new StringBuilder();
    while ((line = r.readLine()) != null) {
      sb.append(line);
      sb.append("\n");
    }
    return sb.toString();
  }

}
