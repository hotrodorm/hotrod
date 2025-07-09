package org.hotrod.livesql.queries.subqueries;

import java.util.logging.Logger;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.util.OUtil;

public class SubqueryObjectRefColumn extends ObjectExpression implements SubqueryColumn {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SubqueryObjectRefColumn.class.getName());

  // Properties

  private Subquery subquery;
  private String referencedColumnName;

  private Expression column;

  // Constructor

  public SubqueryObjectRefColumn(final Subquery subquery, final String referencedColumnName, final Expression column) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.referencedColumnName = referencedColumnName;
    this.column = column;
    if (this.column != null) {
      this.setTypeHandler(Shield.getTypeHandler(this.column));
    }
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
//    log.info("*** getEmergingExpression for '" + this.referencedColumnName + "' on Subquery" + OUtil.hc(this.subquery)
//        + " - this.column=" + this.column);

    if (this.column == null) {
      String colName = this.referencedColumnName;
      this.column = this.subquery.findColumnByName(colName);
//      log.info("%% column " + this.subquery.getName() + "." + colName + "=" + this.column);
      if (this.column == null) {
        throw new RuntimeException(
            "Could not find column '" + colName + "' in subquery '" + this.subquery.getName() + "'");
      }
    }

    this.typeHandler = Shield.getTypeHandler(this.column);
    return this;
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

  public String toString() {
    return this.subquery.getName().toString() + ":" + this.referencedColumnName + " - column: "
        + (this.column == null ? "null" : this.column.getClass().getName()) + " - typeHandler: " + this.typeHandler;
  }

}
