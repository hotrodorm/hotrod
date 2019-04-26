package sql.predicates;

public class Or extends BinaryBooleanOperator {

  private static final int PRECEDENCE = 12;

  public Or(final Predicate a, final Predicate b) {
    super(a, "or", b, PRECEDENCE);
  }

}
