package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.Select;

public class CombinedSelectOrderByPhase<R> implements ExecutableSelect<R> {

  // Properties

  private Select<R> select;

  // Constructor

  CombinedSelectOrderByPhase(final Select<R> select, final CombinedOrderingTerm... orderingTerms) {
    this.select = select;
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same stage

  // Next stages

  public CombinedSelectOffsetPhase<R> offset(final int offset) {
    return new CombinedSelectOffsetPhase<R>(this.select, offset);
  }

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.select, limit);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<R> execute() {
    return this.select.execute();
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor();
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.select.validateTableReferences(tableReferences, ag);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview();
  }

  @Override
  public List<ResultSetColumn> listColumns() throws IllegalAccessException {
    return this.select.listColumns();
  }

  // Executable Select

  @Override
  public Select<R> getSelect() {
    return this.select;
  }

}
