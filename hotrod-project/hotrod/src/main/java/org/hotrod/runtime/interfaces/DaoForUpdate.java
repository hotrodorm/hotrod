package org.hotrod.runtime.interfaces;

public class DaoForUpdate<P> {

  private P p;
  private long currentVersion;
  private long min;
  private long max;

  public DaoForUpdate(final P p, final long currentVersion, final long min, final long max) {
    this.p = p;
    this.currentVersion = currentVersion;
    this.min = min;
    this.max = max;
  }

  public P getP() {
    return p;
  }

  public long getNextVersionValue() {
    return this.currentVersion >= this.max ? this.min : this.currentVersion + 1;
  }

}
