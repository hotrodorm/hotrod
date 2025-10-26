package org.hotrod.livesql.queries.select;

public class CriteriaLimitPhase<T> extends CriteriaPhase<T> {

  public CriteriaLimitPhase(CriteriaPhase<T> previous) {
    super(previous);
  }

  // next phases

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this);
  }

  public CriteriaForUpdatePhase<T> forShare() {
    this.select.setForShare();
    return new CriteriaForUpdatePhase<T>(this);
  }

}
