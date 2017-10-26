package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

public class StructuredColumnsMetadata {

  // Properties

  private String vo;
  private List<ExpressionsMetadata> expressions = new ArrayList<ExpressionsMetadata>();
  private List<VOMetadata> vos = new ArrayList<VOMetadata>();

  // Constructor

  public StructuredColumnsMetadata(final String vo, final List<ExpressionsMetadata> expressions,
      final List<VOMetadata> vos) {
    this.vo = vo;
    this.expressions = expressions;
    this.vos = vos;
  }

  // Getters

  public String getVO() {
    return vo;
  }

  public List<ExpressionsMetadata> getExpressions() {
    return expressions;
  }

  public List<VOMetadata> getVOs() {
    return vos;
  }

  public List<ColumnMetadata> getExpressionsColumns() {
    List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
    for (ExpressionsMetadata exp : this.expressions) {
      columns.addAll(exp.getColumns());
    }
    return columns;
  }

}
