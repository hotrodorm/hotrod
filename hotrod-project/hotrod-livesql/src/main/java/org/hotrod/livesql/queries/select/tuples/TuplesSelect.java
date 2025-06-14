package org.hotrod.livesql.queries.select.tuples;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.sets.MultiSet;

public class TuplesSelect<T> extends MultiSet<T> {

  private TuplesMetadata metadata;

  public TuplesSelect(TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  @Override
  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Expression> assembleColumnsOf(TableExpression te) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Expression findColumnWithName(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void renderTo(QueryWriter w, boolean inline) {
    // TODO Auto-generated method stub

  }

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
    // TODO Auto-generated method stub

  }

}
