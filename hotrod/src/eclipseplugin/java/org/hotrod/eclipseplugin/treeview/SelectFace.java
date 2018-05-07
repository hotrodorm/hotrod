package org.hotrod.eclipseplugin.treeview;

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
  public String getDecoration() {
    return "select";
  }

  @Override
  public String getTooltip() {
    return "SQL select " + super.getName();
  }

}
