package org.hotrod.livesql.metadata;

import java.util.logging.Logger;

import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class DirectEntityColumnMetaData extends EntityColumnMetaData {

  private static final Logger log = Logger.getLogger(DirectEntityColumnMetaData.class.getName());

  // Properties

  private String type;
  private Integer columnSize;
  private Integer decimalDigits;
  private TypeHandler<?, ?> handler;

  private String property;

  // Constructor

  public DirectEntityColumnMetaData(final Name name, final String property, final String type, final Integer columnSize,
      final Integer decimalDigits, final TypeHandler<?, ?> handler) {
    super(name);
    log.fine("init");
    this.property = property;
    this.type = type;
    this.columnSize = columnSize;
    this.decimalDigits = decimalDigits;
    this.handler = handler;
  }

  // Getters

  @Override
  public String getReferenceName() {
    return this.property;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Integer getColumnSize() {
    return columnSize;
  }

  @Override
  public Integer getDecimalDigits() {
    return decimalDigits;
  }

  @Override
  public String getProperty() {
    return property;
  }

  @Override
  public TypeHandler<?, ?> getTypeHandler() {
    return this.handler;
  }

}
