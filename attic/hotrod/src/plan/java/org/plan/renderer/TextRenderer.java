package org.plan.renderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.hotrod.runtime.util.ListWriter;
import org.nocrala.tools.texttreeformatter.TextTreeFormatter;
import org.nocrala.tools.texttreeformatter.TextTreeStyle;
import org.plan.ExecutionPlan;
import org.plan.operator.Operator;
import org.plan.operator.Operator.IndexColumn;
import org.plan.predicate.AccessPredicate;
import org.plan.predicate.FilterPredicate;
import org.plan.renderer.CostRenderer.Scalar;

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

    OperatorNode root = renderOperator(op, 0, costRenderer);
    TextTreeFormatter formatter = new TextTreeFormatter(TextTreeStyle.CLASSIC_FULL2);
//    String tree = formatter.render(root);
//    sb.append(tree);

    sb.append("\n");

    renderPredicates(op, sb);

    sb.append("\n");

    return sb.toString();
  }

  private static OperatorNode renderOperator(final Operator<?> op, final int level, final CostRenderer costRenderer) {

    StringBuilder label = new StringBuilder();

    // cost

    Scalar renderedCost = costRenderer.renderCost(op.getMetrics());
    if (renderedCost != null) {
      label.append(" " + renderedCost.getFormatterNumber()
          + (renderedCost.getUnit() != null ? " " + renderedCost.getUnit() : ""));
    }

    // tags (not yet)

    // Operator

    label.append(" " + op.getGenericName() + " (" + op.getSpecificName() + ")");

    // Foot note

    if (!op.getAccessPredicates().isEmpty() || !op.getFilterPredicates().isEmpty()) {
      label.append(" *" + op.getId());
    }

    // rows

    String renderedRows = costRenderer.renderRows(op.getMetrics());
    if (renderedRows != null) {
      label.append(" (" + renderedRows + " rows)");
    }

    // source set

    if (op.getSourceSet() != null) {
      if (op.getSourceSet().getSourceIndex() != null) { // index access
        label.append(" " + op.getSourceSet().getSourceIndex());
        if (op.getSourceSet().getSourceTable() != null) {
          label.append(" on " + op.getSourceSet().getSourceTable());
          if (op.getSourceSet().getTableAlias() != null) {
            label.append(" \"" + op.getSourceSet().getTableAlias() + "\"");
          }
        }
        List<IndexColumn> sic = op.getSourceSet().getSourceIndexColumns();
        if (sic != null && !sic.isEmpty()) {
          ListWriter lw = new ListWriter(", ");
          for (IndexColumn o : sic) {
            lw.add( //
                (o.getColumnName() != null ? o.getColumnName() : o.getExpression()) //
                    + (o.isAscending() ? "" : " desc"));
          }
          label.append(" (" + lw.toString() + ")");
        }
      } else { // table access
        label.append(" " + op.getSourceSet().getSourceTable());
        if (op.getSourceSet().getTableAlias() != null) {
          label.append(" \"" + op.getSourceSet().getTableAlias() + "\"");
        }
      }

    }

    // Render inner operators

    List<OperatorNode> children = new ArrayList<OperatorNode>();
    for (Operator<?> co : op.getChildren()) {
      children.add(renderOperator(co, level + 1, costRenderer));
    }

    // Return

    return new OperatorNode(label.toString().trim(), children);

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

  private static class OperatorNode {

    private String label;
    private List<OperatorNode> children;

    public OperatorNode(final String label, final List<OperatorNode> children) {
      this.label = label;
      this.children = children;
    }

//    @Override
//    public String getLabel() {
//      return this.label;
//    }
//
//    @Override
//    public List<TreeNode> getChildren() {
//      return new ArrayList<TreeNode>(this.children);
//    }

  }

}
