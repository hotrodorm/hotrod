package org.hotrod.metadata;

import java.util.List;

import org.hotrod.config.structuredcolumns.ExpressionsTag;
import org.hotrod.runtime.dynamicsql.SourceLocation;

public class ExpressionsMetadata {

  private ExpressionsTag tag;
  private List<StructuredColumnMetadata> columns;

  public ExpressionsMetadata(final ExpressionsTag tag, final List<StructuredColumnMetadata> columns) {
    this.tag = tag;
    this.columns = columns;
  }

  public List<StructuredColumnMetadata> getColumns() {
    return this.columns;
  }

  public SourceLocation getSourceLocation() {
    return this.tag.getSourceLocation();
  }

}
