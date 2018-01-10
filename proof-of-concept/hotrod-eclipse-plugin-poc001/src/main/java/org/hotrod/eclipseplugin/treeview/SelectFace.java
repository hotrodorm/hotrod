package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.SelectMethod;

public class SelectFace extends AbstractLeafFace {

  private SelectMethod method;

  public SelectFace(final SelectMethod method) {
    super(method.getName(), false);
    this.method = method;
  }

  @Override
  public String getIconPath() {
    return "icons/sql-select8-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + "() -- SQL select";
  }

  @Override
  public String getTooltip() {
    return "SQL select " + super.getLabel();
  }

}
