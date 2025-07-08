package org.hotrod.livesql.queries.select.tuples;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.expressions.AliasedExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.ResultSetColumn;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.metadata.WrappingColumn;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.SingleSelectObject;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.utils.Separator;

public class TuplesSelectObject<T> extends SingleSelectObject<T> {

  private static final Logger log = Logger.getLogger(TuplesSelectObject.class.getName());

  private LiveSQLContext context;

  public TuplesSelectObject(List<ResultSetColumn> resultSetColumns, TuplesMetadata metadata) {
    super(metadata.getCtes(), metadata.isDistinct());
    this.resultSetColumns = resultSetColumns;
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
  public List<Expression> assembleColumnsOf(Subquery te) {

    log.info("resultSetColumns.size()=" + (resultSetColumns == null ? "null" : resultSetColumns.size()));

    boolean isListingColumns = this.resultSetColumns != null && !this.resultSetColumns.isEmpty();

    if (isListingColumns) {

      expandQueryColumns();

    } else { // columns not listed

//    TupleMetadata m = new TupleMetadata(t, MDHelper.getModeClass(t));
//    this.tuplesMetadata.add(m);

      // Tuples:
      // - List: Model:
      // - - List: alias, getter, setter
      // - unbound:
      // - - alias, getter

      this.resultSetColumns = new ArrayList<>();

      addTableColumns((TableOrView<?>) this.from, this.resultSetColumns);
      for (Join j : this.joins) {
        addTableColumns((TableOrView<?>) SShield.getTableExpression(j), this.resultSetColumns);
        this.resultSetColumns.add(SShield.star(j));
      }

      expandQueryColumns();

    }

    this.columnsAssembled = true;
    return this.expandedQueryColumns;
  }

  private void addTableColumns(TableOrView<?> te, List<ResultSetColumn> filledIn) {
    log.info("Adding te: " + te.getAlias());
    WrappingColumn wrapped = SShield.star(te);
    List<Expression> unwrapped = MDShield.unwrap(wrapped);
    for (Expression expr : unwrapped) {
      String property = Shield.getProperty(expr);
      String calias = this.context.getLiveSQLDialect().canonicalToNatural(te.getAlias() + ":" + property);
      log.info(" + " + calias);
      Expression aliased = new AliasedExpression(expr, calias);
      filledIn.add(aliased);
    }
  }

  @Override
  public TuplesRowReader<T> getRowReader() {
    List<TableOrView<?>> allTables = new ArrayList<>();
    allTables.add((TableOrView<?>) this.from);
    List<TableOrView<?>> joined = this.joins.stream().map(j -> (TableOrView<?>) SShield.getTableExpression(j))
        .collect(Collectors.toList());
    allTables.addAll(joined);
    return new TuplesRowReader<>(this.resultSetColumns, allTables);
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
