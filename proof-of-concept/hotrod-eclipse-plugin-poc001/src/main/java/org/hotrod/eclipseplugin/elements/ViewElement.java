package org.hotrod.eclipseplugin.elements;

public class ViewElement extends TreeContainerElement {

  public ViewElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public ViewElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    // return "icons/view5-16.png";
    return "icons/view6-16.png";
    // return "icons/view7-16.png";
    // return "icons/view9-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + " (view)";
  }

  @Override
  public String getTooltip() {
    return "View " + super.getLabel();
  }

}
