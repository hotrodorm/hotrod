package org.hotrod.runtime.livesql.dialects;

public abstract class SQLDialect {

  private String databaseName;
  private String databaseVersion;
  private int databaseMajorVersion;
  private int databaseMinorVersion;

  public SQLDialect(final String databaseName, final String databaseVersion, final int databaseMajorVersion,
      final int databaseMinorVersion) {
    this.databaseName = databaseName;
    this.databaseVersion = databaseVersion;
    this.databaseMajorVersion = databaseMajorVersion;
    this.databaseMinorVersion = databaseMinorVersion;
  }

  public String getProductName() {
    return databaseName;
  }

  public String getProductVersion() {
    return databaseVersion;
  }

  public int getMajorVersion() {
    return databaseMajorVersion;
  }

  public int getMinorVersion() {
    return databaseMinorVersion;
  }

  // Version comparator

  protected boolean versionIsAtLeast(final int major, final int minor) {
    return this.databaseMajorVersion > major
        || this.databaseMajorVersion == major && this.databaseMinorVersion >= minor;
  }

  protected boolean versionIsAtLeast(final int major) {
    return this.databaseMajorVersion >= major;
  }

  protected String renderVersion() {
    return "" + databaseMajorVersion + "." + databaseMinorVersion + " (" + databaseVersion + ")";
  }

  // Renderers

  public abstract IdentifierRenderer getIdentifierRenderer();

  public abstract JoinRenderer getJoinRenderer();

  public abstract PaginationRenderer getPaginationRenderer();

  public abstract FunctionRenderer getFunctionRenderer();

}
