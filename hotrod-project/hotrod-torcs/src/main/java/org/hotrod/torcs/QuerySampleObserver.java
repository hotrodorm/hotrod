package org.hotrod.torcs;

public abstract class QuerySampleObserver {

  private boolean active = true;

  public void activate() {
    this.active = true;
  }

  public void deactivate() {
    this.active = false;
  }

  public boolean isActive() {
    return this.active;
  }

  public abstract String getTitle();

  public abstract void apply(QueryExecution execution);

  public abstract void reset();

}
