package org.plan.operator;

import java.util.List;

import org.plan.metrics.Metrics;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;

public abstract class Operator<T extends Comparable<T>> {

  private T id;
  private String genericName;
  private String specificName;
  private String joinType;

  private SourceSet sourceSet;

  private List<AccessPredicate> accessPredicates;
  private List<FilterPredicate> filterPredicates;
  private List<Operator<T>> children;

  private Metrics metrics;

  public Operator(final String genericName, final T id, final String specificName, final String joinType,
      final SourceSet sourceSet, final List<AccessPredicate> accessPredicates,
      final List<FilterPredicate> filterPredicates, final List<Operator<T>> children, final Metrics metrics) {
    this.genericName = genericName;
    this.id = id;
    this.specificName = specificName;
    this.joinType = joinType;
    this.sourceSet = sourceSet;
    this.accessPredicates = accessPredicates;
    this.filterPredicates = filterPredicates;
    this.children = children;
    this.metrics = metrics;
  }

  public String getGenericName() {
    return genericName;
  }

  public T getId() {
    return id;
  }

  public String getSpecificName() {
    return specificName;
  }

  public String getJoinType() {
    return joinType;
  }

  public SourceSet getSourceSet() {
    return sourceSet;
  }

  public List<AccessPredicate> getAccessPredicates() {
    return accessPredicates;
  }

  public List<FilterPredicate> getFilterPredicates() {
    return filterPredicates;
  }

  public List<Operator<T>> getChildren() {
    return children;
  }

  public Metrics getMetrics() {
    return metrics;
  }

  // Classes

  public static class SourceSet {

    private String sourceTable;
    private String tableAlias;
    private String sourceIndex;
    private List<IndexColumn> sourceIndexColumns;
    private boolean includesHeapFetch;

    public SourceSet(final String sourceTable, final String tableAlias, final String sourceIndex,
        final List<IndexColumn> sourceIndexColumns, final boolean includesHeapFetch) {

      if (sourceTable == null && sourceIndex == null) {
        throw new IllegalArgumentException("sourceTable or sourceIndex must be specified; both cannot be null");
      }
      if (sourceIndexColumns != null && sourceIndex == null) {
        throw new IllegalArgumentException("sourceIndexColumns cannot be specified when sourceIndex is null");
      }

      this.sourceTable = sourceTable;
      this.tableAlias = tableAlias;
      this.sourceIndex = sourceIndex;
      this.sourceIndexColumns = sourceIndexColumns;
      this.includesHeapFetch = includesHeapFetch;
    }

    public String getSourceTable() {
      return sourceTable;
    }

    public String getTableAlias() {
      return tableAlias;
    }

    public String getSourceIndex() {
      return sourceIndex;
    }

    public List<IndexColumn> getSourceIndexColumns() {
      return sourceIndexColumns;
    }

    public boolean includesHeapFetch() {
      return includesHeapFetch;
    }

  }

  public static class IndexColumn {

    private String columnName;
    private String expression;
    private boolean ascending;

    public IndexColumn(final String columnName, final String expression, final boolean ascending) {

      if (columnName == null && expression == null) {
        throw new IllegalArgumentException("At least columnName or expression must be specified");
      }
      if (columnName != null && expression != null) {
        throw new IllegalArgumentException("columnName and expression cannot be specified simultaneously");
      }

      this.columnName = columnName;
      this.expression = expression;
      this.ascending = ascending;
    }

    public String getColumnName() {
      return columnName;
    }

    public String getExpression() {
      return expression;
    }

    public boolean isAscending() {
      return ascending;
    }

  }

}
