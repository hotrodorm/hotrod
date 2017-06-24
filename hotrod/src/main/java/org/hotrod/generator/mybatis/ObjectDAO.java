package org.hotrod.generator.mybatis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.identifiers.DataSetIdentifier;

public class ObjectDAO {

  private static final Logger log = Logger.getLogger(ObjectDAO.class);

  private DataSetMetadata metadata;
  private DataSetLayout layout;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ObjectDAOPrimitives daoPrimitives;
  private ClassPackage classPackage;

  public ObjectDAO(final DataSetMetadata metadata, final DataSetLayout layout) {
    this.metadata = metadata;
    this.layout = layout;
    this.fragmentConfig = this.metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    this.classPackage = this.layout.getDAOPackage(fragmentPackage);
  }

  public void setDaoPrimitives(final ObjectDAOPrimitives daoPrimitives) {
    this.daoPrimitives = daoPrimitives;
  }

  public void generate() throws UncontrolledException {
    String sourceClassName = this.getClassName() + ".java";

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    File dir = this.layout.getDAOPackageDir(fragmentPackage);
    File dao = new File(dir, sourceClassName);
    log.debug("dao file:" + dao.getAbsolutePath());
    if (!dao.exists()) {
      Writer w = null;

      try {
        w = new BufferedWriter(new FileWriter(dao));

        w.write("package " + this.classPackage.getPackage() + ";\n\n");

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
    return this.classPackage.getFullClassName(this.getClassName());
  }

  public String getJavaClassIdentifier() {
    return this.metadata.getIdentifier().getJavaClassIdentifier();
  }

}
