package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.ReferenceableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;

public class ObjectColumn extends Expression<Object> implements ReferenceableExpression {

  private static final int PRECEDENCE = 1;

  private TableOrView objectInstance;

  private String name;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;
  private boolean nullable;
  private String defaultValue;
  private boolean lob;

  public ObjectColumn(final TableOrView objectIntance, final String name) {
    super(PRECEDENCE);
    this.objectInstance = objectIntance;
    this.name = name;
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

  String getName() {
    return this.name;
  }

  public TableOrView getObjectInstance() {
    return objectInstance;
  }

  public String getCatalog() {
    return this.objectInstance.getCatalog();
  }

  public String getSchema() {
    return this.objectInstance.getSchema();
  }

  public String getTable() {
    return this.objectInstance.getName();
  }

  public String getType() {
    return type;
  }

  public Integer getColumnSize() {
    return columnSize;
  }

  public Integer getDecimalDigits() {
    return decimalDigits;
  }

  public boolean isNullable() {
    return nullable;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public boolean isLob() {
    return lob;
  }

}
