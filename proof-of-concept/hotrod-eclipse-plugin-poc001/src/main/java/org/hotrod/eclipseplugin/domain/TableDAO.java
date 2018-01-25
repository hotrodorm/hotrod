package org.hotrod.eclipseplugin.domain;

import org.hotrod.eclipseplugin.domain.loader.ConfigFileLoader.NameContent;

public class TableDAO extends DAO {

  private String name;
  private String content;

  public TableDAO(final NameContent nc, final int lineNumber) {
    super(lineNumber);
    this.name = nc.getName();
    this.content = nc.getContent();
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
    TableDAO other = (TableDAO) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  // Computing item changes

  // Checks if it's the same ID; the other properties do not matter
  @Override
  public boolean sameID(final ConfigItem fresh) {
    try {
      TableDAO f = (TableDAO) fresh;
      return this.name.equals(f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Copy non-ID properties; informs if there were any changes.
  @Override
  public boolean copyProperties(final ConfigItem fresh) {
    try {
      TableDAO f = (TableDAO) fresh;
      boolean hasChanges = !this.content.equals(f.content);
      this.content = f.content;
      return hasChanges;
    } catch (ClassCastException e) {
      return false;
    }
  }

}
