package org.plan.renderer.text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.hotrod.runtime.util.ListWriter;
import org.plan.ExecutionPlan;
import org.plan.operator.Operator;
import org.plan.operator.Operator.Ordinal;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;
import org.plan.renderer.text.cost.CostRenderer;
import org.plan.renderer.text.cost.CostRenderer.Scalar;

public class TextRenderer {

  public static <T extends Comparable<T>> String render(final ExecutionPlan<T> plan, final boolean showPercentageCost) {

    StringBuilder sb = new StringBuilder();

    sb.append("Execution Plan Report\n");
    sb.append("---------------------\n");

    sb.append("Query Tag: " + plan.getQueryTag() + "\n");

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    sb.append("Produced at: " + df.format(plan.getProducedAt()) + "\n");

    sb.append("\n");
    sb.append("Query:\n");
    sb.append("------\n");
    sb.append(plan.getQuery() + "\n");
    sb.append("\n");

    sb.append("Parameter Values:\n");
    sb.append("-----------------\n");
    for (String name : plan.getParameterValues().keySet()) {
      Object value = plan.getParameterValues().get(name);
      sb.append(" - " + name + ": " + value + "\n");
    }
    sb.append("\n");

    // Tree

    sb.append("Plan Tree:\n");
    sb.append("----------\n");

    Operator<?> op = plan.getRootOperator();

    Double fullCost = op.getMetrics().getCost();

    CostRenderer costRenderer = CostRenderer.instantiate(plan.getMetricsFactory(), fullCost, showPercentageCost);

    renderOperator(op, 0, costRenderer, sb);

    sb.append("\n");

    renderPredicates(op, sb);

    sb.append("\n");

    return sb.toString();
  }

  private static void renderOperator(final Operator<?> op, final int level, final CostRenderer costRenderer,
      final StringBuilder out) {

    // indent

    String indent = repeat("+- ", level);
    out.append(indent);

    StringBuilder line = new StringBuilder();

    // cost

    Scalar renderedCost = costRenderer.renderCost(op.getMetrics());
    if (renderedCost != null) {
      line.append(" " + renderedCost.getFormatterNumber()
          + (renderedCost.getUnit() != null ? " " + renderedCost.getUnit() : ""));
    }

    // tags (not yet)

    // Operator

    line.append(" " + op.getGenericName() + " (" + op.getSpecificName() + ")");

    // Foot note

    if (!op.getAccessPredicates().isEmpty() || !op.getFilterPredicates().isEmpty()) {
      line.append(" *" + op.getId());
    }

    // rows

    String renderedRows = costRenderer.renderRows(op.getMetrics());
    if (renderedRows != null) {
      line.append(" (" + renderedRows + " rows)");
    }

    // source set

    if (op.getSourceSet() != null) {
      if (op.getSourceSet().getSourceIndex() != null) { // index access
        line.append(" " + op.getSourceSet().getSourceIndex());
        if (op.getSourceSet().getSourceTable() != null) {
          line.append(" on " + op.getSourceSet().getSourceTable());
        }
        List<Ordinal> sic = op.getSourceSet().getSourceIndexColumns();
        if (sic != null && !sic.isEmpty()) {
          ListWriter lw = new ListWriter(", ");
          for (Ordinal o : sic) {
            lw.add( //
                (o.getColumnName() != null ? o.getColumnName() : o.getExpression()) //
                    + (o.isAscending() ? "" : " desc"));
          }
          line.append(" (" + lw.toString() + ")");
        }
      } else { // table access
        line.append(" " + op.getSourceSet().getSourceTable());
      }

    }

    // End of line

    out.append(line.toString().trim());
    out.append("\n");

    // Render inner operators

    for (Operator<?> co : op.getChildren()) {
      renderOperator(co, level + 1, costRenderer, out);
    }

  }

  private static <T extends Comparable<T>> void renderPredicates(final Operator<T> op, final StringBuilder out) {
    TreeMap<T, List<String>> predicates = new TreeMap<T, List<String>>();
    compilePredicates(op, predicates);

    out.append("Predicates:\n");
    out.append("-----------\n");

    for (T key : predicates.keySet()) {
      String indent = null;
      for (String p : predicates.get(key)) {

        // Foot Note / Indent

        if (indent == null) {
          String footNote = "*" + key + " ";
          indent = repeat(" ", footNote.length());
          out.append(footNote);
        } else {
          out.append(indent);
        }

        // Predicate

        out.append(p);
        out.append("\n");

      }
    }

  }

  private static <T extends Comparable<T>> void compilePredicates(final Operator<T> op,
      final TreeMap<T, List<String>> predicates) {

    T id = op.getId();
    for (AccessPredicate ap : op.getAccessPredicates()) {
      List<String> ps = predicates.get(id);
      if (ps == null) {
        ps = new ArrayList<String>();
        predicates.put(id, ps);
      }
      ps.add("access: " + ap.getPredicate());
    }

    for (FilterPredicate fp : op.getFilterPredicates()) {
      List<String> ps = predicates.get(id);
      if (ps == null) {
        ps = new ArrayList<String>();
        predicates.put(id, ps);
      }
      ps.add("filter: " + fp.getPredicate());
    }

    for (Operator<T> c : op.getChildren()) {
      compilePredicates(c, predicates);
    }

  }

  // Utilities

  private static String repeat(final String x, final int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(x);
    }
    return sb.toString();
  }

}
