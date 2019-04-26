package sql.predicates;

public class And extends BinaryBooleanOperator {

  private static final int PRECEDENCE = 11;

  public And(final Predicate a, final Predicate b) {
    super(a, "and", b, PRECEDENCE);
  }

}
