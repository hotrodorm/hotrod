package org.hotrod.eclipseplugin.domain;

public class EnumDAO extends DAO {

  private String name;

  public EnumDAO(final String name, final int lineNumber) {
    super(lineNumber);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    EnumDAO other = (EnumDAO) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

}
