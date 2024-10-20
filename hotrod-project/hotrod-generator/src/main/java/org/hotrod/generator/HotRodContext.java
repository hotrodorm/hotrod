package org.hotrod.generator;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.Constants;
import org.hotrod.config.DaosTag;
import org.hotrod.config.EnumTag;
import org.hotrod.config.ExcludeTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.MyBatisSpringTag;
import org.hotrod.config.SchemaTag;
import org.hotrod.config.SelectGenerationTag.SelectStrategy;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.DatabaseAdapterFactory;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnrecognizedDatabaseException;
import org.hotrod.metadata.Metadata;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrodorm.hotrod.utils.SUtil;
import org.hotrodorm.hotrod.utils.XUtil;
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

  private static final Logger log = LogManager.getLogger(HotRodContext.class);

  private DatabaseLocation loc;
  private DatabaseAdapter adapter;
  private HotRodConfigTag config;
  private JdbcDatabase db;
  private Metadata metadata;

  public HotRodContext(final File configFile, final String jdbcdriverclass, final String jdbcurl,
      final String jdbcusername, final String jdbcpassword, final String currentJDBCCatalog,
      final String currentJDBCSchema, final File baseDir, final LinkedHashSet<String> facetNames,
      final Feedback feedback) throws ControlledException {

    if (configFile != null) {
      feedback.info("");
      feedback.info("Configuration File: " + configFile);
    }

    this.loc = new DatabaseLocation(jdbcdriverclass, jdbcurl, jdbcusername, jdbcpassword, currentJDBCCatalog,
        currentJDBCSchema, null);

    feedback.info("Database URL: " + loc.getUrl());

    Connection conn = null;
    try {

      try {
        conn = this.loc.getConnection();
        log.debug("Connection open.");
      } catch (SQLException e) {
        throw new ControlledException("Could not connect to the database: " + XUtil.trim(e));
      }

      // Database Version

      DatabaseConnectionVersion cv;
      try {
        log.debug("Getting initial metadata.");
        cv = new DatabaseConnectionVersion(conn.getMetaData());
        log.debug("Metadata retrieval complete.");

      } catch (SQLException e) {
        throw new ControlledException("Could not retrieve database metadata: " + XUtil.trim(e));
      }
      feedback.info("Database Name: " + cv.renderDatabaseName());
      feedback.info("JDBC Driver: " + cv.renderJDBCDriverName() + " - implements JDBC Specification "
          + cv.renderJDBCSpecification());

      // Adapter

      try {
        this.adapter = DatabaseAdapterFactory.getAdapter(conn);
        feedback.info("HotRod Adapter: " + adapter.getName());
      } catch (UnrecognizedDatabaseException e) {
        throw new ControlledException("Could not identify database at URL " + loc.getUrl() + " - " + e.getMessage());
      } catch (UncontrolledException e) {
        throw new ControlledException("Could not identify database at URL " + loc.getUrl() + " - " + e.getMessage()
            + ": " + XUtil.trim(e.getCause()));
      } catch (RuntimeException e) {
        throw new ControlledException("Could not identify database at URL " + loc.getUrl() + " - " + XUtil.trim(e));
      } catch (SQLException e) {
        throw new ControlledException("Could not identify database at URL " + loc.getUrl() + " - " + XUtil.trim(e));
      }
      log.debug("Adapter loaded.");

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
          this.config = ConfigurationLoader.loadPrimary(baseDir, configFile, adapter, facetNames, currentCS);
          log.debug("Main Configuration loaded.");
        } catch (ControlledException e) {
          if (e.getLocation() != null) {
            throw new ControlledException("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
          } else {
            throw new ControlledException("\n" + e.getMessage());
          }
        } catch (UncontrolledException e) {
          throw new ControlledException("Could not load configuration file " + configFile + " - " + e.getMessage()
              + ": " + XUtil.trim(e.getCause()));
        } catch (FacetNotFoundException e) {
          throw new ControlledException("facet '" + e.getMessage() + "' not found.");
        } catch (RuntimeException e) {
          throw new ControlledException("Could not load configuration file " + configFile + " - " + e.getMessage()
              + ": " + XUtil.trim(e.getCause()));
        } catch (Throwable e) { // Added to display JVM errors, such as JAXB not present (Java 11 and up for Ant
                                // generation)
          log.error("Could not load the configuration", e);
          throw new ControlledException("Could not load configuration file " + configFile + " - " + e.getMessage());
        }
      } else {
        log.debug("Will load 3");
        try {
          this.config = ConfigurationLoader.prepareNoConfig(baseDir, configFile, adapter, facetNames, currentCS);
          log.debug("Default configuration loaded.");
        } catch (Throwable e) { // Added to display JVM errors, such as JAXB not present (Java 11 and up for Ant
          // generation)
          log.error("Could not load default configuration", e);
          throw new ControlledException("Could not load configuration file " + configFile + " - " + e.getMessage());
        }
      }

      // Apply current schema to declared tables with no schema and no catalog

      this.config.applyCurrentSchema(this.loc.getCatalogSchema());

      // Discover schemas

      MyBatisSpringTag mst = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();
      boolean discover = mst.getDiscover() != null;
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

      try {

        log.debug("gen 1");
        if (mst.getSelectGeneration().getStrategy() == SelectStrategy.RESULT_SET) {
          if (discover) { // 1. Discover

            List<CatalogSchema> discoverCSs = new ArrayList<>();
            Set<DatabaseObject> excludeIds = new HashSet<>();

            for (SchemaTag s : mst.getDiscover().getAllSchemaTags()) {
              discoverCSs.add(new CatalogSchema(s.getCanonicalCatalog(), s.getCanonicalSchema()));
              for (ExcludeTag ex : s.getExcludeList()) {
                DatabaseObject id = new DatabaseObject(s.getCanonicalCatalog(), s.getCanonicalSchema(),
                    ex.getCanonicalName());
                log.debug("-----> exclude: " + id);
                excludeIds.add(id);
              }
            }

            log.debug("gen 2");
            this.db = new JdbcDatabase(conn, currentCS, tables, views, discoverCSs, excludeIds);
            removeCurrentCatalogSchema(currentCS);
            log.debug("gen 3");
            this.config.getFacetTables();// FIXME

            DaosTag daosTag = mst.getDaos();
            try {
              log.debug("gen 3.1");
              for (JdbcTable t : this.db.getTables()) {
                config.includeInAllFacets(t, false, daosTag, config, adapter);
              }
              log.debug("gen 3.2");
              this.config.getFacetTables();// FIXME
              for (JdbcTable v : this.db.getViews()) {
                config.includeInAllFacets(v, true, daosTag, config, adapter);
              }
              this.config.getFacetTables();// FIXME
            } catch (InvalidConfigurationFileException e) {
              throw new ControlledException(
                  "Could not use a discovered table or view because of its peculiar name. " + e.getMessage());
            }
            this.config.getFacetTables();// FIXME

          } else { // 2. No Discover

            log.debug("gen 4");
            this.db = new JdbcDatabase(conn, currentCS, tables, views);
            removeCurrentCatalogSchema(currentCS);
            log.debug("gen 5");

          }
        } else { // 3. Create View Strategy

          log.debug("gen 6");
          this.db = new JdbcDatabase(loc, tables, views);
          removeCurrentCatalogSchema(currentCS);
          log.debug("gen 7");

        }

        log.debug("gen 8");
        this.config.getFacetTables(); // FIXME
        adapter.setCurrentCatalogSchema(conn, loc.getCurrentCatalog(), loc.getCurrentSchema());
        log.debug("gen 9");

      } catch (ReaderException e) {
        throw new ControlledException(e.getMessage());
      } catch (SQLException e) {
        e.printStackTrace();
        throw new ControlledException("Could not retrieve database metadata - " + XUtil.trim(e));
      } catch (InvalidCatalogSchemaException e) {
        String msg = "Invalid catalog/schema: " + e.getMessage();
        throw new ControlledException(msg);
      } catch (CatalogNotSupportedException e) {
        throw new ControlledException("This database does not support catalogs through the JDBC driver. "
            + "Please specify an empty value for the current catalog property instead of '" + loc.getCurrentCatalog()
            + "'.");
      } catch (InvalidCatalogException e) {
        e.printStackTrace();
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
        throw new ControlledException(sb.toString());
      } catch (SchemaNotSupportedException e) {
        throw new ControlledException("This database does not support schemas through the JDBC driver. "
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
        throw new ControlledException(sb.toString());
      } catch (UnsupportedDatabaseException e) {
        throw new ControlledException("This database is not currently supported by " + Constants.TOOL_NAME);
      } catch (DatabaseObjectNotFoundException e) {
        throw new ControlledException(
            "Database object not found. Please check this is the correct database, catalog, and schema: "
                + e.getMessage());
      } catch (RuntimeException e) {
        throw new ControlledException(
            "Could not retrieve database metadata" + (e.getCause() != null ? XUtil.trim(e.getCause()) : XUtil.trim(e)));
      }

      log.debug("gen 10");
      this.metadata = new Metadata(db, adapter, loc);

      log.debug("gen 10.5");
      this.config.getFacetTables();// FIXME
      log.debug("gen 11");
      try {
        metadata.load(config, loc, conn);
        log.debug("gen 12");
      } catch (InvalidConfigurationFileException e) {
        log.debug("gen 13");
        SourceLocation sl = e.getTag() == null ? null : e.getTag().getSourceLocation();
        if (sl != null) {
          throw new ControlledException("\n" + e.getMessage() + "\n  in " + sl.render());
        } else {
          throw new ControlledException("\n" + e.getMessage());
        }
      } catch (UncontrolledException e) {
        log.debug("gen 14");
        throw new ControlledException(
            "Could not retrieve database metadata  - " + e.getMessage() + ": " + XUtil.trim(e.getCause()));
      } catch (Throwable e) {
        log.debug("gen 15");
        e.printStackTrace();
        throw new ControlledException(
            "Could not retrieve database metadata  - " + e.getMessage() + ": " + XUtil.trim(e.getCause()));
      }
      log.debug("gen 16");

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
