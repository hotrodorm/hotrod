package org.hotrod.eclipseplugin.domain;

import org.hotrod.eclipseplugin.domain.loader.ConfigFileLoader.NameContent;

public class SelectMethod extends Method {

  private String name;
  private String content;

  public SelectMethod(final NameContent nc, final int lineNumber) {
    super(lineNumber);
    this.name = nc.getName();
    this.content = nc.getContent();
  }

  public String getName() {
    return name;
  }

  public String getContent() {
    return content;
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((content == null) ? 0 : content.hashCode());
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
    if (content == null) {
      if (other.content != null)
        return false;
    } else if (!content.equals(other.content))
      return false;
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
      SelectMethod f = (SelectMethod) fresh;
      return this.name.equals(f.name) && this.content.equals(f.content);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Copy non-ID properties; informs if there were any changes.
  @Override
  public boolean copyProperties(final ConfigItem fresh) {
    try {
      SelectMethod f = (SelectMethod) fresh;
      boolean hasChanges = !this.content.equals(f.content);
      this.content = f.content;
      return hasChanges;
    } catch (ClassCastException e) {
      return false;
    }
  }

}
