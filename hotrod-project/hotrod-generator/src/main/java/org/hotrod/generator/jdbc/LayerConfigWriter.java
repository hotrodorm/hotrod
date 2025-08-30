package org.hotrod.generator.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.JDBCTag;
import org.hotrod.config.TypeSolverTag;
import org.hotrod.config.TypeSolverWhenTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.SUtil;

public class LayerConfigWriter {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(LayerConfigWriter.class.getName());

  private static final String SINGLE_LAYER_CLASS_NAME = "MainLayerConfiguration";
  private static final String MULTI_LAYER_CLASS_NAME_PREFIX = "LayerConfiguration_";

  private JDBCTag jdbcTag;
  private TypeSolverTag typeSolver;

  private String className;
  private ClassWriter w;

  public LayerConfigWriter(final JDBCTag jdbcTag, final TypeSolverTag typeSolver) {
    this.jdbcTag = jdbcTag;
    this.typeSolver = typeSolver;
  }

  public void generate(final FileGenerator fileGenerator, final JDBCGenerator mg, final String qualifier)
      throws UncontrolledException, ControlledException {

    File dir = this.jdbcTag.getLayerPackageDir();
    this.className = qualifier == null ? SINGLE_LAYER_CLASS_NAME : MULTI_LAYER_CLASS_NAME_PREFIX + qualifier;
    File f = new File(dir, className + ".java");

    try (TextWriter tw = fileGenerator.createWriter(f)) {

      this.w = new ClassWriter(this.jdbcTag.getLayerPackage());

      this.writeHeader();
      this.writeRules();
      this.writeFooter();

      w.writeTo(tw);

    } catch (IOException e) {
      throw new UncontrolledException("Could not generate LayerConfig class", e);
    }

  }

  private void writeHeader() throws IOException {
    w.println("@", Const.COMPONENT);
    w.println("public class " + this.className + " implements ", LayerConfiguration.class, " {");
    w.println();
    w.println("  @", Override.class);
    w.println("  public ", List.class, "<", TypeRule.class, "> getTypeRules() {");
    w.print("    ", List.class, "<", TypeRule.class, "> rules = ");
    w.println("new ", ArrayList.class, "<>();");
    w.println();
  }

  private void writeRules() throws IOException {
    int n = 1;
    for (TypeSolverWhenTag when : this.typeSolver.getWhens()) {
      if (!SUtil.isEmpty(when.getTestResultSet())) {
        w.print("    rules.add(", TypeRule.class, ".of(\"" + SUtil.escapeJavaString(when.getTestResultSet()) + "\", ",
            TypeHandler.class);
        w.println(".forClass(", ExternalClass.of(when.getJavaType()), ".class, ", TypeSource.class,
            "." + TypeSource.RUNTIME_LAYER_RULE.name() + "), " + n + "));");
      }
      n++;
    }
  }

  private void writeFooter() throws IOException {
    w.println();
    w.println("    return rules;");
    w.println("  }");
    w.println();
    w.println("}");
  }

}
