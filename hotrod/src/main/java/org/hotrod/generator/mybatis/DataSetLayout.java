package org.hotrod.generator.mybatis;

import java.io.File;

import org.hotrod.config.tags.DaosTag;
import org.hotrod.config.tags.HotRodConfigTag;
import org.hotrod.config.tags.MappersTag;
import org.hotrod.config.tags.MyBatisTag;
import org.hotrod.config.tags.TableTag;
import org.hotrod.utils.ClassPackage;

public class DataSetLayout {

  private HotRodConfigTag config;
  private String columnSeam;
  private String sessionFactoryGetter;

  private DaosTag daos;
  private MappersTag mappers;

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

    this.daos = mybatis.getDaos();
    this.mappers = mybatis.getMappers();

    this.sessionFactoryGetter = mybatis.getSessionFactory().getSessionFactoryGetter();
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

  public File getMapperPrimitiveDir(final ClassPackage fragmentPackage) {
    return this.mappers.getPrimitivesDir(fragmentPackage);
  }

  public File getMapperRuntimeDir(final ClassPackage fragmentPackage) {
    return this.mappers.getRuntimeDir(fragmentPackage);
  }

  public String getColumnSeam() {
    return columnSeam;
  }

  public String getSessionFactoryGetter() {
    return sessionFactoryGetter;
  }

}
