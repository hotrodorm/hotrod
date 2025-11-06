package org.hotrod.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.logging.Logger;

import org.hotrod.BuildInformation;
import org.hotrod.config.Constants;
import org.hotrod.config.DisplayMode;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.T;

public abstract class AbstractExportColumnsOperation {

  private static final Logger log = Logger.getLogger(AbstractExportColumnsOperation.class.getName());

  protected File baseDir;
  protected String configfilename = null;
  protected String localproperties = null;

  protected String jdbcdriverclass = null;
  protected String jdbcurl = null;
  protected String jdbcusername = null;
  protected String jdbcpassword = null;
  protected String jdbccatalog = null;
  protected String jdbcschema = null;
  protected String facets = null;
  protected String display = null;
  protected String exportfilenameProperty = null;
  protected String exportfilename = null;

  // Computed properties (during validation)

  protected File configFile;
  protected DisplayMode displayMode;
  protected File exportFile;

  private LinkedHashSet<String> facetNames = null;

  protected AbstractExportColumnsOperation(final File baseDir, final String configfilename,
      final String localproperties, final String jdbcdriverclass, final String jdbcurl, final String jdbcusername,
      final String jdbcpassword, final String jdbccatalog, final String jdbcschema, final String facets,
      final String display, final String exportfilenameProperty, final String exportfilename) {
    log.fine("exportfilename=" + exportfilename);
    this.baseDir = baseDir;
    this.configfilename = configfilename;
    this.localproperties = localproperties;
    this.jdbcdriverclass = jdbcdriverclass;
    this.jdbcurl = jdbcurl;
    this.jdbcusername = jdbcusername;
    this.jdbcpassword = jdbcpassword;
    this.jdbccatalog = jdbccatalog;
    this.jdbcschema = jdbcschema;
    this.facets = facets;
    this.display = display;
    this.exportfilenameProperty = exportfilenameProperty;
    this.exportfilename = exportfilename;

    boolean logTimes = "true".equals(System.getProperty("timers"));
    T.start(logTimes);
  }

  public final void execute(final Feedback feedback) throws ErrorMessageException, FaultException {
    log.fine("init");

    feedback.info(Constants.TOOL_NAME + " version " + BuildInformation.VERSION + " (build " + BuildInformation.BUILD_ID
        + ") - Export Columns TXT");

    validateParameters(feedback);

    HotRodContext hc = new HotRodContext(configFile, jdbcdriverclass, jdbcurl, jdbcusername, jdbcpassword, jdbccatalog,
        jdbcschema, baseDir, facetNames, feedback, false);

    try {

      Generator g = hc.getConfig().getGenerators().getSelectedGeneratorTag().instantiateGenerator(hc, null,
          this.displayMode, false, feedback);
      log.fine("Generator instantiated.");

      g.prepareGeneration();
      exportColumns(g);

      feedback.info("Column export saved to: " + this.exportFile);

    } catch (InvalidConfigurationFileException e) {
      throw new ErrorMessageException(e.getTag(),
          Constants.TOOL_NAME + " could not load the configuration. Invalid configuration in "
              + e.getTag().getSourceLocation().render() + ":\n" + e.getMessage());
    } catch (RuntimeException e) {
      throw new FaultException(Constants.TOOL_NAME + " could not load the configuration.", e);
    }

  }

  protected abstract void exportColumns(final Generator g) throws ErrorMessageException, FaultException;

  // Validation

  private final void validateParameters(final Feedback feedback) throws ErrorMessageException, FaultException {

    // 1. Apply local properties file, if any

    if (!SUtil.isEmpty(this.localproperties)) {

      feedback.info("Loading local properties from: " + this.localproperties);

      // 1.a Load local properties

      File p = new File(this.baseDir, this.localproperties);
      if (!p.exists()) {
        throw new ErrorMessageException(
            Constants.TOOL_NAME + " parameter: " + "localproperties file does not exist: " + this.localproperties);
      }
      if (!p.isFile()) {
        throw new ErrorMessageException(Constants.TOOL_NAME + " parameter: "
            + "localproperties file exists but it's not a regular file: " + this.localproperties);
      }

      Properties props = new Properties();
      try (BufferedReader r = new BufferedReader(new FileReader(p))) {
        props.load(r);
      } catch (FileNotFoundException e) {
        throw new FaultException(
            Constants.TOOL_NAME + " parameter: " + "localproperties file does not exist: " + this.localproperties, e);
      } catch (IOException e) {
        throw new FaultException(Constants.TOOL_NAME + " parameter: " + "localproperties: cannot read file: "
            + e.getMessage() + ": " + this.localproperties, e);
      }

      // 1.b Override default values

      this.configfilename = props.getProperty("configfile", this.configfilename);
      this.jdbcdriverclass = props.getProperty("jdbcdriverclass", this.jdbcdriverclass);

      this.jdbcurl = props.getProperty("jdbcurl");
      this.jdbcusername = props.getProperty("jdbcusername");
      this.jdbcpassword = props.getProperty("jdbcpassword");
      this.jdbccatalog = props.getProperty("jdbccatalog");
      this.jdbcschema = props.getProperty("jdbcschema");
      this.facets = props.getProperty("facets");
      this.display = props.getProperty("display");
      this.exportfilename = props.getProperty(this.exportfilenameProperty, this.exportfilename);

    }

    // 2. Validate properties

    // configfile

    if (this.configfilename == null) {
      this.configFile = null;
    } else {
      if (SUtil.isEmpty(this.configfilename)) {
        throw new ErrorMessageException(Constants.TOOL_NAME + " parameter: " + "configfile attribute cannot be empty.");
      }
      this.configFile = new File(this.baseDir, this.configfilename);
      if (!this.configFile.exists()) {
        throw new ErrorMessageException(
            Constants.TOOL_NAME + " parameter: " + "configfile does not exist: " + this.configfilename);
      }
    }

    // driverclass

    if (this.jdbcdriverclass == null) {
      throw new ErrorMessageException(
          Constants.TOOL_NAME + " parameter: " + "jdbcdriverclass attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcdriverclass)) {
      throw new ErrorMessageException(
          Constants.TOOL_NAME + " parameter: " + "jdbcdriverclass attribute cannot be empty.");
    }

    // url

    if (this.jdbcurl == null) {
      throw new ErrorMessageException(Constants.TOOL_NAME + " parameter: " + "jdbcurl attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcurl)) {
      throw new ErrorMessageException(Constants.TOOL_NAME + " parameter: " + "jdbcurl attribute cannot be empty.");
    }

    // username

    if (this.jdbcusername == null) {
      throw new ErrorMessageException(
          Constants.TOOL_NAME + " parameter: " + "jdbcusername attribute must be specified.");
    }
    if (SUtil.isEmpty(this.jdbcusername)) {
      throw new ErrorMessageException(Constants.TOOL_NAME + " parameter: " + "jdbcusername attribute cannot be empty.");
    }

    // password

    if (this.jdbcpassword == null) {
      throw new ErrorMessageException(
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
        throw new ErrorMessageException(Constants.TOOL_NAME + " parameter: "
            + "If specified, the attribute display must have one of the following values: " + "summary, list");
      }
    }

    // Export File Name

    if (this.exportfilename == null) {
      throw new ErrorMessageException(Constants.TOOL_NAME + " parameter: The '" + this.exportfilenameProperty
          + "' attribute must be specified (in the Maven plugin configuration, Ant plugin, or other).");
    }
    if (SUtil.isEmpty(this.exportfilename)) {
      throw new ErrorMessageException(Constants.TOOL_NAME + " parameter: The '" + this.exportfilenameProperty
          + "' attribute cannot be empty (in the Maven plugin configuration, Ant plugin, or other).");
    }
    this.exportFile = new File(this.baseDir, this.exportfilename);

  }

}