package org.hotrod.livesql.queries;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.expressions.bool.converter.ConvertedEntityColumn;

public class ConvertedAssignment<R, D> extends Assignment {

  private ConvertedEntityColumn<R, D> c;
  private D d;
  private TypeConverter<R, D> converter;

  public ConvertedAssignment(final ConvertedEntityColumn<R, D> c, final D d, TypeConverter<R, D> converter) {
    super(c, null);
    this.c = c;
    this.d = d;
    this.converter = converter;
  }

  public ConvertedEntityColumn<R, D> getColumn() {
    return c;
  }

  public R getRawValue(Connection conn) {
    return this.converter.encode(d, conn);
  }

}
