package sql.predicates;

public class Not extends Predicate {

  private static final int PRECEDENCE = 2;

  private Predicate a;

  public Not(final Predicate a) {
    super(PRECEDENCE);
    this.a = a;
  }

}
