package org.hotrod.runtime.sql.metadata;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.ReferenceableExpression;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.OrderingTerm;

public class Column extends Expression implements ReferenceableExpression {

  private static final int PRECEDENCE = 1;

  private TableOrView objectInstance;

  private String catalog;
  private String schema;
  private String table;
  private String name;
  private int dataType;
  private String typeName;
  private Integer columnSize;
  private Integer decimalDigits;
  private boolean nullable;
  private String defaultValue;
  private int ordinalPosition;
  private boolean serial;
  private boolean lob;

  public Column(final TableOrView objectIntance, final String name) {
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

  // Getters

  String getName() {
    return this.name;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(this.objectInstance.getAlias());
      w.write(".");
    }
    w.write(w.getSqlDialect().renderName(this.name));
  }

}
