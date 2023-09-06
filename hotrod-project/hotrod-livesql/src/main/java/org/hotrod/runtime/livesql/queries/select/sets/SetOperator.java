package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

//                Union-2
//                  / \
//                 /   \
//           Union-1   SELECT-c
//            / \
//           /   \
//    SELECT-a   SELECT-b
//
//
//           Union-1   
//            / \
//           /   \
//    SELECT-a   Intersect-2
//                 /  \
//                /    \
//          SELECT-b  SELECT-c

public abstract class SetOperator<R> implements MultiSet<R> {

  public static final int PRECEDENCE_INTERSECT = 1;
  public static final int PRECEDENCE_INTERSECT_ALL = 1;
  public static final int PRECEDENCE_UNION = 3;
  public static final int PRECEDENCE_UNION_ALL = 3;
  public static final int PRECEDENCE_EXCEPT = 3;
  public static final int PRECEDENCE_EXCEPT_ALL = 3;

  private SetOperator<R> parent;
  private List<MultiSet<R>> children;

  public SetOperator() {
    this.parent = null;
    this.children = new ArrayList<>();
  }

  @Override
  public void setParentOperator(final SetOperator<R> parent) {
    this.parent = parent;
  }

  @Override
  public SetOperator<R> getParentOperator() {
    return this.parent;
  }

  public void add(final MultiSet<R> child) {
    this.children.add(child);
    child.setParentOperator(this);
  }

  public abstract int getPrecedence();

  public SetOperator<R> findRoot() {
    SetOperator<R> root = this;
    while (root.parent != null) {
      root = root.parent;
    }
    return root;
  }

  // Rendering

  protected abstract void renderSetOperator(final QueryWriter w);

  public void renderTo(final QueryWriter w) {
//    this.left.renderTo(w);
    this.renderSetOperator(w);
//    this.right.renderTo(w);
  }

//  // ExecutableSelect
//
//  @Override
//  public List<R> execute() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Cursor<R> executeCursor() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getPreview() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Select<R> getSelect() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  public List<ResultSetColumn> listColumns() throws IllegalAccessException {
//    // TODO Auto-generated method stub
//    return null;
//  }

}
