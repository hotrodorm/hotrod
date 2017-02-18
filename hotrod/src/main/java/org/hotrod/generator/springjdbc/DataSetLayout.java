package org.hotrod.generator.springjdbc;

import java.io.File;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.SpringJDBCTag;
import org.hotrod.config.TableTag;
import org.hotrod.utils.ClassPackage;

public class DataSetLayout {

  private HotRodConfigTag config;
  private File daoPackageDir;
  private ClassPackage daoPackage;
  private ClassPackage daoPrimitivePackage;
  private File daoPrimitivePackageDir;
  private String columnSeam;
  private String sessionFactoryGetter;

  public DataSetLayout(final HotRodConfigTag config, final TableTag tag) {
    initialize(config);
    this.columnSeam = tag.getColumnSeam();
  }

  public DataSetLayout(final HotRodConfigTag config) {
    initialize(config);
    this.columnSeam = null;
  }

  private void initialize(final HotRodConfigTag config) {
    this.config = config;
    SpringJDBCTag springTag = (SpringJDBCTag) this.config.getGenerators().getSelectedGeneratorTag();

    this.daoPackageDir = springTag.getDaos().getDaosPackageDir();
    this.daoPackage = springTag.getDaos().getDaoPackage();
    this.daoPrimitivePackage = springTag.getDaos().getPrimitivesPackage();
    this.daoPrimitivePackageDir = springTag.getDaos().getPrimitivesPackageDir();
  }

  public File getDAOPackageDir() {
    return daoPackageDir;
  }

  public ClassPackage getDAOPackage() {
    return daoPackage;
  }

  public ClassPackage getDAOPrimitivePackage() {
    return this.daoPrimitivePackage;
  }

  public File getDaoPrimitivePackageDir() {
    return daoPrimitivePackageDir;
  }

  public String getColumnSeam() {
    return columnSeam;
  }

  public String getSessionFactoryGetter() {
    return sessionFactoryGetter;
  }

}
