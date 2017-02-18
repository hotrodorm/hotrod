package org.hotrod.runtime.util;

import java.util.Map;

public class SQLJoin {
  public enum JoinType {
    JOIN, LEFT_JOIN, RIGHT_JOIN, INNER_JOIN, OUTER_JOIN
  }

  private SQLTable table;
  private String type;
  private SQLLogicalExpression condition;

  public SQLJoin(SQLTable table, JoinType type, SQLLogicalExpression condition) {
    super();
    this.table = table;
    this.condition = condition;
    switch (type) {
    case JOIN:
      this.type = "JOIN";
      break;
    case LEFT_JOIN:
      this.type = "LEFT JOIN";
      break;
    case RIGHT_JOIN:
      this.type = "RIGHT JOIN";
      break;
    case INNER_JOIN:
      this.type = "INNER JOIN";
      break;
    case OUTER_JOIN:
      this.type = "OUTER JOIN";
      break;
    }

  }

  public SQLTable getTable() {
    return this.table;
  }

  public String render() {
    return " " + this.type + " " + this.table.getName()
        + (this.table.getAlias() != null ? " " + this.table.getAlias() : "") + " ON (" + this.condition.render() + ")";
  }

  @Override
  public String toString() {
    return this.render();
  }

  public Map<String, Object> getParameters() {
    return this.condition.getParameters();
  }

}
