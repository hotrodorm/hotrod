package org.hotrod.metadata;

import java.util.List;

public class ExpressionsMetadata {

  private List<AllottedColumnMetadata> columns;

  public ExpressionsMetadata(final List<AllottedColumnMetadata> columns) {
    this.columns = columns;
  }

  public List<AllottedColumnMetadata> getColumns() {
    return this.columns;
  }

}
