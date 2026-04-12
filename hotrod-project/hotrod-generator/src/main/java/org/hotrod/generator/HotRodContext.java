package org.hotrod.generator;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.Constants;
import org.hotrod.config.EnumTag;
import org.hotrod.config.ExcludeTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.SchemaTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.DatabaseAdapterFactory;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnrecognizedDatabaseException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.T;
import org.hotrod.utils.XUtil;
import org.nocrala.tools.database.tartarus.connectors.DatabaseConnectorFactory.UnsupportedDatabaseException;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.DatabaseObject;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase.DatabaseConnectionVersion;
import org.nocrala.tools.database.tartarus.core.JdbcTable;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.DatabaseObjectNotFoundException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogSchemaException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.database.tartarus.exception.ReaderException;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;

public class HotRodContext {

  private static final Logger log = Logger.getLogger(HotRodContext.class.getName());

  private DatabaseLocation loc;
  private DatabaseAdapter adapter;
  private HotRodConfigTag config;
  private JdbcDatabase db;
  private Metadata metadata;

  public HotRodContext(final File configFile, final String jdbcdriverclass, final String jdbcurl,
      final String jdbcusername, final String jdbcpassword, final String currentJDBCCatalog,
      final String currentJDBCSchema, final File baseDir, final LinkedHashSet<String> facetNames,
      final Feedback feedback, final boolean logTimes) throws ErrorMessageException, FaultException {

    log.fine("init");

    feedback.info("");
    feedback.info("Configuration File: " + (configFile == null ? "(no-config mode)" : configFile));

    this.loc = new DatabaseLocation(jdbcdriverclass, jdbcurl, jdbcusername, jdbcpassword, currentJDBCCatalog,
        currentJDBCSchema, null);
    T.endPhase("DB Location");

    feedback.info("Database URL: " + loc.getUrl());

    Connection conn = null;
    try {

      try {
        conn = this.loc.getConnection();
        log.fine("Connection open.");
      } catch (SQLException e) {
        throw new ErrorMessageException("Could not connect to the database: " + XUtil.trim(e));
      }
      T.endPhase("DB connected");

      // Database Version

      DatabaseConnectionVersion cv;
      try {
        log.fine("Getting initial metadata.");
        cv = new DatabaseConnectionVersion(conn.getMetaData());
        log.fine("Metadata retrieval complete.");

      } catch (SQLException e) {
        throw new ErrorMessageException("Could not retrieve database metadata: " + XUtil.trim(e));
      }
      feedback.info("Database Name: " + cv.renderDatabaseName());
      feedback.info("JDBC Driver: " + cv.renderJDBCDriverName() + " - implements JDBC Specification "
          + cv.renderJDBCSpecification());
      T.endPhase("DB Version");

      // Adapter

      try {
        this.adapter = DatabaseAdapterFactory.getAdapter(conn);
        feedback.info("HotRod Adapter: " + adapter.getName());
      } catch (UnrecognizedDatabaseException e) {
        throw new ErrorMessageException("Could not identify database at URL " + loc.getUrl() + " - " + e.getMessage());
      } catch (FaultException e) {
        throw new ErrorMessageException("Could not identify database at URL " + loc.getUrl() + " - " + e.getMessage()
            + ": " + XUtil.trim(e.getCause()));
      } catch (RuntimeException e) {
        throw new ErrorMessageException("Could not identify database at URL " + loc.getUrl() + " - " + XUtil.trim(e));
      } catch (SQLException e) {
        throw new ErrorMessageException("Could not identify database at URL " + loc.getUrl() + " - " + XUtil.trim(e));
      }
      T.endPhase("DB Adapter");

      // Current Catalog & Schema

      feedback.info(" ");
      if (adapter.supportsCatalog()) {
        feedback.info("Current Catalog: " + (loc.getCurrentCatalog() == null ? "" : loc.getCurrentCatalog()));
      }
      if (adapter.supportsSchema()) {
        feedback.info("Current Schema: " + (loc.getCurrentSchema() == null ? "" : loc.getCurrentSchema()));
      }
      feedback.info(" ");

      // Loading Configuration

      CatalogSchema currentCS = loc.getCatalogSchema();
      if (configFile != null) {
        try {
          log.fine("will load configuration");
          this.config = ConfigurationLoader.loadPrimary(baseDir, configFile, adapter, facetNames, currentCS, feedback);
//          log.info("Main Configuration loaded.");
//        } catch (ErrorMessageException e) {
//          if (e.getLocation() != null) {
//            throw new ErrorMessageException("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
//          } else {
//            throw new ErrorMessageException("\n" + e.getMessage());
//          }
//        } catch (FaultException e) {
//          throw new ErrorMessageException("Could not load configuration file " + configFile + " - " + e.getMessage()
//              + ": " + XUtil.trim(e.getCause()));
        } catch (FacetNotFoundException e) {
          throw new ErrorMessageException("facet '" + e.getMessage() + "' not found.");
        } catch (RuntimeException e) {
          log.log(Level.SEVERE, "Could not load default configuration", e);
          throw new ErrorMessageException("Could not load configuration file " + configFile + " - " + e.getMessage()
              + ": " + XUtil.trim(e.getCause()));
        }
      } else {
        log.fine("No config mode");
        try {
          this.config = ConfigurationLoader.prepareNoConfig(baseDir, configFile, adapter, facetNames, currentCS,
              feedback);
          log.fine("Default configuration loaded.");
        } catch (Throwable e) { // Added to display JVM errors, such as JAXB not present (Java 11 and up for Ant
          // generation)
          log.log(Level.SEVERE, "Could not load default configuration", e);
          throw new ErrorMessageException("Could not load configuration file " + configFile + " - " + e.getMessage());
        }
      }
      T.endPhase("Configuration Loaded");

      // Apply current schema to declared tables with no schema and no catalog

      this.config.applyCurrentSchema(this.loc.getCatalogSchema());

      // Discover schemas

      JDBCTag jdbcTag = (JDBCTag) this.config.getGenerators().getSelectedGeneratorTag();
      boolean discover = jdbcTag.getDiscover() != null;
      feedback.info("Discover " + (discover ? "enabled." : "disabled."));
      feedback.info(" ");

      // Database Object Scope

      Set<DatabaseObject> tables = new HashSet<DatabaseObject>();
      for (TableTag t : this.config.getFacetTables()) {
        tables.add(t.getDatabaseObject());
      }
      for (EnumTag e : this.config.getFacetEnums()) {
        tables.add(e.getDatabaseObjectId());
      }

      Set<DatabaseObject> views = new HashSet<DatabaseObject>();
      for (ViewTag v : this.config.getFacetViews()) {
        views.add(v.getDatabaseObjectId());
      }
      T.endPhase("Facets");

//      log.info("db object scope.");

      try {

        if (discover) { // 1. Discover

          List<CatalogSchema> discoverCSs = new ArrayList<>();
          Set<DatabaseObject> excludeIds = new HashSet<>();

          for (SchemaTag s : jdbcTag.getDiscover().getAllSchemaTags()) {
            discoverCSs.add(new CatalogSchema(s.getCanonicalCatalog(), s.getCanonicalSchema()));
            for (ExcludeTag ex : s.getExcludeList()) {
              DatabaseObject id = new DatabaseObject(s.getCanonicalCatalog(), s.getCanonicalSchema(),
                  ex.getCanonicalName());
              excludeIds.add(id);
            }
          }

          this.db = new JdbcDatabase(conn, currentCS, tables, views, discoverCSs, excludeIds, false, false, false,
              logTimes);
          removeCurrentCatalogSchema(currentCS);
          this.config.getFacetTables();// FIXME

          try {
            log.fine("gen 3.1");
            for (JdbcTable t : this.db.getTables()) {
              config.includeInAllFacets(t, false, jdbcTag, config, adapter);
            }
            log.fine("gen 3.2");
            this.config.getFacetTables();// FIXME
            for (JdbcTable v : this.db.getViews()) {
              config.includeInAllFacets(v, true, jdbcTag, config, adapter);
            }
            this.config.getFacetTables();// FIXME
          } catch (InvalidConfigurationFileException e) {
            throw new ErrorMessageException(
                "Could not use a discovered table or view because of its peculiar name. " + e.getMessage());
          }
          this.config.getFacetTables();// FIXME

        } else { // 2. No Discover

          log.fine("gen 4");
          T.endPhase("DB pre-meta");
          this.db = new JdbcDatabase(conn, currentCS, tables, views, false, false, false, logTimes);
          T.endPhase("DB post-meta");
          removeCurrentCatalogSchema(currentCS);
          log.fine("gen 5");

        }
//        } else { // 3. Create View Strategy
//
//          log.fine("gen 6");
//          this.db = new JdbcDatabase(loc, tables, views);
//          removeCurrentCatalogSchema(currentCS);
//          log.fine("gen 7");
//
//        }

        T.endPhase("DB Meta data obtained.");

        log.fine("gen 8");
        this.config.getFacetTables(); // FIXME
        adapter.setCurrentCatalogSchema(conn, loc.getCurrentCatalog(), loc.getCurrentSchema());
        log.fine("gen 9");

      } catch (ReaderException e) {
        throw new ErrorMessageException(e.getMessage());
      } catch (SQLException e) {
        log.log(Level.SEVERE, "Failed to retrieve the database meta data ", e);
        throw new ErrorMessageException("Could not retrieve database metadata - " + XUtil.trim(e));
      } catch (InvalidCatalogSchemaException e) {
        String msg = "Invalid catalog/schema: " + e.getMessage();
        throw new ErrorMessageException(msg);
      } catch (CatalogNotSupportedException e) {
        throw new ErrorMessageException("This database does not support catalogs through the JDBC driver. "
            + "Please specify an empty value for the current catalog property instead of '" + loc.getCurrentCatalog()
            + "'.");
      } catch (InvalidCatalogException e) {
        log.log(Level.SEVERE, "Failed to retrieve the database meta data ", e);
        StringBuilder sb = new StringBuilder();
        if (loc.getCurrentCatalog() == null) {
          sb.append("Please specify a current catalog.\n\n");
        } else {
          sb.append(
              "The specified current catalog '" + loc.getCurrentCatalog() + "' does not exist in this database.\n\n");
        }
        sb.append("The available catalogs are:\n");
        for (String c : e.getExistingCatalogs()) {
          sb.append("  " + c + "\n");
        }
        throw new ErrorMessageException(sb.toString());
      } catch (SchemaNotSupportedException e) {
        throw new ErrorMessageException("This database does not support schemas through the JDBC driver. "
            + "Please specify an empty value for the current schema property instead of '" + loc.getCurrentCatalog()
            + "'.");
      } catch (InvalidSchemaException e) {
        StringBuilder sb = new StringBuilder();
        if (loc.getCurrentSchema() == null) {
          sb.append("Please specify a current schema.\n\n");
        } else {
          sb.append(
              "The specified current schema '" + loc.getCurrentSchema() + "' does not exist in this database.\n\n");
        }
        sb.append("The available schemas are:\n");
        for (String s : e.getExistingSchemas()) {
          sb.append("  " + s + "\n");
        }
        throw new ErrorMessageException(sb.toString());
      } catch (UnsupportedDatabaseException e) {
        throw new ErrorMessageException("This database is not currently supported by " + Constants.TOOL_NAME);
      } catch (DatabaseObjectNotFoundException e) {
        throw new ErrorMessageException(
            "Database object not found. Please check this is the correct database, catalog, and schema: "
                + e.getMessage());
      } catch (RuntimeException e) {
        throw new ErrorMessageException(
            "Could not retrieve database metadata" + (e.getCause() != null ? XUtil.trim(e.getCause()) : XUtil.trim(e)));
      }

      this.metadata = new Metadata(db, adapter, loc);

      T.endPhase("DB Meta Data Post-processing");

      log.fine("gen 10.5");
      this.config.getFacetTables();// FIXME
      log.fine("gen 11");
//      try {
      metadata.load(config, conn, feedback);
      log.fine("gen 12");
//      } catch (InvalidConfigurationFileException e) {
//        log.fine("gen 13");
//        SourceLocation sl = e.getTag() == null ? null : e.getTag().getSourceLocation();
//        if (sl != null) {
//          throw new ErrorMessageException("\n" + e.getMessage() + "\n  in " + sl.render());
//        } else {
//          throw new ErrorMessageException("\n" + e.getMessage());
//        }
//      } catch (FaultException e) {
//        log.fine("gen 14");
//        throw new ErrorMessageException(
//            "Could not retrieve database metadata  - " + e.getMessage() + ": " + XUtil.trim(e.getCause()));
//      } catch (RuntimeException e) {
//        throw new ErrorMessageException(
//            "Could not retrieve database metadata  - " + e.getMessage() + ": " + XUtil.trim(e.getCause()));
//      }
      log.fine("gen 16");
      T.endPhase("Facets Post-processing");

//      for (TableDataSetMetadata t : this.metadata.getTables()) {
//        log.info("* table=" + t.getId());
//        for (ColumnMetadata cm : t.getColumns()) {
//          log.info("** cm=" + cm.getId());
//        }
//      }

    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          // Ignore
        }
      }
    }

  }

  private void removeCurrentCatalogSchema(CatalogSchema currentCS) {
    for (JdbcTable t : this.db.getTables()) {
      if (SUtil.equals(t.getCatalog(), currentCS.getCatalog()) && SUtil.equals(t.getSchema(), currentCS.getSchema())) {
        t.setCatalog(null);
        t.setSchema(null);
      }
    }
    for (JdbcTable t : this.db.getViews()) {
      if (SUtil.equals(t.getCatalog(), currentCS.getCatalog()) && SUtil.equals(t.getSchema(), currentCS.getSchema())) {
        t.setCatalog(null);
        t.setSchema(null);
      }
    }
  }

  // Getters

  public DatabaseLocation getLoc() {
    return loc;
  }

  public DatabaseAdapter getAdapter() {
    return adapter;
  }

  public HotRodConfigTag getConfig() {
    return config;
  }

  public JdbcDatabase getDb() {
    return db;
  }

  public Metadata getMetadata() {
    return metadata;
  }

}
