package org.hotrod.torcs;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class QuerySampleConsumer {

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

  public abstract void consume(QuerySample sample);;

  public abstract void reset();


}
