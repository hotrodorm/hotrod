package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.Converter;

public class ConverterFace extends AbstractLeafFace {

  private Converter converter;

  public ConverterFace(final Converter converter) {
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
