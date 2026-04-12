package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.TypedSQLName;
import org.hotrod.typesolver.UnresolvableDataTypeException;

public class StructuredColumnMetadata extends ColumnMetadata {

  // Properties

  private String entityPrefix;
  private String columnAlias;
  private String formula;
  private boolean id;
  private AbstractConfigurationTag tag;
  private ColumnsProvider columnsProvider;

  // Constructor

  public StructuredColumnMetadata(final ColumnMetadata cm, final String entityPrefix, final String columnAlias,
      final boolean id, final AbstractConfigurationTag tag) {
    super(cm);
    this.entityPrefix = entityPrefix;
    this.columnAlias = columnAlias;
    this.formula = null;
    this.id = id;
    this.tag = tag;
    this.columnsProvider = null;
  }

  public StructuredColumnMetadata(final ColumnMetadata cm, final String entityPrefix, final String columnAlias,
      final boolean id, final AbstractConfigurationTag tag, final ColumnsProvider columnsProvider) {
    super(cm);
    this.entityPrefix = entityPrefix;
    this.columnAlias = columnAlias;
    this.formula = null;
    this.id = id;
    this.tag = tag;
    this.columnsProvider = columnsProvider;
  }

  // Behavior

  public static StructuredColumnMetadata applyColumnTag(final StructuredColumnMetadata orig, final ColumnTag t,
      final AbstractConfigurationTag tag, final DatabaseAdapter adapter)
      throws UnresolvableDataTypeException, InvalidIdentifierException {
    ColumnMetadata cm = ColumnMetadata.applyColumnTag(orig, t, adapter);
    return new StructuredColumnMetadata(cm, orig.entityPrefix, orig.columnAlias, orig.id, tag);
  }

  // Setters

  public void setId(final boolean id) {
    this.id = id;
  }

  public void setFormula(final String formula) {
    this.formula = formula;
  }

  // Getters

  public String getColumnAlias() {
    return columnAlias;
  }

  public String getFormula() {
    return formula;
  }

  public boolean isId() {
    return id;
  }

  public String getEntityPrefix() {
    return entityPrefix;
  }

  public AbstractConfigurationTag getTag() {
    return this.tag;
  }

  public ColumnsProvider getColumnsProvider() {
    return columnsProvider;
  }

  // Rendering

  public String renderAliasedSQLColumn() {
    if (this.formula == null) {
      return this.entityPrefix + "." + super.getId().getRenderedSQLName() + " as " + this.columnAlias;
    } else {
      return this.formula + " as " + this.columnAlias;
    }
  }

  // Utilities

  public static List<StructuredColumnMetadata> promote(final String entityPrefix, final List<ColumnMetadata> cols,
      final String aliasPrefix) {
    if (aliasPrefix == null) {
      throw new IllegalArgumentException("aliasPrefix cannot be null!");
    }
    List<StructuredColumnMetadata> columns = new ArrayList<StructuredColumnMetadata>();
    for (ColumnMetadata cm : cols) {
      StructuredColumnMetadata m = new StructuredColumnMetadata(cm, entityPrefix, aliasPrefix + cm.getName(),
          cm.belongsToPK(), null);
      columns.add(m);
    }
    return columns;
  }

  public static List<StructuredColumnMetadata> promote(final String entityPrefix, final List<ColumnMetadata> cols,
      final String aliasPrefix, final Set<TypedSQLName> idNames) throws IdColumnNotFoundException {
    if (aliasPrefix == null) {
      throw new IllegalArgumentException("aliasPrefix cannot be null!");
    }

    for (TypedSQLName idName : idNames) {
      if (!idIsColumn(cols, idName)) {
        throw new IdColumnNotFoundException(idName);
      }
    }

    List<StructuredColumnMetadata> columns = new ArrayList<StructuredColumnMetadata>();
    for (ColumnMetadata cm : cols) {
      boolean id = columnIsId(cm, idNames);
      StructuredColumnMetadata m = new StructuredColumnMetadata(cm, entityPrefix, aliasPrefix + cm.getName(), id, null);
      columns.add(m);
    }
    return columns;
  }

  private static boolean columnIsId(final ColumnMetadata cm, final Set<TypedSQLName> idNames) {
    for (TypedSQLName idName : idNames) {
      if (cm.isConfigurationName(idName)) {
        return true;
      }
    }
    return false;
  }

  private static boolean idIsColumn(final List<ColumnMetadata> cols, final TypedSQLName idName) {
    for (ColumnMetadata cm : cols) {
      if (cm.isConfigurationName(idName)) {
        return true;
      }
    }
    return false;
  }

  public static class IdColumnNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private TypedSQLName idName;

    public IdColumnNotFoundException(final TypedSQLName idName) {
      super();
      this.idName = idName;
    }

    public TypedSQLName getIdName() {
      return idName;
    }

  }

}
