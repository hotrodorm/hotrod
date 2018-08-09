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
import org.hotrod.ant.HotRodAntTask.DisplayMode;
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
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UncontrolledException;
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
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.identifiers2.ObjectId;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.DatabaseObjectFilter;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase.DatabaseConnectionVersion;
import org.nocrala.tools.database.tartarus.core.JdbcForeignKey;
import org.nocrala.tools.database.tartarus.core.JdbcTable;
import org.nocrala.tools.database.tartarus.core.JdbcTableFilter;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.DifferentCatalogSchemaException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.database.tartarus.exception.ReaderException;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;

public abstract class HotRodGenerator {

  private static final Logger log = Logger.getLogger(HotRodGenerator.class);

  private static final Logger logm = Logger.getLogger("hotrod-metadata-retrieval");

  protected DatabaseLocation dloc;
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

  public HotRodGenerator(final CachedMetadata cachedMetadata, final DatabaseLocation dloc, final HotRodConfigTag config,
      final DisplayMode displayMode, final boolean incrementalMode, final DatabaseAdapter adapter)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {

    log.debug(">>> HG 1 cachedMetadata=" + cachedMetadata);
    log.debug(">>> HG 1 cachedMetadata.getSelectMetadataCache()=" + cachedMetadata.getSelectMetadataCache());

    this.dloc = dloc;
    this.config = config;
    this.displayMode = displayMode;
    this.cachedMetadata = cachedMetadata;
    this.adapter = adapter;

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
      display("Database Catalog: " + (this.adapter.supportsCatalog() ? dloc.getDefaultCatalog() : "(not supported)"));
      display("Database Schema: " + (this.adapter.supportsSchema() ? dloc.getDefaultSchema() : "(not supported)"));
      display("");

      // Decide about using cached or fresh database objects

      JdbcDatabase cachedDatabase = incrementalMode ? cachedMetadata.getCachedDatabase() : null;

      // TODO: remove extra logging once fixed.
      if (cachedDatabase != null) {
        JdbcTable found = null;
        for (JdbcTable t : cachedDatabase.getTables()) {
          if (this.adapter.isTableIdentifier(t.getName(), "client")) {
            found = t;
          }
        }
        if (found != null) {
          log.debug("---> CLIENT [from cache] imported FKs:");
          for (JdbcForeignKey ifk : found.getImportedFks()) {
            log.debug("   FK points to: " + ifk.getRemoteTable().getName());
          }
          for (JdbcForeignKey efk : found.getExportedFks()) {
            log.debug("   FK pointed from: " + efk.getRemoteTable().getName());
          }
          log.debug("---> [end of table CLIENT]");
        } else {
          log.debug("---> CLIENT table was not found in cache.");
        }
      } else {
        log.debug("---> no CLIENT table in cache, since there's no cache.");
      }
      HotRodConfigTag ch = cachedMetadata.getConfig();
      if (ch != null) {
        log.debug("...=== Enums from cache config ===");
        for (EnumTag et : ch.getAllEnums()) {
          log.debug("... enum '" + et.getJdbcName() + "'");
        }
        log.debug("...=== End of enums from cache config ===");
      } else {
        log.debug("... cached-config is null.");
      }

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

      log.debug("--> retrieveFreshDatabaseObjects=" + retrieveFreshDatabaseObjects);

      DatabaseObjectFilter filter = new DatabaseObjectFilter(new TableFilter(this.adapter),
          new ViewFilter(this.config, this.adapter));

      try {

        if (!retrieveFreshDatabaseObjects) {
          try {
            log.debug("Will activate now.");
            cachedDatabase.activate(this.dloc, true);
            this.db = cachedDatabase;
          } catch (DifferentCatalogSchemaException e) {
            // catalog or schema changed -- retrieve the database again.
            this.db = new JdbcDatabase(this.dloc, true, filter);
          }
        } else {
          // Retrieve database objects
          this.db = new JdbcDatabase(this.dloc, true, filter);
        }

      } catch (ReaderException e) {
        throw new ControlledException(e.getMessage());
      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve database metadata.", e);
      } catch (CatalogNotSupportedException e) {
        throw new ControlledException("A catalog name was specified for the database with the value '"
            + dloc.getDefaultCatalog() + "', " + "but this database does not support catalogs through the JDBC driver. "
            + "Please specify it with an empty value.");
      } catch (InvalidCatalogException e) {
        StringBuilder sb = new StringBuilder();
        if (dloc.getDefaultCatalog() == null) {
          sb.append(
              "This database requires a catalog name. Please specify in " + Constants.TOOL_NAME + "'s Ant task.\n\n");
        } else {
          sb.append(
              "The specified catalog name '" + dloc.getDefaultCatalog() + "' does not exist in this database.\n\n");
        }
        sb.append("The available catalogs are:\n");
        for (String c : e.getExistingCatalogs()) {
          sb.append("  " + c + "\n");
        }
        throw new ControlledException(sb.toString());
      } catch (SchemaNotSupportedException e) {
        throw new ControlledException("A schema name was specified for the database with the value '"
            + dloc.getDefaultSchema() + "', " + "but this database does not support schemas through the JDBC driver. "
            + "Please specify it with an empty value.");
      } catch (InvalidSchemaException e) {
        StringBuilder sb = new StringBuilder();
        if (dloc.getDefaultSchema() == null) {
          sb.append(
              "This database requires a schema name. Please specify in " + Constants.TOOL_NAME + "'s Ant task.\n\n");
        } else {
          sb.append("The specified schema name '" + dloc.getDefaultSchema() + "' does not exist in this database.\n\n");
        }
        sb.append("The available schemas are:\n");
        for (String s : e.getExistingSchemas()) {
          sb.append("  " + s + "\n");
        }
        throw new ControlledException(sb.toString());
      } catch (Exception e) {
        throw new UncontrolledException("Could not retrieve database metadata using JDBC URL " + dloc.getUrl(), e);
      }

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
                  + m.getColumnName() + "' of table/view/select '" + m.getTableName() + "'.");

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
            SourceLocation loc = em.getDaoTag().getSourceLocation();
            throw new ControlledException("Invalid configuration file '" + loc.getFile().getPath() + "' (line "
                + loc.getLineNumber() + ", col " + loc.getColumnNumber() + "):\n" + "cannot specify the enum '"
                + em.renderSQLIdentifier() + "' on the 'many' side of a 1-to-many relationship.");

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

      for (TableTag tt : config.getTables()) {
        String canonicalName = tt.getId().getCanonicalSQLName();
        if (tablesAndEnumsCanonicalNames.contains(canonicalName)) {
          throw new ControlledException(tt.getSourceLocation(), "Duplicate database <table> name '" + canonicalName
              + "'. This table is already defined in the configuration file(s).");
        }
        tablesAndEnumsCanonicalNames.add(canonicalName);
      }

      for (EnumTag et : config.getEnums()) {
        String canonicalName = et.getId().getCanonicalSQLName();
        if (tablesAndEnumsCanonicalNames.contains(canonicalName)) {
          throw new ControlledException(et.getSourceLocation(), "Duplicate database <enum> name '" + canonicalName
              + "'. This enum is already defined in the configuration file(s), as a <table> or <enum>.");
        }
        tablesAndEnumsCanonicalNames.add(canonicalName);
      }

      Set<String> viewsCanonicalNames = new HashSet<String>();

      for (ViewTag vt : config.getViews()) {
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
      for (ExecutorTag tag : config.getExecutors()) {
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

      if (!this.config.getSelects().isEmpty()) {
        retrieveSelectMetadata = true;
      }

      this.selects = new LinkedHashSet<SelectDataSetMetadata>();
      if (!this.config.getSelects().isEmpty()) {

        SelectClassTag current = null;
        SelectDataSetMetadata sm = null;

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
            display("   - " + cm.getId().getJavaMemberName() + " (" + cm.getType().getJavaClassName() + ")");
          }
        }
        display("--- end select method ---");
      }
    }
  }

  private void logExpressions(final List<StructuredColumnMetadata> expressions, final int level) {
    String filler = SUtils.getFiller(' ', level);
    for (StructuredColumnMetadata cm : expressions) {
      display("   " + filler + "+ " + cm.getId().getJavaMemberName() + " [expr] <is "
          + (cm.isId() ? "" : "not ") + "ID> (" + (cm.getConverter() != null
              ? "<converted-to> " + cm.getConverter().getJavaType() : cm.getType().getJavaClassName())
          + ") --> " + cm.getColumnAlias());
    }
  }

  private void logCollections(final List<VOMetadata> collections, final int level) {
    String filler = SUtils.getFiller(' ', level);
    for (VOMetadata c : collections) {
      boolean extendsVO = c.getSuperClass() != null;
      String based = c.getTableMetadata() != null
          ? (extendsVO ? "<extends>" : "<corresponds to>") + " table "
              + c.getTableMetadata().getId().getCanonicalSQLName() + (extendsVO ? " <as> " + c.getSuperClass() : "")
          : (extendsVO ? "<extends>" : "<corresponds to>") + " view "
              + c.getViewMetadata().getId().getCanonicalSQLName() + (extendsVO ? " <as> " + c.getSuperClass() : "");
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
      String based = a.getTableMetadata() != null
          ? (extendsVO ? "<extends>" : "<corresponds to>") + " table "
              + a.getTableMetadata().getId().getCanonicalSQLName() + (extendsVO ? " <as> " + a.getSuperClass() : "")
          : (extendsVO ? "<extends>" : "<corresponds to>") + " view "
              + a.getViewMetadata().getId().getCanonicalSQLName() + (extendsVO ? " <as> " + a.getSuperClass() : "");

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

      String based = vo.getTableMetadata() != null
          ? (extendsVO ? "<extends>" : "<corresponds to>") + " table "
              + vo.getTableMetadata().getId().getCanonicalSQLName() + (extendsVO ? " <as> " + vo.getSuperClass() : "")
          : (extendsVO ? "<extends>" : "<corresponds to>") + " view "
              + vo.getViewMetadata().getId().getCanonicalSQLName() + (extendsVO ? " <as> " + vo.getSuperClass() : "");

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
      display("   " + indent + "- " + cm.getId().getJavaMemberName()
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
        display("Table " + t.getId().getCanonicalSQLName() + " included.");
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
        display("View " + v.getId().getCanonicalSQLName() + " included.");
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
        ns.registerDAOTag(t.getDaoTag(), "table", t.getId().getCanonicalSQLName());
      }

      for (TableDataSetMetadata v : this.views) {
        ns.registerDAOTag(v.getDaoTag(), "view", v.getId().getCanonicalSQLName());
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
      final ObjectId id) throws ControlledException {
    if (id.getCanonicalSQLName() != null && SQLNames.contains(id.getCanonicalSQLName())) {
      throw new ControlledException("Duplicate database object name '" + id.getCanonicalSQLName() + "' on " + objectType
          + " '" + sqlName + "'. There's another table, view, dao, or select "
          + "whose java-name resolves to the same value (either specified or computed).");
    }
    SQLNames.add(id.getCanonicalSQLName());
  }

  // TODO: remove

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

  public TableDataSetMetadata findTableMetadata(final ObjectId id) {
    for (TableDataSetMetadata tm : this.tables) {
      if (tm.getId().equals(id)) {
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

  public TableDataSetMetadata findViewMetadata(final ObjectId id) {
    for (TableDataSetMetadata tm : this.views) {
      if (tm.getId().equals(id)) {
        return tm;
      }
    }
    return null;
  }

  // TODO: remove
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
        if (this.adapter.isTableIdentifier(jdbcName, t.getId().getCanonicalSQLName())) {
          log.debug("table '" + jdbcName + "' accepted.");
          return true;
        }
      }
      for (EnumTag e : config.getEnums()) {
        if (this.adapter.isTableIdentifier(jdbcName, e.getId().getCanonicalSQLName())) {
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
        this.includedViews.add(v.getId().getCanonicalSQLName());
      }
      this.adapter = adapter;
    }

    public boolean accepts(final String jdbcName) {
      log.debug("ACCEPTS? " + jdbcName);
      for (ViewTag v : config.getViews()) {
        if (this.adapter.isTableIdentifier(jdbcName, v.getId().getCanonicalSQLName())) {
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
    return this.dloc;
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