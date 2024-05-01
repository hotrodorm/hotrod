package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.converter.TypeConverter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ObjectColumn extends ObjectExpression implements Column {

  // Properties

  private TableOrView objectInstance;

  private String name;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;

  private String property;

  // Constructor

  public ObjectColumn(final TableOrView objectInstance, final String name, final String property, final String type,
      final Integer columnSize, final Integer decimalDigits, final Class<?> javaClass, final Class<?> rawClass,
      final TypeConverter<?, ?> converter) {
    super(Expression.PRECEDENCE_COLUMN);
    this.objectInstance = objectInstance;
    this.name = name;
    this.property = property;
    this.type = type;
    this.columnSize = columnSize;
    this.decimalDigits = decimalDigits;
    this.javaClass = javaClass;
    this.rawClass = rawClass;
    this.converter = converter;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(
          w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.objectInstance.getAlias())));
      w.write(".");
    }
    renderUnqualifiedNameTo(w);
  }

  @Override
  public void renderUnqualifiedNameTo(final QueryWriter w) {
    w.write(w.getSQLDialect().canonicalToNatural(this.name));
  }

  // Getters

  @Override
  public final String getName() {
    return this.name;
  }

  @Override
  public final TableOrView getObjectInstance() {
    return objectInstance;
  }

  @Override
  public final Name getCatalog() {
    return this.objectInstance.getCatalog();
  }

  @Override
  public final Name getSchema() {
    return this.objectInstance.getSchema();
  }

  @Override
  public final Name getObjectName() {
    return this.objectInstance.getName();
  }

  @Override
  public final String getType() {
    return type;
  }

  @Override
  public final Integer getColumnSize() {
    return columnSize;
  }

  @Override
  public final Integer getDecimalDigits() {
    return decimalDigits;
  }

  @Override
  public final String getProperty() {
    return property;
  }

}
