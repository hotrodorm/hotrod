package org.hotrod.metadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.DaosTag;
import org.hotrod.config.EnumTag;
import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.ExecutorTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.generator.DAONamespace;
import org.hotrod.generator.DAONamespace.DuplicateDAOClassException;
import org.hotrod.generator.DAONamespace.DuplicateDAOClassMethodException;
import org.hotrod.generator.SelectMetadataCache;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.VORegistry.EntityVOClass;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.JdbcTypes;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class Metadata {

  private static final Logger log = LogManager.getLogger(Metadata.class);

  private JdbcDatabase db;
  private DatabaseAdapter adapter;
  private DatabaseLocation dloc;

  private LinkedHashSet<TableDataSetMetadata> tables = null;
  private LinkedHashSet<TableDataSetMetadata> views = null;
  private LinkedHashSet<EnumDataSetMetadata> enums = null;
  private LinkedHashSet<ExecutorDAOMetadata> executors = null;
  private VORegistry voRegistry = null;

  public Metadata(final JdbcDatabase db, final DatabaseAdapter adapter, final DatabaseLocation dloc) {
    this.db = db;
    this.adapter = adapter;
    this.dloc = dloc;
  }

  // Load metadata

  public void load(final HotRodConfigTag config, final DatabaseLocation dloc, final Connection conn)
      throws ControlledException, InvalidConfigurationFileException, UncontrolledException {

    this.voRegistry = new VORegistry();

    ColumnsRetriever cr = null;

    try {

      // Prepare tables meta data

      DataSetLayout layout = new DataSetLayout(config);
      DaosTag daosTag = config.getGenerators().getSelectedGeneratorTag().getDaos();

      this.tables = new LinkedHashSet<TableDataSetMetadata>();
      for (JdbcTable t : this.db.getTables()) {
        try {
          log.debug("t.getName()=" + t.getName());
          TableDataSetMetadata tm = DataSetMetadataFactory.getMetadata(t, this.adapter, config, layout);
          log.debug("*** tm=" + tm);

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

      // Separate enums metadata from tables'

      log.debug("Prepare enums metadata.");

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

      log.debug("Prepare views metadata.");

      this.views = new LinkedHashSet<TableDataSetMetadata>();
      TableDataSetMetadata vmd = null;
      for (JdbcTable v : this.db.getViews()) {
        try {

          vmd = DataSetMetadataFactory.getMetadata(v, this.adapter, config, layout);
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

      this.executors = new LinkedHashSet<ExecutorDAOMetadata>();
      for (ExecutorTag tag : config.getFacetExecutors()) {
        ExecutorDAOMetadata dm;
        try {
          dm = new ExecutorDAOMetadata(tag, adapter, config, tag.getFragmentConfig());
        } catch (InvalidIdentifierException e) {
          throw new ControlledException(tag.getSourceLocation(),
              "Invalid DAO with namename '" + tag.getJavaClassName() + "': " + e.getMessage());
        }
        this.executors.add(dm);
      }

      // Validate against the database

      cr = ColumnsRetriever.getInstance(config, dloc, adapter, db, conn);
      log.debug("ColumnsRetriever: " + cr);

      // TODO: make sure the cache includes enum values from table rows.
      // if (retrieveFreshDatabaseObjects) {
      try {
        config.validateAgainstDatabase(this, conn, adapter);
      } catch (InvalidConfigurationFileException e) {
        throw new ControlledException(e.getTag().getSourceLocation(), e.getMessage());
      }
      // }

      // Prepare <select> methods metadata - phase 1

      for (TableDataSetMetadata tm : this.tables) {
        try {
          tm.gatherSelectsMetadataPhase1(this, cr, layout);
        } catch (InvalidConfigurationFileException e) {
          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
        }
      }

      for (TableDataSetMetadata vm : this.views) {
        try {
          vm.gatherSelectsMetadataPhase1(this, cr, layout);
        } catch (InvalidConfigurationFileException e) {
          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
        }
      }

      for (TableDataSetMetadata em : this.enums) {
        try {
          em.gatherSelectsMetadataPhase1(this, cr, layout);
        } catch (InvalidConfigurationFileException e) {
          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
        }
      }

      for (ExecutorDAOMetadata dm : this.executors) {
        try {
          dm.gatherSelectsMetadataPhase1(this, cr, layout);
        } catch (InvalidConfigurationFileException e) {
          throw new ControlledException(e.getTag().getSourceLocation(), e.getInteractiveMessage(), e.getMessage());
        }
      }

    } catch (

    SQLException e) {
      throw new UncontrolledException("Could not retrieve database metadata.", e);

    } finally {
      if (conn != null) {
        try {
          log.debug("Closing connection...");
          conn.close();
          log.debug("Connection closed.");
        } catch (SQLException e) {
          throw new UncontrolledException("Could not retrieve database metadata.", e);
        }
      }
    }

    // Prepare <select> DAOs meta data - phase 2

    SelectMetadataCache selectMetadataCache = new SelectMetadataCache();
    Map<String, List<EnumConstant>> tableEnumConstants = new HashMap<String, List<EnumConstant>>();

    log.debug("Prepare selects metadata - phase 2.");

    try {

      for (TableDataSetMetadata tm : this.tables) {
        tm.gatherSelectsMetadataPhase2(this.voRegistry);
        addSelectsMetaData(tm.getDaoTag().getJavaClassName(), tm.getSelectsMetadata(), selectMetadataCache);
      }

      for (TableDataSetMetadata vm : this.views) {
        vm.gatherSelectsMetadataPhase2(this.voRegistry);
        addSelectsMetaData(vm.getDaoTag().getJavaClassName(), vm.getSelectsMetadata(), selectMetadataCache);
      }

      for (TableDataSetMetadata em : this.enums) {
        em.gatherSelectsMetadataPhase2(this.voRegistry);
        addSelectsMetaData(em.getDaoTag().getJavaClassName(), em.getSelectsMetadata(), selectMetadataCache);
        addTableEnumConstants(em, tableEnumConstants);
      }

      for (ExecutorDAOMetadata xm : this.executors) {
        xm.gatherSelectsMetadataPhase2(this.voRegistry);
        addSelectsMetaData(xm.getDaoTag().getJavaClassName(), xm.getSelectsMetadata(), selectMetadataCache);
      }

    } catch (InvalidConfigurationFileException e) {
      throw new ControlledException(e.getTag().getSourceLocation(), e.getMessage());
    } finally {
      log.debug("Closing connection (selects)...");
      try {
        cr.close();
      } catch (Exception e) {
        log.debug("Could not close database connection", e);
      }
      log.debug("Connection closed (selects).");
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

    // Display the retrieved meta data

    log.debug("Metadata initialized.");

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

  // Getters & Setters

  public LinkedHashSet<TableDataSetMetadata> getTables() {
    return tables;
  }

  public LinkedHashSet<TableDataSetMetadata> getViews() {
    return views;
  }

  public LinkedHashSet<EnumDataSetMetadata> getEnums() {
    return enums;
  }

  public LinkedHashSet<ExecutorDAOMetadata> getExecutors() {
    return executors;
  }

  public VORegistry getVORegistry() {
    return voRegistry;
  }

  // implements MetadataRepository

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

  public DatabaseAdapter getAdapter() {
    return this.adapter;
  }

  public JdbcDatabase getJdbcDatabase() {
    return this.db;
  }

  public DatabaseLocation getLoc() {
    return this.dloc;
  }

}
