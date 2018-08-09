package org.hotrod.generator.springjdbc;

import java.io.IOException;
import java.util.StringTokenizer;

import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.runtime.util.ListWriter;

public class CodeGenerationHelper {
  // FIXME generaliza: duplicated code at mybatis package (DAOPrimitives)
  // Helpers

  public static String toParametersSignature(final KeyMetadata km) throws UnresolvableDataTypeException {
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : km.getColumns()) {
      lw.add("final " + cm.getType().getJavaClassName() + " " + cm.getId().getJavaMemberName());
    }
    return lw.toString();
  }

  public static String toParametersCall(final KeyMetadata km, boolean useQuotationMarks) {
    ListWriter lw = new ListWriter(", ");
    String q = (useQuotationMarks ? "\"" : "");
    for (ColumnMetadata cm : km.getColumns()) {
      lw.add(q + cm.getId().getJavaMemberName() + q);
    }
    return lw.toString();
  }

  public static String toParametersCallJdbcType(final KeyMetadata km, boolean useQuotationMarks) {
    ListWriter lw = new ListWriter(", ");
    String q = (useQuotationMarks ? "\"" : "");
    for (ColumnMetadata cm : km.getColumns()) {
      lw.add(q + cm.getType().getJDBCType() + q);
    }
    return lw.toString();
  }

  public static String writeSQLColumns(final KeyMetadata km, boolean useQuotationMarks) {
    ListWriter lw = new ListWriter(", ");
    String q = (useQuotationMarks ? "\"" : "");
    for (ColumnMetadata cm : km.getColumns()) {
      lw.add(q + cm.getTableName() + "." + cm.getColumnName() + q);
    }
    return lw.toString();
  }

  public static String writeSQLColumns(DataSetMetadata ds, boolean excludePK, boolean useQuotationMarks,
      boolean includeLOB, boolean includeTablename) throws IOException {
    String tablenamePrefix = (includeTablename ? ds.getId().getRenderedSQLName() + "." : "");
    StringBuilder sb = new StringBuilder();
    ListWriter lw = new ListWriter(",");
    String q = (useQuotationMarks ? "\"" : "");
    for (ColumnMetadata cm : (excludePK ? ds.getNonPkColumns() : ds.getColumns())) {
      if (!cm.getType().isLOB() || includeLOB) {
        String sqlColumn = q + tablenamePrefix + cm.getColumnName() + q;
        lw.add(sqlColumn);
      }
    }
    sb.append(lw.toString());

    return sb.toString();

  }

  public static String writeSQLQuestionMarks(String insertParamsList) {
    int q = new StringTokenizer(insertParamsList, ",").countTokens();
    if (q <= 0)
      return "";
    StringBuilder sb = new StringBuilder();
    ListWriter lw = new ListWriter(",");
    for (int i = 0; i < q; i++) {
      lw.add("?");
    }
    sb.append(lw.toString());
    return sb.toString();

  }

  public static String getSQLWhereConditionByIndex(final KeyMetadata km) throws IOException {
    return getSQLWhereConditionByIndex(km, "");
  }

  public static String getSQLWhereConditionByIndex(final KeyMetadata km, final String prefix) throws IOException {
    ListWriter lw;
    lw = new ListWriter(" and ");
    for (ColumnMetadata cm : km.getColumns()) {
      lw.add(cm.getTableName() + "." + cm.getColumnName() + " = ?");
    }
    return lw.toString();
  }

  @Deprecated
  public static String mapJavaType2JDBCGetter(PropertyType pt) {
    return mapJavaType2JDBCGetter(pt.getJavaClassName());
  }

  public static String mapJavaType2JDBCGetter(String classname) {
    String[] s = classname.split("\\.");
    String t = s[s.length - 1];
    if (s.length == 1) {
      if (t.equals("byte[]")) {
        return "getBytes";
      }
      throw new RuntimeException(
          "Unsuported conversion from JabaType to JDBC getter. Java class name '" + classname + "'.");
    }
    if (t.equals("Integer")) {
      return "getInt";
    }
    return "get" + t;
  }

  @Deprecated
  public static String mapJavaType2JDBCSetter(PropertyType pt) {
    return mapJavaType2JDBCSetter(pt.getJavaClassName());
  }

  public static String mapJavaType2JDBCSetter(String classname) {
    String[] s = classname.split("\\.");
    String t = s[s.length - 1];
    if (s.length == 1) {
      if (t.equals("byte[]")) {
        return "setBytes";
      }
      throw new RuntimeException(
          "Unsuported conversion from JabaType to JDBC setter. Java class name '" + classname + "'.");
    }
    if (t.equals("Integer")) {
      return "setInt";
    }
    return "set" + t;
  }

  public static final String SPRING_BEAN_SUFFIX = "JDBCImplementation";

  public static void main(String[] args) {
    System.out.println("java.lang.Integer".split("\\.").length);
    System.out.println("byte[]".split("\\.").length);
  }
}
