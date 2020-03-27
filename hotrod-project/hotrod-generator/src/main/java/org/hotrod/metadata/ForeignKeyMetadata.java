package org.hotrod.metadata;

import java.io.Serializable;
import java.util.Set;

import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class ForeignKeyMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

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
    return this.local;
  }

  public KeyMetadata getRemote() {
    return this.remote;
  }

  public boolean pointsToPK() {
    return this.pointsToPK;
  }

  void linkReferencedTableMetadata(final Set<TableDataSetMetadata> tableMetadata) {
    for (TableDataSetMetadata tm : tableMetadata) {
      if (tm.correspondsToJdbcTable(this.remoteTable)) {
        this.remote.linkReferencedDataSet(tm);
        return;
      }
    }
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

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.local.toString());
    sb.append(" -> ");
    sb.append(this.remote.toString());
    return sb.toString();
  }

}
