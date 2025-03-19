package org.hotrod.generator.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;

public class Model {

  private static final Logger log = Logger.getLogger(Model.class.getName());

  private DataSetMetadata metadata;
  @SuppressWarnings("unused")
  private JDBCGenerator generator;
  private Layout abstractVO;
  private JDBCTag jdbcTag;

  private DAO dao;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  public Model(final DataSetMetadata metadata, final JDBCGenerator generator, final Layout abstractVO,
      final JDBCTag jdbcTag) {
    super();
    log.fine("init");

    this.metadata = metadata;
    this.generator = generator;
    this.abstractVO = abstractVO;
    this.jdbcTag = jdbcTag;

    this.fragmentConfig = this.metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    this.classPackage = this.jdbcTag.getModelPackage(this.fragmentPackage);
  }

  public void setDAO(DAO dao) {
    this.dao = dao;
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {
    String sourceClassName = this.getClassName() + ".java";

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    ClassPackage cp = this.jdbcTag.getModelPackage(fragmentPackage);

    File dir = this.jdbcTag.getModelPackageDir(fragmentPackage);
    File vo = new File(dir, sourceClassName);

    if (!vo.exists()) {

      try (TextWriter tw = fileGenerator.createWriter(vo)) {

        ClassWriter w = new ClassWriter(cp);
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

    w.print("public class " + this.getClassName() + " extends ", ExternalClass.of(this.abstractVO.getFullClassName()));

    if (this.metadata.getDaoTag().getImplementsClasses() != null) {
      w.print(" implements " + this.metadata.getDaoTag().getImplementsClasses());
    }

    w.println(" {");
    w.println();

    w.println("  private static final long serialVersionUID = 1L;");
    w.println();

    w.println("  @SuppressWarnings(\"unused\")");
    w.println("  @", Const.AUTOWIRED);
    w.println("  private ", ExternalClass.of(this.dao.getFullClassName()), " " + this.dao.getMemberName() + ";");
    w.println();

    w.println("  // Add custom code below.");
    w.println();

    w.println("}");
  }

  // Info

  public String getClassName() {
    ObjectId id = this.metadata.getId();
    String name = this.jdbcTag.getModelName(id);
    return name;
  }

  public String getFullClassName() {
    return this.classPackage.getFullClassName(this.getClassName());
  }

  public ClassPackage getClassPackage() {
    return this.classPackage;
  }

  public String getJavaClassIdentifier() {
    return this.metadata.getId().getJavaClassName();
  }

}
