package org.hotrod.livesql.queries.select.tuples;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.SQLMetaExpression;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.GeneratedKeysInsertObject.InsertSelectRenderingEdits;
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
import org.hotrod.livesql.queries.select.sets.SelectObject;
import org.hotrod.livesql.queries.select.tuples.TuplesMetadata.TuplesJoin;
import org.hotrod.utils.Separator;

public class TuplesSelectObject<T> extends BaseSelectObject<T> {

  private static final Logger log = Logger.getLogger(TuplesSelectObject.class.getName());

  @SuppressWarnings("unused")
  private LiveSQLContext context;

  private List<TuplesJoin> tuplesJoins;

  public TuplesSelectObject(TuplesMetadata metadata) {
    super(metadata.getCtes(), metadata.isDistinct());
    log.fine("init");
    this.sqlExpressions = metadata.getResultSetColumns();
    this.from = metadata.getFrom();
    this.tuplesJoins = metadata.getJoins();
    this.context = metadata.getContext();

    super.joins = this.tuplesJoins.stream() //
        .map(tj -> tj.getJoin()) //
        .collect(Collectors.toList());
  }

  @Override
  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
    SShield.validateTableReferences(this.from, tableReferences, ag);
    for (TuplesJoin tj : this.tuplesJoins) {
      SShield.validateTableReferences(tj.getJoin(), tableReferences, ag);
    }
  }

  @Override
  public boolean excludeTuplesFromUniqueNames() {
    return true;
  }

  @Override
  protected void prepareColumnCompilation(Set<SelectObject<?>> compiling) {

    if (this.getCTEs() != null) {
      for (CTE cte : this.getCTEs()) {
        SShield.renderColumns(cte, compiling);
      }
    }

    if (this.from != null) {
      SShield.renderColumns(this.from, compiling);
    }

    if (this.tuplesJoins != null) {
      this.tuplesJoins.forEach(tj -> {
        if (tj.includeInResultSet())
          SShield.prepareColumnCompilation(tj.getJoin(), compiling);
      });
    }

    if (this.sqlExpressions == null || this.sqlExpressions.isEmpty()) {
      this.sqlExpressions = new ArrayList<>();
      addTableColumns(this.from, this.sqlExpressions);
      for (TuplesJoin tj : this.tuplesJoins) {
        if (tj.includeInResultSet()) {
          addTableColumns(SShield.getTableExpression(tj.getJoin()), this.sqlExpressions);
        }
      }
    }

  }

  private void addTableColumns(TableExpression te, List<SQLExpression> filledIn) {
    SQLMetaExpression wrapped = SShield.star(te);
    List<Expression> unwrapped = MDShield.unwrap(wrapped);
    for (Expression expr : unwrapped) {
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

    for (TuplesJoin tj : this.tuplesJoins) {
      if (tj.includeInResultSet()) {
        TableExpression te = SShield.getTableExpression(tj.getJoin());
        try {
          TableOrView<?> tv = (TableOrView<?>) te;
          allTables.add(tv);
        } catch (ClassCastException e) {
          // Ignore other table expressions
        }
      }
    }
//    List<TableOrView<?>> joined = this.joins.stream().map(j -> (TableOrView<?>) SShield.getTableExpression(j))
//        .collect(Collectors.toList());
//    allTables.addAll(joined);
    return new TuplesRowReader<>(this.compiledColumns, allTables);
  }

  @Override
  public List<T> execute(LiveSQLContext context, LiveSQLLogging loggingAdapter) {
//    log.info("--- execute TUPLES");
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    TuplesRowReader<T> rowReader = getRowReader();
    return executeLiveSQL(context, q, rowReader, loggingAdapter);

  }

  @Override
  public List<T> execute(LiveSQLContext context, RowReader<T> rowReader, LiveSQLLogging loggingAdapter) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Cursor<T> executeCursor(LiveSQLContext context, LiveSQLLogging loggingAdapter) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Cursor<T> executeCursor(LiveSQLContext context, LiveSQLLogging loggingAdapter, Integer fetchSize)
      throws SQLException {
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
  public T executeOne(LiveSQLContext context, LiveSQLLogging loggingAdapter) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public T executeOne(LiveSQLContext context, RowReader<T> rowReader) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void flatten() {
    // Nothing to do
  }

  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins,
      final InsertSelectRenderingEdits edits) {
    Separator sep = new Separator();

    if (edits != null && edits.getPrependValue() != null) {
      w.write(sep.render());
      w.write("\n  ");
      Shield.renderTo(edits.getPrependValue(), w);
    }

    for (Expression expr : this.compiledColumns) {

      w.write(sep.render());
      w.write("\n  ");
      Shield.renderTo(expr, w);

      try {
        @SuppressWarnings("unused")
        EntityColumn entityColumn = (EntityColumn) expr;
        // It's a column from a table; no need to alias it

      } catch (ClassCastException e) {
        // It's a SQL expression; needs to include the alias
        String property = Shield.getProperty(expr);
        if (property != null) {
          w.write(" as " + w.getSQLDialect().canonicalToNatural(property));
        }

      }

    }
  }

}
