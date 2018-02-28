package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.QueryMethodTag;

public class QueryFace extends AbstractMethodFace {

  public QueryFace(final QueryMethodTag method) {
    super(method.getMethod(), method);
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-query10-16.png";
    return "eclipse-plugin/icons/sql-query6-16.png";
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
