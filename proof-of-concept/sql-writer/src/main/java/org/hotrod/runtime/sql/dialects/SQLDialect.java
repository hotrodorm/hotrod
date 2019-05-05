package org.hotrod.runtime.sql.dialects;

public abstract class SQLDialect {

  protected String productName;
  protected String productVersion;
  protected int majorVersion;
  protected int minorVersion;

  public SQLDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    this.productName = productName;
    this.productVersion = productVersion;
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
  }

  public String getProductName() {
    return productName;
  }

  public String getProductVersion() {
    return productVersion;
  }

  public int getMajorVersion() {
    return majorVersion;
  }

  public int getMinorVersion() {
    return minorVersion;
  }

  // Renderers

  public abstract IdentifierRenderer getIdentifierRenderer();

  public abstract JoinRenderer getJoinRenderer();

  public abstract PaginationRenderer getPaginationRenderer();

  public abstract FunctionRenderer getFunctionRenderer();

}
