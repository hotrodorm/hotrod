package org.hotrodorm.hotrod.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
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
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.runtime.BuildInformation;
import org.hotrod.utils.EUtils;
import org.hotrod.utils.LocalFileGenerator;
import org.hotrodorm.hotrod.utils.SUtils;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

@Mojo(name = "gen", defaultPhase = LifecyclePhase.COMPILE)
public class GenMojo extends AbstractMojo {

  private static transient final Logger log = LogManager.getLogger(GenMojo.class);

  // Note: 1) Each property must be annotated by @Parameter. 2) The property
  // attribute -- if declared -- must be the exact same name as the Java member

  @Parameter(property = "skip")
  private String skip = null;

  @Parameter(property = "configfile")
  private String configfile = null;

  @Parameter(property = "generator")
  private String generator = null;

  @Parameter(property = "driverclass")
  private String driverclass = null;

  @Parameter()
  private String localproperties = null;

  // Loaded locally

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
  private String facets = null;

  @Parameter(property = "display", defaultValue = "list")
  private String display = null;

  // Computed properties (during validation)

  private boolean enabled;
  private File projectBaseDir;
  private File configFile;
  private DisplayMode displayMode;

  private LinkedHashSet<String> facetNames = null;

  // Project information

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  // Mojo logic

  public void execute() throws MojoExecutionException {
    // getLog().info("[ HotRod -- Gen ]");

    log.debug("init");

    display(
        Constants.TOOL_NAME + " version " + BuildInformation.VERSION + " (build " + BuildInformation.BUILD_ID + ")");
    log.debug(" >> artifactId: " + this.project.getArtifactId());
    log.debug(" >> object: " + System.identityHashCode(this));
    log.debug(" >> configfile: " + this.configfile);
    log.debug(" >> base dir: " + this.project.getBasedir());

    validateParameters();

    if (!this.enabled) {
      display(" >> skipped.");
      return;
    }

    display("");
    display("Configuration File: " + this.configFile);

    DatabaseLocation loc = new DatabaseLocation(this.driverclass, this.url, this.username, this.password, this.catalog,
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

    // 1. Process skip parameter

    if (this.skip != null) {
      if (this.skip.equals("true")) {
        this.enabled = false;
      } else if (this.skip.equals("false")) {
        this.enabled = true;
      } else {
        throw new MojoExecutionException(
            Constants.TOOL_NAME + " parameter: " + "when skip is specified it must be either true or false.");
      }
    } else {
      this.enabled = true;
    }

    if (!this.enabled) {
      return;
    }

    // 2. Retrieve the base dir

    this.projectBaseDir = this.project.getBasedir();

    // 3. Load the local properties file

    if (this.localproperties == null || this.localproperties.trim().isEmpty()) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "localproperties must be specified.");
    }

    File p = new File(this.projectBaseDir, this.localproperties);
    if (!p.exists()) {
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " parameter: " + "localproperties file does not exist: " + this.localproperties);
    }
    if (!p.isFile()) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: "
          + "localproperties file exists but it's not a regular file: " + this.localproperties);
    }

    BufferedReader r = null;
    Properties props = null;

    try {
      props = new Properties();
      r = new BufferedReader(new FileReader(p));
      props.load(r);

    } catch (FileNotFoundException e) {
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " parameter: " + "localproperties file does not exist: " + this.localproperties);

    } catch (IOException e) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "localproperties: cannot read file: "
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

    // 4. Load/Replace with local values if specified

    this.configfile = props.getProperty("configfile", this.configfile);
    this.generator = props.getProperty("generator", this.generator);
    this.driverclass = props.getProperty("driverclass", this.driverclass);

    this.url = props.getProperty("url");
    this.username = props.getProperty("username");
    this.password = props.getProperty("password");
    this.catalog = props.getProperty("catalog");
    this.schema = props.getProperty("schema");
    this.facets = props.getProperty("facets");
    this.display = props.getProperty("display");

    // 5. Validate properties

    // configfile

    if (this.configfile == null) {
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " parameter: " + "configfile attribute must be specified.");
    }
    if (SUtils.isEmpty(this.configfile)) {
      throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: " + "configfile attribute cannot be empty.");
    }
    this.configFile = new File(this.projectBaseDir, this.configfile);
    if (!this.configFile.exists()) {
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " parameter: " + "configfile does not exist: " + this.configfile);
    }

    // generator

    if (SUtils.isEmpty(this.generator)) {
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " parameter: " + "The attribute 'generator' must be specified.");
    }

    // driverclass

    if (this.driverclass == null) {
      throw new MojoExecutionException(
          Constants.TOOL_NAME + " parameter: " + "driverclass attribute must be specified.");
    }
    if (SUtils.isEmpty(this.driverclass)) {
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

    // facets

    this.facetNames = new LinkedHashSet<String>();
    if (!SUtils.isEmpty(this.facets)) {
      for (String facetName : this.facets.split(",")) {
        if (!SUtils.isEmpty(facetName)) {
          this.facetNames.add(facetName.trim());
        }
      }
    }

    // display

    if (this.display == null) {
      this.displayMode = DisplayMode.LIST;
    } else {
      this.displayMode = DisplayMode.parse(this.display);
      if (this.displayMode == null) {
        throw new MojoExecutionException(Constants.TOOL_NAME + " parameter: "
            + "If specified, the attribute display must have one of the following values: " + "summary, list");
      }
    }

  }

  private void display(final String txt) {
    getLog().info(SUtils.isEmpty(txt) ? " " : txt);
  }

}