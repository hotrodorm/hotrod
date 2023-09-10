package org.hotrod.runtime.livesql.dialects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hotrod.runtime.livesql.metadata.DatabaseObject;

public abstract class LiveSQLDialect {

  private boolean discovered; // discovered or designated
  private String databaseName;
  private String databaseVersion;
  private int databaseMajorVersion;
  private int databaseMinorVersion;
  private int patchVersion;

  protected LiveSQLDialect(final boolean discovered, final String databaseName, final String databaseVersion,
      final int databaseMajorVersion, final int databaseMinorVersion) {
    this.discovered = discovered;
    this.databaseName = databaseName;
    this.databaseVersion = databaseVersion;
    this.databaseMajorVersion = databaseMajorVersion;
    this.databaseMinorVersion = databaseMinorVersion;
    this.patchVersion = parsePatchVersion(this.databaseVersion);
  }

  public boolean getDiscovered() {
    return discovered;
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

  protected boolean versionIsAtLeast(final int major, final int minor, final int patch) {
    return this.databaseMajorVersion > major //
        || this.databaseMajorVersion == major && this.databaseMinorVersion > minor //
        || this.databaseMajorVersion == major && this.databaseMinorVersion == minor && this.patchVersion >= patch;
  }

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

  // Parsing

  private int parsePatchVersion(final String databaseVersion) {
    Pattern p = Pattern.compile("^[0-9]+\\.[0-9]+\\.([0-9]+).*$");
    Matcher m = p.matcher(databaseVersion);
    if (m.find()) {
      String patch = m.group(1);
      return Integer.parseInt(patch);
    } else {
      return 0;
    }
  }

  // Renderers

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " [" + (databaseName != null ? "databaseName=" + databaseName + ", " : "")
        + (databaseVersion != null ? "databaseVersion=" + databaseVersion + ", " : "") + "databaseMajorVersion="
        + databaseMajorVersion + ", databaseMinorVersion=" + databaseMinorVersion + "]";
  }

  public abstract WithRenderer getWithRenderer();

  public abstract FromRenderer getFromRenderer();

  public abstract TableExpressionRenderer getTableExpressionRenderer();

  public abstract JoinRenderer getJoinRenderer();

  public abstract PaginationRenderer getPaginationRenderer();

  public abstract SetOperatorRenderer getSetOperationRenderer();

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
