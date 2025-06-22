package org.hotrod.livesql.queries.subqueries;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Helper;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class SubqueryObjectColumn extends ObjectExpression implements SubqueryColumn {

  // Properties

  private Subquery subquery;
  private String referencedColumnName;

  private Expression column;

  // Constructor

  public SubqueryObjectColumn(final Subquery subquery, final String referencedColumnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.referencedColumnName = referencedColumnName;
  }

  @Override
  protected String getReferenceName() {
    return this.referencedColumnName;
  }

  @Override
  public final String getProperty() {
    return this.referencedColumnName;
  }

  @Override
  protected Expression getEmergingExpression() {
    this.column = this.subquery.findColumnByName(this.referencedColumnName);
    if (this.column == null) {
      throw new RuntimeException(
          "Could not find column '" + this.referencedColumnName + "' in subquery '" + this.subquery.getName() + "'");
    }
    return this;
  }

  @Override
  protected TypeHandler getTypeHandler() {
    try {
      EntityColumn ec = (EntityColumn) this.column;
      return Helper.getTypeHandler(this.column);
    } catch (ClassCastException e) {
      return super.typeHandler;
    }
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    this.subquery.getName().renderTo(w);
    w.write(".");
    w.write(w.getSQLDialect().canonicalToNatural(this.referencedColumnName));
  }

  protected String render() {
    return this.subquery.getName().toString() + ":" + this.referencedColumnName + " typeHandler="
        + super.getTypeHandler();
  }

}
