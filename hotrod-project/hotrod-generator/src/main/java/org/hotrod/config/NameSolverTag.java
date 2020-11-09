package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.NameSolverNameTag.Scope;
import org.hotrod.exceptions.CouldNotResolveNameException;
import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "name-solver")
public class NameSolverTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(NameSolverTag.class);

  // Properties

  private List<NameSolverNameTag> namingRules = new ArrayList<NameSolverNameTag>();

  // Constructor

  public NameSolverTag() {
    super("name-solver");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement(name = "name")
  public void setNameSolverName(final NameSolverNameTag n) {
    this.namingRules.add(n);
  }

  // Behavior

  public void validate(final HotRodConfigTag config) throws InvalidConfigurationFileException {

    // whens

    for (NameSolverNameTag n : this.namingRules) {
      n.validate(config);
    }

  }

  public String resolveName(final String name, final Scope scope) throws CouldNotResolveNameException {
    // log.info("this.namingRules=" + this.namingRules.size());
    for (NameSolverNameTag n : this.namingRules) {
      String newName = n.tryToReplace(name, scope);
      if (newName != null) {
        return newName;
      }
    }
    return null;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    return false;
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    return true;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
