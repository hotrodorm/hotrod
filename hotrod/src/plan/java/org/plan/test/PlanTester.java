package org.plan.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.plan.ExecutionPlan;
import org.plan.metrics.Metrics;
import org.plan.metrics.MetricsFactory;
import org.plan.operator.IndexRangeScanOperator;
import org.plan.operator.Operator;
import org.plan.operator.Operator.Ordinal;
import org.plan.operator.Operator.SourceSet;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;
import org.plan.renderer.text.TextRenderer;

public class PlanTester {

  public static void main(final String[] args) {

    ExecutionPlan<?> plan = retrievePlan();

    String report = TextRenderer.render(plan, false);

    System.out.println("Execution Plan Report");
    System.out.println();
    System.out.println(report);

  }

  private static ExecutionPlan<Integer> retrievePlan() {

    boolean includesEstimatedMetrics = true;
    boolean includesActualMetrics = false;
    MetricsFactory factory = MetricsFactory.instantiate(includesEstimatedMetrics, includesActualMetrics);

    LinkedHashMap<String, Object> parameterValues = new LinkedHashMap<String, Object>();
    parameterValues.put("id", new Integer(10));
    parameterValues.put("minDate", new Date());

    // Index Scan

    Operator<Integer> is;
    {
      List<Ordinal> sourceIndexColumns = new ArrayList<Ordinal>();
      sourceIndexColumns.add(new Ordinal("ID", null, true));
      sourceIndexColumns.add(new Ordinal("VERSION", null, false));
      SourceSet sourceSet = new SourceSet("ACCOUNT", "IX1_ACCOUNT_ID", sourceIndexColumns, true);

      List<AccessPredicate> accessPredicates = new ArrayList<AccessPredicate>();
      accessPredicates.add(new AccessPredicate("START: ID = $p001"));
      accessPredicates.add(new AccessPredicate("STOP: ID <> $p001"));

      List<FilterPredicate> filterPredicates = new ArrayList<FilterPredicate>();
      filterPredicates.add(new FilterPredicate("SARG: STARTED_AT > $p002"));

      List<Operator<Integer>> children = new ArrayList<Operator<Integer>>();

      Metrics metrics = factory.getMetrics(1234.5678, 87.456, null, null, null);

      is = new IndexRangeScanOperator<Integer>(new Integer(101), "Index Scan Ranged", sourceSet, accessPredicates,
          filterPredicates, children, metrics);
    }

    // Sort

    Operator<Integer> rootOperator;
    {
      SourceSet sourceSet = null;

      List<AccessPredicate> accessPredicates = new ArrayList<AccessPredicate>();

      List<FilterPredicate> filterPredicates = new ArrayList<FilterPredicate>();
      filterPredicates.add(new FilterPredicate("ORDER BY: NAME"));

      List<Operator<Integer>> children = new ArrayList<Operator<Integer>>();
      children.add(is);

      Metrics metrics = factory.getMetrics(2345.6789, 187.456, null, null, null);

      rootOperator = new IndexRangeScanOperator<Integer>(new Integer(104), "Sort", sourceSet, accessPredicates,
          filterPredicates, children, metrics);
    }

    ExecutionPlan<Integer> plan = ExecutionPlan.instantiate("query-001", new Date(),
        "select *\nfrom account\nwhere id = #{id} and started_at > #{minDate}\norder by name", parameterValues,
        rootOperator, true, false);
    return plan;
  }

}
