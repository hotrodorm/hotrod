package org.hotrod.generator.springjdbc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hotrod.config.ConverterTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.DAOType;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.metadata.SelectDataSetMetadata;
import org.hotrod.utils.ClassPackage;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;
import org.nocrala.tools.database.tartarus.core.JdbcKey;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class DAOPrimitives {

  private DataSetMetadata ds;
  private DataSetLayout dsg;
  private SpringJDBCGenerator generator;
  private DAOType type;

  private DAO dao = null;
  private ClassPackage fragmentPackage;

  private Writer w;

  private boolean hasLobs = false;

  // FIXME manage versioned rows
  // FIXME implement unique index methods
  public DAOPrimitives(final HotRodFragmentConfigTag hrtag, final DataSetMetadata dataSet, final DataSetLayout dsg,
      final SpringJDBCGenerator generator, final DAOType type) {
    this.ds = dataSet;
    this.dsg = dsg;
    this.generator = generator;
    if (type == null) {
      throw new RuntimeException("DAOType cannot be null.");
    }
    this.type = type;

    this.fragmentPackage = hrtag != null && hrtag.getFragmentPackage() != null ? hrtag.getFragmentPackage() : null;
  }

  public boolean isTable() {
    return this.type == DAOType.TABLE;
  }

  public boolean isView() {
    return this.type == DAOType.VIEW;
  }

  public boolean isSelect() {
    return this.type == DAOType.SELECT;
  }

  public void setDao(final DAO dao) {
    this.dao = dao;
  }

  public void generate() throws UncontrolledException, ControlledException {

    String className = this.getClassName() + ".java";
    this.dsg.getDAOPrimitivePackage(fragmentPackage);
    File prim = new File(this.dsg.getDaoPrimitivePackageDir(fragmentPackage), className);
    this.w = null;

    for (ColumnMetadata cm : this.ds.getColumns()) {
      if (cm.getType().isLOB()) {
        hasLobs = true;
        break;
      }
    }

    // DataSetMetadata
    try {
      this.w = new BufferedWriter(new FileWriter(prim));

      writeClassHeader();
      if (this.isTable()) {
        writeJavaToDatabaseFieldMapper();
      }
      writeVOClass();
      writeProperties();
      writeVOCloneMethods();

      if (this.isTable()) {
        writePersistenceMethods();
      } else if (this.isSelect()) {
        writeSelectMethods();
      }

      writeToString();
      if (this.isTable()) {
        writeTableOrderBy();
      } else if (this.isSelect()) {
        writeSelectOrderBy();
      }
      writeGettersAndSetters();
      writeMapper();

      writeJDBCTemplate();

      writeClassFooter();

    } catch (IOException e) {
      throw new UncontrolledException(
          "Could not generate DAO primitives class: could not write to file '" + prim.getName() + "'.", e);
    } catch (UnresolvableDataTypeException e) {
      e.printStackTrace();
      throw new ControlledException("Could not generate DAO primitives for table '" + e.getTableName()
          + "'. Could not handle columns '" + e.getColumnName() + "' type: " + e.getTypeName());
    } finally {
      if (this.w != null) {
        try {
          this.w.close();
        } catch (IOException e) {
          throw new UncontrolledException(
              "Could not generate DAO primitives class: could not close file '" + prim.getName() + "'.", e);
        }
      }
    }

  }

  private void writeSelectMethods() throws IOException, UnresolvableDataTypeException {
    SelectDataSetMetadata sm = (SelectDataSetMetadata) this.ds;
    println("  // ===================");
    println("  // Persistence methods");
    println("  // ===================");
    println();
    String springBeanName = this.ds.getId().getJavaMemberName() + CodeGenerationHelper.SPRING_BEAN_SUFFIX;
    String springBeanClassName = this.ds.getId().getJavaClassName() + CodeGenerationHelper.SPRING_BEAN_SUFFIX;

    String params = null;
    List<ParameterTag> pl = sm.getSelectTag().getParameterDefinitions();
    if (pl != null && pl.size() > 0) {
      ListWriter lw = new ListWriter(", ");
      for (ParameterTag p : pl) {
        lw.add("final " + p.getJavaType() + " " + p.getName());

      }
      params = lw.toString();
    }

    // SELECT by Plain SQL
    println("  // select by plain SQL with optional ordering (always available)");
    println();
    println("  public static List<" + this.dao.getClassName() + "> select(" + (params != null ? params + ", " : "")
        + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
    println(
        "    " + springBeanClassName + " " + springBeanName + " = ApplicationContextProvider.getApplicationContext()");
    println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
    println("    Map<String,Object> params = new HashMap<String,Object>();");
    if (pl != null && pl.size() > 0) {
      for (ParameterTag p : pl) {
        println("    params.put(\"" + p.getName() + "\", " + p.getName() + ");");
      }
    }
    println("    return " + springBeanName + ".selectCustom(params, orderBies);");
    println("  }");

    println();

  }

  private void writePersistenceMethods() throws IOException, UnresolvableDataTypeException {
    println("  // ===================");
    println("  // Persistence methods");
    println("  // ===================");
    println();
    String springBeanName = this.ds.getId().getJavaMemberName() + CodeGenerationHelper.SPRING_BEAN_SUFFIX;
    String springBeanClassName = this.ds.getId().getJavaClassName() + CodeGenerationHelper.SPRING_BEAN_SUFFIX;

    boolean hasPK = (this.ds.getPK() != null);
    // SELECT by PK
    if (hasPK) {
      println("  // select by PK (available when the table has a PK)");
      println();
      if (hasLobs) {

        println("  public static " + this.dao.getClassName() + " select("
            + CodeGenerationHelper.toParametersSignature(this.ds.getPK()) + ") {");
        println("    return " + this.dao.getClassName() + ".select("
            + CodeGenerationHelper.toParametersCall(this.ds.getPK(), false) + ", false);");
        println("  }");
        println();
        println("  // select by PK (optionally excludes LOB fields)");
      }
      println("  public static " + this.dao.getClassName() + " select("
          + CodeGenerationHelper.toParametersSignature(this.ds.getPK()) + (hasLobs ? ", boolean excludeLOB" : "")
          + ") {");
      println("    " + springBeanClassName + " " + springBeanName
          + " = ApplicationContextProvider.getApplicationContext()");
      println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
      println("    return " + springBeanName + ".select("
          + CodeGenerationHelper.toParametersCall(this.ds.getPK(), false) + (hasLobs ? ", excludeLOB" : "") + ");");
      println("  }");
    } else {
      println("    // select by PK: no mapping generated, since the table does not have a PK");
    }
    println();

    // FIXME implement select by UIx

    // SELECT by Example
    println("  // select by example with optional ordering (always available)");
    println();
    if (hasLobs) {
      println("  public static List<" + this.dao.getClassName() + "> selectByExample(final " + this.dao.getClassName()
          + " example, final " + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
      println("    return " + this.dao.getClassName() + ".selectByExample(example, false, orderBies);");
      println("  }");

      println("  public static List<" + this.dao.getClassName() + "> selectByExample(final " + this.dao.getClassName()
          + " example, boolean excludeLOB, final " + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
      println("    " + springBeanClassName + " " + springBeanName
          + " = ApplicationContextProvider.getApplicationContext()");
      println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
      println("    return " + springBeanName + ".selectByExample(example, excludeLOB, orderBies);");
      println("  }");
    } else {
      // TODO improve this: duplicated code
      println("  public static List<" + this.dao.getClassName() + "> selectByExample(final " + this.dao.getClassName()
          + " example, final " + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
      println("    " + springBeanClassName + " " + springBeanName
          + " = ApplicationContextProvider.getApplicationContext()");
      println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
      println("    return " + springBeanName + ".selectByExample(example, orderBies);");
      println("  }");
      println();
    }
    println();

    // SELECT by Criteria
    println("  // select by criteria with optional ordering (always available)");
    println();
    if (hasLobs) {
      println("  public static List<" + this.dao.getClassName()
          + "> selectByCriteria(final SQLLogicalExpression criteria, final " + this.ds.getId().getJavaClassName()
          + "OrderBy... orderBies) {");
      println("    return " + this.dao.getClassName() + ".selectByCriteria(criteria, false, orderBies);");
      println("  }");

      println("  public static List<" + this.dao.getClassName()
          + "> selectByCriteria(final SQLLogicalExpression criteria, boolean excludeLOB, final "
          + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
      println("    " + springBeanClassName + " " + springBeanName
          + " = ApplicationContextProvider.getApplicationContext()");
      println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
      println("    return " + springBeanName + ".selectByCriteria(criteria, excludeLOB, orderBies);");
      println("  }");
    } else {
      // TODO improve this: duplicated code
      println("  public static List<" + this.dao.getClassName()
          + "> selectByCriteria(final SQLLogicalExpression criteria, final " + this.ds.getId().getJavaClassName()
          + "OrderBy... orderBies) {");
      println("    " + springBeanClassName + " " + springBeanName
          + " = ApplicationContextProvider.getApplicationContext()");
      println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
      println("    return " + springBeanName + ".selectByCriteria(criteria, orderBies);");
      println("  }");
      println();
    }
    println();

    // SELECT by Plain SQL
    println("  // select by plain SQL with optional ordering (always available)");
    println();
    println("  public static List<" + this.dao.getClassName()
        + "> selectBySQL(final List<SQLJoin> joins, final SQLLogicalExpression where, final "
        + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
    println(
        "    " + springBeanClassName + " " + springBeanName + " = ApplicationContextProvider.getApplicationContext()");
    println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
    println("    return " + springBeanName + ".selectBySQL(joins, where, orderBies);");
    println("  }");

    println();

    println("  // select by plain SQL with optional ordering (always available)");
    println();
    println("  public static List<" + this.dao.getClassName()
        + "> selectBySQL(final SQLJoin join, final SQLLogicalExpression where, final "
        + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
    println(
        "    " + springBeanClassName + " " + springBeanName + " = ApplicationContextProvider.getApplicationContext()");
    println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
    // TODO improve called method instead
    println("    List<SQLJoin> joins = new ArrayList<SQLJoin>(1);");
    println("    joins.add(join);");
    println("    return " + springBeanName + ".selectBySQL(joins, where, orderBies);");
    println("  }");

    println();

    // FIXME imlement (parent/children) navigation by FKs

    // INSERT
    println("  // insert");
    println();
    println("  public int insert() {");
    println(
        "    " + springBeanClassName + " " + springBeanName + " = ApplicationContextProvider.getApplicationContext()");
    println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
    println("    return " + springBeanName + ".insert((" + this.dao.getClassName() + ") this);");
    println("  }");
    println();
    println();

    if (hasPK) {
      println("  // update by PK");
      println();
      // TODO improve: duplicated code
      if (hasLobs) {
        println("  public int update() {");
        println("    return this.update(false);");
        println("  }");
        println();

        println("  public int update(boolean excludeLOB) {");
        println("    " + springBeanClassName + " " + springBeanName
            + " = ApplicationContextProvider.getApplicationContext()");
        println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
        println("    return " + springBeanName + ".update((" + this.dao.getClassName() + ") this, excludeLOB);");
        println("  }");
      } else {
        println("  public int update() {");
        println("    " + springBeanClassName + " " + springBeanName
            + " = ApplicationContextProvider.getApplicationContext()");
        println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
        println("    return " + springBeanName + ".update((" + this.dao.getClassName() + ") this);");
        println("  }");
      }
    } else {
      println("    // update by PK: no mapping generated, since the table does not have a PK");
    }
    println();

    println("  // update by example");
    println();
    println("  public static int updateByExample(final " + this.dao.getClassName() + " example, final "
        + this.dao.getClassName() + " updateValues) {");
    println(
        "    " + springBeanClassName + " " + springBeanName + " = ApplicationContextProvider.getApplicationContext()");
    println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
    println("    return " + springBeanName + ".updateByExample(example, updateValues);");
    println("  }");
    println();
    println();

    if (hasPK) {
      println("  // delete by PK");
      println();
      println("  public int delete() {");
      println("    " + springBeanClassName + " " + springBeanName
          + " = ApplicationContextProvider.getApplicationContext()");
      println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
      println("    return " + springBeanName + ".delete((" + this.dao.getClassName() + ") this);");
      println("  }");
      println();
    } else {
      println("    // update by PK: no mapping generated, since the table does not have a PK");
    }
    println();

    println("  // delete by example");
    println();
    println("  public static int deleteByExample(final " + this.dao.getClassName() + " example) {");
    println(
        "    " + springBeanClassName + " " + springBeanName + " = ApplicationContextProvider.getApplicationContext()");
    println("        .getBean(\"" + springBeanName + "\", " + springBeanClassName + ".class);");
    println("    return " + springBeanName + ".deleteByExample(example);");
    println("  }");
    println();

    println("  // refresh");
    println();
    println("  public void refresh() {");
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : this.ds.getPK().getColumns()) {
      lw.add("this.vo." + cm.getId().getJavaMemberName());
    }
    println("    this.vo = select(" + lw.toString() + ").getVO();");
    println("  }");
    println();
    println();
  }

  private void writeMapper() throws IOException, UnresolvableDataTypeException {
    println("  // ====================");
    println("  // Spring JDBC - Mapper");
    println("  // ====================");
    println();
    println("  public static class " + this.ds.getId().getJavaClassName() + "Mapper implements RowMapper<"
        + this.dao.getClassName() + "> {");
    println();
    println("    public " + this.dao.getClassName() + " mapRow(ResultSet rs, int rowNum) throws SQLException {");
    println("      " + this.dao.getClassName() + " row = new " + this.dao.getClassName() + "();");
    for (ColumnMetadata cm : this.ds.getColumns()) {
      println("      {");
      if (cm.getType().isLOB()) {
        // TODO improve this
        println("        try {");
        println("          " + cm.getType().getJavaClassName() + " v = rs."
            + CodeGenerationHelper.mapJavaType2JDBCGetter(cm.getType().getJavaClassName()) + "(\"" + cm.getColumnName()
            + "\");");
        println("          row." + cm.getId().getJavaSetter() + "(rs.wasNull() ? null : v);");
        println("        } catch (SQLException e) {");
        println("          //metadata discarded");
        println("        }");

      } else {
        ConverterTag cvt = cm.getConverter();
        if (cvt != null) {
          println("        " + cvt.getJavaIntermediateType() + " v = rs."
              + CodeGenerationHelper.mapJavaType2JDBCGetter(cvt.getJavaIntermediateType()) + "(\"" + cm.getColumnName()
              + "\");");
          println("        row." + cm.getId().getJavaSetter() + "(rs.wasNull() ? null : new " + cvt.getJavaClass()
              + "().decode(v));");
        } else {
          println("        " + cm.getType().getJavaClassName() + " v = rs."
              + CodeGenerationHelper.mapJavaType2JDBCGetter(cm.getType().getJavaClassName()) + "(\""
              + cm.getColumnName() + "\");");
          println("        row." + cm.getId().getJavaSetter() + "(rs.wasNull() ? null : v);");
        }
      }
      println("      }");
    }

    println();
    println("      return row;");
    println("    }");
    println();
    if (this.isTable()) {
      println("    public static void setValues(PreparedStatement pst, " + this.dao.getClassName()
          + " domain) throws SQLException {");

      int i = 0;
      boolean isAutogeneratedPk = false;

      if (this.ds.getPK() != null) {
        ColumnMetadata pkm = this.ds.getPK().getColumns().get(0);
        isAutogeneratedPk = (pkm.getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT)
            || (pkm.getSequenceId() != null);
      }

      for (ColumnMetadata cm : (isAutogeneratedPk ? this.ds.getNonPkColumns() : this.ds.getColumns())) {
        i++;
        println("      if (domain." + cm.getId().getJavaGetter() + "() == null) {");
        println("        pst.setNull(" + i + ", " + cm.getType().getJDBCType() + ");");
        println("      } else {");
        ConverterTag cvt = cm.getConverter();
        if (cvt != null) {
          println("        pst." + CodeGenerationHelper.mapJavaType2JDBCSetter(cvt.getJavaIntermediateType()) + "(" + i
              + ", new " + cvt.getJavaClass() + "().encode(domain." + cm.getId().getJavaGetter() + "()));");
        } else {
          println("        pst." + CodeGenerationHelper.mapJavaType2JDBCSetter(cm.getType().getJavaClassName()) + "("
              + i + ", domain." + cm.getId().getJavaGetter() + "());");
        }
        println("      }");
      }
      println("    }");
    }
    println("  }");
    println();
  }

  private void writeToString() throws IOException {
    println("  // to string");
    println();
    println("  public String toString() {");
    println("    return this.vo.toString();");
    println("  }");

    println();
  }

  private void writeTableOrderBy() throws IOException {
    println("  // ========");
    println("  // Ordering");
    println("  // ========");
    println();
    println("  public enum " + ds.getId().getJavaClassName() + "OrderBy implements OrderBy {");
    println();
    ListWriter lw = new ListWriter(",\n    ");
    for (ColumnMetadata cm : this.ds.getColumns()) {
      if (!cm.getType().isLOB()) {
        lw.add(cm.getColumnName() + "(\"" + cm.getTableName() + "\",\"" + cm.getColumnName() + "\",true)");
        lw.add(cm.getColumnName() + "$DESC(\"" + cm.getTableName() + "\",\"" + cm.getColumnName() + "\",false)");
      }
    }
    println("    " + lw.toString() + ";");

    println();
    println("    private String tableName;");
    println("    private String columnName;");
    println("    private boolean ascending;");
    println();
    println("    private " + ds.getId().getJavaClassName()
        + "OrderBy(final String tableName, final String columnName, boolean ascending) {");
    println("      this.tableName = tableName;");
    println("      this.columnName = columnName;");
    println("      this.ascending = ascending;");
    println("    }");
    println();
    println("    public String getTableName() {");
    if (this.isView()) {
      println("      return null;");
    } else {
      println("      return this.tableName;");
    }
    println("    }");
    println();
    println("    public String getColumnName() {");
    println("      return this.columnName;");
    println("    }");
    println();
    println("    public boolean isAscending() {");
    println("      return this.ascending;");
    println("    }");
    println();
    println("  }");
    println();
  }

  private void writeSelectOrderBy() throws IOException {
    println("  // ========");
    println("  // Ordering");
    println("  // ========");
    println();
    println("  public enum " + ds.getId().getJavaClassName() + "OrderBy implements OrderBy {");
    println();
    ListWriter lw = new ListWriter(",\n    ");
    for (ColumnMetadata cm : this.ds.getColumns()) {
      if (!cm.getType().isLOB()) {
        lw.add(cm.getColumnName() + "(\"" + cm.getColumnName() + "\",true)");
        lw.add(cm.getColumnName() + "$DESC(\"" + cm.getColumnName() + "\",false)");
      }
    }
    println("    " + lw.toString() + ";");

    println();
    println("    private String columnName;");
    println("    private boolean ascending;");
    println();
    println("    private " + ds.getId().getJavaClassName() + "OrderBy(final String columnName, boolean ascending) {");
    println("      this.columnName = columnName;");
    println("      this.ascending = ascending;");
    println("    }");
    println();
    println("    public String getTableName() {");
    println("      return null;");
    println("    }");
    println();
    println("    public String getColumnName() {");
    println("      return this.columnName;");
    println("    }");
    println();
    println("    public boolean isAscending() {");
    println("      return this.ascending;");
    println("    }");
    println();
    println("  }");
    println();
  }

  private void writeVOClass() throws IOException, UnresolvableDataTypeException {
    println("  // =================");
    println("  // Primitive VO");
    println("  // =================");
    println();
    println("  public static class " + this.getVOName() + " implements Serializable {");
    println();
    println("  private static final long serialVersionUID = 1L;");
    println();

    // properties
    println("  // VO Properties (" + (this.isTable() ? "table" : (this.isView() ? "view" : "select")) + " columns)");
    println();
    for (ColumnMetadata cm : this.ds.getColumns()) {
      ConverterTag cvt = cm.getConverter();
      if (cvt != null) {
        println("    private " + cvt.getJavaIntermediateType() + " " + cm.getId().getJavaMemberName() + " = null;");
      } else {
        println("    private " + cm.getType().getJavaClassName() + " " + cm.getId().getJavaMemberName() + " = null;");
      }
    }
    println();

    // getters/setters
    for (ColumnMetadata cm : this.ds.getColumns()) {
      PropertyType type = cm.getType();
      String m = cm.getId().getJavaMemberName();

      ConverterTag cvt = cm.getConverter();
      if (cvt != null) {
        println("    public " + cvt.getJavaType() + " " + cm.getId().getJavaGetter() + "() {");
        println("      return new " + cvt.getJavaClass() + "().decode(this." + m + ");");
        println("    }");
        println();

        println("    public void " + cm.getId().getJavaSetter() + "(final " + cvt.getJavaType() + " " + m + ") {");
        println("      this." + m + " = new " + cvt.getJavaClass() + "().encode(" + m + ");");
        println("    }");
      } else {
        println("    public " + type.getJavaClassName() + " " + cm.getId().getJavaGetter() + "() {");
        println("      return this." + m + ";");
        println("    }");
        println();

        println(
            "    public void " + cm.getId().getJavaSetter() + "(final " + type.getJavaClassName() + " " + m + ") {");
        println("      this." + m + " = " + m + ";");
        println("    }");
      }
      println();

    }
    // toString
    println("    // to string");
    println("    public String toString() {");
    println("      StringBuilder sb = new StringBuilder();");
    println("      sb.append(\"[\");");
    ListWriter lw = new ListWriter(" + \", \");\n");
    for (ColumnMetadata cm : this.ds.getColumns()) {
      if (cm.getType().isLOB()) {
        lw.add("      sb.append(\"" + cm.getId().getJavaMemberName() + ": \" + (this." + cm.getId().getJavaMemberName()
            + " != null ? \"<LOB set>\" : null)");
      } else {
        lw.add("      sb.append(\"" + cm.getId().getJavaMemberName() + ": \" + this." + cm.getId().getJavaMemberName());
      }
    }
    println(lw.toString() + ");");
    println("      sb.append(\"]\");");
    println("      return sb.toString();");
    println("    }");

    println("  }");
    println();
  }

  public String getVOName() {
    return "PrimitiveVO";
  }

  private void writeGettersAndSetters() throws IOException, UnresolvableDataTypeException {
    println("  // =========================");
    println("  // Adapted Getters & Setters");
    println("  // =========================");
    println();

    for (ColumnMetadata cm : this.ds.getColumns()) {
      PropertyType type = cm.getType();
      String m = cm.getId().getJavaMemberName();

      ConverterTag cvt = cm.getConverter();
      if (cvt != null) {
        println("  public " + cvt.getJavaType() + " " + cm.getId().getJavaGetter() + "() {");
        // println(" return this." + m + ";");
        println("    return this.vo." + cm.getId().getJavaGetter() + "();");
        println("  }");
        println();

        println("  public void " + cm.getId().getJavaSetter() + "(final " + cvt.getJavaType() + " " + m + ") {");
        // println(" this." + m + " = " + m + ";");
        println("    this.vo." + cm.getId().getJavaSetter() + "(" + m + ");");
        println("  }");
      } else {
        println("  public " + type.getJavaClassName() + " " + cm.getId().getJavaGetter() + "() {");
        // println(" return this." + m + ";");
        println("    return this.vo." + cm.getId().getJavaGetter() + "();");
        println("  }");
        println();

        println("  public void " + cm.getId().getJavaSetter() + "(final " + type.getJavaClassName() + " " + m + ") {");
        // println(" this." + m + " = " + m + ";");
        println("    this.vo." + cm.getId().getJavaSetter() + "(" + m + ");");
        println("  }");
      }
      println();

    }

  }

  private void writeJDBCTemplate() throws IOException, UnresolvableDataTypeException {
    println("  // ======================");
    println("  // Spring JDBC - Template");
    println("  // ======================");
    println();
    println(
        "  public static class " + this.ds.getId().getJavaClassName() + CodeGenerationHelper.SPRING_BEAN_SUFFIX + " {");
    println("    private JdbcTemplate jdbcTemplateObject;");
    println("    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;");
    println();
    println("    // Data Source Injection");
    println("    public void setDataSource(DataSource dataSource) {");
    println("      this.jdbcTemplateObject = new JdbcTemplate(dataSource);");
    println("      this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplateObject);");
    println("    }");
    println();
    println();

    if (this.isTable()) {
      writeSelectByPK();
      writeSelectByExample();
      writeSelectByCriteria();
      writeSelectBySQL();

      writeInsert();

      writeUpdateByPK();
      writeUpdateByExample();

      writeDeleteByPK();
      writeDeleteByExample();

      writeUtilities();

    } else if (this.isSelect()) {
      writeCustomSelect();

    }
    println("  }");
    println();

  }

  private void writeCustomSelect() throws IOException {
    SelectDataSetMetadata sm = (SelectDataSetMetadata) this.ds;
    println();
    // TODO improve: duplicated code
    println("    public List<" + this.dao.getClassName() + "> selectCustom(Map<String,Object> params, "
        + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
    String sentence = sm.getSelectTag().renderSQLSentence(new SpringParameterRenderer()).replaceAll("\n",
        "\" +\n      \"");
    println("      String sql = \"" + sentence + "\"\n      + OrderByRenderer.render(orderBies);");
    println();
    println("      return this.namedParameterJdbcTemplate.query(sql, params, new " + this.ds.getId().getJavaClassName()
        + "Mapper());");
    println("    }");
    println();

  }

  private void writeDeleteByExample() throws IOException {
    println("    public int deleteByExample(" + this.dao.getClassName() + " example) {");
    println("      SQLRenderer where = new SQLRenderer(SQLSegmentType.WHERE);");
    println("      appendDynamicColumns(example, where, true);");
    println("      String sql = \"delete from " + this.ds.getId().getRenderedSQLName() + "\" + where.render();");
    println(
        "      return jdbcTemplateObject.update(sql, SQLRenderer.getParamValues(where), SQLRenderer.getParamTypes(where));");
    println("    }");
    println();
  }

  private void writeDeleteByPK() throws IOException {
    boolean hasPK = (this.ds.getPK() != null);

    if (hasPK) {
      println("    private int delete(" + this.dao.getClassName() + " dao) {");
      println("      if (dao == null) {");
      println("      // Nothing to delete.");
      println("        return 0;");
      println("      }");
      print("      String sql = \"delete from " + this.ds.getId().getRenderedSQLName() + " where ");
      ListWriter lw = new ListWriter(" and ");
      for (ColumnMetadata cm : this.ds.getPK().getColumns()) {
        lw.add(cm.getColumnName() + " = ?");
      }
      println(lw.toString() + "\";");

      lw = new ListWriter(", ");

      print("      return jdbcTemplateObject.update(sql,");
      lw = new ListWriter(", ");
      print(" new Object[] { ");
      for (ColumnMetadata cm : this.ds.getPK().getColumns()) {
        lw.add("dao." + cm.getId().getJavaGetter() + "()");
      }
      print(lw.toString() + " },");
      lw = new ListWriter(", ");
      print(" new int[] { ");
      for (ColumnMetadata cm : this.ds.getPK().getColumns()) {
        lw.add(cm.getType().getJDBCType());
      }
      println(lw.toString() + " });");
      println();

      println("    }");
    } else {
      println("      // delete by PK: no mapping generated, since the table does not have a PK");
    }
    println();
  }

  private void writeUtilities() throws IOException {

    println();
    println("    // Utilities: Append dynamic column segments");
    println();
    println("    private void appendDynamicPKColumns(" + this.dao.getClassName() + " example, SQLRenderer r) {");
    if (this.ds.getPK() != null) {
      for (ColumnMetadata cm : this.ds.getPK().getColumns()) {
        println("      r.add(example." + cm.getId().getJavaGetter() + "(), \"" + cm.getColumnName() + "\", "
            + cm.getType().getJDBCType() + ");");
      }
    }
    println("    }");
    println();

    println("    private void appendDynamicNonPKColumns(" + this.dao.getClassName()
        + " example, SQLRenderer r, boolean includeLOB) {");
    if (!this.ds.getNonPkColumns().isEmpty()) {
      for (ColumnMetadata cm : this.ds.getNonPkColumns()) {
        ConverterTag cvt = cm.getConverter();
        if (cm.getType().isLOB()) {
          println("      if (includeLOB) {");

          if (cvt != null) {
            println("        r.add(new " + cvt.getJavaClass() + "().decode(example." + cm.getId().getJavaGetter()
                + "()), \"" + cm.getColumnName() + "\", " + cm.getType().getJDBCType() + ");");
          } else {
            println("        r.add(example." + cm.getId().getJavaGetter() + "(), \"" + cm.getColumnName() + "\", "
                + cm.getType().getJDBCType() + ");");
          }

          println("      }");
        } else {
          if (cvt != null) {
            println("      r.add(new " + cvt.getJavaClass() + "().encode(example." + cm.getId().getJavaGetter()
                + "()), \"" + cm.getColumnName() + "\", " + cm.getType().getJDBCType() + ");");
          } else {
            println("      r.add(example." + cm.getId().getJavaGetter() + "(), \"" + cm.getColumnName() + "\", "
                + cm.getType().getJDBCType() + ");");
          }

        }
      }
    }
    println("    }");
    println();

    println("    private void appendDynamicColumns(" + this.dao.getClassName()
        + " example, SQLRenderer r, boolean includeLOB) {");
    println("    appendDynamicPKColumns(example, r);");
    println("    appendDynamicNonPKColumns(example, r, includeLOB);");
    println("    }");
    println();
  }

  // FIXME implement 'version control'
  private void writeUpdateByExample() throws IOException, UnresolvableDataTypeException {
    println("    public int updateByExample(" + this.dao.getClassName() + " example, " + this.dao.getClassName()
        + " updateValues) {");
    println("      SQLRenderer set = new SQLRenderer(SQLSegmentType.SET);");
    println("      appendDynamicNonPKColumns(updateValues, set, true);");
    println("      if (set.getItemCount() == 0) {");
    println("      // No columns to update. Nothing to do.");
    println("        return 0;");
    println("      }");
    println("      SQLRenderer where = new SQLRenderer(SQLSegmentType.WHERE);");
    println("      appendDynamicColumns(example, where, true);");

    println(
        "      String sql = \"update " + this.ds.getId().getRenderedSQLName() + "\" + set.render() + where.render();");
    println("      return jdbcTemplateObject.update(sql, SQLRenderer.getParamValues(set, where),");
    println("          SQLRenderer.getParamTypes(set, where));");
    println("    }");
    println();
  }

  // FIXME implement 'version control'
  private void writeUpdateByPK() throws IOException, UnresolvableDataTypeException {
    boolean hasPK = (this.ds.getPK() != null);

    // Select
    if (hasPK) {
      if (this.ds.getNonPkColumns().isEmpty()) {
        println("  // update by PK: no update mapping generated, since there aren't any columns besides the PK");
        println();
      }
      println("    public int update(" + this.dao.getClassName() + " dao" + (hasLobs ? ", boolean excludeLOB" : "")
          + ") {");
      println("      if (dao == null) {");
      println("        // Null parameter. Nothing to update");
      println("        return 0;");
      println("      }");
      println("       // -- update by PK --");
      println();
      println("      String sql;");

      ListWriter lw = new ListWriter(" and ");
      for (ColumnMetadata cm : this.ds.getPK().getColumns()) {
        lw.add(cm.getColumnName() + " = ?");
      }

      if (hasLobs) {
        println("      if(!excludeLOB) {");
        print("        sql = \"update " + this.ds.getId().getRenderedSQLName());
        print(" set " + getUpdateDbFields(true));

        print(" where " + lw.toString());
        println("\";");
        println("        return jdbcTemplateObject.update(sql,");
        print("          new Object[] { ");
        print(getUpdateGetterList(true));
        println(" },");

        print("          new int[] { ");
        print(getUpdateDbTypes(true));
        println(" });");

        println("      } else {");

        print("        sql = \"update " + this.ds.getId().getRenderedSQLName());
        print(" set " + getUpdateDbFields(false));

        print(" where " + lw.toString());
        println("\";");
        println("        return jdbcTemplateObject.update(sql,");
        print("          new Object[] { ");
        print(getUpdateGetterList(false));
        println(" },");

        print("          new int[] { ");
        print(getUpdateDbTypes(false));
        println(" });");
        println("      }");

      } else {
        print("        sql = \"update " + this.ds.getId().getRenderedSQLName());
        print(" set " + getUpdateDbFields(true));

        print(" where " + lw.toString());
        println("\";");
        println("        return jdbcTemplateObject.update(sql,");
        print("          new Object[] { ");
        print(getUpdateGetterList(true));
        println(" },");

        print("          new int[] { ");
        print(getUpdateDbTypes(true));
        println(" });");
      }
      println();

      println("    }");
    } else {
      println("      // update by PK: no mapping generated, since the table does not have a PK");
    }
    println();
  }

  private String getUpdateDbFields(boolean includeLobs) {
    ListWriter lw = new ListWriter(",");
    for (ColumnMetadata cm : this.ds.getNonPkColumns()) {
      if (!cm.getType().isLOB() || includeLobs) {
        lw.add(cm.getColumnName() + " = ?");
      }
    }
    return lw.toString();
  }

  private String getUpdateGetterList(boolean includeLobs) {
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : this.ds.getNonPkColumns()) {
      if (!cm.getType().isLOB() || includeLobs) {
        ConverterTag cvt = cm.getConverter();
        if (cvt != null) {
          lw.add("new " + cvt.getJavaClass() + "().encode(dao." + cm.getId().getJavaGetter() + "())");
        } else {
          lw.add("dao." + cm.getId().getJavaGetter() + "()");
        }
      }
    }
    for (ColumnMetadata cm : this.ds.getPK().getColumns()) {
      lw.add("dao." + cm.getId().getJavaGetter() + "()");
    }
    return lw.toString();
  }

  private String getUpdateDbTypes(boolean includeLobs) {
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : this.ds.getNonPkColumns()) {
      if (!cm.getType().isLOB() || includeLobs) {
        lw.add(cm.getType().getJDBCType());
      }
    }
    for (ColumnMetadata cm : this.ds.getPK().getColumns()) {
      lw.add(cm.getType().getJDBCType());
    }
    return lw.toString();
  }

  // FIXME This method does not fully-consider scenario where non-PK fields
  // are marked as autogenerated in HotRod configuration file. Only
  // single-autogenerated-pk-field is recovered after insert.
  private void writeInsert() throws IOException, UnresolvableDataTypeException {

    KeyMetadata pk = this.ds.getPK();
    boolean hasPK = (pk != null);
    boolean isIdentity = false;
    boolean isSequence = false;

    // insert

    // TODO update code to include autogenerated non-pk-fields
    final int SCENARIO_ALL_FIELDS_PROVIDED = 0;
    final int SCENARIO_PK_AUTOGENERATED = 1;

    int scenario = SCENARIO_ALL_FIELDS_PROVIDED;
    if (hasPK) {
      if (pk.getColumns().size() == 1) {
        isIdentity = (pk.getColumns().get(0).getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT);
        isSequence = !isIdentity && (pk.getColumns().get(0).getSequenceId() != null);
        if (isIdentity || isSequence) {
          // es SERIAL, BIGSERIAL o SMALLSERIAL
          scenario = SCENARIO_PK_AUTOGENERATED;
        }
      }
    }

    String insertParams = "";
    String insertValues = "";

    println("    public int insert(final " + this.dao.getClassName() + " dao) {");
    if (scenario == SCENARIO_PK_AUTOGENERATED) {
      insertParams = CodeGenerationHelper.writeSQLColumns(this.ds, true, false, true, false);
      insertValues = CodeGenerationHelper.writeSQLQuestionMarks(insertParams);
      if (isSequence) {
        insertParams = pk.getColumns().get(0).getColumnName() + "," + insertParams;
        // FIXME non portable line
        insertValues = pk.getColumns().get(0).getSequenceId().getRenderedSQLName() + ".nextval," + insertValues;
      }
      println("      KeyHolder keyHolder = new GeneratedKeyHolder();");
    } else {
      insertParams = CodeGenerationHelper.writeSQLColumns(this.ds, false, false, true, false);
      insertValues = CodeGenerationHelper.writeSQLQuestionMarks(insertParams);
    }

    println("      int rows = jdbcTemplateObject.update(new PreparedStatementCreator() {");
    println("        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {");
    println("          String sql = \"insert into " + this.ds.getId().getRenderedSQLName() + " (" + insertParams
        + ") values (" + insertValues + ")\";");
    if (scenario == SCENARIO_PK_AUTOGENERATED) {
      println("          PreparedStatement pst = con.prepareStatement(sql, new String[] { \""
          + pk.getColumns().get(0).getId().getRenderedSQLName() + "\" });");
      // println(" PreparedStatement pst = con.prepareStatement(sql,
      // Statement.RETURN_GENERATED_KEYS);");
    } else {
      println("          PreparedStatement pst = con.prepareStatement(sql);");
    }
    println("          " + this.ds.getId().getJavaClassName() + "Mapper.setValues(pst, dao);");
    println("          return pst;");
    println("        }");
    if (scenario == SCENARIO_PK_AUTOGENERATED) {
      println("      }, keyHolder);");
      // Tricky: Si el campo Pk es autogenerado, entonces ES �NICO (by Vladi).
      // En el caso de los Oracle sequences esto no es cierto, es una
      // convenci�n.
      println("      dao." + ds.getPK().getColumns().get(0).getId().getJavaSetter() + "(("
          + ds.getPK().getColumns().get(0).getType().getPrimitiveClassJavaType() + ")keyHolder.getKey().intValue());");
    } else {
      println("      });");
    }
    println("      return rows;");
    println("    }");
    println();

  }

  private void writeSelectByPK() throws IOException, UnresolvableDataTypeException {
    // Select
    boolean hasPK = (this.ds.getPK() != null);

    if (hasPK) {
      println("    public " + this.dao.getClassName() + " select("
          + CodeGenerationHelper.toParametersSignature(this.ds.getPK())
          + (hasLobs ? ", boolean excludeLOB) {" : ") {"));
      println("      String sql;");
      if (hasLobs) {
        println("      if (!excludeLOB) {");
      }

      println("        sql = \"select " + CodeGenerationHelper.writeSQLColumns(ds, false, false, true, true) + " from "
          + this.ds.getId().getRenderedSQLName() + " where "
          + CodeGenerationHelper.getSQLWhereConditionByIndex(this.ds.getPK()) + "\";");

      if (hasLobs) {
        println("      } else {");
        println("        sql = \"select " + CodeGenerationHelper.writeSQLColumns(ds, false, false, false, true)
            + " from " + this.ds.getId().getRenderedSQLName() + " where "
            + CodeGenerationHelper.getSQLWhereConditionByIndex(this.ds.getPK()) + "\";");
        println("      }");
      }

      println("      try {");
      println("        return jdbcTemplateObject.queryForObject(sql, new Object[] { "
          + CodeGenerationHelper.toParametersCall(this.ds.getPK(), false) + " }, new int[] { "
          + CodeGenerationHelper.toParametersCallJdbcType(this.ds.getPK(), false) + " },");
      println("            new " + this.ds.getId().getJavaClassName() + "Mapper());");
      println("      } catch (IncorrectResultSizeDataAccessException e) {");
      println("        return null;");
      println("      }");
      println("    }");
    } else {
      println("      // select by PK: no mapping generated, since the table does not have a PK");
    }
    println();

  }

  private void writeSelectByExample() throws IOException, UnresolvableDataTypeException {
    // selectByExample
    println();
    // TODO improve: duplicated code
    if (hasLobs) {
      println("    public List<" + this.dao.getClassName() + "> selectByExample(" + this.dao.getClassName()
          + " example, boolean excludeLOB, " + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
      println("      SQLRenderer where = new SQLRenderer(SQLSegmentType.WHERE);");
      println("      appendDynamicColumns(example, where, true);");
      println("      String sql;");
      println("      if (!excludeLOB) {");
      println("        sql = \"select " + CodeGenerationHelper.writeSQLColumns(ds, false, false, true, true) + " from "
          + this.ds.getId().getRenderedSQLName() + " \" + where.render() + OrderByRenderer.render(orderBies);");
      println("      } else {");
      println("        sql = \"select " + CodeGenerationHelper.writeSQLColumns(ds, false, false, false, true) + " from "
          + this.ds.getId().getRenderedSQLName() + " \" + where.render() + OrderByRenderer.render(orderBies);");
      println("      }");
    } else {
      println("    public List<" + this.dao.getClassName() + "> selectByExample(" + this.dao.getClassName()
          + " example, " + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
      println("      SQLRenderer where = new SQLRenderer(SQLSegmentType.WHERE);");
      println("      appendDynamicColumns(example, where, true);");
      println(
          "      String sql = \"select " + CodeGenerationHelper.writeSQLColumns(ds, false, false, true, true) + " from "
              + this.ds.getId().getRenderedSQLName() + " \" + where.render() + OrderByRenderer.render(orderBies);");
    }
    println();
    println(
        "      return jdbcTemplateObject.query(sql, SQLRenderer.getParamValues(where), SQLRenderer.getParamTypes(where),");
    println("          new " + this.ds.getId().getJavaClassName() + "Mapper());");
    println("    }");
    println();

  }

  private void writeSelectByCriteria() throws IOException, UnresolvableDataTypeException {
    // selectByCriteria
    println();
    // TODO improve: duplicated code
    if (hasLobs) {
      println("    public List<" + this.dao.getClassName()
          + "> selectByCriteria(SQLLogicalExpression criteria, boolean excludeLOB, "
          + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
      println("      String sql;");
      println("      if (!excludeLOB) {");
      println("        sql = \"select " + CodeGenerationHelper.writeSQLColumns(ds, false, false, true, true) + " from "
          + this.ds.getId().getRenderedSQLName()
          + " where \" + criteria.render() + OrderByRenderer.render(orderBies);");
      println("      } else {");
      println("        sql = \"select " + CodeGenerationHelper.writeSQLColumns(ds, false, false, false, true) + " from "
          + this.ds.getId().getRenderedSQLName()
          + " where \" + criteria.render() + OrderByRenderer.render(orderBies);");
      println("      }");
    } else {
      println("    public List<" + this.dao.getClassName() + "> selectByCriteria(SQLLogicalExpression criteria, "
          + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
      println("      String sql = \"select " + CodeGenerationHelper.writeSQLColumns(ds, false, false, true, true)
          + " from " + this.ds.getId().getRenderedSQLName()
          + " where \" + criteria.render() + OrderByRenderer.render(orderBies);");
    }
    println();
    println("      return this.namedParameterJdbcTemplate.query(sql, criteria.getParameters(), new "
        + this.ds.getId().getJavaClassName() + "Mapper());");
    println("    }");
    println();

  }

  private void writeSelectBySQL() throws IOException, UnresolvableDataTypeException {
    println(
        "    public List<" + this.dao.getClassName() + "> selectBySQL(List<SQLJoin> joins, SQLLogicalExpression where, "
            + this.ds.getId().getJavaClassName() + "OrderBy... orderBies) {");
    println("      StringBuffer sql = new StringBuffer();");
    println("      Map<String,Object> paramMap = new HashMap<String, Object>();");
    // FIXME always excludes LOBs
    println("      sql.append( \"select distinct " + CodeGenerationHelper.writeSQLColumns(ds, false, false, false, true)
        + " from " + this.ds.getId().getRenderedSQLName() + "\");");

    println("      if (joins != null && joins.size() > 0) {");
    println("        for (SQLJoin j : joins) {");
    println("          sql.append(j.render());");
    println("          if (j.getParameters()!=null)");
    println("            paramMap.putAll(j.getParameters());");
    println("        }");
    println("      }");
    println("      if (where != null) {");
    println("        sql.append(\" where \" + where.render());");
    println("        if (where.getParameters() != null)");
    println("           paramMap.putAll(where.getParameters());");
    println("      }");
    println("      sql.append(OrderByRenderer.render(orderBies));");
    println("      return this.namedParameterJdbcTemplate.query(sql.toString(), paramMap, new "
        + this.ds.getId().getJavaClassName() + "Mapper());");
    println("    }");
    println();
  }

  private void writeClassHeader() throws IOException {

    // Comment

    println("// Autogenerated by EmpusaMB. Do not edit.");
    println();

    // Package

    println("package " + this.dsg.getDAOPrimitivePackage(this.fragmentPackage).getPackage() + ";");
    println();

    // Imports

    println("import java.io.Serializable;");
    if (this.isTable())
      println("import java.sql.Connection;");
    println("import java.sql.PreparedStatement;");
    println("import java.sql.ResultSet;");
    println("import java.sql.SQLException;");
    println("import java.sql.Statement;");
    println("import java.util.ArrayList;");
    println("import java.util.HashMap;");
    println("import java.util.List;");
    println("import java.util.Map;");
    println();
    println("import javax.sql.DataSource;");
    println();
    println("import org.hotrod.runtime.interfaces.OrderBy;");
    println("import org.hotrod.runtime.interfaces.OrderByRenderer;");
    if (this.isTable()) {
      println("import org.hotrod.runtime.interfaces.SQLRenderer;");
      println("import org.hotrod.runtime.interfaces.SQLRenderer.SQLSegmentType;");
    }
    println("import org.hotrod.runtime.spring.ApplicationContextProvider;");
    if (this.isTable()) {
      println("import org.hotrod.runtime.util.SQLField;");
      println("import org.hotrod.runtime.util.SQLJoin;");
      println("import org.hotrod.runtime.util.SQLLogicalExpression;");
      println("import org.hotrod.runtime.util.SQLTable;");
      println("import org.springframework.dao.IncorrectResultSizeDataAccessException;");
    }
    println("import org.springframework.jdbc.core.JdbcTemplate;");
    println("import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;");
    if (this.isTable())
      println("import org.springframework.jdbc.core.PreparedStatementCreator;");
    println("import org.springframework.jdbc.core.RowMapper;");
    if (this.isTable()) {
      println("import org.springframework.jdbc.support.GeneratedKeyHolder;");
      println("import org.springframework.jdbc.support.KeyHolder;");
    }

    // FIXME check this for Spring
    if (this.ds.getVersionControlMetadata() != null) {
      println("import org.nocrala.tools.persistence.empusamb.interfaces.DaoForUpdate;");
      println("import org.nocrala.tools.database.empusamb.exceptions.StaleDataException;");
    }

    println();

    Set<String> importedDaos = new HashSet<String>();

    importedDaos.add(this.dao.getFullClassName());
    println("import " + this.dao.getFullClassName() + ";");
    println();

    // Signature

    println("public class " + this.getClassName() + " implements Serializable {");
    println();

    // Serial Version UID

    println("  private static final long serialVersionUID = 1L;");
    println();

  }

  private void writeProperties() throws IOException, UnresolvableDataTypeException {
    println("  private " + this.getVOName() + " vo = new " + this.getVOName() + "();");
    println();
  }

  // FIXME clone char arrays content instead of copy reference to it.
  private void writeVOCloneMethods() throws IOException, UnresolvableDataTypeException {
    println("  protected " + this.getVOName() + " copyTo(" + this.getVOName() + " target) {");
    for (ColumnMetadata cm : this.ds.getColumns()) {
      println("    target." + cm.getId().getJavaSetter() + "(this." + cm.getId().getJavaGetter() + "());");
    }
    println();

    println("    return target;");
    println("  }");
    println();
  }

  ////////////////////////

  public static class TableKey {

    private JdbcKey key;

    public TableKey(final JdbcKey key) {
      this.key = key;
    }

    public JdbcKey getKey() {
      return key;
    }

    @Override
    public int hashCode() {
      return 1;
    }

    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;
      if (other == null)
        return false;
      if (getClass() != other.getClass())
        return false;
      TableKey o = (TableKey) other;
      if (key == null) {
        if (o.key != null)
          return false;
      } else if (o.key == null)
        return false;
      else {
        if (this.key.getKeyColumns().size() != o.key.getKeyColumns().size()) {
          return false;
        }
        for (int i = 0; i < this.key.getKeyColumns().size(); i++) {
          JdbcKeyColumn tc = this.key.getKeyColumns().get(i);
          JdbcKeyColumn oc = o.key.getKeyColumns().get(i);
          if (tc.getColumnSequence() != oc.getColumnSequence()) {
            return false;
          }
          if (!tc.getColumn().getName().equals(oc.getColumn().getName())) {
            return false;
          }
        }
      }
      return true;
    }

  }

  private void writeClassFooter() throws IOException {
    println("}");
  }

  private void writeJavaToDatabaseFieldMapper() throws IOException {
    println("  public static final String DBTABLE$NAME = \"" + ds.getId().getRenderedSQLName() + "\";");
    println("  public static final SQLTable DB$TABLE = new SQLTable(\"" + ds.getId().getRenderedSQLName() + "\");");
    println();
    for (ColumnMetadata cm : this.ds.getColumns()) {
      println("  public static final SQLField DBFIELD$" + cm.getColumnName() + " = new SQLField(DB$TABLE,\""
          + cm.getColumnName() + "\");");
    }
    println();

    println("  private static Map<String, Integer> dbFieldSqlTypeMapper = new HashMap<String, Integer>("
        + this.ds.getColumns().size() + ");");
    println("  static {");

    for (ColumnMetadata cm : this.ds.getColumns()) {
      println("    dbFieldSqlTypeMapper.put(DBFIELD$" + cm.getColumnName() + ".toString(), "
          + cm.getType().getJDBCType() + ");");
    }
    println("  }");
    println();
  }

  // Identifiers

  public String getFullClassName() {
    return this.dsg.getDAOPrimitivePackage(fragmentPackage).getFullClassName(getClassName());
  }

  public String getOrderByFullClassName() {
    return getFullClassName() + "." + getOrderByClassName();
  }

  private String getOrderByClassName() {
    return this.ds.getId().getJavaClassName() + "OrderBy";
  }

  public String getClassName() {
    return this.ds.generatePrimitivesName(this.ds.getId());
  }

  public String getParameterClassName() {
    return this.getClassName() + "Parameter";
  }

  public String getSelectByUI(final KeyMetadata ui) {
    return "selectByUI" + ui.toCamelCase(this.dsg.getColumnSeam());
  }

  public String getSelectByColumns(final KeyMetadata ui) {
    return "by" + ui.toCamelCase(this.dsg.getColumnSeam());
  }

  // Helpers

  public static String toParametersSignature(final KeyMetadata km) throws UnresolvableDataTypeException {
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : km.getColumns()) {
      lw.add("final " + cm.getType().getJavaClassName() + " " + cm.getId().getJavaMemberName());
    }
    return lw.toString();
  }

  public static String toParametersCall(final KeyMetadata km) {
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : km.getColumns()) {
      lw.add(cm.getId().getJavaMemberName());
    }
    return lw.toString();
  }

  private void print(final String txt) throws IOException {
    this.w.write(txt);
  }

  private void println(final String txt) throws IOException {
    this.w.write(txt);
    println();
  }

  private void println() throws IOException {
    this.w.write("\n");
  }

}