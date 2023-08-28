package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.Select;

public abstract class SetOperator<R> {

  private Select<R> left;
  private Select<R> right;

  public SetOperator(final Select<R> a) {
    this.left = a;
    this.left.setParentOperator(this);
    this.right = null;
  }

  public void setRight(final Select<R> right) {
    this.right = right;
    this.right.setParentOperator(this);
  }

  public Select<R> getLeft() {
    return left;
  }

  public Select<R> getRight() {
    return right;
  }

}
