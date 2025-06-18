package org.hotrod.livesql.queries.select.tuples;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Helper;
import org.hotrod.livesql.expressions.ResultSetColumn;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.SHelper;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.SingleSelectObject;
import org.hotrod.utils.Separator;

public class CompositeSelectObject<T> extends SingleSelectObject<T> {

  private static final Logger log = Logger.getLogger(CompositeSelectObject.class.getName());

  public CompositeSelectObject(List<ResultSetColumn> resultSetColumns, TuplesMetadata metadata) {
    super(metadata.getCtes(), metadata.isDistinct());
    this.resultSetColumns = resultSetColumns;
    this.baseTableExpression = metadata.getFrom();
    this.joins = metadata.getJoins();
  }

  @Override
  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
    SHelper.validateTableReferences(this.baseTableExpression, tableReferences, ag);
    for (Join j : this.joins) {
      SHelper.validateTableReferences(j, tableReferences, ag);
    }
  }

  @Override
  public List<Expression> assembleColumnsOf(TableExpression te) {

    log.info("resultSetColumns.size()=" + resultSetColumns.size());

    boolean isListingColumns = this.resultSetColumns != null && !this.resultSetColumns.isEmpty();

    if (isListingColumns) {

      populateQueryColumns(this.resultSetColumns);

    } else { // columns not listed

      List<ResultSetColumn> filledIn = new ArrayList<>();
      filledIn.add(SHelper.star(this.baseTableExpression));
      for (Join j : this.joins) {
        filledIn.add(SHelper.star(j));
      }
      populateQueryColumns(filledIn);

    }

    this.columnsAssembled = true;
    return this.queryColumns;
  }

//  @Override
//  public Expression findColumnWithName(String name) {
//    // TODO Auto-generated method stub
//    return null;
//  }

  @Override
  public List<T> execute(LiveSQLContext context) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<T> execute(LiveSQLContext context, RowReader<T> rowReader) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Cursor<T> executeCursor(LiveSQLContext context) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Cursor<T> executeCursor(LiveSQLContext context, Integer fetchSize) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Cursor<T> executeCursor(LiveSQLContext context, RowReader<T> rowReader, Integer fetchSize)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public T executeOne(LiveSQLContext context) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void flatten() {
    // Nothing to do
  }

  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    Separator sep = new Separator();
    for (Expression expr : this.queryColumns) {

      w.write(sep.render());
      w.write("\n  ");
      Helper.renderTo(expr, w);

      try {
        EntityColumn entityColumn = (EntityColumn) expr;

        // It's a column from a table
        String alias = entityColumn.getObjectInstance().getAlias();
        String property = Helper.getProperty(expr);
        w.write(" as " + w.getSQLDialect().canonicalToNatural(alias + ":" + property));

      } catch (ClassCastException e) {
        // It's a free expression
        String property = Helper.getProperty(expr);
        if (property != null) {
          w.write(" as " + w.getSQLDialect().canonicalToNatural(property));
        }

      }

    }
  }

}
