package org.hotrod.generator.mybatis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.utils.identifiers.DataSetIdentifier;

public class ObjectDAO {

  private static final Logger log = Logger.getLogger(ObjectDAO.class);

  private DataSetMetadata metadata;
  private DataSetLayout layout;

  private ObjectDAOPrimitives daoPrimitives;

  public ObjectDAO(final DataSetMetadata dsm, final DataSetLayout dsg) {
    this.metadata = dsm;
    this.layout = dsg;
  }

  public void setDaoPrimitives(final ObjectDAOPrimitives daoPrimitives) {
    this.daoPrimitives = daoPrimitives;
  }

  public void generate() throws UncontrolledException {
    String sourceClassName = this.getClassName() + ".java";
    File dao = new File(this.layout.getDAOPackageDir(), sourceClassName);
    log.debug("dao file:" + dao.getAbsolutePath());
    if (!dao.exists()) {
      Writer w = null;

      try {
        w = new BufferedWriter(new FileWriter(dao));

        w.write("package " + this.layout.getDAOPackage().getPackage() + ";\n\n");

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
    DataSetIdentifier id = this.metadata.getIdentifier();
    log.debug(">>> id.wasJavaNameSpecified()=" + id.wasJavaNameSpecified() + " id.getJavaClassIdentifier()="
        + id.getJavaClassIdentifier());
    return id.wasJavaNameSpecified() ? id.getJavaClassIdentifier() : this.metadata.generateDAOName(id);
  }

  public String getFullClassName() {
    return this.layout.getDAOPackage().getFullClassName(this.getClassName());
  }

  public String getJavaClassIdentifier() {
    return this.metadata.getIdentifier().getJavaClassIdentifier();
  }

}
