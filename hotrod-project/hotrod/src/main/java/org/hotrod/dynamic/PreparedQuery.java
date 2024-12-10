package org.hotrod.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamic.segments.ParameterSegment;
import org.hotrod.dynamic.segments.StaticSegmentConsumer;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.SUtil;

public abstract class PreparedQuery implements StaticSegmentConsumer {

  protected StringBuilder sb = new StringBuilder();
  protected List<ParameterSegment> parameters = new ArrayList<>();

  @Override
  public void consume(String literal) {
    this.sb.append(literal);
  }

  @Override
  public void consume(ParameterSegment p) {
    this.parameters.add(p);
  }

  @Override
  public void startNextEntry() {
    // Nothing to do
  }

  private static final int MAX_DISPLAY_VALUE = 100;

  public String getPreview() {
    StringBuilder p = new StringBuilder();
    p.append(this.sb.toString());
    p.append("\n=== Parameters (" + this.parameters.size() + ") ===\n");
    int pos = 1;
    for (ParameterSegment ps : this.parameters) {
      String sqlTypeName = JDBCTypes.codeToShortName(ps.getSQLType());
      Object value = ps.getValue();
      String tostring = "" + value;
      if (tostring.length() > MAX_DISPLAY_VALUE) {
        tostring = tostring.substring(0, MAX_DISPLAY_VALUE - 3) + "...";
      }
      p.append("" + pos++ + ". " + ps.getName() + " (" + SUtil.coalesce(sqlTypeName, "OTHER/" + ps.getSQLType()) + "): "
          + tostring + (value == null ? "" : " (" + value.getClass().getName() + ")") + "\n");
    }
    if (!this.parameters.isEmpty()) {
      p.append("======================\n");
    }
    return p.toString();
  }

}
