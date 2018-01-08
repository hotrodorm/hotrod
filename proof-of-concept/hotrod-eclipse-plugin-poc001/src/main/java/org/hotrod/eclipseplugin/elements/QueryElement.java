package org.hotrod.eclipseplugin.elements;

import org.hotrod.eclipseplugin.domain.QueryMethod;

public class QueryElement extends TreeLeafElement {

  private QueryMethod method;

  public QueryElement(final QueryMethod method) {
    super(method.getName(), false);
    this.method = method;
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-query10-16.png";
    return "icons/sql-query6-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + "() -- SQL query";
  }

  @Override
  public String getTooltip() {
    return "SQL query " + super.getLabel();
  }

}
