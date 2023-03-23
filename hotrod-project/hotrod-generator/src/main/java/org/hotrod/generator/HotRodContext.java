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
import org.hotrod.config.DaosSpringMyBatisTag;
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

    feedback.info("");
    feedback.info("Configuration File: " + configFile);

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

      feedback.info("");
      if (adapter.supportsCatalog()) {
        feedback.info("Current Catalog: " + (loc.getDefaultCatalog() == null ? "" : loc.getDefaultCatalog()));
      }
      if (adapter.supportsSchema()) {
        feedback.info("Current Schema: " + (loc.getDefaultSchema() == null ? "" : loc.getDefaultSchema()));
      }
      feedback.info("");

      // Loading Configuration

      try {
        this.config = ConfigurationLoader.loadPrimary(baseDir, configFile, adapter, facetNames, loc.getCatalogSchema());
      } catch (ControlledException e) {
        if (e.getLocation() != null) {
          throw new ControlledException("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
        } else {
          throw new ControlledException("\n" + e.getMessage());
        }
      } catch (UncontrolledException e) {
        throw new ControlledException("Could not load configuration file " + configFile + " - " + e.getMessage() + ": "
            + XUtil.trim(e.getCause()));
      } catch (FacetNotFoundException e) {
        throw new ControlledException("facet '" + e.getMessage() + "' not found.");
      } catch (RuntimeException e) {
        throw new ControlledException("Could not load configuration file " + configFile + " - " + e.getMessage() + ": "
            + XUtil.trim(e.getCause()));
      }
      log.debug("Main Configuration loaded.");

      MyBatisSpringTag mst = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();
      boolean discover = mst.getDiscover() != null;
      feedback.info("Discover " + (discover ? "enabled." : "disabled."));
      feedback.info(" ");

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
        if (mst.getSelectGeneration().getStrategy() == SelectStrategy.RESULT_SET) {
          if (discover) {

            List<CatalogSchema> discoverCSs = new ArrayList<>();
            Set<DatabaseObject> excludeIds = new HashSet<>();

            for (SchemaTag s : mst.getDiscover().getAllSchemaTags()) {
              discoverCSs.add(new CatalogSchema(s.getCanonicalCatalog(), s.getCanonicalSchema()));
              for (ExcludeTag ex : s.getExcludeList()) {
                DatabaseObject id = new DatabaseObject(s.getCanonicalCatalog(), s.getCanonicalSchema(), ex.getCanonicalName());
                log.debug("-----> exclude: " + id);
                excludeIds.add(id);
              }
            }

            log.debug("gen 2");
            this.db = new JdbcDatabase(conn, loc.getCatalogSchema(), tables, views, discoverCSs, excludeIds);
            log.debug("gen 3");

            DaosSpringMyBatisTag daosTag = mst.getDaos();
            try {
              for (JdbcTable t : this.db.getTables()) {
                config.includeInAllFacets(t, false, daosTag, config, adapter);
              }
              for (JdbcTable v : this.db.getViews()) {
                config.includeInAllFacets(v, true, daosTag, config, adapter);
              }
            } catch (InvalidConfigurationFileException e) {
              throw new ControlledException(
                  "Could not use a discovered table or view because of its peculiar name. " + e.getMessage());
            }

          } else {

            log.debug("gen 4");
            this.db = new JdbcDatabase(conn, loc.getCatalogSchema(), tables, views);
            log.debug("gen 5");

          }
        } else {

          log.debug("gen 6");
          this.db = new JdbcDatabase(loc, tables, views);
          log.debug("gen 7");

        }

        log.debug("gen 8");
        adapter.setCurrentCatalogSchema(conn, loc.getDefaultCatalog(), loc.getDefaultSchema());
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
            + "Please specify an empty value for the default catalog property instead of '" + loc.getDefaultCatalog()
            + "'.");
      } catch (InvalidCatalogException e) {
        e.printStackTrace();
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
        throw new ControlledException(
            "Could not retrieve database metadata" + (e.getCause() != null ? XUtil.trim(e.getCause()) : XUtil.trim(e)));
      }

      log.debug("gen 10");
      this.metadata = new Metadata(db, adapter, loc);
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
