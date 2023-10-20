package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

@SuppressWarnings("deprecation")
public class AbstractSelectPhase<R> implements ExecutableSelect<R> {

  // Properties

  protected LiveSQLContext context;
  protected SelectObject<R> select;

  public AbstractSelectPhase(final LiveSQLContext context, final SelectObject<R> select) {
    this.context = context;
    this.select = select;
  }

  // Execute

  @Override
  public final List<R> execute() {
    return this.select.findRoot().execute(this.context);
  }

  @Override
  public final Cursor<R> executeCursor() {
    return this.select.findRoot().executeCursor(this.context);
  }

  // Utilities

  @Override
  public final String getPreview() {
    return this.select.findRoot().getPreview(this.context);
  }

  // Executable Select

  @Override
  public final SelectObject<R> getSelect() {
    return this.select;
  }

  // Set Operatiors - Inline

  // .union().select()...
  // .union().selectDistinct()...

  public CombinedSelectLinkingPhase<R> union() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new UnionOperator<>());
  }

  public CombinedSelectLinkingPhase<R> unionAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new UnionAllOperator<>());
  }

  public CombinedSelectLinkingPhase<R> except() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new ExceptOperator<>());
  }

  public CombinedSelectLinkingPhase<R> exceptAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new ExceptAllOperator<>());
  }

  public CombinedSelectLinkingPhase<R> intersect() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new IntersectOperator<>());
  }

  public CombinedSelectLinkingPhase<R> intersectAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new IntersectAllOperator<>());
  }

}
