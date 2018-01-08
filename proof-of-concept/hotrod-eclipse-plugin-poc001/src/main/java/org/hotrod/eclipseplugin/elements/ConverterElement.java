package org.hotrod.eclipseplugin.elements;

import org.hotrod.eclipseplugin.domain.Converter;

public class ConverterElement extends TreeLeafElement {

  private Converter converter;

  public ConverterElement(final Converter converter) {
    super(converter.getName(), false);
    this.converter = converter;
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
