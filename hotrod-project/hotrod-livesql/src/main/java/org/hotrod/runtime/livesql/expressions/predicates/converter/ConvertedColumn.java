package org.hotrod.runtime.livesql.expressions.predicates.converter;

import org.hotrod.converter.TypeConverter;
import org.hotrod.runtime.livesql.expressions.EquatableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.SortableExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.EntityColumn;
import org.hotrod.runtime.livesql.metadata.MDHelper;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;

public class ConvertedColumn<R, D> extends EquatableExpression implements EntityColumn {

  private TableOrView objectInstance;
  private String canonicalName;
  private TypeConverter<R, D> converter;

  private String property;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;

  public ConvertedColumn(final TableOrView objectInstance, final String canonicalName, final String property,
      final String type, final Integer columnSize, final Integer decimalDigits, final TypeHandler<R, D> handler,
      final TypeConverter<R, D> converter) {
    super(Expression.PRECEDENCE_COLUMN);
    this.objectInstance = objectInstance;
    this.canonicalName = canonicalName;
    this.converter = converter;
    this.property = property;
    this.type = type;
    this.columnSize = columnSize;
    this.decimalDigits = decimalDigits;
    super.setTypeHandler(handler);
  }

  public Predicate eq(final D d) {
    return new ConvertedEqual<R, D>(this, this.converter, d);
  }

  public Predicate ne(final D d) {
    return new ConvertedNotEqual<R, D>(this, this.converter, d);
  }

  @SuppressWarnings("unchecked")
  public Predicate in(final D... d) {
    return new ConvertedIn<R, D>(this, this.converter, d);
  }

  @SuppressWarnings("unchecked")
  public Predicate notIn(final D... d) {
    return new ConvertedNotIn<R, D>(this, this.converter, d);
  }

  public SortableExpression coalesce(final D d) {
    return new ConvertedCoalesce<R, D>(this, this.converter, d);
  }

  public SortableExpression nullIf(final D d) {
    return new ConvertedNullIf<R, D>(this, this.converter, d);
  }

  protected void renderTo(QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(
          w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.objectInstance.getAlias())));
      w.write(".");
    }
    w.write(w.getSQLDialect().canonicalToNatural(this.canonicalName));
  }

  // EntityColumn

  @Override
  public TableOrView getObjectInstance() {
    return this.objectInstance;
  }

  @Override
  public Name getCatalog() {
    return this.objectInstance.getCatalog();
  }

  @Override
  public Name getSchema() {
    return this.objectInstance.getSchema();
  }

  @Override
  public Name getObjectName() {
    return MDHelper.getName(this.objectInstance);
  }

  @Override
  public String getCanonicalName() {
    return this.canonicalName;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public Integer getColumnSize() {
    return this.columnSize;
  }

  @Override
  public Integer getDecimalDigits() {
    return this.decimalDigits;
  }

  @Override
  public final String getProperty() {
    return property;
  }

  @Override
  public String getReferenceName() {
    return this.property;
  }

}
