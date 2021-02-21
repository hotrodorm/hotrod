package org.hotrod.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.Constants;
import org.hotrod.config.DisplayMode;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.runtime.BuildInformation;
import org.hotrod.utils.LocalFileGenerator;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.utils.XUtil;

public class GenOperation {

  private static final Logger log = LogManager.getLogger(GenOperation.class);

  private File baseDir;
  private String configfilename = null;
  private String generatorName = null;
  private String localproperties = null;

  private String jdbcdriverclass = null;
  private String jdbcurl = null;
  private String jdbcusername = null;
  private String jdbcpassword = null;
  private String jdbccatalog = null;
  private String jdbcschema = null;
  private String facets = null;
  private String display = null;

  // Computed properties (during validation)

  private File configFile;
  private DisplayMode displayMode;

  private LinkedHashSet<String> facetNames = null;

  public GenOperation(final File baseDir, final String configfilename, final String generatorName,
      final String localproperties, final String jdbcdriverclass, final String jdbcurl, final String jdbcusername,
      final String jdbcpassword, final String jdbccatalog, final String jdbcschema, final String facets,
      final String display) {
    this.baseDir = baseDir;
    this.configfilename = configfilename;
    this.generatorName = generatorName;
    this.localproperties = localproperties;
    this.jdbcdriverclass = jdbcdriverclass;
    this.jdbcurl = jdbcurl;
    this.jdbcusername = jdbcusername;
    this.jdbcpassword = jdbcpassword;
    this.jdbccatalog = jdbccatalog;
    this.jdbcschema = jdbcschema;
    this.facets = facets;
    this.display = display;
  }

  public void execute(final Feedback feedback) throws Exception {
    log.debug("init");

    feedback.info(Constants.TOOL_NAME + " version " + BuildInformation.VERSION + " (build " + BuildInformation.BUILD_ID
        + ") - Generate");

    validateParameters();

//    feedback.info("");
//    feedback.info("Configuration File: " + this.configFile);
//
//    DatabaseLocation loc = new DatabaseLocation(this.jdbcdriverclass, this.jdbcurl, this.jdbcusername,
//        this.jdbcpassword, this.jdbccatalog, this.jdbcschema, null);
//
//    feedback.info("Database URL: " + loc.getUrl());
//
//    EnabledFKs enabledFKs = EnabledFKs.loadIfPresent(this.baseDir);
//
//    log.debug("FKs Definition loaded.");
//
//    // Retrieve metadata
//
//    Connection conn = null;
    try {
//
//      conn = loc.getConnection();
//      log.debug("Connection open.");
//
//      // Database Version
//
//      DatabaseConnectionVersion cv;
//      try {
//        log.debug("Getting initial metadata.");
//        cv = new DatabaseConnectionVersion(conn.getMetaData());
//        log.debug("Metadata retrieval complete.");
//
//      } catch (SQLException e) {
//        throw new UncontrolledException("Could not retrieve database metadata.", e);
//      }
//      feedback.info("Database Name: " + cv.renderDatabaseName());
//      feedback.info("JDBC Driver: " + cv.renderJDBCDriverName() + " - implements JDBC Specification "
//          + cv.renderJDBCSpecification());
//
//      // Adapter
//
//      DatabaseAdapter adapter;
//      try {
//        adapter = DatabaseAdapterFactory.getAdapter(conn);
//        feedback.info("Database Adapter: " + adapter.getName());
//      } catch (UnrecognizedDatabaseException e) {
//        throw new Exception("Could not recognize database type at JDBC URL " + loc.getUrl() + " - " + e.getMessage());
//      } catch (UncontrolledException e) {
//        Throwable cause = e.getCause();
//        throw new Exception(e.getMessage() + (cause == null ? "" : ": " + cause.getMessage()));
//      } catch (Throwable e) {
//        throw new Exception("Could not connect to database: " + XUtil.abridge(e));
//      }
//      log.debug("Adapter loaded.");
//
//      // Current Catalog & Schema
//
//      feedback.info("");
//      if (adapter.supportsCatalog()) {
//        feedback.info("Current Catalog: " + (loc.getDefaultCatalog() == null ? "" : loc.getDefaultCatalog()));
//      }
//      if (adapter.supportsSchema()) {
//        feedback.info("Current Schema: " + (loc.getDefaultSchema() == null ? "" : loc.getDefaultSchema()));
//      }
//      feedback.info("");
//
//      // Configuration
//
//      HotRodConfigTag config = null;
//      try {
//        config = ConfigurationLoader.loadPrimary(this.baseDir, this.configFile, this.generatorName, adapter,
//            this.facetNames);
//      } catch (ControlledException e) {
//        if (e.getLocation() != null) {
//          throw new Exception("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
//        } else {
//          throw new Exception("\n" + e.getMessage());
//        }
//      } catch (UncontrolledException e) {
//        feedback.error("Technical error found: " + XUtil.abridge(e));
//        throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code.");
//      } catch (FacetNotFoundException e) {
//        throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code: " + "facet '"
//            + e.getMessage() + "' not found.");
//      } catch (Throwable e) {
//        feedback.error("Technical error found: " + XUtil.abridge(e));
//        log.error("Technical error found", e);
//        throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code.");
//      }
//      log.debug("Main Configuration loaded.");
//
//      // Database Object Scope
//
//      Set<DatabaseObject> tables = new HashSet<DatabaseObject>();
//      for (TableTag t : config.getFacetTables()) {
//        tables.add(t.getDatabaseObjectId());
//      }
//      for (EnumTag e : config.getFacetEnums()) {
//        tables.add(e.getDatabaseObjectId());
//      }
//
//      Set<DatabaseObject> views = new HashSet<DatabaseObject>();
//      for (ViewTag v : config.getFacetViews()) {
//        views.add(v.getDatabaseObjectId());
//      }
//
//      JdbcDatabase db = null;
//
//      try {
//
//        db = new JdbcDatabase(loc, tables, views);
//        adapter.setCurrentCatalogSchema(conn, loc.getDefaultCatalog(), loc.getDefaultSchema());
//
//      } catch (ReaderException e) {
//        throw new ControlledException(e.getMessage());
//      } catch (SQLException e) {
//        throw new UncontrolledException("Could not retrieve database metadata.", e);
//      } catch (CatalogNotSupportedException e) {
//        throw new ControlledException("This database does not support catalogs through the JDBC driver. "
//            + "Please specify an empty value for the default catalog property instead of '" + loc.getDefaultCatalog()
//            + "'.");
//      } catch (InvalidCatalogException e) {
//        StringBuilder sb = new StringBuilder();
//        if (loc.getDefaultCatalog() == null) {
//          sb.append("Please specify a default catalog.\n\n");
//        } else {
//          sb.append(
//              "The specified default catalog '" + loc.getDefaultCatalog() + "' does not exist in this database.\n\n");
//        }
//        sb.append("The available catalogs are:\n");
//        for (String c : e.getExistingCatalogs()) {
//          sb.append("  " + c + "\n");
//        }
//        throw new ControlledException(sb.toString());
//      } catch (SchemaNotSupportedException e) {
//        throw new ControlledException("This database does not support schemas through the JDBC driver. "
//            + "Please specify an empty value for the default schema property instead of '" + loc.getDefaultCatalog()
//            + "'.");
//      } catch (InvalidSchemaException e) {
//        StringBuilder sb = new StringBuilder();
//        if (loc.getDefaultSchema() == null) {
//          sb.append("Please specify a default schema.\n\n");
//        } else {
//          sb.append(
//              "The specified default schema '" + loc.getDefaultSchema() + "' does not exist in this database.\n\n");
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
//        throw new UncontrolledException("Could not retrieve database metadata using JDBC URL " + loc.getUrl(), e);
//      }
//
//      HotRodMetadata metadata = new HotRodMetadata(db, adapter, loc);
//      metadata.load(config, loc, conn);

      HotRodContext hc = new HotRodContext(configFile, jdbcdriverclass, jdbcurl, jdbcusername, jdbcpassword,
          jdbccatalog, jdbcschema, generatorName, baseDir, facetNames, feedback);

      // Generate

      Generator g = hc.getConfig().getGenerators().getSelectedGeneratorTag().instantiateGenerator(hc, null,
          this.displayMode, false, feedback);
      log.debug("Generator instantiated.");

      try {

        LiveGenerator liveGenerator = (LiveGenerator) g;

        // a live generator

        g.prepareGeneration();
        FileGenerator fg = new LocalFileGenerator();
        liveGenerator.generate(fg);

      } catch (ClassCastException e) {

        // a batch generator

        g.prepareGeneration();
        g.generate();
      }

      log.debug("Generation complete.");

    } catch (ControlledException e) {
      if (e.getLocation() == null) {
        throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code:\n" + e.getMessage());
      } else {
        throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
            + e.getLocation().render() + ":\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      feedback.error("Technical error found: " + XUtil.abridge(e));
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (InvalidConfigurationFileException e) {
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
          + e.getTag().getSourceLocation().render() + ":\n" + e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code.");
    }

  }

  // Validation

  private void validateParameters() throws Exception {

    // 1. Apply local properties file, if any

    if (!SUtil.isEmpty(this.localproperties)) {

      // 1.a Load local properties

      File p = new File(this.baseDir, this.localproperties);
      if (!p.exists()) {
        throw new Exception(
            Constants.TOOL_NAME + " parameter: " + "localproperties file does not exist: " + this.localproperties);
      }
      if (!p.isFile()) {
        throw new Exception(Constants.TOOL_NAME + " parameter: "
            + "localproperties file exists but it's not a regular file: " + this.localproperties);
      }

      BufferedReader r = null;
      Properties props = null;

      try {
        props = new Properties();
        r = new BufferedReader(new FileReader(p));
        props.load(r);

      } catch (FileNotFoundException e) {
        throw new Exception(
            Constants.TOOL_NAME + " parameter: " + "localproperties file does not exist: " + this.localproperties);

      } catch (IOException e) {
        throw new Exception(Constants.TOOL_NAME + " parameter: " + "localproperties: cannot read file: "
            + e.getMessage() + ": " + this.localproperties);

      } finally {
        if (r != null) {
          try {
            r.close();
          } catch (IOException e) {
            // Swallow this exception
          }
        }
      }

      // 1.b Override default values

      this.configfilename = props.getProperty("configfile", this.configfilename);
      this.generatorName = props.getProperty("generator", this.generatorName);
      this.jdbcdriverclass = props.getProperty("jdbcdriverclass", this.jdbcdriverclass);

      this.jdbcurl = props.getProperty("jdbcurl");
      this.jdbcusername = props.getProperty("jdbcusername");
      this.jdbcpassword = props.getProperty("jdbcpassword");
      this.jdbccatalog = props.getProperty("jdbccatalog");
      this.jdbcschema = props.getProperty("jdbcschema");
      this.facets = props.getProperty("facets");
      this.display = props.getProperty("display");
    }

    // 2. Validate properties

    // configfile

    if (this.configfilename == null) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "configfile attribute must be specified.");
    }
    if (SUtil.isEmpty(this.configfilename)) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "configfile attribute cannot be empty.");
    }
    this.configFile = new File(this.baseDir, this.configfilename);
    if (!this.configFile.exists()) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "configfile does not exist: " + this.configfilename);
    }

    // generator

    if (SUtil.isEmpty(this.generatorName)) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "The attribute 'generator' must be specified.");
    }

    // driverclass

    if (this.jdbcdriverclass == null) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "jdbcdriverclass attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcdriverclass)) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "jdbcdriverclass attribute cannot be empty.");
    }

    // url

    if (this.jdbcurl == null) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "jdbcurl attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcurl)) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "jdbcurl attribute cannot be empty.");
    }

    // username

    if (this.jdbcusername == null) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "jdbcusername attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcusername)) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "jdbcusername attribute cannot be empty.");
    }

    // password

    if (this.jdbcpassword == null) {
      throw new Exception(
          Constants.TOOL_NAME + " parameter: " + "jdbcpassword attribute must be specified, even if empty.");
    }

    // catalog

    if (SUtil.isEmpty(this.jdbccatalog)) {
      this.jdbccatalog = null;
    }

    // schema

    if (SUtil.isEmpty(this.jdbcschema)) {
      this.jdbcschema = null;
    }

    // facets

    this.facetNames = new LinkedHashSet<String>();
    if (!SUtil.isEmpty(this.facets)) {
      for (String facetName : this.facets.split(",")) {
        if (!SUtil.isEmpty(facetName)) {
          this.facetNames.add(facetName.trim());
        }
      }
    }

    // display

    if (SUtil.isEmpty(this.display)) {
      this.displayMode = DisplayMode.LIST;
    } else {
      this.displayMode = DisplayMode.parse(this.display);
      if (this.displayMode == null) {
        throw new Exception(Constants.TOOL_NAME + " parameter: "
            + "If specified, the attribute display must have one of the following values: " + "summary, list");
      }
    }

  }

}