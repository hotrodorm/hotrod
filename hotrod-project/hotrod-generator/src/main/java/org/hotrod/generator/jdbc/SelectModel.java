package org.hotrod.generator.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.hotrod.config.JDBCTag;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;

public class SelectModel {

  private static final Logger log = Logger.getLogger(SelectModel.class.getName());

  private JDBCTag jdbcTag;

  private SelectVOClass soloVO;
  private String className;
  private ClassPackage classPackage;

  private SelectLayout abstractVO;

  public SelectModel(final SelectVOClass soloVO, final SelectLayout abstractVO, final JDBCTag jdbcTag) {
    log.fine("init");
    this.jdbcTag = jdbcTag;
    this.soloVO = soloVO;
    this.className = soloVO.getName();
    this.classPackage = soloVO.getClassPackage();
    this.abstractVO = abstractVO;
  }

  public SelectModel(final VOMetadata vo, final SelectLayout abstractVO, final JDBCTag jdbcTag) {
    this.jdbcTag = jdbcTag;
    this.className = vo.getName();
    this.classPackage = vo.getClassPackage();
    this.abstractVO = abstractVO;
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {
    log.fine("GENERATE VO...");
    String sourceClassName = this.className + ".java";

    File dir = this.jdbcTag.getModelPackageDir(this.classPackage);

    File vo = new File(dir, sourceClassName);
    log.fine("vo=" + vo);
    if (!vo.exists()) {

      try (TextWriter tw = fileGenerator.createWriter(vo)) {

        ClassWriter w = new ClassWriter(this.classPackage);
        writeBody(w);
        w.writeTo(tw);

      } catch (IOException e) {
        throw new UncontrolledException("Could not generate VO class: could not write to file '" + vo.getName() + "'.",
            e);
      }

    }
  }

  private void writeBody(ClassWriter w) throws IOException {
    w.println("@", Const.COMPONENT);
    w.println("@", Const.SCOPE, "(value = ", Const.CONFIGURABLE_BEAN_FACTORY, ".SCOPE_PROTOTYPE)");
    ExternalClass avo = ExternalClass.of(this.abstractVO.getFullClassName());
    w.print("public class " + this.className + " extends ", avo);

    if (this.soloVO != null && this.soloVO.getImplementClasses() != null) {
      w.print(" implements " + this.soloVO.getImplementClasses());
    }

    w.println(" {");
    w.println();

    w.println("  private static final long serialVersionUID = 1L;");
    w.println();

    w.println("  // Add custom code below.");
    w.println();

    w.println("}");
  }

  public String getClassName() {
    return className;
  }

  // Info

  // public String getClassName() {
  // DataSetIdentifier id = this.metadata.getIdentifier();
  // log.fine("id.wasJavaNameSpecified()=" + id.wasJavaNameSpecified());
  // String name = this.myBatisTag.getDaos().generateVOName(id);
  // log.fine("name=" + name);
  // return name;
  // }

  // public String getFullClassName() {
  // return this.classPackage.getFullClassName(this.getClassName());
  // }

  // public String getJavaClassIdentifier() {
  // return this.metadata.getIdentifier().getJavaClassIdentifier();
  // }

}
