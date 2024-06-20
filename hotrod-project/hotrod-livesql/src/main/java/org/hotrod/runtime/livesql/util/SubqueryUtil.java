package org.hotrod.runtime.livesql.util;

import java.lang.reflect.Method;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
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
import org.hotrod.runtime.livesql.queries.subqueries.AllSubqueryColumns;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;
import org.hotrod.runtime.livesql.queries.subqueries.SubqueryBooleanColumn;
import org.hotrod.runtime.livesql.queries.subqueries.SubqueryByteArrayColumn;
import org.hotrod.runtime.livesql.queries.subqueries.SubqueryDateTimeColumn;
import org.hotrod.runtime.livesql.queries.subqueries.SubqueryNumberColumn;
import org.hotrod.runtime.livesql.queries.subqueries.SubqueryObjectColumn;
import org.hotrod.runtime.livesql.queries.subqueries.SubqueryStringColumn;
import org.springframework.util.ReflectionUtils;

public class SubqueryUtil {

  public static List<ResultSetColumn> listColumns(final AllSubqueryColumns asc) {
    Method m = ReflectionUtils.findMethod(AllSubqueryColumns.class, "listColumns");
    m.setAccessible(true);
    ReflectionUtils.invokeMethod(m, asc);
    @SuppressWarnings("unchecked")
    List<ResultSetColumn> cols = (List<ResultSetColumn>) ReflectionUtils.invokeMethod(m, asc);
    return cols;

  }

  // Check for columns of a table or a view
  public static Expression castPersistenceColumnAsSubqueryColumn(final Subquery subquery, final ResultSetColumn c)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      NumberColumn nc = (NumberColumn) c;
      return new SubqueryNumberColumn(subquery, nc.getProperty());
    } catch (ClassCastException e1) {
      try {
        StringColumn nc = (StringColumn) c;
        return new SubqueryStringColumn(subquery, nc.getProperty());
      } catch (ClassCastException e2) {
        try {
          BooleanColumn nc = (BooleanColumn) c;
          return new SubqueryBooleanColumn(subquery, nc.getProperty());
        } catch (ClassCastException e3) {
          try {
            DateTimeColumn nc = (DateTimeColumn) c;
            return new SubqueryDateTimeColumn(subquery, nc.getProperty());
          } catch (ClassCastException e4) {
            try {
              ByteArrayColumn nc = (ByteArrayColumn) c;
              return new SubqueryNumberColumn(subquery, nc.getProperty());
            } catch (ClassCastException e5) {
              try {
                ObjectColumn nc = (ObjectColumn) c;
                return new SubqueryObjectColumn(subquery, nc.getProperty());
              } catch (ClassCastException e6) {
                try {
                  AliasedExpression nc = (AliasedExpression) c;
                  Expression expr = // ReflectionUtil.getExpressionField(nc, "expression");
                      Helper.getExpression(nc);
                  String alias = ReflectionUtil.getStringField(nc, "alias");
                  return castExpressionAsSubqueryColumn(subquery, expr, alias);
                } catch (ClassCastException e7) {
                  return castSubqueryColumnAsExternalLevelSubqueryColumn(subquery, c);
                }
              }
            }
          }
        }
      }
    }
  }

  // check for subquery columns
  public static ComparableExpression castSubqueryColumnAsExternalLevelSubqueryColumn(final Subquery subquery,
      final ResultSetColumn c) throws IllegalArgumentException, IllegalAccessException {
    try {
      SubqueryNumberColumn nc = (SubqueryNumberColumn) c;
      String alias = nc.getReferencedColumnName();
      return new SubqueryNumberColumn(subquery, alias);
    } catch (ClassCastException e1) {
      try {
        SubqueryStringColumn nc = (SubqueryStringColumn) c;
        String alias = nc.getReferencedColumnName();
        return new SubqueryStringColumn(subquery, alias);
      } catch (ClassCastException e2) {
        try {
          SubqueryBooleanColumn nc = (SubqueryBooleanColumn) c;
          String alias = nc.getReferencedColumnName();
          return new SubqueryBooleanColumn(subquery, alias);
        } catch (ClassCastException e3) {
          try {
            SubqueryDateTimeColumn nc = (SubqueryDateTimeColumn) c;
            String alias = nc.getReferencedColumnName();
            return new SubqueryDateTimeColumn(subquery, alias);
          } catch (ClassCastException e4) {
            try {
              SubqueryByteArrayColumn nc = (SubqueryByteArrayColumn) c;
              String alias = nc.getReferencedColumnName();
              return new SubqueryNumberColumn(subquery, alias);
            } catch (ClassCastException e5) {
              try {
                SubqueryObjectColumn nc = (SubqueryObjectColumn) c;
                String alias = nc.getReferencedColumnName();
                return new SubqueryObjectColumn(subquery, alias);
              } catch (ClassCastException e6) {
                throw new IllegalArgumentException("Unknown subquery column type '" + c.getClass().getName() + "'");
              }
            }
          }
        }
      }
    }
  }

  // check for subquery columns
  public static Expression castSubqueryColumnAsExternalLevelSubqueryColumn(final Subquery subquery,
      final ResultSetColumn c, final String alias) throws IllegalArgumentException, IllegalAccessException {
    try {
      @SuppressWarnings("unused")
      NumberExpression nc = (NumberExpression) c;
      return new SubqueryNumberColumn(subquery, alias);
    } catch (ClassCastException e1) {
      try {
        @SuppressWarnings("unused")
        StringExpression nc = (StringExpression) c;
        return new SubqueryStringColumn(subquery, alias);
      } catch (ClassCastException e2) {
        try {
          @SuppressWarnings("unused")
          Predicate nc = (Predicate) c;
          return new SubqueryBooleanColumn(subquery, alias);
        } catch (ClassCastException e3) {
          try {
            @SuppressWarnings("unused")
            DateTimeExpression nc = (DateTimeExpression) c;
            return new SubqueryDateTimeColumn(subquery, alias);
          } catch (ClassCastException e4) {
            try {
              @SuppressWarnings("unused")
              ByteArrayExpression nc = (ByteArrayExpression) c;
              return new SubqueryNumberColumn(subquery, alias);
            } catch (ClassCastException e5) {
              try {
                @SuppressWarnings("unused")
                ObjectExpression nc = (ObjectExpression) c;
                return new SubqueryObjectColumn(subquery, alias);
              } catch (ClassCastException e6) {
                try {
                  AliasedExpression nc = (AliasedExpression) c;
                  Expression expr = ReflectionUtil.getExpressionField(nc, "expression");
//                  String alias = ReflectionUtil.getStringField(nc, "alias");
                  return castExpressionAsSubqueryColumn(subquery, expr, alias);
                } catch (ClassCastException e7) {
                  throw new IllegalArgumentException("Unknown subquery column type '" + c.getClass().getName() + "'");
                }
              }
            }
          }
        }
      }
    }
  }

  // check for general expressions
  public static Expression castExpressionAsSubqueryColumn(final Subquery subquery, final Expression c,
      final String alias) throws IllegalArgumentException, IllegalAccessException {
    try {
      @SuppressWarnings("unused")
      NumberExpression nc = (NumberExpression) c;
      return new SubqueryNumberColumn(subquery, alias);
    } catch (ClassCastException e1) {
      try {
        @SuppressWarnings("unused")
        StringExpression nc = (StringExpression) c;
        return new SubqueryStringColumn(subquery, alias);
      } catch (ClassCastException e2) {
        try {
          @SuppressWarnings("unused")
          Predicate nc = (Predicate) c;
          return new SubqueryBooleanColumn(subquery, alias);
        } catch (ClassCastException e3) {
          try {
            @SuppressWarnings("unused")
            DateTimeExpression nc = (DateTimeExpression) c;
            return new SubqueryDateTimeColumn(subquery, alias);
          } catch (ClassCastException e4) {
            try {
              @SuppressWarnings("unused")
              ByteArrayExpression nc = (ByteArrayExpression) c;
              return new SubqueryNumberColumn(subquery, alias);
            } catch (ClassCastException e5) {
              try {
                @SuppressWarnings("unused")
                ObjectExpression nc = (ObjectExpression) c;
                return new SubqueryObjectColumn(subquery, alias);
              } catch (ClassCastException e6) {
                throw new IllegalArgumentException("Unknown subquery column type '" + c.getClass().getName() + "'");
              }
            }
          }
        }
      }
    }
  }

}
