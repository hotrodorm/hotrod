package sql;

import metadata.DatabaseObject;
import sql.exceptions.UnsupportedFeatureException;

public abstract class SQLDialect {

  private String unquotedIdentifierPattern;
  private String quotePrefix;
  private String quoteSuffix;

  public SQLDialect(final String unquotedIdentifierPattern, final String quotePrefix, final String quoteSuffix) {
    this.unquotedIdentifierPattern = unquotedIdentifierPattern;
    this.quotePrefix = quotePrefix;
    this.quoteSuffix = quoteSuffix;
  }

  public final String renderName(final String canonicalName) {
    if (canonicalName == null) {
      return null;
    }
    if (canonicalName.matches(this.unquotedIdentifierPattern)) {
      return canonicalName;
    }
    return (this.quotePrefix == null ? "" : this.quotePrefix) + canonicalName
        + (this.quoteSuffix == null ? "" : this.quoteSuffix);
  }

  public String renderObjectName(final DatabaseObject databaseObject) {
    if (databaseObject == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    if (databaseObject.getCatalog() != null) {
      sb.append(this.renderName(databaseObject.getCatalog()));
      sb.append(".");
    }
    if (databaseObject.getSchema() != null) {
      sb.append(this.renderName(databaseObject.getSchema()));
      sb.append(".");
    } else if (databaseObject.getCatalog() != null) {
      sb.append(".");
    }
    sb.append(this.renderName(databaseObject.getName()));
    return sb.toString();
  }

  public abstract String renderJoinKeywords(Join join) throws UnsupportedFeatureException;

  // Pagination

  public enum PaginationType {
    TOP, BOTTOM
  };

  public abstract PaginationType getPaginationType(Integer offset, Integer limit);

  public abstract void renderTopPagination(Integer offset, Integer limit, QueryWriter w);

  public abstract void renderBottomPagination(Integer offset, Integer limit, QueryWriter w);

}
