package sql.predicates;

public abstract class Predicate extends Expression {

  protected Predicate(final int precedence) {
    super(precedence);
  }

}
