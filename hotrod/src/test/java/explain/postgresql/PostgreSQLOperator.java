package explain.postgresql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import explain.Operator;

public class PostgreSQLOperator implements Operator {

  // Properties

  private static int nextId = 1;

  private int id;
  private String type;
  private String joinType;
  private String parentRelationship;
  private String alias;
  private String indexName;
  private String indexDescription;
  private String indexCond;
  private String recheckCond;
  private Double startupCost;
  private Double cost;
  private Double producedRows;
  private Double width;
  private String relationName;
  private String filter;
  private List<PostgreSQLOperator> sources;
  private boolean includesHeapFetch;

  // Constructor

  public PostgreSQLOperator(final int id, final String type, final String joinType, final String parentRelationship,
      final String alias, final String indexName, final String indexDescription, final String indexCond,
      final String recheckCond, final Double startupCost, final Double cost, final Double producedRows,
      final Double width, final String relationName, final String filter, final boolean includesHeapFetch,
      final List<PostgreSQLOperator> sources) {
    this.id = id;
    this.type = type;
    this.joinType = joinType;
    this.parentRelationship = parentRelationship;
    this.alias = alias;
    this.indexName = indexName;
    this.indexDescription = indexDescription;
    this.indexCond = indexCond;
    this.recheckCond = recheckCond;
    this.startupCost = startupCost;
    this.cost = cost;
    this.producedRows = producedRows;
    this.width = width;
    this.relationName = relationName;
    this.filter = filter;
    this.includesHeapFetch = includesHeapFetch;
    this.sources = sources;
  }

  public static int produceUniqueId() {
    synchronized (PostgreSQLOperator.class) {
      int n = nextId;
      nextId++;
      return n;
    }
  }

  // Getters

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public String getType() {
    return this.type + (this.joinType == null ? "" : " " + this.joinType + " Join");
  }

  @Override
  public boolean includesHeapFetch() {
    return includesHeapFetch;
  }

  @Override
  public Double getCost() {
    return cost;
  }

  @Override
  public Double getExaminedRows() {
    return producedRows;
  }

  @Override
  public Long getProducedBytes() {
    if (this.width == null || this.producedRows == null) {
      return null;
    } else {
      Double bytes = this.width * this.producedRows;
      return bytes.longValue();
    }
  }

  @Override
  public Double getProducedRows() {
    return producedRows;
  }

  @Override
  public String getRowsSource() {
    return relationName;
  }

  @Override
  public String getRowsSourceAlias() {
    return this.alias;
  }

  @Override
  public String getIndexName() {
    return indexName;
  }

  @Override
  public String getIndexDescription() {
    return this.indexDescription;
  }

  @Override
  public List<String> getAccessPredicates() {
    List<String> access = new ArrayList<String>();
    if (this.indexCond != null) {
      access.add(this.indexCond);
    }
    return access;
  }

  @Override
  public List<String> getFilterPredicates() {
    List<String> filters = new ArrayList<String>();
    if (this.filter != null) {
      filters.add(this.filter);
    }
    if (this.recheckCond != null) {
      filters.add(this.recheckCond);
    }
    return filters;
  }

  public List<Operator> getInnerOperators() {
    List<Operator> opes = new ArrayList<Operator>();
    for (PostgreSQLOperator o : this.sources) {
      opes.add(o);
    }
    return Collections.unmodifiableList(opes);
  }

  // Extra

  public LinkedHashMap<String, String> getExtraProperties() {
    LinkedHashMap<String, String> p = new LinkedHashMap<String, String>();
    // if (this.joinType != null) {
    // p.put("Join Type", this.joinType);
    // }
    // if (this.parentRelationship != null) {
    // p.put("Parent Relationship", this.parentRelationship);
    // }
    // if (this.alias != null) {
    // p.put("Alias", this.alias);
    // }
    // if (this.startupCost != null) {
    // p.put("Startup Cost", "" + this.startupCost);
    // }
    // if (this.width != null) {
    // p.put("Width", "" + this.width);
    // }
    return p;
  }

}
