package metadata;

import sql.QueryWriter;
import sql.predicates.Expression;
import sql.predicates.Predicate;

public class Column extends Expression {

  private static final int PRECEDENCE = 20;

  private TableOrView t;

  private String catalog;
  private String schema;
  private String table;
  private String name;
  private int dataType;
  private String typeName;
  private Integer columnSize;
  private Integer decimalDigits;
  private boolean nullable;
  private String defaultValue;
  private int ordinalPosition;
  private boolean serial;
  private boolean lob;

  public Column(final TableOrView t, final String name) {
    super(PRECEDENCE);
    this.t = t;
    this.name = name;
  }

  // Column ordering

  public ColumnOrdering asc() {
    return new ColumnOrdering(this, true);
  }

  public ColumnOrdering desc() {
    return new ColumnOrdering(this, false);
  }

  // Behavior

  public Predicate between(final Expression from, final Expression to) {
    return null;
  }

  public Predicate equals(final Expression e) {
    return null;
  }

  public Predicate greaterThan(final Expression e) {
    return null;
  }

  public Predicate greaterThanOrEqual(final Expression e) {
    return null;
  }

  public Predicate isNotNull(final Expression e) {
    return null;
  }

  public Predicate isNull(final Expression e) {
    return null;
  }

  public Predicate lessThan(final Expression e) {
    return null;
  }

  public Predicate lessThanOrEqual(final Expression e) {
    return null;
  }

  public Predicate like(final Expression e) {
    return null;
  }

  public Predicate notBetween(final Expression e) {
    return null;
  }

  public Predicate notEqual(final Expression e) {
    return null;
  }

  public Predicate notLike(final Expression e) {
    return null;
  }

  // Getters

  public String getName() {
    return this.name;
  }

  @Override
  public void renderTo(final QueryWriter pq) {
    pq.write(pq.getSqlDialect().renderName(this.name));
  }

}
