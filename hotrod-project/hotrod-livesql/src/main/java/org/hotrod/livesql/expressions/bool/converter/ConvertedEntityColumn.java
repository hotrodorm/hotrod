package org.hotrod.livesql.expressions.bool.converter;

import java.util.Arrays;
import java.util.logging.Logger;

import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SortableExpression;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.ConvertedValue;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class ConvertedEntityColumn<R, D> extends EquatableExpression implements EntityColumn {

  private static final Logger log = Logger.getLogger(ConvertedEntityColumn.class.getName());

  private TableOrView<?, ?, ?> objectInstance;
  private ConvertedEntityColumnMetaData<R, D> metaData;

  public ConvertedEntityColumn(final TableOrView<?, ?, ?> objectInstance,
      final ConvertedEntityColumnMetaData<R, D> metaData) {
    super(Expression.PRECEDENCE_COLUMN);
    log.fine("init");
    this.objectInstance = objectInstance;
    this.metaData = metaData;
  }

  @Override
  protected TypeHandler<?, ?> getTypeHandler() {
    return this.metaData.getTypeHandler();
  }

  // Operators

  public BooleanSyntaxExpression eq(final D d) {
    ConvertedValue cv = ConvertedValue.of(d, this.metaData.getConverterToObject());
    return new ConvertedEqual<R, D>(this, cv);
  }

  public BooleanSyntaxExpression ne(final D d) {
    ConvertedValue cv = ConvertedValue.of(d, this.metaData.getConverterToObject());
    return new ConvertedNotEqual<R, D>(this, cv);
  }

  @SuppressWarnings("unchecked")
  public BooleanSyntaxExpression in(final D... d) {
    ConvertedValue[] vs = Arrays.stream(d).map(v -> ConvertedValue.of(v, this.metaData.getConverterToObject()))
        .toArray(ConvertedValue[]::new);
    return new ConvertedIn<R, D>(this, vs);
  }

  @SuppressWarnings("unchecked")
  public BooleanSyntaxExpression notIn(final D... d) {
    ConvertedValue[] vs = Arrays.stream(d).map(v -> ConvertedValue.of(v, this.metaData.getConverterToObject()))
        .toArray(ConvertedValue[]::new);
    return new ConvertedNotIn<R, D>(this, vs);
  }

  public SortableExpression coalesce(final D d) {
    ConvertedValue cv = ConvertedValue.of(d, this.metaData.getConverterToObject());
    return new ConvertedCoalesce<R, D>(this, cv);
  }

  public SortableExpression nullIf(final D d) {
    ConvertedValue cv = ConvertedValue.of(d, this.metaData.getConverterToObject());
    return new ConvertedNullIf<R, D>(this, cv);
  }

  // Rendering

  @Override
  protected void renderTo(QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(
          w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.objectInstance.getAlias())));
      w.write(".");
    }
    w.write(w.getSQLDialect().canonicalToNatural(this.metaData.getName().getName()));
  }

  // Converted Entity Column

  @Override
  public TableOrView<?, ?, ?> getObjectInstance() {
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
    return MDShield.getName(this.objectInstance);
  }

  @Override
  public Name getName() {
    return this.metaData.getName();
  }

  @Override
  public String getType() {
    return this.metaData.getType();
  }

  @Override
  public Integer getColumnSize() {
    return this.metaData.getColumnSize();
  }

  @Override
  public Integer getDecimalDigits() {
    return this.metaData.getDecimalDigits();
  }

  @Override
  public final String getProperty() {
    return this.metaData.getProperty();
  }

  @Override
  public String getReferenceName() {
    return this.metaData.getReferenceName();
  }

}
