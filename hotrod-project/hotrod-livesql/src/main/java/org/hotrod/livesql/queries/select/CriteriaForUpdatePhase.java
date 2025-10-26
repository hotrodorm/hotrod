package org.hotrod.livesql.queries.select;

public class CriteriaForUpdatePhase<T> extends CriteriaPhase<T> {

  public CriteriaForUpdatePhase(CriteriaPhase<T> previous) {
    super(previous);
  }

  // next phases

  public CriteriaForUpdateConcurrencyPhase<T> noWait() {
    return new CriteriaForUpdateConcurrencyPhase<>(this, null, false);
  }

  public CriteriaForUpdateConcurrencyPhase<T> wait(final int time) {
    return new CriteriaForUpdateConcurrencyPhase<>(this, time, false);
  }

  public CriteriaForUpdateConcurrencyPhase<T> wait(final double time) {
    return new CriteriaForUpdateConcurrencyPhase<>(this, time, false);
  }

  public CriteriaForUpdateConcurrencyPhase<T> skipLocked() {
    return new CriteriaForUpdateConcurrencyPhase<>(this, null, true);
  }

}
