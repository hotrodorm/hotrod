package org.hotrod.livesql.queries.select;

public class CriteriaForUpdateConcurrencyPhase<T> extends CriteriaPhase<T> {

  public CriteriaForUpdateConcurrencyPhase(CriteriaPhase<T> previous, final Number waitTime, final boolean skipLocked) {
    super(previous);
    super.select.setLockingConcurrency(waitTime, skipLocked);
  }

  // next phases

}
