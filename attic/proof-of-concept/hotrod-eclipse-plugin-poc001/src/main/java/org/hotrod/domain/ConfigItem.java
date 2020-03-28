package org.hotrod.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfigItem {

  public enum ItemStatus {
    UNAFFECTED, MODIFIED, ADDED, DELETED;
  }

  private final int lineNumber;
  private ItemStatus status;
  private List<ConfigItem> subItems;

  protected ConfigItem(int lineNumber) {
    this.lineNumber = lineNumber;
    this.status = ItemStatus.UNAFFECTED;
    this.subItems = new ArrayList<ConfigItem>();
  }

  public final int getLineNumber() {
    return this.lineNumber;
  }

  public void setStatus(final ItemStatus status) {
    this.status = status;
  }

  public final ItemStatus getStatus() {
    return this.status;
  }

  public final List<ConfigItem> getSubItems() {
    return this.subItems;
  }

  // Compute changes

  public final void computeChangesFrom(final ConfigItem fresh) {

    // own changes

    if (this.copyProperties(fresh)) {
      this.status = ItemStatus.MODIFIED;
    }

    // sub item changes

    List<ConfigItem> existing = new ArrayList<ConfigItem>(this.subItems);
    this.subItems.clear();

    for (ConfigItem f : fresh.subItems) {
      ConfigItem e = findItem(f, existing);
      if (e != null) {
        e.computeChangesFrom(f);
        this.subItems.add(e);
      } else {
        this.subItems.add(f);
      }
    }

  }

  private ConfigItem findItem(final ConfigItem f, final List<ConfigItem> existing) {
    for (ConfigItem e : existing) {
      if (f.sameID(e)) {
        return e;
      }
    }
    return null;
  }

  // Abstract methods for computing item changes

  // Checks if it's the same ID; the other properties do not matter
  public abstract boolean sameID(ConfigItem fresh);

  // Copy non-ID properties; informs if there were any changes.
  public abstract boolean copyProperties(ConfigItem fresh);

}
