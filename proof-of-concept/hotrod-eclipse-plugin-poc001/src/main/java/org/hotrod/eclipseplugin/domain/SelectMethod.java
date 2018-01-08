package org.hotrod.eclipseplugin.domain;

public class SelectMethod implements Method {

  private String name;

  public SelectMethod(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    SelectMethod other = (SelectMethod) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

}
