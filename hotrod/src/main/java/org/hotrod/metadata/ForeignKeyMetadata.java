package org.hotrod.metadata;

import java.util.Set;

import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class ForeignKeyMetadata {

  private KeyMetadata local;
  private KeyMetadata remote;
  private boolean pointsToPK;
  private JdbcTable remoteTable;

  public ForeignKeyMetadata(final KeyMetadata local, final KeyMetadata remote, final boolean pointsToPK,
      final JdbcTable remoteTable) {
    this.local = local;
    this.remote = remote;
    this.pointsToPK = pointsToPK;
    this.remoteTable = remoteTable;
  }

  // Getters

  public KeyMetadata getLocal() {
    return local;
  }

  public KeyMetadata getRemote() {
    return remote;
  }

  public boolean pointsToPK() {
    return pointsToPK;
  }

  void linkReferencedDataSet(final Set<TableDataSetMetadata> dss) {
    for (TableDataSetMetadata tds : dss) {
      if (tds.correspondsToJdbcTable(this.remoteTable)) {
        // System.out.println(
        // " + this.remoteTable=" + this.remoteTable.getName() + " tds=" +
        // tds.getIdentifier().getSQLIdentifier());
        remote.linkReferencedDataSet(tds);
        return;
      }
    }
    // System.out.println(
    // " + this.remoteTable " + this.remoteTable + " NOT
    // FOUND!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    // remote.linkReferencedDataSet(null);
  }

  // Indexing methods

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((local == null) ? 0 : local.hashCode());
    result = prime * result + ((remote == null) ? 0 : remote.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ForeignKeyMetadata other = (ForeignKeyMetadata) obj;
    if (local == null) {
      if (other.local != null)
        return false;
    } else if (!local.equals(other.local))
      return false;
    if (remote == null) {
      if (other.remote != null)
        return false;
    } else if (!remote.equals(other.remote))
      return false;
    return true;
  }

}
