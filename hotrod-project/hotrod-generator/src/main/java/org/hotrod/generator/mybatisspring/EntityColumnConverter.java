package org.hotrod.generator.mybatisspring;

import java.io.IOException;
import java.io.Writer;

import org.hotrod.metadata.ColumnMetadata;

public class EntityColumnConverter {

  // Properties

  private Writer w;
  private ColumnMetadata cm;
  private String typeHandlerClassName;

  // Constructor

  public EntityColumnConverter(final Writer w, final ColumnMetadata cm) {
    this.w = w;
    this.cm = cm;
    this.typeHandlerClassName = cm.getId().getJavaClassName() + "TypeHandler";
  }

  public String getTypeHandlerClassName() {
    return typeHandlerClassName;
  }

  // Behavior

  public void write() throws IOException {
    String interType = this.cm.getConverter().getJavaRawType();
    String type = this.cm.getConverter().getJavaType();
    String setter = this.cm.getConverter().getJdbcSetterMethod();
    String getter = this.cm.getConverter().getJdbcGetterMethod();
    String converter = this.cm.getConverter().getJavaClass();

    String property = this.cm.getId().getJavaMemberName();

    println("  // TypeHandler for " + (property != null ? "property " + property : "column " + cm.getName())
        + " using Converter " + converter + ".");
    println();
    println("  public static class " + typeHandlerClassName + " implements TypeHandler<" + type + "> {");
    println();
    println("    private static TypeConverter<" + interType + ", " + type + "> CONVERTER = new " + converter + "();");
    println();
    println("    @Override");
    println("    public " + type + " getResult(final ResultSet rs, final String columnName) throws SQLException {");
    println("      " + interType + " value = rs." + getter + "(columnName);");
    println("      if (rs.wasNull()) {");
    println("        value = null;");
    println("      }");
    println("      return CONVERTER.decode(value);");
    println("    }");
    println();
    println("    @Override");
    println("    public " + type + " getResult(final ResultSet rs, final int columnIndex) throws SQLException {");
    println("      " + interType + " value = rs." + getter + "(columnIndex);");
    println("      if (rs.wasNull()) {");
    println("        value = null;");
    println("      }");
    println("      return CONVERTER.decode(value);");
    println("    }");
    println();
    println("    @Override");
    println(
        "    public " + type + " getResult(final CallableStatement cs, final int columnIndex) throws SQLException {");
    println("      " + interType + " value = cs." + getter + "(columnIndex);");
    println("      if (cs.wasNull()) {");
    println("        value = null;");
    println("      }");
    println("      return CONVERTER.decode(value);");
    println("    }");
    println();
    println("    @Override");
    println("    public void setParameter(final PreparedStatement ps, final int columnIndex, final " + type
        + " v, final JdbcType jdbcType)");
    println("        throws SQLException {");
    println("      " + interType + " value = CONVERTER.encode(v);");
    println("      if (value == null) {");
    println("        ps.setNull(columnIndex, jdbcType.TYPE_CODE);");
    println("      } else {");
    println("        ps." + setter + "(columnIndex, value);");
    println("      }");
    println("    }");
    println();
    println("  }");
    println();
  }

  // Utilities

  private void println(final String txt) throws IOException {
    this.w.write(txt);
    println();
  }

  private void println() throws IOException {
    this.w.write("\n");
  }

}
