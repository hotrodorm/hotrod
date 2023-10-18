package org.hotrod.runtime.livesql.queries.select.sets;

public class CombinedSelectPhase<R> extends AbstractSelectPhase<R> {

  private CombinedSelectObject<R> multiset;

  public CombinedSelectPhase(final CombinedSelectObject<R> multiset) {
    super(null, null);
    this.multiset = multiset;
  }

  // Set phases

  public void union() {
  }

  public void unionAll() {
  }

  public void except() {
  }

  public void exceptAll() {
  }

  public void intersect() {
  }

  public void intersectAll() {
  }

  // Next phases

//  public SelectOrderByPhase<R> orderBy(final OrderingTerm... orderingTerms) {
//    return new SelectOrderByPhase<R>(this.select, orderingTerms);
//  }
//
//  public SelectOffsetPhase<R> offset(final int offset) {
//    return new SelectOffsetPhase<R>(this.select, offset);
//  }
//
//  public SelectLimitPhase<R> limit(final int limit) {
//    return new SelectLimitPhase<R>(this.select, limit);
//  }

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
//  public SelectObject<R> getSelect() {
//    // TODO Auto-generated method stub
//    return null;
//  }

}
