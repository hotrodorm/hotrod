package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.structuredcolumns.AssociationTag;
import org.hotrod.config.structuredcolumns.CollectionTag;
import org.hotrod.config.structuredcolumns.ExpressionsTag;
import org.hotrod.config.structuredcolumns.VOTag;

public class VOMetadata {

  // Constants

  private static final Logger log = Logger.getLogger(VOMetadata.class);

  // Properties

  private List<ExpressionsMetadata> expressions;
  private List<VOMetadata> associations;
  private List<VOMetadata> collections;

  private TableDataSetMetadata tableMetadata;
  private TableDataSetMetadata viewMetadata;
  private List<StructuredColumnMetadata> columns;
  private String alias;
  private String property;
  private String extendedVO;

  // Constructor

  public VOMetadata(final VOTag tag) {
    log.debug("init");

    this.tableMetadata = tag.getTableMetadata();
    this.viewMetadata = tag.getViewMetadata();
    this.columns = tag.getColumns();
    this.alias = tag.getAlias();
    this.property = tag.getProperty();
    this.extendedVO = tag.getExtendedVO();

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

  // Getters

  public List<ExpressionsMetadata> getExpressions() {
    return expressions;
  }

  public List<VOMetadata> getAssociations() {
    return associations;
  }

  public List<VOMetadata> getCollections() {
    return collections;
  }

  public TableDataSetMetadata getTableMetadata() {
    return tableMetadata;
  }

  public TableDataSetMetadata getViewMetadata() {
    return viewMetadata;
  }

  public List<StructuredColumnMetadata> getColumns() {
    return columns;
  }

  public String getAlias() {
    return alias;
  }

  public String getProperty() {
    return this.property;
  }

  public String getExtendedVO() {
    return extendedVO;
  }

}
