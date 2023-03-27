package org.hotrod.runtime.livesql.queries;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class InsertIntoPhase {

  // Properties

  private Insert insert;

  // Constructor

  public InsertIntoPhase(final LiveSQLDialect sqlDialect, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper, final TableOrView into) {
    this.insert = new Insert(sqlDialect, sqlSession, liveSQLMapper);
    this.insert.setInto(into);
  }

  // Next stages

  public InsertColumnsPhase columns(final List<Column> columns) {
    this.insert.setColumns(columns);
    return new InsertColumnsPhase(this.insert);
  }

  public InsertValuesPhase values(final Expression... values) {
    this.insert.setValues(Arrays.asList(values));
    return new InsertValuesPhase(this.insert);
  }

  public InsertSelectPhase select(final ExecutableSelect<?> select) {
    this.insert.setSelect(select);
    return new InsertSelectPhase(this.insert);
  }

}
