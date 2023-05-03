package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.metadata.DatabaseObject;

public abstract class SQLDialect {

  private String databaseName;
  private String databaseVersion;
  private int databaseMajorVersion;
  private int databaseMinorVersion;

  protected SQLDialect(final String databaseName, final String databaseVersion, final int databaseMajorVersion,
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

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " [" + (databaseName != null ? "databaseName=" + databaseName + ", " : "")
        + (databaseVersion != null ? "databaseVersion=" + databaseVersion + ", " : "") + "databaseMajorVersion="
        + databaseMajorVersion + ", databaseMinorVersion=" + databaseMinorVersion + "]";
  }

  public abstract JoinRenderer getJoinRenderer();

  public abstract PaginationRenderer getPaginationRenderer();

  public abstract SetOperationRenderer getSetOperationRenderer();

  public abstract FunctionRenderer getFunctionRenderer();

  // New SQL Identifier rendering

  public abstract String naturalToCanonical(final String natural);

  public abstract String canonicalToNatural(final String canonical);

  public String canonicalToNatural(final DatabaseObject databaseObject) {
    if (databaseObject == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    if (databaseObject.getCatalog() != null) {
      sb.append(this.canonicalToNatural(databaseObject.getCatalog()));
      sb.append(".");
    }
    if (databaseObject.getSchema() != null) {
      sb.append(this.canonicalToNatural(databaseObject.getSchema()));
      sb.append(".");
    } else if (databaseObject.getCatalog() != null) {
      sb.append(".");
    }
    sb.append(this.canonicalToNatural(databaseObject.getName()));
    return sb.toString();
  }

}
