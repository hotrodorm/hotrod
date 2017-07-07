package org.hotrod.generator.mybatis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisTag;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.identifiers.DataSetIdentifier;

public class ObjectVO {

  private static final Logger log = Logger.getLogger(ObjectVO.class);

  private DataSetMetadata metadata;
  private DataSetLayout layout;
  private ObjectAbstractVO abstractVO;
  private MyBatisTag myBatisTag;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  public ObjectVO(final DataSetMetadata metadata, final DataSetLayout layout, final ObjectAbstractVO abstractVO,
      final MyBatisTag myBatisTag) {
    log.debug("init");

    this.metadata = metadata;
    this.layout = layout;
    this.abstractVO = abstractVO;
    this.myBatisTag = myBatisTag;

    this.fragmentConfig = this.metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    this.classPackage = this.layout.getDAOPackage(fragmentPackage);
  }

  public void generate() throws UncontrolledException {
    String sourceClassName = this.getClassName() + ".java";

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    File dir = this.layout.getDAOPackageDir(fragmentPackage);
    File vo = new File(dir, sourceClassName);
    log.debug("vo file:" + vo.getAbsolutePath());
    if (!vo.exists()) {
      Writer w = null;

      try {
        w = new BufferedWriter(new FileWriter(vo));

        w.write("package " + this.classPackage.getPackage() + ";\n\n");

        w.write("import " + this.abstractVO.getFullClassName() + ";\n\n");

        w.write("public class " + this.getClassName() + " extends " + this.abstractVO.getClassName() + " {\n\n");

        w.write("  private static final long serialVersionUID = 1L;\n\n");

        w.write("  // Add custom code below.\n\n");

        w.write("}\n");

      } catch (IOException e) {
        throw new UncontrolledException("Could not generate VO class: could not write to file '" + vo.getName() + "'.",
            e);
      } finally {
        if (w != null) {
          try {
            w.close();
          } catch (IOException e) {
            throw new UncontrolledException("Could not generate VO class: could not close file '" + vo.getName() + "'.",
                e);
          }
        }
      }
    }
  }

  // Info

  public String getClassName() {
    DataSetIdentifier id = this.metadata.getIdentifier();
    log.debug("id.wasJavaNameSpecified()=" + id.wasJavaNameSpecified());
    String name = this.myBatisTag.getDaos().generateVOName(id);
    log.debug("name=" + name);
    return name;
  }

  public String getFullClassName() {
    return this.classPackage.getFullClassName(this.getClassName());
  }

  public String getJavaClassIdentifier() {
    return this.metadata.getIdentifier().getJavaClassIdentifier();
  }

}
