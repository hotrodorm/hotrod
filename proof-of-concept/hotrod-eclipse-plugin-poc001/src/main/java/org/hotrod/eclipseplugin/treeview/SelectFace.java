package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.SelectMethod;

public class SelectFace extends AbstractMethodFace {

  public SelectFace(final SelectMethod method) {
    super(method.getName(), method);
  }

  @Override
  public String getIconPath() {
    return "icons/sql-select8-16.png";
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
