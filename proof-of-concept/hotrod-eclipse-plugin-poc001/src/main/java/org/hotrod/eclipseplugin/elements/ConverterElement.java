package org.hotrod.eclipseplugin.elements;

public class ConverterElement extends TreeLeafElement {

  public ConverterElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public ConverterElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    return "icons/converter1-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + " -- converter";
  }

  @Override
  public String getTooltip() {
    return "Converter " + super.getLabel();
  }

}
