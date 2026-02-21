package org.hotrod.livesql.queries;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.livesql.metadata.TableWithGeneratedKey;

public class GeneratedKeysInsertIntoPhase<T> {

  // Properties

  private LiveSQLContext context;
  private GeneratedKeysInsertObject<T> insert;

  // Constructor

  public GeneratedKeysInsertIntoPhase(final LiveSQLContext context, final TableWithGeneratedKey<?, T> into) {
    this.context = context;
    this.insert = new GeneratedKeysInsertObject<T>(into.getExecutor());
    this.insert.setInto(into);
  }

  // Next stages

  public GeneratedKeysInsertColumnsPhase<T> columns(final EntityColumnMetadata... columns) {
    this.insert.setColumns(Arrays.asList(columns));
    return new GeneratedKeysInsertColumnsPhase<T>(this.context, this.insert);
  }

  public GeneratedKeysInsertValuesPhase<T> values(final ComparableExpression... values) {
    this.insert.setValues(Arrays.asList(values));
    return new GeneratedKeysInsertValuesPhase<T>(this.context, this.insert);
  }

}
