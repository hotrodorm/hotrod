package org.hotrod.livesql.metadata;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.subqueries.BinarySubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class BinaryEntityColumn extends BinaryExpression implements EntityColumn {

  private static final Logger log = Logger.getLogger(BinaryEntityColumn.class.getName());

  // Properties

  private TableOrView<?> objectInstance;
  private BinaryEntityColumnMetaData entityColumn;

  // Constructor

  public BinaryEntityColumn(final TableOrView<?> objectInstance, final BinaryEntityColumnMetaData entityColumn) {
    super(Expression.PRECEDENCE_COLUMN);
    log.fine("init");
    this.objectInstance = objectInstance;
    this.entityColumn = entityColumn;
    super.setTypeHandler(Shield.getTypeHandler(entityColumn));
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    BinarySubqueryExpression c = new BinarySubqueryExpression(subquery, alias, this);
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
