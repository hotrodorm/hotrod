package org.hotrod.plugin.ant;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hotrod.plugin.GenOperation;

public class GenAntTask extends Task {

  private static transient final Logger log = LogManager.getLogger(GenAntTask.class);

  private String configfile = null;
  private String localproperties = null;

  private String jdbcdriverclass = null;
  private String jdbcurl = null;
  private String jdbcusername = null;
  private String jdbcpassword = null;
  private String jdbccatalog = null;
  private String jdbcschema = null;
  private String display = null;
  private String facets = null;

  @Override
  public void execute() {
    log.debug("init");

    try {
      GenOperation op = new GenOperation(new File("."), this.configfile, this.localproperties, this.jdbcdriverclass,
          this.jdbcurl, this.jdbcusername, this.jdbcpassword, this.jdbccatalog, this.jdbcschema, this.facets,
          this.display);
      op.execute(new AntFeedback(this));
    } catch (Exception e) {
      throw new BuildException(e.getMessage(), e.getCause());
    }

  }

  // Ant Setters

  public void setConfigfile(final String configfile) {
    this.configfile = configfile;
  }

  public void setLocalproperties(String localproperties) {
    this.localproperties = localproperties;
  }

  public void setJdbcdriverclass(final String jdbcdriverclass) {
    this.jdbcdriverclass = jdbcdriverclass;
  }

  public void setJdbcurl(final String jdbcurl) {
    this.jdbcurl = jdbcurl;
  }

  public void setJdbcusername(final String jdbcusername) {
    this.jdbcusername = jdbcusername;
  }

  public void setJdbcpassword(final String jdbcpassword) {
    this.jdbcpassword = jdbcpassword;
  }

  public void setJdbccatalog(final String jdbccatalog) {
    this.jdbccatalog = jdbccatalog;
  }

  public void setJdbcschema(final String jdbcschema) {
    this.jdbcschema = jdbcschema;
  }

  public void setDisplay(final String display) {
    this.display = display;
  }

  public void setFacets(final String facets) {
    this.facets = facets;
  }

}
