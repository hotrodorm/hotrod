package org.hotrod.livesql.queries.select.sets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Row;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;
import org.hotrod.livesql.queries.typesolver.ResultSetColumnMetadata;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeRule.CouldNotResolveResultSetDataTypeException;
import org.hotrod.livesql.util.ColumnReader;

public class UnaryRowReader<T> implements RowReader<T> {

  private static final Logger log = Logger.getLogger(UnaryRowReader.class.getName());

  private List<Expression> queryColumns;

  public UnaryRowReader(final LiveSQLContext context, final LiveSQLPreparedQuery q, final ResultSet rs)
      throws SQLException {
    log.fine("init");
    this.queryColumns = q.getQueryColumns();
    ResultSetMetaData rm = rs.getMetaData();
    int ordinal = 1;
    for (Expression expr : this.queryColumns) {
      TypeHandler<Object, Object> cth = Shield.getTypeHandler(expr);
      String name = Shield.getReferenceName(expr);
      if (cth == null) {
        ResultSetColumnMetadata cm = ResultSetColumnMetadata.of(rm, ordinal);
        try {
          TypeHandler<?, ?> th = context.getTypeSolver().resolveRuntimeType(cm);
//          log.info("#" + ordinal + " th.getJavaClass()=" + th.getJavaClass());
          Shield.setTypeHandler(expr, th);
        } catch (CouldNotResolveResultSetDataTypeException e) {
          throw new LiveSQLException("Could not determine the type for the column '" + name + "' in the query. "
//              + "The ResultSetMetaData properties available for this test expression are:\n" + cm.toString()
              , e);
        }
      }
      ordinal++;
    }
    context.logExecution(q);
  }

  @SuppressWarnings("unchecked")
  @Override
  public T readRowFrom(ResultSet rs, Connection conn) throws SQLException {
    Row r = new Row();
    int ordinal = 1;
    for (Expression expr : queryColumns) {
      String alias = Shield.getReferenceName(expr);
      TypeHandler<?, ?> th = Shield.getTypeHandler(expr);
      Object value = ColumnReader.read(rs, ordinal, th, conn);
      r.put(alias, value);
      ordinal++;
    }
    return (T) r;
  }

//  private void logQueryColumns() {
//    int n;
//    n = 1;
//    for (Expression qc : this.queryColumns) {
//      int i = n++;
//      String alias = Shield.getReferenceName(qc);
//      TypeHandler<?, ?> th = Shield.getTypeHandler(qc);
//      log.info("### column #" + i + " " + alias + ": " + th);
//    }
//  }

}
