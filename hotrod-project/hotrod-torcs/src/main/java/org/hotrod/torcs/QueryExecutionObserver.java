package org.hotrod.torcs;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class QueryExecutionObserver {

  private static final Logger log = Logger.getLogger(QueryExecutionObserver.class.getName());

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

  public interface ResetObserver {
    void doBeforeReset();
  }

  private ResetObserver resetObserver = null;

  public void setResetObserver(ResetObserver resetObserver) {
    this.resetObserver = resetObserver;
  }

  public void reset() {
    if (this.resetObserver != null) {
      try {
        this.resetObserver.doBeforeReset();
      } catch (Throwable e) {
        log.log(Level.SEVERE, "Torcs reset observer exception", e);
      }
    }
    this.executeReset();
  }

  public abstract void executeReset();

}
