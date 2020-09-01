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
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.Constants;
import org.hotrod.config.DisplayMode;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.DatabaseAdapterFactory;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnrecognizedDatabaseException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.BuildInformation;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.utils.XUtil;

public abstract class AbstractExportColumnsOperation {

  private static final Logger log = LogManager.getLogger(AbstractExportColumnsOperation.class);

  protected File baseDir;
  protected String configfilename = null;
  protected String generator = null;
  protected String localproperties = null;

  protected String jdbcdriverclass = null;
  protected String jdbcurl = null;
  protected String jdbcusername = null;
  protected String jdbcpassword = null;
  protected String jdbccatalog = null;
  protected String jdbcschema = null;
  protected String facets = null;
  protected String display = null;
  protected String exportfilename = null;

  // Computed properties (during validation)

  protected File configFile;
  protected DisplayMode displayMode;
  protected File exportFile;

  private LinkedHashSet<String> facetNames = null;

  protected AbstractExportColumnsOperation(final File baseDir, final String configfilename, final String generator,
      final String localproperties, final String jdbcdriverclass, final String jdbcurl, final String jdbcusername,
      final String jdbcpassword, final String jdbccatalog, final String jdbcschema, final String facets,
      final String display, final String exportfilename) {
    log.info("exportfilename=" + exportfilename);
    this.baseDir = baseDir;
    this.configfilename = configfilename;
    this.generator = generator;
    this.localproperties = localproperties;
    this.jdbcdriverclass = jdbcdriverclass;
    this.jdbcurl = jdbcurl;
    this.jdbcusername = jdbcusername;
    this.jdbcpassword = jdbcpassword;
    this.jdbccatalog = jdbccatalog;
    this.jdbcschema = jdbcschema;
    this.facets = facets;
    this.display = display;
    this.exportfilename = exportfilename;
  }

  public final void execute(final Feedback feedback) throws Exception {
    log.debug("init");

    feedback.info(Constants.TOOL_NAME + " version " + BuildInformation.VERSION + " (build " + BuildInformation.BUILD_ID
        + ") - Export Columns TXT");

    validateParameters(feedback);

    feedback.info(" ");
    feedback.info("Configuration File: " + this.configFile);

    DatabaseLocation loc = new DatabaseLocation(this.jdbcdriverclass, this.jdbcurl, this.jdbcusername,
        this.jdbcpassword, this.jdbccatalog, this.jdbcschema, null);

    DatabaseAdapter adapter;
    try {
      adapter = DatabaseAdapterFactory.getAdapter(loc);
      feedback.info("HotRod Database Adapter: " + adapter.getName());
    } catch (UnrecognizedDatabaseException e) {
      throw new Exception("Could not recognize database type at JDBC URL " + loc.getUrl() + " - " + e.getMessage());
    } catch (UncontrolledException e) {
      Throwable cause = e.getCause();
      throw new Exception(e.getMessage() + (cause == null ? "" : ": " + cause.getMessage()));
    } catch (Throwable e) {
      throw new Exception("Could not connect to database: " + XUtil.abridge(e));
    }

    log.debug("Adapter loaded.");

    HotRodConfigTag config = null;
    try {
      config = ConfigurationLoader.loadPrimary(this.baseDir, this.configFile, this.generator, adapter,
          new LinkedHashSet<String>());
    } catch (ControlledException e) {
      if (e.getLocation() != null) {
        throw new Exception("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
      } else {
        throw new Exception("\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      feedback.error("Technical error found: " + XUtil.abridge(e));
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (FacetNotFoundException e) {
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code: " + "facet '"
          + e.getMessage() + "' not found.");
    } catch (Throwable e) {
      feedback.error("Technical error found: " + XUtil.abridge(e));
      log.error("Technical error found", e);
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code.");
    }

    log.debug("Configuration loaded.");

    try {
      CachedMetadata cachedMetadata = new CachedMetadata();
      HotRodGenerator g = config.getGenerators().getSelectedGeneratorTag().instantiateGenerator(cachedMetadata, loc,
          config, this.displayMode, false, adapter, feedback);
      log.debug("Generator instantiated.");

      g.prepareGeneration();
      exportColumns(g);

      feedback.info("Column export saved to: " + this.exportFile);

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

  protected abstract void exportColumns(final HotRodGenerator g) throws IOException;

  // Validation

  private final void validateParameters(final Feedback feedback) throws Exception {

    // 1. Apply local properties file, if any

    if (!SUtil.isEmpty(this.localproperties)) {

      feedback.info("Loading local properties from: " + this.localproperties);

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
      this.generator = props.getProperty("generator", this.generator);
      this.jdbcdriverclass = props.getProperty("jdbcdriverclass", this.jdbcdriverclass);

      this.jdbcurl = props.getProperty("jdbcurl");
      this.jdbcusername = props.getProperty("jdbcusername");
      this.jdbcpassword = props.getProperty("jdbcpassword");
      this.jdbccatalog = props.getProperty("jdbccatalog");
      this.jdbcschema = props.getProperty("jdbcschema");
      this.facets = props.getProperty("facets");
      this.display = props.getProperty("display");
      this.exportfilename = props.getProperty("exportfile", this.exportfilename);

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

    if (SUtil.isEmpty(this.generator)) {
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

    // Export File Name

    if (this.exportfilename == null) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "exportfilename attribute must be specified.");
    }
    if (SUtil.isEmpty(this.exportfilename)) {
      throw new Exception(Constants.TOOL_NAME + " parameter: " + "exportfilename attribute cannot be empty.");
    }
    this.exportFile = new File(this.baseDir, this.exportfilename);

  }

}