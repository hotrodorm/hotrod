package org.hotrod.livesql.queries.select;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.sets.BaseSelectObject;
import org.hotrod.livesql.util.IdUtil;
import org.hotrod.utils.Separator;
import org.springframework.util.ReflectionUtils;

public class UnarySelectObject<T> extends BaseSelectObject<T> {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(UnarySelectObject.class.getName());

  private boolean doNotAliasColumns;

  public enum LockingMode {
    FOR_UPDATE, FOR_SHARE
  };

  public enum LockingConcurrency {
    WAIT, NO_WAIT, SKIP_LOCKED
  };

  public UnarySelectObject(final List<CTE> ctes, final boolean distinct, final boolean doNotAliasColumns) {
    super(ctes, distinct);
    this.distinctOn = null;
    this.doNotAliasColumns = doNotAliasColumns;
  }

  public UnarySelectObject(final List<CTE> ctes, final boolean distinct, final boolean doNotAliasColumns,
      final List<SQLExpression> resultSetColumns) {
    super(ctes, distinct);
    this.distinctOn = null;
    this.doNotAliasColumns = doNotAliasColumns;
    this.sqlExpressions = resultSetColumns;
  }

  public UnarySelectObject(final List<CTE> ctes, final Expression[] distinctOn, final boolean doNotAliasColumns,
      final List<SQLExpression> resultSetColumns) {
    super(ctes, false);

    if (distinctOn == null || distinctOn.length == 0) {
      throw new LiveSQLException("The list of DISTINCT ON expressions cannot be empty.");
    }
    for (Expression e : distinctOn) {
      if (e == null) {
        throw new LiveSQLException("A DISTINCT ON expression cannot be null.");
      }
    }
    this.distinctOn = Arrays.asList(distinctOn);

    this.doNotAliasColumns = doNotAliasColumns;
    this.sqlExpressions = resultSetColumns;
  }

  public void setDistinctOn(final List<Expression> expressions) {
    this.distinctOn = expressions;
  }

  public void setResultSetColumns(final List<SQLExpression> resultSetColumns) {
    this.sqlExpressions = resultSetColumns;
  }

  // Rendering

  @Override
  public boolean excludeTuplesFromUniqueNames() {
    return true;
  }

  @Override
  public List<Expression> assembleColumns() {
//    String froms = this.from == null ? "N/A"
//        : (this.from.getName().getName() + ":" + this.joins.stream()
//            .map(j -> j.getTableExpression().getName().getName()).collect(Collectors.joining(", ")));
//    log.info("=== 1. ASSEMBLE COLUMNS === " + froms);

    if (this.from != null) {
      this.from.assembleColumns();
    }

    if (this.joins != null) {
      this.joins.forEach(j -> j.getTableExpression().assembleColumns());
    }

    if (this.sqlExpressions == null || this.sqlExpressions.isEmpty()) {
//      log.info("== Adding all columns...");
      this.sqlExpressions = new ArrayList<>();
      this.sqlExpressions.add(this.from.star());
      for (Join j : this.joins) {
        this.sqlExpressions.add(j.getTableExpression().star());
      }
    } else {
//      log.info("== Columns were specified (" + this.resultSetColumns.size() + ")");
    }

//    log.info("=== 1.3");

    // sql.val(3).mult(7) -- Expression N/A
    // a.id -- Column te.id
    // x.num("amount") -- SubqueryXXXColumn te.amount

    // sql.val(3).mult(7).as("multi") -- Expression te.multi
    // a.id.as("bid") -- Column te.bid
    // x.num("amount").as("total") -- SubqueryXXXColumn te.total

    super.expandQueryColumns();

//    for (Expression col : this.expandedQueryColumns) {
//      log.info("   --  expanded col: " + col);
//    }

    this.columnsAssembled = true;
//    log.info("=== 1. END ASSEMBLE COLUMNS === " + froms);
    return this.expandedQueryColumns;

  }

//  @Override
//  public Expression findColumnWithName(final String name) {
//    for (Expression c : this.queryColumns) {
//      if (name.equals(Helper.getReferenceName(c))) {
//        // Only Entity columns, AliasedExpressions and SubqueryTTTColumns return names.
//        return c;
//      }
//    }
//    return null;
//  }

  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
//    log.info("=== 4. WRITE COLUMNS ===");
    Separator sep = new Separator();
//    log.info(">2 this@" + OUtil.hc(this) + ".expandedQueryColumns=" + this.expandedQueryColumns);
    for (Expression expr : this.expandedQueryColumns) {

      w.write(sep.render());
      w.write("\n  ");
      Shield.renderTo(expr, w);

//      Add alias?
//
//          Select Type           EntityCol  AliasedExpr  SubqueryCol  Other
//          --------------------  ---------  -----------  -----------  -----
//          Scalar SELECT         No         Yes          --           No
//          Criteria SELECT       No         Yes          --           No
//          Other/Main SELECT     Yes        Yes          Yes          No

      if (!this.doNotAliasColumns) { // other than scalar selects or criteria selects
        String property = Shield.getProperty(expr);
        if (property != null) {
          w.write(" as " + w.getSQLDialect().canonicalToNatural(property));
        }
      }

    }
  }

  @Override
  public void flatten() {
    // Nothing to do. It's already a single level
  }

  @Override
  public RowReader<T> getRowReader() {
//    log.info("=== GET ROW READER ===");
    // No default RowReader for the Unary SELECT
    return null;
  }

  // Setters

  private void setCTEs(final List<CTE> ctes) {
    if (ctes != null) {
      for (CTE c : ctes) {
        this.ctes.add(c);
      }
    }
  }

  // Execute

  @Override
  public List<T> execute(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQL(context, q, null);
  }

  @Override
  public List<T> execute(LiveSQLContext context, RowReader<T> rowReader) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQL(context, q, rowReader);
  }

  @Override
  public Cursor<T> executeCursor(final LiveSQLContext context) throws SQLException {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQLCursor(context, q);
  }

  @Override
  public Cursor<T> executeCursor(final LiveSQLContext context, final Integer fetchSize) throws SQLException {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQLCursor(context, q, null, fetchSize);
  }

  @Override
  public Cursor<T> executeCursor(final LiveSQLContext context, RowReader<T> rowReader, Integer fetchSize)
      throws SQLException {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQLCursor(context, q, rowReader, fetchSize);
  }

  @Override
  public T executeOne(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQLOne(context, q, null);
  }

  @Override
  public T executeOne(final LiveSQLContext context, RowReader<T> rowReader) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQLOne(context, q, rowReader);
  }

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    if (this.from != null) {
      this.from.validateTableReferences(tableReferences, ag);
    }
    if (this.joins != null) {
      for (Join j : this.joins) {
        j.getTableExpression().validateTableReferences(tableReferences, ag);
      }
    }
    if (this.wherePredicate != null) {
      Shield.validateTableReferences(this.wherePredicate, tableReferences, ag);
//      this.wherePredicate.validateTableReferences(tableReferences, ag);
    }
    if (this.groupBy != null) {
      for (ComparableExpression e : this.groupBy) {
        Shield.validateTableReferences(e, tableReferences, ag);
//        e.validateTableReferences(tableReferences, ag);
      }
    }
    if (this.havingPredicate != null) {
      Shield.validateTableReferences(this.havingPredicate, tableReferences, ag);
//      this.havingPredicate.validateTableReferences(tableReferences, ag);
    }
    if (this.orderingTerms != null) {
      for (@SuppressWarnings("unused")
      OrderingTerm e : this.orderingTerms) {
        //
      }
    }

  }

  public static class AliasGenerator {

    private Set<String> used = new HashSet<String>();

    private char letter = 'a';
    private int seq = 0;

    public void register(final Name alias, final TableOrView tov) {
      if (alias == null) {
        return;
      }

      if (alias.getName().isEmpty()) {
        throw new InvalidLiveSQLStatementException("Empty alias found for " + tov.getType().toLowerCase() + " "
            + MDShield.renderUnescapedName(tov) + ". Any specified alias for a table or view must be non-empty. "
            + "Use any combination of alphanumeric characters as an alias. "
            + "Usually aliases are very short, commonly a single letter.");
      }

      if (!this.used.add(alias.getName())) {
        throw new InvalidLiveSQLStatementException(
            "Same alias '" + alias + "' for tables/views cannot be used multiple times in a Live SQL statement. "
                + "If a query includes multiple tables or views "
                + "two of them cannot share the same alias, even when using subqueries.");
      }
    }

    public String next() {
      do {
        String alias = this.seq == 0 ? "" + this.letter : "" + this.letter + this.seq;
        if (this.used.add(alias)) {
          return alias;
        }
        if (this.letter >= 'z') {
          this.letter = 'a';
          this.seq++;
        } else {
          this.letter++;
        }
      } while (true);
    }

  }

  protected List<SQLExpression> getColumnsField(final Object cs, final String colName)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      Field cf = ReflectionUtils.findField(cs.getClass(), colName);
      if (cf != null) {
        cf.setAccessible(true);
        Object object = cf.get(cs);
        @SuppressWarnings("unchecked")
        List<EntityColumn> columns = (List<EntityColumn>) object;
        return columns.stream().map(c -> (SQLExpression) c).collect(Collectors.toList());
      } else {
        return new ArrayList<>();
      }
    } catch (ClassCastException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public final String toString() {
    return "s" + IdUtil.id(this) + (this.orderingTerms != null ? "o" : "");
  }

}
