package sql.predicates;

public class Or extends Predicate {

  private static final int PRECEDENCE = 12;

  private Predicate a;
  private Predicate b;

  public Or(final Predicate a, final Predicate b) {
    super(PRECEDENCE);
    this.a = a;
    this.b = b;
  }

}
