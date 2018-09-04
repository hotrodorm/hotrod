package explain.renderers;

import java.awt.Color;
import java.sql.SQLException;
import java.text.DecimalFormat;

import explain.Operator;

public class DotPlanRenderer implements PlanRenderer {

  private static final double ROWS_EXP = 2.0;
  private static final double ROWS_MIN_WIDTH = 1.0;
  private static final double ROWS_MAX_WIDTH = 8.0;
  private static final Color ROWS_MIN_COLOR = new Color(150, 50, 50);
  private static final Color ROWS_MAX_COLOR = new Color(255, 0, 0);

  private static final double COST_EXP = 10.0;
  private static final Color COST_MIN_COLOR = new Color(255, 255, 255);
  private static final Color COST_MAX_COLOR = new Color(255, 140, 140);

  private static final DecimalFormat WIDTH_FORMATTER = new DecimalFormat("0.0");

  @Override
  public String render(final Operator op) throws SQLException {

    StringBuilder sb = new StringBuilder();

    Range rowsRange = new Range(ROWS_EXP, ROWS_MIN_WIDTH, ROWS_MAX_WIDTH, ROWS_MIN_COLOR, ROWS_MAX_COLOR);
    Range costRange = new Range(COST_EXP, 1, 100, COST_MIN_COLOR, COST_MAX_COLOR);
    gatherRanges(op, rowsRange, costRange);

    renderHeader(sb);
    renderNodes(op, rowsRange, costRange, sb);
    renderLinks(op, rowsRange, costRange, sb);
    renderFooter(op, sb);

    String plan = sb.toString();

    return plan;
  }

  private void renderHeader(final StringBuilder sb) {
    sb.append("digraph p1 {\n");
    sb.append("  rankdir=BT; ranksep=0.3;\n");
    sb.append(
        "  graph [fontname = \"helvetica\", fontsize = 9]; node [fontname = \"helvetica\", fontsize = 9]; edge [fontname = \"helvetica\", fontsize = 9];\n");
    sb.append("  labelloc=\"t\"; label=\"SQL Execution Plan - 2018-09-03 10:11\";\n");
    sb.append("subgraph tree {\n");
  }

  private void renderNodes(final Operator op, final Range rowsRange, final Range costRange, final StringBuilder sb) {

    String cc = renderColor(costRange.computeColorLinear(op.getCost()));

    sb.append("  " + op.getId() + " [shape=none width=0 height=0 margin=0 style=\"rounded\" color=\"gray40\" "
        + "label=<<table cellspacing=\"0\" border=\"0\" cellborder=\"1\"><tr><td width=\"60%\" bgcolor=\"" + cc + "\">"
        + "<font point-size=\"18\">" //
        + formatDouble(op.getCost()) + "</font></td><td bgcolor=\"#ffeac1\">" //
        + (op.getProducedRows() != null ? (formatDouble(op.getProducedRows()) + "<br/>rows") : "--") //
        + "</td><td bgcolor=\"#e0e0e0\">" //
        // + "4<br/>io" // io
        + "--" + "</td></tr><tr><td colspan=\"3\"><b>" //
        + op.getType() //
        + (!op.getAccessPredicates().isEmpty() || !op.getFilterPredicates().isEmpty() ? " *" + op.getId() : "") //
        + "</b></td></tr></table>>];\n");

    if (op.getRowsSource() != null || op.getRowsSourceAlias() != null) {
      String label;
      if (op.getRowsSource() != null) {
        label = op.getRowsSource() + (op.getRowsSourceAlias() != null ? " " + op.getRowsSourceAlias() : "");
      } else {
        label = op.getRowsSourceAlias() != null ? op.getRowsSourceAlias() : "";
      }
      if (op.getIndexName() != null) {
        label = label + "<br/>{" + op.getIndexName() + "}";
      }
      sb.append(
          "  d" + op.getId() + " [shape=\"box\" style=\"filled\" fillcolor=\"#d3e4ff\" label=<" + label + ">];\n");
    } else if (op.getIndexName() != null) {
      sb.append("  i" + op.getId() + " [shape=\"box\" style=\"filled\" fillcolor=\"#fffdd3\" label=<"
          + op.getIndexName() + ">];\n");
    }

    for (Operator io : op.getInnerOperators()) {
      renderNodes(io, rowsRange, costRange, sb);
    }

  }

  private static final DecimalFormat INTF = new DecimalFormat("0");
  private static final DecimalFormat DECF = new DecimalFormat("0.0");

  private String formatDouble(final Double n) {
    if (n == null) {
      return "";
    }
    if (n < 9.95) { // 0.0 - 9.9
      return DECF.format(n);
    } else if (n < 9999.5) { // 10 - 9999
      return INTF.format(n);
    } else if (n < 99.5 * 1000) { // 10.0k - 99.9k
      return DECF.format(n / 1000) + " k";
    } else if (n < 999.5 * 1000) { // 100k - 999k
      return INTF.format(n / 1000) + " k";
    } else if (n < 99.95 * 1000000) { // 1.0M - 99.9M
      return DECF.format(n / 1000000) + " M";
    } else { // 100M - 9999M
      return INTF.format(n / 1000000) + " M";
    }
  }

  private void gatherRanges(final Operator op, final Range rowsRange, final Range costRange) {
    rowsRange.register(op.getProducedRows());
    costRange.register(op.getCost());
    for (Operator io : op.getInnerOperators()) {
      gatherRanges(io, rowsRange, costRange);
    }
  }

  private void renderLinks(final Operator op, final Range rowsRange, final Range costRange, final StringBuilder sb) {
    if (op.getRowsSource() != null || op.getRowsSourceAlias() != null) {
      sb.append("  d" + op.getId() + " -> " + op.getId() + " [color=\"blue\" arrowhead=none penwidth=1];\n");
    } else if (op.getIndexName() != null) {
      sb.append("  i" + op.getId() + " -> " + op.getId() + " [color=\"blue\" arrowhead=none penwidth=1];\n");
    }
    for (Operator io : op.getInnerOperators()) {
      double width = rowsRange.computeScalar(io.getProducedRows());
      String rc = renderColor(rowsRange.computeColor(io.getProducedRows()));
      sb.append("  " + io.getId() + " -> " + op.getId() + " [color=\"" + rc + "\" penwidth="
          + WIDTH_FORMATTER.format(width) + "];\n");
      renderLinks(io, rowsRange, costRange, sb);
    }
  }

  private void renderFooter(final Operator op, final StringBuilder sb) {
    sb.append("}\n");
    sb.append("subgraph key {\n");
    sb.append("    rank=source; p [fontname = \"monospace\", shape=plaintext, style=solid, label=\"");
    sb.append("Predicates:\\l");
    renderPredicates(op, sb);
    sb.append("\"];\n");
    sb.append("}\n");
    sb.append("}\n");
  }

  private void renderPredicates(final Operator op, final StringBuilder sb) {
    String note = "  *" + op.getId();
    boolean first = true;
    for (String p : op.getAccessPredicates()) {
      sb.append(note + "  Access: " + escape(p) + "\\l");
      if (first) {
        first = false;
        note = repeat(" ", note.length());
      }
    }
    for (String p : op.getFilterPredicates()) {
      sb.append(note + "  Filter: " + escape(p) + "\\l");
      if (first) {
        first = false;
        note = repeat(" ", note.length());
      }
    }
    for (Operator io : op.getInnerOperators()) {
      renderPredicates(io, sb);
    }
  }

  private String escape(final String p) {
    return p == null ? null : p.replace("\"", "\\\"");
  }

  private String renderColor(final Color c) {
    return String.format("#%2x%2x%2x", c.getRed(), c.getGreen(), c.getBlue());
  }

  private String repeat(final String s, final int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(s);
    }
    return sb.toString();
  }

}
