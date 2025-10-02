package org.hotrod.livesql.queries.select.sets;

public class SetOperatorTerm<R> {

  private SetOperator operator;
  private SelectObject<R> multiset;

  public SetOperatorTerm(final SetOperator operator, final SelectObject<R> multiset) {
    this.operator = operator;
    this.multiset = multiset;
  }

  public SetOperator getOperator() {
    return operator;
  }

  public SelectObject<R> getMultiset() {
    return multiset;
  }

  public String toString() {
    String k = this.operator.getClass().getSimpleName();
    String acronym = k.substring(0, 1).toLowerCase() + (k.endsWith("AllOperator") ? "a" : "");
    return acronym + "/" + this.multiset.toString();
  }

}
