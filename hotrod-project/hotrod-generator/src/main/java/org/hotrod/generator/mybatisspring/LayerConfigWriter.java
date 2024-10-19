package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.TypeSolverTag;
import org.hotrod.config.TypeSolverWhenTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrodorm.hotrod.utils.SUtil;

public class LayerConfigWriter {

  private static final Logger log = LogManager.getLogger(LayerConfigWriter.class);

  private static final String CLASS_NAME = "ThisLayerConfig";

  private DataSetLayout layout;
  private TypeSolverTag typeSolver;

  private TextWriter w;

  public LayerConfigWriter(final DataSetLayout layout, final TypeSolverTag typeSolver) {
    this.layout = layout;
    this.typeSolver = typeSolver;
  }

  public void generate(final FileGenerator fileGenerator, final MyBatisSpringGenerator mg)
      throws UncontrolledException, ControlledException {

    File dir = this.layout.getDaoPrimitivePackageDir(null);
    File f = new File(dir, CLASS_NAME + ".java");
//    log.info("f=" + f);

    try {
      this.w = fileGenerator.createWriter(f);

      this.writeHeader();
      this.writeRules();
      this.writeFooter();

    } catch (IOException e) {
      throw new UncontrolledException("Could not generate LayerConfig class", e);
    } finally {
      try {
        w.close();
      } catch (IOException e) {
        throw new UncontrolledException("Could not generate LayerConfig class", e);
      }
    }

  }

  private void writeHeader() throws IOException {

    println("package " + this.layout.getDAOPrimitivePackage(null).getPackage() + ";");
    println();
    println("import java.util.ArrayList;");
    println("import java.util.List;");
    println();
    println("import org.hotrod.runtime.livesql.LayerConfig;");
    println("import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;");
    println("import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;");
    println("import org.springframework.stereotype.Component;");
    println("import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.TypeSource;");
    println();
    println("@Component");
    println("public class " + CLASS_NAME + " implements LayerConfig {");
    println();
    println("  @Override");
    println("  public List<TypeRule> getTypeRules() {");
    println("    List<TypeRule> rules = new ArrayList<>();");
    println();

  }

  private void writeRules() throws IOException {
//    log.info("%%% this.typeSolver.getWhens().size()=" + this.typeSolver.getWhens().size());
    int n = 1;
    for (TypeSolverWhenTag w : this.typeSolver.getWhens()) {
//      log.info("%%%  - w.getTestResultSet()=" + w.getTestResultSet());
      if (!SUtil.isEmpty(w.getTestResultSet())) {
        println("    rules.add(TypeRule.of(\"" + SUtil.escapeJavaString(w.getTestResultSet()) + "\", TypeHandler.of("
            + w.getJavaType() + ".class, TypeSource.LIVESQL_RULES), " + n + "));");
      }
      n++;
    }
  }

  private void writeFooter() throws IOException {
    println();
    println("    return rules;");
    println("  }");
    println();
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
