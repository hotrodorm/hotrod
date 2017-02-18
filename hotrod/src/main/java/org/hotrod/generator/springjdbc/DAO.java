package org.hotrod.generator.springjdbc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.hotrod.ant.UncontrolledException;
import org.hotrod.metadata.DataSetMetadata;

public class DAO {

  private DataSetMetadata dsm;
  private DataSetLayout dsg;

  private DAOPrimitives daoPrimitives;

  public DAO(final DataSetMetadata dsm, final DataSetLayout dsg) {
    this.dsm = dsm;
    this.dsg = dsg;
  }

  public void setDaoPrimitives(final DAOPrimitives daoPrimitives) {
    this.daoPrimitives = daoPrimitives;
  }

  public void generate() throws UncontrolledException {
    String sourceClassName = this.getClassName() + ".java";
    File dao = new File(this.dsg.getDAOPackageDir(), sourceClassName);
    if (!dao.exists()) {
      Writer w = null;

      try {
        w = new BufferedWriter(new FileWriter(dao));

        w.write("package " + this.dsg.getDAOPackage().getPackage() + ";\n\n");

        w.write("import " + this.daoPrimitives.getFullClassName() + ";\n\n");

        w.write("public class " + this.getClassName() + " extends " + this.daoPrimitives.getClassName() + " {\n\n");

        w.write("  private static final long serialVersionUID = 1L;\n\n");

        w.write("  // Add custom code below.\n\n");

        w.write("}\n");

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

  // Info

  public String getClassName() {
    return this.dsm.generateDAOName(this.dsm.getIdentifier());
  }

  public String getFullClassName() {
    return this.dsg.getDAOPackage().getFullClassName(this.getClassName());
  }

  public String getJavaClassIdentifier() {
    return this.dsm.getIdentifier().getJavaClassIdentifier();
  }

}
