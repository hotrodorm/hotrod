package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.Converter;

public class ConverterFace extends AbstractFace {

  public ConverterFace(final Converter converter) {
    super(converter.getName(), converter);
  }

  @Override
  public String getIconPath() {
    return "icons/converter1-16.png";
  }

  @Override
  public String getDecoration() {
    return "converter";
  }

  @Override
  public String getTooltip() {
    return "Converter " + super.getName();
  }

}
