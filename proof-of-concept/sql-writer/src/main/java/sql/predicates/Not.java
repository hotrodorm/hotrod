package sql.predicates;

import sql.QueryWriter;

public class Not extends Predicate {

  private static final int PRECEDENCE = 2;

  private Predicate a;

  public Not(final Predicate a) {
    super(PRECEDENCE);
    this.a = a;
  }

  @Override
  public void renderTo(final QueryWriter pq) {
    pq.write("not (");
    super.renderInner(this.a, pq);
    pq.write(")");
  }

}
