package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.QueryMethodTag;
import org.hotrod.eclipseplugin.HotRodView;

public class QueryFace extends AbstractMethodFace {

  public QueryFace(final QueryMethodTag method) {
    super(method.getMethod(), method);
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "sql-query.png";
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
