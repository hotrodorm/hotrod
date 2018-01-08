package org.hotrod.eclipseplugin.domain;

import org.hotrod.eclipseplugin.domain.loader.ConfigFileLoader.NameContent;

public class SequenceMethod implements Method {

  private String name;
  private String content;

  public SequenceMethod(final NameContent nc) {
    super();
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
    SequenceMethod other = (SequenceMethod) obj;
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

}
