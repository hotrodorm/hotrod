package sql.predicates;

public class Predicate extends Expression {

  protected Predicate(final int precedence) {
    super(precedence);
  }

  public static Predicate or(final Predicate a, final Predicate b) {
    return new Or(a, b);
  }

  public static Predicate and(final Predicate a, final Predicate b) {
    return new And(a, b);
  }

  public static Predicate not(final Predicate a) {
    return new Not(a);
  }

}
