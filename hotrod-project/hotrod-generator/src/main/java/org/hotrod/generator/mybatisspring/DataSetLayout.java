package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.Serializable;

import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.MappersTag;
import org.hotrod.config.MyBatisSpringTag;
import org.hotrod.config.TableTag;
import org.hotrod.utils.ClassPackage;

public class DataSetLayout implements Serializable {

  private static final long serialVersionUID = 1L;

  private HotRodConfigTag config;
  private String columnSeam;
//  private String sessixxonFactoryGetter;

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

    try {
      MyBatisSpringTag gtag = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();
      this.daos = gtag.getDaos();
      this.mappers = gtag.getMappers();
//      this.sessionFactoryGetter = mybatis.getSessionFactory().getSessionFactoryGetter();
    } catch (ClassCastException e) {
      MyBatisSpringTag mybatis = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();
      this.daos = mybatis.getDaos();
      this.mappers = mybatis.getMappers();
//      this.sessionFactoryGetter = null;
    }

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

//  public String getSessionFactoryGetter() {
//    return sessionFactoryGetter;
//  }

  // VOs

  public String generateVOName(final String name) {
    return this.daos.generateVOName(name);
  }

  public String generateAbstractVOName(final String name) {
    return this.daos.generateAbstractVOName(name);
  }

  // ClassPackage methods

  public File getVOPackageDir(final ClassPackage classPackage) {
    return this.daos.getVOPackageDir(classPackage);
  }

  public ClassPackage getPrimitivesVOPackage(final ClassPackage classPackage) {
    return this.daos.getPrimitivesVOPackage(classPackage);
  }

}
