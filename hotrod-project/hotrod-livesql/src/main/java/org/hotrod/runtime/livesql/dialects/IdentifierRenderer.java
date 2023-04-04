package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.metadata.DatabaseObject;

public class IdentifierRenderer {

  private String unquotedIdentifierPattern;
  private String quotePrefix;
  private String quoteSuffix;
  private boolean caseSensitiveWhenUnquoted;

  public IdentifierRenderer(final String unquotedIdentifierPattern, final String quotePrefix, final String quoteSuffix,
      final boolean caseSensitiveWhenUnquoted) {
    this.unquotedIdentifierPattern = unquotedIdentifierPattern;
    this.quotePrefix = quotePrefix;
    this.quoteSuffix = quoteSuffix;
    this.caseSensitiveWhenUnquoted = caseSensitiveWhenUnquoted;
  }

  public final String renderSQLName(final String canonicalName) {
    if (canonicalName == null) {
      return null;
    }
    if (canonicalName.matches(this.unquotedIdentifierPattern)) {
      return this.caseSensitiveWhenUnquoted ? canonicalName : canonicalName.toLowerCase();
    }
    return (this.quotePrefix == null ? "" : this.quotePrefix) + canonicalName
        + (this.quoteSuffix == null ? "" : this.quoteSuffix);
  }

  public final String renderVerbatimName(final String quotedName) {
    return (this.quotePrefix == null ? "" : this.quotePrefix) + quotedName
        + (this.quoteSuffix == null ? "" : this.quoteSuffix);
  }

  public String renderSQLObjectName(final DatabaseObject databaseObject) {
    if (databaseObject == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    if (databaseObject.getCatalog() != null) {
      sb.append(this.renderSQLName(databaseObject.getCatalog()));
      sb.append(".");
    }
    if (databaseObject.getSchema() != null) {
      sb.append(this.renderSQLName(databaseObject.getSchema()));
      sb.append(".");
    } else if (databaseObject.getCatalog() != null) {
      sb.append(".");
    }
    sb.append(this.renderSQLName(databaseObject.getName()));
    return sb.toString();
  }

}
