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
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.ExecutorDAOMetadata;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodReturnType;
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

  private LinkedHashMap<DataSetMetadata, Layout> layouts = new LinkedHashMap<>();
  private LinkedHashMap<DataSetMetadata, Model> models = new LinkedHashMap<>();
  private LinkedHashMap<DataSetMetadata, DAO> daos = new LinkedHashMap<>();
  private LinkedHashMap<EnumDataSetMetadata, EnumClass> enumClasses = new LinkedHashMap<>();
  private List<Layout> tableAbstractVOs = new ArrayList<>();

  private LayerConfigBeanWriter layerConfig;

  public JDBCGenerator(final HotRodContext hc, final EnabledFKs enabledFKs, final DisplayMode displayMode,
      final boolean incrementalMode, final Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {

//    log.info("CONFIGURE GENERATION");

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

//    log.info("PREPARE GENERATION");

    this.jdbcTag = (JDBCTag) this.config.getGenerators().getSelectedGeneratorTag();

    this.layerConfig = new LayerConfigBeanWriter(this.jdbcTag, this.config.getTypeSolverTag(),
        this.jdbcTag.getQualifier());

    // Add tables

    for (TableDataSetMetadata tm : this.md.getTables()) {
      EntityDTOs entityVOs = addDaosAndMapper(tm, DAOType.TABLE);
      for (SelectMethodMetadata sm : tm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

    // Add views

    for (TableDataSetMetadata vm : this.md.getViews()) {
      EntityDTOs entityVOs = addDaosAndMapper(vm, DAOType.VIEW);
      for (SelectMethodMetadata sm : vm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

    // Add enums

    for (EnumDataSetMetadata em : this.md.getEnums()) {
      this.enumClasses.put(em, new EnumClass(em, this.jdbcTag, this));
    }

    // Add executors

    for (ExecutorDAOMetadata dm : this.md.getExecutors()) {
      EntityDTOs entityVOs = addDaosAndMapper(dm, DAOType.EXECUTOR);
      for (SelectMethodMetadata sm : dm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

  }

  private EntityDTOs addDaosAndMapper(final DataSetMetadata metadata, final DAOType type) throws ControlledException {

    JDBCTag jdbcTag = (JDBCTag) this.config.getGenerators().getSelectedGeneratorTag();

    Layout layout;
    Model model;
    DAO dao;

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

      layout = new Layout(metadata, this, DAOType.TABLE, jdbcTag);
      this.tableAbstractVOs.add(layout);
      model = new Model(metadata, this, layout, jdbcTag);
      dao = new DAO(ttag, metadata, this, type, jdbcTag, this.adapter, layout, model, this.layerConfig);
      model.setDAO(dao);
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

      layout = new Layout(metadata, this, DAOType.VIEW, jdbcTag);
      model = new Model(metadata, this, layout, jdbcTag);
      dao = new DAO(vtag, metadata, this, type, jdbcTag, this.adapter, layout, model, this.layerConfig);
      model.setDAO(dao);
      break;

    case EXECUTOR:
      AbstractDAOTag tag = metadata.getDaoTag();
      layout = null;
      model = null;

      dao = new DAO(tag, metadata, this, type, jdbcTag, this.adapter, layout, model, this.layerConfig);
      break;

    default:
      throw new ControlledException(
          "Unrecognized type for database object '" + metadata.getId().getCanonicalSQLName() + "'.");
    }

    if (layout != null) {
      this.layouts.put(metadata, layout);
    }
    if (model != null) {
      this.models.put(metadata, model);
    }
    this.daos.put(metadata, dao);

    return model == null ? null : new EntityDTOs(layout, model);

  }

  private LinkedHashSet<SelectLayout> abstractSelectVOs = new LinkedHashSet<SelectLayout>();
  private LinkedHashSet<SelectModel> selectVOs = new LinkedHashSet<SelectModel>();

  private void addSelectVOs(final SelectMethodMetadata sm, final EntityDTOs entityVOs) throws ControlledException {

    if (entityVOs != null) {
//      log.info("entityVOs");

      sm.setEntityVOs(entityVOs);

    } else {
//      log.info("other");

      // DataSetLayout layout = new DataSetLayout(this.config);
      HotRodFragmentConfigTag fragmentConfig = sm.getFragmentConfig();
      ClassPackage fragmentPackage = fragmentConfig != null && fragmentConfig.getFragmentPackage() != null
          ? fragmentConfig.getFragmentPackage()
          : null;
      ClassPackage daoPackage = this.jdbcTag.getDAOPackage(fragmentPackage);
      SelectMethodReturnType rt = sm.getReturnType(daoPackage);

      // solo VO

      SelectVOClass soloVO = rt.getSoloVO();
      SelectVOClass abstractSoloVO = rt.getAbstractSoloVO();
//      log.info("soloVO=" + soloVO + " - abstractSoloVO=" + abstractSoloVO);

      if (soloVO != null) {
        SelectLayout abstractVO = new SelectLayout(abstractSoloVO, this.jdbcTag);
        this.abstractSelectVOs.add(abstractVO);
        SelectModel vo = new SelectModel(soloVO, abstractVO, this.jdbcTag);
        this.selectVOs.add(vo);
//        log.fine("### soloVO.getName()=" + soloVO.getName() + " abstractVO.getName()=" + abstractVO.getName());
      }

      // connected VOs (all)

      if (sm.getStructuredColumns() != null) {
        for (VOMetadata vo : sm.getStructuredColumns().getVOs()) {
//          log.finer("### Metadata: vo.getName()=" + vo.getName());
//          registerVOs(vo);
        }
      }

    }

  }

  @Override
  public void generate(FileGenerator fileGenerator) throws UncontrolledException, ControlledException {

    for (Model mo : this.models.values()) {
      mo.generate(fileGenerator);
    }

    for (Layout la : this.layouts.values()) {
      la.generate(fileGenerator);
    }

    for (DAO dao : this.daos.values()) {
      dao.generate(fileGenerator, this);
    }

    for (SelectLayout sla : this.abstractSelectVOs) {
      sla.generate(fileGenerator);
    }

    for (SelectModel smo : this.selectVOs) {
      smo.generate(fileGenerator);
    }

    this.layerConfig.generate(fileGenerator, this);

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
    return this.config;
  }

  @Override
  public void generate() throws UncontrolledException, ControlledException {
    // TODO Auto-generated method stub

  }

}
