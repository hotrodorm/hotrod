package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.ConverterTag;

public class ConverterFace extends AbstractFace {

  public ConverterFace(final ConverterTag converter) {
    super(converter.getName(), converter);
  }

  @Override
  public String getIconPath() {
    return "eclipse-plugin/icons/converter1-16.png";
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
