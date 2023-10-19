package org.hotrod.runtime.livesql.queries.select.sets;

public class SetOperatorTerm<R> {

  private SetOperator<R> operator;
  private MultiSet<R> multiset;

  public SetOperatorTerm(final SetOperator<R> operator, final MultiSet<R> multiset) {
    this.operator = operator;
    this.multiset = multiset;
  }

  public SetOperator<R> getOperator() {
    return operator;
  }

  public MultiSet<R> getMultiset() {
    return multiset;
  }

  public String toString() {
    String k = this.operator.getClass().getSimpleName();
    String acronym = k.substring(0, 1).toLowerCase() + (k.endsWith("AllOperator") ? "a" : "");
    return acronym + "/" + this.multiset.toString();
  }

}
