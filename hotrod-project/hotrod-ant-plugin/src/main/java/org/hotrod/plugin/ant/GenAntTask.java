package org.hotrod.plugin.ant;

import java.io.File;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.plugin.GeneratePersistenceLayerOperation;

public class GenAntTask extends Task {

  private static final Logger log = Logger.getLogger(GenAntTask.class.getName());

  static {
    JULCustomFormatter.initialize();
  }

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
    log.fine("init");
    try {
      GeneratePersistenceLayerOperation op = new GeneratePersistenceLayerOperation(new File("."), this.configfile,
          this.localproperties, this.jdbcdriverclass, this.jdbcurl, this.jdbcusername, this.jdbcpassword,
          this.jdbccatalog, this.jdbcschema, this.facets, this.display);
      op.execute(new AntFeedback(this));
    } catch (ErrorMessageException e) {
      throw new BuildException(e.renderErrorMessage("HotRod could not generate the persistence layer"));
    } catch (FaultException e) {
      throw new BuildException("HotRod could not generate the persistence layer", e.getCause());
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
