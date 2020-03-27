package org.hotrod.runtime.interfaces;

public class UpdateByExampleDao<D> {

  private D filter;
  private D values;

  public UpdateByExampleDao(final D filter, final D values) {
    this.filter = filter;
    this.values = values;
  }

  public D getFilter() {
    return filter;
  }

  public D getValues() {
    return values;
  }

}
