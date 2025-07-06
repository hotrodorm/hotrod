package org.hotrod.livesql.queries.subqueries;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.numbers.NumberExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

@Deprecated
public class SubqueryNumberResolvedColumn extends NumberExpression implements SubqueryColumn {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SubqueryNumberResolvedColumn.class.getName());

  // Properties

  private Subquery subquery;
  private String referencedColumnName;
  private Expression column;

  // Constructor

  public SubqueryNumberResolvedColumn(final Subquery subquery, final String referencedColumnName, Expression column) {
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

//  @Override
//  protected Expression getEmergingExpression() {
//    Expression c = this.subquery.findColumnByName(this.referencedColumnName);
//    if (c == null) {
//      throw new RuntimeException(
//          "Could not find column '" + this.subquery.getName() + "." + this.referencedColumnName + "'.");
//    }
//    return c;
//  }

  @Override
  protected Expression getEmergingExpression() {

    log.info("getEmergingExpression: " + this.subquery.getName() + "." + this.referencedColumnName);

    if (this.column == null) {
      String colName = this.referencedColumnName;
//      log.info("%% Searching for column '" + colName + "' in '" + this.subquery.getName() + "'...");
      this.column = this.subquery.findColumnByName(colName);
      if (this.column == null) {
        throw new RuntimeException(
            "Could not find column '" + colName + "' in subquery '" + this.subquery.getName() + "'");
      }
    }

//    this.column.

//SELECT
//    y."baly" as "balm"
//  FROM (
//    SELECT
//      x."balx" as "baly"
//    FROM (
//      SELECT
//        a.balance as "balx"
//      FROM account a
//      LIMIT 1
//    ) x
//    LIMIT 1
//  ) y

    int length = Thread.currentThread().getStackTrace().length;
    log.info("Stack length=" + length);

//    Helper.
//    this.column.get

    log.info("  --> " + this.column + " (" + System.identityHashCode(this.column) + ")");
    if (length <= 25) {
      Expression ee = Shield.getEmergingExpression(this.column);
      log.info("  ==> " + ee + " (" + System.identityHashCode(ee) + ")");
    }
    return this;
  }

  @Override
  protected TypeHandler getTypeHandler() {
    log.info(">>>> getTypeHandler() >>>> '" + this.subquery.getName() + "' ref=" + referencedColumnName + " "
        + System.identityHashCode(this) + " -- this.column(" + Shield.getProperty(this.column) + "/"
        + Shield.getReferenceName(this.column) + ")=" + this.column + " (" + System.identityHashCode(this.column)
        + ")");
//    log.info(">>>>>>>>>>>>>>>>>>>>>>> '" + referencedColumnName + "' TH=" + Helper.getTypeHandler(this.column));

    try {
      EntityColumn ec = (EntityColumn) this.column;
      return Shield.getTypeHandler(this.column);
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
