package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferencesValidator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;

public class DateTimeColumn extends DateTimeExpression implements Column {

  private static final int PRECEDENCE = 1;

  // Properties

  private TableOrView objectInstance;

  private String name;
  private String type;
  private Integer columnSize;
  private Integer decimalDigits;
  private boolean nullable;
  private String defaultValue;
  private boolean lob;

  private String property;

  // Constructor

  public DateTimeColumn(final TableOrView objectInstance, final String name, final String property) {
    super(PRECEDENCE);
    this.objectInstance = objectInstance;
    this.name = name;
    this.property = property;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(this.objectInstance.getAlias());
      w.write(".");
    }
    w.write(w.getSqlDialect().getIdentifierRenderer().renderSQLName(this.name));
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferencesValidator tableReferences, final AliasGenerator ag) {
    // nothing to do. No inner queries
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    // nothing to do. No inner queries
  }

  // Getters

  @Override
  public final String getName() {
    return this.name;
  }

  @Override
  public final TableOrView getObjectInstance() {
    return objectInstance;
  }

  @Override
  public final String getCatalog() {
    return this.objectInstance.getCatalog();
  }

  @Override
  public final String getSchema() {
    return this.objectInstance.getSchema();
  }

  @Override
  public final String getObjectName() {
    return this.objectInstance.getName();
  }

  @Override
  public final String getType() {
    return type;
  }

  @Override
  public final Integer getColumnSize() {
    return columnSize;
  }

  @Override
  public final Integer getDecimalDigits() {
    return decimalDigits;
  }

  @Override
  public final boolean isNullable() {
    return nullable;
  }

  @Override
  public final String getDefaultValue() {
    return defaultValue;
  }

  @Override
  public final boolean isLob() {
    return lob;
  }

  @Override
  public final String getProperty() {
    return property;
  }

}
