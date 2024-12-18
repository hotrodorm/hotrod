package org.hotrod.generator.jdbc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.DisplayMode;
import org.hotrod.config.EnabledFKs;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.generator.mybatisspring.EnumClass;
import org.hotrod.generator.mybatisspring.SelectAbstractVO;
import org.hotrod.generator.mybatisspring.SelectVO;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.ExecutorDAOMetadata;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodMetadata.SelectMethodReturnType;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.metadata.VORegistry;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.utils.ClassPackage;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class JDBCGenerator implements Generator, LiveGenerator {

  private static final Logger log = Logger.getLogger(JDBCGenerator.class.getName());

  private HotRodContext hc;

  protected DatabaseLocation dloc;
  protected DatabaseAdapter adapter;
  protected HotRodConfigTag config;
  protected JdbcDatabase db;
  private Metadata md;

  protected DisplayMode displayMode;
  protected Feedback feedback;

  private Long lastLog = null;

  private JDBCTag jdbcTag;
  private DataSetLayout layout;

  private LinkedHashMap<DataSetMetadata, Entity> abstractVos = new LinkedHashMap<>();
  private LinkedHashMap<DataSetMetadata, Model> vos = new LinkedHashMap<>();
  private LinkedHashMap<DataSetMetadata, DAO> daos = new LinkedHashMap<>();
  private LinkedHashMap<EnumDataSetMetadata, EnumClass> enumClasses = new LinkedHashMap<>();
  private List<Entity> tableAbstractVOs = new ArrayList<>();

  private LayerConfigWriter layerConfigWriter;

  public JDBCGenerator(final HotRodContext hc, final EnabledFKs enabledFKs, final DisplayMode displayMode,
      final boolean incrementalMode, final Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {

    this.hc = hc;

    this.dloc = this.hc.getLoc();
    this.adapter = this.hc.getAdapter();
    this.config = this.hc.getConfig();
    this.db = this.hc.getDb();
    this.md = this.hc.getMetadata();

    this.displayMode = displayMode;
    this.feedback = feedback;

  }

  // =============================================================================================
  // =============================================================================================
  // =============================================================================================

  @Override
  public void prepareGeneration() throws UncontrolledException, ControlledException, InvalidConfigurationFileException {

    this.jdbcTag = (JDBCTag) this.config.getGenerators().getSelectedGeneratorTag();
    this.layout = new DataSetLayout(this.config);

    // Add tables

    for (TableDataSetMetadata tm : this.md.getTables()) {
      Model entityVOs = addDaosAndMapper(tm, DAOType.TABLE);
      for (SelectMethodMetadata sm : tm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

    // Add views

    for (TableDataSetMetadata vm : this.md.getViews()) {
      Model entityVOs = addDaosAndMapper(vm, DAOType.VIEW);
      for (SelectMethodMetadata sm : vm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

    // Add enums

    for (EnumDataSetMetadata em : this.md.getEnums()) {
      this.enumClasses.put(em, new EnumClass(em, new DataSetLayout(this.config), this.jdbcTag.getDaos(), this));
    }

    // Add executors

    for (ExecutorDAOMetadata dm : this.md.getExecutors()) {
      Model entityVOs = addDaosAndMapper(dm, DAOType.EXECUTOR);
      for (SelectMethodMetadata sm : dm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

    this.layerConfigWriter = new LayerConfigWriter(layout, this.config.getTypeSolverTag());

  }

  private Model addDaosAndMapper(final DataSetMetadata metadata, final DAOType type) throws ControlledException {

    JDBCTag myBatisTag = (JDBCTag) this.config.getGenerators().getSelectedGeneratorTag();

    DataSetLayout layout;
    Entity abstractVO;
    Model vo;
    DAO dao;

//    Bundle bundle;

    switch (type) {

    case TABLE:
      TableTag ttag = this.config.findFacetTable(metadata, this.adapter);
      if (ttag == null) {
        ttag = (TableTag) metadata.getDaoTag();
        if (ttag == null) {
          throw new ControlledException(
              "Could not find table tag for table '" + metadata.getId().getCanonicalSQLName() + "'.");
        }
      }
      layout = new DataSetLayout(this.config, ttag);

      abstractVO = new Entity(metadata, layout, this, DAOType.TABLE, myBatisTag);
      this.tableAbstractVOs.add(abstractVO);
      vo = new Model(metadata, layout, this, abstractVO, myBatisTag);
      dao = new DAO(ttag, metadata, layout, this, type, myBatisTag, this.adapter, abstractVO, vo);
      vo.setDAO(dao);
      break;

    case VIEW:
      ViewTag vtag = this.config.findFacetView(metadata, this.adapter);
      if (vtag == null) {
        vtag = (ViewTag) metadata.getDaoTag();
        if (vtag == null) {
          throw new ControlledException(
              "Could not find view tag for table '" + metadata.getId().getCanonicalSQLName() + "'.");
        }
      }
      layout = new DataSetLayout(this.config);

      abstractVO = new Entity(metadata, layout, this, DAOType.VIEW, myBatisTag);
      vo = new Model(metadata, layout, this, abstractVO, myBatisTag);
      dao = new DAO(vtag, metadata, layout, this, type, myBatisTag, this.adapter, abstractVO, vo);
      vo.setDAO(dao);
      break;

    case EXECUTOR:
      AbstractDAOTag tag = metadata.getDaoTag();
      layout = new DataSetLayout(this.config);
      abstractVO = null;
      vo = null;

      dao = new DAO(tag, metadata, layout, this, type, myBatisTag, this.adapter, abstractVO, vo);
      break;

    default:
      throw new ControlledException(
          "Unrecognized type for database object '" + metadata.getId().getCanonicalSQLName() + "'.");
    }

    if (abstractVO != null) {
      this.abstractVos.put(metadata, abstractVO);
    }
    if (vo != null) {
      this.vos.put(metadata, vo);
    }
    this.daos.put(metadata, dao);

    return vo;

  }

  private LinkedHashSet<SelectAbstractVO> abstractSelectVOs = new LinkedHashSet<SelectAbstractVO>();
  private LinkedHashSet<SelectVO> selectVOs = new LinkedHashSet<SelectVO>();

  private void addSelectVOs(final SelectMethodMetadata sm, final Model entityVOs) throws ControlledException {

    if (entityVOs != null) {

//      sm.setEntityVOs(entityVOs);

    } else {
      // DataSetLayout layout = new DataSetLayout(this.config);
      HotRodFragmentConfigTag fragmentConfig = sm.getFragmentConfig();
      ClassPackage fragmentPackage = fragmentConfig != null && fragmentConfig.getFragmentPackage() != null
          ? fragmentConfig.getFragmentPackage()
          : null;
      ClassPackage daoPackage = this.layout.getDAOPackage(fragmentPackage);
      SelectMethodReturnType rt = sm.getReturnType(daoPackage);

      // solo VO

      SelectVOClass soloVO = rt.getSoloVO();
      SelectVOClass abstractSoloVO = rt.getAbstractSoloVO();

      if (soloVO != null) {
        SelectAbstractVO abstractVO = new SelectAbstractVO(abstractSoloVO, this.layout, this.jdbcTag);
        this.abstractSelectVOs.add(abstractVO);
        SelectVO vo = new SelectVO(soloVO, abstractVO, this.layout);
        this.selectVOs.add(vo);
        log.fine("### soloVO.getName()=" + soloVO.getName() + " abstractVO.getName()=" + abstractVO.getName());
      }

      // connected VOs (all)

      if (sm.getStructuredColumns() != null) {
        for (VOMetadata vo : sm.getStructuredColumns().getVOs()) {
          log.finer("### Metadata: vo.getName()=" + vo.getName());
//          registerVOs(vo);
        }
      }

    }

  }

  // =============================================================================================
  // =============================================================================================
  // =============================================================================================

  @Override
  public void generate(FileGenerator fileGenerator) throws UncontrolledException, ControlledException {
    log.info("JDBC GENERATE");

    for (Model vo : this.vos.values()) {
      vo.generate(fileGenerator);
    }

    for (Entity avo : this.abstractVos.values()) {
      avo.generate(fileGenerator);
    }

    for (DAO dao : this.daos.values()) {
      dao.generate(fileGenerator, this);
    }

    this.layerConfigWriter.generate(fileGenerator, this);

  }

  public EnumClass getEnum(final DataSetMetadata dataSet) {
    return this.enumClasses.get(dataSet);
  }

  @Override
  public void display(String txt) {
    // TODO Auto-generated method stub

  }

  @Override
  public VORegistry getVORegistry() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public HotRodConfigTag getConfig() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void generate() throws UncontrolledException, ControlledException {
    // TODO Auto-generated method stub

  }

}
