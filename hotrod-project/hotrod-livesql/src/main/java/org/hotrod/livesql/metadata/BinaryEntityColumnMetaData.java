package org.hotrod.livesql.metadata;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.subqueries.BinarySubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class BinaryEntityColumnMetaData extends BinaryExpression implements EntityColumnMetadata {

  private static final Logger log = Logger.getLogger(BinaryEntityColumnMetaData.class.getName());

  // Properties

  private String name;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;

  private String property;

  // Constructor

  public BinaryEntityColumnMetaData(final String name, final String property, final String type, final Integer columnSize,
      final Integer decimalDigits, final TypeHandler<?, ?> handler) {
    super(Expression.PRECEDENCE_COLUMN);
    log.fine("init");
    this.name = name;
    this.property = property;
    this.type = type;
    this.columnSize = columnSize;
    this.decimalDigits = decimalDigits;
    super.setTypeHandler(handler);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    BinarySubqueryExpression c = new BinarySubqueryExpression(subquery, alias, this);
    return c;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write(w.getSQLDialect().canonicalToNatural(this.name));
  }

  // Getters

  @Override
  public String getReferenceName() {
    return this.property;
  }

  @Override
  protected boolean isEntityColumn() {
    return true;
  }

  public String getCanonicalName() {
    return this.name;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Integer getColumnSize() {
    return columnSize;
  }

  @Override
  public Integer getDecimalDigits() {
    return decimalDigits;
  }

  @Override
  public String getProperty() {
    return property;
  }

}
