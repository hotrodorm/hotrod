package sql;

import metadata.DatabaseObject;
import sql.exceptions.UnsupportedFeatureException;

public class PostgreSQLDialect implements SQLDialect {

  @Override
  public String renderObjectName(final DatabaseObject databaseObject) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String renderName(final String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String renderJoinClause(final Join join) throws UnsupportedFeatureException {
    // TODO Auto-generated method stub
    return null;
  }

  // Pagination

  @Override
  public PaginationType getPaginationType(final Integer offset, final Integer limit) {
    return PaginationType.BOTTOM;
  }

  @Override
  public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
    throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in PostgreSQL");
  }

  @Override
  public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
    if (limit != null) {
      if (offset != null) {
        w.write("limit " + limit + " offset " + offset);
      } else {
        w.write("limit " + limit);
      }
    } else {
      w.write("offset " + offset);
    }
  }

}
