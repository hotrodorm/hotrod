package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.TypeSolverTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;

public class TypeSolverWriter {

  private static final Logger log = LogManager.getLogger(TypeSolverWriter.class);

  private DataSetLayout layout;
  private TypeSolverTag typeSolver;

  private TextWriter w;

  public TypeSolverWriter(final DataSetLayout layout, final TypeSolverTag typeSolver) {
    this.layout = layout;
    this.typeSolver = typeSolver;
  }

  public void generate(final FileGenerator fileGenerator, final MyBatisSpringGenerator mg)
      throws UncontrolledException, ControlledException {

    File dir = this.layout.getDaoPrimitivePackageDir(null);
    File f = new File(dir, "TypeSolver.class");
    log.info("f=" + f);

    try {
      this.w = fileGenerator.createWriter(f);

      this.writeHeader();

      this.writeFooter();

    } catch (IOException e) {
      throw new UncontrolledException("Could not generate Type solver class", e);
    } finally {
      try {
        w.close();
      } catch (IOException e) {
        throw new UncontrolledException("Could not generate Type solver class", e);
      }
    }

  }

  private void writeHeader() throws IOException {

    println("package " + this.layout.getDAOPrimitivePackage(null).getPackage() + ";");
    println();
    println("import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;");
    println("import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;");
    println("import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;");
    println("import org.springframework.context.annotation.Bean;");
    println("import org.springframework.context.annotation.Configuration;");
    println();
    println("@Configuration");
    println("public class MyTypeSolver {");
    println();
    println("  @Bean");
    if (this.layout.getLiveSQLDialectBeanQualifier() != null) {
      println("  @Qualifier(\"" + this.layout.getLiveSQLDialectBeanQualifier() + "\")");
    }
    println("  public TypeSolver getTypeSolver() {");
    println("    TypeSolver s = new TypeSolver();");

    // s.addDialectRule(TypeRule.of("name == 'BAD'", "This is not a valid type
    // 1."));

//    s.addUserRule(TypeRule.of("name == 'bad'", "This is not a valid type 2."));

  }
  
  private void writeRules() {
    for (TypeRule r : this.typeSolver.getRules()) {
      
    }
  }

  private void writeFooter() throws IOException {
    println();
    println("    return s;");
    println("  }");

    println("}");
  }

  @SuppressWarnings("unused")
  private void print(final String txt) throws IOException {
    this.w.write(txt);
  }

  private void println(final String txt) throws IOException {
    this.w.write(txt);
    println();
  }

  private void println() throws IOException {
    this.w.write("\n");
  }

}
