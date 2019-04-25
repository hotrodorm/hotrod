package sql.predicates;

import java.sql.Date;

public class Constant extends Expression {

  private static final int PRECEDENCE = 20;

  public static enum JDBCType {
    VARCHAR, NUMERIC, DATE, TIMESTAMP, BOOLEAN
  };

  // Properties

  private Object value;
  private JDBCType type;

  private boolean parameterize;

  // Constructors

  public Constant(final String value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.VARCHAR;
    this.parameterize = true;
  }

  public Constant(final Character value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.VARCHAR;
    this.parameterize = true;
  }

  public Constant(final Number value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.NUMERIC;
    this.parameterize = false;
  }

  public Constant(final Boolean value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.BOOLEAN;
    this.parameterize = false;
  }

  public Constant(final Date value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.DATE;
    this.parameterize = true;
  }

  public Constant(final java.util.Date value) {
    super(PRECEDENCE);
    this.value = value;
    this.type = JDBCType.TIMESTAMP;
    this.parameterize = true;
  }

  public Constant(final Object value, final JDBCType type) {
    super(PRECEDENCE);
    this.value = value;
    if (type == null) {
      throw new IllegalArgumentException("Specified type cannot be null");
    }
    this.type = type;
    this.parameterize = true;
  }

  // Rendering

  // @Override
  // protected void render(final PreparedQuery pq) {
  // QueryWriter w = pq.getWriter();
  // if (this.parameterize) {
  // String name = pq.registerParameter(this.value);
  // w.text("#{" + name + ",jdbcType=" + this.type + "}");
  // } else {
  // w.text("" + this.value);
  // }
  // }

}
