package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.ConverterTag;
import org.hotrod.eclipseplugin.HotRodView;

public class ConverterFace extends AbstractFace {

  public ConverterFace(final ConverterTag converter) {
    super(converter.getName(), converter);
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "converter.png";
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
