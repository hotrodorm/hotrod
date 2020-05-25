package org.hotrod.plugin.maven;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.hotrod.plugin.PurgeOperation;

@Mojo(name = "purge", defaultPhase = LifecyclePhase.COMPILE)
public class PurgeMojo extends AbstractMojo {

  private static transient final Logger log = LogManager.getLogger(PurgeMojo.class);

  // Note: 1) Each property must be annotated by @Parameter. 2) The property
  // attribute -- if declared -- must be the exact same name as the Java member

  @Parameter(property = "configfile")
  private String configfile = null;

  @Parameter(property = "generator")
  private String generator = null;

  @Parameter(property = "localproperties")
  private String localproperties = null;

  @Parameter(property = "jdbcdriverclass")
  private String jdbcdriverclass = null;

  // Loaded locally

  @Parameter(property = "jdbcurl")
  private String jdbcurl = null;

  @Parameter(property = "jdbcusername")
  private String jdbcusername = null;

  @Parameter(property = "jdbcpassword")
  private String jdbcpassword = null;

  @Parameter(property = "jdbccatalog")
  private String jdbccatalog = null;

  @Parameter(property = "jdbcschema")
  private String jdbcschema = null;

  // Project information

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  // Mojo logic

  public void execute() throws MojoExecutionException {
    log.debug("init");

    PurgeOperation op = new PurgeOperation(this.project.getBasedir(), this.configfile, this.generator,
        this.localproperties, this.jdbcdriverclass, this.jdbcurl, this.jdbcusername, this.jdbcpassword,
        this.jdbccatalog, this.jdbcschema);

    try {
      op.execute(new MojoFeedback(this));
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e.getCause());
    }
  }

}