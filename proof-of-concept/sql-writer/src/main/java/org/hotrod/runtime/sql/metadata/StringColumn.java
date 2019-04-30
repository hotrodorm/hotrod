package org.hotrod.runtime.sql.metadata;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.ReferenceableExpression;
import org.hotrod.runtime.sql.expressions.OrderingTerm;
import org.hotrod.runtime.sql.expressions.strings.StringExpression;

public class StringColumn extends StringExpression implements ReferenceableExpression {

  private static final int PRECEDENCE = 1;

  private TableOrView objectInstance;

  private String catalog;
  private String schema;
  private String table;
  private String name;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;
  private boolean nullable;
  private String defaultValue;
  private boolean lob;

  public StringColumn(final TableOrView objectIntance, final String name) {
    super(PRECEDENCE);
    this.objectInstance = objectIntance;
    this.name = name;
  }

  // Column ordering

  public OrderingTerm asc() {
    return new OrderingTerm(this, true);
  }

  public OrderingTerm desc() {
    return new OrderingTerm(this, false);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(this.objectInstance.getAlias());
      w.write(".");
    }
    w.write(w.getSqlDialect().renderName(this.name));
  }

  // Getters

  String getName() {
    return this.name;
  }

  public TableOrView getObjectInstance() {
    return objectInstance;
  }

  public String getCatalog() {
    return catalog;
  }

  public String getSchema() {
    return schema;
  }

  public String getTable() {
    return table;
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
