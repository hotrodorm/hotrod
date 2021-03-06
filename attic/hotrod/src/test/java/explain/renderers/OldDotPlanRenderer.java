package explain.renderers;

import java.awt.Color;
import java.sql.SQLException;
import java.text.DecimalFormat;

import explain.Operator;

public class OldDotPlanRenderer implements PlanRenderer {

  private static final DecimalFormat WIDTH_FORMATTER = new DecimalFormat("0.0");

  @Override
  public String render(final Operator op) throws SQLException {

    StringBuilder sb = new StringBuilder();

    OldStats stats = new OldStats();
    gatherStats(op, stats);

    renderHeader(sb);
    renderNodes(op, stats, sb);
    renderLinks(op, stats, sb);
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

  private void renderNodes(final Operator op, final OldStats stats, final StringBuilder sb) {

    boolean actualData = op.getActualTime() != null || op.getActualRows() != null || op.getActualLoops() != null;

    String cc;
    if (actualData) {
      cc = renderColor(stats.getTime().computeLinearColor(op.getActualTime()));
    } else {
      cc = renderColor(stats.getCost().computeLinearColor(op.getCost()));
    }

    sb.append("  " + op.getId() + " [shape=none width=0 height=0 margin=0 style=\"rounded\" color=\"gray40\" "
        + "label=<<table cellspacing=\"0\" border=\"0\" cellborder=\"1\">");

    if (actualData) {
      sb.append("<tr><td width=\"60%\" bgcolor=\"" + cc + "\">" + "<font point-size=\"18\">" //
          + formatTimeMs(op.getActualTime()) + "</font></td><td bgcolor=\"#ffeac1\">" //
          + (op.getActualRows() != null ? (op.getActualRows() + "<br/>rows") : "--") //
          + "</td><td bgcolor=\"#e0e0e0\">" //
          // + "4<br/>io" // io
          + (op.getActualLoops() != null ? (op.getActualLoops() + "<br/>loops") : "--") //
          + "</td></tr>");
      sb.append("<tr><td width=\"60%\" bgcolor=\"#ffffff\"><i>" //
          + formatDouble(op.getCost()) + " cost</i></td><td bgcolor=\"#ffeac1\">" //
          + (op.getProducedRows() != null ? ("<i>" + formatDouble(op.getProducedRows()) + "</i>") : "--") //
          + "</td><td bgcolor=\"#e0e0e0\">" //
          // + "4<br/>io" // io
          + "--" + "</td></tr>");
    } else {
      sb.append("<tr><td width=\"60%\" bgcolor=\"" + cc + "\">" + "<font point-size=\"18\">" //
          + formatDouble(op.getCost()) + "</font></td><td bgcolor=\"#ffeac1\">" //
          + (op.getProducedRows() != null ? (formatDouble(op.getProducedRows()) + "<br/>rows") : "--") //
          + "</td><td bgcolor=\"#e0e0e0\">" //
          // + "4<br/>io" // io
          + "--" + "</td></tr>");
    }

    sb.append("<tr><td colspan=\"3\" bgcolor=\"#" + (op.includesHeapFetch() ? "d3e4ff" : "ffffff") + "\"><b>" //
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
      // celeste: d3e4ff
      sb.append(
          "  d" + op.getId() + " [shape=\"box\" style=\"filled\" fillcolor=\"#ceefb3\" label=<" + label + ">];\n");
    } else if (op.getIndexName() != null) {
      // light green: a3f75d
      // light yellow: fffdd3
      sb.append("  i" + op.getId() + " [shape=\"box\" style=\"filled\" fillcolor=\"#a3f75d\" label=<"
          + op.getIndexName() + ">];\n");
    }

    for (Operator io : op.getInnerOperators()) {
      renderNodes(io, stats, sb);
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

  private String formatTimeMs(final Double ms) {
    if (ms == null) {
      return "";
    }
    if (ms < 9.95) { // 0.0 - 9.9
      return DECF.format(ms) + " ms";
    } else if (ms < 9999.5) { // 10 - 9999
      return INTF.format(ms) + " ms";
    } else if (ms < 99.5 * 1000) { // 10.0s - 99.9s
      return DECF.format(ms / 1000) + " s";
    } else { // > 100s
      return INTF.format(ms / 1000) + " s";
    }
  }

  private void gatherStats(final Operator op, final OldStats stats) {
    stats.getRows().register(op.getProducedRows());
    stats.getCost().register(op.getCost());
    stats.getTime().register(op.getActualTime());
    for (Operator io : op.getInnerOperators()) {
      gatherStats(io, stats);
    }
  }

  private void renderLinks(final Operator op, final OldStats stats, final StringBuilder sb) {
    if (op.getRowsSource() != null || op.getRowsSourceAlias() != null) {
      sb.append("  d" + op.getId() + " -> " + op.getId() + " [color=\"blue\" arrowhead=none penwidth=1];\n");
    } else if (op.getIndexName() != null) {
      sb.append("  i" + op.getId() + " -> " + op.getId() + " [color=\"blue\" arrowhead=none penwidth=1];\n");
    }
    for (Operator io : op.getInnerOperators()) {
      double width = stats.getRows().computeScalar(io.getProducedRows());
      String rc = renderColor(stats.getRows().computeLogColor(io.getProducedRows()));
      sb.append("  " + io.getId() + " -> " + op.getId() + " [color=\"" + rc + "\" penwidth="
          + WIDTH_FORMATTER.format(width) + "];\n");
      renderLinks(io, stats, sb);
    }
  }

  private void renderFooter(final Operator op, final StringBuilder sb) {
    sb.append("}\n");
    sb.append("subgraph key {\n");
    sb.append("    rank=min; p [fontname = \"monospace\", shape=plaintext, style=solid, label=\"");
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
