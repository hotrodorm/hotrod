package org.hotrod.generator.mybatisspring;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.DisplayMode;
import org.hotrod.config.EnabledFKs;
import org.hotrod.config.ExecutorTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisSpringTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
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
import org.hotrod.utils.LocalFileGenerator;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class MyBatisSpringGenerator implements Generator, LiveGenerator {

  private static final Logger log = LogManager.getLogger(MyBatisSpringGenerator.class);
  private static final Logger logm = LogManager.getLogger("hotrod-metadata-retrieval");

  private HotRodContext hc;

  protected DatabaseLocation dloc;
  protected DatabaseAdapter adapter;
  protected HotRodConfigTag config;
  protected JdbcDatabase db;
  private Metadata md;

  protected DisplayMode displayMode;
  protected Feedback feedback;

  private Long lastLog = null;

  private MyBatisSpringTag myBatisSpringTag;
  private DataSetLayout layout;

  private LinkedHashMap<DataSetMetadata, ObjectAbstractVO> abstractVos = new LinkedHashMap<DataSetMetadata, ObjectAbstractVO>();
  private LinkedHashMap<DataSetMetadata, ObjectVO> vos = new LinkedHashMap<DataSetMetadata, ObjectVO>();
  private LinkedHashMap<DataSetMetadata, ObjectDAO> daos = new LinkedHashMap<DataSetMetadata, ObjectDAO>();
  private LinkedHashMap<DataSetMetadata, Mapper> mappers = new LinkedHashMap<DataSetMetadata, Mapper>();
  private LinkedHashMap<EnumDataSetMetadata, EnumClass> enumClasses = new LinkedHashMap<EnumDataSetMetadata, EnumClass>();
  private List<ObjectAbstractVO> tableAbstractVOs = new ArrayList<ObjectAbstractVO>();

  private EntityDAORegistry entityDAORegistry = new EntityDAORegistry();

  private LayerConfigWriter layerConfigWriter;

  public MyBatisSpringGenerator(final HotRodContext hc, final EnabledFKs enabledFKs, final DisplayMode displayMode,
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

    logm("Starting core generator.");

    // Display the retrieved meta data

    // logm("Metadata initialized.");

    displayGenerationMetadata(config);

    // logSelectMethodMetadata(); // keep for debugging purposes only

  }

  @Override
  public void prepareGeneration() throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
    log.debug("prepare");

    // Load and validate the configuration file

    this.myBatisSpringTag = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();
    this.layout = new DataSetLayout(this.config);

    // Reset the generated object counters

    this.config.resetTreeGeneratables();

    // Add tables

    for (TableDataSetMetadata tm : this.md.getTables()) {
      log.debug("tm=" + tm.getId().getCanonicalSQLName());
      EntityVOs entityVOs = addDaosAndMapper(tm, DAOType.TABLE);
      for (SelectMethodMetadata sm : tm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

    // Link parent tables

    for (ObjectAbstractVO avo : this.tableAbstractVOs) {
      if (avo.getMetadata().getParentMetadata() != null) {
        avo.getBundle().setParent(null);
        for (ObjectAbstractVO ovo : this.tableAbstractVOs) {
          if (avo != ovo && avo.getMetadata().getParentMetadata().getId().equals(ovo.getMetadata().getId())) {
            avo.getBundle().setParent(ovo.getBundle());
          }
        }
        if (avo.getBundle().getParent() == null) {
          throw new InvalidConfigurationFileException(avo.getMetadata().getDaoTag(),
              "Could not find parent table '" + avo.getMetadata().getParentMetadata().getId() + "' extended by table '"
                  + avo.getMetadata().getId() + "'.");
        }
      }
    }

    // Add views

    for (TableDataSetMetadata vm : this.md.getViews()) {
      EntityVOs entityVOs = addDaosAndMapper(vm, DAOType.VIEW);
      for (SelectMethodMetadata sm : vm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

    // Add enums

    for (EnumDataSetMetadata em : this.md.getEnums()) {
      this.enumClasses.put(em,
          new EnumClass(em, new DataSetLayout(this.config), this.myBatisSpringTag.getDaos(), this));
    }

    // Add executors

    for (ExecutorDAOMetadata dm : this.md.getExecutors()) {
      EntityVOs entityVOs = addDaosAndMapper(dm, DAOType.EXECUTOR);
      for (SelectMethodMetadata sm : dm.getSelectsMetadata()) {
        addSelectVOs(sm, entityVOs);
      }
    }

    this.layerConfigWriter = new LayerConfigWriter(layout, this.config.getTypeSolverTag());

  }

  public static class EntityVOs {

    private ObjectAbstractVO abstractVO;
    private ObjectVO vo;

    public EntityVOs(ObjectAbstractVO abstractVO, ObjectVO vo) {
      this.abstractVO = abstractVO;
      this.vo = vo;
    }

    public ObjectAbstractVO getAbstractVO() {
      return abstractVO;
    }

    public ObjectVO getVo() {
      return vo;
    }

  }

  private EntityVOs addDaosAndMapper(final DataSetMetadata metadata, final DAOType type) throws ControlledException {

    MyBatisSpringTag myBatisTag = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();

    DataSetLayout layout;
    ObjectAbstractVO abstractVO;
    ObjectVO vo;
    Mapper mapper;
    ObjectDAO dao;

    Bundle bundle;

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

      abstractVO = new ObjectAbstractVO(metadata, layout, this, DAOType.TABLE, myBatisTag);
      this.tableAbstractVOs.add(abstractVO);
      vo = new ObjectVO(metadata, layout, this, abstractVO, myBatisTag);
      mapper = new Mapper(ttag, metadata, layout, this, type, this.adapter, vo, this.entityDAORegistry);
      dao = new ObjectDAO(ttag, metadata, layout, this, type, myBatisTag, this.adapter, abstractVO, vo, mapper);
      vo.setDAO(dao);
      this.entityDAORegistry.add(vo.getFullClassName(), dao);
      mapper.setDao(dao);

      bundle = new Bundle(abstractVO, vo, dao, mapper);
      abstractVO.setBundle(bundle);
      vo.setBundle(bundle);
      mapper.setBundle(bundle);
      dao.setBundle(bundle);

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

      abstractVO = new ObjectAbstractVO(metadata, layout, this, DAOType.VIEW, myBatisTag);
      vo = new ObjectVO(metadata, layout, this, abstractVO, myBatisTag);
      mapper = new Mapper(vtag, metadata, layout, this, type, this.adapter, vo, this.entityDAORegistry);
      dao = new ObjectDAO(vtag, metadata, layout, this, type, myBatisTag, this.adapter, abstractVO, vo, mapper);
      vo.setDAO(dao);
      this.entityDAORegistry.add(vo.getFullClassName(), dao);
      mapper.setDao(dao);

      bundle = new Bundle(abstractVO, vo, dao, mapper);
      abstractVO.setBundle(bundle);
      vo.setBundle(bundle);
      mapper.setBundle(bundle);
      dao.setBundle(bundle);

      break;

    case EXECUTOR:
      AbstractDAOTag tag = metadata.getDaoTag();
      layout = new DataSetLayout(this.config);
      abstractVO = null;
      vo = null;

      mapper = new Mapper(tag, metadata, layout, this, type, this.adapter, vo, this.entityDAORegistry);
      dao = new ObjectDAO(tag, metadata, layout, this, type, myBatisTag, this.adapter, abstractVO, vo, mapper);
      mapper.setDao(dao);

      bundle = new Bundle(abstractVO, vo, dao, mapper);
      mapper.setBundle(bundle);
      dao.setBundle(bundle);

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
    this.mappers.put(metadata, mapper);
    this.daos.put(metadata, dao);

    return vo == null ? null : new EntityVOs(abstractVO, vo);

  }

  private List<String> getAllMappersSourceFileNames() {

    List<String> allMappersSourceFileNames = new ArrayList<String>();

    for (TableTag t : this.config.getAllTables()) {
      ClassPackage fragmentPackage = t.getFragmentConfig() != null && t.getFragmentConfig().getFragmentPackage() != null
          ? t.getFragmentConfig().getFragmentPackage()
          : null;
      String sourceFileName = Mapper.assembleSourceFileName(layout, fragmentPackage, t.getId());
      allMappersSourceFileNames.add(sourceFileName);
    }

    for (ViewTag t : this.config.getAllViews()) {
      ClassPackage fragmentPackage = t.getFragmentConfig() != null && t.getFragmentConfig().getFragmentPackage() != null
          ? t.getFragmentConfig().getFragmentPackage()
          : null;
      String sourceFileName = Mapper.assembleSourceFileName(layout, fragmentPackage, t.getId());
      allMappersSourceFileNames.add(sourceFileName);
    }

    for (ExecutorTag t : this.config.getAllExecutors()) {
      ClassPackage fragmentPackage = t.getFragmentConfig() != null && t.getFragmentConfig().getFragmentPackage() != null
          ? t.getFragmentConfig().getFragmentPackage()
          : null;
      ObjectId id = null;
      try {
        id = new ObjectId(Id.fromJavaClass(t.getJavaClassName()));
      } catch (InvalidIdentifierException e) {
        // Ignore
      }
      String sourceFileName = Mapper.assembleSourceFileName(layout, fragmentPackage, id);
      allMappersSourceFileNames.add(sourceFileName);
    }

    return allMappersSourceFileNames;
  }

  private LinkedHashSet<SelectAbstractVO> abstractSelectVOs = new LinkedHashSet<SelectAbstractVO>();
  private LinkedHashSet<SelectVO> selectVOs = new LinkedHashSet<SelectVO>();

  private void addSelectVOs(final SelectMethodMetadata sm, final EntityVOs entityVOs) throws ControlledException {

    if (entityVOs != null) {

      sm.setEntityVOs(entityVOs);

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
        SelectAbstractVO abstractVO = new SelectAbstractVO(abstractSoloVO, this.layout, this.myBatisSpringTag);
        this.abstractSelectVOs.add(abstractVO);
        SelectVO vo = new SelectVO(soloVO, abstractVO, this.layout);
        this.selectVOs.add(vo);
        log.debug("### soloVO.getName()=" + soloVO.getName() + " abstractVO.getName()=" + abstractVO.getName());
      }

      // connected VOs (all)

      if (sm.getStructuredColumns() != null) {
        for (VOMetadata vo : sm.getStructuredColumns().getVOs()) {
          log.trace("### Metadata: vo.getName()=" + vo.getName());
          registerVOs(vo);
        }
      }

    }

  }

  private void registerVOs(final VOMetadata vo) {

    if (vo.getEntityVOSuperClass() != null) {

      VOClasses voc = produceVOClasses(vo);

      log.trace("@@@ x=" + voc.vo.getClassName() + " abstractVO.getName()=" + voc.abstractVO.getName());

      this.abstractSelectVOs.add(voc.abstractVO);
      this.selectVOs.add(voc.vo);

    }

    for (VOMetadata a : vo.getAssociations()) {
      this.registerVOs(a);
    }
    for (VOMetadata c : vo.getCollections()) {
      this.registerVOs(c);
    }

  }

  public VOClasses produceVOClasses(final VOMetadata vo) {
    VOClasses voc = new VOClasses();
    voc.abstractVO = new SelectAbstractVO(vo, this.layout, this.myBatisSpringTag);
    voc.vo = new SelectVO(vo, voc.abstractVO, this.layout, this.myBatisSpringTag);
    return voc;
  }

  public static class VOClasses {
    public SelectAbstractVO abstractVO;
    public SelectVO vo;
  }

  @Override // HotRodGenerator
  public void generate() throws UncontrolledException, ControlledException {
    display("");
    display("Generating MyBatis DAOs & VOs...");
    LocalFileGenerator fg = new LocalFileGenerator();
    this.generate(fg);
    display("");
    display("MyBatis generation complete.");
  }

  @Override // LiveGenerator
  public void generate(final FileGenerator fileGenerator) throws UncontrolledException, ControlledException {

    // Abstract VOs, VOs, DAOs, and Mappers for <table> & <view> tags

    log.debug("this.abstractVos=" + this.abstractVos.size());
    log.debug("this.vos=" + this.vos.size());
    log.debug("this.daos=" + this.daos.size());

    for (ObjectAbstractVO abstractVO : this.abstractVos.values()) {
      abstractVO.generate(fileGenerator);
    }

    for (ObjectVO vo : this.vos.values()) {
      vo.generate(fileGenerator);
    }

    for (SelectAbstractVO avo : this.abstractSelectVOs) {
      avo.generate(fileGenerator);
    }

    for (SelectVO vo : this.selectVOs) {
      vo.generate(fileGenerator);
    }

    for (Mapper mapper : this.mappers.values()) {
      mapper.generate(fileGenerator);
    }

    for (ObjectDAO dao : this.daos.values()) {
      dao.generate(fileGenerator, this);
    }

    // TODO: Re-enable the Available FKs file.
    // this.availableFKs.generate(fileGenerator);

    // Enums

    for (EnumClass ec : this.enumClasses.values()) {
      ec.generate(fileGenerator);
    }

    // MyBatis cursor implementation

    // this.mybatisCursor.generate();
    
    this.layerConfigWriter.generate(fileGenerator, this);

  }

  // Getters

  public ObjectVO getVO(final DataSetMetadata dataSet) {
    return this.vos.get(dataSet);
  }

  public ObjectDAO getDAO(final DataSetMetadata dataSet) {
    return this.daos.get(dataSet);
  }

  public EnumClass getEnum(final DataSetMetadata dataSet) {
    return this.enumClasses.get(dataSet);
  }

  public Mapper getMapper(final DataSetMetadata dataSet) {
    return this.mappers.get(dataSet);
  }

  public boolean isClassicFKNavigationEnabled() {
    return this.myBatisSpringTag.getClassicFKNavigation() != null;
  }

  // Helpers

  public void display(final String txt) {
    this.feedback.info(SUtil.isEmpty(txt) ? " " : txt);
  }

  private void logm(final String msg) {
    long now = System.currentTimeMillis();
    if (this.lastLog == null) {
      logm.debug("[===== Initial =====] - " + msg);
    } else {
      logm.debug("[===== " + (now - this.lastLog) + " ms =====] - " + msg);
    }
    this.lastLog = now;
  }

  // Implements Generator

  @Override
  public VORegistry getVORegistry() {
    throw new UnsupportedOperationException("getVORegistry() is not supported by generators");
  }

  // Getters

  @Override
  public HotRodConfigTag getConfig() {
    throw new UnsupportedOperationException("getConfig() is not supported by generators");
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
    display("");

    if (this.displayMode == DisplayMode.LIST) {

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
            log.debug("s=" + s);
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
