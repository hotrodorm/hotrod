package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "livesql")
public class LiveSQLTag extends AbstractConfigurationTag {

  private static final Logger log = Logger.getLogger(LiveSQLTag.class.getName());

  private List<LiveSQLFunctionTag> functions = new ArrayList<LiveSQLFunctionTag>();

  public LiveSQLTag() {
    super("livesql");
    log.fine("init");
  }

  @XmlElement(name = "function")
  public void setFunction(final LiveSQLFunctionTag function) {
    this.functions.add(function);
  }

  public void validate(File basedir, final File parentDir, final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {
    for (LiveSQLFunctionTag function : this.functions) {
      function.validate(basedir, parentDir, adapter, currentCS);
    }
  }

  public List<LiveSQLFunctionTag> getFunctions() {
    return this.functions;
  }

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
