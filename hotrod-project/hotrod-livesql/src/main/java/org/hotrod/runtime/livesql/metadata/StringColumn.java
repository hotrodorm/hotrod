package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;

public class StringColumn extends StringExpression implements Column {

  // Properties

  private TableOrView objectInstance;

  private String name;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;

  private String property;

  // Constructor

  public StringColumn(final TableOrView objectInstance, final String name, final String property, final String type,
      final Integer columnSize, final Integer decimalDigits, final TypeHandler handler) {
    super(Expression.PRECEDENCE_COLUMN);
    this.objectInstance = objectInstance;
    this.name = name;
    this.property = property;
    this.type = type;
    this.columnSize = columnSize;
    this.decimalDigits = decimalDigits;
    super.setTypeHandler(handler);
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(
          w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.objectInstance.getAlias())));
      w.write(".");
    }
    w.write(w.getSQLDialect().canonicalToNatural(this.name));
  }

  // Getters

  @Override
  public final String getReferenceName() {
    return this.property;
  }

  public String getName() {
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
