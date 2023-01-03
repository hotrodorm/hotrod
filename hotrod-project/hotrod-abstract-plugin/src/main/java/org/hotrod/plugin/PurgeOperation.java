package org.hotrod.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.Constants;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.DatabaseAdapterFactory;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnrecognizedDatabaseException;
import org.hotrod.generator.Feedback;
import org.hotrod.runtime.BuildInformation;
import org.hotrodorm.hotrod.utils.SUtil;
import org.hotrodorm.hotrod.utils.XUtil;
import org.nocrala.tools.database.tartarus.connectors.DatabaseConnector.IdentifierAdapter;
import org.nocrala.tools.database.tartarus.connectors.DatabaseConnectorFactory.UnsupportedDatabaseException;
import org.nocrala.tools.database.tartarus.connectors.ObjectName;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.DatabaseObject;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.exception.DatabaseObjectNotFoundException;
import org.nocrala.tools.database.tartarus.exception.ReaderException;

public class PurgeOperation {

  private static final Logger log = LogManager.getLogger(PurgeOperation.class);

  private File baseDir;
  private String configfilename = null;
  private String localproperties = null;

  private String jdbcdriverclass = null;
  private String jdbcurl = null;
  private String jdbcusername = null;
  private String jdbcpassword = null;
  private String jdbccatalog = null;
  private String jdbcschema = null;

  // Computed properties (during validation)

  private File configFile;

  public PurgeOperation(final File baseDir, final String configfilename, final String localproperties,
      final String jdbcdriverclass, final String jdbcurl, final String jdbcusername, final String jdbcpassword,
      final String jdbccatalog, final String jdbcschema) {
    this.baseDir = baseDir;
    this.configfilename = configfilename;
    this.localproperties = localproperties;
    this.jdbcdriverclass = jdbcdriverclass;
    this.jdbcurl = jdbcurl;
    this.jdbcusername = jdbcusername;
    this.jdbcpassword = jdbcpassword;
    this.jdbccatalog = jdbccatalog;
    this.jdbcschema = jdbcschema;
  }

  public void execute(final Feedback feedback) throws OperationException {
    log.debug("init");

    feedback.info(Constants.TOOL_NAME + " version " + BuildInformation.VERSION + " (build " + BuildInformation.BUILD_ID
        + ") - Purge");

    validateParameters();

    feedback.info("");
    feedback.info("Configuration File: " + this.configFile);

    DatabaseLocation loc = new DatabaseLocation(this.jdbcdriverclass, this.jdbcurl, this.jdbcusername,
        this.jdbcpassword, this.jdbccatalog, this.jdbcschema, null);

    DatabaseAdapter adapter;
    try {
      adapter = DatabaseAdapterFactory.getAdapter(loc);
      feedback.info("Database Adapter: " + adapter.getName());
    } catch (UnrecognizedDatabaseException e) {
      throw new OperationException(
          "Could not recognize database type at JDBC URL " + loc.getUrl() + " - " + e.getMessage());
    } catch (UncontrolledException e) {
      Throwable cause = e.getCause();
      throw new OperationException(e.getMessage() + (cause == null ? "" : ": " + cause.getMessage()));
    } catch (Throwable e) {
      throw new OperationException("Could not connect to database: " + XUtil.renderThrowable(e));
    }

    log.debug("Adapter loaded.");

    HotRodConfigTag config = null;
    try {
      config = ConfigurationLoader.loadPrimary(this.baseDir, this.configFile, adapter, new LinkedHashSet<String>(),
          loc.getCatalogSchema());
    } catch (ControlledException e) {
      if (e.getLocation() != null) {
        throw new OperationException("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
      } else {
        throw new OperationException("\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      feedback.error("Technical error found: " + XUtil.renderThrowable(e));
      throw new OperationException(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (Throwable e) {
      feedback.error("Technical error found: " + XUtil.renderThrowable(e));
      log.error("Technical error found", e);
      throw new OperationException(Constants.TOOL_NAME + " could not generate the persistence code.");
    }

    log.debug("Configuration loaded.");

    try (Connection conn = loc.getConnection()) {

      JdbcDatabase db = null;
      try {
        db = new JdbcDatabase(conn, loc.getCatalogSchema(), new HashSet<DatabaseObject>(),
            new HashSet<DatabaseObject>());
      } catch (ReaderException | UnsupportedDatabaseException | DatabaseObjectNotFoundException e) {
        throw new OperationException("Could 1 not retrieve the list of temp views:" + XUtil.renderThrowable(e));
      }

      IdentifierAdapter identifierAdapter = db.getDatabaseConnector().getIdentifierAdapter();
      String baseName = config.getGenerators().getSelectedGeneratorTag().getSelectGeneration().getTempViewBaseName();

      List<ObjectName> tempViews;
      try {
        tempViews = db.findViews(baseName + "%");
      } catch (SQLException e) {
        throw new OperationException("Could 2 not retrieve the list of temp views:" + XUtil.renderThrowable(e));
      }
      if (tempViews.isEmpty()) {
        feedback.info("No temp views found. Nothing to drop.");
      } else {
        feedback.info("" + tempViews.size() + " temp view" + (tempViews.size() == 1 ? "" : "s") + " to drop:");
      }

      for (ObjectName v : tempViews) {
        try {
          db.dropView(v);
          feedback.info(" - View " + identifierAdapter.renderSQL(v) + " dropped.");
        } catch (SQLException e) {
          feedback.info(" - Could not drop view " + identifierAdapter.renderSQL(v) + ": " + XUtil.renderThrowable(e));
        }
      }

    } catch (SQLException e) {
      throw new OperationException("Could not connect to database: " + XUtil.renderThrowable(e));
    }

  }

  // Validation

  private void validateParameters() throws OperationException {

    // 1. Apply local properties file, if any

    if (!SUtil.isEmpty(this.localproperties)) {

      // 1.a Load local properties

      File p = new File(this.baseDir, this.localproperties);
      if (!p.exists()) {
        throw new OperationException(
            Constants.TOOL_NAME + " parameter: " + "localproperties file does not exist: " + this.localproperties);
      }
      if (!p.isFile()) {
        throw new OperationException(Constants.TOOL_NAME + " parameter: "
            + "localproperties file exists but it's not a regular file: " + this.localproperties);
      }

      BufferedReader r = null;
      Properties props = null;

      try {
        props = new Properties();
        r = new BufferedReader(new FileReader(p));
        props.load(r);

      } catch (FileNotFoundException e) {
        throw new OperationException(
            Constants.TOOL_NAME + " parameter: " + "localproperties file does not exist: " + this.localproperties);

      } catch (IOException e) {
        throw new OperationException(Constants.TOOL_NAME + " parameter: " + "localproperties: cannot read file: "
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
      this.jdbcdriverclass = props.getProperty("jdbcdriverclass", this.jdbcdriverclass);

      this.jdbcurl = props.getProperty("jdbcurl");
      this.jdbcusername = props.getProperty("jdbcusername");
      this.jdbcpassword = props.getProperty("jdbcpassword");
      this.jdbccatalog = props.getProperty("jdbccatalog");
      this.jdbcschema = props.getProperty("jdbcschema");
    }

    // 2. Validate properties

    // configfile

    if (this.configfilename == null) {
      throw new OperationException(Constants.TOOL_NAME + " parameter: " + "configfile attribute must be specified.");
    }
    if (SUtil.isEmpty(this.configfilename)) {
      throw new OperationException(Constants.TOOL_NAME + " parameter: " + "configfile attribute cannot be empty.");
    }
    this.configFile = new File(this.baseDir, this.configfilename);
    if (!this.configFile.exists()) {
      throw new OperationException(
          Constants.TOOL_NAME + " parameter: " + "configfile does not exist: " + this.configfilename);
    }

    // driverclass

    if (this.jdbcdriverclass == null) {
      throw new OperationException(
          Constants.TOOL_NAME + " parameter: " + "jdbcdriverclass attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcdriverclass)) {
      throw new OperationException(Constants.TOOL_NAME + " parameter: " + "jdbcdriverclass attribute cannot be empty.");
    }

    // url

    if (this.jdbcurl == null) {
      throw new OperationException(Constants.TOOL_NAME + " parameter: " + "jdbcurl attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcurl)) {
      throw new OperationException(Constants.TOOL_NAME + " parameter: " + "jdbcurl attribute cannot be empty.");
    }

    // username

    if (this.jdbcusername == null) {
      throw new OperationException(Constants.TOOL_NAME + " parameter: " + "jdbcusername attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcusername)) {
      throw new OperationException(Constants.TOOL_NAME + " parameter: " + "jdbcusername attribute cannot be empty.");
    }

    // password

    if (this.jdbcpassword == null) {
      throw new OperationException(
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

  }

}