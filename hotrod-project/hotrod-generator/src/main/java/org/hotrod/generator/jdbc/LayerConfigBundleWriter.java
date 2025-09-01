package org.hotrod.generator.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.config.JDBCTag;
import org.hotrod.config.TypeSolverTag;
import org.hotrod.config.TypeSolverWhenTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.LiveSQLDialectFactory;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.SUtil;

public class LayerConfigBundleWriter {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(LayerConfigBundleWriter.class.getName());

  private static final String DEFAULT_SUFFIX = "main";
  private static final String LAYER_CONFIG_CLASS_PREFIX = "LayerConfigurationBundle";

  private JDBCTag jdbcTag;
  private TypeSolverTag typeSolver;

  private String className;
  private String suffixLower;
  private String suffixCap;

  private ClassWriter w;

  public LayerConfigBundleWriter(final JDBCTag jdbcTag, final TypeSolverTag typeSolver) {
    this.jdbcTag = jdbcTag;
    this.typeSolver = typeSolver;
  }

  public void generate(final FileGenerator fileGenerator, final JDBCGenerator mg, final String qualifier)
      throws UncontrolledException, ControlledException {

    File dir = this.jdbcTag.getLayerPackageDir();

    {
      String suffix = qualifier == null ? DEFAULT_SUFFIX : qualifier;
      this.suffixLower = suffix.toLowerCase();
      this.suffixCap = SUtil.capitalize(suffix);
    }

    this.className = LAYER_CONFIG_CLASS_PREFIX + this.suffixCap;
    File f = new File(dir, className + ".java");

    try (TextWriter tw = fileGenerator.createWriter(f)) {

      this.w = new ClassWriter(this.jdbcTag.getLayerPackage());

      this.writeHeader();
      this.writeLayerConfig();

      if (qualifier != null) {
        this.writeDataSourceProperties();
        this.writeDataSource();
        this.writeDialectProperties();
        this.writeLiveSQL();
      }

      this.writeFooter();

      w.writeTo(tw);

    } catch (IOException e) {
      throw new UncontrolledException("Could not generate LayerConfig class", e);
    }

  }

  private void writeHeader() throws IOException {
    w.println("@", Const.CONFIGURATION);
    w.println("public class " + this.className + " {");
  }

  private void writeLayerConfig() throws IOException {
    w.println("");
    w.println("  @", Const.BEAN);
    w.println("  public ", LayerConfiguration.class, " layerConfig" + this.suffixCap + "() {");
    w.print("    ", List.class, "<", TypeRule.class, "> rules = new ");
    w.println(ArrayList.class, "<>();");
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
    w.println("    return () -> rules;");
    w.println("  }");
  }

  private void writeDataSourceProperties() throws IOException {
    w.println("");
    w.println("  @", Const.BEAN);
    w.println("  @", Const.CONFIGURATION_PROPERTIES, "(\"datasource." + this.suffixLower + "\")");
    w.println("  public ", Const.DATA_SOURCE_PROPERTIES, " dataSourceProperties" + this.suffixCap + "() {");
    w.println("    return new ", Const.DATA_SOURCE_PROPERTIES, "();");
    w.println("  }");
  }

  private void writeDataSource() throws IOException {
    w.println("");
    w.println("  @", Const.BEAN);
    w.println("  public ", DataSource.class, " dataSource" + this.suffixCap + "(", Const.DATA_SOURCE_PROPERTIES,
        " dataSourceProperties" + this.suffixCap + ") {");
    w.println("    ", DataSource.class,
        " ds = dataSourceProperties" + this.suffixCap + ".initializeDataSourceBuilder().build();");
    w.println("    return ds;");
    w.println("  }");
  }

  private void writeDialectProperties() throws IOException {
    w.println("");
    w.println("  @", Const.VALUE, "(\"${datasource." + this.suffixLower + ".livesqldialect.name:#{null}}\")");
    w.println("  private String liveSQLDialectName;");
    w.println("  @", Const.VALUE, "(\"${datasource." + this.suffixLower + ".livesqldialect.databaseName:#{null}}\")");
    w.println("  private String liveSQLDialectVDatabaseName;");
    w.println("  @", Const.VALUE, "(\"${datasource." + this.suffixLower + ".livesqldialect.versionString:#{null}}\")");
    w.println("  private String liveSQLDialectVersionString;");
    w.println("  @", Const.VALUE, "(\"${datasource." + this.suffixLower + ".livesqldialect.majorVersion:#{null}}\")");
    w.println("  private String liveSQLDialectMajorVersion;");
    w.println("  @", Const.VALUE, "(\"${datasource." + this.suffixLower + ".livesqldialect.minorVersion:#{null}}\")");
    w.println("  private String liveSQLDialectMinorVersion;");
  }

  private void writeLiveSQL() throws IOException {
    w.println("");
    w.println("  @", Const.BEAN);
    w.print("  public ", LiveSQL.class, " " + this.suffixLower + "(", DataSource.class, " dataSource" + this.suffixCap);
    w.println(", ", LayerConfiguration.class, " layerConfig" + this.suffixCap + ") throws ", Exception.class, " {");
    w.println("    ", LiveSQLDialect.class, " liveSQLDialect = ", LiveSQLDialectFactory.class,
        ".getLiveSQLDialect(dataSource" + this.suffixCap + ", this.liveSQLDialectName,");
    w.println(
        "        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,");
    w.println("        this.liveSQLDialectMinorVersion);");
    w.println("    ", LiveSQL.class, " ls = new ", LiveSQL.class, "(liveSQLDialect, dataSource" + this.suffixCap
        + ", \"layerConfiguration" + this.suffixCap + "\", layerConfig" + this.suffixCap + ".getTypeRules());");
    w.println("    return ls;");
    w.println("  }");

  }

  private void writeFooter() throws IOException {
    w.println();
    w.println("}");
  }

}
