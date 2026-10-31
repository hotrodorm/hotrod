package org.hotrod.livesql.expressions.bool.converter;

import org.hotrod.converter.TypeConverter;

public abstract class CShield {

  private CShield() {
    // static class
  }

  @SuppressWarnings("unchecked")
  public static TypeConverter<?, Object> getConverter(ConvertedEntityColumn<?, ?> column) {
    return (TypeConverter<?, Object>) column.getTypeHandler().getConverter();
  }

}
