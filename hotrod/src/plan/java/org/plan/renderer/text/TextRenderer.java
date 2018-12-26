package org.plan.renderer.text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.plan.ExecutionPlan;
import org.plan.operator.Operator;

public class TextRenderer {

  private static final DecimalFormat PF = new DecimalFormat("#0%");

  public static String render(final ExecutionPlan p) {

    StringBuilder sb = new StringBuilder();

    sb.append("Execution Plan for query '" + p.getQueryTag() + "'\n");

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    sb.append("Produced at: " + df.format(p.getProducedAt()) + "\n");

    sb.append("Query:\n");
    sb.append("\n");
    sb.append(p.getQuery() + "\n");
    sb.append("\n");

    sb.append("Parameter Values:\n");
    for (String name : p.getParameterValues().keySet()) {
      Object value = p.getParameterValues().get(name);
      sb.append(" - " + name + ": " + value + "\n");
    }
    sb.append("\n");

    // Tree

    Operator op = p.getRootOperator();

    Double actualFullCost = op.getActualMetrics().getActualTimeMs();
    Double estimatedFullCost = op.getEstimatedMetrics().getEstimatedCost();

    renderOperator(op, 0, actualFullCost, estimatedFullCost, sb);

    sb.append("\n");
    sb.append("\n");
    sb.append("\n");

    return sb.toString();
  }

  private static void renderOperator(final Operator op, final int level, final Double actualFullCost,
      final Double estimatedFullCost, final StringBuilder sb) {
    
    // indent
    
    String indent = repeat(".  ", level);
    
    // cost
    
    if (actualFullCost != null) {
      
    }
    
    
  }

  private static String repeat(final String x, final int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(x);
    }
    return sb.toString();
  }

}
