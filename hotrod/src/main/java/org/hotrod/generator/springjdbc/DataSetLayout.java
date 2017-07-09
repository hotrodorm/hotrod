package org.hotrod.generator.springjdbc;

import java.io.File;

import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.SpringJDBCTag;
import org.hotrod.config.TableTag;
import org.hotrod.utils.ClassPackage;

public class DataSetLayout {

  private HotRodConfigTag config;
  // private File daoPackageDir;
  // private ClassPackage daoPackage;
  // private ClassPackage daoPrimitivePackage;
  // private File daoPrimitivePackageDir;
  private String columnSeam;
  private String sessionFactoryGetter;
  private DaosTag daos;

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

    this.daos = springTag.getDaos();

    // this.daoPackageDir = springTag.getDaos().getDaosPackageDir();
    // this.daoPackage = springTag.getDaos().getDaoPackage();
    // this.daoPrimitivePackage = springTag.getDaos().getPrimitivesPackage();
    // this.daoPrimitivePackageDir =
    // springTag.getDaos().getPrimitivesPackageDir();
  }

  public ClassPackage getDAOPackage(final ClassPackage fragmentPackage) {
    return this.daos.getDaoPackage(fragmentPackage);
  }

  public File getDAOPackageDir(final ClassPackage fragmentPackage) {
    return this.daos.getDaosPackageDir(fragmentPackage);
  }

  public ClassPackage getDAOPrimitivePackage(final ClassPackage fragmentPackage) {
    return this.daos.getPrimitivesPackage(fragmentPackage);
  }

  public File getDaoPrimitivePackageDir(final ClassPackage fragmentPackage) {
    return this.daos.getPrimitivesPackageDir(fragmentPackage);
  }

  public String getColumnSeam() {
    return columnSeam;
  }

  public String getSessionFactoryGetter() {
    return sessionFactoryGetter;
  }

}
