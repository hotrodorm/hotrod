package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.SelectMethodTag;

public class SelectFace extends AbstractMethodFace {

  public SelectFace(final SelectMethodTag method) {
    super(method.getMethod(), method);
  }

  @Override
  public String getIconPath() {
    return "eclipse-plugin/icons/sql-select8-16.png";
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
