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
import org.hotrod.runtime.dynamicsql.SourceLocation;
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
  private MyBatisConfiguration myBatisConfig;

  private EntityDAORegistry entityDAORegistry = new EntityDAORegistry();

  public MyBatisSpringGenerator(final HotRodContext hc, final EnabledFKs enabledFKs, final DisplayMode displayMode,
      final boolean incrementalMode, final Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {

    this.hc = hc;

    this.dloc = this.hc.getLoc();
    this.adapter = this.hc.getAdapter();
    this.config = this.hc.getConfig();
    this.db = this.hc.getDb();
    this.md = hc.getMetadata();

    this.displayMode = displayMode;
    this.feedback = feedback;

    logm("Starting core generator.");

//    display("Database URL: " + dloc.getUrl());

//    Connection conn = null;
//    boolean retrieveSelectMetadata = false;

//    ColumnsRetriever cr = null;

//    try {

//      // Get Connection
//
//      logm("Opening connection...");
//      conn = this.dloc.getConnection();
//      logm("Connection open.");
//      this.adapter.setCurrentCatalogSchema(conn, this.dloc.getDefaultCatalog(), this.dloc.getDefaultSchema());

//      // Database Version
//
//      DatabaseConnectionVersion cv;
//
//      try {
//        logm("Getting initial metadata.");
//        cv = new DatabaseConnectionVersion(conn.getMetaData());
//        logm("Metadata retrieval complete.");
//
//      } catch (SQLException e) {
//        throw new UncontrolledException("Could not retrieve database metadata.", e);
//      }
//
//      display("Database Name: " + cv.renderDatabaseName());
//      display("JDBC Driver: " + cv.renderJDBCDriverName() + " - implements JDBC Specification "
//          + cv.renderJDBCSpecification());

//      // Database Adapter
//
//      display("");
//      if (this.adapter.supportsCatalog()) {
//        display("Default Database Catalog: " + (dloc.getDefaultCatalog() == null ? "" : dloc.getDefaultCatalog()));
//      }
//      if (this.adapter.supportsSchema()) {
//        display("Default Database Schema: " + (dloc.getDefaultSchema() == null ? "" : dloc.getDefaultSchema()));
//      }
//      display("");
//
//      Set<DatabaseObject> tables = new HashSet<DatabaseObject>();
//      for (TableTag t : this.config.getFacetTables()) {
//        tables.add(t.getDatabaseObjectId());
//      }
//      for (EnumTag e : this.config.getFacetEnums()) {
//        tables.add(e.getDatabaseObjectId());
//      }
//
//      Set<DatabaseObject> views = new HashSet<DatabaseObject>();
//      for (ViewTag v : this.config.getFacetViews()) {
//        views.add(v.getDatabaseObjectId());
//      }
//
//      log.debug("database retrieval (if needed).");
//
//      try {
//
//        this.db = new JdbcDatabase(this.dloc, tables, views);
//        this.adapter.setCurrentCatalogSchema(conn, this.dloc.getDefaultCatalog(), this.dloc.getDefaultSchema());
//
//      } catch (ReaderException e) {
//        throw new ControlledException(e.getMessage());
//      } catch (SQLException e) {
//        throw new UncontrolledException("Could not retrieve database metadata.", e);
//      } catch (CatalogNotSupportedException e) {
//        throw new ControlledException("This database does not support catalogs through the JDBC driver. "
//            + "Please specify an empty value for the default catalog property instead of '" + dloc.getDefaultCatalog()
//            + "'.");
//      } catch (InvalidCatalogException e) {
//        StringBuilder sb = new StringBuilder();
//        if (dloc.getDefaultCatalog() == null) {
//          sb.append("Please specify a default catalog.\n\n");
//        } else {
//          sb.append(
//              "The specified default catalog '" + dloc.getDefaultCatalog() + "' does not exist in this database.\n\n");
//        }
//        sb.append("The available catalogs are:\n");
//        for (String c : e.getExistingCatalogs()) {
//          sb.append("  " + c + "\n");
//        }
//        throw new ControlledException(sb.toString());
//      } catch (SchemaNotSupportedException e) {
//        throw new ControlledException("This database does not support schemas through the JDBC driver. "
//            + "Please specify an empty value for the default schema property instead of '" + dloc.getDefaultCatalog()
//            + "'.");
//      } catch (InvalidSchemaException e) {
//        StringBuilder sb = new StringBuilder();
//        if (dloc.getDefaultSchema() == null) {
//          sb.append("Please specify a default schema.\n\n");
//        } else {
//          sb.append(
//              "The specified default schema '" + dloc.getDefaultSchema() + "' does not exist in this database.\n\n");
//        }
//        sb.append("The available schemas are:\n");
//        for (String s : e.getExistingSchemas()) {
//          sb.append("  " + s + "\n");
//        }
//        throw new ControlledException(sb.toString());
//      } catch (UnsupportedDatabaseException e) {
//        throw new ControlledException("This database is not currently supported by " + Constants.TOOL_NAME);
//      } catch (DatabaseObjectNotFoundException e) {
//        throw new ControlledException(
//            "Database object not found. Please check this is the correct database, catalog, and schema: "
//                + e.getMessage());
//      } catch (RuntimeException e) {
//        e.printStackTrace();
//        throw new UncontrolledException("Could not retrieve database metadata using JDBC URL " + dloc.getUrl(), e);
//      }
//
//      log.debug("database loaded.");
//      config.logGenerateMark("VALIDATE", ':');

    // Validate names

//      logm("Validate Names.");
//
//      Set<String> sqlNames = new HashSet<String>();

    // Prepare tables meta data

//      logm("Prepare tables metadata.");
//
//      DataSetLayout layout = new DataSetLayout(this.config);
//      DaosTag daosTag = this.config.getGenerators().getSelectedGeneratorTag().getDaos();

//      this.tables = new LinkedHashSet<TableDataSetMetadata>();
//      for (JdbcTable t : this.db.getTables()) {
//        try {
//          log.debug("t.getName()=" + t.getName());
////          TableDataSetMetadata tm = DataSetMetadataFactory.getMetadata(t, this.adapter, config, layout, cachedMetadata);
////          log.debug("*** tm=" + tm);
//
//          // This validates the java-name, and it's related to a specific generator, not
//          // the metadata
////          validateIdentifier(sqlNames, "table", t.getName(), tm.getId());
////          this.tables.add(tm);
//
////          ClassPackage fragmentPackage = tm.getFragmentConfig() != null
////              && tm.getFragmentConfig().getFragmentPackage() != null ? tm.getFragmentConfig().getFragmentPackage()
////                  : null;
////          ClassPackage classPackage = layout.getDAOPackage(fragmentPackage);
////          String voName = daosTag.generateVOName(tm.getId());
////          EntityVOClass vo = new EntityVOClass(tm, classPackage, voName, tm.getColumns(), tm.getDaoTag());
////          this.voRegistry.addVO(vo);
//
//        } catch (UnresolvableDataTypeException e) {
//          ColumnMetadata m = e.getColumnMetadata();
//
//          String typeName = JdbcTypes.codeToName(m.getDataType());
//
//          throw new ControlledException(
//              "Unrecognized column data type (reported as '" + m.getTypeName() + "', JDBC type " + m.getDataType() + " "
//                  + (typeName == null ? "(non-standard JDBC type)" : "'" + typeName + "'") + ") on column '"
//                  + m.getColumnName() + "' of table/view/select '" + m.getTableName()
//                  + (e.getMessage() == null ? "" : ": " + e.getMessage()));
//
//        } catch (VOAlreadyExistsException e) {
//          throw new ControlledException("Duplicate table with name '" + t.getName() + "'.");
//        } catch (StructuredVOAlreadyExistsException e) {
//          throw new ControlledException("Duplicate table with name '" + t.getName() + "'.");
//
//        }
//      }

//      // Link table meta data by foreign keys
//
//      for (TableDataSetMetadata tm : this.tables) {
//        tm.linkReferencedTableMetadata(this.tables);
//      }

    // Validate there are no 1-many FK relationships with enums on the many
    // side.

//      for (TableDataSetMetadata tm : this.tables) {
//        for (ForeignKeyMetadata efk : tm.getExportedFKs()) {
//          try {
//            EnumDataSetMetadata em = (EnumDataSetMetadata) efk.getRemote().getTableMetadata();
//            // it's an enum! An enum cannot be used as the children table
//            throw new ControlledException(em.getDaoTag().getSourceLocation(), "Cannot specify the enum '"
//                + em.getId().getRenderedSQLName() + "' on the 'many' side of a 1-to-many relationship.");
//
//          } catch (ClassCastException e) {
//            // It's a table, not an enum - it's valid.
//          }
//        }
//
//      }

//      config.logGenerateMark("AFTER LINKING", ':');

    // // Propagate generation to related db changes (if incremental
    // generation)
    //
    // config.displayGenerateMark("BEFORE PROPAGATE", ':');
    //
    // if (incrementalMode) {
    // Set<TableDataSetMetadata> alreadyWalked = new
    // HashSet<TableDataSetMetadata>();
    // for (TableDataSetMetadata tm : this.tables) {
    // propagateGeneration(tm, alreadyWalked);
    // }
    // }
    //
    // config.displayGenerateMark("AFTER PROPAGATE", ':');

    // Separate enums metadata from tables'

//      logm("Prepare enums metadata.");
//
//      this.enums = new LinkedHashSet<EnumDataSetMetadata>();
//
//      for (Iterator<TableDataSetMetadata> it = this.tables.iterator(); it.hasNext();) {
//        TableDataSetMetadata tm = it.next();
//        try {
//          EnumDataSetMetadata em = (EnumDataSetMetadata) tm;
//          // It's an enum - move it to the enum set.
//          this.enums.add(em);
//          it.remove();
//        } catch (ClassCastException e) {
//          // Not an enum - nothing to do.
//        }
//      }
//
//      // Link EnumMetadata to ColumnMetadata
//
//      for (TableDataSetMetadata ds : this.tables) {
//        ds.linkEnumMetadata(this.enums);
//      }

    // Check tables and enums duplicate names

//      Set<String> tablesAndEnumsCanonicalNames = new HashSet<String>();
//
//      for (TableTag tt : config.getFacetTables()) {
//        String canonicalName = tt.getId().getCanonicalSQLName();
//        if (tablesAndEnumsCanonicalNames.contains(canonicalName)) {
//          throw new ControlledException(tt.getSourceLocation(), "Duplicate database <table> name '" + canonicalName
//              + "'. This table is already defined in the configuration file(s).");
//        }
//        tablesAndEnumsCanonicalNames.add(canonicalName);
//      }
//
//      for (EnumTag et : config.getFacetEnums()) {
//        String canonicalName = et.getId().getCanonicalSQLName();
//        if (tablesAndEnumsCanonicalNames.contains(canonicalName)) {
//          throw new ControlledException(et.getSourceLocation(), "Duplicate database <enum> name '" + canonicalName
//              + "'. This enum is already defined in the configuration file(s), as a <table> or <enum>.");
//        }
//        tablesAndEnumsCanonicalNames.add(canonicalName);
//      }
//
//      Set<String> viewsCanonicalNames = new HashSet<String>();
//
//      for (ViewTag vt : config.getFacetViews()) {
//        String canonicalName = vt.getId().getCanonicalSQLName();
//        if (viewsCanonicalNames.contains(canonicalName)) {
//          throw new ControlledException(vt.getSourceLocation(), "Duplicate database <view> name '" + canonicalName
//              + "'. This enum is already defined in the configuration file(s).");
//        }
//        viewsCanonicalNames.add(canonicalName);
//      }

//      // Prepare views meta data
//
//      logm("Prepare views metadata.");
//
//      this.views = new LinkedHashSet<TableDataSetMetadata>();
//      TableDataSetMetadata vmd = null;
//      for (JdbcTable v : this.db.getViews()) {
//        try {
//
//          vmd = DataSetMetadataFactory.getMetadata(v, this.adapter, config, layout, cachedMetadata);
//
//          validateIdentifier(sqlNames, "view", v.getName(), vmd.getId());
//          this.views.add(vmd);
//
//          ClassPackage fragmentPackage = vmd.getFragmentConfig() != null
//              && vmd.getFragmentConfig().getFragmentPackage() != null ? vmd.getFragmentConfig().getFragmentPackage()
//                  : null;
//          ClassPackage classPackage = layout.getDAOPackage(fragmentPackage);
//          String voName = daosTag.generateVOName(vmd.getId());
//          EntityVOClass vo = new EntityVOClass(vmd, classPackage, voName, vmd.getColumns(), vmd.getDaoTag());
//          this.voRegistry.addVO(vo);
//
//        } catch (UnresolvableDataTypeException e) {
//          throw new ControlledException(e.getMessage());
//
//        } catch (VOAlreadyExistsException e) {
//          throw new ControlledException(vmd.getDaoTag().getSourceLocation(),
//              "Duplicate view with name '" + v.getName() + "'.");
//        } catch (StructuredVOAlreadyExistsException e) {
//          throw new ControlledException(vmd.getDaoTag().getSourceLocation(),
//              "Duplicate view with name '" + v.getName() + "'.");
//
//        }
//      }

//      // Prepare executor DAOs meta data
//
//      SelectMetadataCache selectMetadataCache = cachedMetadata.getSelectMetadataCache();
//      log.debug(">>> 1 selectMetadataCache=" + selectMetadataCache);
//
//      this.executors = new LinkedHashSet<ExecutorDAOMetadata>();
//      for (ExecutorTag tag : config.getFacetExecutors()) {
//        ExecutorDAOMetadata dm;
//        try {
//          dm = new ExecutorDAOMetadata(tag, this.adapter, config, tag.getFragmentConfig(), selectMetadataCache);
//        } catch (InvalidIdentifierException e) {
//          throw new ControlledException(tag.getSourceLocation(),
//              "Invalid DAO with namename '" + tag.getJavaClassName() + "': " + e.getMessage());
//        }
//        this.executors.add(dm);
//      }

//      // Validate against the database
//
////      cr = ColumnsRetriever.getInstance(this.config, this.dloc, this.adapter, this.db, conn);
////      log.debug("ColumnsRetriever: " + cr);
//
//      // TODO: make sure the cache includes enum values from table rows.
//      // if (retrieveFreshDatabaseObjects) {
//      try {
//        this.config.validateAgainstDatabase(this, conn, adapter);
//      } catch (InvalidConfigurationFileException e) {
//        throw new ControlledException(e.getTag().getSourceLocation(), e.getMessage());
//      }
//      // }

//      // Prepare <select> methods meta data - phase 1
//
//      for (TableDataSetMetadata tm : this.tables) {
//        boolean retrieving;
//        try {
//          retrieving = tm.gatherSelectsMetadataPhase1(this, cr, layout);
//        } catch (InvalidConfigurationFileException e) {
//          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
//        }
//        if (retrieving) {
//          retrieveSelectMetadata = true;
//        }
//      }
//
//      for (TableDataSetMetadata vm : this.views) {
//        boolean retrieving;
//        try {
//          retrieving = vm.gatherSelectsMetadataPhase1(this, cr, layout);
//        } catch (InvalidConfigurationFileException e) {
//          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
//        }
//        if (retrieving) {
//          retrieveSelectMetadata = true;
//        }
//      }
//
//      for (TableDataSetMetadata em : this.enums) {
//        boolean retrieving;
//        try {
//          retrieving = em.gatherSelectsMetadataPhase1(this, cr, layout);
//        } catch (InvalidConfigurationFileException e) {
//          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
//        }
//        if (retrieving) {
//          retrieveSelectMetadata = true;
//        }
//      }
//
//      for (ExecutorDAOMetadata dm : this.executors) {
//        boolean retrieving;
//        try {
//          retrieving = dm.gatherSelectsMetadataPhase1(this, cr, layout);
//        } catch (InvalidConfigurationFileException e) {
//          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
//        }
//        if (retrieving) {
//          retrieveSelectMetadata = true;
//        }
//      }

//    } catch (SQLException e) {
//      throw new UncontrolledException("Could not retrieve database metadata.", e);
//
//    } finally {
//      if (conn != null) {
//        try {
//          logm("Closing connection...");
//          conn.close();
//          logm("Connection closed.");
//        } catch (SQLException e) {
//          throw new UncontrolledException("Could not retrieve database metadata.", e);
//        }
//      }
//    }

//    // Prepare <select> DAOs meta data - phase 2
//
//    SelectMetadataCache selectMetadataCache = new SelectMetadataCache();
//    Map<String, List<EnumConstant>> tableEnumConstants = new HashMap<String, List<EnumConstant>>();
//
//    log.debug("/////////////// retrieveSelectMetadata=" + retrieveSelectMetadata);
//
//    if (retrieveSelectMetadata) {
//
//      logm("Prepare selects metadata - phase 2.");
//
//      try {
//
//        for (TableDataSetMetadata tm : this.tables) {
//          tm.gatherSelectsMetadataPhase2(this.voRegistry);
//          addSelectsMetaData(tm.getDaoTag().getJavaClassName(), tm.getSelectsMetadata(), selectMetadataCache);
//        }
//
//        for (TableDataSetMetadata vm : this.views) {
//          vm.gatherSelectsMetadataPhase2(this.voRegistry);
//          addSelectsMetaData(vm.getDaoTag().getJavaClassName(), vm.getSelectsMetadata(), selectMetadataCache);
//        }
//
//        for (TableDataSetMetadata em : this.enums) {
//          em.gatherSelectsMetadataPhase2(this.voRegistry);
//          addSelectsMetaData(em.getDaoTag().getJavaClassName(), em.getSelectsMetadata(), selectMetadataCache);
//          addTableEnumConstants(em, tableEnumConstants);
//        }
//
//        for (ExecutorDAOMetadata xm : this.executors) {
//          xm.gatherSelectsMetadataPhase2(this.voRegistry);
//          addSelectsMetaData(xm.getDaoTag().getJavaClassName(), xm.getSelectsMetadata(), selectMetadataCache);
//        }
//
//      } catch (InvalidConfigurationFileException e) {
//        throw new ControlledException(e.getTag().getSourceLocation(), e.getMessage());
//      } finally {
//        logm("Closing connection (selects)...");
//        try {
//          cr.close();
//        } catch (Exception e) {
//          log.debug("Could not close database connection", e);
//        }
//        logm("Connection closed (selects).");
//      }
//    }

//    // Validate DAO names and methods
//
//    try {
//
//      validateDAONamesAndMethods(config);
//
//    } catch (DuplicateDAOClassException e) {
//      throw new ControlledException(
//          "Duplicate DAO class name '" + e.getClassName() + "' on " + e.getType() + " '" + e.getName()
//              + "'. There's another " + e.getType() + " with the same class name (either specified or computed).");
//    } catch (DuplicateDAOClassMethodException e) {
//      throw new ControlledException("Duplicate method name '" + e.getMethodName() + "' (on DAO class '"
//          + e.getClassName() + "') for the " + e.getType() + " '" + e.getName() + "'. Please consider method names "
//          + "may have been specified (as in <update> tags) "
//          + "or may have been computed based on the SQL names (as in the <sequence> tag).");
//    }

    // Assemble cached metadata for non-incremental generation

//    // TODO: refresh the cache. This strategy should be revisited.
//    if (!incrementalMode) {
//    }
//    this.cachedMetadata.setConfig(config);
//    this.cachedMetadata.setSelectMetadataCache(selectMetadataCache);
//    this.cachedMetadata.setEnumConstants(tableEnumConstants);
//    this.cachedMetadata.setCachedDatabase(this.db);

    // Display the retrieved meta data

//    logm("Metadata initialized.");

    displayGenerationMetadata(config);

    // logSelectMethodMetadata(); // keep for debugging purposes only

  }

//  private void addSelectsMetaData(final String daoName, final List<SelectMethodMetadata> selects,
//      final SelectMetadataCache selectMetadataCache) {
//    for (SelectMethodMetadata sm : selects) {
//      selectMetadataCache.put(daoName, sm.getMethod(), sm);
//    }
//  }
//
//  private void addTableEnumConstants(final TableDataSetMetadata em,
//      final Map<String, List<EnumConstant>> tableEnumConstants) {
//    if (tableEnumConstants.containsKey(em.getDaoTag().getJavaClassName())) {
//      tableEnumConstants.remove(em.getDaoTag().getJavaClassName());
//    }
//    EnumTag enumTag = (EnumTag) em.getDaoTag();
//    tableEnumConstants.put(em.getDaoTag().getJavaClassName(), enumTag.getTableConstants());
//  }
//
//  private void validateDAONamesAndMethods(final HotRodConfigTag config)
//      throws DuplicateDAOClassException, DuplicateDAOClassMethodException {
//
//    DAONamespace ns = new DAONamespace();
//
//    try {
//      for (TableDataSetMetadata t : this.md.getTables()) {
//        ns.registerDAOTag(t.getDaoTag(), "table", t.getId().getCanonicalSQLName());
//      }
//
//      for (TableDataSetMetadata v : this.md.getViews()) {
//        ns.registerDAOTag(v.getDaoTag(), "view", v.getId().getCanonicalSQLName());
//      }
//
//      for (ExecutorTag c : config.getFacetExecutors()) {
//        ns.registerDAOTag(c, "dao", c.getJavaClassName());
//      }
//    } catch (DuplicateDAOClassException e) {
//      // e.printStackTrace();
//      throw e;
//    } catch (DuplicateDAOClassMethodException e) {
//      // e.printStackTrace();
//      throw e;
//    }
//
//  }

  @Override
  public void prepareGeneration() throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
    log.debug("prepare");

    // Load and validate the configuration file

    this.myBatisConfig = new MyBatisConfiguration(this.config);
    this.myBatisSpringTag = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();
    this.layout = new DataSetLayout(this.config);

    // Reset the generated object counters

    this.config.resetTreeGeneratables();

    // Add tables

    for (TableDataSetMetadata tm : this.md.getTables()) {
      log.debug("tm=" + tm.getId().getCanonicalSQLName());
      addDaosAndMapper(tm, DAOType.TABLE);
      if (tm.getAutoGeneratedColumnMetadata() != null) {
        SourceLocation loc = tm.getDaoTag().getSourceLocation();
        throw new ControlledException("Invalid configuration file '" + loc.getFile().getPath() + "' (line "
            + loc.getLineNumber() + ", col " + loc.getColumnNumber()
            + "): The tag <auto-generated-column> is not supported by the MyBatis generator. "
            + "Use <column> tags with a 'sequence' attribute instead.");
      }
      for (SelectMethodMetadata sm : tm.getSelectsMetadata()) {
        addSelectVOs(sm);
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
      addDaosAndMapper(vm, DAOType.VIEW);
      for (SelectMethodMetadata sm : vm.getSelectsMetadata()) {
        addSelectVOs(sm);
      }
    }

    // Add enums

    for (EnumDataSetMetadata em : this.md.getEnums()) {
      this.enumClasses.put(em,
          new EnumClass(em, new DataSetLayout(this.config), this.myBatisSpringTag.getDaos(), this));
    }

    // Add executors

    for (ExecutorDAOMetadata dm : this.md.getExecutors()) {
      addDaosAndMapper(dm, DAOType.EXECUTOR);
      for (SelectMethodMetadata sm : dm.getSelectsMetadata()) {
        addSelectVOs(sm);
      }
    }

    // Prepare MyBatis Configuration File list

    if (this.myBatisSpringTag.getTemplate() != null) {
      for (Mapper mapper : this.mappers.values()) {
        String sourceFile = mapper.getRuntimeSourceFileName();
        this.myBatisConfig.addFacetSourceFile(sourceFile);
      }

      for (String sourceFile : getAllMappersSourceFileNames()) {
        this.myBatisConfig.addAnySourceFile(sourceFile);
      }
    }

//    // AvailableFKs
//
//    this.availableFKs = new AvailableFKs(this.config,
//        this.tables.stream().map(t -> t.getImportedFKs()).flatMap(l -> l.stream()).collect(Collectors.toList()));

  }

  private void addDaosAndMapper(final DataSetMetadata metadata, final DAOType type) throws ControlledException {

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
        throw new ControlledException(
            "Could not find table tag for table '" + metadata.getId().getCanonicalSQLName() + "'.");
      }
      layout = new DataSetLayout(this.config, ttag);

      abstractVO = new ObjectAbstractVO(metadata, layout, this, DAOType.TABLE, myBatisTag);
      this.tableAbstractVOs.add(abstractVO);
      vo = new ObjectVO(metadata, layout, this, abstractVO, myBatisTag);
      mapper = new Mapper(ttag, metadata, layout, this, type, this.adapter, vo, this.entityDAORegistry);
      dao = new ObjectDAO(ttag, metadata, layout, this, type, myBatisTag, this.adapter, abstractVO, vo, mapper);
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
        throw new ControlledException(
            "Could not find view tag for table '" + metadata.getId().getCanonicalSQLName() + "'.");
      }
      layout = new DataSetLayout(this.config);

      abstractVO = new ObjectAbstractVO(metadata, layout, this, DAOType.VIEW, myBatisTag);
      vo = new ObjectVO(metadata, layout, this, abstractVO, myBatisTag);
      mapper = new Mapper(vtag, metadata, layout, this, type, this.adapter, vo, this.entityDAORegistry);
      dao = new ObjectDAO(vtag, metadata, layout, this, type, myBatisTag, this.adapter, abstractVO, vo, mapper);
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

  private void addSelectVOs(final SelectMethodMetadata sm) throws ControlledException {

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

    // MyBatis Main configuration file

    if (this.myBatisSpringTag.getTemplate() != null) {
      this.myBatisConfig.generate(fileGenerator);
    }

    // MyBatis cursor implementation

    // this.mybatisCursor.generate();

    // compute tree generation status

    this.config.promoteTreeToGenerated();

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
//
//  @Override
//  public TableDataSetMetadata findTableMetadata(final ObjectId id) {
//    throw new UnsupportedOperationException("findTableMetadata() is not supported by generators");
//  }
//
//  @Override
//  public JdbcTable findJdbcTable(final String name) {
//    throw new UnsupportedOperationException("findJdbcTable() is not supported by generators");
//  }
//
//  @Override
//  public JdbcTable findJdbcView(final String name) {
//    throw new UnsupportedOperationException("findJdbcView() is not supported by generators");
//  }
//
//  @Override
//  public JdbcColumn findJdbcColumn(final JdbcTable t, final String name) {
//    throw new UnsupportedOperationException("findJdbcColumn() is not supported by generators");
//  }
//
//  @Override
//  public TableDataSetMetadata findViewMetadata(final ObjectId id) {
//    throw new UnsupportedOperationException("findViewMetadata() is not supported by generators");
//  }

  @Override
  public VORegistry getVORegistry() {
    throw new UnsupportedOperationException("getVORegistry() is not supported by generators");
  }

  // Getters

//  @Override
//  public DatabaseAdapter getAdapter() {
//    throw new UnsupportedOperationException("getAdapter() is not supported by generators");
//  }

  @Override
  public HotRodConfigTag getConfig() {
    throw new UnsupportedOperationException("getConfig() is not supported by generators");
  }

//  @Override
//  public JdbcDatabase getJdbcDatabase() {
//    throw new UnsupportedOperationException("getJdbcDatabase() is not supported by generators");
//  }

//  @Override
//  public DatabaseLocation getLoc() {
//    throw new UnsupportedOperationException("getLoc() is not supported by generators");
//  }

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
        display("Table " + t.getId().getCanonicalSQLName() + " included.");
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
        display("View " + v.getId().getCanonicalSQLName() + " included.");
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
        display("Enum " + e.getJdbcName() + " included.");
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

}
