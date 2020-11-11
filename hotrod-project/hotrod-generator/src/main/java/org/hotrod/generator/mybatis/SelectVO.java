package org.hotrod.generator.mybatis;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.generator.NamePackageResolver;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.utils.ClassPackage;

public class SelectVO {

  private static final Logger log = LogManager.getLogger(SelectVO.class);

  private DataSetLayout layout;

  private String className;
  private ClassPackage classPackage;

  private SelectAbstractVO abstractVO;

  public SelectVO(final SelectVOClass soloVO, final SelectAbstractVO abstractVO, final DataSetLayout layout) {
    log.debug("init");
    this.layout = layout;
    this.className = soloVO.getName();
    this.classPackage = soloVO.getClassPackage();
    this.abstractVO = abstractVO;
  }

  public SelectVO(final VOMetadata vo, final SelectAbstractVO abstractVO, final DataSetLayout layout,
      final NamePackageResolver npResolver) {
    this.layout = layout;
    this.className = vo.getName();
    this.classPackage = vo.getClassPackage();
    this.abstractVO = abstractVO;
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {
    log.debug("GENERATE VO...");
    String sourceClassName = this.className + ".java";

    File dir = this.layout.getVOPackageDir(this.classPackage);

    File vo = new File(dir, sourceClassName);
    log.debug("vo=" + vo);
    if (!vo.exists()) {
      TextWriter w = null;

      try {
        w = fileGenerator.createWriter(vo);

        w.write("package " + this.classPackage.getPackage() + ";\n\n");

        w.write("import " + this.abstractVO.getFullClassName() + ";\n\n");

        w.write("public class " + this.className + " extends " + this.abstractVO.getName() + " {\n\n");

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

  public String getClassName() {
    return className;
  }

  // Info

  // public String getClassName() {
  // DataSetIdentifier id = this.metadata.getIdentifier();
  // log.debug("id.wasJavaNameSpecified()=" + id.wasJavaNameSpecified());
  // String name = this.myBatisTag.getDaos().generateVOName(id);
  // log.debug("name=" + name);
  // return name;
  // }

  // public String getFullClassName() {
  // return this.classPackage.getFullClassName(this.getClassName());
  // }

  // public String getJavaClassIdentifier() {
  // return this.metadata.getIdentifier().getJavaClassIdentifier();
  // }

}
