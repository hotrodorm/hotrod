package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.metadata.BooleanColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeColumn;
import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.ObjectColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.util.ReflectionUtil;

public class Subquery<T> implements TableExpression {

  private String alias;
  private ExecutableSelect<T> select;

  public Subquery(final String alias, final ExecutableSelect<T> select) {
    this.alias = alias;
    this.select = select;
  }

  // Getters

  public String getAlias() {
    return alias;
  }

  public ExecutableSelect<T> getSelect() {
    return select;
  }

  // Subquery column extraction

  public NumberExpression num(final String name) {
    return new SubqueryNumberColumn(this, name);
  }

  public StringExpression str(final String name) {
    return new SubqueryStringColumn(this, name);
  }

  public DateTimeExpression dt(final String name) {
    return new SubqueryDateTimeColumn(this, name);
  }

  public Predicate bool(final String name) {
    return new SubqueryBooleanColumn(this, name);
  }

  public ByteArrayExpression bin(final String name) {
    return new SubqueryByteArrayColumn(this, name);
  }

  public ObjectExpression obj(final String name) {
    return new SubqueryObjectColumn(this, name);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    ag.register(this.alias, null);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    if (this.alias == null) {
      this.alias = ag.next();
    }
  }

  // Rendering

  @Override
  public void renderTo(QueryWriter w, LiveSQLDialect dialect) {
    w.enterLevel();
    w.write("(\n");
    this.select.renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(") ");
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(this.alias)));
  }

  @Override
  public List<ResultSetColumn> getColumns() throws IllegalAccessException {
    System.out.println("#3 this.select: " + this.select);
    List<ResultSetColumn> cols = this.select.listColumns();
    List<ResultSetColumn> subqueryColumns = new ArrayList<>();
    for (ResultSetColumn c : cols) {
      System.out.println("#3.1 " + c);
      Expression expr = castAsSubqueryColumn(c);
      subqueryColumns.add(expr);
    }
    return subqueryColumns;
  }

  private Expression castAsSubqueryColumn(final ResultSetColumn c)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      NumberColumn nc = (NumberColumn) c;
      return new SubqueryNumberColumn(this, nc.getProperty());
    } catch (ClassCastException e1) {
      try {
        StringColumn nc = (StringColumn) c;
        return new SubqueryStringColumn(this, nc.getProperty());
      } catch (ClassCastException e2) {
        try {
          BooleanColumn nc = (BooleanColumn) c;
          return new SubqueryBooleanColumn(this, nc.getProperty());
        } catch (ClassCastException e3) {
          try {
            DateTimeColumn nc = (DateTimeColumn) c;
            return new SubqueryDateTimeColumn(this, nc.getProperty());
          } catch (ClassCastException e4) {
            try {
              ByteArrayColumn nc = (ByteArrayColumn) c;
              return new SubqueryNumberColumn(this, nc.getProperty());
            } catch (ClassCastException e5) {
              try {
                ObjectColumn nc = (ObjectColumn) c;
                return new SubqueryObjectColumn(this, nc.getProperty());
              } catch (ClassCastException e6) {
                try {
                  AliasedExpression nc = (AliasedExpression) c;
                  String alias = ReflectionUtil.getStringField(nc, "alias");
                  return new SubqueryObjectColumn(this, alias);
                } catch (ClassCastException e7) {
                  throw new IllegalArgumentException("Uknown subquery column type '" + c.getClass().getName() + "'");
                }
              }
            }
          }
        }
      }
    }
  }

}
