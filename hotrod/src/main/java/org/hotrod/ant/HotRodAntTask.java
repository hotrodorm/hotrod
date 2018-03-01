package org.hotrod.ant;

import java.io.File;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hotrod.buildinfo.BuildConstants;
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.util.SUtils;
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
        this.schema);

    HotRodConfigTag config = null;
    try {
      config = ConfigurationLoader.loadPrimary(this.projectBaseDir, this.configFile, this.generator);
    } catch (ControlledException e) {
      throw new BuildException("\n" + e.getMessage());
    } catch (UncontrolledException e) {
      throw new BuildException(e);
    } catch (RuntimeException e) {
      throw new BuildException(e);
    }

    log.debug("Configuration loaded.");

    try {
      config.setChosenFacets(this.facetNames);
    } catch (FacetNotFoundException e) {
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code: " + "facet '"
          + e.getMessage() + "' not found.");
    }

    try {
      HotRodGenerator g = config.getGenerators().getSelectedGeneratorTag().getGenerator(loc, config, this.displayMode);
      log.debug("Generator instantiated.");

      g.prepareGeneration();
      log.debug("Generation prepared.");

      g.generate();
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
      e.printStackTrace();
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code.");
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
