package org.hotrod.config;

public abstract class AbstractEntityDAOTag extends AbstractDAOTag {

  private static final long serialVersionUID = 1L;

  protected AbstractEntityDAOTag(final String tagName, final boolean isEntity) {
    super(tagName, isEntity);
  }

}
