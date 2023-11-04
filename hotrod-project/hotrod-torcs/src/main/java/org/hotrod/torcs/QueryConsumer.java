package org.hotrod.torcs;

import org.hotrod.torcs.rankings.Query;

public abstract class QueryConsumer {

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

  public abstract void consume(Query q);

}
