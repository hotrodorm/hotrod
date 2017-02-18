package org.hotrod.config;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.utils.ClassPackage;

public abstract class AbstractDAOTag {

  private static final Logger log = Logger.getLogger(AbstractDAOTag.class);

  protected LinkedHashSet<String> declaredMethodNames = new LinkedHashSet<String>();

  public final Set<String> getDeclaredMethodNames() {
    log.debug("get methods.");
    return this.declaredMethodNames;
  }

  // Abstract methods

  public abstract ClassPackage getPackage();

  public abstract String getJavaClassName();

}
