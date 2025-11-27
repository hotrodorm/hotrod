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
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
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
import org.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

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

  private LinkedHashMap<DataSetMetadata, LayoutWriter> layouts = new LinkedHashMap<>();
  private LinkedHashMap<DataSetMetadata, ModelWriter> models = new LinkedHashMap<>();
  private LinkedHashMap<DataSetMetadata, DAOWriter> daos = new LinkedHashMap<>();
  private LinkedHashMap<EnumDataSetMetadata, EnumClass> enumClasses = new LinkedHashMap<>();
  private List<LayoutWriter> tableAbstractVOs = new ArrayList<>();

  private LayerResourcesBeanWriter layerConfig;

  public JDBCGenerator(final HotRodContext hc, final EnabledFKs enabledFKs, final DisplayMode displayMode,
      final boolean incrementalMode, final Feedback feedback)
      throws FaultException, ErrorMessageException, InvalidConfigurationFileException {

//    log.info("CONFIGURE GENERATION");

    this.hc = hc;

    this.dloc = this.hc.getLoc();
    this.adapter = this.hc.getAdapter();
    this.config = this.hc.getConfig();
    this.db = this.hc.getDb();
    this.md = this.hc.getMetadata();

    this.displayMode = displayMode;
    this.feedback = feedback;

    displayGenerationMetadata(config);

  }

  // =============================================================================================
  // =============================================================================================
  // =============================================================================================

  @Override
  public void prepareGeneration() throws FaultException, ErrorMessageException, InvalidConfigurationFileException {

//    log.info("PREPARE GENERATION");

    this.jdbcTag = (JDBCTag) this.config.getGenerators().getSelectedGeneratorTag();

    this.layerConfig = new LayerResourcesBeanWriter(this.jdbcTag, this.config.getRuntimeTypeSolverTag());

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

  private EntityDTOs addDaosAndMapper(final DataSetMetadata metadata, final DAOType type) throws ErrorMessageException {

    JDBCTag jdbcTag = (JDBCTag) this.config.getGenerators().getSelectedGeneratorTag();

    LayoutWriter layout;
    ModelWriter model;
    DAOWriter dao;

    switch (type) {

    case TABLE:
      TableTag ttag = this.config.findFacetTable(metadata, this.adapter);
      if (ttag == null) {
        ttag = (TableTag) metadata.getDaoTag();
        if (ttag == null) {
          throw new ErrorMessageException(
              "Could not find table tag for table '" + metadata.getId().getCanonicalSQLName() + "'.");
        }
      }

      layout = new LayoutWriter(metadata, this, DAOType.TABLE, jdbcTag);
      this.tableAbstractVOs.add(layout);
      model = new ModelWriter(metadata, this, layout, jdbcTag);
      dao = new DAOWriter(ttag, metadata, this, type, jdbcTag, this.adapter, layout, model, this.layerConfig);
      model.setDAO(dao);
      break;

    case VIEW:
      ViewTag vtag = this.config.findFacetView(metadata, this.adapter);
      if (vtag == null) {
        vtag = (ViewTag) metadata.getDaoTag();
        if (vtag == null) {
          throw new ErrorMessageException(
              "Could not find view tag for table '" + metadata.getId().getCanonicalSQLName() + "'.");
        }
      }

      layout = new LayoutWriter(metadata, this, DAOType.VIEW, jdbcTag);
      model = new ModelWriter(metadata, this, layout, jdbcTag);
      dao = new DAOWriter(vtag, metadata, this, type, jdbcTag, this.adapter, layout, model, this.layerConfig);
      model.setDAO(dao);
      break;

    case EXECUTOR:
      AbstractDAOTag tag = metadata.getDaoTag();
      layout = null;
      model = null;

      dao = new DAOWriter(tag, metadata, this, type, jdbcTag, this.adapter, layout, model, this.layerConfig);
      break;

    default:
      throw new ErrorMessageException(
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

  private LinkedHashSet<SelectLayoutWriter> abstractSelectVOs = new LinkedHashSet<SelectLayoutWriter>();
  private LinkedHashSet<SelectModelWriter> selectVOs = new LinkedHashSet<SelectModelWriter>();

  private void addSelectVOs(final SelectMethodMetadata sm, final EntityDTOs entityVOs) throws ErrorMessageException {

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
        SelectLayoutWriter abstractVO = new SelectLayoutWriter(abstractSoloVO, this.jdbcTag);
        this.abstractSelectVOs.add(abstractVO);
        SelectModelWriter vo = new SelectModelWriter(soloVO, abstractVO, this.jdbcTag);
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
  public void generate(FileGenerator fileGenerator) throws FaultException, ErrorMessageException {

    for (ModelWriter mo : this.models.values()) {
      mo.generate(fileGenerator);
    }

    for (LayoutWriter la : this.layouts.values()) {
      la.generate(fileGenerator);
    }

    for (DAOWriter dao : this.daos.values()) {
      dao.generate(fileGenerator, this);
    }

    for (SelectLayoutWriter sla : this.abstractSelectVOs) {
      sla.generate(fileGenerator);
    }

    for (SelectModelWriter smo : this.selectVOs) {
      smo.generate(fileGenerator);
    }

    this.layerConfig.generate(fileGenerator);

  }

  public EnumClass getEnum(final DataSetMetadata dataSet) {
    return this.enumClasses.get(dataSet);
  }

  @Override
  public void display(final String txt) {
    this.feedback.info(SUtil.isEmpty(txt) ? " " : txt);
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
  public void generate() throws FaultException, ErrorMessageException {
    // TODO Auto-generated method stub

  }

  private void displayGenerationMetadata(final HotRodConfigTag config) {

    int sequences = 0;
    int queries = 0;
    int selectMethods = 0;

    if (config.getFacetNames().isEmpty()) {
      display("Generating all facets.");
    } else {
      ListWriter lw = new ListWriter(", ");
      for (String facetName : config.getFacetNames()) {
        lw.add(facetName);
      }
      display("Generating facet" + (config.getFacetNames().size() == 1 ? "" : "s") + ": " + lw.toString());
    }

    if (this.displayMode == DisplayMode.LIST) {

      display("");

      // tables

      for (TableDataSetMetadata t : this.md.getTables()) {
        String ns = getNS(t);
        display("Table " + getNS(t) + t.getId().getCanonicalSQLName() + " included.");
        for (SequenceMethodTag s : t.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getSequenceId().getRenderedSQLName() + " included.");
          }
        }
        for (QueryMethodTag q : t.getQueries()) {
          queries++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Query " + q.getMethod() + " included.");
          }
        }
        for (SelectMethodMetadata s : t.getSelectsMetadata()) {
          selectMethods++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Select " + s.getMethod() + " included.");
          }
        }
      }

      // views

      for (TableDataSetMetadata v : this.md.getViews()) {
        display("View " + getNS(v) + v.getId().getCanonicalSQLName() + " included.");
        for (SequenceMethodTag s : v.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getSequenceId().getRenderedSQLName() + " included.");
          }
        }
        for (QueryMethodTag q : v.getQueries()) {
          queries++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Query " + q.getMethod() + " included.");
          }
        }
        for (SelectMethodMetadata s : v.getSelectsMetadata()) {
          selectMethods++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Select " + s.getMethod() + " included.");
          }
        }
      }

      // enums

      for (EnumDataSetMetadata e : this.md.getEnums()) {
        display("Enum " + getNS(e) + e.getJdbcName() + " included.");
      }

      // daos

      for (ExecutorDAOMetadata d : this.md.getExecutors()) {
        if (this.displayMode == DisplayMode.LIST) {
          display("DAO " + d.getJavaClassName() + " included.");
        }
        for (SequenceMethodTag s : d.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getSequenceId().getRenderedSQLName() + " included.");
          }
        }
        for (QueryMethodTag q : d.getQueries()) {
          queries++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Query " + q.getMethod() + " included.");
          }
        }
        for (SelectMethodMetadata s : d.getSelectsMetadata()) {
          selectMethods++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Select " + s.getMethod() + " included.");
          }
        }
      }

    }

    display("");
    StringBuilder sb = new StringBuilder();
    sb.append("Total of: ");
    sb.append(this.md.getTables().size() + " " + (this.md.getTables().size() == 1 ? "table" : "tables") + ", ");
    sb.append(this.md.getViews().size() + " " + (this.md.getViews().size() == 1 ? "view" : "views") + ", ");
    sb.append(this.md.getEnums().size() + " " + (this.md.getEnums().size() == 1 ? "enum" : "enums") + ", ");
    sb.append(
        this.config.getFacetExecutors().size() + " " + (this.config.getFacetExecutors().size() == 1 ? "DAO" : "DAOs") //
            + ", and ");

    sb.append(sequences + " sequence" + (sequences == 1 ? "" : "s") + " -- including ");
    sb.append(selectMethods + " " + (selectMethods == 1 ? "select method" : "select methods") + ", ");
    sb.append("and " + queries + " " + (queries == 1 ? "query method" : "query methods") + ".");

    display(sb.toString());

  }

  private String getNS(final TableDataSetMetadata t) {
    String cat = t.getId().getCatalog() == null ? null : t.getId().getCatalog().getCanonicalSQLName();
    String sche = t.getId().getSchema() == null ? null : t.getId().getSchema().getCanonicalSQLName();
    if (!t.isFromCurrentCatalog()) {
      return t.isFromCurrentSchema() ? cat + "." : cat + "." + sche + ".";
    } else {
      return t.isFromCurrentSchema() ? "" : sche + ".";
    }
  }

}
