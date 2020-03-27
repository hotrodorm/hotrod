package org.hotrod.runtime.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class SQLRenderer {

  public enum SQLSegmentType {
    SET(" set ", ", "), //
    WHERE(" where ", " and ");

    private String prefix;
    private String separator;

    private SQLSegmentType(String prefix, String separator) {
      this.prefix = prefix;
      this.separator = separator;
    }

    public String getPrefix() {
      return prefix;
    }

    public String getSeparator() {
      return separator;
    }

  };

  private SQLSegmentType segmentType;
  private ListWriter lw;
  private List<Object> paramValues;
  private List<Integer> paramTypes;

  public SQLRenderer(SQLSegmentType segmentType) {
    this.segmentType = segmentType;
    this.paramValues = new ArrayList<Object>();
    this.paramTypes = new ArrayList<Integer>();
    this.lw = new ListWriter(segmentType.separator);
  }

  public SQLRenderer(SQLSegmentType segmentType, SQLRenderer sr) {
    this.segmentType = segmentType;
    this.paramValues = sr.paramValues;
    this.paramTypes = sr.paramTypes;
    this.lw = new ListWriter(segmentType.separator);
  }

  public void add(final Object value, final String name, final int jdbcType) {
    if (value != null) {
      this.lw.add(name + " = ?");
      this.paramValues.add(value);
      this.paramTypes.add(jdbcType);
    }
  }

  public String render() {
    return this.lw.getCount() > 0 ? this.segmentType.getPrefix() + this.lw.toString() : "";
  }

  public int getItemCount() {
    return this.lw.getCount();
  }

  public static Object[] getParamValues(SQLRenderer... renderers) {
    List<Object> allValues = new ArrayList<Object>();
    for (SQLRenderer r : renderers) {
      allValues.addAll(r.paramValues);
    }
    Object[] values = new Object[allValues.size()];
    return allValues.toArray(values);
  }

  public static int[] getParamTypes(SQLRenderer... renderers) {
    List<Integer> allTypes = new ArrayList<Integer>();
    for (SQLRenderer r : renderers) {
      allTypes.addAll(r.paramTypes);
    }
    int[] types = new int[allTypes.size()];
    int i = 0;
    for (Integer t : allTypes) {
      types[i++] = t;
    }
    return types;
  }

}
