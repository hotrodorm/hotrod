package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisSpringTag;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.GeneratableObject;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;

public class ObjectVO extends GeneratableObject {

  private static final Logger log = Logger.getLogger(ObjectVO.class.getName());

  private DataSetMetadata metadata;
  private DataSetLayout layout;
  @SuppressWarnings("unused")
  private MyBatisSpringGenerator generator;
  private ObjectAbstractVO abstractVO;
  private MyBatisSpringTag myBatisTag;

  private ObjectDAO dao;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  public ObjectVO(final DataSetMetadata metadata, final DataSetLayout layout, final MyBatisSpringGenerator generator,
      final ObjectAbstractVO abstractVO, final MyBatisSpringTag myBatisTag) {
    super();
    log.fine("init");

    this.metadata = metadata;
    this.layout = layout;
    this.generator = generator;
    metadata.getDaoTag().addGeneratableObject(this);
    this.abstractVO = abstractVO;
    this.myBatisTag = myBatisTag;

    this.fragmentConfig = this.metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    this.classPackage = this.layout.getDAOPackage(this.fragmentPackage);
  }

  public void setDAO(ObjectDAO dao) {
    this.dao = dao;
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {
    String sourceClassName = this.getClassName() + ".java";

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    ClassPackage cp = this.layout.getDAOPackage(fragmentPackage);

    File dir = this.layout.getDAOPackageDir(fragmentPackage);
    File vo = new File(dir, sourceClassName);

    if (vo.exists()) {
      super.markGenerated();
    } else {

      try (TextWriter tw = fileGenerator.createWriter(vo)) {

        ClassWriter w = new ClassWriter(cp);
        writeBody(w);
        w.writeTo(tw);
        super.markGenerated();

      } catch (IOException e) {
        throw new UncontrolledException("Could not generate VO class: could not write to file '" + vo.getName() + "'.",
            e);
      }
    }
  }

  private static final ExternalClass AUTOWIRED = ExternalClass
      .of("org.springframework.beans.factory.annotation.Autowired");
  private static final ExternalClass COMPONENT = ExternalClass.of("org.springframework.stereotype.Component");
  private static final ExternalClass SCOPE = ExternalClass.of("org.springframework.context.annotation.Scope");
  private static final ExternalClass CONFIGURABLE_BEAN_FACTORY = ExternalClass
      .of("org.springframework.beans.factory.config.ConfigurableBeanFactory");

  private void writeBody(ClassWriter w) throws IOException {
//    w.registerClass(this.abstractVO.getFullClassName());
//
//    w.registerClass("org.springframework.stereotype.Component");
//    w.registerClass("org.springframework.beans.factory.config.ConfigurableBeanFactory");
//    w.registerClass("org.springframework.context.annotation.Scope");
//    w.registerClass(this.dao.getFullClassName());
//    w.registerClass("org.springframework.beans.factory.annotation.Autowired");

    w.println("@", COMPONENT);

    w.println("@", SCOPE, "(value = ", CONFIGURABLE_BEAN_FACTORY, ".SCOPE_PROTOTYPE)");
//    w.println("@", SCOPE);
//    w.println("(value = ", CONFIGURABLE_BEAN_FACTORY, ".SCOPE_PROTOTYPE)");

    w.print("public class " + this.getClassName() + " extends ", ExternalClass.of(this.abstractVO.getFullClassName()));

    if (this.metadata.getDaoTag().getImplementsClasses() != null) {
      w.print(" implements " + this.metadata.getDaoTag().getImplementsClasses());
    }

    w.println(" {");
    w.println();

    w.println("  private static final long serialVersionUID = 1L;");
    w.println();

    w.println("  @SuppressWarnings(\"unused\")");
    w.println("  @", AUTOWIRED);
    w.println("  private ", ExternalClass.of(this.dao.getFullClassName()), " " + this.dao.getMemberName() + ";");
    w.println();

    w.println("  // Add custom code below.");
    w.println();

    w.println("}");
  }

  // Info

  public String getClassName() {
    ObjectId id = this.metadata.getId();
    String name = this.myBatisTag.getDaos().generateVOName(id);
    log.fine("name=" + name);
    return name;
  }

  public String getFullClassName() {
    return this.classPackage.getFullClassName(this.getClassName());
  }

  public String getJavaClassIdentifier() {
    return this.metadata.getId().getJavaClassName();
  }

}
