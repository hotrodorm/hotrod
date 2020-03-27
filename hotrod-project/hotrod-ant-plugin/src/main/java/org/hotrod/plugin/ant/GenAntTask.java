package org.hotrod.plugin.ant;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hotrod.plugin.GenOperation;

public class GenAntTask extends Task {

  private static transient final Logger log = LogManager.getLogger(GenAntTask.class);

  private String configFileName = null;
  private String generator = null;
  private String driverClass = null;
  private String localproperties = null;

  private String url = null;
  private String username = null;
  private String password = null;
  private String catalog = null;
  private String schema = null;
  private String display = null;
  private String facets = null;

  public void execute() {
    log.debug("init");

    GenOperation op = new GenOperation(new File("."), this.configFileName, this.generator, this.localproperties,
        this.driverClass, this.url, this.username, this.password, this.catalog, this.schema, this.facets, this.display);

    try {
      op.execute(new AntFeedback(this));
    } catch (Exception e) {
      throw new BuildException(e.getMessage(), e.getCause());
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
    this.display = display;
  }

  public void setGenerator(final String generator) {
    this.generator = generator;
  }

  public void setFacets(final String facets) {
    this.facets = facets;
  }

}
