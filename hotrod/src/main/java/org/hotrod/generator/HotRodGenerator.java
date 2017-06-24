package org.hotrod.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.CustomDAOTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.QueryTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectTag;
import org.hotrod.config.SequenceTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.DatabaseAdapterFactory;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnrecognizedDatabaseException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.DAONamespace.DuplicateDAOClassException;
import org.hotrod.generator.DAONamespace.DuplicateDAOClassMethodException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.SelectDataSetMetadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.SUtils;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
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

  protected LinkedHashSet<TableDataSetMetadata> tables = null;
  protected LinkedHashSet<TableDataSetMetadata> views = null;
  protected LinkedHashSet<SelectDataSetMetadata> selects = null;

  private Long lastLog = null;

  public HotRodGenerator(final DatabaseLocation loc, final HotRodConfigTag config, final DisplayMode displayMode)
      throws UncontrolledException, ControlledException {
    this.loc = loc;
    this.config = config;
    this.displayMode = displayMode;

    logm("Starting core generator.");

    display("");
    display("Database URL: " + loc.getUrl());

    Connection conn = null;

    try {
      logm("Opening connection...");
      conn = this.loc.getConnection();
      logm("Connection open.");

      String databaseName = null;
      String databaseVersion = null;
      String jdbcDriverName = null;
      String jdbcDriverVersion = null;

      try {
        logm("Getting initial metadata.");
        DatabaseMetaData dm = conn.getMetaData();
        logm("Initial metadata retrieval started.");

        databaseName = dm.getDatabaseProductName();
        databaseVersion = dm.getDatabaseMajorVersion() + "." + dm.getDatabaseMinorVersion() + " ("
            + dm.getDatabaseProductVersion() + ")";
        jdbcDriverName = dm.getDriverName();
        jdbcDriverVersion = dm.getDriverMajorVersion() + "." + dm.getDriverMinorVersion() + " (" + dm.getDriverVersion()
            + ")";
        logm("Initial metadata retrieval complete.");

      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve database metadata.", e);
      }

      display("Database Name: " + databaseName + " - version " + databaseVersion);
      display("JDBC Driver: " + jdbcDriverName + " - version " + jdbcDriverVersion);

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

      try {
        logm("Ready for database objects retrieval.");
        this.db = new JdbcDatabase(this.loc, true, new TableFilter(this.config, this.adapter),
            new ViewFilter(this.config, this.adapter));
        logm("After retrieval 1.");

      } catch (ReaderException e) {
        throw new ControlledException(e.getMessage());
      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve database metadata.", e);
      } catch (CatalogNotSupportedException e) {
        throw new ControlledException("A catalog name was specified for the database with the value '"
            + loc.getDefaultCatalog() + "', " + "but this database does not support catalogs through the JDBC driver. "
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
          sb.append("The specified schema name '" + loc.getDefaultSchema() + "' does not exist in this database.\n\n");
        }
        sb.append("The available schemas are:\n");
        for (String s : e.getExistingSchemas()) {
          sb.append("  " + s + "\n");
        }
        throw new ControlledException(sb.toString());
      } catch (Exception e) {
        throw new UncontrolledException("Could not retrieve database metadata using JDBC URL " + loc.getUrl(), e);
      }

      try {

        this.config.validateAgainstDatabase(this.db, this.adapter);

      } catch (InvalidConfigurationFileException e) {
        throw new ControlledException(e.getMessage());
      }

      // Validate names

      logm("Validate Names.");

      Set<String> sqlNames = new HashSet<String>();

      // Prepare tables metadata

      logm("Prepare tables metadata.");

      this.tables = new LinkedHashSet<TableDataSetMetadata>();
      for (JdbcTable t : this.db.getTables()) {
        TableDataSetMetadata tm;
        try {
          log.debug("t.getName()=" + t.getName());
          tm = new TableDataSetMetadata(t, this.adapter, this.config);
          log.debug("*** tm=" + tm);
          validateIdentifier(sqlNames, "table", t.getName(), tm.getIdentifier());
          this.tables.add(tm);
        } catch (UnresolvableDataTypeException e) {
          ColumnMetadata m = e.getColumnMetadata();

          String typeName = JdbcTypes.codeToName(m.getDataType());

          throw new ControlledException(
              "Unrecognized column data type (reported as '" + m.getTypeName() + "', JDBC type " + m.getDataType() + " "
                  + (typeName == null ? "(non-standard JDBC type)" : "'" + typeName + "'") + ") on column '"
                  + m.getColumnName() + "' of table/view/select '" + m.getTableName() + "'.");
        }
      }
      for (TableDataSetMetadata ds : this.tables) {
        ds.linkReferencedDataSets(this.tables);
      }

      // Prepare views metadata

      logm("Prepare views metadata.");

      this.views = new LinkedHashSet<TableDataSetMetadata>();
      for (JdbcTable v : this.db.getViews()) {
        try {
          TableDataSetMetadata vm = new TableDataSetMetadata(v, this.adapter, this.config);
          validateIdentifier(sqlNames, "view", v.getName(), vm.getIdentifier());
          this.views.add(vm);
        } catch (UnresolvableDataTypeException e) {
          throw new ControlledException(e.getMessage());
        }
      }

      // Prepare selects metadata - phase 1/2

      logm("Prepare selects metadata - phase 1.");

      this.selects = new LinkedHashSet<SelectDataSetMetadata>();
      log.debug(">>>>>>>>>>>>>>>>>>> this.config.getSelects().size()=" + this.config.getSelects().size());
      if (!this.config.getSelects().isEmpty()) {

        SelectTag current = null;
        SelectDataSetMetadata sm = null;

        try {

          int i = 0;
          for (SelectTag s : this.config.getSelects()) {
            log.debug("::: select '" + s.getJavaClassName() + "': " + s.renderSQLSentence(new ParameterRenderer() {
              @Override
              public String render(SQLParameter parameter) {
                return "?";
              }
            }));
            current = s;
            String tempViewNameBase = this.config.getGenerators().getSelectedGeneratorTag().getSelectGeneration()
                .getTempViewBaseName();
            sm = new SelectDataSetMetadata(this.db, this.adapter, this.loc, s, tempViewNameBase + (i++), this.config);
            this.selects.add(sm);
            sm.prepareViews(conn);
          }

        } catch (SQLException e) {
          throw new ControlledException("Failed to retrieve metadata for <" + SelectTag.TAG_NAME + "> query '"
              + current.getJavaClassName() + "' while creating a temporary SQL view for it.\n" + "[ " + e.getMessage()
              + " ]\n" + "* Do all resulting columns have different and valid names?\n"
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

    // Prepare selects metadata - phase 2/2

    if (!this.config.getSelects().isEmpty()) {

      logm("Prepare selects metadata - phase 2.");

      SelectDataSetMetadata currDs = null;

      Connection conn2 = null;
      try {

        logm("Opening connection (selects)...");
        conn2 = this.loc.getConnection();
        logm("Connection open (selects).");
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
        throw new UncontrolledException("Failed to retrieve metadata for <" + SelectTag.TAG_NAME + "> query with name '"
            + currDs.getIdentifier().getSQLIdentifier() + "'.", e);
      } catch (UnresolvableDataTypeException e) {
        throw new ControlledException(e.getMessage());
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

    // Display the retrieved metadata

    logm("Metadata initialized.");

    displayGenerationMetadata(config);

  }

  private void displayGenerationMetadata(final HotRodConfigTag config) {

    int sequences = 0;
    int queries = 0;

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
        for (SequenceTag s : t.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getName() + " included.");
          }
        }
        for (QueryTag q : t.getQueries()) {
          queries++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Query " + q.getJavaMethodName() + " included.");
          }
        }
      }

      // views

      for (TableDataSetMetadata v : this.views) {
        display("View " + v.getIdentifier().getSQLIdentifier() + " included.");
        for (SequenceTag s : v.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getName() + " included.");
          }
        }
        for (QueryTag q : v.getQueries()) {
          queries++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Query " + q.getJavaMethodName() + " included.");
          }
        }
      }

      // daos

      for (CustomDAOTag c : config.getDAOs()) {
        if (this.displayMode == DisplayMode.LIST) {
          display("Custom DAO " + c.getJavaClassName() + " included.");
        }
        for (SequenceTag s : c.getSequences()) {
          sequences++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Sequence " + s.getName() + " included.");
          }
        }
        for (QueryTag q : c.getQueries()) {
          queries++;
          if (this.displayMode == DisplayMode.LIST) {
            display(" - Query " + q.getJavaMethodName() + " included.");
          }
        }
      }

      // selects

      for (SelectTag q : config.getSelects()) {
        display("Select " + q.getJavaClassName() + " included.");
      }

    }

    display("");

    StringBuilder sb = new StringBuilder();
    sb.append("Total of: ");
    sb.append(this.db.getTables().size() + " " + (this.db.getTables().size() == 1 ? "table" : "tables") + ", ");
    sb.append(this.db.getViews().size() + " " + (this.db.getViews().size() == 1 ? "view" : "views") + ", ");
    sb.append(this.config.getDAOs().size() + " " + (this.config.getDAOs().size() == 1 ? "DAO" : "DAOs") //
        + ", ");
    sb.append(sequences + " sequence" + (sequences == 1 ? "" : "s") + ", ");
    sb.append(queries + " " + (queries == 1 ? "query" : "queries") + ", ");
    sb.append(
        "and " + config.getSelects().size() + " " + (config.getSelects().size() == 1 ? "select" : "selects") + ".");

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

      for (SelectTag s : config.getSelects()) {
        ns.registerDAOTag(s, "select", s.getJavaClassName());
      }

      for (CustomDAOTag c : config.getDAOs()) {
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
  //
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

  public class TableFilter implements JdbcTableFilter {

    private Set<String> includedTables;
    private DatabaseAdapter adapter;

    public TableFilter(final HotRodConfigTag config, final DatabaseAdapter adapter) {
      this.includedTables = new HashSet<String>();
      for (TableTag t : config.getTables()) {
        this.includedTables.add(t.getName());
      }
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
      log.debug("table '" + jdbcName + "' rejected.");
      return false;
    }
  }

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

  public DatabaseAdapter getAdapter() {
    return adapter;
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