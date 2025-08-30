package manual;

import java.io.File;
import java.io.IOException;

import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.LocalFileGenerator;

public class T2 {

  public static void main(String[] args) throws InvalidPackageException, IOException {

    FileGenerator fileGenerator = new LocalFileGenerator();
    ClassPackage cp = ClassPackage.parse("abc.def");
    File mc = new File("VO1.java");

    try (TextWriter tw = fileGenerator.createWriter(mc)) {

      ClassWriter w = new ClassWriter(cp);
      writeBody(w);
      w.writeTo(tw);

    }

    System.out.println("done.");

  }

  private static void writeBody(ClassWriter w) {
    
    

    w.print("public class VO1 {\n");

    w.print(" private ");
    w.print(Integer.class);
    w.print(" a;\n");

    w.print(" private ");
    w.print(ClassWriter.class);
    w.print(" b;\n");

    w.print(" private ");
    w.print(ExternalClass.of("my.library.tools.Calc[]"));
    w.print(" c;\n");

    w.print(" private ");
    w.print(ExternalClass.of("other.library.tools.Calc<>"));
    w.print(" d;\n");

    w.print("}\n");

  }

}
