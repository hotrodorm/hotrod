package org.hotrod.generator.springjdbc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.utils.ClassPackage;

public class DAO {

  private DataSetMetadata dsm;
  private DataSetLayout dsg;
  private ClassPackage fragmentPackage;

  private DAOPrimitives daoPrimitives;

  private Writer w = null;

  public DAO(final HotRodFragmentConfigTag hrtag, final DataSetMetadata dsm, final DataSetLayout dsg) {
    this.dsm = dsm;
    this.dsg = dsg;
    
    this.fragmentPackage = hrtag != null && hrtag.getFragmentPackage() != null
        ? hrtag.getFragmentPackage() : null;
  }

  public void setDaoPrimitives(final DAOPrimitives daoPrimitives) {
    this.daoPrimitives = daoPrimitives;
  }

  public void generate() throws UncontrolledException {
    String sourceClassName = this.getClassName() + ".java";
    File dao = new File(this.dsg.getDAOPackageDir(fragmentPackage), sourceClassName);
    if (!dao.exists()) {

      try {
        w = new BufferedWriter(new FileWriter(dao));

        println("package " + this.dsg.getDAOPackage(fragmentPackage).getPackage() + ";");
        println();
        println("import " + this.daoPrimitives.getFullClassName() + ";");
        println();
        println("public class " + this.getClassName() + " extends " + this.daoPrimitives.getClassName() + " {");
        println();
        println("  private static final long serialVersionUID = 1L;");
        println();
        println("  // =================");
        println("  // VO");
        println("  // =================");
        println();
        println("  public static class " + this.getVOName() + " extends " + this.daoPrimitives.getClassName() + "."
            + this.daoPrimitives.getVOName() + " {");
        println("    private static final long serialVersionUID = 1L;");
        println();
        println("  }");
        println();

        println("  public " + this.getVOName() + " get" + this.getVOName() + "() {");
        println("    return (" + this.getVOName() + ") super.copyTo(new " + this.getVOName() + "());");
        println("  }");
        println();

        println("  // Add custom code below.");
        println();
        println("}");

      } catch (IOException e) {
        throw new UncontrolledException(
            "Could not generate DAO class: could not write to file '" + dao.getName() + "'.", e);
      } finally {
        if (w != null) {
          try {
            w.close();
          } catch (IOException e) {
            throw new UncontrolledException(
                "Could not generate DAO class: could not close file '" + dao.getName() + "'.", e);
          }
        }
      }
    }
  }

  private String getVOName() {
    return "VO";
  }

  // Info

  public String getClassName() {
    return this.dsm.generateDAOName(this.dsm.getIdentifier());
  }

  public String getFullClassName() {
    return this.dsg.getDAOPackage(fragmentPackage).getFullClassName(this.getClassName());
  }

  public String getJavaClassIdentifier() {
    return this.dsm.getIdentifier().getJavaClassIdentifier();
  }

  // FIXME duplicated code with DAOPrimitives
  private void println(final String txt) throws IOException {
    this.w.write(txt);
    println();
  }

  private void println() throws IOException {
    this.w.write("\n");
  }
}
