package org.hotrod.eclipseplugin.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class DAO implements ConfigItem {

  // Properties

  protected List<Method> methods = new ArrayList<Method>();

  // Populate

  public void addMethod(final Method m) {
    this.methods.add(m);
  }

  // Getters

  public List<Method> getMethods() {
    return this.methods;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((methods == null) ? 0 : methods.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DAO other = (DAO) obj;
    if (methods == null) {
      if (other.methods != null)
        return false;
    } else if (!methods.equals(other.methods))
      return false;
    return true;
  }

}
