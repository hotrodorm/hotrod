package org.hotrod.generator.mybatis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.CustomDAOTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.MyBatisTag;
import org.hotrod.config.SelectTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.mybatis.ObjectDAOPrimitives.DAOType;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.SelectDataSetMetadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public class MyBatisGenerator extends HotRodGenerator {

  private static transient final Logger log = Logger.getLogger(MyBatisGenerator.class);

  private LinkedHashMap<DataSetMetadata, ObjectDAO> daos = new LinkedHashMap<DataSetMetadata, ObjectDAO>();
  private LinkedHashMap<DataSetMetadata, ObjectDAOPrimitives> daoPrimitives = //
      new LinkedHashMap<DataSetMetadata, ObjectDAOPrimitives>();
  private LinkedHashMap<DataSetMetadata, MapperPrimitives> mapPrimitives = //
      new LinkedHashMap<DataSetMetadata, MapperPrimitives>();
  private MyBatisConfiguration myBatisConfig;

  private List<CustomDAO> collectionsDAOs = new ArrayList<CustomDAO>();
  private List<CustomDAOMapper> collectionsMappers = new ArrayList<CustomDAOMapper>();

  public MyBatisGenerator(final DatabaseLocation loc, final HotRodConfigTag config, final DisplayMode displayMode)
      throws UncontrolledException, ControlledException {
    super(loc, config, displayMode);
  }

  @Override
  public void prepareGeneration() throws UncontrolledException, ControlledException {
    log.debug("prepare");

    // Load and validate the configuration file

    this.myBatisConfig = new MyBatisConfiguration(this.config);

    // Add tables

    for (DataSetMetadata tm : this.dss) {
      log.debug("tm=" + tm.getIdentifier().getSQLIdentifier());
      addDaosAndMapper(tm, DAOType.TABLE);
    }

    // Add views

    for (TableDataSetMetadata vm : this.views) {
      addDaosAndMapper(vm, DAOType.VIEW);
    }

    // Add selects

    for (SelectDataSetMetadata sm : this.selects) {
      addDaosAndMapper(sm, DAOType.SELECT);
    }

    // Add custom DAOs

    DataSetLayout layout = new DataSetLayout(this.config);

    for (CustomDAOTag tag : this.config.getDAOs()) {
      CustomDAO dao = new CustomDAO(tag, layout, this);
      CustomDAOMapper mapper = new CustomDAOMapper(tag, layout, this);
      dao.setMapper(mapper);
      mapper.setDao(dao);
      this.collectionsDAOs.add(dao);
      this.collectionsMappers.add(mapper);
    }

    // Prepare MyBatis Configuration File list

    for (CustomDAOTag tag : this.config.getAllDAOs()) {
      CustomDAOMapper mapper = new CustomDAOMapper(tag, layout, this);
      this.myBatisConfig.addMapperPrimitives(mapper);
    }

    for (TableTag t : this.config.getAllTables()) {
      Identifier id = new DataSetIdentifier(t.getName(), t.getJavaName());
      this.myBatisConfig.addSourceFile(MapperPrimitives.getSourceFile(id));
    }

    for (ViewTag v : this.config.getAllViews()) {
      Identifier id = new DataSetIdentifier(v.getName(), v.getJavaName());
      this.myBatisConfig.addSourceFile(MapperPrimitives.getSourceFile(id));
    }

    for (SelectTag t : this.config.getAllSelects()) {
      Identifier id = new DataSetIdentifier("s", t.getJavaClassName(), false);
      this.myBatisConfig.addSourceFile(MapperPrimitives.getSourceFile(id));
    }

  }

  private void addDaosAndMapper(final DataSetMetadata metadata, final DAOType type) throws ControlledException {

    DataSetLayout layout;
    ObjectDAOPrimitives daoPrimitives;
    MapperPrimitives mapPrimitives;

    MyBatisTag myBatisTag = (MyBatisTag) this.config.getGenerators().getSelectedGeneratorTag();

    switch (type) {

    case TABLE:
      TableTag ttag = this.config.findTable(metadata, this.adapter);
      if (ttag == null) {
        throw new ControlledException(
            "Could not find table tag for table '" + metadata.getIdentifier().getSQLIdentifier() + "'.");
      }
      layout = new DataSetLayout(this.config, ttag);
      daoPrimitives = new ObjectDAOPrimitives(ttag, metadata, layout, this, type, myBatisTag);
      mapPrimitives = new MapperPrimitives(ttag, metadata, layout, this, type, this.adapter);
      break;

    case VIEW:
      ViewTag vtag = this.config.findView(metadata, this.adapter);
      if (vtag == null) {
        throw new ControlledException(
            "Could not find view tag for table '" + metadata.getIdentifier().getSQLIdentifier() + "'.");
      }
      layout = new DataSetLayout(this.config);
      daoPrimitives = new ObjectDAOPrimitives(vtag, metadata, layout, this, type, myBatisTag);
      mapPrimitives = new MapperPrimitives(vtag, metadata, layout, this, type, this.adapter);
      break;

    case SELECT:
      SelectDataSetMetadata sm = (SelectDataSetMetadata) metadata;
      SelectTag stag = this.config.findSelect(sm, this.adapter);
      if (stag == null) {
        throw new ControlledException(
            "Could not find select tag for with java-class-name '" + sm.getSelectTag().getJavaClassName() + "'.");
      }
      layout = new DataSetLayout(this.config);
      daoPrimitives = new ObjectDAOPrimitives(null, metadata, layout, this, type, myBatisTag);
      mapPrimitives = new MapperPrimitives(null, metadata, layout, this, type, this.adapter);
      break;

    default:
      throw new ControlledException(
          "Unrecognized type for database object '" + metadata.getIdentifier().getSQLIdentifier() + "'.");
    }

    ObjectDAO dao = new ObjectDAO(metadata, layout);

    dao.setDaoPrimitives(daoPrimitives);
    daoPrimitives.setDao(dao);
    daoPrimitives.setMapperPrimitives(mapPrimitives);

    mapPrimitives.setDao(dao);

    this.daos.put(metadata, dao);
    this.daoPrimitives.put(metadata, daoPrimitives);
    this.mapPrimitives.put(metadata, mapPrimitives);
  }

  @Override
  public void generate() throws UncontrolledException, ControlledException {
    log.debug("starting generation 1.");
    for (ObjectDAO dao : this.daos.values()) {
      log.debug("gen: " + dao.getFullClassName());
      dao.generate();
    }
    for (ObjectDAOPrimitives p : this.daoPrimitives.values()) {
      p.generate();
    }
    for (MapperPrimitives m : this.mapPrimitives.values()) {
      m.generate();
    }
    for (CustomDAO d : this.collectionsDAOs) {
      d.generate();
    }
    for (CustomDAOMapper m : this.collectionsMappers) {
      m.generate();
    }
    log.debug("starting generation 2.");
    this.myBatisConfig.generate();
    display("");
    display("MyBatis generation complete.");
  }

  public ObjectDAO getDAO(final DataSetMetadata dataSet) {
    return this.daos.get(dataSet);
  }

  public ObjectDAOPrimitives getDAOPrimitives(final DataSetMetadata dataSet) {
    return this.daoPrimitives.get(dataSet);
  }

  public MapperPrimitives getMapper(final DataSetMetadata dataSet) {
    return this.mapPrimitives.get(dataSet);
  }

  // public DatabaseAdapter getAdapter() {
  // return adapter;
  // }

  // Helpers

}
