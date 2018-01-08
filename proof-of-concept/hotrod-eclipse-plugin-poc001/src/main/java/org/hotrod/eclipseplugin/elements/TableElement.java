package org.hotrod.eclipseplugin.elements;

public class TableElement extends TreeContainerElement {

  public TableElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public TableElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    // return "icons/table2-16.png";
    // return "icons/table1-16.png";

    // return "icons/table20-16.png";
    return "icons/table21-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + " -- table";
  }

  @Override
  public String getTooltip() {
    return "Table " + super.getLabel();
  }

}
