package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.ReferenceableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class Column<T> extends Expression<T> implements ReferenceableExpression {

  private static final int PRECEDENCE = 1;

  private TableOrView objectInstance;

  private String name;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;
  private boolean nullable;
  private String defaultValue;
  private boolean lob;

  private String property;

  public Column(final TableOrView objectIntance, final String name, final String property) {
    super(PRECEDENCE);
    this.objectInstance = objectIntance;
    this.name = name;
    this.property = property;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(this.objectInstance.getAlias());
      w.write(".");
    }
    w.write(w.getSqlDialect().getIdentifierRenderer().renderSQLName(this.name));
  }

  // Getters

  public final String getName() {
    return this.name;
  }

  public final TableOrView getObjectInstance() {
    return objectInstance;
  }

  public final String getCatalog() {
    return this.objectInstance.getCatalog();
  }

  public final String getSchema() {
    return this.objectInstance.getSchema();
  }

  public final String getTable() {
    return this.objectInstance.getName();
  }

  public final String getType() {
    return type;
  }

  public final Integer getColumnSize() {
    return columnSize;
  }

  public final Integer getDecimalDigits() {
    return decimalDigits;
  }

  public final boolean isNullable() {
    return nullable;
  }

  public final String getDefaultValue() {
    return defaultValue;
  }

  public final boolean isLob() {
    return lob;
  }

  public final String getProperty() {
    return property;
  }

}
