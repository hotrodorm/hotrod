package org.hotrod.generator.mybatis;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.Constants;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.GeneratableObject;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.utils.ClassPackage;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class ObjectAbstractVO extends GeneratableObject {

  private static final Logger log = LogManager.getLogger(ObjectAbstractVO.class);

  private DataSetMetadata metadata;
  private DataSetLayout layout;
  private MyBatisGenerator generator;
  private DAOType daoType;
  private MyBatisTag myBatisTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  private TextWriter w;

  // Constructor

  public ObjectAbstractVO(final DataSetMetadata metadata, final DataSetLayout layout, final MyBatisGenerator generator,
      final DAOType daoType, final MyBatisTag myBatisTag) {
    log.debug("init");

    this.metadata = metadata;
    this.layout = layout;
    this.generator = generator;
    if (daoType == null) {
      throw new RuntimeException("VOType cannot be null.");
    }
    metadata.getDaoTag().addGeneratableObject(this);
    this.daoType = daoType;
    this.myBatisTag = myBatisTag;
    this.fragmentConfig = metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    this.classPackage = this.layout.getDAOPrimitivePackage(this.fragmentPackage);
  }

  // Getters

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException, ControlledException {

    String className = this.getClassName() + ".java";

    File dir = this.layout.getDaoPrimitivePackageDir(this.fragmentPackage);

    File f = new File(dir, className);

    this.w = null;

    try {
      this.w = fileGenerator.createWriter(f);

      writeClassHeader();

      writeProperties();

      writeGettersAndSetters();

      writeToString();

      writePropertiesChangeLog();

      writeClassFooter();

      super.markGenerated();

    } catch (IOException e) {
      throw new UncontrolledException(
          "Could not generate DAO primitives class: could not write to file '" + f.getName() + "'.", e);
    } catch (UnresolvableDataTypeException e) {
      throw new ControlledException("Could not generate DAO primitives for table '" + e.getTableName()
          + "'. Could not handle columns '" + e.getColumnName() + "' type: " + e.getTypeName());
    } finally {
      if (this.w != null) {
        try {
          this.w.close();
        } catch (IOException e) {
          throw new UncontrolledException(
              "Could not generate DAO primitives class: could not close file '" + f.getName() + "'.", e);
        }
      }
    }

  }

  private void writeClassHeader() throws IOException {

    // Comment

    println("// Autogenerated by " + Constants.TOOL_NAME + " -- Do not edit.");
    println();

    // Package

    println("package " + this.classPackage.getPackage() + ";");
    println();

    // Imports

    println("import java.io.Serializable;");
    println();

    // Signature

    println("public class " + this.getClassName() + " implements Serializable {");
    println();

    // Serial Version UID

    println("  private static final long serialVersionUID = 1L;");
    println();

  }

  private void writeProperties() throws IOException, UnresolvableDataTypeException {
    println("  // VO Properties ("
        + (this.daoType == DAOType.TABLE ? "table" : this.daoType == DAOType.VIEW ? "view" : "select") + " columns)");
    println();
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      println("  protected " + javaType + " " + cm.getId().getJavaMemberName() + " = null;"
          + (cm.getType().isLOB() ? " // it's a LOB type" : ""));
    }
    println();
  }

  private String resolveType(final ColumnMetadata cm) {
    EnumClass ec = this.generator.getEnum(cm.getEnumMetadata());
    return ec != null ? ec.getFullClassName() : cm.getType().getJavaClassName();
  }

  private void writeGettersAndSetters() throws IOException, UnresolvableDataTypeException {
    println("  // getters & setters");
    println();

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      String m = cm.getId().getJavaMemberName();

      println("  public final " + javaType + " " + cm.getId().getJavaGetter() + "() {");
      println("    return this." + m + ";");
      println("  }");
      println();

      String setter = cm.getId().getJavaSetter();
      writeSetter(cm, javaType, m, setter);
    }

  }

  private void writeSetter(final ColumnMetadata cm, final String javaType, final String m, final String setter)
      throws IOException {
    println("  public final void " + setter + "(final " + javaType + " " + m + ") {");
    println("    this." + m + " = " + m + ";");
    String name = cm.getId().getJavaMemberName() + "WasSet";
    println("    this.propertiesChangeLog." + name + " = true;");
    println("  }");
    println();
  }

  private void writeToString() throws IOException, UnresolvableDataTypeException {
    println("  // to string");
    println();

    println("  public String toString() {");
    println("    java.lang.StringBuilder sb = new java.lang.StringBuilder();");

    if (this.myBatisTag.getProperties().isMultilineTostring()) {
      println("    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + \"\\n\");");

      String prefix = "";
      String elemPrefix = "    sb.append(";
      String elemSuffix = "";
      String separator = " + \"\\n\");\n";
      String lastSeparator = separator;
      String suffix = ");";
      ListWriter lw = new ListWriter(prefix, elemPrefix, elemSuffix, separator, lastSeparator, suffix);
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        String prop = cm.getId().getJavaMemberName();
        lw.add("\"- " + prop + "=\" + this." + prop);
      }
      println(lw.toString());

    } else {
      println("    sb.append(\"[\");");

      String prefix = "";
      String elemPrefix = "    sb.append(";
      String elemSuffix = "";
      String separator = " + \", \");\n";
      String lastSeparator = separator;
      String suffix = ");";
      ListWriter lw = new ListWriter(prefix, elemPrefix, elemSuffix, separator, lastSeparator, suffix);
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        String prop = cm.getId().getJavaMemberName();
        lw.add("\"" + prop + "=\" + this." + prop);
      }
      println(lw.toString());

      println("    sb.append(\"]\");");
    }

    println("    return sb.toString();");
    println("  }");
    println();

  }

  /**
   * <pre>
   * // Properties change log
   * 
   * private PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();
   * 
   * public class PropertiesChangeLog {
   *   boolean idWasSet = false;
   *   boolean nameWasSet = false;
   *   boolean typeWasSet = false;
   *   boolean currentBalanceWasSet = false;
   *   boolean createdOnWasSet = false;
   * }
   * </pre>
   * 
   * @throws IOException
   */
  private void writePropertiesChangeLog() throws IOException {
    println("  // Properties change log");
    println();
    println("  public PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();");
    println();
    println("  public class PropertiesChangeLog {");

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String name = cm.getId().getJavaMemberName() + "WasSet";
      println("    public boolean " + name + " = false;");
    }

    println("  }");
    println();

  }

  private void writeClassFooter() throws IOException {
    println("}");
  }

  // Identifiers & File Paths

  public String getFullClassName() {
    return this.classPackage.getFullClassName(getClassName());
  }

  public String getClassName() {
    return this.myBatisTag.getDaos().generateAbstractVOName(this.metadata.getId());
  }

  // Helpers

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