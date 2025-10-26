package org.hotrod.livesql.queries.select;

public class CriteriaOffsetPhase<T> extends CriteriaPhase<T> {

  public CriteriaOffsetPhase(CriteriaPhase<T> previous) {
    super(previous);
  }

  // next phases

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this);
  }

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this);
  }

  public CriteriaForUpdatePhase<T> forShare() {
    this.select.setForShare();
    return new CriteriaForUpdatePhase<T>(this);
  }

}
