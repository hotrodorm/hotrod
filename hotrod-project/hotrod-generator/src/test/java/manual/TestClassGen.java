package manual;

import java.io.File;
import java.io.IOException;

import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.LocalFileGenerator;

public class TestClassGen {

  public static void main(final String[] args) throws InvalidPackageException, IOException {

    ClassPackage p = new ClassPackage("abc.def");
    ClassWriter w = new ClassWriter(p);

    w.println();
    w.println("public class T1 {");
    w.println("  private Integer a;");

    w.println("  private ", java.util.Date.class, " ud;");
    w.println("  private ", java.sql.Date.class, " sd;");
    w.println("  private ", java.util.Date.class, " ud2;");
    w.println("  private ", java.sql.Date.class, " sd2;");
    w.println("  private ", MyClass.class, " sd2;");

    w.println("}");

    FileGenerator g = new LocalFileGenerator();
    try (TextWriter tw = g.createWriter(new File("T1.java"))) {
      w.write(tw);
    }

  }

  public static class MyClass {

  }

}
