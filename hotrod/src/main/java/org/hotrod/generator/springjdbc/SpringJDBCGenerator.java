package org.hotrod.generator.springjdbc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.ConfigTag;
import org.hotrod.config.DisplayMode;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.SelectClassTag;
import org.hotrod.config.SpringJDBCTag;
import org.hotrod.config.TableTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.SelectDataSetMetadata;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public class SpringJDBCGenerator extends HotRodGenerator {

  private static transient final Logger log = Logger.getLogger(SpringJDBCGenerator.class);
  private LinkedHashMap<DataSetMetadata, DAO> daos = new LinkedHashMap<DataSetMetadata, DAO>();
  private LinkedHashMap<DataSetMetadata, DAOPrimitives> daoPrimitives = //
      new LinkedHashMap<DataSetMetadata, DAOPrimitives>();
  private List<SpringBean> beans = new ArrayList<SpringBean>();
  private SpringJDBCConfiguration springJDBCConfiguration;

  public SpringJDBCGenerator(final DatabaseLocation loc, final HotRodConfigTag config, final DisplayMode displayMode,
      final DatabaseAdapter adapter)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
    super(null, loc, config, displayMode, false, adapter);
  }

  @Override
  public void prepareGeneration() throws UncontrolledException, ControlledException {
    log.debug("prepareGeneration()");
    log.debug("prepare");

    this.springJDBCConfiguration = new SpringJDBCConfiguration(this.config);

    for (DataSetMetadata ds : this.tables) {
      addDaosAndMapper(ds, DAOType.TABLE);
    }

    // Add selects

    for (SelectDataSetMetadata sm : this.selects) {
      addDaosAndMapper(sm, DAOType.SELECT);
    }

    // // Add custom DAOs
    //
    // DataSetLayout layout = new DataSetLayout(this.config);
    //
    // for (CustomDAOTag c : this.config.getDAOs()) {
    // CustomDAO dao = new CustomDAO(c, layout, this);
    // this.collectionsDAOs.add(dao);
    // this.beans.add(new SpringBeanCustomDAO(c, layout));
    // }

  }

  private void addDaosAndMapper(final DataSetMetadata metadata, final DAOType type) throws ControlledException {
    DataSetLayout layout;
    DAO dao;
    DAOPrimitives daoPrimitives;
    SpringBean springBean;

    switch (type) {
    case TABLE:
      TableTag tag = this.config.findTable(metadata, this.adapter);
      if (tag == null) {
        throw new ControlledException(
            "Could not find table tag for table '" + metadata.getId().getCanonicalSQLName() + "'.");
      }
      layout = new DataSetLayout(this.config, tag);

      dao = new DAO(tag.getFragmentConfig(), metadata, layout);

      daoPrimitives = new DAOPrimitives(tag.getFragmentConfig(), metadata, layout, this, type);
      springBean = new SpringBeanTable(metadata, daoPrimitives);

      // TODO Architecture: simplify mutual reference implementation.
      dao.setDaoPrimitives(daoPrimitives);
      daoPrimitives.setDao(dao);

      this.daos.put(metadata, dao);
      this.daoPrimitives.put(metadata, daoPrimitives);
      this.beans.add(springBean);
      break;

    case SELECT:
      SelectDataSetMetadata sm = (SelectDataSetMetadata) metadata;
      SelectClassTag stag = this.config.findSelect(sm, this.adapter);
      if (stag == null) {
        throw new ControlledException(
            "Could not find select tag for with java-class-name '" + sm.getSelectTag().getJavaClassName() + "'.");
      }
      layout = new DataSetLayout(this.config);
      dao = new DAO(stag.getFragmentConfig(), metadata, layout);
      daoPrimitives = new DAOPrimitives(stag.getFragmentConfig(), metadata, layout, this, type);
      springBean = new SpringBeanTable(metadata, daoPrimitives);

      // TODO Architecture: simplify mutual reference implementation.
      dao.setDaoPrimitives(daoPrimitives);
      daoPrimitives.setDao(dao);

      this.daos.put(metadata, dao);
      this.daoPrimitives.put(metadata, daoPrimitives);
      this.beans.add(springBean);

      break;
    default:
      throw new ControlledException(
          "Unrecognized type for database object '" + metadata.getId().getCanonicalSQLName() + "'.");
    }

  }

  @Override
  public void generate() throws UncontrolledException, ControlledException {
    log.debug("generate()");
    for (DAO dao : this.daos.values()) {
      dao.generate();
    }
    for (DAOPrimitives p : this.daoPrimitives.values()) {
      p.generate();
    }

    this.springJDBCConfiguration.generate(); // do nothing (just for
                                             // completeness).
    this.generateSpringBeansFile();

    display("SpringJDBC DAOs generated.");
  }

  private void generateSpringBeansFile() throws ControlledException, UncontrolledException {
    SpringJDBCTag springTag = (SpringJDBCTag) this.config.getGenerators().getSelectedGeneratorTag();
    ConfigTag ctag = springTag.getConfig();
    StringBuffer sb = new StringBuffer();
    sb.append(ctag.getGenBaseDir() + ctag.getRelativeDir() + ctag.getPrefix() + "-spring.xml");

    File sFile = new File(ctag.getGenBaseDir() + ctag.getRelativeDir());
    if (!sFile.exists()) {
      if (!sFile.mkdirs()) {
        throw new ControlledException("Unable to create config folder [" + ctag.getGenBaseDir() + ctag.getRelativeDir()
            + "] mentioned in the <mybatis-configuration-template> tag.");
      }
    }

    File cFile = new File(sFile, ctag.getPrefix() + "spring.xml");

    for (SpringBean b : this.beans) {
      writeSingleBean(b, sFile, ctag);
    }

    // File cFile = new File(ctag.getGenBaseDir() + ctag.getRelativeDir() +
    // ctag.getPrefix() + "-spring.xml");
    if (cFile.exists()) {
      System.out.println("Overwriting existing configuration file for Spring: '" + cFile.getAbsolutePath() + "'.");
    }

    Writer w = null;
    try {
      w = new BufferedWriter(new FileWriter(cFile));
      w.write(buildSpringBeanXmlHeader());

      // for (SpringBean b : this.beans.values()) {
      // b.writeBeanTag(w);
      // }
      // include individual bean files into a single DAO beans file.
      File[] files = sFile.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith("-bean.xml");
        }
      });

      for (File f : files) {
        w.write("\t<import resource=\"" + f.getName() + "\"/>\n");
      }

      w.write("</beans>\n");

    } catch (IOException e) {
      throw new UncontrolledException("Could not generate the Spring configuration file: '" + cFile.getName() + "'.",
          e);
    } finally {
      if (w != null) {
        try {
          w.close();
        } catch (IOException e) {
          throw new UncontrolledException(
              "Could not generate the Spring configuration file: '" + cFile.getName() + "'.", e);
        }
      }
    }
  }

  private static void writeSingleBean(SpringBean b, File sFile, ConfigTag ctag) throws UncontrolledException {
    File beanFile = new File(sFile, ctag.getPrefix() + b.getBeanName() + "-bean.xml");
    if (beanFile.exists()) {
      System.out.println("Overwriting existing configuration file for Spring: '" + beanFile.getAbsolutePath() + "'.");
    }
    Writer w = null;
    try {
      w = new BufferedWriter(new FileWriter(beanFile));
      w.write(buildSpringBeanXmlHeader());
      b.writeBeanTag(w);
      w.write("</beans>\n");
    } catch (IOException e) {
      throw new UncontrolledException("Could not generate the Spring configuration file: '" + beanFile.getName() + "'.",
          e);
    } finally {
      if (w != null) {
        try {
          w.close();
        } catch (IOException e) {
          throw new UncontrolledException(
              "Could not generate the Spring configuration file: '" + beanFile.getName() + "'.", e);
        }
      }
    }

  }

  private static String buildSpringBeanXmlHeader() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n"
        + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
        + "\txsi:schemaLocation=\"http://www.springframework.org/schema/beans\n"
        + "\thttp://www.springframework.org/schema/beans/spring-beans-2.5.xsd\">\n\n";

  }

  // FIXME tricky public methods
  public DAO getDAO(final DataSetMetadata dataSet) {
    return this.daos.get(dataSet);
  }

  public DAOPrimitives getDAOPrimitives(final DataSetMetadata dataSet) {
    return this.daoPrimitives.get(dataSet);
  }
}
