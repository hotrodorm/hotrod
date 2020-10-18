package org.hotrod.generator.mybatisspring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.AbstractEntityDAOTag;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.Constants;
import org.hotrod.config.DaosTag;
import org.hotrod.config.DisplayMode;
import org.hotrod.config.EnumTag;
import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.ExecutorTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisSpringTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectClassTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.DAONamespace;
import org.hotrod.generator.DAONamespace.DuplicateDAOClassException;
import org.hotrod.generator.DAONamespace.DuplicateDAOClassMethodException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.Generator;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.generator.SelectMetadataCache;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.generator.mybatis.SelectAbstractVO;
import org.hotrod.generator.mybatis.SelectVO;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.DataSetMetadataFactory;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.ExecutorDAOMetadata;
import org.hotrod.metadata.ForeignKeyMetadata;
import org.hotrod.metadata.SelectDataSetMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodMetadata.SelectMethodReturnType;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.metadata.VORegistry;
import org.hotrod.metadata.VORegistry.EntityVOClass;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.LocalFileGenerator;
import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.ObjectId;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.connectors.DatabaseConnectorFactory.UnsupportedDatabaseException;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.DatabaseObject;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase.DatabaseConnectionVersion;
import org.nocrala.tools.database.tartarus.core.JdbcTable;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.DatabaseObjectNotFoundException;
import org.nocrala.tools.database.tartarus.exception.DifferentCatalogSchemaException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.database.tartarus.exception.ReaderException;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;

public class MyBatisSpringGenerator implements Generator, LiveGenerator {

  private static final Logger log = LogManager.getLogger(MyBatisSpringGenerator.class);

  // ===

  private static final Logger logm = LogManager.getLogger("hotrod-metadata-retrieval");

  protected DatabaseLocation dloc;
  protected HotRodConfigTag config;
  protected DisplayMode displayMode;

  protected DatabaseAdapter adapter = null;
  protected Feedback feedback;
  protected JdbcDatabase db = null;

  protected CachedMetadata cachedMetadata = null;

  protected LinkedHashSet<TableDataSetMetadata> tables = null;
  protected LinkedHashSet<TableDataSetMetadata> views = null;
  protected LinkedHashSet<EnumDataSetMetadata> enums = null;
  protected LinkedHashSet<SelectDataSetMetadata> selects = null; // obsolete
  protected LinkedHashSet<ExecutorDAOMetadata> executors = null;

  private VORegistry voRegistry = new VORegistry();

  private Long lastLog = null;

  // ===

  private MyBatisSpringTag myBatisSpringTag;
  private DataSetLayout layout;

  private LinkedHashMap<DataSetMetadata, ObjectAbstractVO> abstractVos = new LinkedHashMap<DataSetMetadata, ObjectAbstractVO>();
  private LinkedHashMap<DataSetMetadata, ObjectVO> vos = new LinkedHashMap<DataSetMetadata, ObjectVO>();
  private LinkedHashMap<DataSetMetadata, ObjectDAO> daos = new LinkedHashMap<DataSetMetadata, ObjectDAO>();
  private LinkedHashMap<DataSetMetadata, Mapper> mappers = new LinkedHashMap<DataSetMetadata, Mapper>();
  private LinkedHashMap<EnumDataSetMetadata, EnumClass> enumClasses = new LinkedHashMap<EnumDataSetMetadata, EnumClass>();
  private List<ObjectAbstractVO> tableAbstractVOs = new ArrayList<ObjectAbstractVO>();
  private MyBatisConfiguration myBatisConfig;
  private MyBatisCursorImpl mybatisCursor;
  private AvailableFKs availableFKs;

  private EntityDAORegistry entityDAORegistry = new EntityDAORegistry();

//  public MyBatisSpringGenerator(final CachedMetadata cachedMetadata, final DatabaseLocation loc,
//      final HotRodConfigTag config, final DisplayMode displayMode, final boolean incrementalMode,
//      final DatabaseAdapter adapter, final Feedback feedback)
//      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
//    super(cachedMetadata, loc, config, displayMode, incrementalMode, adapter, feedback);
//  }

  public MyBatisSpringGenerator(final CachedMetadata cachedMetadata, final DatabaseLocation dloc,
      final HotRodConfigTag config, final DisplayMode displayMode, final boolean incrementalMode,
      final DatabaseAdapter adapter, final Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {

    log.debug(">>> HG 1 cachedMetadata=" + cachedMetadata);
    log.debug(">>> HG 1 cachedMetadata.getSelectMetadataCache()=" + cachedMetadata.getSelectMetadataCache());

    this.dloc = dloc;
    this.config = config;
    this.displayMode = displayMode;
    this.cachedMetadata = cachedMetadata;
    this.adapter = adapter;
    this.feedback = feedback;

    if (!incrementalMode) {
      config.markGenerateTree();
    }

    logm("Starting core generator.");

    display("Database URL: " + dloc.getUrl());

    Connection conn = null;
    boolean retrieveSelectMetadata = false;

    try {

      // Get Connection

      logm("Opening connection...");
      conn = this.dloc.getConnection();
      logm("Connection open.");

      // Database Version

      DatabaseConnectionVersion cv;

      try {
        logm("Getting initial metadata.");
        cv = new DatabaseConnectionVersion(conn.getMetaData());
        logm("Metadata retrieval complete.");

      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve database metadata.", e);
      }

      display("Database Name: " + cv.renderDatabaseName());
      display("JDBC Driver: " + cv.renderJDBCDriverName() + " - implements JDBC Specification "
          + cv.renderJDBCSpecification());

      // Database Adapter

      display("");
      if (this.adapter.supportsCatalog()) {
        display("Default Database Catalog: " + (dloc.getDefaultCatalog() == null ? "" : dloc.getDefaultCatalog()));
      }
      if (this.adapter.supportsSchema()) {
        display("Default Database Schema: " + (dloc.getDefaultSchema() == null ? "" : dloc.getDefaultSchema()));
      }
      display("");

      // Decide about using cached or fresh database objects

      JdbcDatabase cachedDatabase = incrementalMode ? cachedMetadata.getCachedDatabase() : null;

      boolean retrieveFreshDatabaseObjects = false;
      if (!incrementalMode) {
        retrieveFreshDatabaseObjects = true;
      } else {
        if (cachedDatabase == null) {
          retrieveFreshDatabaseObjects = true;
        } else {
          for (AbstractConfigurationTag tag : config.getTagsToGenerate()) {
            try {
              @SuppressWarnings("unused")
              AbstractEntityDAOTag entity = (AbstractEntityDAOTag) tag;
              // An entity was modified -- refresh metadata
              retrieveFreshDatabaseObjects = true;
            } catch (ClassCastException e) {
              // Not an entity DAO -- ignore.
            }
          }
        }
      }

      // check if an entity was removed from the configuration file

      if (!retrieveFreshDatabaseObjects) {
        for (JdbcTable t : cachedDatabase.getTables()) {
          if (config.getTableTag(t) == null && config.getEnumTag(t) == null && config.getViewTag(t) == null) {
            retrieveFreshDatabaseObjects = true;
          }
        }
      }

      log.debug("retrieveFreshDatabaseObjects=" + retrieveFreshDatabaseObjects);

      Set<DatabaseObject> tables = new HashSet<DatabaseObject>();
      for (TableTag t : this.config.getFacetTables()) {
        tables.add(t.getDatabaseObjectId());
      }
      for (EnumTag e : this.config.getFacetEnums()) {
        tables.add(e.getDatabaseObjectId());
      }

      Set<DatabaseObject> views = new HashSet<DatabaseObject>();
      for (ViewTag v : this.config.getFacetViews()) {
        views.add(v.getDatabaseObjectId());
      }

      log.debug("database retrieval (if needed).");

      try {

        if (!retrieveFreshDatabaseObjects) {
          try {
            log.debug("Will activate now.");
            cachedDatabase.activate(this.dloc, true);
            this.db = cachedDatabase;
          } catch (DifferentCatalogSchemaException e) {
            // catalog or schema changed -- retrieve the database again.
            log.debug("Retrieve 1.");
            this.db = new JdbcDatabase(this.dloc, tables, views);
            log.debug("Retrieve 1 - done.");
          }
        } else {
          // Retrieve database objects
          log.debug("Retrieve 2.");
          this.db = new JdbcDatabase(this.dloc, tables, views);
        }

      } catch (ReaderException e) {
        throw new ControlledException(e.getMessage());
      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve database metadata.", e);
      } catch (CatalogNotSupportedException e) {
        throw new ControlledException("This database does not support catalogs through the JDBC driver. "
            + "Please specify an empty value for the default catalog property instead of '" + dloc.getDefaultCatalog()
            + "'.");
      } catch (InvalidCatalogException e) {
        StringBuilder sb = new StringBuilder();
        if (dloc.getDefaultCatalog() == null) {
          sb.append("Please specify a default catalog.\n\n");
        } else {
          sb.append(
              "The specified default catalog '" + dloc.getDefaultCatalog() + "' does not exist in this database.\n\n");
        }
        sb.append("The available catalogs are:\n");
        for (String c : e.getExistingCatalogs()) {
          sb.append("  " + c + "\n");
        }
        throw new ControlledException(sb.toString());
      } catch (SchemaNotSupportedException e) {
        throw new ControlledException("This database does not support schemas through the JDBC driver. "
            + "Please specify an empty value for the default schema property instead of '" + dloc.getDefaultCatalog()
            + "'.");
      } catch (InvalidSchemaException e) {
        StringBuilder sb = new StringBuilder();
        if (dloc.getDefaultSchema() == null) {
          sb.append("Please specify a default schema.\n\n");
        } else {
          sb.append(
              "The specified default schema '" + dloc.getDefaultSchema() + "' does not exist in this database.\n\n");
        }
        sb.append("The available schemas are:\n");
        for (String s : e.getExistingSchemas()) {
          sb.append("  " + s + "\n");
        }
        throw new ControlledException(sb.toString());
      } catch (UnsupportedDatabaseException e) {
        throw new ControlledException("This database is not currently supported by " + Constants.TOOL_NAME);
      } catch (DatabaseObjectNotFoundException e) {
        throw new ControlledException(
            "Database object not found. Please check this is the correct database, catalog, and schema: "
                + e.getMessage());
      } catch (RuntimeException e) {
        e.printStackTrace();
        throw new UncontrolledException("Could not retrieve database metadata using JDBC URL " + dloc.getUrl(), e);
      }

      log.debug("database loaded.");
      config.logGenerateMark("VALIDATE", ':');

      // Validate names

      logm("Validate Names.");

      Set<String> sqlNames = new HashSet<String>();

      // Prepare tables meta data

      logm("Prepare tables metadata.");

      DataSetLayout layout = new DataSetLayout(this.config);
      DaosTag daosTag = this.config.getGenerators().getSelectedGeneratorTag().getDaos();

      this.tables = new LinkedHashSet<TableDataSetMetadata>();
      for (JdbcTable t : this.db.getTables()) {
        try {
          log.debug("t.getName()=" + t.getName());
          TableDataSetMetadata tm = DataSetMetadataFactory.getMetadata(t, this.adapter, config, layout, cachedMetadata);
          log.debug("*** tm=" + tm);
          validateIdentifier(sqlNames, "table", t.getName(), tm.getId());
          this.tables.add(tm);

          ClassPackage fragmentPackage = tm.getFragmentConfig() != null
              && tm.getFragmentConfig().getFragmentPackage() != null ? tm.getFragmentConfig().getFragmentPackage()
                  : null;
          ClassPackage classPackage = layout.getDAOPackage(fragmentPackage);
          String voName = daosTag.generateVOName(tm.getId());
          EntityVOClass vo = new EntityVOClass(tm, classPackage, voName, tm.getColumns(), tm.getDaoTag());
          this.voRegistry.addVO(vo);

        } catch (UnresolvableDataTypeException e) {
          ColumnMetadata m = e.getColumnMetadata();

          String typeName = JdbcTypes.codeToName(m.getDataType());

          throw new ControlledException(
              "Unrecognized column data type (reported as '" + m.getTypeName() + "', JDBC type " + m.getDataType() + " "
                  + (typeName == null ? "(non-standard JDBC type)" : "'" + typeName + "'") + ") on column '"
                  + m.getColumnName() + "' of table/view/select '" + m.getTableName()
                  + (e.getMessage() == null ? "" : ": " + e.getMessage()));

        } catch (VOAlreadyExistsException e) {
          throw new ControlledException("Duplicate table with name '" + t.getName() + "'.");
        } catch (StructuredVOAlreadyExistsException e) {
          throw new ControlledException("Duplicate table with name '" + t.getName() + "'.");

        }
      }

      // Link table meta data by foreign keys

      for (TableDataSetMetadata tm : this.tables) {
        tm.linkReferencedTableMetadata(this.tables);
      }

      // Validate there are no 1-many FK relationships with enums on the many
      // side.

      for (TableDataSetMetadata tm : this.tables) {
        for (ForeignKeyMetadata efk : tm.getExportedFKs()) {
          try {
            EnumDataSetMetadata em = (EnumDataSetMetadata) efk.getRemote().getTableMetadata();
            // it's an enum! An enum cannot be used as the children table
            throw new ControlledException(em.getDaoTag().getSourceLocation(), "Cannot specify the enum '"
                + em.getId().getRenderedSQLName() + "' on the 'many' side of a 1-to-many relationship.");

          } catch (ClassCastException e) {
            // It's a table, not an enum - it's valid.
          }
        }

      }

      config.logGenerateMark("AFTER LINKING", ':');

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

      logm("Prepare enums metadata.");

      this.enums = new LinkedHashSet<EnumDataSetMetadata>();

      for (Iterator<TableDataSetMetadata> it = this.tables.iterator(); it.hasNext();) {
        TableDataSetMetadata tm = it.next();
        try {
          EnumDataSetMetadata em = (EnumDataSetMetadata) tm;
          // It's an enum - move it to the enum set.
          this.enums.add(em);
          it.remove();
        } catch (ClassCastException e) {
          // Not an enum - nothing to do.
        }
      }

      // Link EnumMetadata to ColumnMetadata

      for (TableDataSetMetadata ds : this.tables) {
        ds.linkEnumMetadata(this.enums);
      }

      // Check tables and enums duplicate names

      Set<String> tablesAndEnumsCanonicalNames = new HashSet<String>();

      for (TableTag tt : config.getFacetTables()) {
        String canonicalName = tt.getId().getCanonicalSQLName();
        if (tablesAndEnumsCanonicalNames.contains(canonicalName)) {
          throw new ControlledException(tt.getSourceLocation(), "Duplicate database <table> name '" + canonicalName
              + "'. This table is already defined in the configuration file(s).");
        }
        tablesAndEnumsCanonicalNames.add(canonicalName);
      }

      for (EnumTag et : config.getFacetEnums()) {
        String canonicalName = et.getId().getCanonicalSQLName();
        if (tablesAndEnumsCanonicalNames.contains(canonicalName)) {
          throw new ControlledException(et.getSourceLocation(), "Duplicate database <enum> name '" + canonicalName
              + "'. This enum is already defined in the configuration file(s), as a <table> or <enum>.");
        }
        tablesAndEnumsCanonicalNames.add(canonicalName);
      }

      Set<String> viewsCanonicalNames = new HashSet<String>();

      for (ViewTag vt : config.getFacetViews()) {
        String canonicalName = vt.getId().getCanonicalSQLName();
        if (viewsCanonicalNames.contains(canonicalName)) {
          throw new ControlledException(vt.getSourceLocation(), "Duplicate database <view> name '" + canonicalName
              + "'. This enum is already defined in the configuration file(s).");
        }
        viewsCanonicalNames.add(canonicalName);
      }

      // Prepare views meta data

      logm("Prepare views metadata.");

      this.views = new LinkedHashSet<TableDataSetMetadata>();
      TableDataSetMetadata vmd = null;
      for (JdbcTable v : this.db.getViews()) {
        try {

          vmd = DataSetMetadataFactory.getMetadata(v, this.adapter, config, layout, cachedMetadata);

          validateIdentifier(sqlNames, "view", v.getName(), vmd.getId());
          this.views.add(vmd);

          ClassPackage fragmentPackage = vmd.getFragmentConfig() != null
              && vmd.getFragmentConfig().getFragmentPackage() != null ? vmd.getFragmentConfig().getFragmentPackage()
                  : null;
          ClassPackage classPackage = layout.getDAOPackage(fragmentPackage);
          String voName = daosTag.generateVOName(vmd.getId());
          EntityVOClass vo = new EntityVOClass(vmd, classPackage, voName, vmd.getColumns(), vmd.getDaoTag());
          this.voRegistry.addVO(vo);

        } catch (UnresolvableDataTypeException e) {
          throw new ControlledException(e.getMessage());

        } catch (VOAlreadyExistsException e) {
          throw new ControlledException(vmd.getDaoTag().getSourceLocation(),
              "Duplicate view with name '" + v.getName() + "'.");
        } catch (StructuredVOAlreadyExistsException e) {
          throw new ControlledException(vmd.getDaoTag().getSourceLocation(),
              "Duplicate view with name '" + v.getName() + "'.");

        }
      }

      // Prepare executor DAOs meta data

      SelectMetadataCache selectMetadataCache = cachedMetadata.getSelectMetadataCache();
      log.debug(">>> 1 selectMetadataCache=" + selectMetadataCache);

      this.executors = new LinkedHashSet<ExecutorDAOMetadata>();
      for (ExecutorTag tag : config.getFacetExecutors()) {
        ExecutorDAOMetadata dm;
        try {
          dm = new ExecutorDAOMetadata(tag, this.adapter, config, tag.getFragmentConfig(), selectMetadataCache);
        } catch (InvalidIdentifierException e) {
          throw new ControlledException(tag.getSourceLocation(),
              "Invalid DAO with namename '" + tag.getJavaClassName() + "': " + e.getMessage());
        }
        this.executors.add(dm);
      }

      // Validate against the database

      // TODO: make sure the cache includes enum values from table rows.
      // if (retrieveFreshDatabaseObjects) {
      try {
        this.config.validateAgainstDatabase(this, conn, adapter);
      } catch (InvalidConfigurationFileException e) {
        throw new ControlledException(e.getTag().getSourceLocation(), e.getMessage());
      }
      // }

      // Prepare <select> methods meta data - phase 1

      for (TableDataSetMetadata tm : this.tables) {
        boolean retrieving;
        try {
          retrieving = tm.gatherSelectsMetadataPhase1(this, conn, layout);
        } catch (InvalidConfigurationFileException e) {
          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
        }
        if (retrieving) {
          retrieveSelectMetadata = true;
        }
      }

      for (TableDataSetMetadata vm : this.views) {
        boolean retrieving;
        try {
          retrieving = vm.gatherSelectsMetadataPhase1(this, conn, layout);
        } catch (InvalidConfigurationFileException e) {
          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
        }
        if (retrieving) {
          retrieveSelectMetadata = true;
        }
      }

      for (TableDataSetMetadata em : this.enums) {
        boolean retrieving;
        try {
          retrieving = em.gatherSelectsMetadataPhase1(this, conn, layout);
        } catch (InvalidConfigurationFileException e) {
          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
        }
        if (retrieving) {
          retrieveSelectMetadata = true;
        }
      }

      for (ExecutorDAOMetadata dm : this.executors) {
        boolean retrieving;
        try {
          retrieving = dm.gatherSelectsMetadataPhase1(this, conn, layout);
        } catch (InvalidConfigurationFileException e) {
          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
        }
        if (retrieving) {
          retrieveSelectMetadata = true;
        }
      }

      // Prepare <select> DAOs meta data - phase 1

      logm("Prepare selects metadata - phase 1.");

      if (!this.config.getFacetSelects().isEmpty()) {
        retrieveSelectMetadata = true;
      }

      this.selects = new LinkedHashSet<SelectDataSetMetadata>();
      if (!this.config.getFacetSelects().isEmpty()) {

        SelectClassTag current = null;
        SelectDataSetMetadata sm = null;

        for (SelectClassTag s : this.config.getFacetSelects()) {
          log.debug("::: select '" + s.getJavaClassName() + "': " + s.renderSQLSentence(new ParameterRenderer() {
            @Override
            public String render(SQLParameter parameter) {
              return "?";
            }
          }));
          current = s;
          String tempViewName = this.config.getGenerators().getSelectedGeneratorTag().getSelectGeneration()
              .getNextTempViewName();
          try {
            sm = new SelectDataSetMetadata(this.db, this.adapter, this.dloc, s, tempViewName, this.config);
          } catch (InvalidIdentifierException e) {
            throw new ControlledException(s.getSourceLocation(),
                "Invalid identifier for <" + new SelectClassTag().getTagName() + "> query '"
                    + current.getJavaClassName() + "': " + e.getMessage());
          }
          this.selects.add(sm);
          log.debug("prepareView() will be called...");
          try {
            sm.prepareViews(conn);
          } catch (SQLException e) {
            throw new ControlledException(s.getSourceLocation(),
                "Failed to retrieve metadata for <" + new SelectClassTag().getTagName() + "> query '"
                    + current.getJavaClassName() + "' while creating a temporary SQL view for it.\n" + "[ "
                    + e.getMessage() + " ]\n" + "* Do all resulting columns have different and valid names?\n"
                    + "* Is the trimmed create view SQL code below valid?\n" + "--- begin SQL ---\n"
                    + sm.getCreateView() + "\n--- end SQL ---");
          }
          log.debug("prepareView() complete.");
        }

      }

    } catch (SQLException e) {
      throw new UncontrolledException("Could not retrieve database metadata.", e);

    } finally {
      if (conn != null) {
        try {
          logm("Closing connection...");
          conn.close();
          logm("Connection closed.");
        } catch (SQLException e) {
          throw new UncontrolledException("Could not retrieve database metadata.", e);
        }
      }
    }

    // Prepare <select> DAOs meta data - phase 2

    SelectMetadataCache selectMetadataCache = new SelectMetadataCache();
    Map<String, List<EnumConstant>> tableEnumConstants = new HashMap<String, List<EnumConstant>>();

    log.debug("/////////////// retrieveSelectMetadata=" + retrieveSelectMetadata);

    if (retrieveSelectMetadata) {

      logm("Prepare selects metadata - phase 2.");

      SelectDataSetMetadata currDs = null;

      Connection conn2 = null;
      try {

        log.debug("ret 2");
        logm("Opening connection (selects)...");
        conn2 = this.dloc.getConnection();
        logm("Connection open (selects).");
        log.debug("ret 3");

        for (TableDataSetMetadata tm : this.tables) {
          tm.gatherSelectsMetadataPhase2(conn2, this.voRegistry);
          addSelectsMetaData(tm.getDaoTag().getJavaClassName(), tm.getSelectsMetadata(), selectMetadataCache);
        }

        for (TableDataSetMetadata vm : this.views) {
          vm.gatherSelectsMetadataPhase2(conn2, this.voRegistry);
          addSelectsMetaData(vm.getDaoTag().getJavaClassName(), vm.getSelectsMetadata(), selectMetadataCache);
        }

        for (TableDataSetMetadata em : this.enums) {
          em.gatherSelectsMetadataPhase2(conn2, this.voRegistry);
          addSelectsMetaData(em.getDaoTag().getJavaClassName(), em.getSelectsMetadata(), selectMetadataCache);
          addTableEnumConstants(em, tableEnumConstants);
        }

        for (ExecutorDAOMetadata xm : this.executors) {
          xm.gatherSelectsMetadataPhase2(conn2, this.voRegistry);
          addSelectsMetaData(xm.getDaoTag().getJavaClassName(), xm.getSelectsMetadata(), selectMetadataCache);
        }

        for (SelectDataSetMetadata ds : this.selects) {
          currDs = ds;
          ds.retrieveColumnsMetadata(conn2);

          for (ColumnTag ct : ds.getSelectTag().getColumns()) {
            ColumnMetadata cm = this.findColumnMetadata(ct.getName(), ds);
            log.debug("cm=" + (cm == null ? "null" : cm.toString()));
            if (cm == null) {
              throw new ControlledException("Could not find database column '" + ct.getName()
                  + "' as specified in <column> tag, on the <select> tag '" + ds.getSelectTag().getJavaClassName()
                  + "'. Make sure the resulting SQL select statement return a column with this name.");
            }
          }

        }

      } catch (SQLException e) {
        throw new UncontrolledException("Failed to retrieve metadata for <" + new SelectClassTag().getTagName()
            + "> query with name '" + currDs.getId().getCanonicalSQLName() + "'.", e);
      } catch (UnresolvableDataTypeException e) {
        throw new ControlledException(e.getMessage());
      } catch (InvalidConfigurationFileException e) {
        throw new ControlledException(e.getTag().getSourceLocation(), e.getMessage());
      } finally {
        if (conn2 != null) {
          try {
            logm("Closing connection (selects)...");
            conn2.close();
            logm("Connection closed (selects).");
          } catch (SQLException e) {
            throw new UncontrolledException("Could not retrieve database metadata.", e);
          }
        }
      }

    }

    // Validate DAO names and methods

    try {

      validateDAONamesAndMethods(config);

    } catch (DuplicateDAOClassException e) {
      throw new ControlledException(
          "Duplicate DAO class name '" + e.getClassName() + "' on " + e.getType() + " '" + e.getName()
              + "'. There's another " + e.getType() + " with the same class name (either specified or computed).");
    } catch (DuplicateDAOClassMethodException e) {
      throw new ControlledException("Duplicate method name '" + e.getMethodName() + "' (on DAO class '"
          + e.getClassName() + "') for the " + e.getType() + " '" + e.getName() + "'. Please consider method names "
          + "may have been specified (as in <update> tags) "
          + "or may have been computed based on the SQL names (as in the <sequence> tag).");
    }

    // Assemble cached metadata for non-incremental generation

    // TODO: refresh the cache. This strategy should be revisited.
    if (!incrementalMode) {
    }
    this.cachedMetadata.setConfig(config);
    this.cachedMetadata.setSelectMetadataCache(selectMetadataCache);
    this.cachedMetadata.setEnumConstants(tableEnumConstants);
    this.cachedMetadata.setCachedDatabase(this.db);

    // Display the retrieved meta data

    logm("Metadata initialized.");

    // logSelectMethodMetadata(); // keep for debugging purposes only

  }

  private void addSelectsMetaData(final String daoName, final List<SelectMethodMetadata> selects,
      final SelectMetadataCache selectMetadataCache) {
    for (SelectMethodMetadata sm : selects) {
      selectMetadataCache.put(daoName, sm.getMethod(), sm);
    }
  }

  private void addTableEnumConstants(final TableDataSetMetadata em,
      final Map<String, List<EnumConstant>> tableEnumConstants) {
    if (tableEnumConstants.containsKey(em.getDaoTag().getJavaClassName())) {
      tableEnumConstants.remove(em.getDaoTag().getJavaClassName());
    }
    EnumTag enumTag = (EnumTag) em.getDaoTag();
    tableEnumConstants.put(em.getDaoTag().getJavaClassName(), enumTag.getTableConstants());
  }

  private void validateDAONamesAndMethods(final HotRodConfigTag config)
      throws DuplicateDAOClassException, DuplicateDAOClassMethodException {

    DAONamespace ns = new DAONamespace();

    try {
      for (TableDataSetMetadata t : this.tables) {
        ns.registerDAOTag(t.getDaoTag(), "table", t.getId().getCanonicalSQLName());
      }

      for (TableDataSetMetadata v : this.views) {
        ns.registerDAOTag(v.getDaoTag(), "view", v.getId().getCanonicalSQLName());
      }

      for (SelectClassTag s : config.getFacetSelects()) {
        ns.registerDAOTag(s, "select", s.getJavaClassName());
      }

      for (ExecutorTag c : config.getFacetExecutors()) {
        ns.registerDAOTag(c, "dao", c.getJavaClassName());
      }
    } catch (DuplicateDAOClassException e) {
      // e.printStackTrace();
      throw e;
    } catch (DuplicateDAOClassMethodException e) {
      // e.printStackTrace();
      throw e;
    }

  }

  @Override
  public void prepareGeneration() throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
    log.debug("prepare");

    // Load and validate the configuration file

    this.myBatisConfig = new MyBatisConfiguration(this.config);
    this.myBatisSpringTag = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();
    this.layout = new DataSetLayout(this.config);

    // Reset the generated object counters

    this.config.resetTreeGeneratables();

    // Prepare the cursor implementation

    this.mybatisCursor = new MyBatisCursorImpl(this.layout);

    // Add tables

    for (TableDataSetMetadata tm : this.tables) {
      log.debug("tm=" + tm.getId().getCanonicalSQLName());
      addDaosAndMapper(tm, DAOType.TABLE, mybatisCursor);
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

    for (TableDataSetMetadata vm : this.views) {
      addDaosAndMapper(vm, DAOType.VIEW, mybatisCursor);
      for (SelectMethodMetadata sm : vm.getSelectsMetadata()) {
        addSelectVOs(sm);
      }
    }

    // Add enums

    for (EnumDataSetMetadata em : this.enums) {
      this.enumClasses.put(em,
          new EnumClass(em, new DataSetLayout(this.config), this.myBatisSpringTag.getDaos(), this));
    }

    // First-level select tags are no longer supported

    if (!this.selects.isEmpty()) {
      SourceLocation loc = this.selects.iterator().next().getDaoTag().getSourceLocation();
      throw new ControlledException("Invalid configuration in " + loc.render() + ":\n"
          + "The MyBatis generator does not support first-level <select> tags. "
          + "Include any <select> tag inside another <table>, <view>, or <dao> tag.");
    }

    // Add executors

    for (ExecutorDAOMetadata dm : this.executors) {
      addDaosAndMapper(dm, DAOType.EXECUTOR, mybatisCursor);
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

    // AvailableFKs

    this.availableFKs = new AvailableFKs(this.config,
        this.tables.stream().map(t -> t.getImportedFKs()).flatMap(l -> l.stream()).collect(Collectors.toList()));

  }

  private void addDaosAndMapper(final DataSetMetadata metadata, final DAOType type,
      final MyBatisCursorImpl mybatisCursor) throws ControlledException {

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
      dao = new ObjectDAO(ttag, metadata, layout, this, type, myBatisTag, vo, mapper, mybatisCursor);
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
      dao = new ObjectDAO(vtag, metadata, layout, this, type, myBatisTag, vo, mapper, mybatisCursor);
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
      dao = new ObjectDAO(tag, metadata, layout, this, type, myBatisTag, vo, mapper, mybatisCursor);
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
      log.trace("### soloVO.getName()=" + soloVO.getName() + " abstractVO.getName()=" + abstractVO.getName());
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

    this.mybatisCursor.generate();

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

  // Validation

  private void validateIdentifier(final Set<String> SQLNames, final String objectType, final String sqlName,
      final ObjectId id) throws ControlledException {
    log.debug("id=" + id + " SQLNames=" + SQLNames);
    if (id.getCanonicalSQLName() != null && SQLNames.contains(id.getCanonicalSQLName())) {
      throw new ControlledException("Duplicate database object name '" + id.getCanonicalSQLName() + "' on " + objectType
          + " '" + sqlName + "'. There's another table, view, dao, or select "
          + "whose java-name resolves to the same value (either specified or computed).");
    }
    SQLNames.add(id.getCanonicalSQLName());
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
  public TableDataSetMetadata findTableMetadata(final ObjectId id) {
    for (TableDataSetMetadata tm : this.tables) {
      if (tm.getId().equals(id)) {
        return tm;
      }
    }
    return null;
  }

  @Override
  public JdbcTable findJdbcTable(final String name) {
    for (JdbcTable t : this.db.getTables()) {
      if (this.adapter.isTableIdentifier(t.getName(), name)) {
        return t;
      }
    }
    return null;
  }

  @Override
  public JdbcTable findJdbcView(final String name) {
    for (JdbcTable t : this.db.getViews()) {
      if (this.adapter.isTableIdentifier(t.getName(), name)) {
        return t;
      }
    }
    return null;
  }

  @Override
  public JdbcColumn findJdbcColumn(final JdbcTable t, final String name) {
    for (JdbcColumn c : t.getColumns()) {
      if (this.adapter.isColumnIdentifier(c.getName(), name)) {
        return c;
      }
    }
    return null;
  }

  @Override
  public TableDataSetMetadata findViewMetadata(final ObjectId id) {
    for (TableDataSetMetadata tm : this.views) {
      if (tm.getId().equals(id)) {
        return tm;
      }
    }
    return null;
  }

  private ColumnMetadata findColumnMetadata(final String name, final SelectDataSetMetadata ds) {
    for (ColumnMetadata cm : ds.getColumns()) {
      if (this.adapter.isColumnIdentifier(cm.getColumnName(), name)) {
        return cm;
      }
    }
    return null;
  }

  @Override
  public VORegistry getVORegistry() {
    return voRegistry;
  }

  // Getters

  @Override
  public DatabaseAdapter getAdapter() {
    return this.adapter;
  }

  @Override
  public HotRodConfigTag getConfig() {
    return this.config;
  }

  @Override
  public JdbcDatabase getJdbcDatabase() {
    return this.db;
  }

  @Override
  public DatabaseLocation getLoc() {
    return this.dloc;
  }

}
