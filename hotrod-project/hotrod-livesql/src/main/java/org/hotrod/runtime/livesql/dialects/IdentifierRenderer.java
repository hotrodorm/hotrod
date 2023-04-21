package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.metadata.DatabaseObject;

public class IdentifierRenderer {

  private String unquotedCanonicalIdentifierPattern;
  private boolean caseSensitiveWhenUnquoted;
  private String unquotedParsingTypedPattern;
  private TypedIdentifierCase typedCase;

  public static enum TypedIdentifierCase {
    UPPER, LOWER, CASE_SENSITIVE;

    public String switchCase(final String s) {
      if (this == UPPER)
        return s.toUpperCase();
      if (this == LOWER)
        return s.toLowerCase();
      return s;
    }
  };

  private Quoter quoter;

  public IdentifierRenderer(final String unquotedCanonicalIdentifierPattern, final boolean caseSensitiveWhenUnquoted,
      final String unquotedParsingTypedPattern, final TypedIdentifierCase typedCase, final Quoter quoter) {
    this.unquotedCanonicalIdentifierPattern = unquotedCanonicalIdentifierPattern;
    this.caseSensitiveWhenUnquoted = caseSensitiveWhenUnquoted;
    this.unquotedParsingTypedPattern = unquotedParsingTypedPattern;
    this.typedCase = typedCase;
    this.quoter = quoter;
  }

  public final String renderSQLIdentifier(final String canonicalName) {
    if (canonicalName == null) {
      return null;
    }
    if (canonicalName.matches(this.unquotedCanonicalIdentifierPattern)) {
      return this.caseSensitiveWhenUnquoted ? canonicalName : canonicalName.toLowerCase();
    } else {
      return this.quoter.quote(canonicalName);
    }
  }

  public final String renderTypedSQLIdentifier(final String typedSQLName) {
//    System.out.println("typedSQLName=" + typedSQLName);
    if (typedSQLName == null) {
//      System.out.println("1) typedSQLName=" + typedSQLName + " out: null");
      return null;
    }
    if (typedSQLName.matches(this.unquotedParsingTypedPattern)) {
      String r = this.typedCase.switchCase(typedSQLName);
//      System.out.println("2) typedSQLName=" + typedSQLName + " out: " + r);
      return r;
    } else {
      String r = this.quoter.quote(typedSQLName);
//      System.out.println("3) typedSQLName=" + typedSQLName + " out: " + r);
      return r;
    }
  }

  public String renderSQLObjectName(final DatabaseObject databaseObject) {
    if (databaseObject == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    if (databaseObject.getCatalog() != null) {
      sb.append(this.renderSQLIdentifier(databaseObject.getCatalog()));
      sb.append(".");
    }
    if (databaseObject.getSchema() != null) {
      sb.append(this.renderSQLIdentifier(databaseObject.getSchema()));
      sb.append(".");
    } else if (databaseObject.getCatalog() != null) {
      sb.append(".");
    }
    sb.append(this.renderSQLIdentifier(databaseObject.getName()));
    return sb.toString();
  }

}
