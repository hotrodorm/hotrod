package org.hotrod.generator;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.Constants;
import org.hotrod.config.EnumTag;
import org.hotrod.config.HotRodConfigTag;
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
import org.nocrala.tools.database.tartarus.connectors.DatabaseConnectorFactory.UnsupportedDatabaseException;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.DatabaseObject;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase.DatabaseConnectionVersion;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.DatabaseObjectNotFoundException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.database.tartarus.exception.ReaderException;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;
import org.nocrala.tools.database.tartarus.utils.XUtil;

public class HotRodContext {

  private static final Logger log = LogManager.getLogger(HotRodContext.class);

  private DatabaseLocation loc;
  private DatabaseAdapter adapter;
  private HotRodConfigTag config;
  private JdbcDatabase db;
  private Metadata metadata;

  public HotRodContext(final File configFile, final String jdbcdriverclass, final String jdbcurl,
      final String jdbcusername, final String jdbcpassword, final String jdbccatalog, final String jdbcschema,
      final String generatorName, final File baseDir, final LinkedHashSet<String> facetNames, final Feedback feedback)
      throws ControlledException {

    feedback.info("");
    feedback.info("Configuration File: " + configFile);

    this.loc = new DatabaseLocation(jdbcdriverclass, jdbcurl, jdbcusername, jdbcpassword, jdbccatalog, jdbcschema,
        null);

    feedback.info("Database URL: " + loc.getUrl());

    Connection conn = null;
    try {

      try {
        conn = this.loc.getConnection();
        log.debug("Connection open.");
      } catch (SQLException e) {
        throw new ControlledException("Could not connect to the database: " + XUtil.abridge(e));
      }

      // Database Version

      DatabaseConnectionVersion cv;
      try {
        log.debug("Getting initial metadata.");
        cv = new DatabaseConnectionVersion(conn.getMetaData());
        log.debug("Metadata retrieval complete.");

      } catch (SQLException e) {
        throw new ControlledException("Could not retrieve database metadata: " + XUtil.abridge(e));
      }
      feedback.info("Database Name: " + cv.renderDatabaseName());
      feedback.info("JDBC Driver: " + cv.renderJDBCDriverName() + " - implements JDBC Specification "
          + cv.renderJDBCSpecification());

      // Adapter

      try {
        this.adapter = DatabaseAdapterFactory.getAdapter(conn);
        feedback.info("Database Adapter: " + adapter.getName());
      } catch (UnrecognizedDatabaseException e) {
        throw new ControlledException("Could not identify database at URL " + loc.getUrl() + " - " + e.getMessage());
      } catch (UncontrolledException e) {
        throw new ControlledException("Could not identify database at URL " + loc.getUrl() + " - " + e.getMessage()
            + ": " + XUtil.abridge(e.getCause()));
      } catch (RuntimeException e) {
        throw new ControlledException("Could not identify database at URL " + loc.getUrl() + " - " + XUtil.abridge(e));
      } catch (SQLException e) {
        throw new ControlledException("Could not identify database at URL " + loc.getUrl() + " - " + XUtil.abridge(e));
      }
      log.debug("Adapter loaded.");

      // Current Catalog & Schema

      feedback.info("");
      if (adapter.supportsCatalog()) {
        feedback.info("Current Catalog: " + (loc.getDefaultCatalog() == null ? "" : loc.getDefaultCatalog()));
      }
      if (adapter.supportsSchema()) {
        feedback.info("Current Schema: " + (loc.getDefaultSchema() == null ? "" : loc.getDefaultSchema()));
      }
      feedback.info("");

      // Configuration

      try {
        this.config = ConfigurationLoader.loadPrimary(baseDir, configFile, generatorName, adapter, facetNames);
      } catch (ControlledException e) {
        if (e.getLocation() != null) {
          throw new ControlledException("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
        } else {
          throw new ControlledException("\n" + e.getMessage());
        }
      } catch (UncontrolledException e) {
        throw new ControlledException("Could not load configuration file " + configFile + " - " + e.getMessage() + ": "
            + XUtil.abridge(e.getCause()));
      } catch (FacetNotFoundException e) {
        throw new ControlledException("facet '" + e.getMessage() + "' not found.");
      } catch (RuntimeException e) {
        e.printStackTrace();
        throw new ControlledException("Could not load configuration file " + configFile + " - " + e.getMessage() + ": "
            + XUtil.abridge(e.getCause() == null ? e : e.getCause()));
      }
      log.debug("Main Configuration loaded.");

      // Database Object Scope

      Set<DatabaseObject> tables = new HashSet<DatabaseObject>();
      for (TableTag t : config.getFacetTables()) {
        tables.add(t.getDatabaseObjectId());
      }
      for (EnumTag e : config.getFacetEnums()) {
        tables.add(e.getDatabaseObjectId());
      }

      Set<DatabaseObject> views = new HashSet<DatabaseObject>();
      for (ViewTag v : config.getFacetViews()) {
        views.add(v.getDatabaseObjectId());
      }

      try {

        log.debug("gen 1");
        db = new JdbcDatabase(loc, tables, views);
        log.debug("gen 2");
        adapter.setCurrentCatalogSchema(conn, loc.getDefaultCatalog(), loc.getDefaultSchema());
        log.debug("gen 3");

      } catch (ReaderException e) {
        throw new ControlledException(e.getMessage());
      } catch (SQLException e) {
        throw new ControlledException("Could not retrieve database metadata - " + XUtil.abridge(e));
      } catch (CatalogNotSupportedException e) {
        throw new ControlledException("This database does not support catalogs through the JDBC driver. "
            + "Please specify an empty value for the default catalog property instead of '" + loc.getDefaultCatalog()
            + "'.");
      } catch (InvalidCatalogException e) {
        StringBuilder sb = new StringBuilder();
        if (loc.getDefaultCatalog() == null) {
          sb.append("Please specify a default catalog.\n\n");
        } else {
          sb.append(
              "The specified default catalog '" + loc.getDefaultCatalog() + "' does not exist in this database.\n\n");
        }
        sb.append("The available catalogs are:\n");
        for (String c : e.getExistingCatalogs()) {
          sb.append("  " + c + "\n");
        }
        throw new ControlledException(sb.toString());
      } catch (SchemaNotSupportedException e) {
        throw new ControlledException("This database does not support schemas through the JDBC driver. "
            + "Please specify an empty value for the default schema property instead of '" + loc.getDefaultCatalog()
            + "'.");
      } catch (InvalidSchemaException e) {
        StringBuilder sb = new StringBuilder();
        if (loc.getDefaultSchema() == null) {
          sb.append("Please specify a default schema.\n\n");
        } else {
          sb.append(
              "The specified default schema '" + loc.getDefaultSchema() + "' does not exist in this database.\n\n");
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
        throw new ControlledException("Could not retrieve database metadata" + XUtil.abridge(e.getCause()));
      }

      this.metadata = new Metadata(db, adapter, loc);
      try {
        metadata.load(config, loc, conn);
      } catch (InvalidConfigurationFileException e) {
        SourceLocation sl = e.getTag() == null ? null : e.getTag().getSourceLocation();
        if (sl != null) {
          throw new ControlledException("\n" + e.getMessage() + "\n  in " + sl.render());
        } else {
          throw new ControlledException("\n" + e.getMessage());
        }
      } catch (UncontrolledException e) {
        throw new ControlledException(
            "Could not retrieve database metadata  - " + e.getMessage() + ": " + XUtil.abridge(e.getCause()));
      }

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
