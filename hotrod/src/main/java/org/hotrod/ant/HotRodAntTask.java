package org.hotrod.ant;

import java.io.File;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hotrod.buildinfo.BuildConstants;
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.DatabaseAdapterFactory;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnrecognizedDatabaseException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.EUtils;
import org.hotrod.utils.LocalFileGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public class HotRodAntTask extends Task {

  private static transient final Logger log = Logger.getLogger(HotRodAntTask.class);

  // Properties

  private String driverClass = null;
  private String url = null;
  private String username = null;
  private String password = null;
  private String catalog = null;
  private String schema = null;
  private String configFileName = null;
  private String sDisplayMode = null;
  private String facetsList = null;
  private String generator = null;

  private File projectBaseDir;
  private File configFile;
  private DisplayMode displayMode;

  private LinkedHashSet<String> facetNames = null;

  // Execute

  private void validateParameters() {

    // driverclass

    if (this.driverClass == null) {
      throw new BuildException(Constants.TOOL_NAME + " parameter: " + "driverclass attribute must be specified.");
    }
    if (SUtils.isEmpty(this.driverClass)) {
      throw new BuildException(Constants.TOOL_NAME + " parameter: " + "driverclass attribute cannot be empty.");
    }

    // url

    if (this.url == null) {
      throw new BuildException(Constants.TOOL_NAME + " parameter: " + "url attribute must be specified.");
    }
    if (SUtils.isEmpty(this.url)) {
      throw new BuildException(Constants.TOOL_NAME + " parameter: " + "url attribute cannot be empty.");
    }

    // username

    if (this.username == null) {
      throw new BuildException(Constants.TOOL_NAME + " parameter: " + "username attribute must be specified.");
    }
    if (SUtils.isEmpty(this.username)) {
      throw new BuildException(Constants.TOOL_NAME + " parameter: " + "username attribute cannot be empty.");
    }

    // password

    if (this.password == null) {
      throw new BuildException(
          Constants.TOOL_NAME + " parameter: " + "password attribute must be specified, even if empty.");
    }

    // catalog

    if (SUtils.isEmpty(this.catalog)) {
      this.catalog = null;
    }

    // schema

    if (SUtils.isEmpty(this.schema)) {
      this.schema = null;
    }

    // configfile

    this.projectBaseDir = new File(".");
    if (this.configFileName == null) {
      throw new BuildException("configfile attribute must be specified.");
    }
    if (SUtils.isEmpty(this.configFileName)) {
      throw new BuildException("configfile attribute cannot be empty.");
    }
    this.configFile = new File(this.projectBaseDir, this.configFileName);
    if (!this.configFile.exists()) {
      throw new BuildException(Constants.TOOL_NAME + " parameter: " + "Config file '"
          + this.configFile.getAbsolutePath() + "' does not exist.");
    }

    // display mode

    if (this.sDisplayMode == null) {
      this.displayMode = DisplayMode.LIST;
    } else {
      this.displayMode = DisplayMode.parse(this.sDisplayMode);
      if (this.displayMode == null) {
        throw new BuildException(Constants.TOOL_NAME + " parameter: "
            + "If specified, the attribute 'display' must have " + "one of the following values: " + "summary, list");
      }
    }

    // facets

    this.facetNames = new LinkedHashSet<String>();
    if (!SUtils.isEmpty(this.facetsList)) {
      for (String facetName : this.facetsList.split(",")) {
        if (!SUtils.isEmpty(facetName)) {
          this.facetNames.add(facetName.trim());
        }
      }
    }

    // generator

    if (SUtils.isEmpty(this.generator)) {
      throw new BuildException(Constants.TOOL_NAME + " parameter: " + "The attribute 'generator' must be specified.");
    }

  }

  public void execute() {

    log.debug("init");

    display(Constants.TOOL_NAME + " version " + BuildConstants.APPLICATION_VERSION + " (build "
        + BuildConstants.BUILD_TIME_TIMESTAMP + ")");

    validateParameters();

    display("");
    display("Configuration File: " + this.configFile);

    DatabaseLocation loc = new DatabaseLocation(this.driverClass, this.url, this.username, this.password, this.catalog,
        this.schema, null);

    DatabaseAdapter adapter;
    try {
      adapter = DatabaseAdapterFactory.getAdapter(loc);
      display("HotRod Database Adapter: " + adapter.getName());
    } catch (UnrecognizedDatabaseException e) {
      throw new BuildException(
          "Could not recognize database type at JDBC URL " + loc.getUrl() + " - " + e.getMessage());
    } catch (UncontrolledException e) {
      Throwable cause = e.getCause();
      throw new BuildException(e.getMessage() + (cause == null ? "" : ": " + cause.getMessage()));
    } catch (Throwable e) {
      throw new BuildException("Could not connect to database: " + EUtils.renderMessages(e));
    }

    log.debug("Adapter loaded.");

    HotRodConfigTag config = null;
    try {
      config = ConfigurationLoader.loadPrimary(this.projectBaseDir, this.configFile, this.generator, adapter);
    } catch (ControlledException e) {
      if (e.getLocation() != null) {
        throw new BuildException("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
      } else {
        throw new BuildException("\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      display("Technical error found: " + EUtils.renderMessages(e));
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (Throwable e) {
      display("Technical error found: " + EUtils.renderMessages(e));
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code.");
    }

    log.debug("Configuration loaded.");

    try {
      config.setChosenFacets(this.facetNames);
    } catch (FacetNotFoundException e) {
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code: " + "facet '"
          + e.getMessage() + "' not found.");
    }

    try {
      CachedMetadata cachedMetadata = new CachedMetadata();
      HotRodGenerator g = config.getGenerators().getSelectedGeneratorTag().instantiateGenerator(cachedMetadata, loc,
          config, this.displayMode, false, adapter);
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
        throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code:\n" + e.getMessage());
      } else {
        throw new BuildException(
            Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
                + e.getLocation().render() + ":\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      display("Technical error found: " + EUtils.renderMessages(e));
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (InvalidConfigurationFileException e) {
      throw new BuildException(
          Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
              + e.getTag().getSourceLocation().render() + ":\n" + e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code.");
    }

  }

  // Ant Setters

  public void setDriverclass(final String driverclass) {
    this.driverClass = driverclass;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  public void setSchema(final String schema) {
    this.schema = schema;
  }

  public void setConfigfile(final String configFile) {
    this.configFileName = configFile;
  }

  public void setDisplay(final String display) {
    this.sDisplayMode = display;
  }

  public void setGenerator(final String generator) {
    this.generator = generator;
  }

  public void setFacets(final String facets) {
    this.facetsList = facets;
  }

  // Helpers

  public enum DisplayMode {
    SUMMARY, LIST;

    public static DisplayMode parse(final String txt) {
      for (DisplayMode dm : DisplayMode.values()) {
        if (dm.name().equalsIgnoreCase(txt)) {
          return dm;
        }
      }
      return null;
    }
  }

  private void display(final String txt) {
    System.out.println(SUtils.isEmpty(txt) ? " " : txt);
  }

}
