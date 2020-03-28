package org.hotrod.eclipseplugin.treefaces;

import org.hotrod.config.SelectMethodTag;
import org.hotrod.eclipseplugin.HotRodView;

public class SelectFace extends AbstractMethodFace {

  public SelectFace(final SelectMethodTag method) {
    super(method.getMethod(), method);
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "sql-select.png";
  }

  @Override
  public String getErrorIconPath() {
    return HotRodView.ICONS_DIR + "sql-select-error.png";
  }

  @Override
  public String getDecoration() {
    return "select";
  }

  @Override
  public String getTooltip() {
    return "SQL select " + super.getName();
  }

}
