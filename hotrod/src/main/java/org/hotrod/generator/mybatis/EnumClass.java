package org.hotrod.generator.mybatis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.DaosTag;
import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.metadata.EnumMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;

public class EnumClass {

  // Constants

  private static final Logger log = Logger.getLogger(EnumClass.class);

  // Properties

  private EnumMetadata metadata;

  private DaosTag daos;
  @SuppressWarnings("unused")
  private MyBatisGenerator generator;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage primitivesPackage;
  private Writer w;

  // Constructor

  public EnumClass(final EnumMetadata metadata, final DaosTag daos, final MyBatisGenerator generator) {
    log.debug("init");
    this.metadata = metadata;
    this.daos = daos;
    this.generator = generator;

    this.fragmentConfig = this.metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;
    this.primitivesPackage = this.daos.getPrimitivesPackage(this.fragmentPackage);
  }

  // Behavior

  public void generate() throws UncontrolledException, ControlledException {
    String sourceClassName = this.getClassName() + ".java";

    String valueClassName = this.metadata.getValueTypeManager().getValueClassName();

    File dir = this.daos.getPrimitivesPackageDir(this.fragmentPackage);
    File ec = new File(dir, sourceClassName);
    this.w = null;

    try {
      this.w = new BufferedWriter(new FileWriter(ec));

      println("package " + this.primitivesPackage.getPackage() + ";");
      println();

      println("public enum " + this.getClassName() + " {");
      println();

      // prefix, elemPrefix, elemSuffix, separator, suffix
      ListWriter lw = new ListWriter("", "  ", "", ", //\n", ";");
      for (EnumConstant c : this.metadata.getEnumConstants()) {
        lw.add(c.getJavaConstantName() + "(" + this.metadata.getValueTypeManager().renderJavaValue(c.getValue())
            + ", \"" + SUtils.escapeJavaString(c.getTitle()) + "\")");
      }
      println(lw.toString());
      println();

      println("  // Properties (table columns)");
      println();
      println("  private " + valueClassName + " value;");
      println("  private String name;");
      println();

      println("  // Constructor");
      println();
      println("  private " + this.getClassName() + "(final " + valueClassName + " value, final String name) {");
      println("    this.value = value;");
      println("    this.name = name;");
      println("  }");
      println();

      println("  // Getters");
      println();
      println("  public " + valueClassName + " getValue() {");
      println("    return value;");
      println("  }");
      println();
      println("  public String getName() {");
      println("    return name;");
      println("  }");
      println();

      println("}");

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

  // Getters

  public String getClassName() {
    return this.metadata.getJavaClassName();
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
