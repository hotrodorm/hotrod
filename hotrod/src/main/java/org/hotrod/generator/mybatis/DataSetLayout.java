package org.hotrod.generator.mybatis;

import java.io.File;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.MyBatisTag;
import org.hotrod.config.TableTag;
import org.hotrod.utils.ClassPackage;

public class DataSetLayout {

  private HotRodConfigTag config;
  private File daoPackageDir;
  private ClassPackage daoPackage;
  private ClassPackage daoPrimitivePackage;
  private File mapperPrimitiveDir;
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
    MyBatisTag mybatis = (MyBatisTag) this.config.getGenerators().getSelectedGeneratorTag();

    this.daoPackageDir = mybatis.getDaos().getDaosPackageDir();
    this.daoPackage = mybatis.getDaos().getDaoPackage();
    this.daoPrimitivePackage = mybatis.getDaos().getPrimitivesPackage();
    this.mapperPrimitiveDir = mybatis.getMappers().getPrimitivesDir();
    this.daoPrimitivePackageDir = mybatis.getDaos().getPrimitivesPackageDir();
    this.sessionFactoryGetter = mybatis.getSessionFactory().getSessionFactoryGetter();
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

  public File getMapperPrimitiveDir() {
    return mapperPrimitiveDir;
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
