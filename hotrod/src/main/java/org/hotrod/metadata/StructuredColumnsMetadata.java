package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

public class StructuredColumnsMetadata {

  // Properties

  private String vo;
  private List<ExpressionsMetadata> expressions = new ArrayList<ExpressionsMetadata>();
  private List<VOMetadata> collections = new ArrayList<VOMetadata>();
  private List<VOMetadata> vos = new ArrayList<VOMetadata>();

  // Constructor

  public StructuredColumnsMetadata(final String vo, final List<ExpressionsMetadata> expressions,
      final List<VOMetadata> collections, final List<VOMetadata> vos) {
    this.vo = vo;
    this.expressions = expressions;
    this.collections = collections;
    this.vos = vos;
  }

  // Getters

  public String getVO() {
    return vo;
  }

  public List<ExpressionsMetadata> getExpressions() {
    return expressions;
  }

  public List<VOMetadata> getCollections() {
    return collections;
  }

  public List<VOMetadata> getVOs() {
    return vos;
  }

}
