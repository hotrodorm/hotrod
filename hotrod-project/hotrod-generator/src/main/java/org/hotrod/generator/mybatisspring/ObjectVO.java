package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisSpringTag;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.generator.GeneratableObject;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.ForeignKeyMetadata;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ImportsRenderer;

public class ObjectVO extends GeneratableObject {

  private static final Logger log = LogManager.getLogger(ObjectVO.class);

  private DataSetMetadata metadata;
  private DataSetLayout layout;
  private MyBatisSpringGenerator generator;
  private ObjectAbstractVO abstractVO;
  private MyBatisSpringTag myBatisTag;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  public ObjectVO(final DataSetMetadata metadata, final DataSetLayout layout, final MyBatisSpringGenerator generator,
      final ObjectAbstractVO abstractVO, final MyBatisSpringTag myBatisTag) {
    super();
    log.debug("init");

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

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {
    String sourceClassName = this.getClassName() + ".java";

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    File dir = this.layout.getDAOPackageDir(fragmentPackage);
    File vo = new File(dir, sourceClassName);
    log.debug("vo file:" + vo.getAbsolutePath());
    if (vo.exists()) {
      super.markGenerated();
    } else {
      TextWriter w = null;

      try {
        w = fileGenerator.createWriter(vo);

        w.write("package " + this.classPackage.getPackage() + ";\n\n");

        w.write("import " + this.abstractVO.getFullClassName() + ";\n");

        ImportsRenderer imports = new ImportsRenderer();
        for (ForeignKeyMetadata ik : this.metadata.getImportedFKs()) {
          EnumClass ec = this.generator.getEnum(ik.getRemote().getTableMetadata());
          if (ec != null) {
            imports.add(ec.getFullClassName());
          }
        }
        if (!this.metadata.getImportedFKs().isEmpty()) {
          imports.newLine();
        }

        imports.add("org.springframework.stereotype.Component");
        imports.add("org.springframework.beans.factory.config.ConfigurableBeanFactory");
        imports.add("org.springframework.context.annotation.Scope");
        imports.newLine();

        w.write(imports.render());

        w.write("@Component\n");
        w.write("@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)\n");
        w.write("public class " + this.getClassName() + " extends " + this.abstractVO.getClassName() + " {\n\n");

        w.write("  private static final long serialVersionUID = 1L;\n\n");

        w.write("  // Add custom code below.\n\n");

        w.write("}\n");

        super.markGenerated();

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
    ObjectId id = this.metadata.getId();
    // log.debug("id.wasJavaNameSpecified()=" + id.wasJavaNameSpecified());
    String name = this.myBatisTag.getDaos().generateVOName(id);
    log.debug("name=" + name);
    return name;
  }

  public String getFullClassName() {
    return this.classPackage.getFullClassName(this.getClassName());
  }

  public String getJavaClassIdentifier() {
    return this.metadata.getId().getJavaClassName();
  }

}
