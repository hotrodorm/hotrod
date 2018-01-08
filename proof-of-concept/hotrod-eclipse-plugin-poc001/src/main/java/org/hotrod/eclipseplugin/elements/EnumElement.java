package org.hotrod.eclipseplugin.elements;

public class EnumElement extends TreeContainerElement {

  public EnumElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public EnumElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    // return "icons/enum3-16.png";

    // return "icons/enum20-16.png";
    return "icons/enum21-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + " -- enum";
  }

  @Override
  public String getTooltip() {
    return "Table " + super.getLabel();
  }

}
