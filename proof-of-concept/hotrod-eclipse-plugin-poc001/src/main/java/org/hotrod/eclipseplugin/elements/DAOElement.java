package org.hotrod.eclipseplugin.elements;

public class DAOElement extends TreeContainerElement {

  public DAOElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public DAOElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    return "icons/dao3-16.png";
  }

  @Override
  public String getTooltip() {
    return "DAO " + super.getLabel();
  }

}
