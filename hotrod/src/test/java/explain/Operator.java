package explain;

import java.util.LinkedHashMap;
import java.util.List;

public interface Operator {

  // Node Identification

  Integer getId();

  // Type

  String getType();

  boolean includesHeapFetch();

  // Estimated Cost

  Double getCost();

  Double getExaminedRows();

  Long getProducedBytes();

  Double getProducedRows();

  // Actual Cost

  Double getActualTime();

  Long getActualRows();

  Long getActualLoops();

  // Source of rows

  String getRowsSource();

  String getRowsSourceAlias();

  String getIndexName();

  List<String> getAccessPredicates();

  // Filtering

  List<String> getFilterPredicates();

  // Sources

  List<Operator> getInnerOperators();

  // Extra Properties

  LinkedHashMap<String, String> getExtraProperties();

}
