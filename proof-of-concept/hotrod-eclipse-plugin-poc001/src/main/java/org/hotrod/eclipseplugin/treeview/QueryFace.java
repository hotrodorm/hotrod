package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.QueryMethod;

public class QueryFace extends AbstractMethodFace {

  public QueryFace(final QueryMethod method) {
    super(method.getName(), method);
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-query10-16.png";
    return "icons/sql-query6-16.png";
  }

  @Override
  public String getDecoration() {
    return "query";
  }

  @Override
  public String getTooltip() {
    return "SQL query " + super.getName();
  }

}
