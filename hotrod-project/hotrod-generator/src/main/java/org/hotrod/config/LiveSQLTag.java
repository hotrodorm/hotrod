package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    Set<String> functionNames = new HashSet<String>();
    for (LiveSQLFunctionTag function : this.functions) {
      function.validate(basedir, parentDir, adapter, currentCS);
      if (functionNames.contains(function.getName())) {
        throw new InvalidConfigurationFileException(function,
            "Duplicate function name '" + function.getName() + "' in <livesql> tag.");
      }
      functionNames.add(function.getName());
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
