package org.hotrod.livesql.metadata;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.subqueries.NumericSubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class NumericEntityColumn extends NumericExpression implements EntityColumn {

  private static final Logger log = Logger.getLogger(NumericEntityColumn.class.getName());

  // Properties

  private TableOrView<?> objectInstance;
  private EntityColumnMetaData metaData;

  // Constructor

  public NumericEntityColumn(final TableOrView<?> objectInstance, final EntityColumnMetaData metaData) {
    super(Expression.PRECEDENCE_COLUMN);
    log.fine("init");
    this.objectInstance = objectInstance;
    this.metaData = metaData;
    super.setTypeHandler(metaData.getTypeHandler());
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
    this.metaData.renderTo(w);
  }

  // Getters

  @Override
  public String getReferenceName() {
    return this.metaData.getProperty();
  }

  @Override
  protected boolean isEntityColumn() {
    return true;
  }

  public String getCanonicalName() {
    return this.metaData.getCanonicalName();
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
    return this.metaData.getType();
  }

  @Override
  public Integer getColumnSize() {
    return this.metaData.getColumnSize();
  }

  @Override
  public Integer getDecimalDigits() {
    return this.metaData.getDecimalDigits();
  }

  @Override
  public String getProperty() {
    return this.metaData.getProperty();
  }

}
