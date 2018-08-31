package explain.mysql8.parser;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.hotrod.runtime.util.ListWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import explain.Operator;

/**
 * <pre>
 * MySQL8Plan
 * -> === QueryBlock ===
 *   -> Table?
 *     -> AttachedSubquery*
 *       -> [QueryBlock]
 *   -> NestedLoopElement?
 *     -> [Table] x2
 *
 *A QueryBlock is the OPERATOR!
 *
 *A QueryBlock has either:
 * * a Table, or
 * * a list of NestedLoopElements.
 * * May have a list of OptimizedAwaySubqueries.
 * 
 * </pre>
 */

public class MySQLJSONPlanParser {

  public static MySQLOperator parse(final Connection conn, final String jsonPlan) {
    Gson gson = new GsonBuilder().create();
    MySQL8Plan plan = gson.fromJson(jsonPlan, MySQL8Plan.class);

    QueryBlock b = plan.getQueryBlock();
    MySQLOperator op = getOperator(null, b);

    addMissingIds(op);

    return op;
  }

  private static MySQLOperator getOperator(final String prefix, final QueryBlock b) {
    Integer id = b.getSelectId();

    if (b.getTable() != null) {

      Table t = b.getTable();

      // It's a table

      String type = (prefix != null ? prefix : "") + t.getAccessType();
      boolean includesHeapFetch = false;
      Double cost = b.getCostInfo() != null ? b.getCostInfo().getQueryCost() : null;
      Double examinedRows = t.getRowsExaminedPerScan();
      Long producedBytes = null;
      Double producedRows = t.getRowsProducedPerJoin();
      String rowsSource = null;
      String rowsSourceAlias = t.getTableName();

      // TODO: fill when using index.
      String indexName = t.getKey();
      List<String> accessPredicates = new ArrayList<String>();
      if (t.getKey() != null) {
        accessPredicates.add("index " + t.getKey() + " (" + ListWriter.render(t.getUsedKeyParts(), ", ") + ")");
      }

      List<String> filterPredicates = new ArrayList<String>();
      filterPredicates.add(t.getAttachedCondition());
      List<MySQLOperator> innerOperators = new ArrayList<MySQLOperator>();
      for (AttachedSubquery s : t.getAttachedSubqueries()) {
        QueryBlock sb = s.getQueryBlock();
        if (sb != null) {
          boolean correlated = s.getDependent() != null && s.getDependent();
          MySQLOperator so = getOperator("(" + (correlated ? "correlated " : "") + "subquery) ", sb);
          innerOperators.add(so);
        }
      }
      // System.out.println("t.getMaterializedFromSubquery()=" +
      // t.getMaterializedFromSubquery());
      if (t.getMaterializedFromSubquery() != null) {
        QueryBlock mb = t.getMaterializedFromSubquery().getQueryBlock();
        MySQLOperator mo = getOperator("(materialized subquery) ", mb);
        innerOperators.add(mo);
      }

      for (OptimizedAwaySubquery oa : b.getOptimizedAwaySubqueries()) {
        QueryBlock oab = oa.getQueryBlock();
        if (oab != null) {
          MySQLOperator so = getOperator("(optimized_away_subquery) ", oab);
          innerOperators.add(so);
        }
      }

      // Assemble operator

      LinkedHashMap<String, String> extraProperties = new LinkedHashMap<String, String>();

      MySQLOperator op = new MySQLOperator(id, type, includesHeapFetch, cost, examinedRows, producedBytes, producedRows,
          rowsSource, rowsSourceAlias, indexName, accessPredicates, filterPredicates, innerOperators, extraProperties);

      return op;

    } else if (!b.getNestedLoopElements().isEmpty()) {

      MySQLOperator op = getNestedLoopOperator(prefix, id, b.getCostInfo(), b.getNestedLoopElements());

      for (OptimizedAwaySubquery oa : b.getOptimizedAwaySubqueries()) {
        QueryBlock oab = oa.getQueryBlock();
        if (oab != null) {
          MySQLOperator so = getOperator("(optimized away subquery) ", oab);
          op.addInner(so);
        }
      }

      return op;

    } else if (b.getMessage() != null) {

      MySQLOperator op = getMessageOperator(b);
      return op;

    } else {

      throw new IllegalArgumentException("Invalid query_block");

    }

  }

  private static MySQLOperator getNestedLoopOperator(final String prefix, final Integer id, final CostInfo costInfo,
      final List<NestedLoopElement> nles) {

    // Current table

    NestedLoopElement nle = nles.get(0);
    Table t = nle.getTable();
    MySQLOperator op = getTableOperator(prefix, id, t);

    // Rest of the tables

    List<NestedLoopElement> restNles = nles.subList(1, nles.size());
    if (restNles.size() > 1) {
      MySQLOperator nop = getNestedLoopOperator(null, null, null, restNles);
      op.addInner(nop);
    } else {
      NestedLoopElement nle0 = restNles.get(0);
      Table t0 = nle0.getTable();
      // System.out.println("-- Last table: " + t0.getTableName());
      MySQLOperator xop0 = getTableOperator(null, null, t0);
      op.addInner(xop0);
    }

    return op;

  }

  private static MySQLOperator getTableOperator(final String prefix, final Integer id, final Table t) {
    String type = (prefix != null ? prefix : "") + "NL." + t.getAccessType();
    boolean includesHeapFetch = false; // TODO ?
    Double cost = t.getCostInfo() == null ? null : t.getCostInfo().computeCost();
    Double examinedRows = t.getRowsExaminedPerScan();
    Long producedBytes = null;
    Double producedRows = t.getRowsProducedPerJoin();
    String rowsSource = null;
    String rowsSourceAlias = t.getTableName();

    // TODO: fill when using index.
    String indexName = t.getKey();
    List<String> accessPredicates = new ArrayList<String>();
    // accessPredicates.add(t.getAccessType());
    if (t.getKey() != null) {
      accessPredicates.add("index " + t.getKey() + " (" + ListWriter.render(t.getUsedKeyParts(), ", ") + ")");
    }

    List<String> filterPredicates = new ArrayList<String>();
    if (t.getAttachedCondition() != null) {
      filterPredicates.add(t.getAttachedCondition());
    }
    List<MySQLOperator> innerOperators = new ArrayList<MySQLOperator>();

    // System.out.println("*** t.getAttachedSubqueries()=" +
    // t.getAttachedSubqueries() + " table=" + t.getTableName());
    for (AttachedSubquery s : t.getAttachedSubqueries()) {
      QueryBlock sb = s.getQueryBlock();
      boolean correlated = s.getDependent() != null && s.getDependent();
      String sPrefix = "(" + (correlated ? "correlated " : "") + "subquery) ";

      if (sb != null) {
        MySQLOperator so = getOperator(sPrefix, sb);
        innerOperators.add(so);
      } else if (s.getTable() != null) {
        Table st = s.getTable();
        MySQLOperator sop = getTableOperator(sPrefix, null, st);
        innerOperators.add(sop);
      }
    }
    // System.out
    // .println("t.getMaterializedFromSubquery()=" +
    // t.getMaterializedFromSubquery() + " table=" + t.getTableName());
    if (t.getMaterializedFromSubquery() != null) {
      QueryBlock mb = t.getMaterializedFromSubquery().getQueryBlock();
      // System.out.println("mb=" + mb);
      MySQLOperator mo = getOperator("(materialized subquery) ", mb);
      innerOperators.add(mo);
    }

    LinkedHashMap<String, String> extraProperties = new LinkedHashMap<String, String>();

    MySQLOperator xop = new MySQLOperator(id, type, includesHeapFetch, cost, examinedRows, producedBytes, producedRows,
        rowsSource, rowsSourceAlias, indexName, accessPredicates, filterPredicates, innerOperators, extraProperties);
    return xop;
  }

  private static MySQLOperator getMessageOperator(final QueryBlock b) {
    Integer id = b.getSelectId();
    // System.out.println("<materialized_from_subquery> id=" + id);
    String type = "<materialized_from_subquery>";
    boolean includesHeapFetch = false; // TODO ?
    Double cost = null;
    Double examinedRows = null;
    Long producedBytes = null;
    Double producedRows = null;
    String rowsSource = null;
    String rowsSourceAlias = null;

    // TODO: fill when using index.
    String indexName = null;
    List<String> accessPredicates = new ArrayList<String>();

    List<String> filterPredicates = new ArrayList<String>();
    List<MySQLOperator> innerOperators = new ArrayList<MySQLOperator>();
    LinkedHashMap<String, String> xExtraProperties = new LinkedHashMap<String, String>();

    MySQLOperator op = new MySQLOperator(id, type, includesHeapFetch, cost, examinedRows, producedBytes, producedRows,
        rowsSource, rowsSourceAlias, indexName, accessPredicates, filterPredicates, innerOperators, xExtraProperties);
    return op;
  }

  private static Integer nextId;

  private static void addMissingIds(final MySQLOperator op) {
    Set<Integer> existing = new HashSet<Integer>();
    compileExistingIds(op, existing);

    nextId = null;
    for (Integer ei : existing) {
      if (nextId == null || ei > nextId) {
        nextId = ei;
      }
    }
    nextId = nextId == null ? 1 : nextId + 1;
    nextId = Math.max(nextId, 100);

    addIds(op, existing);
  }

  private static void compileExistingIds(final MySQLOperator op, final Set<Integer> existing) {
    if (op.getId() != null) {
      existing.add(op.getId());
    }
    for (Operator inner : op.getInnerOperators()) {
      compileExistingIds((MySQLOperator) inner, existing);
    }
  }

  private static void addIds(final MySQLOperator op, final Set<Integer> existing) {
    if (op.getId() == null) {
      op.setId(nextId);
      nextId = nextId + 1;
    }
    for (Operator inner : op.getInnerOperators()) {
      addIds((MySQLOperator) inner, existing);
    }
  }

}
