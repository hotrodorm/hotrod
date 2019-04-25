package sql.predicates;

public class And extends Predicate {

  private static final int PRECEDENCE = 11;

  private Predicate a;
  private Predicate b;

  public And(final Predicate a, final Predicate b) {
    super(PRECEDENCE);
    this.a = a;
    this.b = b;
  }

}
