package org.hotrod.livesql.expressions.bool.converter;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.metadata.EntityColumnMetaData;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class ConvertedColumnMetaData<R, D> extends EntityColumnMetaData {

  private TypeConverter<R, D> converter;

  private String property;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;
  private TypeHandler<R, D> handler;

  public ConvertedColumnMetaData(final String canonicalName, final String property, final String type,
      final Integer columnSize, final Integer decimalDigits, final TypeHandler<R, D> handler,
      final TypeConverter<R, D> converter) {
    super(canonicalName);
    this.converter = converter;
    this.property = property;
    this.type = type;
    this.columnSize = columnSize;
    this.decimalDigits = decimalDigits;
    this.handler = handler;
  }

  // EntityColumn

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public Integer getColumnSize() {
    return this.columnSize;
  }

  @Override
  public Integer getDecimalDigits() {
    return this.decimalDigits;
  }

  @Override
  public final String getProperty() {
    return property;
  }

  @Override
  public String getReferenceName() {
    return this.property;
  }

  @Override
  public final TypeHandler<R, D> getTypeHandler() {
    return handler;
  }

}
