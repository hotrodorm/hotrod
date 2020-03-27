package org.hotrod.plugin.maven;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.hotrod.plugin.GenOperation;

@Mojo(name = "gen", defaultPhase = LifecyclePhase.COMPILE)
public class GenMojo extends AbstractMojo {

  private static transient final Logger log = LogManager.getLogger(GenMojo.class);

  // Note: 1) Each property must be annotated by @Parameter. 2) The property
  // attribute -- if declared -- must be the exact same name as the Java member

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

  // Project information

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  // Mojo logic

  public void execute() throws MojoExecutionException {
    log.debug("init");

    GenOperation op = new GenOperation(this.project.getBasedir(), this.configfile, this.generator, this.localproperties,
        this.driverclass, this.url, this.username, this.password, this.catalog, this.schema, this.facets, this.display);

    try {
      op.execute(new MojoFeedback(this));
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e.getCause());
    }
  }

}