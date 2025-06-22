package org.hotrod.livesql.queries.subqueries;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Helper;
import org.hotrod.livesql.expressions.numbers.NumberExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class SubqueryNumberColumn extends NumberExpression implements SubqueryColumn {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SubqueryNumberColumn.class.getName());

  // Properties

  private Subquery subquery;
  private String referencedColumnName;

  private Expression column;

  // Constructor

  public SubqueryNumberColumn(final Subquery subquery, final String referencedColumnName) {
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
    String colName = this.referencedColumnName;
    log.info("%% Searching for column '" + colName + "' in '" + this.subquery.getName() + "'...");
    this.column = this.subquery.findColumnByName(colName);
    if (this.column == null) {
      throw new RuntimeException(
          "Could not find column '" + colName + "' in subquery '" + this.subquery.getName() + "'");
    }
    log.info(this.subquery.getName() + "." + colName + " -> " + this.column + " ("
        + System.identityHashCode(this.column) + ")");
    return this;
  }

  @Override
  protected TypeHandler getTypeHandler() {
    log.info(">>>> getTypeHandler() >>>> '" + this.subquery.getName() + "' ref=" + referencedColumnName + " "
        + System.identityHashCode(this) + " -- this.column(" + Helper.getProperty(this.column) + "/"
        + Helper.getReferenceName(this.column) + ")=" + this.column + " (" + System.identityHashCode(this.column)
        + ")");
//    log.info(">>>>>>>>>>>>>>>>>>>>>>> '" + referencedColumnName + "' TH=" + Helper.getTypeHandler(this.column));

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
