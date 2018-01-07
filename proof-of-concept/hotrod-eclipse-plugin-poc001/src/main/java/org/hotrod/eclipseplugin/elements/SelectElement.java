package org.hotrod.eclipseplugin.elements;

public class SelectElement extends TreeLeafElement {

  public SelectElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public SelectElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    System.out.println("get SELECT icon path.");
    return "icons/sql-select8-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + "()";
  }

  @Override
  public String getTooltip() {
    return "SQL select " + super.getLabel();
  }

}
