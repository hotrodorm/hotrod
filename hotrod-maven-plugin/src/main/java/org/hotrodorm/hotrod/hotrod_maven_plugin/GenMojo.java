package org.hotrodorm.hotrod.hotrod_maven_plugin;

import java.io.File;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Settings;
import org.hotrod.ant.Constants;
import org.hotrod.buildinfo.BuildConstants;
import org.hotrod.config.ConfigurationLoader;
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
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.EUtils;
import org.hotrod.utils.LocalFileGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

@Mojo(name = "gen", defaultPhase = LifecyclePhase.COMPILE)
public class GenMojo extends AbstractMojo {

  private static transient final Logger log = Logger.getLogger(GenMojo.class);

  // Project properties: common to all developers in a project

  @Parameter(property = "configfile")
  private String configFileName = null;

  @Parameter(property = "generator")
  private String generator = null;

  // Developer properties: could be different per developer

  @Parameter(property = "driverclass")
  private String driverClass = null;

  @Parameter(property = "url")
  private String url = null;

  @Parameter(property = "username")
  private String username = null;

  @Parameter(property = "password")
  private String password = null;

  @Parameter(property = "catalog")
  private String catalog = null;

  @Parameter(property = "schema")
  private String schema = null;

  @Parameter(property = "facets", defaultValue = "")
  private String facetsList = null;

  @Parameter(property = "display", defaultValue = "list")
  private String sDisplayMode = null;

  // Extra access to settings (may not be needed)

  @Parameter(defaultValue = "${settings}", readonly = true)
  private Settings settings = null;

  // Computed properties (during validation)

  private File projectBaseDir;
  private File configFile;
  private DisplayMode displayMode;

  private LinkedHashSet<String> facetNames = null;

  // Mojo logic

  public void execute() throws MojoExecutionException {
    getLog().info("[ HotRod -- Gen ]");

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
      throw new MojoExecutionException(
          "Could not recognize database type at JDBC URL " + loc.getUrl() + " - " + e.getMessage());
    } catch (UncontrolledException e) {
      Throwable cause = e.getCause();
      throw new MojoExecutionException(e.getMessage() + (cause == null ? "" : ": " + cause.getMessage()));
    } catch (Throwable e) {
      throw new MojoExecutionException("Could not connect to database: " + EUtils.renderMessages(e));
    }

    log.debug("Adapter loaded.");

    HotRodConfigTag config = null;
    try {
      config = ConfigurationLoader.loadPrimary(this.projectBaseDir, this.configFile, this.generator, adapter);
    } catch (ControlledException e) {
      if (e.getLocation() != null) {
        throw new MojoExecutionException("\n" + e.getMessage() + "\n  in " + e.getLocation().render());
      } else {
        throw new MojoExecutionException("\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      display("Technical error found: " + EUtils.renderMessages(e));
      throw new MojoExecutionException(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (Throwable e) {
      display("Technical error found: " + EUtils.renderMessages(e));
      log.error("Technical error found", e);
      throw new MojoExecutionException(Constants.TOOL_NAME + " could not generate the persistence code.");
    }

    log.debug("Configuration loaded.");

    try {
      config.setChosenFacets(this.facetNames);
    } catch (FacetNotFoundException e) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " could not generate the persistence code: " + "facet '"
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
        throw new MojoExecutionException(
            Constants.TOOL_NAME + " could not generate the persistence code:\n" + e.getMessage());
      } else {
        throw new MojoExecutionException(
            Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
                + e.getLocation().render() + ":\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      display("Technical error found: " + EUtils.renderMessages(e));
      throw new MojoExecutionException(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (InvalidConfigurationFileException e) {
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
              + e.getTag().getSourceLocation().render() + ":\n" + e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
      throw new MojoExecutionException(Constants.TOOL_NAME + " could not generate the persistence code.");
    }

  }

  // Validation

  private void validateParameters() throws MojoExecutionException {

    // driverclass

    if (this.driverClass == null) {
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " parameter: " + "driverclass attribute must be specified.");
    }
    if (SUtils.isEmpty(this.driverClass)) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "driverclass attribute cannot be empty.");
    }

    // url

    if (this.url == null) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "url attribute must be specified.");
    }
    if (SUtils.isEmpty(this.url)) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "url attribute cannot be empty.");
    }

    // username

    if (this.username == null) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "username attribute must be specified.");
    }
    if (SUtils.isEmpty(this.username)) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "username attribute cannot be empty.");
    }

    // password

    if (this.password == null) {
      throw new MojoExecutionException(
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
      throw new MojoExecutionException("configfile attribute must be specified.");
    }
    if (SUtils.isEmpty(this.configFileName)) {
      throw new MojoExecutionException("configfile attribute cannot be empty.");
    }
    this.configFile = new File(this.projectBaseDir, this.configFileName);
    if (!this.configFile.exists()) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "Config file '"
          + this.configFile.getAbsolutePath() + "' does not exist.");
    }

    // display mode

    if (this.sDisplayMode == null) {
      this.displayMode = DisplayMode.LIST;
    } else {
      this.displayMode = DisplayMode.parse(this.sDisplayMode);
      if (this.displayMode == null) {
        throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: "
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
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " parameter: " + "The attribute 'generator' must be specified.");
    }

  }

  private void display(final String txt) {
    getLog().info(SUtils.isEmpty(txt) ? " " : txt);
  }

}