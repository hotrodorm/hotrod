package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.WrappingColumn;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.sets.SingleSelectObject;
import org.hotrod.livesql.util.ToString;

public class SShield {

  public static void validateTableReferences(TableExpression te, TableReferences tableReferences, AliasGenerator ag) {
    te.validateTableReferences(tableReferences, ag);
  }

  public static void validateTableReferences(Join j, TableReferences tableReferences, AliasGenerator ag) {
    j.getTableExpression().validateTableReferences(tableReferences, ag);
  }

  public static Name getName(TableExpression te) {
    return te == null ? Name.parse("N/A") : te.getName();
  }

  public static <R> CombinedSelectObject<R> getCombinedSelect(Select<R> select) {
    return select.getCombinedSelect();
  }

  public static WrappingColumn star(TableExpression t) {
    return t.star();
  }

  public static TableExpression getTableExpression(Join j) {
    return j.getTableExpression();
  }

  public static WrappingColumn star(Join j) {
    return j.getTableExpression().star();
  }

  public static <R> SelectWherePhase<R> getSelectWherePhase(final LiveSQLContext context,
      final SingleSelectObject<R> select, final GeneralBooleanExpression predicate) {
    CombinedSelectObject<R> combined = new CombinedSelectObject<R>(select);
    return new SelectWherePhase<R>(context, combined, predicate);
  }

  public static <R> SelectGroupByPhase<R> getSelectGroupByPhase(final LiveSQLContext context,
      final SingleSelectObject<R> select, final ComparableExpression... expressions) {
    CombinedSelectObject<R> combined = new CombinedSelectObject<R>(select);
    return new SelectGroupByPhase<R>(context, combined, expressions);
  }

  public static <R> LockableSelectOrderByPhase<R> getSelectOrderByPhase(final LiveSQLContext context,
      final SingleSelectObject<R> select, final OrderingTerm... orderingTerms) {
    CombinedSelectObject<R> combined = new CombinedSelectObject<R>(select);
    return new LockableSelectOrderByPhase<R>(context, combined, orderingTerms);
  }

  public static <R> LockableSelectOffsetPhase<R> getSelectOffsetPhase(final LiveSQLContext context,
      final SingleSelectObject<R> select, final int offset) {
    CombinedSelectObject<R> combined = new CombinedSelectObject<R>(select);
    return new LockableSelectOffsetPhase<R>(context, combined, offset);
  }

  public static <R> LockableSelectLimitPhase<R> getSelectLimitPhase(final LiveSQLContext context,
      final SingleSelectObject<R> select, final int limit) {
    CombinedSelectObject<R> combined = new CombinedSelectObject<R>(select);
    return new LockableSelectLimitPhase<R>(context, combined, limit);
  }

  public static void renderTo(TableExpression te, QueryWriter w) {
    te.renderTo(w);
  }

  public static void renderTo(Join j, QueryWriter w) {
    j.getTableExpression().renderTo(w);
  }

  public static void log(TableExpression te, ToString t) {
    te.log(t);
  }

}
