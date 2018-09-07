package explain.renderers;

import java.awt.Color;
import java.sql.SQLException;
import java.text.DecimalFormat;

import explain.Operator;

public class DotPlanRenderer2 implements PlanRenderer {

  private final DecimalFormat WIDTH_FORMATTER = new DecimalFormat("0.0");
  private final DecimalFormat INTEGER_FORMATTER = new DecimalFormat("0");

  private static final String GREY = "808080";
  private static final String BLUE = "0000ff";
  private static final String RED = "ff0000";
  private static final String WHITE = "ffffff";

  @Override
  public String render(final Operator op) throws SQLException {

    StringBuilder sb = new StringBuilder();

    Stats stats = new Stats();
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
    sb.append("  bgcolor=\"#f4f4f4\";\n");

    sb.append(
        "  graph [fontname = \"helvetica\", fontsize = 9]; node [fontname = \"helvetica\", fontsize = 9]; edge [fontname = \"helvetica\", fontsize = 9];\n");
    sb.append("  labelloc=\"t\"; label=\"SQL Execution Plan - 2018-09-03 10:11\";\n");
    sb.append("subgraph tree {\n");
  }

  private void renderNodes(final Operator op, final Stats stats, final StringBuilder sb) {

    boolean actualData = op.getActualTime() != null || op.getActualRows() != null || op.getActualLoops() != null;

    String cc;
    double ratio;
    if (actualData) {
      cc = renderColor(stats.getTime().computeLinearColor(op.getActualTime()));
      ratio = stats.getTime().computeLinearRatio(op.getActualTime());
    } else {
      cc = renderColor(stats.getCost().computeLinearColor(op.getCost()));
      ratio = stats.getCost().computeLinearRatio(op.getCost());
    }

    // Opening node

    sb.append("  " + op.getId() + " [shape=none width=0 height=0 margin=0 style=\"rounded\" color=\"#c0c0c0\" "
        + "label=<<table cellspacing=\"0\" border=\"1\" bgcolor=\"#ffffff\" cellborder=\"0\">");

    // First row

    if (actualData) {

      FormattedTime ft = formatTimeMs(op.getActualTime());
      Double tp = null;
      if (op.getActualTime() != null && stats.getTime().getMaxValue() != null) {
        tp = op.getActualTime() / stats.getTime().getMaxValue();
      }

      System.out.println(ft.getValue());

      sb.append("<tr>" //
          + "<td width=\"50%\" color=\"#c0c0c0\" border=\"1\" bgcolor=\"" + cc + "\">" //
          + ft.getValue() //
          + "<font color=\"#" + (ratio < 0.7 ? GREY : WHITE) + "\">" //
          + "&nbsp;" + ft.getUnit() + " | " //
          + "</font>" //
          + (tp != null ? INTEGER_FORMATTER.format(100 * tp) : "") //
          + "<font color=\"#" + (ratio < 0.7 ? GREY : WHITE) + "\">&nbsp;%" //
          + "</font>" //
          + "</td>" //
          + "<td align=\"right\">&nbsp;" //
          + (op.getActualRows() != null ? (op.getActualRows() //
              + "<font color=\"#" + GREY + "\"> rows</font>" //
          ) : "") //
          + "&nbsp;" + (op.getActualLoops() != null ? //
              +op.getActualLoops() + "<font color=\"#" + GREY + "\"> x</font>" : "") //
          + "</td>" //
          + "</tr>");
    } else {
      // sb.append("<tr><td width=\"60%\" bgcolor=\"" + cc + "\">" + "<font
      // point-size=\"18\">" //
      // + formatDouble(op.getCost()) + "</font></td><td bgcolor=\"#ffeac1\">"
      // //
      // + (op.getProducedRows() != null ? (formatDouble(op.getProducedRows()) +
      // "<br/>rows") : "--") //
      // + "</td><td bgcolor=\"#e0e0e0\">" //
      // // + "4<br/>io" // io
      // + "--" + "</td></tr>");
    }

    // Second row

    boolean hasPredicates = !op.getAccessPredicates().isEmpty() || !op.getFilterPredicates().isEmpty();
    sb.append("<tr><td colspan=\"2\" align=\"left\">" //
        + "<b>" + (op.getType() != null ? op.getType().toUpperCase() : "") + "</b>" //
        + (hasPredicates ? "<font color=\"#" + GREY + "\">&nbsp;&nbsp;*" + op.getId() + "</font>" : "&nbsp;") //
        + (op.includesHeapFetch() ? "<font color=\"#" + BLUE + "\">&#x25e9;</font> " : "") //
        + "</td></tr>");
    // &#x25c6;&#x25c0;&#x25c1;&#x25c2;&#x25c3;&#x25c4;&#x25c5;&#x2b88;&#x2b05;&#x21e6;

    // Third row

    sb.append("<tr>" //
        + "<td align=\"left\">" //
        + (op.getJoinType() != null ? "Inner " + "<font color=\"#" + GREY + "\">" + "join" + "</font>" : "") //
        + "</td>" //
        + "<td align=\"right\">" //
//        + "<IMG SRC=\"costly.png\">"
        + "<font color=\"#" + RED + "\">" 
        + "<b>($) (T) (=) (x)</b></font>" //
        + "</td>" //
        + "</tr>");

    // Closing node

    sb.append("</table>>];\n");

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

  private static class FormattedTime {

    private String value;
    private String unit;

    public FormattedTime(final String value, final String unit) {
      this.value = value;
      this.unit = unit;
    }

    public String getValue() {
      return value;
    }

    public String getUnit() {
      return unit;
    }

  }

  private FormattedTime formatTimeMs(final Double ms) {
    if (ms == null) {
      return new FormattedTime("", "");
    }
    if (ms < 1) { // < 1 ms
      return new FormattedTime("&lt;1", "ms");
    } else if (ms < 99999) { // < 99999 ms
      return new FormattedTime(INTF.format(ms), "ms");
    } else { // > 100s
      return new FormattedTime(INTF.format(ms / 1000), " s");
    }
  }

  private void gatherStats(final Operator op, final Stats stats) {
    stats.getRows().register(op.getProducedRows());
    stats.getCost().register(op.getCost());
    stats.getTime().register(op.getActualTime());
    for (Operator io : op.getInnerOperators()) {
      gatherStats(io, stats);
    }
  }

  private void renderLinks(final Operator op, final Stats stats, final StringBuilder sb) {
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
