package org.hotrod.metadata;

import java.util.List;

public class ExpressionsMetadata {

  private List<StructuredColumnMetadata> columns;

  public ExpressionsMetadata(final List<StructuredColumnMetadata> columns) {
    this.columns = columns;
  }

  public List<StructuredColumnMetadata> getColumns() {
    return this.columns;
  }

}
