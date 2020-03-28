package explain.mysql8;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import explain.Operator;

public class MySQLOperator implements Operator {

  // Properties

  private Integer id;
  private String type;
  private Boolean includesHeapFetch;
  private Double cost;
  private Double examinedRows;
  private Long producedBytes;
  private Double producedRows;
  private String rowsSource;
  private String rowsSourceAlias;
  private String indexName;
  private List<String> accessPredicates;
  private List<String> filterPredicates;
  private List<MySQLOperator> innerOperators;
  private LinkedHashMap<String, String> extraProperties;

  // Constructor

  public MySQLOperator(Integer id, String type, boolean includesHeapFetch, Double cost, Double examinedRows,
      Long producedBytes, Double producedRows, String rowsSource, String rowsSourceAlias, String indexName,
      List<String> accessPredicates, List<String> filterPredicates, List<MySQLOperator> innerOperators,
      LinkedHashMap<String, String> extraProperties) {
    this.id = id;
    this.type = type;
    this.includesHeapFetch = includesHeapFetch;
    this.cost = cost;
    this.examinedRows = examinedRows;
    this.producedBytes = producedBytes;
    this.producedRows = producedRows;
    this.rowsSource = rowsSource;
    this.rowsSourceAlias = rowsSourceAlias;
    this.indexName = indexName;
    this.accessPredicates = accessPredicates;
    this.filterPredicates = filterPredicates;
    this.innerOperators = innerOperators;
    this.extraProperties = extraProperties;
  }

  // Behavior

  public void addInner(final MySQLOperator io) {
    this.innerOperators.add(io);
  }

  // Getters

  @Override
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  // Type

  @Override
  public String getType() {
    return type;
  }


  @Override
  public String getJoinType() {
    // TODO: fix!
    return null;
  }

  @Override
  public boolean includesHeapFetch() {
    return includesHeapFetch;
  }

  // Estimated Cost

  @Override
  public Double getCost() {
    return cost;
  }

  @Override
  public Double getExaminedRows() {
    return examinedRows;
  }

  @Override
  public Long getProducedBytes() {
    return producedBytes;
  }

  @Override
  public Double getProducedRows() {
    return producedRows;
  }

  // Actual Cost

  @Override
  public Double getActualTime() {
    return null;
  }

  @Override
  public Long getActualRows() {
    return null;
  }

  @Override
  public Long getActualLoops() {
    return null;
  }

  // Source of rows

  @Override
  public String getRowsSource() {
    return rowsSource;
  }

  @Override
  public String getRowsSourceAlias() {
    return rowsSourceAlias;
  }

  @Override
  public String getIndexName() {
    return indexName;
  }

  @Override
  public List<String> getAccessPredicates() {
    return accessPredicates;
  }

  // Filtering

  @Override
  public List<String> getFilterPredicates() {
    return filterPredicates;
  }

  // Sources

  @Override
  public List<Operator> getInnerOperators() {
    List<Operator> opes = new ArrayList<Operator>();
    for (MySQLOperator mo : this.innerOperators) {
      opes.add(mo);
    }
    return opes;
  }

  // Extra Properties

  @Override
  public LinkedHashMap<String, String> getExtraProperties() {
    return extraProperties;
  }

}
