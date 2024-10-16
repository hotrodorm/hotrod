package org.hotrod.runtime.livesql.util;

public class SubqueryUtil {

//  public static NumberExpression newSubqueryColumn(final Subquery subquery, final NumberExpression expr) {
//    return new SubqueryNumberColumn(subquery, Helper.getAlias(expr));
//  }
//
//  public static StringExpression newSubqueryColumn(final Subquery subquery, final StringExpression expr) {
//    return new SubqueryStringColumn(subquery, Helper.getAlias(expr));
//  }

  // TODO: remove if not used

//  public static List<ResultSetColumn> listColumns(final AllSubqueryColumns asc) {
//    Method m = ReflectionUtils.findMethod(AllSubqueryColumns.class, "listColumns");
//    m.setAccessible(true);
//    ReflectionUtils.invokeMethod(m, asc);
//    @SuppressWarnings("unchecked")
//    List<ResultSetColumn> cols = (List<ResultSetColumn>) ReflectionUtils.invokeMethod(m, asc);
//    return cols;
//  }

//  // Check for columns of a table or a view
//  public static Expression castPersistenceColumnAsSubqueryColumn(final Subquery subquery, final ResultSetColumn c)
//      throws IllegalArgumentException, IllegalAccessException {
//    try {
//      NumberColumn nc = (NumberColumn) c;
//      return new SubqueryNumberColumn(subquery, nc.getProperty());
//    } catch (ClassCastException e1) {
//      try {
//        StringColumn nc = (StringColumn) c;
//        return new SubqueryStringColumn(subquery, nc.getProperty());
//      } catch (ClassCastException e2) {
//        try {
//          BooleanColumn nc = (BooleanColumn) c;
//          return new SubqueryBooleanColumn(subquery, nc.getProperty());
//        } catch (ClassCastException e3) {
//          try {
//            DateTimeColumn nc = (DateTimeColumn) c;
//            return new SubqueryDateTimeColumn(subquery, nc.getProperty());
//          } catch (ClassCastException e4) {
//            try {
//              ByteArrayColumn nc = (ByteArrayColumn) c;
//              return new SubqueryNumberColumn(subquery, nc.getProperty());
//            } catch (ClassCastException e5) {
//              try {
//                ObjectColumn nc = (ObjectColumn) c;
//                return new SubqueryObjectColumn(subquery, nc.getProperty());
//              } catch (ClassCastException e6) {
//                try {
//                  AliasedExpression nc = (AliasedExpression) c;
//                  Expression expr = // ReflectionUtil.getExpressionField(nc, "expression");
//                      Helper.getExpression(nc);
//                  String alias = ReflectionUtil.getStringField(nc, "alias");
//                  return castExpressionAsSubqueryColumn(subquery, expr, alias);
//                } catch (ClassCastException e7) {
//                  return castSubqueryColumnAsExternalLevelSubqueryColumn(subquery, c);
//                }
//              }
//            }
//          }
//        }
//      }
//    }
//  }

//  // check for subquery columns
//  public static ComparableExpression castSubqueryColumnAsExternalLevelSubqueryColumn(final Subquery subquery,
//      final ResultSetColumn c) throws IllegalArgumentException, IllegalAccessException {
//    try {
//      SubqueryNumberColumn nc = (SubqueryNumberColumn) c;
//      String alias = nc.getReferencedColumnName();
//      return new SubqueryNumberColumn(subquery, alias);
//    } catch (ClassCastException e1) {
//      try {
//        SubqueryStringColumn nc = (SubqueryStringColumn) c;
//        String alias = nc.getReferencedColumnName();
//        return new SubqueryStringColumn(subquery, alias);
//      } catch (ClassCastException e2) {
//        try {
//          SubqueryBooleanColumn nc = (SubqueryBooleanColumn) c;
//          String alias = nc.getReferencedColumnName();
//          return new SubqueryBooleanColumn(subquery, alias);
//        } catch (ClassCastException e3) {
//          try {
//            SubqueryDateTimeColumn nc = (SubqueryDateTimeColumn) c;
//            String alias = nc.getReferencedColumnName();
//            return new SubqueryDateTimeColumn(subquery, alias);
//          } catch (ClassCastException e4) {
//            try {
//              SubqueryByteArrayColumn nc = (SubqueryByteArrayColumn) c;
//              String alias = nc.getReferencedColumnName();
//              return new SubqueryNumberColumn(subquery, alias);
//            } catch (ClassCastException e5) {
//              try {
//                SubqueryObjectColumn nc = (SubqueryObjectColumn) c;
//                String alias = nc.getReferencedColumnName();
//                return new SubqueryObjectColumn(subquery, alias);
//              } catch (ClassCastException e6) {
//                throw new IllegalArgumentException("Unknown subquery column type '" + c.getClass().getName() + "'");
//              }
//            }
//          }
//        }
//      }
//    }
//  }

//  // check for subquery columns
//  public static Expression castSubqueryColumnAsExternalLevelSubqueryColumn(final Subquery subquery,
//      final ResultSetColumn c, final String alias) throws IllegalArgumentException, IllegalAccessException {
//    try {
//      @SuppressWarnings("unused")
//      NumberExpression nc = (NumberExpression) c;
//      return new SubqueryNumberColumn(subquery, alias);
//    } catch (ClassCastException e1) {
//      try {
//        @SuppressWarnings("unused")
//        StringExpression nc = (StringExpression) c;
//        return new SubqueryStringColumn(subquery, alias);
//      } catch (ClassCastException e2) {
//        try {
//          @SuppressWarnings("unused")
//          Predicate nc = (Predicate) c;
//          return new SubqueryBooleanColumn(subquery, alias);
//        } catch (ClassCastException e3) {
//          try {
//            @SuppressWarnings("unused")
//            DateTimeExpression nc = (DateTimeExpression) c;
//            return new SubqueryDateTimeColumn(subquery, alias);
//          } catch (ClassCastException e4) {
//            try {
//              @SuppressWarnings("unused")
//              ByteArrayExpression nc = (ByteArrayExpression) c;
//              return new SubqueryNumberColumn(subquery, alias);
//            } catch (ClassCastException e5) {
//              try {
//                @SuppressWarnings("unused")
//                ObjectExpression nc = (ObjectExpression) c;
//                return new SubqueryObjectColumn(subquery, alias);
//              } catch (ClassCastException e6) {
//                try {
//                  AliasedExpression nc = (AliasedExpression) c;
//                  Expression expr = ReflectionUtil.getExpressionField(nc, "expression");
////                  String alias = ReflectionUtil.getStringField(nc, "alias");
//                  return castExpressionAsSubqueryColumn(subquery, expr, alias);
//                } catch (ClassCastException e7) {
//                  throw new IllegalArgumentException("Unknown subquery column type '" + c.getClass().getName() + "'");
//                }
//              }
//            }
//          }
//        }
//      }
//    }
//  }

//  // check for general expressions
//  public static Expression castExpressionAsSubqueryColumn(final Subquery subquery, final Expression c,
//      final String alias) throws IllegalArgumentException, IllegalAccessException {
//    try {
//      @SuppressWarnings("unused")
//      NumberExpression nc = (NumberExpression) c;
//      return new SubqueryNumberColumn(subquery, alias);
//    } catch (ClassCastException e1) {
//      try {
//        @SuppressWarnings("unused")
//        StringExpression nc = (StringExpression) c;
//        return new SubqueryStringColumn(subquery, alias);
//      } catch (ClassCastException e2) {
//        try {
//          @SuppressWarnings("unused")
//          Predicate nc = (Predicate) c;
//          return new SubqueryBooleanColumn(subquery, alias);
//        } catch (ClassCastException e3) {
//          try {
//            @SuppressWarnings("unused")
//            DateTimeExpression nc = (DateTimeExpression) c;
//            return new SubqueryDateTimeColumn(subquery, alias);
//          } catch (ClassCastException e4) {
//            try {
//              @SuppressWarnings("unused")
//              ByteArrayExpression nc = (ByteArrayExpression) c;
//              return new SubqueryNumberColumn(subquery, alias);
//            } catch (ClassCastException e5) {
//              try {
//                @SuppressWarnings("unused")
//                ObjectExpression nc = (ObjectExpression) c;
//                return new SubqueryObjectColumn(subquery, alias);
//              } catch (ClassCastException e6) {
//                throw new IllegalArgumentException("Unknown subquery column type '" + c.getClass().getName() + "'");
//              }
//            }
//          }
//        }
//      }
//    }
//  }

}
