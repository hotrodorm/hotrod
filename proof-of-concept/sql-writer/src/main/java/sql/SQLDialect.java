package sql;

import metadata.DatabaseObject;
import sql.exceptions.UnsupportedFeatureException;

public interface SQLDialect {

  String renderObjectName(DatabaseObject databaseObject);

  String renderName(String name);

  String renderJoinClause(Join join) throws UnsupportedFeatureException;

  // Pagination

  public enum PaginationType {
    TOP, BOTTOM
  };

  PaginationType getPaginationType(Integer offset, Integer limit);

  void renderTopPagination(Integer offset, Integer limit, QueryWriter w);

  void renderBottomPagination(Integer offset, Integer limit, QueryWriter w);

}
