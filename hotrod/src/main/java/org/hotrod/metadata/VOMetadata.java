package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.config.structuredcolumns.AssociationTag;
import org.hotrod.config.structuredcolumns.CollectionTag;
import org.hotrod.config.structuredcolumns.ExpressionsTag;
import org.hotrod.config.structuredcolumns.VOTag;

public class VOMetadata {

  private List<ExpressionsMetadata> expressions;
  private List<VOMetadata> associations;
  private List<VOMetadata> collections;

  public VOMetadata(final VOTag tag) {

    this.expressions = new ArrayList<ExpressionsMetadata>();
    for (ExpressionsTag e : tag.getExpressions()) {
      this.expressions.add(e.getExpressionsMetadata());
    }

    this.associations = new ArrayList<VOMetadata>();
    for (AssociationTag a : tag.getAssociations()) {
      this.associations.add(a.getMetadata());
    }

    this.collections = new ArrayList<VOMetadata>();
    for (CollectionTag c : tag.getCollections()) {
      this.collections.add(c.getMetadata());
    }

  }

  public List<ExpressionsMetadata> getExpressions() {
    return expressions;
  }

  public List<VOMetadata> getAssociations() {
    return associations;
  }

  public List<VOMetadata> getCollections() {
    return collections;
  }

}
