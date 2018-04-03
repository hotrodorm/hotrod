package org.hotrod.generator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractEntityDAOTag;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.EnumTag;
import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.ExecutorTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectClassTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.DatabaseAdapterFactory;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnrecognizedDatabaseException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.DAONamespace.DuplicateDAOClassException;
import org.hotrod.generator.DAONamespace.DuplicateDAOClassMethodException;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadataFactory;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.ExecutorDAOMetadata;
import org.hotrod.metadata.ForeignKeyMetadata;
import org.hotrod.metadata.SelectDataSetMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.metadata.StructuredColumnsMetadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.metadata.VORegistry;
import org.hotrod.metadata.VORegistry.EntityVOClass;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.DatabaseObjectFilter;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase.DatabaseConnectionVersion;
import org.nocrala.tools.database.tartarus.core.JdbcTable;
import org.nocrala.tools.database.tartarus.core.JdbcTableFilter;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.database.tartarus.exception.ReaderException;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;

public abstract class HotRodGenerator {

  private static final Logger log = Logger.getLogger(HotRodGenerator.class);

  private static final Logger logm = Logger.getLogger("hotrod-metadata-retrieval");

  protected DatabaseLocation loc;
  protected HotRodConfigTag config;
  protected DisplayMode displayMode;

  protected DatabaseAdapter adapter = null;
  protected JdbcDatabase db = null;

  protected CachedMetadata cachedMetadata = null;

  protected LinkedHashSet<TableDataSetMetadata> tables = null;
  protected LinkedHashSet<TableDataSetMetadata> views = null;
  protected LinkedHashSet<EnumDataSetMetadata> enums = null;
  protected LinkedHashSet<SelectDataSetMetadata> selects = null;
  protected LinkedHashSet<ExecutorDAOMetadata> executors = null;

  private VORegistry voRegistry = new VORegistry();

  private Long lastLog = null;

  public HotRodGenerator(final CachedMetadata cachedMetadata, final DatabaseLocation loc, final HotRodConfigTag config,
      final DisplayMode displayMode, final boolean incrementalMode) throws UncontrolledException, ControlledException {

    log.info(">>> HG 1 cachedMetadata=" + cachedMetadata);
    log.info(">>> HG 1 cachedMetadata.getSelectMetadataCache()=" + cachedMetadata.getSelectMetadataCache());

    this.loc = loc;
    this.config = config;
    this.displayMode = displayMode;
    this.cachedMetadata = cachedMetadata;

    if (!incrementalMode) {
      config.markGenerateSubtree(true);
    }

    logm("Starting core generator.");

    display("Database URL: " + loc.getUrl());

    Connection conn = null;
    boolean retrieveSelectMetadata = false;

    try {

      // Get Connection

      logm("Opening connection...");
      conn = this.loc.getConnection();
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

      try {
        this.adapter = DatabaseAdapterFactory.getAdapter(this.loc, this.config);
        display("HotRod Database Adapter: " + this.adapter.getName());

      } catch (UnrecognizedDatabaseException e) {
        throw new ControlledException(
            "Could not recognize database type at JDBC URL " + loc.getUrl() + " - " + e.getMessage());
      }

      display("Database Catalog: " + (this.adapter.supportsCatalog() ? loc.getDefaultCatalog() : "(not supported)"));
      display("Database Schema: " + (this.adapter.supportsSchema() ? loc.getDefaultSchema() : "(not supported)"));
      display("");

      // Decide about using cached or fresh database objects

      JdbcDatabase cachedDatabase = cachedMetadata.getCachedDatabase();

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

      if (!retrieveFreshDatabaseObjects) {

        this.db = cachedDatabase;

      } else {

        // Retrieve database objects

        try {
          logm("Ready for database objects retrieval.");

          DatabaseObjectFilter filter = new DatabaseObjectFilter(new TableFilter(this.adapter),
              new ViewFilter(this.config, this.adapter));

          // TODO: Use the existing connection instead of opening a new one.
          this.db = new JdbcDatabase(this.loc, true, filter);

          logm("After retrieval 1.");

        } catch (ReaderException e) {
          throw new ControlledException(e.getMessage());
        } catch (SQLException e) {
          throw new UncontrolledException("Could not retrieve database metadata.", e);
        } catch (CatalogNotSupportedException e) {
          throw new ControlledException(
              "A catalog name was specified for the database with the value '" + loc.getDefaultCatalog() + "', "
                  + "but this database does not support catalogs through the JDBC driver. "
                  + "Please specify it with an empty value.");
        } catch (InvalidCatalogException e) {
          StringBuilder sb = new StringBuilder();
          if (loc.getDefaultCatalog() == null) {
            sb.append(
                "This database requires a catalog name. Please specify in " + Constants.TOOL_NAME + "'s Ant task.\n\n");
          } else {
            sb.append(
                "The specified catalog name '" + loc.getDefaultCatalog() + "' does not exist in this database.\n\n");
          }
          sb.append("The available catalogs are:\n");
          for (String c : e.getExistingCatalogs()) {
            sb.append("  " + c + "\n");
          }
          throw new ControlledException(sb.toString());
        } catch (SchemaNotSupportedException e) {
          throw new ControlledException("A schema name was specified for the database with the value '"
              + loc.getDefaultSchema() + "', " + "but this database does not support schemas through the JDBC driver. "
              + "Please specify it with an empty value.");
        } catch (InvalidSchemaException e) {
          StringBuilder sb = new StringBuilder();
          if (loc.getDefaultSchema() == null) {
            sb.append(
                "This database requires a schema name. Please specify in " + Constants.TOOL_NAME + "'s Ant task.\n\n");
          } else {
            sb.append(
                "The specified schema name '" + loc.getDefaultSchema() + "' does not exist in this database.\n\n");
          }
          sb.append("The available schemas are:\n");
          for (String s : e.getExistingSchemas()) {
            sb.append("  " + s + "\n");
          }
          throw new ControlledException(sb.toString());
        } catch (Exception e) {
          throw new UncontrolledException("Could not retrieve database metadata using JDBC URL " + loc.getUrl(), e);
        }

      }

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
          validateIdentifier(sqlNames, "table", t.getName(), tm.getIdentifier());
          this.tables.add(tm);

          ClassPackage fragmentPackage = tm.getFragmentConfig() != null
              && tm.getFragmentConfig().getFragmentPackage() != null ? tm.getFragmentConfig().getFragmentPackage()
                  : null;
          ClassPackage classPackage = layout.getDAOPackage(fragmentPackage);
          String voName = daosTag.generateVOName(tm.getIdentifier());
          EntityVOClass vo = new EntityVOClass(tm, classPackage, voName, tm.getColumns(),
              tm.getDaoTag().getSourceLocation());
          this.voRegistry.addVO(vo);

        } catch (UnresolvableDataTypeException e) {
          ColumnMetadata m = e.getColumnMetadata();

          String typeName = JdbcTypes.codeToName(m.getDataType());

          throw new ControlledException(
              "Unrecognized column data type (reported as '" + m.getTypeName() + "', JDBC type " + m.getDataType() + " "
                  + (typeName == null ? "(non-standard JDBC type)" : "'" + typeName + "'") + ") on column '"
                  + m.getColumnName() + "' of table/view/select '" + m.getTableName() + "'.");

        } catch (VOAlreadyExistsException e) {
          throw new ControlledException("Duplicate table with name '" + t.getName() + "'.");
        } catch (StructuredVOAlreadyExistsException e) {
          throw new ControlledException("Duplicate table with name '" + t.getName() + "'.");

        }
      }

      // Link meta data by foreign keys

      for (TableDataSetMetadata tm : this.tables) {
        tm.linkReferencedTableMetadata(this.tables);
      }

      // Propagate generation to related db changes (if incremental generation)

      if (incrementalMode) {
        Set<TableDataSetMetadata> alreadyWalked = new HashSet<TableDataSetMetadata>();
        for (TableDataSetMetadata tm : this.tables) {
          propagateGeneration(tm, alreadyWalked);
        }
      }

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

      for (TableTag tt : config.getTables()) {
        String canonicalName = this.adapter.canonizeName(tt.getName(), false);
        if (tablesAndEnumsCanonicalNames.contains(canonicalName)) {
          throw new ControlledException(tt.getSourceLocation(), "Duplicate database <table> name '" + canonicalName
              + "'. This table is already defined in the configuration file(s).");
        }
        tablesAndEnumsCanonicalNames.add(canonicalName);
      }

      for (EnumTag et : config.getEnums()) {
        String canonicalName = this.adapter.canonizeName(et.getName(), false);
        if (tablesAndEnumsCanonicalNames.contains(canonicalName)) {
          throw new ControlledException(et.getSourceLocation(), "Duplicate database <enum> name '" + canonicalName
              + "'. This enum is already defined in the configuration file(s), as a <table> or <enum>.");
        }
        tablesAndEnumsCanonicalNames.add(canonicalName);
      }

      Set<String> viewsCanonicalNames = new HashSet<String>();

      for (ViewTag vt : config.getViews()) {
        String canonicalName = this.adapter.canonizeName(vt.getName(), false);
        if (viewsCanonicalNames.contains(canonicalName)) {
          throw new ControlledException(vt.getSourceLocation(), "Duplicate database <view> name '" + canonicalName
              + "'. This enum is already defined in the configuration file(s).");
        }
        viewsCanonicalNames.add(canonicalName);
      }

      // Prepare views meta data

      logm("Prepare views metadata.");

      this.views = new LinkedHashSet<TableDataSetMetadata>();
      for (JdbcTable v : this.db.getViews()) {
        try {

          TableDataSetMetadata vm = DataSetMetadataFactory.getMetadata(v, this.adapter, config, layout, cachedMetadata);

          validateIdentifier(sqlNames, "view", v.getName(), vm.getIdentifier());
          this.views.add(vm);

          ClassPackage fragmentPackage = vm.getFragmentConfig() != null
              && vm.getFragmentConfig().getFragmentPackage() != null ? vm.getFragmentConfig().getFragmentPackage()
                  : null;
          ClassPackage classPackage = layout.getDAOPackage(fragmentPackage);
          String voName = daosTag.generateVOName(vm.getIdentifier());
          EntityVOClass vo = new EntityVOClass(vm, classPackage, voName, vm.getColumns(),
              vm.getDaoTag().getSourceLocation());
          this.voRegistry.addVO(vo);

        } catch (UnresolvableDataTypeException e) {
          throw new ControlledException(e.getMessage());

        } catch (VOAlreadyExistsException e) {
          throw new ControlledException("Duplicate view with name '" + v.getName() + "'.");
        } catch (StructuredVOAlreadyExistsException e) {
          throw new ControlledException("Duplicate view with name '" + v.getName() + "'.");

        }
      }

      // Prepare executor DAOs meta data

      SelectMetadataCache selectMetadataCache = cachedMetadata.getSelectMetadataCache();
      log.info(">>> 1 selectMetadataCache=" + selectMetadataCache);

      this.executors = new LinkedHashSet<ExecutorDAOMetadata>();
      for (ExecutorTag tag : config.getExecutors()) {

        ExecutorDAOMetadata dm = new ExecutorDAOMetadata(tag, this.adapter, config, tag.getFragmentConfig(),
            selectMetadataCache);
        this.executors.add(dm);
      }

      // Validate against the database

      // TODO: make sure the cache includes enum values from table rows.
      if (retrieveFreshDatabaseObjects) {
        try {
          this.config.validateAgainstDatabase(this, conn);
        } catch (InvalidConfigurationFileException e) {
          String message = (e.getSourceLocation() == null ? ""
              : "[file: " + e.getSourceLocation().getFile().getPath() + ", line "
                  + e.getSourceLocation().getLineNumber() + ", col " + e.getSourceLocation().getColumnNumber() + "] ")
              + e.getMessage();
          throw new ControlledException(message);
        }
      }

      // Prepare <select> methods meta data - phase 1

      for (TableDataSetMetadata tm : this.tables) {
        boolean retrieving = tm.gatherSelectsMetadataPhase1(this, conn, layout);
        if (retrieving) {
          retrieveSelectMetadata = true;
        }
      }

      for (TableDataSetMetadata vm : this.views) {
        boolean retrieving = vm.gatherSelectsMetadataPhase1(this, conn, layout);
        if (retrieving) {
          retrieveSelectMetadata = true;
        }
      }

      for (TableDataSetMetadata em : this.enums) {
        boolean retrieving = em.gatherSelectsMetadataPhase1(this, conn, layout);
        if (retrieving) {
          retrieveSelectMetadata = true;
        }
      }

      for (ExecutorDAOMetadata dm : this.executors) {
        boolean retrieving = dm.gatherSelectsMetadataPhase1(this, conn, layout);
        if (retrieving) {
          retrieveSelectMetadata = true;
        }
      }

      // Prepare <select> DAOs meta data - phase 1

      logm("Prepare selects metadata - phase 1.");

      if (!this.config.getSelects().isEmpty()) {
        retrieveSelectMetadata = true;
      }

      this.selects = new LinkedHashSet<SelectDataSetMetadata>();
      if (!this.config.getSelects().isEmpty()) {

        SelectClassTag current = null;
        SelectDataSetMetadata sm = null;

        try {

          for (SelectClassTag s : this.config.getSelects()) {
            log.debug("::: select '" + s.getJavaClassName() + "': " + s.renderSQLSentence(new ParameterRenderer() {
              @Override
              public String render(SQLParameter parameter) {
                return "?";
              }
            }));
            current = s;
            String tempViewName = this.config.getGenerators().getSelectedGeneratorTag().getSelectGeneration()
                .getNextTempViewName();
            sm = new SelectDataSetMetadata(this.db, this.adapter, this.loc, s, tempViewName, this.config);
            this.selects.add(sm);
            log.debug("prepareView() will be called...");
            sm.prepareViews(conn);
            log.debug("prepareView() complete.");
          }

        } catch (SQLException e) {
          throw new ControlledException("Failed to retrieve metadata for <" + new SelectClassTag().getTagName()
              + "> query '" + current.getJavaClassName() + "' while creating a temporary SQL view for it.\n" + "[ "
              + e.getMessage() + " ]\n" + "* Do all resulting columns have different and valid names?\n"
              + "* Is the trimmed create view SQL code below valid?\n" + "--- begin SQL ---\n" + sm.getCreateView()
              + "\n--- end SQL ---");
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

    log.debug("ret 1");
    if (retrieveSelectMetadata) {

      logm("Prepare selects metadata - phase 2.");

      SelectDataSetMetadata currDs = null;

      Connection conn2 = null;
      try {

        log.debug("ret 2");
        logm("Opening connection (selects)...");
        conn2 = this.loc.getConnection();
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
            + "> query with name '" + currDs.getIdentifier().getSQLIdentifier() + "'.", e);
      } catch (UnresolvableDataTypeException e) {
        throw new ControlledException(e.getMessage());
      } catch (InvalidConfigurationFileException e) {
        String message = (e.getSourceLocation() == null ? ""
            : "[file: " + e.getSourceLocation().getFile().getPath() + ", line " + e.getSourceLocation().getLineNumber()
                + ", col " + e.getSourceLocation().getColumnNumber() + "] ")
            + e.getMessage();
        throw new ControlledException(message);
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

    if (!incrementalMode) {
      this.cachedMetadata.setConfig(config);
      this.cachedMetadata.setCachedDatabase(this.db);
      this.cachedMetadata.setSelectMetadataCache(selectMetadataCache);
      this.cachedMetadata.setEnumConstants(tableEnumConstants);
    }

    // Display the retrieved meta data

    logm("Metadata initialized.");

    displayGenerationMetadata(config);

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

  private void propagateGeneration(final TableDataSetMetadata tm, final Set<TableDataSetMetadata> alreadyWalked) {
    if (!alreadyWalked.contains(tm)) {
      alreadyWalked.add(tm);
      if (tm.getDaoTag().getGenerateMark()) {
        for (ForeignKeyMetadata efk : tm.getExportedFKs()) {
          propagateGeneration(efk.getRemote().getTableMetadata(), alreadyWalked);
        }
        for (ForeignKeyMetadata ifk : tm.getImportedFKs()) {
          propagateGeneration(ifk.getRemote().getTableMetadata(), alreadyWalked);
        }
      }
    }
  }

  @SuppressWarnings("unused")
  private void logSelectMethodMetadata() {
    for (ExecutorDAOMetadata d : this.executors) {
      for (SelectMethodMetadata sm : d.getSelectsMetadata()) {
        display("=== Select method " + sm.getMethod() + " [" + (sm.isStructured() ? "structured" : "non-structured")
            + "] " + (sm.getVO() != null ? "returns " + sm.getVO() + " " : "")
            + (sm.isMultipleRows() ? "<multiple-rows-return>" : "<single-row-return>") + " ===");
        if (sm.isStructured()) {
          StructuredColumnsMetadata sc = sm.getStructuredColumns();
          if (sc.getSoloVOClass() == null) {
            logVOs(sc.getVOs(), 0);
            logExpressions(sc.getExpressions().getMetadata(), 0);
          } else {
            display("    + <main-single-vo> (new) " + sc.getSoloVOClass());
            logVOs(sc.getVOs(), 2);
            logExpressions(sc.getExpressions().getMetadata(), 2);
          }
        } else {
          for (ColumnMetadata cm : sm.getNonStructuredColumns()) {
            display(
                "   - " + cm.getIdentifier().getJavaMemberIdentifier() + " (" + cm.getType().getJavaClassName() + ")");
          }
        }
        display("--- end select method ---");
      }
    }
  }

  private void logExpressions(final List<StructuredColumnMetadata> expressions, final int level) {
    String filler = SUtils.getFiller(' ', level);
    for (StructuredColumnMetadata cm : expressions) {
      display("   " + filler + "+ " + cm.getIdentifier().getJavaMemberIdentifier() + " [expr] <is "
          + (cm.isId() ? "" : "not ") + "ID> (" + (cm.getConverter() != null
              ? "<converted-to> " + cm.getConverter().getJavaType() : cm.getType().getJavaClassName())
          + ") --> " + cm.getColumnAlias());
    }
  }

  private void logCollections(final List<VOMetadata> collections, final int level) {
    String filler = SUtils.getFiller(' ', level);
    for (VOMetadata c : collections) {
      boolean extendsVO = c.getSuperClass() != null;
      String based = c.getTableMetadata() != null ? (extendsVO ? "<extends>" : "<corresponds to>") + " table "
          + c.getTableMetadata().getIdentifier().getSQLIdentifier() + (extendsVO ? " <as> " + c.getSuperClass() : "")
          : (extendsVO ? "<extends>" : "<corresponds to>") + " view "
              + c.getViewMetadata().getIdentifier().getSQLIdentifier()
              + (extendsVO ? " <as> " + c.getSuperClass() : "");
      String property = c.getProperty() != null ? c.getProperty() : "<main-vo>";
      display("   " + filler + "+ " + property + " [collection] " + based);
      logColumns(c.getDeclaredColumns(), level + 2);
      logExpressions(c.getExpressions().getMetadata(), level + 2);
      logCollections(c.getCollections(), level + 2);
      logAssociations(c.getAssociations(), level + 2);
    }
  }

  private void logAssociations(final List<VOMetadata> associations, final int level) {
    String filler = SUtils.getFiller(' ', level);
    for (VOMetadata a : associations) {

      boolean extendsVO = a.getSuperClass() != null;
      String based = a.getTableMetadata() != null ? (extendsVO ? "<extends>" : "<corresponds to>") + " table "
          + a.getTableMetadata().getIdentifier().getSQLIdentifier() + (extendsVO ? " <as> " + a.getSuperClass() : "")
          : (extendsVO ? "<extends>" : "<corresponds to>") + " view "
              + a.getViewMetadata().getIdentifier().getSQLIdentifier()
              + (extendsVO ? " <as> " + a.getSuperClass() : "");

      String property = a.getProperty() != null ? a.getProperty() : "<main-vo>";

      display("   " + filler + "+ " + property + " [association] " + based);
      logColumns(a.getDeclaredColumns(), level + 2);
      logExpressions(a.getExpressions().getMetadata(), level + 2);
      logCollections(a.getCollections(), level + 2);
      logAssociations(a.getAssociations(), level + 2);
    }
  }

  private void logVOs(final List<VOMetadata> vos, final int level) {
    String filler = SUtils.getFiller(' ', level);
    for (VOMetadata vo : vos) {

      boolean extendsVO = vo.getSuperClass() != null;

      String based = vo.getTableMetadata() != null ? (extendsVO ? "<extends>" : "<corresponds to>") + " table "
          + vo.getTableMetadata().getIdentifier().getSQLIdentifier() + (extendsVO ? " <as> " + vo.getSuperClass() : "")
          : (extendsVO ? "<extends>" : "<corresponds to>") + " view "
              + vo.getViewMetadata().getIdentifier().getSQLIdentifier()
              + (extendsVO ? " <as> " + vo.getSuperClass() : "");

      String property = vo.getProperty() != null ? vo.getProperty() : "<main-single-vo>";
      display("   " + filler + "+ " + property + " [vo] " + based);
      logColumns(vo.getDeclaredColumns(), level + 2);
      logExpressions(vo.getExpressions().getMetadata(), level + 2);
      logCollections(vo.getCollections(), level + 2);
      logAssociations(vo.getAssociations(), level + 2);
    }
  }

  private void logColumns(final List<StructuredColumnMetadata> columns, final int level) {
    String indent = SUtils.getFiller(' ', level);
    for (StructuredColumnMetadata cm : columns) {
      display("   " + indent + "- " + cm.getIdentifier().getJavaMemberIdentifier()
          + (cm.isId() ? " <<id>>" : "") + " (" + (cm.getConverter() != null
              ? "<converted-to> " + cm.getConverter().getJavaType() : cm.getType().getJavaClassName())
          + ") --> " + cm.getColumnAlias());
    }
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

      for (TableDataSetMetadata t : this.tables) {
        display("Table " + t.getIdentifier().getSQLIdentifier() + " included.");
        for (SequenceMethodTag s : t.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getName() + " included.");
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

      for (TableDataSetMetadata v : this.views) {
        display("View " + v.getIdentifier().getSQLIdentifier() + " included.");
        for (SequenceMethodTag s : v.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getName() + " included.");
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

      for (EnumDataSetMetadata e : this.enums) {
        display("Enum " + e.getJdbcName() + " included.");
      }

      // daos

      for (ExecutorDAOMetadata d : this.executors) {
        if (this.displayMode == DisplayMode.LIST) {
          display("DAO " + d.getJavaClassName() + " included.");
        }
        for (SequenceMethodTag s : d.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getName() + " included.");
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

      // selects

      for (SelectClassTag q : config.getSelects()) {
        display("Select " + q.getJavaClassName() + " included.");
      }

    }

    display("");

    StringBuilder sb = new StringBuilder();
    sb.append("Total of: ");
    sb.append(this.db.getTables().size() + " " + (this.db.getTables().size() == 1 ? "table" : "tables") + ", ");
    sb.append(this.db.getViews().size() + " " + (this.db.getViews().size() == 1 ? "view" : "views") + ", ");
    sb.append(this.enums.size() + " " + (this.enums.size() == 1 ? "enum" : "enums") + ", ");
    sb.append(this.config.getExecutors().size() + " " + (this.config.getExecutors().size() == 1 ? "DAO" : "DAOs") //
        + ", ");
    sb.append(sequences + " sequence" + (sequences == 1 ? "" : "s") + ", ");
    sb.append(queries + " " + (queries == 1 ? "query" : "queries") + ", ");
    int totalSelects = config.getSelects().size() + selectMethods;
    sb.append("and " + totalSelects + " " + (totalSelects == 1 ? "select" : "selects") + ".");

    display(sb.toString());

  }

  private void validateDAONamesAndMethods(final HotRodConfigTag config)
      throws DuplicateDAOClassException, DuplicateDAOClassMethodException {

    DAONamespace ns = new DAONamespace();

    try {
      for (TableDataSetMetadata t : this.tables) {
        ns.registerDAOTag(t.getDaoTag(), "table", t.getIdentifier().getSQLIdentifier());
      }

      for (TableDataSetMetadata v : this.views) {
        ns.registerDAOTag(v.getDaoTag(), "view", v.getIdentifier().getSQLIdentifier());
      }

      for (SelectClassTag s : config.getSelects()) {
        ns.registerDAOTag(s, "select", s.getJavaClassName());
      }

      for (ExecutorTag c : config.getExecutors()) {
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

  private void validateIdentifier(final Set<String> SQLNames, final String objectType, final String sqlName,
      final Identifier id) throws ControlledException {
    if (id.hasSQLName() && SQLNames.contains(id.getSQLIdentifier())) {
      throw new ControlledException("Duplicate database object name '" + id.getSQLIdentifier() + "' on " + objectType
          + " '" + sqlName + "'. There's another table, view, dao, or select "
          + "whose java-name resolves to the same value (either specified or computed).");
    }
    SQLNames.add(id.getSQLIdentifier());
  }

  // private void validateAgainstDatabase(final JdbcDatabase db) throws
  // ControlledException {

  // Check tables

  // for (TableTag t : this.config.getTables()) {
  //
  // JdbcTable jt = this.findJdbcTable(t.getName(), db);
  // if (jt == null) {
  // throw new ControlledException("Could not find database table '" +
  // t.getName()
  // + "' as specified in the <table> tag of the configuration file. "
  // + "\n\nPlease verify the specified database catalog and schema names are
  // correct according to this database. "
  // + "You can try leaving the catalog/schema values empty, so " +
  // Constants.TOOL_NAME
  // + " will list all available values.");
  // }
  //
  // for (ColumnTag ct : t.getColumns()) {
  // JdbcColumn jc = this.findJdbcColumn(ct.getName(), jt);
  // if (jc == null) {
  // throw new ControlledException("Could not find column '" + ct.getName() + "'
  // on database table '" + t.getName()
  // + "', as specified in the <column> tag of the configuration file. ");
  // }
  // }
  //
  // }

  // Check views

  // for (ViewTag v : this.config.getViews()) {
  //
  // JdbcTable jv = this.findJdbcView(v.getName(), db);
  // if (jv == null) {
  // throw new ControlledException("Could not find database view '" +
  // v.getName()
  // + "' as specified in the <view> tag of the configuration file. "
  // + "\n\nPlease verify the specified database catalog and schema names are
  // correct according to this database. "
  // + "You can try leaving the catalog/schema values empty, so " +
  // Constants.TOOL_NAME
  // + " will list all available values.");
  // }
  //
  // for (ColumnTag ct : v.getColumns()) {
  // JdbcColumn jc = this.findJdbcColumn(ct.getName(), jv);
  // if (jc == null) {
  // throw new ControlledException("Could not find column '" + ct.getName() + "'
  // on database view '" + v.getName()
  // + "', as specified in the <column> tag of the configuration file. ");
  // }
  // }
  //
  // }

  // }

  // private JdbcTable findJdbcTable(final String name, final JdbcDatabase jd) {
  // for (JdbcTable t : jd.getTables()) {
  // if (this.adapter.isTableIdentifier(t.getName(), name)) {
  // return t;
  // }
  // }
  // return null;
  // }

  public TableDataSetMetadata findTableMetadata(final String name) {
    for (TableDataSetMetadata tm : this.tables) {
      if (this.adapter.isTableIdentifier(tm.getIdentifier().getSQLIdentifier(), name)) {
        return tm;
      }
    }
    return null;
  }

  public JdbcTable findJdbcTable(final String name) {
    for (JdbcTable t : this.db.getTables()) {
      if (this.adapter.isTableIdentifier(t.getName(), name)) {
        return t;
      }
    }
    return null;
  }

  public JdbcTable findJdbcView(final String name) {
    for (JdbcTable t : this.db.getViews()) {
      if (this.adapter.isTableIdentifier(t.getName(), name)) {
        return t;
      }
    }
    return null;
  }

  public JdbcColumn findJdbcColumn(final JdbcTable t, final String name) {
    for (JdbcColumn c : t.getColumns()) {
      if (this.adapter.isColumnIdentifier(c.getName(), name)) {
        return c;
      }
    }
    return null;
  }

  public TableDataSetMetadata findViewMetadata(final String name) {
    for (TableDataSetMetadata tm : this.views) {
      if (this.adapter.isTableIdentifier(tm.getIdentifier().getSQLIdentifier(), name)) {
        return tm;
      }
    }
    return null;
  }

  // private JdbcTable findJdbcView(final String name, final JdbcDatabase jd) {
  // for (JdbcTable jt : jd.getViews()) {
  // if (this.adapter.isTableIdentifier(jt.getName(), name)) {
  // return jt;
  // }
  // }
  // return null;
  // }

  // private JdbcColumn findJdbcColumn(final String name, final JdbcTable jt) {
  // for (JdbcColumn jc : jt.getColumns()) {
  // if (this.adapter.isColumnIdentifier(jc.getName(), name)) {
  // return jc;
  // }
  // }
  // return null;
  // }

  private ColumnMetadata findColumnMetadata(final String name, final SelectDataSetMetadata ds) {
    for (ColumnMetadata cm : ds.getColumns()) {
      if (this.adapter.isColumnIdentifier(cm.getColumnName(), name)) {
        return cm;
      }
    }
    return null;
  }

  // Utilities

  public static void display(final String txt) {
    System.out.println(SUtils.isEmpty(txt) ? " " : txt);
  }

  // Table Filter

  public class TableFilter implements JdbcTableFilter {

    private DatabaseAdapter adapter;

    public TableFilter(final DatabaseAdapter adapter) {
      this.adapter = adapter;
    }

    public boolean accepts(final String jdbcName) {
      log.debug("ACCEPTS? " + jdbcName);
      for (TableTag t : config.getTables()) {
        if (this.adapter.isTableIdentifier(jdbcName, t.getName())) {
          log.debug("table '" + jdbcName + "' accepted.");
          return true;
        }
      }
      for (EnumTag e : config.getEnums()) {
        if (this.adapter.isTableIdentifier(jdbcName, e.getName())) {
          log.debug("enum '" + jdbcName + "' accepted.");
          return true;
        }
      }

      log.debug("table/enum '" + jdbcName + "' rejected.");
      return false;
    }
  }

  // View Filter

  public class ViewFilter implements JdbcTableFilter {

    private Set<String> includedViews;
    private DatabaseAdapter adapter;

    public ViewFilter(final HotRodConfigTag config, final DatabaseAdapter adapter) {
      this.includedViews = new HashSet<String>();
      for (ViewTag v : config.getViews()) {
        this.includedViews.add(v.getName());
      }
      this.adapter = adapter;
    }

    public boolean accepts(final String jdbcName) {
      log.debug("ACCEPTS? " + jdbcName);
      for (ViewTag v : config.getViews()) {
        if (this.adapter.isTableIdentifier(jdbcName, v.getName())) {
          log.debug("view '" + jdbcName + "' accepted.");
          return true;
        }
      }
      log.debug("view '" + jdbcName + "' rejected.");
      return false;
    }
  }

  // VO Registry

  public VORegistry getVORegistry() {
    return voRegistry;
  }

  // Getters

  public DatabaseAdapter getAdapter() {
    return this.adapter;
  }

  public JdbcDatabase getJdbcDatabase() {
    return this.db;
  }

  public DatabaseLocation getLoc() {
    return this.loc;
  }

  // Abstract methods

  /**
   * Gathers all extra metadata for the specific generator.
   * 
   * Fails if any inconsistency is found between the existing database and the
   * config file. No code generation is performed by this method, so any failure
   * will not override a previous code generation.
   * 
   * @throws UncontrolledException
   * @throws ControlledException
   */
  public abstract void prepareGeneration() throws UncontrolledException, ControlledException;

  /**
   * Generates new persistence code.
   * 
   * This is the second step of the generation and should be called only after
   * the prepareGeneration() method has succeeded.
   * 
   * @throws UncontrolledException
   * @throws ControlledException
   */

  public abstract void generate() throws UncontrolledException, ControlledException;

  // Logging

  private void logm(final String msg) {
    long now = System.currentTimeMillis();
    if (this.lastLog == null) {
      logm.debug("[===== Initial =====] - " + msg);
    } else {
      logm.debug("[===== " + (now - this.lastLog) + " ms =====] - " + msg);
    }
    this.lastLog = now;
  }

}