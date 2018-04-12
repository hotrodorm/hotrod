package org.hotrod.generator.mybatis;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.DaosTag;
import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.EnumTag.EnumProperty;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.GeneratableObject;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.ClassPackage;

public class EnumClass extends GeneratableObject {

  // Constants

  private static final Logger log = Logger.getLogger(EnumClass.class);

  // Properties

  private EnumDataSetMetadata metadata;
  private DataSetLayout layout;

  private DaosTag daos;
  @SuppressWarnings("unused")
  private MyBatisGenerator generator;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage primitivesPackage;

  private ClassPackage classPackage;

  private TextWriter w;

  // Constructor

  public EnumClass(final EnumDataSetMetadata metadata, final DataSetLayout layout, final DaosTag daos,
      final MyBatisGenerator generator) {
    log.debug("init");
    this.metadata = metadata;
    this.layout = layout;
    this.daos = daos;
    this.generator = generator;
    metadata.getDaoTag().addGeneratableObject(this);

    this.fragmentConfig = this.metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;
    this.primitivesPackage = this.daos.getPrimitivesPackage(this.fragmentPackage);

    this.classPackage = this.layout.getDAOPrimitivePackage(this.fragmentPackage);

  }

  // Behavior

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException, ControlledException {
    String sourceClassName = this.getClassName() + ".java";

    // String valueClassName =
    // this.metadata.getValueTypeManager().getValueClassName();

    File dir = this.daos.getPrimitivesPackageDir(this.fragmentPackage);
    File ec = new File(dir, sourceClassName);
    this.w = null;

    try {
      this.w = fileGenerator.createWriter(ec);

      println("package " + this.primitivesPackage.getPackage() + ";");
      println();

      println("public enum " + this.getClassName() + " {");
      println();

      // prefix, elemPrefix, elemSuffix, separator, suffix
      ListWriter lw = new ListWriter("", "  ", "", ", //\n", ";");
      for (EnumConstant c : this.metadata.getEnumConstants()) {
        lw.add(c.getJavaConstantName() + "(" + ListWriter.render(c.getJavaLiteralValues(), ", ") + ")");
      }
      println(lw.toString());
      println();

      println("  // Properties (table columns)");
      println();
      for (EnumProperty p : this.metadata.getProperties()) {
        println("  private " + p.getClassName() + " " + p.getName() + ";");
      }
      println();

      ListWriter params = new ListWriter(", ");
      for (EnumProperty p : this.metadata.getProperties()) {
        params.add("final " + p.getClassName() + " " + p.getName());
      }
      println("  // Constructor");
      println();
      println("  private " + this.getClassName() + "(" + params.toString() + ") {");
      for (EnumProperty p : this.metadata.getProperties()) {
        println("    this." + p.getName() + " = " + p.getName() + ";");
      }
      println("  }");
      println();

      println("  // Getters");
      for (EnumProperty p : this.metadata.getProperties()) {
        println();
        println("  public " + p.getClassName() + " " + p.getGetter() + "() {");
        println("    return this." + p.getName() + ";");
        println("  }");
      }
      println();

      writeCodec();

      println("}");

      super.markGenerated();

    } catch (IOException e) {
      throw new UncontrolledException(
          "Could not generate Java enum class: could not write to file '" + ec.getName() + "'.", e);
    } finally {
      if (this.w != null) {
        try {
          this.w.close();
        } catch (IOException e) {
          throw new UncontrolledException(
              "Could not generate Java enum class: could not close file '" + ec.getName() + "'.", e);
        }
      }
    }

  }

  private void writeCodec() throws IOException {

    EnumProperty valueColumn = this.metadata.getValueColumn();

    println("  // Encode & Decode");
    println();
    println("  public static " + this.getClassName() + " decode(final " + valueColumn.getClassName() + " value) {");
    println("    if (value == null) {");
    println("      return null;");
    println("    }");
    println("    for (" + this.getClassName() + " e : " + this.getClassName() + ".values()) {");
    println("      if (e." + valueColumn.getName() + ".equals(value)) {");
    println("        return e;");
    println("      }");
    println("    }");
    println("    throw new IllegalArgumentException(");
    println("        \"Invalid " + this.metadata.getIdentifier().getSQLIdentifier() + " " + valueColumn.getName()
        + " value (\" + value + \"). There's no enum constant for this value.\\n\\n"
        + "***** Maybe the <enum> table data has changed, and the <enum> table needs to be regenerated *****\\n\");");
    println("  }");
    println();
    println("  public static " + valueColumn.getClassName() + " encode(final " + this.getClassName() + " e) {");
    println("    return e == null ? null : e." + valueColumn.getName() + ";");
    println("  }");
    println();

  }

  // Getters

  public String getClassName() {
    return this.metadata.getJavaClassName();
  }

  public String getFullClassName() {
    return this.classPackage.getFullClassName(this.getClassName());
  }

  public EnumProperty getValueColumn() {
    return this.metadata.getValueColumn();
  }

  // Utilities

  @SuppressWarnings("unused")
  private void print(final String txt) throws IOException {
    this.w.write(txt);
  }

  private void println(final String txt) throws IOException {
    this.w.write(txt + "\n");
  }

  private void println() throws IOException {
    this.w.write("\n");
  }

}
