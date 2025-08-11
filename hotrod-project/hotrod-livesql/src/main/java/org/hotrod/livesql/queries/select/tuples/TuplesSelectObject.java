package org.hotrod.livesql.queries.select.tuples;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.MetaExpression;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.BaseSelectObject;
import org.hotrod.utils.Separator;

public class TuplesSelectObject<T> extends BaseSelectObject<T> {

  private static final Logger log = Logger.getLogger(TuplesSelectObject.class.getName());

  @SuppressWarnings("unused")
  private LiveSQLContext context;

  public TuplesSelectObject(TuplesMetadata metadata) {
    super(metadata.getCtes(), metadata.isDistinct());
    this.sqlExpressions = metadata.getResultSetColumns();
    this.from = metadata.getFrom();
    this.joins = metadata.getJoins();
    this.context = metadata.getContext();
  }

  @Override
  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
    SShield.validateTableReferences(this.from, tableReferences, ag);
    for (Join j : this.joins) {
      SShield.validateTableReferences(j, tableReferences, ag);
    }
  }

  @Override
  public boolean excludeTuplesFromUniqueNames() {
    return true;
  }

  @Override
  public List<Expression> assembleColumns() {

    log.info("resultSetColumns.size()=" + (sqlExpressions == null ? "null" : sqlExpressions.size()));

    boolean isListingColumns = this.sqlExpressions != null && !this.sqlExpressions.isEmpty();

    if (this.getCTEs() != null) {
      for (CTE cte : this.getCTEs()) {
        SShield.assembleColumns(cte);
      }
    }

    if (this.from != null) {
      SShield.assembleColumns(this.from);
    }

    if (this.joins != null) {
      this.joins.forEach(j -> SShield.assembleColumns(j));
    }

    if (isListingColumns) {

      super.expandQueryColumns();

    } else { // columns not listed

//    TupleMetadata m = new TupleMetadata(t, MDHelper.getModeClass(t));
//    this.tuplesMetadata.add(m);

      // Tuples:
      // - List: Model:
      // - - List: alias, getter, setter
      // - unbound:
      // - - alias, getter

      this.sqlExpressions = new ArrayList<>();

      addTableColumns(this.from, this.sqlExpressions);
      for (Join j : this.joins) {
        addTableColumns(SShield.getTableExpression(j), this.sqlExpressions);
//        this.sqlExpressions.add(SShield.star(j));
      }

      super.expandQueryColumns();

    }

    this.columnsAssembled = true;
    return this.expandedQueryColumns;
  }

  private void addTableColumns(TableExpression te, List<SQLExpression> filledIn) {
//    log.info("Adding te: " + te.getAlias());
    MetaExpression wrapped = SShield.star(te);
    List<Expression> unwrapped = MDShield.unwrap(wrapped);
    for (Expression expr : unwrapped) {
      String property = Shield.getProperty(expr);
//      String calias = this.context.getLiveSQLDialect().canonicalToNatural(te.getAlias() + ":" + property);
//      log.info(" + " + calias);
//      Expression aliased = new AliasedExpression(expr, calias);
      filledIn.add(expr);
    }
  }

  @Override
  public TuplesRowReader<T> getRowReader() {
    List<TableOrView<?>> allTables = new ArrayList<>();

    try {
      TableOrView<?> tv = (TableOrView<?>) this.from;
      allTables.add(tv);
    } catch (ClassCastException e) {
      // Ignore other table expressions
    }

    for (Join j : this.joins) {
      TableExpression te = SShield.getTableExpression(j);
      try {
        TableOrView<?> tv = (TableOrView<?>) te;
        allTables.add(tv);
      } catch (ClassCastException e) {
        // Ignore other table expressions
      }
    }
//    List<TableOrView<?>> joined = this.joins.stream().map(j -> (TableOrView<?>) SShield.getTableExpression(j))
//        .collect(Collectors.toList());
//    allTables.addAll(joined);
    return new TuplesRowReader<>(this.expandedQueryColumns, allTables);
  }

  @Override
  public List<T> execute(LiveSQLContext context) {
    log.info("--- execute TUPLES");
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    TuplesRowReader<T> rowReader = getRowReader();
    return executeLiveSQL(context, q, false, rowReader);

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
    for (Expression expr : this.expandedQueryColumns) {

      w.write(sep.render());
      w.write("\n  ");
      Shield.renderTo(expr, w);

      try {
        EntityColumn entityColumn = (EntityColumn) expr;

        // It's a column from a table
        String alias = entityColumn.getObjectInstance().getAlias();
        String property = Shield.getProperty(expr);
        w.write(" as " + w.getSQLDialect().canonicalToNatural(alias + ":" + property));

      } catch (ClassCastException e) {
        // It's a free expression
        String property = Shield.getProperty(expr);
        if (property != null) {
          w.write(" as " + w.getSQLDialect().canonicalToNatural(property));
        }

      }

    }
  }

}
