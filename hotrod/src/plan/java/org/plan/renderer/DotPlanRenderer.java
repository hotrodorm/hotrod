package org.plan.renderer;

import java.awt.Color;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.plan.ExecutionPlan;
import org.plan.operator.Operator;
import org.plan.renderer.CostRenderer.Scalar;

public class DotPlanRenderer {

  private static final DecimalFormat WIDTH_FORMATTER = new DecimalFormat("0.0");

  private static final String GREY = "808080";
  private static final String BLUE = "0000ff";
  private static final String RED = "ff0000";
  private static final String WHITE = "ffffff";

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

    Measurement measurement = new Measurement(1.0, 2.0);
    gatherStats(op, measurement);

    renderHeader(sb);
    renderNodes(op, measurement, costRenderer, sb);
    renderArcs(op, measurement, sb);
    renderFooter(op, sb);

  }

  private static void renderHeader(final StringBuilder sb) {
    sb.append("digraph p1 {\n");
    sb.append("  rankdir=BT; ranksep=0.3;\n");
    sb.append("  bgcolor=\"#f4f4f4\";\n");

    sb.append(
        "  graph [fontname = \"helvetica\", fontsize = 9]; node [fontname = \"helvetica\", fontsize = 9]; edge [fontname = \"helvetica\", fontsize = 9];\n");
    sb.append("  labelloc=\"t\"; label=\"SQL Execution Plan - 2018-09-03 10:11\";\n");
    sb.append("subgraph tree {\n");
  }

  // TODO: nothing todo, just a marker

  private static <T extends Comparable<T>> void renderNodes(final Operator<T> op, final Measurement measurement,
      final CostRenderer costRenderer, final StringBuilder sb) {

    Double cost = op.getMetrics().getCost();
    Double rows = op.getMetrics().getRows();
    String cc = renderColor(measurement.getTime().computeLinearColor(cost == null ? 1.0 : cost));
    double ratio = measurement.getTime().computeLinearRatio(cost == null ? 1.0 : cost);

    // Opening node

    sb.append("  " + op.getId() + " [shape=none width=0 height=0 margin=0 style=\"rounded\" color=\"#c0c0c0\" "
        + "label=<<table cellspacing=\"0\" border=\"1\" bgcolor=\"#ffffff\" cellborder=\"0\">");

    // First row

    if (cost != null || rows != null) {

      Scalar fc = costRenderer.renderCost(op.getMetrics());
      String fr = costRenderer.renderRows(op.getMetrics());

      sb.append("<tr>");

      sb.append("<td width=\"50%\" color=\"#c0c0c0\" border=\"1\" bgcolor=\"" + cc + "\">" //
          + fc.getFormatterNumber() //
          + "<font color=\"#" + (ratio < 0.7 ? GREY : WHITE) + "\">" //
          + "&nbsp;" + fc.getUnit() //
          + "</font>" //
          + "</td>" //
      );

      sb.append("<td align=\"right\">&nbsp;" //
          + (fr != null ? (fr //
              + "<font color=\"#" + GREY + "\"> rows</font>" //
          ) : "") //
          + "</td>" //
      );

      sb.append("</tr>");

    }

    // Second row

    boolean hasPredicates = !op.getAccessPredicates().isEmpty() || !op.getFilterPredicates().isEmpty();
    sb.append("<tr><td colspan=\"2\" align=\"left\">" //
        + "<b>" + op.getSpecificName() + "</b>" //
        + (hasPredicates ? "<font color=\"#" + GREY + "\">&nbsp;&nbsp;*" + op.getId() + "</font>" : "&nbsp;") //
        + (op.getSourceSet() != null && op.getSourceSet().includesHeapFetch()
            ? "<font color=\"#" + BLUE + "\">&#x25e9;</font> "
            : "") //
        + "</td></tr>");

    // Third row

    sb.append("<tr>");

    sb.append("<td align=\"left\">" //
        + (op.getJoinType() != null ? op.getJoinType() + "<font color=\"#" + GREY + "\">" + "join" + "</font>" : "") //
        + "</td>" //
    );

    sb.append("<td align=\"right\">" //
        // + "<IMG SRC=\"costly.png\">"
        + "<font color=\"#" + RED + "\">" + "<b>($) (T) (=) (x)</b></font>" //
        + "</td>" //
    );

    sb.append("</tr>");

    // Closing node

    sb.append("</table>>];\n");

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
      renderNodes(c, measurement, costRenderer, sb);
    }

  }

  private static <T extends Comparable<T>> void gatherStats(final Operator<T> op, final Measurement measurement) {
    measurement.getRows().register(op.getMetrics().getRows());
    measurement.getCost().register(op.getMetrics().getCost());
    for (Operator<T> c : op.getChildren()) {
      gatherStats(c, measurement);
    }
  }

  // TODO: nothing todo, just a marker

  private static <T extends Comparable<T>> void renderArcs(final Operator<T> op, final Measurement measurement,
      final StringBuilder sb) {
    for (Operator<T> c : op.getChildren()) {
      Double rows = c.getMetrics().getRows();
      double width = rows == null ? 1.0 : measurement.getRows().computeScalar(rows);
      String rc = renderColor(measurement.getRows().computeLogColor(rows == null ? 1.0 : rows));
      sb.append("  " + c.getId() + " -> " + op.getId() + " [color=\"" + rc + "\" penwidth="
          + WIDTH_FORMATTER.format(width) + "];\n");
      renderArcs(c, measurement, sb);
    }
  }

  private static <T extends Comparable<T>> void renderFooter(final Operator<T> op, final StringBuilder sb) {
    sb.append("}\n");

    sb.append("}\n");
  }

  private static String renderColor(final Color c) {
    return String.format("#%2x%2x%2x", c.getRed(), c.getGreen(), c.getBlue());
  }

}
