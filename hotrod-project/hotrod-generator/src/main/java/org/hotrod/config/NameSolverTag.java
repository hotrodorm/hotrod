package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.NameSolverNameTag.Scope;
import org.hotrod.exceptions.CouldNotResolveNameException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.nocrala.tools.database.tartarus.utils.TUtil;

@XmlRootElement(name = "name-solver")
public class NameSolverTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(NameSolverTag.class.getName());

  // Properties

  private List<NameSolverNameTag> namingRules = new ArrayList<NameSolverNameTag>();

  // Constructor

  public NameSolverTag() {
    super("name-solver");
    log.fine("init");
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
//    log.info("## " + TUtil.compactStackTrace());
    // log.info("this.namingRules=" + this.namingRules.size());
    for (NameSolverNameTag n : this.namingRules) {
      String newName = n.tryToReplace(name, scope);
//      log.info(" -> newName=" + newName);
      if (newName != null) {
        // new Exception().printStackTrace();
        return newName;
      }
    }
    return null;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
