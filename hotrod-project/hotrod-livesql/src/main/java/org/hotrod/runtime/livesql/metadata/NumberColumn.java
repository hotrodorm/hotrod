package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NumberColumn extends NumberExpression implements Column {

  // Properties

  private TableOrView objectInstance;

  private String name;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;

  private String property;

  // Constructor

  public NumberColumn(final TableOrView objectInstance, final String name, final String property, final String type,
      final Integer columnSize, final Integer decimalDigits) {
    super(Expression.PRECEDENCE_COLUMN);
    this.objectInstance = objectInstance;
    this.name = name;
    this.property = property;
    this.type = type;
    this.columnSize = columnSize;
    this.decimalDigits = decimalDigits;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(this.objectInstance.getAlias());
      w.write(".");
    }
    renderUnqualifiedNameTo(w);
  }

  @Override
  public void renderUnqualifiedNameTo(final QueryWriter w) {
    w.write(w.getSqlDialect().canonicalToNatural(this.name));
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
  public final String getCatalog() {
    return this.objectInstance.getCatalog();
  }

  @Override
  public final String getSchema() {
    return this.objectInstance.getSchema();
  }

  @Override
  public final String getObjectName() {
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
