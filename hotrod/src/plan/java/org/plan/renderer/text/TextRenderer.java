package org.plan.renderer.text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.plan.ExecutionPlan;
import org.plan.operator.Operator;
import org.plan.renderer.text.cost.CostRenderer;

public class TextRenderer {

  public static String render(final ExecutionPlan plan, final boolean showPercentageCost) {

    StringBuilder sb = new StringBuilder();

    sb.append("Execution Plan for query '" + plan.getQueryTag() + "'\n");

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    sb.append("Produced at: " + df.format(plan.getProducedAt()) + "\n");

    sb.append("Query:\n");
    sb.append("\n");
    sb.append(plan.getQuery() + "\n");
    sb.append("\n");

    sb.append("Parameter Values:\n");
    for (String name : plan.getParameterValues().keySet()) {
      Object value = plan.getParameterValues().get(name);
      sb.append(" - " + name + ": " + value + "\n");
    }
    sb.append("\n");

    // Tree

    Operator op = plan.getRootOperator();

    Double fullCost = op.getMetrics().getCost();

    CostRenderer costRenderer = CostRenderer.instantiate(plan.getMetricsFactory(), fullCost, showPercentageCost);

    renderOperator(op, 0, costRenderer, sb);

    sb.append("\n");
    sb.append("\n");
    sb.append("\n");

    return sb.toString();
  }

  private static void renderOperator(final Operator op, final int level, final CostRenderer costRenderer,
      final StringBuilder sb) {

    // indent

    String indent = repeat(".  ", level);
    sb.append(indent);

    // cost

    String renderedCost = costRenderer.renderCost(op.getMetrics());
    if (renderedCost != null) {
      sb.append(" " + renderedCost);
    }

    // tags (not yet)

    // Operator

    sb.append(" " + op.getOperatorName());

    // Foot note

    if (!op.getAccessPredicates().isEmpty() || !op.getFilterPredicates().isEmpty()) {
      sb.append(" *" + op.getId());
    }

    // rows

    String renderedRows = costRenderer.renderRows(op.getMetrics());
    if (renderedRows != null) {
      sb.append(" (" + renderedRows + " rows)");
    }

    // source result set

  }

  private static String repeat(final String x, final int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(x);
    }
    return sb.toString();
  }

}
