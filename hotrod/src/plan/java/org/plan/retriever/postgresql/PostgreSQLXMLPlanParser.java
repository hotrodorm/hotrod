package org.plan.retriever.postgresql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.plan.ExecutionPlan;
import org.plan.metrics.Metrics;
import org.plan.metrics.MetricsFactory;
import org.plan.operator.HashAggregateOperator;
import org.plan.operator.HashJoinOperator;
import org.plan.operator.HashOperator;
import org.plan.operator.IndexOnlyScanOperator;
import org.plan.operator.IndexRangeScanOperator;
import org.plan.operator.LimitOperator;
import org.plan.operator.NestedLoopOperator;
import org.plan.operator.Operator;
import org.plan.operator.Operator.IndexColumn;
import org.plan.operator.Operator.SourceSet;
import org.plan.operator.ResultOperator;
import org.plan.operator.TableScanOperator;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import explain.InvalidPlanException;

public class PostgreSQLXMLPlanParser {

  // explain (analyze true, timing true, format text)
  // select a.id, a.name, t.time, t.amount, b.id, b.name
  // from account a
  // join transaction t on t.account_id = a.id
  // join federal_branch b on b.id = t.fed_branch_id
  // where a.name like '%0%'
  // and t.amount < 500
  // and b.name like '%a%'

  // select
  // *
  // from account a
  // join transaction t4 on t4.account_id = a.id
  // join federal_branch b5 on b5.id = t4.fed_branch_id
  // join
  // (
  // select
  // max(account_id) as account_id
  // from transaction t7
  // )
  // t6 on t6.account_id = a.id
  // where a.current_balance < 3 *
  // (
  // select
  // avg(amount)
  // from transaction t
  // join federal_branch b on b.id = t.fed_branch_id
  // where t.account_id = a.id
  // and b.name in (select name from federal_branch b7 where name like '%ar%')
  // )
  // and a.current_balance < 5 *
  // (
  // select
  // avg(amount)
  // from transaction t2
  // join federal_branch b2 on b2.id = t2.fed_branch_id
  // where b2.name not in (select name from federal_branch b8 where name like
  // '%y%')
  // )

  public static ExecutionPlan<Long> retrievePlan(final Connection conn, final String sqlQuery,
      final boolean executeForActualMetrics) throws SQLException, InvalidPlanException {

    Date now = new Date();
    SequenceGenerator seq = new SequenceGenerator();

    boolean includesEstimatedMetrics = true;
    boolean includesActualMetrics = executeForActualMetrics;
    MetricsFactory metricsFactory = MetricsFactory.instantiate(includesEstimatedMetrics, includesActualMetrics);

    String doc = retrieveXMLPlan(conn, sqlQuery, executeForActualMetrics);
    Operator<Long> rootOperator = parseDoc(conn, doc, seq, metricsFactory, executeForActualMetrics);

    LinkedHashMap<String, Object> parameterValues = new LinkedHashMap<String, Object>();
    parameterValues.put("id", new Integer(10));
    parameterValues.put("minDate", new Date());

    ExecutionPlan<Long> plan = ExecutionPlan.instantiate("N/A", now, sqlQuery, parameterValues, rootOperator,
        includesEstimatedMetrics, includesActualMetrics);
    return plan;
  }

  private static String retrieveXMLPlan(final Connection conn, final String sqlQuery,
      final boolean executeForActualMetrics) throws SQLException {
    String sql = "explain (" //
        + (executeForActualMetrics ? "analyze true, timing true, " : "") //
        + "format xml) " + sqlQuery;
    Statement st = conn.createStatement();
    System.out.println("sql=" + sql);
    ResultSet rs = st.executeQuery(sql);
    StringBuilder sb = new StringBuilder();
    while (rs.next()) {
      sb.append(rs.getString(1));
      sb.append("\n");
    }
    return sb.toString();
  }

  private static Operator<Long> parseDoc(final Connection conn, final String doc, final SequenceGenerator seq,
      final MetricsFactory metricsFactory, final boolean executeForActualMetrics)
      throws InvalidPlanException, SQLException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document d;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      d = dBuilder.parse(new ByteArrayInputStream(doc.getBytes("UTF-8")));
    } catch (UnsupportedEncodingException e1) {
      throw new InvalidPlanException(e1.getMessage());
    } catch (ParserConfigurationException e1) {
      throw new InvalidPlanException(e1.getMessage());
    } catch (SAXException e1) {
      throw new InvalidPlanException(e1.getMessage());
    } catch (IOException e1) {
      throw new InvalidPlanException(e1.getMessage());
    }

    Element explainTag = d.getDocumentElement();
    // System.out.println("explain=" + explainTag.getTagName());

    NodeList l1 = explainTag.getChildNodes();
    Node queryTag = l1.item(1);
    // System.out.println("query=" + queryTag.getNodeName());

    NodeList l2 = queryTag.getChildNodes();
    Node opTag = l2.item(1);
    // System.out.println("opTag=" + opTag.getNodeName());

    Operator<Long> op = parseOperatorTag(conn, opTag, seq, metricsFactory, executeForActualMetrics);

    return op;
  }

  private static Operator<Long> parseOperatorTag(final Connection conn, final Node operatorTag,
      final SequenceGenerator seq, final MetricsFactory metricsFactory, final boolean executeForActualMetrics)
      throws InvalidPlanException, SQLException {

    long id = seq.next();

    String nodeType = null;
    String joinType = null;
    String parentRelationship = null;
    String alias = null;
    String scanDirection = null;
    String indexName = null;
    String indexCond = null;
    String recheckCond = null;
    Double startupCost = null;
    Double totalCost = null;
    Double planRows = null;
    Double planWidth = null;
    String relationName = null;
    String filter = null;
    Double actualTotalTime = null;
    Long actualRows = null;
    Long actualLoops = null;
    Long heapFetches = null;

    List<Operator<Long>> sources = new ArrayList<Operator<Long>>();

    NodeList children = operatorTag.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node n = children.item(i);
      // System.out.println("n.getNodeName()=" + n.getNodeName());
      if ("Node-Type".equals(n.getNodeName())) {
        nodeType = getString(n);
      } else if ("Join-Type".equals(n.getNodeName())) {
        joinType = getString(n);
      } else if ("Parent-Relationship".equals(n.getNodeName())) {
        parentRelationship = getString(n);
      } else if ("Alias".equals(n.getNodeName())) {
        alias = getString(n);
      } else if ("Scan-Direction".equals(n.getNodeName())) {
        scanDirection = getString(n);
      } else if ("Index-Name".equals(n.getNodeName())) {
        indexName = getString(n);
      } else if ("Index-Cond".equals(n.getNodeName())) {
        indexCond = getString(n);
      } else if ("Recheck-Cond".equals(n.getNodeName())) {
        recheckCond = getString(n);
      } else if ("Startup-Cost".equals(n.getNodeName())) {
        startupCost = getDouble(n);
      } else if ("Total-Cost".equals(n.getNodeName())) {
        totalCost = getDouble(n);
      } else if ("Plan-Rows".equals(n.getNodeName())) {
        planRows = getDouble(n);
      } else if ("Plan-Width".equals(n.getNodeName())) {
        planWidth = getDouble(n);
      } else if ("Relation-Name".equals(n.getNodeName())) {
        relationName = getString(n);
      } else if ("Filter".equals(n.getNodeName())) {
        filter = getString(n);
      } else if ("Actual-Total-Time".equals(n.getNodeName())) {
        actualTotalTime = getDouble(n);
      } else if ("Actual-Rows".equals(n.getNodeName())) {
        actualRows = getLong(n);
      } else if ("Actual-Loops".equals(n.getNodeName())) {
        actualLoops = getLong(n);
      } else if ("Heap-Fetches".equals(n.getNodeName())) {
        heapFetches = getLong(n);
      } else if ("Plans".equals(n.getNodeName())) {
        NodeList plans = n.getChildNodes();
        for (int j = 0; j < plans.getLength(); j++) {
          Node item = plans.item(j);
          if ("Plan".equals(item.getNodeName())) {
            Operator<Long> so = parseOperatorTag(conn, item, seq, metricsFactory, executeForActualMetrics);
            sources.add(so);
          }
        }
      }
    }

    // Source set

    boolean includesHeapFetch = "Bitmap Heap Scan".equals(nodeType) //
        || "Index Scan".equals(nodeType) //
        || "Seq Scan".equals(nodeType) //
    ;

    SourceSet sourceSet = null;
    if (indexName != null) {
      sourceSet = getIndexSource(conn, indexName, alias, includesHeapFetch);
    } else if (relationName != null) {
      sourceSet = new SourceSet(relationName, alias, null, null, includesHeapFetch);
    }

    // Access predicates

    List<AccessPredicate> accessPredicates = new ArrayList<AccessPredicate>();
    if ("Backward".equals(scanDirection)) {
      accessPredicates.add(new AccessPredicate("Scan Direction: " + scanDirection));
    }
    if (heapFetches != null) {
      accessPredicates.add(new AccessPredicate("Heap Fetches: " + heapFetches));
    }

    // Filter predicates

    List<FilterPredicate> filterPredicates = new ArrayList<FilterPredicate>();
    if (filter != null) {
      filterPredicates.add(new FilterPredicate(filter));
    }
    if (indexCond != null) {
      filterPredicates.add(new FilterPredicate(indexCond));
    }
    if (recheckCond != null) {
      filterPredicates.add(new FilterPredicate(recheckCond));
    }

    // Metrics

    Metrics metrics;
    if (executeForActualMetrics) {
      if (totalCost == null) {
        throw new InvalidPlanException("<Total-Cost> not found in plan node of type <" + nodeType + ">");
      }
      if (actualTotalTime == null) {
        throw new InvalidPlanException("<Actual-Total-Time> not found in plan node of type <" + nodeType + ">");
      }
      metrics = metricsFactory.getMetrics(totalCost, planRows, actualTotalTime, actualRows, actualLoops);
    } else {
      if (totalCost == null) {
        throw new InvalidPlanException("<Total-Cost> not found in plan node of type <" + nodeType + ">");
      }
      metrics = metricsFactory.getMetrics(totalCost, planRows, null, null, null);
    }

    // Operator

    // TODO: Use joinType ('inner', 'outer', ...)

    if ("Nested Loop".equals(nodeType)) {
      return new NestedLoopOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources,
          metrics);
    } else if ("Aggregate".equals(nodeType)) { // TODO: is it Hash aggregate or
                                               // sort aggregate?
      return new HashAggregateOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources,
          metrics);
    } else if ("Hash Join".equals(nodeType)) {
      return new HashJoinOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources, metrics);
    } else if ("Seq Scan".equals(nodeType)) {
      return new TableScanOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources, metrics);
    } else if ("Hash".equals(nodeType)) {
      return new HashOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources, metrics);
    } else if ("Result".equals(nodeType)) { // TODO: is this a result set
                                            // "container"?
      return new ResultOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources, metrics);
    } else if ("Limit".equals(nodeType)) {
      return new LimitOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources, metrics);
    } else if ("Index Only Scan".equals(nodeType)) {
      return new IndexOnlyScanOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources,
          metrics);
    } else if ("Index Scan".equals(nodeType)) {
      return new IndexRangeScanOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources,
          metrics);
    } else if ("Bitmap Heap Scan".equals(nodeType)) {
      return new IndexRangeScanOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources,
          metrics);
    } else if ("Bitmap Index Scan".equals(nodeType)) {
      return new IndexRangeScanOperator<Long>(id, nodeType, sourceSet, accessPredicates, filterPredicates, sources,
          metrics);
    } else {
      throw new InvalidPlanException("Unrecognized operator '" + nodeType + "'");
    }

  }

  // select
  // t.relname as table, a.attnum as ordinal,
  // a.attname as column, (i.indoption[a.attnum - 1] & 1 = 0) as ascending
  // from pg_class c
  // join pg_attribute a on a.attrelid = c.oid
  // join pg_index i on i.indexrelid = a.attrelid
  // join pg_class t on t.oid = i.indrelid
  // where c.relname = 'ix18' -- index name
  // order by a.attnum;

  private static SourceSet getIndexSource(final Connection conn, final String indexName, final String alias,
      final boolean includesHeapFetch) throws SQLException {

    String sourceTable = null;
    List<IndexColumn> sourceIndexColumns = new ArrayList<IndexColumn>();

    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = conn.prepareStatement( //
          "select \n" + //
              "  t.relname as table, \n" + //
              "  a.attnum as ordinal, \n" + //
              "  a.attname as column, \n" + //
              "  (i.indoption[a.attnum - 1] & 1 = 0) as ascending \n" + //
              "from pg_class c \n" + //
              "join pg_attribute a on a.attrelid = c.oid \n" + //
              "join pg_index i on i.indexrelid = a.attrelid \n" + //
              "join pg_class t on t.oid = i.indrelid \n" + //
              "where c.relname = ? \n" + //
              "order by a.attnum");
      st.setString(1, indexName);
      rs = st.executeQuery();

      while (rs.next()) {
        sourceTable = rs.getString(1);
        String columnName = rs.getString(3);
        String expression = null;
        boolean ascending = rs.getBoolean(4);
        IndexColumn indexColumn = new IndexColumn(columnName, expression, ascending);
        sourceIndexColumns.add(indexColumn);
      }

      return new SourceSet(sourceTable, alias, indexName, sourceIndexColumns, includesHeapFetch);

    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
      } finally {
        if (st != null) {
          st.close();
        }
      }
    }
  }

  // Helpers

  private static String getString(final Node node) {
    return node.getTextContent();
  }

  private static Double getDouble(final Node node) throws InvalidPlanException {
    String s = getString(node);
    if (s == null) {
      return null;
    } else {
      try {
        return Double.parseDouble(s);
      } catch (ClassCastException e) {
        throw new InvalidPlanException("Invalid floating point value '" + s + "'.");
      }
    }
  }

  private static Long getLong(final Node node) throws InvalidPlanException {
    String s = getString(node);
    if (s == null) {
      return null;
    } else {
      try {
        return Long.parseLong(s);
      } catch (ClassCastException e) {
        throw new InvalidPlanException("Invalid long value '" + s + "'.");
      }
    }
  }

  private static class SequenceGenerator {

    private long nextId = 0;

    public long next() {
      synchronized (this) {
        long n = nextId;
        nextId++;
        return n;
      }
    }

  }

}
