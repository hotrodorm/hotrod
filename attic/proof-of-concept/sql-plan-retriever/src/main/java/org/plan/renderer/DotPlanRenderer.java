package org.plan.renderer;

import java.awt.Color;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.plan.ExecutionPlan;
import org.plan.operator.Operator;
import org.plan.renderer.CostRenderer.Scalar;
import org.plan.renderer.PlanStats.ColorFunction;
import org.plan.renderer.PlanStats.ScalarFunction;

public class DotPlanRenderer {

  private static final DecimalFormat WIDTH_FORMATTER = new DecimalFormat("0.0");

  public static <T extends Comparable<T>> String render(final ExecutionPlan<T> plan, final boolean showPercentageCost)
      throws SQLException {

    StringBuilder sb = new StringBuilder();

    Operator<?> op = plan.getRootOperator();

    Double fullCost = op.getMetrics().getCost();
    CostRenderer costRenderer = CostRenderer.instantiate(plan.getMetricsFactory(), fullCost, showPercentageCost);

    render(op, costRenderer, sb);

    return sb.toString();
  }

  public static <T extends Comparable<T>> void render(final Operator<T> op, final CostRenderer costRenderer,
      final StringBuilder sb) throws SQLException {

    // Gather stats

    PlanStats stats = new PlanStats();
    gatherStats(op, stats);

    // Header

    renderHeader(sb);

    // Nodes

    ScalarFunction nodeWidthFunction = stats.getCost().getLogScalarFunction(1, 6, 10.0);
    Color BORDER_MIN_COLOR = new Color(160, 160, 160);
    Color BORDER_MAX_COLOR = new Color(255, 0, 0);
    ColorFunction nodeColorFunction = stats.getCost().getLinearColorFunction(BORDER_MIN_COLOR, BORDER_MAX_COLOR);

    renderNodes(op, stats, costRenderer, nodeColorFunction, nodeWidthFunction, sb);

    // Arcs

    Color ARC_MIN_COLOR = new Color(255, 199, 0);
    Color ARC_MAX_COLOR = new Color(255, 128, 0);

    ScalarFunction arcWidthFunction = stats.getRows().getLogScalarFunction(1, 8, 2.0);
    ColorFunction arcColorFunction = stats.getRows().getLogColorFunction(ARC_MIN_COLOR, ARC_MAX_COLOR, 2.0);
    renderArcs(op, stats, arcWidthFunction, arcColorFunction, sb);

    // Footer

    renderFooter(op, sb);

  }

  private static void renderHeader(final StringBuilder sb) {
    sb.append("digraph p1 {\n");
    sb.append("  rankdir=BT; ranksep=0.3;\n");
    sb.append("  bgcolor=\"#f4f4f4\";\n");

    sb.append(
        "  graph [fontname = \"helvetica\", fontsize = 9]; node [fontname = \"helvetica\", fontsize = 10]; edge [fontname = \"helvetica\", fontsize = 9];\n");
    // sb.append(" labelloc=\"t\"; label=\"SQL Execution Plan - 2018-09-03
    // 10:11\";\n");
  }

  // TODO: nothing todo, just a marker

  private static <T extends Comparable<T>> void renderNodes(final Operator<T> op, final PlanStats stats,
      final CostRenderer costRenderer, final ColorFunction nodeColorFunction, final ScalarFunction nodeWidthFunction,
      final StringBuilder sb) {

    Double cost = op.getMetrics().getCost();
    Double rows = op.getMetrics().getRows();

    String cc = renderColor(nodeColorFunction.apply(cost == null ? 1.0 : cost));
    long border = Math.round(nodeWidthFunction.apply(cost == null ? 1.0 : cost) - 0.5);

    // Opening node

    sb.append("  " + op.getId() + " [shape=none margin=0 label=<\n"
        + "    <table cellspacing=\"0\" cellpadding=\"1\" border=\"" + border + "\" color=\"" + cc
        + "\" bgcolor=\"#ffffff\" style=\"radial\" cellborder=\"0\">\n");

    // First row -- cost

    if (cost != null || rows != null) {
      Scalar fc = costRenderer.renderCost(op.getMetrics());
      String fr = costRenderer.renderRows(op.getMetrics());

      sb.append("      <tr>\n");
      sb.append(
          "        <td align=\"left\" cellpadding=\"0\" width=\"50%\" color=\"#c0c0c0\" border=\"1\" sides=\"b\"><font point-size=\"18\">"
              + fc.getFormatterNumber() + "<font color=\"#a0a0a0\">&nbsp;" + fc.getUnit() + "</font></font></td>\n");

      sb.append("        <td align=\"right\" color=\"#c0c0c0\" border=\"1\" sides=\"b\"><font point-size=\"10\">&nbsp;"
          + (fr != null ? fr : "--") + "<font color=\"#a0a0a0\"> rows</font></font></td>\n");

      sb.append("      </tr>\n");
    }

    // Separator

    sb.append("      <tr>\n");
    sb.append("        <td align=\"left\" cellpadding=\"0\"><font point-size=\"3\" color=\"#ffffff\">.</font></td>\n");
    sb.append("      </tr>\n");

    // Second row -- operation

    boolean hasPredicates = !op.getAccessPredicates().isEmpty() || !op.getFilterPredicates().isEmpty();

    sb.append("      <tr>\n");
    sb.append("        <td align=\"left\" colspan=\"2\" ><b>" + op.getSpecificName() + "</b>"
        + (hasPredicates ? "&nbsp;<font color=\"#a0a0a0\">*" + op.getId() + "</font>" : "")
        + (op.getSourceSet() != null && op.getSourceSet().includesHeapFetch()
            ? " <font color=\"#0000ff\"><b>&#x24bb;</b></font>"
            : "")
        + "</td>\n");
    sb.append("      </tr>\n");

    // Third row -- tags & extra info

    sb.append("      <tr>\n");
    sb.append(
        "        <td align=\"left\"><font point-size=\"10\" color=\"#ff0000\">&#xff04; &#x231b; &#x26f0; </font></td>\n");
    sb.append("        <td align=\"right\">"
        + (op.getJoinType() != null ? op.getJoinType() + " <font color=\"#808080\">join</font>" : "") + "</td>\n");
    sb.append("      </tr>\n");

    // Fourth row - estimates

    sb.append("      <tr>\n");
    sb.append(
        "        <td align=\"left\"><font point-size=\"12\" color=\"#e08000\">&#x2260;</font><font color=\"#a0a0a0\"><i>est. 51 µs</i></font></td>\n");
    sb.append(
        "        <td align=\"right\"><font point-size=\"12\" color=\"#e08000\"> &#x2260;</font><font color=\"#a0a0a0\"><i>est. 78 rows</i></font></td>\n");
    sb.append("      </tr>\n");

    // Closing node

    sb.append("      </table>\n");
    sb.append("    >];\n");

    // label (hover?)
    // TODO: what it this for?

    // if (op.getRowsSource() != null || op.getRowsSourceAlias() != null) {
    // String label;
    // if (op.getRowsSource() != null) {
    // label = op.getRowsSource() + (op.getRowsSourceAlias() != null ? " " +
    // op.getRowsSourceAlias() : "");
    // } else {
    // label = op.getRowsSourceAlias() != null ? op.getRowsSourceAlias() : "";
    // }
    // if (op.getIndexName() != null) {
    // label = label + "<br/>{" + op.getIndexName() + "}";
    // }
    // // celeste: d3e4ff
    // sb.append(
    // " d" + op.getId() + " [shape=\"box\" style=\"filled\"
    // fillcolor=\"#ceefb3\" label=<" + label + ">];\n");
    // } else if (op.getIndexName() != null) {
    // // light green: a3f75d
    // // light yellow: fffdd3
    // sb.append(" i" + op.getId() + " [shape=\"box\" style=\"filled\"
    // fillcolor=\"#a3f75d\" label=<"
    // + op.getIndexName() + ">];\n");
    // }

    for (Operator<T> c : op.getChildren()) {
      renderNodes(c, stats, costRenderer, nodeColorFunction, nodeWidthFunction, sb);
    }

  }

  private static <T extends Comparable<T>> void gatherStats(final Operator<T> op, final PlanStats stats) {
    stats.getRows().addSample(op.getMetrics().getRows());
    stats.getCost().addSample(op.getMetrics().getCost());
    for (Operator<T> c : op.getChildren()) {
      gatherStats(c, stats);
    }
  }

  // TODO: nothing todo, just a marker

  private static <T extends Comparable<T>> void renderArcs(final Operator<T> op, final PlanStats stats,
      final ScalarFunction arcWidthFunction, final ColorFunction arcColorFunction, final StringBuilder sb) {
    for (Operator<T> c : op.getChildren()) {
      Double rows = c.getMetrics().getRows();
      double arcWidth = rows == null ? 1.0 : arcWidthFunction.apply(rows);
      String rc = renderColor(arcColorFunction.apply(rows == null ? 1.0 : rows));
      sb.append("  " + c.getId() + " -> " + op.getId() + " [color=\"" + rc + "\" penwidth="
          + WIDTH_FORMATTER.format(arcWidth) + "];\n");
      renderArcs(c, stats, arcWidthFunction, arcColorFunction, sb);
    }
  }

  private static <T extends Comparable<T>> void renderFooter(final Operator<T> op, final StringBuilder sb) {
    sb.append("}\n");

    sb.append("}\n");
  }

  private static String renderColor(final Color c) {
    return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
  }

}
