package explain.renderers;

import java.sql.SQLException;
import java.text.DecimalFormat;

import explain.Operator;

public class TextPlanRenderer {

  public String render(final Operator op) throws SQLException {

    StringBuilder sb = new StringBuilder();

    renderOperator(op, 0, sb);

    sb.append("\n");
    sb.append("Predicates:\n");
    sb.append("-----------\n");
    renderPredicates(op, sb);

    renderLegend(sb);

    String plan = sb.toString();

    return plan;
  }

  private void renderOperator(final Operator op, final int level, final StringBuilder sb) {
    DecimalFormat df = new DecimalFormat("0");
    DecimalFormat cf = new DecimalFormat("0.00");
    String indent = getFiller("  ", level);
    sb.append(indent + "+");
    if (op.getCost() != null) {
      sb.append(" " + cf.format(op.getCost()));
    }

    if (op.getCost() != null && !op.getInnerOperators().isEmpty()) {
      double innerCost = 0;
      for (Operator s : op.getInnerOperators()) {
        if (s.getCost() != null) {
          innerCost = innerCost + s.getCost();
        }
      }
      if (op.getCost() > 1.2 * innerCost) {
        sb.append(" $");
      }
    }

    if (op.includesHeapFetch()) {
      sb.append(" <<");
    }
    sb.append(" " + op.getType());

    if (op.getRowsSource() != null) {
      if (op.getRowsSourceAlias() != null) {
        sb.append(" [" + op.getRowsSource() + " " + op.getRowsSourceAlias() + "]");
      } else {
        sb.append(" [" + op.getRowsSource() + "]");
      }
    } else if (op.getRowsSourceAlias() != null) {
      sb.append(" [" + op.getRowsSourceAlias() + "]");
    }
    if (op.getIndexName() != null) {
      sb.append(" {" + op.getIndexName() + "}");
    }

    if (!op.getAccessPredicates().isEmpty() || !op.getFilterPredicates().isEmpty()
        || (op.getIndexName() != null && op.getIndexDescription() != null)) {
      sb.append(" *" + op.getId());
    }

    if (op.getProducedRows() != null || op.getProducedBytes() != null) {
      sb.append(" (");
    }
    if (op.getProducedRows() != null) {
      sb.append("" + df.format(op.getProducedRows()) + " rows");
    }
    if (op.getProducedRows() != null && op.getProducedBytes() != null) {
      sb.append(", ");
    }
    if (op.getProducedBytes() != null) {
      sb.append("" + df.format(op.getProducedBytes()) + " bytes");
    }
    if (op.getProducedRows() != null || op.getProducedBytes() != null) {
      sb.append(")");
    }

    sb.append("\n");

    for (String name : op.getExtraProperties().keySet()) {
      String value = op.getExtraProperties().get(name);
      sb.append(indent + "     [" + name + "=" + value + "]\n");
    }

    for (Operator s : op.getInnerOperators()) {
      renderOperator(s, level + 1, sb);
    }
  }

  private void renderPredicates(final Operator op, final StringBuilder sb) {
    String prompt = " *" + op.getId() + " ";
    for (String access : op.getAccessPredicates()) {
      sb.append(prompt + "Access: " + access + "\n");
      if (op.getIndexName() != null && op.getIndexDescription() != null) {
        sb.append(getFiller(" ", prompt.length() + 8) + "index: " + op.getIndexDescription() + "\n");
      }
    }
    for (String filter : op.getFilterPredicates()) {
      String key = !op.getAccessPredicates().isEmpty() ? getFiller(" ", prompt.length()) : prompt;
      sb.append(key + "Filter: " + filter + "\n");
    }
    for (Operator s : op.getInnerOperators()) {
      renderPredicates(s, sb);
    }
  }

  private void renderLegend(final StringBuilder sb) {
    sb.append("\n");
    sb.append("Legend:\n");
    sb.append("<< Reads from the heap.\n");
    sb.append("$ Operation is at least 20% more expensive than its combined children.");
  }

  private String getFiller(final String txt, final int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(txt);
    }
    return sb.toString();
  }

}
