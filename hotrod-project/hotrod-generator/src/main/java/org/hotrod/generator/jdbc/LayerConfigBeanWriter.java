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
import org.hotrod.exceptions.FaultException;
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

public class LayerConfigBeanWriter {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(LayerConfigBeanWriter.class.getName());

  private static final String LAYER_CONFIG_CLASS_PREFIX = "LayerConfigurationBean";

  private JDBCTag jdbcTag;
  private TypeSolverTag typeSolver;

  private String qualifier;

  private String className;
  private String suffixLower;
  @SuppressWarnings("unused")
  private String suffixCap;

  private ClassWriter w;

  public LayerConfigBeanWriter(final JDBCTag jdbcTag, final TypeSolverTag typeSolver, final String qualifier) {
    this.jdbcTag = jdbcTag;
    this.typeSolver = typeSolver;
    this.qualifier = qualifier;
    this.className = LAYER_CONFIG_CLASS_PREFIX;
    String suffix = SUtil.coalesce(this.qualifier, "");
    this.suffixLower = suffix.toLowerCase();
    this.suffixCap = SUtil.capitalize(suffix);
  }

  public void generate(final FileGenerator fileGenerator) throws FaultException {

    File dir = this.jdbcTag.getLayerPackageDir();

    File f = new File(dir, this.className + ".java");

    try (TextWriter tw = fileGenerator.createWriter(f)) {

      this.w = new ClassWriter(this.jdbcTag.getLayerPackage());

      this.writeHeader();
      this.writeLayerConfig();

      if (this.qualifier != null) {
        this.writeDataSourceProperties();
        this.writeDataSource();
        this.writeDialectProperties();
        this.writeLiveSQL();
      }

      this.writeFooter();

      w.writeTo(tw);

    } catch (IOException e) {
      throw new FaultException("Could not generate LayerConfig class", e);
    }

  }

  private void writeHeader() {
    w.println("@", Const.CONFIGURATION, "(\"" + this.getLayerConfigBeanQualifier() + "\")");
    w.println("public class " + this.className + " {");
  }

  private void writeLayerConfig() {
    w.println("");
    if (this.getLayerConfigQualifier() == null) {
      w.println("  @", Const.BEAN);
    } else {
      w.println("  @", Const.BEAN, "(\"" + this.getLayerConfigQualifier() + "\")");
    }
    w.println("  public ", LayerConfiguration.class, " layerConfig() {");
    w.print("    ", List.class, "<", TypeRule.class, "> rules = new ");
    w.println(ArrayList.class, "<>();");
    int n = 1;
    for (TypeSolverWhenTag when : this.typeSolver.getWhens()) {
      if (!SUtil.isEmpty(when.getTestResultSet())) {
        w.print("    rules.add(", TypeRule.class, ".of(\"" + SUtil.escapeJavaString(when.getTestResultSet()) + "\", ",
            TypeHandler.class);
        w.println(".forClass(", ExternalClass.of(when.getJavaType()), ".class, ", TypeSource.class,
            "." + TypeSource.RUNTIME_TYPESOLVER_RULE.name() + "), " + n + "));");
      }
      n++;
    }
    w.println("    return () -> rules;");
    w.println("  }");
  }

  private void writeDataSourceProperties() {
    w.println("");
    w.println("  @", Const.BEAN, "(\"" + this.getDataSourcePropertiesQualifier() + "\")");
    w.println("  @", Const.CONFIGURATION_PROPERTIES, "(\"" + this.getPropertiesPrefix() + "\")");
    w.println("  public ", Const.DATA_SOURCE_PROPERTIES, " dataSourceProperties() {");
    w.println("    return new ", Const.DATA_SOURCE_PROPERTIES, "();");
    w.println("  }");
  }

  private void writeDataSource() {
    w.println("");
    w.println("  @", Const.BEAN, "(\"" + this.getDataSourceQualifier() + "\")");
    w.print("  public ", DataSource.class, " dataSource(");
    w.print("@", Const.QUALIFIER, "(\"" + this.getDataSourcePropertiesQualifier() + "\") ");
    w.println(Const.DATA_SOURCE_PROPERTIES, " dataSourceProperties) {");
    w.println("    ", DataSource.class, " ds = dataSourceProperties.initializeDataSourceBuilder().build();");
    w.println("    return ds;");
    w.println("  }");
  }

  private void writeDialectProperties() {
    w.println("");
    w.println("  @", Const.VALUE, "(\"${" + this.getPropertiesPrefix() + ".livesqldialect.name:#{null}}\")");
    w.println("  private String liveSQLDialectName;");
    w.println("  @", Const.VALUE, "(\"${" + this.getPropertiesPrefix() + ".livesqldialect.databaseName:#{null}}\")");
    w.println("  private String liveSQLDialectVDatabaseName;");
    w.println("  @", Const.VALUE, "(\"${" + this.getPropertiesPrefix() + ".livesqldialect.versionString:#{null}}\")");
    w.println("  private String liveSQLDialectVersionString;");
    w.println("  @", Const.VALUE, "(\"${" + this.getPropertiesPrefix() + ".livesqldialect.majorVersion:#{null}}\")");
    w.println("  private String liveSQLDialectMajorVersion;");
    w.println("  @", Const.VALUE, "(\"${" + this.getPropertiesPrefix() + ".livesqldialect.minorVersion:#{null}}\")");
    w.println("  private String liveSQLDialectMinorVersion;");
  }

  private void writeLiveSQL() {
    w.println("");
    w.println("  @", Const.BEAN, "(\"" + this.getLiveSQLQualifier() + "\")");
    w.print("  public ", LiveSQL.class, " liveSQL(@", Const.QUALIFIER, "(\"" + this.getDataSourceQualifier() + "\") ");
    w.print(DataSource.class, " dataSource");
    w.print(", @", Const.QUALIFIER, "(\"" + this.getLayerConfigQualifier() + "\") ", LayerConfiguration.class,
        " layerConfig) throws ");
    w.println(Exception.class, " {");
    w.println("    ", LiveSQLDialect.class, " liveSQLDialect = ", LiveSQLDialectFactory.class,
        ".getLiveSQLDialect(dataSource, this.liveSQLDialectName,");
    w.println("        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, "
        + "this.liveSQLDialectMajorVersion,");
    w.println("        this.liveSQLDialectMinorVersion);");
    w.println("    ", LiveSQL.class, " ls = new ", LiveSQL.class,
        "(liveSQLDialect, dataSource, \"" + this.getLayerConfigQualifier() + "\", layerConfig.getTypeRules());");
    w.println("    return ls;");
    w.println("  }");

  }

  private void writeFooter() {
    w.println();
    w.println("}");
  }

  // Getters

  public String getDataSourcePropertiesQualifier() {
    if (this.qualifier == null) {
      return null;
    } else {
      return "dataSourceProperties:" + this.suffixLower;
    }
  }

  public String getDataSourceQualifier() {
    if (this.qualifier == null) {
      return null;
    } else {
      return "dataSource:" + this.suffixLower;
    }
  }

  public String getLiveSQLQualifier() {
    if (this.qualifier == null) {
      return null;
    } else {
      return "liveSQL:" + this.suffixLower;
    }
  }

  public String getClassName() {
    return this.className;
  }

  public String getLayerConfigBeanQualifier() {
    if (this.qualifier == null) {
      return null;
    } else {
      return "layerConfigurationBean:" + this.suffixLower;
    }
  }

  public String getLayerConfigQualifier() {
    if (this.qualifier == null) {
      return null;
    } else {
      return "layerConfiguration:" + this.suffixLower;
    }
  }

  public String getPropertiesPrefix() {
    if (this.qualifier == null) {
      return "";
    } else {
      return "datasource." + this.suffixLower;
    }
  }

}
