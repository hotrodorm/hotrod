package org.hotrod.livesql.metadata;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.subqueries.NumericSubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class NumericEntityInstanceColumn extends NumericExpression implements EntityInstanceColumn {

  private static final Logger log = Logger.getLogger(NumericEntityInstanceColumn.class.getName());

  // Properties

  private TableOrView<?> objectInstance;
  private NumericEntityColumn entityColumn;

  // Constructor

  public NumericEntityInstanceColumn(final TableOrView<?> objectInstance, final NumericEntityColumn entityColumn) {
    super(Expression.PRECEDENCE_COLUMN);
    log.fine("init");
    this.objectInstance = objectInstance;
    this.entityColumn = entityColumn;
    super.setTypeHandler(Shield.getTypeHandler(entityColumn));
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    NumericSubqueryExpression c = new NumericSubqueryExpression(subquery, alias, this);
    return c;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(
          w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.objectInstance.getAlias())));
      w.write(".");
    }
    Shield.renderTo(this.entityColumn, w);
  }

  // Getters

  @Override
  public String getReferenceName() {
    return this.entityColumn.getProperty();
  }

  @Override
  protected boolean isEntityColumn() {
    return true;
  }

  public String getCanonicalName() {
    return this.entityColumn.getCanonicalName();
  }

  @Override
  public TableOrView<?> getObjectInstance() {
    return objectInstance;
  }

  @Override
  public Name getCatalog() {
    return this.objectInstance.getCatalog();
  }

  @Override
  public Name getSchema() {
    return this.objectInstance.getSchema();
  }

  @Override
  public Name getObjectName() {
    return this.objectInstance.getName();
  }

  @Override
  public String getType() {
    return this.entityColumn.getType();
  }

  @Override
  public Integer getColumnSize() {
    return this.entityColumn.getColumnSize();
  }

  @Override
  public Integer getDecimalDigits() {
    return this.entityColumn.getDecimalDigits();
  }

  @Override
  public String getProperty() {
    return this.entityColumn.getProperty();
  }

}
