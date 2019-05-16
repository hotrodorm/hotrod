package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.QueryWriter;

public class NullConstant<T> extends Expression<T> {

  // Properties

  private JDBCType type;

  // Constructor

  public NullConstant(final JDBCType type) {
    super(Expression.PRECEDENCE_LITERAL);
    if (type == null) {
      throw new IllegalArgumentException("Specified type cannot be null");
    }
    this.type = type;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    String name = w.registerParameter(null);
    w.write("#{" + name + ",jdbcType=" + this.type + "}");
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    // nothing to do
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    // nothing to do
  }

}
