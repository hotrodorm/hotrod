package org.hotrod.generator.jdbc;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.Constants;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.EnhancedSQLPart;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.OptimisticLockingTag.OptimisticLockingStrategy;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.PreparedModificationQuery;
import org.hotrod.dynamicsql.PreparedQuery;
import org.hotrod.dynamicsql.PreparedSelectQuery;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.hotrod.dynamicsql.insert.PreparedInsertQuery;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.PersistenceException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.StaleDataException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.identifiers.Id;
import org.hotrod.interfaces.OrderBy;
import org.hotrod.livesql.LShield;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.expressions.bool.converter.ConvertedColumn;
import org.hotrod.livesql.metadata.AllColumns;
import org.hotrod.livesql.metadata.BinaryEntityColumn;
import org.hotrod.livesql.metadata.BooleanEntityColumn;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.NumericEntityColumn;
import org.hotrod.livesql.metadata.ObjectEntityColumn;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.metadata.View;
import org.hotrod.livesql.queries.DeleteWherePhase;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.UpdateSetCompletePhase.Setter;
import org.hotrod.livesql.queries.UpdateWherePhase;
import org.hotrod.livesql.queries.select.CriteriaWherePhase;
import org.hotrod.livesql.queries.typesolver.RuntimeTypeSolver;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.livesql.util.CastUtil;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.metadata.OptimisticLockingMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodReturnType;
import org.hotrod.metadata.SelectParameterMetadata;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.GenUtils;
import org.hotrod.utils.JUtils;
import org.hotrod.utils.SQLUtil;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.Separator;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;

public class DAOWriter {

  private static final Logger log = Logger.getLogger(DAOWriter.class.getName());

  // Properties

  private AbstractDAOTag tag;

  private DataSetMetadata metadata;
  private JDBCGenerator generator;
  private DAOType daoType;
  private JDBCTag jdbcTag;
  private DatabaseAdapter adapter;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  private LayoutWriter layout;
  private ModelWriter model;
  private LayerResourcesBeanWriter layerResources;

  private String metadataClassName;

  private ClassWriter w;

  private List<String> initializersInPostConstruct = new ArrayList<>();

  // Constructors

  public DAOWriter(final AbstractDAOTag tag, final DataSetMetadata metadata, final JDBCGenerator generator,
      final DAOType type, final JDBCTag myBatisTag, final DatabaseAdapter adapter, final LayoutWriter layout,
      final ModelWriter model, final LayerResourcesBeanWriter layerResources) {
    super();
    this.tag = tag;
    this.metadata = metadata;
    this.generator = generator;
    if (type == null) {
      throw new RuntimeException("DAOType cannot be null.");
    }
    this.daoType = type;
    this.jdbcTag = myBatisTag;
    this.adapter = adapter;

    this.layout = layout;
    this.model = model;
    this.layerResources = layerResources;

    this.fragmentConfig = metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    // <package-base>/<fragment-package>/<dao>

    this.classPackage = this.jdbcTag.getDAOPackage(this.fragmentPackage);
    this.metadataClassName = this.metadata.getId().getJavaClassName() + (this.isTable() ? "Table" : "View");

  }

  public void generate(final FileGenerator fileGenerator, final JDBCGenerator mg)
      throws FaultException, ErrorMessageException {

    String className = this.getClassName() + ".java";

    File dir = this.jdbcTag.getDAOPackageDir(this.fragmentPackage);
    File f = new File(dir, className);
    log.fine("f=" + f);

    try (TextWriter tw = fileGenerator.createWriter(f)) {

      this.w = new ClassWriter(this.classPackage, "// Autogenerated by " + Constants.TOOL_NAME + " -- Do not edit.",
          "");
      writeBody(mg);
      this.w.writeTo(tw);

    } catch (IOException e) {
      throw new FaultException(this.tag, "Could not generate DAO: could not write to file '" + f.getName() + "'.", e);
    }

  }

  private void writeBody(final JDBCGenerator g) throws IOException, ErrorMessageException {

    writeClassHeader();

    writeConverterBeans();

    if (!this.isExecutor()) {

      writeRowReaderProperty();

      writeParseRow();

      writeBaseline();

      writeClone();

      if (this.isTable()) {
        writeSelect(false); // by PK
//        writeSelectByUI(mg);
      }
//
      writeSelect(true); // by example
      writeSelectByCriteria();

      writeInsert(false); // INSERT standard
      writeInsert(true); // INSERT by example

      writeUpdateByPK(this.metadata.getOptimisticLocking() != null);

      writeUpdateByExample();
      if (this.isTable() || this.isView()) {
        writeUpdateByCriteria();
      }

      writeDeleteByPK(this.metadata.getOptimisticLocking() != null);

      writeDeleteByExample();
      if (this.isTable() || this.isView()) {
        writeDeleteByCriteria();
      }

//      writeEnumTypeHandlers();

      writeOrderBy();

      writeMetadata();

    }

    if (!this.tag.getSequences().isEmpty()) {
      writeSequenceRowReader();
    }
    int n = 0;
    for (SequenceMethodTag s : this.tag.getSequences()) {
      log.fine("s.getName()=" + s.getSequenceId().getRenderedSQLName());
      writeSelectSequence(s, n);
      n++;
    }

    int i = 0;
    for (QueryMethodTag q : this.tag.getQueries()) {
      writeNitroQuery(q, i++);
    }

    i = 0;
    for (SelectMethodMetadata s : this.metadata.getSelectsMetadata()) {
      if (this.isExecutor()) {
        this.writeNitroFreeSelect(s, i);
      } else {
        this.writeNitroEntitySelect(s, i);
      }
      i++;
    }

    writeGetters();

    writeInternalMethods();

    writeClassFooter();
  }

  private void writeClassHeader() throws IOException {

    w.println("@", Const.COMPONENT);
    w.println("public class " + this.getClassName() + " implements ", Serializable.class, ", ",
        Const.APPLICATION_CONTEXT_AWARE, " {");
    w.println();

    // Serial Version UID

    w.println("  private static final long serialVersionUID = 1L;");
    w.println();

    // Loggers

    w.println("  private static final ", Logger.class, " log = ", Logger.class,
        ".getLogger(" + this.getClassName() + ".class.getName());");
    w.println();

    if (!this.isExecutor()) {
      w.println("  private static final ", LiveSQLLogging.class, " livesql_log = ", LiveSQLLogging.class, ".of(");
      w.println("      () -> log.isLoggable(", Level.class, ".FINE), msg -> log.fine(msg),");
      w.println("      () -> log.isLoggable(", Level.class, ".FINER), msg -> log.finer(msg)");
      w.println("    );");
      w.println();
    }

    // Spring properties

    w.println("  @", Const.AUTOWIRED);
    if (this.layerResources.getDataSourceQualifier() != null) {
      w.println("  @", Const.QUALIFIER, "(\"" + this.layerResources.getDataSourceQualifier() + "\")");
    }
    w.println("  private ", DataSource.class, " dataSource;");
    w.println();

    if (!this.isExecutor()) {
      w.println("  @", Const.AUTOWIRED);
      if (this.layerResources.getLiveSQLQualifier() != null) {
        w.println("  @", Const.QUALIFIER, "(\"" + this.layerResources.getLiveSQLQualifier() + "\")");
      }
      w.println("  private ", LiveSQL.class, " sql;");
      w.println();
    }

    w.println("  private ", DynamicSQL.class, " dyn;");
    w.println();

    w.println("  private ", Const.APPLICATION_CONTEXT, " applicationContext;");
    w.println();

    w.println("  @", Override.class);
    w.println("  public void setApplicationContext(final ", Const.APPLICATION_CONTEXT, " applicationContext) throws ",
        Const.BEANS_EXCEPTION, " {");
    w.println("    this.applicationContext = applicationContext;");
    w.println("  }");

    if (!this.isExecutor()) {
      w.println();
//      w.println("  @", SuppressWarnings.class, "(\"unused\")");
      w.println("  private ", LiveSQLContext.class, " context;");
    }

  }

  private void writeGetters() throws IOException {
    w.println();
    w.println("  // GETTERS");
    w.println();
    w.println("  public ", DataSource.class, " getDataSource() {");
    w.println("    return this.dataSource;");
    w.println("  }");
  }

  private void writeInternalMethods() {
    w.println();
    w.println("  // INTERNAL METHODS");
    w.println();
    w.println("  @", Const.POST_CONSTRUCT);
    w.println("  private void initializeContext() {");
    if (!this.isExecutor()) {
      w.println("    ", LiveSQLDialect.class, " liveSQLDialect = ", LShield.class, ".getLiveSQLDialect(this.sql);");
      w.println("    this.context = new ", LiveSQLContext.class, "(liveSQLDialect, this.dataSource, new ",
          RuntimeTypeSolver.class, "(null, liveSQLDialect));");
    }
    w.println("    this.dyn = new ", DynamicSQL.class, "();");
    for (String ini : this.initializersInPostConstruct) {
      w.println("    this." + ini + "();");
    }
    w.println("  }");
    w.println();
    w.println("  private void logQuery(", PreparedQuery.class, " preparedQuery) {");
    w.println("    if (log.isLoggable(", Level.class, ".FINER)) {");
    w.println("      log.finest(\"SQL:\\n\" + preparedQuery.getPreview(true));");
    w.println("    } else if (log.isLoggable(", Level.class, ".FINE)) {");
    w.println("      log.fine(\"SQL:\\n\" + preparedQuery.getPreview());");
    w.println("    }");
    w.println("  }");
  }

  private void writeClassFooter() throws IOException {
    w.println();
    w.println("}");
  }

  private void writeRowReaderProperty() {
    ExternalClass m = ExternalClass.of(this.model.getFullClassName());

    w.println();
    w.println("  // ROW READER");
    w.println();
    w.print("  private final ", RowReader.class, "<", m, "> rowReader = new ");
    w.println(RowReader.class, "<", m, ">() {");
    w.println();
    w.println("    @", Override.class);
    w.print("    public ", m, " readRowFrom(", ResultSet.class, " rs, ");
    w.print(Connection.class, " conn) ");
    w.println("throws ", SQLException.class, " {");
    w.println("      ", m, " row = applicationContext.getBean(", m, ".class);");

    int ordinal = 1;
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      this.writeColumnReaderLogic(cm, ordinal, false);
      ordinal++;
    }

    w.println();
    w.println("      return row;");
    w.println("    }");
    w.println();
    w.println("  };");
  }

  private void writeParseRow() {
    ExternalClass m = ExternalClass.of(this.model.getFullClassName());

    boolean hasConverters = false;
//    for (ColumnMetadata cm : this.metadata.getColumns()) {
//      if (cm.getConverter() != null) {
//        hasConverters = true;
//      }
//    }

    w.println();
    w.println("  // PARSE ROW");
    w.println();
    if (hasConverters) {
      w.print("  public ", m, " parseRow(", Map.class, "<String, Object> row, ");
      w.println(Connection.class, " conn) {");
      w.println("    return parseRow(row, null, null, conn);");
    } else {
      w.println("  public ", m, " parseRow(", Map.class, "<String, Object> row) {");
      w.println("    return parseRow(row, null, null);");
    }
    w.println("  }");
    w.println();

    if (hasConverters) {
      w.print("  public ", m, " parseRow(", Map.class, "<String, Object> row, String prefix, ");
      w.println(Connection.class, " conn) {");
      w.println("    return parseRow(row, prefix, null, conn);");
    } else {
      w.println("  public ", m, " parseRow(", Map.class, "<String, Object> row, String prefix) {");
      w.println("    return parseRow(row, prefix, null);");
    }

    w.println("  }");
    w.println();

    if (hasConverters) {
      w.print("  public ", m, " parseRow(", Map.class, "<String, Object> row, String prefix, String suffix, ");
      w.println(Connection.class, " conn) {");
    } else {
      w.println("  public ", m, " parseRow(", Map.class, "<String, Object> row, String prefix, String suffix) {");
    }
    w.println("    ", m, " m = applicationContext.getBean(", m, ".class);");
    w.println("    String p = prefix == null ? \"\": prefix;");
    w.println("    String s = suffix == null ? \"\": suffix;");

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      String memberProperty = cm.getId().getJavaMemberName();

//      if (cm.getConverter() != null) {
//        ConverterTag ct = cm.getConverter();
//        String property = this.converterProperties.get(ct.getName());
//
//        ExternalClass rt = ExternalClass.of(ct.getRawClass());
//
//        w.println("    m." + cm.getId().getJavaSetter() + "(this." + property + ".decode((", rt,
//            ") row.get(p + \"" + JUtils.escapeJavaString(memberProperty) + "\" + s), conn));");
//
//      } else

      if ("java.lang.Byte".equals(javaType) || //
          "java.lang.Short".equals(javaType) || //
          "java.lang.Integer".equals(javaType) || //
          "java.lang.Long".equals(javaType) || //
          "java.lang.Float".equals(javaType) || //
          "java.lang.Double".equals(javaType) || //
          "java.math.BigInteger".equals(javaType) || //
          "java.math.BigDecimal".equals(javaType)) {
        int idx = javaType.lastIndexOf(".");
        String st = idx == -1 ? javaType : javaType.substring(idx + 1);

        w.println("    m." + cm.getId().getJavaSetter() + "(", CastUtil.class, ".to" + st + "((", Number.class,
            ") row.get(p + \"" + JUtils.escapeJavaString(memberProperty) + "\" + s)));");
      } else if ("java.lang.Object".equals(javaType)) {
        w.println("    m." + cm.getId().getJavaSetter() + "(row.get(p + \"" + JUtils.escapeJavaString(memberProperty)
            + "\" + s));");
      } else {
        ExternalClass jt = ExternalClass.of(javaType);
        w.println("    m." + cm.getId().getJavaSetter() + "((", jt,
            ") row.get(p + \"" + JUtils.escapeJavaString(memberProperty) + "\" + s));");
      }

    }

    w.println("    return m;");
    w.println("  }");
  }

  public ExternalClass getBaselineClass() {
    return ExternalClass.of(this.model.getClassName() + "Baseline");
  }

  public ExternalClass getBaselineFullClass() {
    return ExternalClass.of(this.getFullClassName() + "." + this.model.getClassName() + "Baseline");
  }

  private void writeBaseline() {
    ExternalClass m = ExternalClass.of(this.model.getFullClassName());
    ExternalClass b = this.getBaselineClass();

    w.println();
    w.println("  // BASELINE");

    w.println();
    w.println("  public class ", b, " {");
    w.println();
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      String mem = cm.getId().getJavaMemberName();
      w.println("    private ", ExternalClass.of(javaType), " " + mem + ";");
    }
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      String mem = cm.getId().getJavaMemberName();
      w.println();
      w.println("    public ", ExternalClass.of(javaType), " " + cm.getId().getJavaGetter() + "() {");
      w.println("      return this." + mem + ";");
      w.println("    }");
    }
    w.println();
    w.println("  }");

    w.println();
    w.println("  public ", b, " baseline(", m, " model) {");
    w.println("    ", b, " b = new ", b, "();");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String mem = cm.getId().getJavaMemberName();
      w.println("    b." + mem + " = model." + cm.getId().getJavaGetter() + "();");
    }
    w.println("    return b;");
    w.println("  };");
  }

  private void writeClone() {
    ExternalClass m = ExternalClass.of(this.model.getFullClassName());
    ExternalClass l = ExternalClass.of(this.layout.getFullClassName());

    w.println();
    w.println("  // CLONE");
    w.println();
    w.println("  public ", m, " clone(", l, " layout) {");
    w.println("    ", m, " m = this.applicationContext.getBean(", m, ".class);");

    for (ColumnMetadata cm : this.metadata.getColumns()) {
//      String mem = cm.getId().getJavaMemberName();
      w.println("    m." + cm.getId().getJavaSetter() + "(layout." + cm.getId().getJavaGetter() + "());");
    }
    w.println("    return m;");
    w.println("  };");
  }

  private void writeSelect(boolean byExample) {

    KeyMetadata pk = this.metadata.getPK();

    if (!byExample && pk == null) {
      w.println();
      w.println("  // SELECT BY PRIMARY KEY -- Not available since the table does not have a primary key.");
    } else {

      w.println();
      w.println("  // SELECT BY " + (byExample ? "EXAMPLE" : "PRIMARY KEY"));

      // Query

      String queryName = byExample ? "selectByExample" : "selectByPrimaryKey";
      String initializerName = "initialize" + SUtil.capitalize(queryName);
      this.initializersInPostConstruct.add(initializerName);

      w.println();
      w.println("  private ", DynamicSelectQuery.class, " " + queryName + ";");
      w.println();
      w.println("  private void " + initializerName + "() {");
      w.println("    this." + queryName + " = dyn");

      w.println("      .literaln(\"SELECT\")");
      int coln = this.metadata.getColumns().size();
      int n = 1;
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        String sqlId = cm.getId().getRenderedSQLName();
        w.println("      .literaln(\"  " + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\")");
        n++;
      }

      w.println("      .literaln(\"FROM " + SUtil.escapeJavaString(this.metadata.getId().getRenderedSQLName()) + "\")");
      if (byExample) {
        fragmentWhereExample("e");
        w.println("      .parameterInjection(\"ordering\")");
      } else {
        fragmentWherePK("f");
      }
      w.println("      .endSelectQuery();");
      w.println("  }");
      w.println();

      // Method

      ExternalClass em = ExternalClass.of(this.model.getFullClassName());
      ExternalClass el = ExternalClass.of(this.layout.getFullClassName());
      if (byExample) {
        w.print("  public ", List.class, "<", em, "> select(");
        ExternalClass ob = ExternalClass.of(this.getOrderByClassName());
        w.print(el, " example, ", ob, "... orderBies");
      } else {
        w.print("  public ", em, " select(");
        fragmentPKParameters(pk);
      }
      w.println(") {");

      if (!byExample) {
        for (ColumnMetadata cm : pk.getColumns()) {
          String m = cm.getId().getJavaMemberName();
          w.println("    if (" + m + " == null) return null;");
        }
        w.println("    ", em, " filter = new ", em, "();");
        for (ColumnMetadata cm : pk.getColumns()) {
          String m = cm.getId().getJavaMemberName();
          String setter = cm.getId().getJavaSetter();
          w.println("    filter." + setter + "(" + m + ");");
        }
      }

      w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
      if (byExample) {
        w.println("    params.add(\"e\", example);");
        w.println("    String ordering = ", SQLUtil.class, ".render(orderBies);");
        w.println("    params.add(\"ordering\", ordering);");
      } else {
        w.println("    params.add(\"f\", filter);");
      }
      w.print("    ", PreparedSelectQuery.class, "<", em, "> preparedQuery = ");
      w.println("this." + queryName + ".prepare(params, this.rowReader);");

      fragmentLogging();
      fragmentExecuteSelect(em, !byExample);

      w.println("  }");

    }

  }

  private void writeSelectByCriteria() {
    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    ExternalClass ec = ExternalClass.of(this.metadataClassName);
    w.println();
    w.println("  // SELECT BY CRITERIA");
    w.println();
    w.print("  public ", CriteriaWherePhase.class, "<", em, "> ");
    w.println("select(final ", ec, " from, final ", Predicate.class, " predicate) {");
    w.println("    return new ", CriteriaWherePhase.class, "<", em,
        ">(this.context, from, predicate, this.rowReader, livesql_log);");
    w.println("  }");
  }

  private void writeInsert(boolean byExample) throws ErrorMessageException {

    w.println();
    w.println("  // " + (byExample ? "INSERT BY EXAMPLE" : "INSERT"));

    KeyMetadata pk = this.metadata.getPK();

    // Limitations
    // 1. This version supports autogeneration for a single-column PK
    // 2. Does not retrieve DEFAULT columns
    // 3. Retrieved value can only be numeric up to LONG (no NUMBER(19) or UUID)
    // 4. Only retrieves a single value for a single-row INSERT (no multi-inserts)

    List<ColumnMetadata> sequences = new ArrayList<>();
    List<ColumnMetadata> identities = new ArrayList<>();
    List<ColumnMetadata> defaults = new ArrayList<>();
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.belongsToPK() && cm.getSequenceId() != null) {
        sequences.add(cm);
      }
      if (cm.belongsToPK() && cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity()) {
        identities.add(cm);
      }
      if (cm.getColumnDefault() != null) {
        defaults.add(cm);
      }
    }

    OptimisticLockingMetadata ol = this.metadata.getOptimisticLocking();

    InsertMechanics mechanics = computeInsertMechanics(sequences, identities, defaults);

    // DynamicSQL Query

    String queryName = byExample ? "insertByExample" : "insert";
    String initializerName = "initialize" + SUtil.capitalize(queryName);
    this.initializersInPostConstruct.add(initializerName);

    w.println();
    w.println("  private ", DynamicInsertQuery.class, " " + queryName + ";");
    w.println();
    w.println("  private void " + initializerName + "() {");
    w.println("    this." + queryName + " = dyn");

    w.println("      .literal(\"INSERT INTO ", SUtil.escapeJavaString(this.metadata.getId().getRenderedSQLName()),
        "\")");
    w.println("      .trim(\" (\\n  \", \",\\n  \", \"\\n) \")");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();

      if (byExample) {
        if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.TIMESTAMP && cm.isOLTimestampColumn()) {
          w.println("      .literal(\"" + SUtil.escapeJavaString(sqlId) + "\")");
        } else {
          w.println("      .if_(\"e." + SUtil.escapeJavaString(memId) + " != null\").literal(\""
              + SUtil.escapeJavaString(sqlId) + "\").endif()");
        }
      } else {

        // Always include column
        if (!cm.belongsToPK()) {
          w.println("      .literal(\"" + SUtil.escapeJavaString(sqlId) + "\")");
        }
        if (cm.belongsToPK() && cm.getSequenceId() != null) {
          w.println("      .literal(\"" + SUtil.escapeJavaString(sqlId) + "\")");
        }
        if (cm.belongsToPK() && cm.getSequenceId() == null && cm.getAutogenerationType() == null) {
          w.println("      .literal(\"" + SUtil.escapeJavaString(sqlId) + "\")");
        }

        // Never include column
        if (cm.belongsToPK() && cm.getAutogenerationType() == AutogenerationType.IDENTITY_ALWAYS) {
          // nothing to do
        }

        // Conditionally include column
        if (cm.belongsToPK() && cm.getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT) {
          w.println("      .if_(\"l." + SUtil.escapeJavaString(memId) + " != null\").literal(\""
              + SUtil.escapeJavaString(sqlId) + "\").endif()");
        }

      }

    }
    w.println("      .endtrim()");

    if (mechanics.getOutputClause() != null && mechanics.getGeneratedKeysNames() != null
        && mechanics.getGeneratedKeysNames().length > 0) {
      String gkn = mechanics.getGeneratedKeysNames()[0];
      w.println("      .literaln(\"" + mechanics.getOutputClause() + SUtil.escapeJavaString(gkn) + "\")");
    }

    w.println("      .literal(\"VALUES\")");
    w.println("      .trim(\" (\\n  \", \",\\n  \", \"\\n)\")");

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String jdbcType = cm.getType().getJDBCShortType();
      String converterParam = cm.getResolvedConverter() == null ? ""
          : ", this." + this.converterProperties.get(cm.getResolvedConverter().getName());

      if (byExample) { // by example

        if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.TIMESTAMP && cm.isOLTimestampColumn()) {
          w.println("      .literal(\"" + SUtil.escapeJavaString(this.adapter.currentTimestampSQLExpression()) + "\")");
        } else {
          w.println("      .if_(\"e." + SUtil.escapeJavaString(memId) + " != null\").parameter(\"e."
              + SUtil.escapeJavaString(memId) + "\"" + converterParam + ")" + ".endif()");
        }

      } else { // insert

        // Always include column
        if (!cm.belongsToPK()) {
          if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.TIMESTAMP && cm.isOLTimestampColumn()) {
            w.println(
                "      .literal(\"" + SUtil.escapeJavaString(this.adapter.currentTimestampSQLExpression()) + "\")");
          } else {
            w.println("      .parameterNullable(\"l." + SUtil.escapeJavaString(memId) + "\", ", Types.class,
                "." + jdbcType + converterParam + ")");
          }
        }
        if (cm.belongsToPK() && cm.getSequenceId() != null) {
          if (mechanics.getMode() == PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH) {
            w.println("      .parameterUpdatable(\"l." + SUtil.escapeJavaString(memId) + "\"" + converterParam + ")");
          } else {
            String si = mechanics.getSequenceInlineSQL();
            w.println("      .literal(\"" + SUtil.escapeJavaString(si) + "\")");
          }
        }
        if (cm.belongsToPK() && cm.getSequenceId() == null && cm.getAutogenerationType() == null) {
          w.println("      .parameterNullable(\"l." + SUtil.escapeJavaString(memId) + "\", ", Types.class,
              "." + jdbcType + converterParam + ")");
        }

        // Never include column
        if (cm.belongsToPK() && cm.getAutogenerationType() == AutogenerationType.IDENTITY_ALWAYS) {
          // nothing to do
        }

        // Conditionally include column
        if (cm.belongsToPK() && cm.getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT) {
          w.println("      .if_(\"l." + SUtil.escapeJavaString(memId) + " != null\").parameter(\"l."
              + SUtil.escapeJavaString(memId) + "\"" + converterParam + ")" + ".endif()");
        }

      }

    }
    w.println("      .endtrim()");

    // end insert query

    String prefix = byExample ? "e" : "l";
    if (mechanics.getMode() == PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH) {
      w.print("      .endInsertQuery(", PrimaryKeyRetrievalMode.class,
          "." + mechanics.getMode() + ", \"" + SUtil.escapeJavaString(mechanics.getSequencePreFetchSQL()) + "\", \""
              + prefix + "." //
              + SUtil.escapeJavaString(mechanics.getPrimaryKeyMemberName()) //
              + "\"");
    } else {
      w.print("      .endInsertQuery(", PrimaryKeyRetrievalMode.class, "." + mechanics.getMode());
    }

    if (mechanics.getOutputClause() == null && mechanics.getGeneratedKeysNames() != null
        && mechanics.getGeneratedKeysNames().length > 0) {
      w.print(", null, null");
      for (String gkn : mechanics.getGeneratedKeysNames()) {
        w.print(", \"" + SUtil.escapeJavaString(gkn) + "\"");
      }
    }

    w.println(");");
    w.println("  }");

    // Method

    String methodName = byExample ? "insertByExample" : "insert";

    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    ExternalClass el = ExternalClass.of(this.layout.getFullClassName());
    w.println();
    w.print("  public ", em, " " + methodName + "(", el);
    if (byExample) {
      w.print(" example");
    } else {
      w.print(" layout");
    }
    w.println(") {");

    w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
    if (byExample) {
      w.println("    params.add(\"e\", example);");
    } else {
      w.println("    params.add(\"l\", layout);");
    }

    if (ol != null) {
      switch (ol.getStrategy()) {
      case VERSION_NUMBER:
        ColumnMetadata cm = ol.getColumnMetadata();
        String setter = cm.getId().getJavaSetter();
        String zero = renderNumericLiteral(0, cm.getType().getJavaClassName());
        w.println("    layout." + setter + "(" + zero + ");");
        break;
      case TIMESTAMP:
        cm = ol.getColumnMetadata();
        setter = cm.getId().getJavaSetter();
        w.println("    layout." + setter + "(null);");
        break;
      default: // do not add setter
      }
    }

    w.println("    ", PreparedInsertQuery.class, " preparedQuery = this." + queryName + ".prepare(params);");

    fragmentLogging();

    if (byExample) {
      w.println("    ", em, " model = this.clone(example);");
    } else {
      w.println("    ", em, " model = this.clone(layout);");
    }

    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    if (mechanics.getMode() == PrimaryKeyRetrievalMode.NO_RETRIEVAL) {
      w.println("      preparedQuery.execute(conn);");
    } else {
      String targetClass = pk.getColumns().get(0).getType().getJavaClassName();
      String pkCast = GenUtils.convertPropertyType(Long.class.getName(), targetClass, "pk");
      w.println("      Long pk = preparedQuery.execute(conn);");
      ColumnMetadata pkcm = pk.getColumns().get(0);
      String setter = pkcm.getId().getJavaSetter();
      w.println("      model." + setter + "(" + pkCast + ");");
    }
    w.println("    } catch (", SQLException.class, " e) {");
    w.println("      throw new ", PersistenceException.class, "(e);");
    w.println("    }");
    w.println("    return model;");
    w.println("  }");

  }

  private String renderNumericLiteral(final long value, final String type) {
    if (type == null) {
      return "" + value;
    }
    String typet = type.trim();
    if ("java.lang.Byte".equals(typet) || "Byte".equals(typet)) {
      return "(byte) " + value;
    } else if ("java.lang.Short".equals(typet) || "Short".equals(typet)) {
      return "(short) " + value;
    } else if ("java.lang.Integer".equals(typet) || "Integer".equals(typet)) {
      return "" + value;
    } else if ("java.lang.Long".equals(typet) || "Long".equals(typet)) {
      return "" + value + "L";
    } else {
      return "" + value;
    }
  }

  private InsertMechanics computeInsertMechanics(List<ColumnMetadata> sequences, List<ColumnMetadata> identities,
      List<ColumnMetadata> defaults) throws ErrorMessageException {

    // Identity

    if (identities.size() > 0) {
      if (identities.size() == 1) {
        @SuppressWarnings("unused")
        ColumnMetadata cm = identities.get(0);
        if (this.adapter.getInsertIntegration().integratesIdentities()) {
          if (this.adapter.getInsertIntegration().identitiesMustDeclarePKColumns()) {
            String[] pkcols = this.metadata.getPK().getColumns().stream().map(c -> c.getId().getRenderedSQLName())
                .toArray(String[]::new);
            return new InsertMechanics(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET, null, null, null, null,
                pkcols);
          } else {
            return new InsertMechanics(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET);
          }
        } else {
          return new InsertMechanics(PrimaryKeyRetrievalMode.NO_RETRIEVAL);
        }
      } else { // Implement in the future
        throw new ErrorMessageException(
            "HotRod does not support multiple columns generated as IDENTITY in the same table: table '"
                + this.metadata.getId().getRenderedSQLName() + "'");
      }
    }

    // Sequence

    if (sequences.size() > 0) {
      if (sequences.size() == 1) {
        ColumnMetadata cm = sequences.get(0);
        String sequenceInlineSQL = null;
        String sequencePreFetchSQL = null;
        try {
          sequenceInlineSQL = this.adapter.renderInlineSequenceOnInsert(cm);
          sequencePreFetchSQL = this.adapter.renderSelectSequence(cm);
        } catch (SequencesNotSupportedException e) {
          throw new ErrorMessageException(e.getMessage());
        }
        if (this.adapter.getInsertIntegration().integratesSequencesKeysResultSet()) {
          if (this.adapter.getInsertIntegration().identitiesMustDeclarePKColumns()) {
            String[] pkcols = this.metadata.getPK().getColumns().stream().map(c -> c.getId().getCanonicalSQLName())
                .toArray(String[]::new);
            return new InsertMechanics(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET, null, null,
                sequenceInlineSQL, null, pkcols);
          } else {
            return new InsertMechanics(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET, null, null,
                sequenceInlineSQL, null, null);
          }
        } else if (this.adapter.getInsertIntegration().integratesSequencesStandardResultSet()) {
          String[] pkcols = null;
          if (this.adapter.getInsertIntegration().getOutputClause() != null) {
            pkcols = this.metadata.getPK().getColumns().stream().map(c -> c.getId().getRenderedSQLName())
                .toArray(String[]::new);
          }
          return new InsertMechanics(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_STANDARD_RESULTSET, null, null,
              sequenceInlineSQL, this.adapter.getInsertIntegration().getOutputClause(), pkcols);
        } else {
          return new InsertMechanics(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, sequencePreFetchSQL,
              cm.getId().getJavaMemberName(), null, null, null);
        }
      } else { // Do not implement yet
        throw new ErrorMessageException("HotRod does not support multiple columns generated using sequences: table '"
            + this.metadata.getId().getRenderedSQLName() + "'");
      }
    }

    // No Identities and no columns populated by sequences

    return new InsertMechanics(PrimaryKeyRetrievalMode.NO_RETRIEVAL);

  }

  private void writeUpdateByPK(boolean optimisticLocking) {

    KeyMetadata pk = this.metadata.getPK();
    OptimisticLockingMetadata ol = optimisticLocking ? this.metadata.getOptimisticLocking() : null;

    if (pk == null) {
      w.println();
      w.println("  // UPDATE BY PRIMARY KEY -- Not available since the table does not have a primary key.");
    } else {

      w.println();
      w.println("  // UPDATE BY PRIMARY KEY"
          + (ol == null ? "" : (" (OPTIMISTIC LOCKING - STRATEGY: " + ol.getStrategy().getTitle() + ")")));

      // DynamicSQL Query

      String queryName = "updateByPK" + (ol == null ? "" : "WithOptimisticLocking");
      String initializerName = "initialize" + SUtil.capitalize(queryName);
      this.initializersInPostConstruct.add(initializerName);

      w.println();
      w.println("  private ", DynamicModificationQuery.class, " " + queryName + ";");
      w.println();
      w.println("  private void " + initializerName + "() {");
      w.println("    this." + queryName + " = dyn");
      w.println(
          "      .literaln(\"UPDATE " + SUtil.escapeJavaString(this.metadata.getId().getRenderedSQLName()) + "\")");
      w.println("      .literaln(\"SET\")");

      int coln = this.metadata.getColumns().size();
      int n = 1;
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        String sqlId = cm.getId().getRenderedSQLName();
        String converterParam = cm.getResolvedConverter() == null ? ""
            : ", this." + this.converterProperties.get(cm.getResolvedConverter().getName());
        if (ol != null && cm.isOLVersionNumberColumn()) {
          w.println("      .literal(\"  " + SUtil.escapeJavaString(sqlId) + " = " + SUtil.escapeJavaString(sqlId)
              + " + 1\"" + converterParam + ")" + (n < coln ? ".literaln(\",\")" : ".literaln()"));
        } else if (ol != null && cm.isOLTimestampColumn()) {
          w.println("      .literal(\"  " + SUtil.escapeJavaString(sqlId) + " = "
              + SUtil.escapeJavaString(this.adapter.currentTimestampSQLExpression()) + "\")"
              + (n < coln ? ".literaln(\",\")" : ".literaln()"));
        } else {
          String memId = cm.getId().getJavaMemberName();
          String jdbcType = cm.getType().getJDBCShortType();
          w.println(
              "      .literal(\"  " + SUtil.escapeJavaString(sqlId) + " = \").parameterNullable(\"m."
                  + SUtil.escapeJavaString(memId) + "\", ",
              Types.class, "." + jdbcType + converterParam + ")" + (n < coln ? ".literaln(\",\")" : ".literaln()"));
        }
        n++;
      }

      if (ol == null) {
        fragmentWherePK("m");
      } else if (ol.getStrategy() == OptimisticLockingStrategy.FULL_ROW_CHECK) {
        fragmentWhereFullRow("b");
      } else {
        fragmentWherePK("m");
        ColumnMetadata cm = this.metadata.getOptimisticLocking().getColumnMetadata();
        String memId = cm.getId().getJavaMemberName();
        String sqlId = cm.getId().getRenderedSQLName();
        String jdbcType = cm.getType().getJDBCShortType();
        w.println("      .literaln(\"  AND " + SUtil.escapeJavaString(sqlId) + " = \").parameter(\"m."
            + SUtil.escapeJavaString(memId) + "\", ", Types.class, "." + jdbcType + ")");
      }

      w.println("    .endModificationQuery();");
      w.println("  }");

      // Method

      ExternalClass em = ExternalClass.of(this.model.getFullClassName());
      w.println();
      if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.FULL_ROW_CHECK) {
        w.print("  public int update(", em, " model, ", this.getBaselineClass(), " baseline");
      } else {
        w.print("  public int update(", em, " model");
      }
      w.println(") {");

      for (ColumnMetadata cm : pk.getColumns()) {
        String getter = cm.getId().getJavaGetter();
        w.println("    if (model." + getter + "() == null) return 0;");
      }

      w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
      w.println("    params.add(\"m\", model);");
      if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.FULL_ROW_CHECK) {
        w.println("    params.add(\"b\", baseline);");
      }
      w.println("    ", PreparedModificationQuery.class, " preparedQuery = this." + queryName + ".prepare(params);");

      fragmentLogging();
      fragmentExecuteModification(optimisticLocking, "UPDATE");

      w.println("  }");

    }

  }

  private void writeUpdateByExample() {

    w.println();
    w.println("  // UPDATE BY EXAMPLE");

    // Query

    String queryName = "updateByExample";
    String initializerName = "initialize" + SUtil.capitalize(queryName);
    this.initializersInPostConstruct.add(initializerName);

    w.println();
    w.println("  private ", DynamicModificationQuery.class, " " + queryName + ";");
    w.println();
    w.println("  private void " + initializerName + "() {");
    w.println("    this." + queryName + " = dyn");
    w.println("      .literal(\"UPDATE " + SUtil.escapeJavaString(this.metadata.getId().getRenderedSQLName()) + "\")");
    fragmentSet("v");
    fragmentWhereExample("e");
    w.println("      .endModificationQuery();");
    w.println("  }");

    // Method

//    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    ExternalClass el = ExternalClass.of(this.layout.getFullClassName());
    w.println();
    w.print("  public int update(", el, " example, ", el, " values");
    w.println(") {");

    w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
    w.println("    params.add(\"e\", example);");
    w.println("    params.add(\"v\", values);");
    w.println("    ", PreparedModificationQuery.class, " preparedQuery = this.updateByExample.prepare(params);");

    fragmentLogging();
    fragmentExecuteModification();

    w.println("  }");

  }

  private void writeUpdateByCriteria() {
//    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    ExternalClass el = ExternalClass.of(this.layout.getFullClassName());
    ExternalClass ec = ExternalClass.of(this.metadataClassName);
    w.println();
    w.println("  // UPDATE BY CRITERIA");
    w.println();
    w.print("  public ", UpdateWherePhase.class, " update(", el, " values, ");
    w.println(ec, " tableOrView,");
    w.println("      final ", Predicate.class, " predicate) {");
    w.print("    ", List.class, "<", Setter.class, "> setters");
    w.println(" = new ", ArrayList.class, "<>();");

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String getter = cm.getId().getJavaGetter();
      String memId = cm.getId().getJavaMemberName();
      w.println("    if (values." + getter + "() != null) setters.add(new ", Setter.class,
          "(tableOrView." + memId + ", sql.val(values." + getter + "())));");
    }

    w.println("    return new ", UpdateWherePhase.class,
        "(this.context, tableOrView, setters, predicate, livesql_log);");
    w.println("  }");
  }

  private void writeDeleteByPK(boolean optimisticLocking) {

    KeyMetadata pk = this.metadata.getPK();
    OptimisticLockingMetadata ol = optimisticLocking ? this.metadata.getOptimisticLocking() : null;

    if (pk == null) {
      w.println();
      w.println("  // DELETE BY PRIMARY KEY -- Not available since the table does not have a primary key.");
    } else {

      w.println();
      w.println("  // DELETE BY PRIMARY KEY"
          + (ol == null ? "" : (" (OPTIMISTIC LOCKING - STRATEGY: " + ol.getStrategy().getTitle() + ")")));

      // Query

      String queryName = "deleteByPK" + (ol == null ? "" : "WithOptimisticLocking");
      String initializerName = "initialize" + SUtil.capitalize(queryName);
      this.initializersInPostConstruct.add(initializerName);

      w.println();
      w.println("  private ", DynamicModificationQuery.class, " " + queryName + ";");
      w.println();
      w.println("  private void " + initializerName + "() {");
      w.println("    this." + queryName + " = dyn");
      w.println("      .literaln(\"DELETE FROM " + SUtil.escapeJavaString(this.metadata.getId().getRenderedSQLName())
          + "\")");

      if (ol == null) {
        fragmentWherePK("f");
      } else if (ol.getStrategy() == OptimisticLockingStrategy.FULL_ROW_CHECK) {
        fragmentWhereFullRow("b");
      } else {
        fragmentWherePK("b");
        ColumnMetadata cm = this.metadata.getOptimisticLocking().getColumnMetadata();
        String memId = cm.getId().getJavaMemberName();
        String sqlId = cm.getId().getRenderedSQLName();
        String jdbcType = cm.getType().getJDBCShortType();
        w.println("      .literaln(\"  AND " + SUtil.escapeJavaString(sqlId) + " = \").parameter(\"b."
            + SUtil.escapeJavaString(memId) + "\", ", Types.class, "." + jdbcType + ")");
      }

      w.println("      .endModificationQuery();");
      w.println("  }");

      // Method

      ExternalClass em = ExternalClass.of(this.model.getFullClassName());

      w.println();
      w.print("  public int delete(");
      if (optimisticLocking) {
        w.print(this.getBaselineClass(), " baseline");
        w.println(") {");
        for (ColumnMetadata cm : pk.getColumns()) {
          String getter = cm.getId().getJavaGetter();
          w.println("    if (baseline." + getter + "() == null) return 0;");
        }
        if (ol != null && !ol.getStrategy().usesAllColumns()) {
          ColumnMetadata cm = ol.getColumnMetadata();
          String getter = cm.getId().getJavaGetter();
          w.println("    if (baseline." + getter + "() == null) return 0;");
        }
        w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
        w.println("    params.add(\"b\", baseline);");
      } else {
        fragmentPKParameters(pk);
        w.println(") {");
        for (ColumnMetadata cm : pk.getColumns()) {
          String m = cm.getId().getJavaMemberName();
          w.println("    if (" + m + " == null) return 0;");
        }
        w.println("    ", em, " filter = new ", em, "();");
        for (ColumnMetadata cm : pk.getColumns()) {
          String m = cm.getId().getJavaMemberName();
          String setter = cm.getId().getJavaSetter();
          w.println("    filter." + setter + "(" + m + ");");
        }
        w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
        w.println("    params.add(\"f\", filter);");
      }

      w.println("    ", PreparedModificationQuery.class, " preparedQuery = this." + queryName + ".prepare(params);");

      fragmentLogging();
      fragmentExecuteModification(optimisticLocking, "DELETE");

      w.println("  }");

    }

  }

  private void writeDeleteByExample() {

    w.println();
    w.println("  // DELETE BY EXAMPLE");

    // Query

    String queryName = "deleteByExample";
    String initializerName = "initialize" + SUtil.capitalize(queryName);
    this.initializersInPostConstruct.add(initializerName);

    w.println();
    w.println("  private ", DynamicModificationQuery.class, " " + queryName + ";");
    w.println();
    w.println("  private void " + initializerName + "() {");
    w.println("    this." + queryName + " = dyn");
    w.println(
        "      .literaln(\"DELETE FROM " + SUtil.escapeJavaString(this.metadata.getId().getRenderedSQLName()) + "\")");
    fragmentWhereExample("e");
    w.println("      .endModificationQuery();");
    w.println("  }");

    // Method

    ExternalClass el = ExternalClass.of(this.layout.getFullClassName());
    w.println();
    w.print("  public int delete(", el, " example");
    w.println(") {");

    w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
    w.println("    params.add(\"e\", example);");
    w.println("    ", PreparedModificationQuery.class, " preparedQuery = this.deleteByExample.prepare(params);");

    fragmentLogging();
    fragmentExecuteModification();

    w.println("  }");

  }

  private void writeDeleteByCriteria() {
    ExternalClass ec = ExternalClass.of(this.metadataClassName);
    w.println();
    w.println("  // DELETE BY CRITERIA");
    w.println();
    w.print("  public ", DeleteWherePhase.class);
    w.println(" delete(final ", ec, " from, final ", Predicate.class, " predicate) {");
    w.println("    return new ", DeleteWherePhase.class, "(this.context, from, predicate, livesql_log);");
    w.println("  }");
  }

  private void writeMetadata() throws IOException {

    Class<?> type = this.isTable() ? Table.class : View.class;
    String typeName = type.getSimpleName();

    Id catalog = this.metadata.getId().getCatalog();
    Id schema = this.metadata.getId().getSchema();
    Id name = this.metadata.getId().getObject();

    ExternalClass pc = ExternalClass.of(type);
    ExternalClass ec = ExternalClass.of(this.metadataClassName);
    ExternalClass el = ExternalClass.of(this.layout.getFullClassName());
    ExternalClass em = ExternalClass.of(this.model.getFullClassName());

    w.println();
    w.println("  // " + type.getSimpleName().toUpperCase() + " METADATA");
    w.println();
    w.println("  public ", ec, " new", pc, "() {");
    w.println("    return new ", ec, "();");
    w.println("  }");
    w.println();
    w.println("  public ", ec, " new", pc, "(final String alias) {");
    w.println("    return new ", ec, "(alias);");
    w.println("  }");
    w.println();
    w.print("  public static class ", ec);
    w.println(" extends ", pc, "<", em, "> {");

    w.println();
    int thId = 0;
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      Class<?> liveSQLColumnType = toLiveSQLType(javaType);
      String memberName = cm.getId().getJavaMemberName();
      String canonicalName = cm.getId().getCanonicalSQLName();
      String property = cm.getId().getJavaMemberName();

      ExternalClass jt = ExternalClass.of(javaType);
      ExternalClass lt = ExternalClass.of(liveSQLColumnType);

      if (cm.getResolvedConverter() == null) {

        w.println("    public final ", lt, " " + memberName + " = new ", lt, "(this,");
        w.print("      " //
            + "\"" + JUtils.escapeJavaString(canonicalName) + "\"" //
            + ", \"" + JUtils.escapeJavaString(property) + "\"" //
            + ", \"" + JUtils.escapeJavaString(cm.getTypeName()) + "\"" //
            + ", " + cm.getPrecision() //
            + ", " + cm.getScale() //
            + ", ");

        w.print(TypeHandler.class, ".forClass(", jt, ".class, ");
        TypeSource typeSource = cm.getType().getTypeSource();
        String ruleNumber = cm.getType().getRuleNumber();
        w.print(TypeSource.class, "." + typeSource.name() + ", "
            + (ruleNumber == null ? "null" : "\"" + SUtil.escapeJavaString(ruleNumber) + "\"") + ")");

        w.println(");");

      } else {

        ExternalClass rawClass = ExternalClass.of(cm.getResolvedConverter().getRawClass());
        ExternalClass domainClass = ExternalClass.of(cm.getResolvedConverter().getDomainClass());
        ExternalClass converterClass = ExternalClass.of(cm.getResolvedConverter().getConverterClass());

        w.print("    private final ", TypeHandler.class, "<", rawClass, ", ");
        w.print(domainClass, "> th" + thId + " = ", TypeHandler.class, ".forConverter(new ", converterClass);
        TypeSource typeSource = cm.getType().getTypeSource();
        String ruleNumber = cm.getType().getRuleNumber();
        w.println("(), ", TypeSource.class, "." + typeSource.name() + ", "
            + (ruleNumber == null ? "null" : "\"" + SUtil.escapeJavaString(ruleNumber) + "\"") + ");");

        w.print("    public final ", ConvertedColumn.class, "<", rawClass, ", ");
        w.print(domainClass, "> " + memberName + " = new ", ConvertedColumn.class);
        w.println("<", rawClass, ", ", domainClass, ">(this, \"" //
            + JUtils.escapeJavaString(canonicalName) + "\", \"" //
            + JUtils.escapeJavaString(property) + "\", \"" //
            + JUtils.escapeJavaString(cm.getTypeName()) + "\", " //
            + cm.getPrecision() //
            + ", " + cm.getScale() //
            + ", th" + thId + ", th" + thId + ".getConverter());");
        thId++;

      }

    }

    w.println();

    ExternalClass ac = ExternalClass.of(AllColumns.class);

    w.println("    @", Override.class);
    w.println("    public ", ac, " star() {");
    w.println("      return new ", ac, "(" + this.metadata.getColumns().stream()
        .map(c -> "this." + c.getId().getJavaMemberName()).collect(Collectors.joining(", ")) + ");");
    w.println("    }");

    ExternalClass nm = ExternalClass.of(Name.class);
    w.println();
    w.println("    " + this.metadataClassName + "() {");
    w.print("      super(");
    if (catalog == null) {
      w.print("null");
    } else {
      w.print(nm,
          ".of(\"" + JUtils.escapeJavaString(catalog.getCanonicalSQLName()) + "\", " + catalog.isQuoted() + ")");
    }
    w.print(", ");
    if (schema == null) {
      w.print("null");
    } else {
      w.print(nm, ".of(\"" + JUtils.escapeJavaString(schema.getCanonicalSQLName()) + "\", " + schema.isQuoted() + ")");
    }
    w.print(", ");
    w.print(nm, ".of(\"" + JUtils.escapeJavaString(name.getCanonicalSQLName()) + "\", " + name.isQuoted() + ")");
    w.println(", \"" + typeName + "\", null, ", el, ".class, ", em, ".class);");
    w.println("      initialize();");
    w.println("    }");
    w.println();
    w.println("    " + this.metadataClassName + "(final String alias) {");
    w.print("      super(");
    if (catalog == null) {
      w.print("null");
    } else {
      w.print(nm,
          ".of(\"" + JUtils.escapeJavaString(catalog.getCanonicalSQLName()) + "\", " + catalog.isQuoted() + ")");
    }
    w.print(", ");
    if (schema == null) {
      w.print("null");
    } else {
      w.print(nm, ".of(\"" + JUtils.escapeJavaString(schema.getCanonicalSQLName()) + "\", " + schema.isQuoted() + ")");
    }
    w.print(", ");
    w.print(nm, ".of(\"" + JUtils.escapeJavaString(name.getCanonicalSQLName()) + "\", " + name.isQuoted() + ")");
    w.println(", \"" + typeName + "\", alias, ", el, ".class, ", em, ".class);");
    w.println("      initialize();");
    w.println("    }");

    w.println();
    w.println("    private void initialize() {");
    w.println("      super.columns = new ", ArrayList.class, "<>();");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      w.println("      super.columns.add(this." + cm.getId().getJavaMemberName() + ");");
    }

    w.println("    }");
    w.println();
    w.println("  }");
  }

  private String resolveType(final ColumnMetadata cm) {
    EnumClass ec = this.generator.getEnum(cm.getEnumMetadata());
    return ec != null ? ec.getFullClassName() : cm.getType().getJavaClassName();
  }

  private Class<?> toLiveSQLType(final String javaType) {
    if ("java.lang.Byte".equals(javaType) || "Byte".equals(javaType) //
        || "java.lang.Short".equals(javaType) || "Short".equals(javaType)//
        || "java.lang.Integer".equals(javaType) || "Integer".equals(javaType) ////
        || "java.lang.Long".equals(javaType) || "Long".equals(javaType) //
        || "java.lang.Float".equals(javaType) || "Float".equals(javaType) //
        || "java.lang.Double".equals(javaType) || "Double".equals(javaType) //
        || "java.math.BigInteger".equals(javaType) //
        || "java.math.BigDecimal".equals(javaType) //
    ) {
      return NumericEntityColumn.class;
    } else if ("java.lang.String".equals(javaType)) {
      return CharEntityColumn.class;
    } else if ("java.util.Date".equals(javaType) //
        || "java.sql.Date".equals(javaType) //
        || "java.sql.Timestamp".equals(javaType) //
        || "java.sql.Time".equals(javaType) //
        || "java.time.LocalDateTime".equals(javaType) //
        || "java.time.LocalDate".equals(javaType) //
        || "java.time.LocalTime".equals(javaType) //
        || "java.time.ZonedDateTime".equals(javaType) //
        || "java.time.OffsetDateTime".equals(javaType) //
        || "java.time.OffsetTime".equals(javaType) //
        || "java.time.Instant".equals(javaType) //
    ) {
      return DateTimeEntityColumn.class;
    } else if ("java.lang.Boolean".equals(javaType)) {
      return BooleanEntityColumn.class;
    } else if ("byte[]".equals(javaType)) {
      return BinaryEntityColumn.class;
    }

    return ObjectEntityColumn.class;
  }

  private void writeOrderBy() {

    w.println();
    w.println("  // ORDER BY");

    ExternalClass ob = ExternalClass.of(this.getOrderByClassName());

    w.println();
    w.println("  public enum ", ob, " implements ", OrderBy.class, " {");

    w.println();
    int col = 1;
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String constantBase = cm.getId().getJavaConstantName();
      String colSQL = cm.getId().getRenderedSQLName();
      w.println("    " + constantBase + "(\"" + SUtil.escapeJavaString(colSQL) + "\", true),");
      w.println("    " + constantBase + "$DESC(\"" + SUtil.escapeJavaString(colSQL) + "\", false)"
          + (col < this.metadata.getColumns().size() ? "," : ";"));
      col++;
    }

    w.println();
    w.println("    private String sqlColumnName;");
    w.println("    private boolean ascending;");
    w.println();
    w.println("    private ", ob, "(String sqlColumnName, boolean ascending) {");
    w.println("      this.sqlColumnName = sqlColumnName;");
    w.println("      this.ascending = ascending;");
    w.println("    }");
    w.println();
    w.println("    @", Override.class);
    w.println("    public String getSQLColumnName() {");
    w.println("      return this.sqlColumnName;");
    w.println("    }");
    w.println();
    w.println("    @", Override.class);
    w.println("    public boolean isAscending() {");
    w.println("      return this.ascending;");
    w.println("    }");
    w.println();
    w.println("  }");

  }

  private void fragmentSet(String ns) {
    OptimisticLockingMetadata ol = this.metadata.getOptimisticLocking();
    w.println("      .set()");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String sqlId = cm.getId().getRenderedSQLName();
      String converterParam = cm.getResolvedConverter() == null ? ""
          : ", this." + this.converterProperties.get(cm.getResolvedConverter().getName());
      if (ol != null && cm.isOLVersionNumberColumn()) {
        w.println("        .if_(\"true\").literal(\"" + ns + "." + SUtil.escapeJavaString(sqlId) + " = "
            + SUtil.escapeJavaString(sqlId) + " + 1\").endif()");
      } else if (ol != null && cm.isOLTimestampColumn()) {
        w.println("        .if_(\"true\").literal(\"" + ns + "." + SUtil.escapeJavaString(sqlId) + " = "
            + SUtil.escapeJavaString(this.adapter.currentTimestampSQLExpression()) + "\").endif()");
      } else {
        String memId = cm.getId().getJavaMemberName();
        w.println("        .if_(\"" + ns + "." + SUtil.escapeJavaString(memId) + " != null\").literal(\""
            + SUtil.escapeJavaString(sqlId) + " = \").parameter(\"" + ns + "." + SUtil.escapeJavaString(memId) + "\""
            + converterParam + ").endif()");
      }
    }
    w.println("      .endset()");

  }

  private void fragmentPKParameters(KeyMetadata pk) {
    Separator sep = new Separator(", ");
    for (ColumnMetadata cm : pk.getColumns()) {
      w.print(sep.render());
      EnumDataSetMetadata em = cm.getEnumMetadata();
      String javaClassName;
      if (em != null) {
        EnumClass ec = this.generator.getEnum(em);
        javaClassName = ec.getFullClassName();
      } else {
        javaClassName = cm.getType().getJavaClassName();
      }
      w.print(ExternalClass.of(javaClassName), " ", cm.getId().getJavaMemberName());
    }
  }

  private void fragmentWherePK(String ns) {
    KeyMetadata pk = this.metadata.getPK();
    Separator sep = Separator.of("WHERE ", "  AND ");
    for (ColumnMetadata cm : pk.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String converterParam = cm.getResolvedConverter() == null ? ""
          : ", this." + this.converterProperties.get(cm.getResolvedConverter().getName());
      w.println("      .literal(\"" + SUtil.escapeJavaString(sep.render()) + SUtil.escapeJavaString(sqlId)
          + " = \").parameter(\"" + ns + "." + SUtil.escapeJavaString(memId) + "\"" + converterParam + ").literaln()");
    }
  }

  private void fragmentWhereFullRow(String ns) {
    Separator sep = Separator.of("\nWHERE ", "  AND ");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String jdbcType = cm.getType().getJDBCShortType();
      String converterParam = cm.getResolvedConverter() == null ? ""
          : ", this." + this.converterProperties.get(cm.getResolvedConverter().getName());
      w.println(
          "      .literaln(\"" + SUtil.escapeJavaString(sep.render()) + "\" + \"" + SUtil.escapeJavaString(sqlId)
              + " = \").parameter(\"" + ns + "." + SUtil.escapeJavaString(memId) + "\", ",
          Types.class, "." + jdbcType + converterParam + ")");
    }
  }

  private void fragmentWhereExample(String ns) {
    w.println("      .where(\"AND\")");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String converterParam = cm.getResolvedConverter() == null ? ""
          : ", this." + this.converterProperties.get(cm.getResolvedConverter().getName());
      w.println("        .if_(\"" + ns + "." + memId + " != null\").literal(\"" + SUtil.escapeJavaString(sqlId)
          + " = \").parameter(\"" + ns + "." + memId + "\"" + converterParam + ").endif()");
    }
    w.println("      .endwhere()");
  }

  private void fragmentLogging() {
    w.println("    logQuery(preparedQuery);");
  }

  private void fragmentExecuteModification() {
    this.fragmentExecuteModification(false, null);
  }

  private void fragmentExecuteModification(boolean optimisticLocking, String clause) {
    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    w.println("      int count = preparedQuery.execute(conn);");
    if (optimisticLocking) {
      w.println("      if (count == 0) {");
      w.println("        throw new ", StaleDataException.class,
          "(" + "\"Optimistic locking " + clause + " failed. The row in the table "
              + this.metadata.getId().getCanonicalSQLName()
              + " had changed or had been deleted since it was read.\");");
      w.println("      }");
    }
    w.println("      return count;");
    w.println("    } catch (", SQLException.class, " e) {");
    w.println("      throw new ", PersistenceException.class, "(e);");
    w.println("    }");
  }

  private void fragmentExecuteSelect(ExternalClass m, boolean singleRow) {
    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    w.println("      ", List.class, "<", m, "> rows = preparedQuery.execute(conn);");

    if (singleRow) {
      w.println("      if (rows.size() == 0) return null;");
      w.println("      if (rows.size() == 1) return rows.get(0);");
      w.println("      throw new ", PersistenceException.class,
          "(\"A single row at most was expected but received \" + rows.size() + \" rows.\");");
    } else {
      w.println("      return rows;");
    }
    w.println("    } catch (", SQLException.class, " e) {");
    w.println("      throw new ", PersistenceException.class, "(e);");
    w.println("    }");
  }

  private LinkedHashMap<String, String> converterProperties = new LinkedHashMap<>();

  private void writeConverterBeans() {
    int n = 0;

    // Table or View columns
    n = writeConverter(n, this.metadata.getColumns());

    // Free Nitro Selects
    for (SelectMethodMetadata s : this.metadata.getSelectsMetadata()) {
      n = writeConverter(n, s.getColumns());
    }

  }

  private int writeConverter(int n, List<ColumnMetadata> cols) {
    for (ColumnMetadata cm : cols) {
      ConverterTag ct = cm.getResolvedConverter();
      if (ct != null) {
        boolean found = this.converterProperties.containsKey(ct.getName());
        if (!found) {
          if (n == 0) {
            w.println();
            w.println("  // CONVERTERS");
          }
          String property = "converter" + n++;
          this.converterProperties.put(ct.getName(), property);
          w.println();
          w.println("  @", Const.AUTOWIRED);
          ExternalClass cc = ExternalClass.of(ct.getConverterClass());
          w.println("  private ", cc, " " + property + ";");
        }
      }
    }
    return n;
  }

  // Utils

  private static class JDBCGetter {

    private String resultSetMethod;
    private boolean returnsPrimitiveType;

    private JDBCGetter(String resultSetMethod, boolean returnsPrimitiveType) {
      this.resultSetMethod = resultSetMethod;
      this.returnsPrimitiveType = returnsPrimitiveType;
    }

    public static JDBCGetter obj(String m) {
      return new JDBCGetter(m, false);
    }

    public static JDBCGetter prim(String m) {
      return new JDBCGetter(m, true);
    }

    public String getResultSetMethod() {
      return resultSetMethod;
    }

    public boolean returnsPrimitiveType() {
      return returnsPrimitiveType;
    }

  }

  private static final Map<String, JDBCGetter> JDBC_GETTERS = new HashMap<>();
  static {
    JDBC_GETTERS.put("java.math.BigDecimal", JDBCGetter.obj("getBigDecimal"));
    JDBC_GETTERS.put("java.sql.Blob", JDBCGetter.obj("getBlob"));
    JDBC_GETTERS.put("java.lang.Byte", JDBCGetter.prim("getByte"));
    JDBC_GETTERS.put("java.lang.Byte[]", JDBCGetter.obj("getBytes"));
    JDBC_GETTERS.put("java.sql.Clob", JDBCGetter.obj("getClob"));
    JDBC_GETTERS.put("java.sql.Date", JDBCGetter.obj("getDate"));
    JDBC_GETTERS.put("java.lang.Double", JDBCGetter.prim("getDouble"));
    JDBC_GETTERS.put("java.lang.Float", JDBCGetter.prim("getFloat"));
    JDBC_GETTERS.put("java.lang.Integer", JDBCGetter.prim("getInt"));
    JDBC_GETTERS.put("java.lang.Long", JDBCGetter.prim("getLong"));
    JDBC_GETTERS.put("java.lang.Boolean", JDBCGetter.prim("getBoolean"));
    JDBC_GETTERS.put("java.lang.Short", JDBCGetter.prim("getShort"));
    JDBC_GETTERS.put("java.sql.SQLXML", JDBCGetter.obj("getSQLXML"));
    JDBC_GETTERS.put("java.lang.String", JDBCGetter.obj("getString"));
    JDBC_GETTERS.put("java.sql.Time", JDBCGetter.obj("getTime"));
    JDBC_GETTERS.put("java.sql.Timestamp", JDBCGetter.obj("getTimestamp"));
  }

  private void writeColumnReaderLogic(ColumnMetadata cm, int ordinal, boolean discoverable) {
    w.println();
    String setter = cm.getId().getJavaSetter();

//    log.info("ROWREADER: " + OUtil.hc(this) + " md" + OUtil.hc(this.metadata) + " gc"
//        + OUtil.hc(this.metadata.getColumns()) + " cm" + OUtil.hc(cm) + " "
//        + this.metadata.getId().getCanonicalSQLName() + "." + cm.getName() + " setter=" + setter);

    String javaClass = cm.getType().getJavaClassName();
    ConverterTag ct = cm.getResolvedConverter();
    String cn = cm.getId().getCanonicalSQLName();
//    log.info("-- " + cn + ": javaClass=" + javaClass);

    String colName = "\"" + SUtil.escapeJavaString(cm.getLabel()) + "\"";

    if (discoverable) {
      w.println("      if (this.present" + ordinal + ") {");
    }
    String indent = discoverable ? "  " : "";

    if (ct == null) { // No converter
      JDBCGetter g = JDBC_GETTERS.get(javaClass);
//      log.info("- g=" + g + " method=" + (g == null ? "null" : g.getResultSetMethod()));
      String var = "col" + ordinal;
      ExternalClass jc = ExternalClass.of(javaClass);
      if (g != null) {
        if (g.returnsPrimitiveType()) {
          w.println(indent + "      ", jc,
              " " + var + " = rs." + g.getResultSetMethod() + "(" + colName + "); // " + cn);
          w.println(indent + "      if (rs.wasNull()) " + var + " = null;");
          w.println(indent + "      row." + setter + "(" + var + ");");
        } else {
          w.println(indent + "      ", jc,
              " " + var + " = rs." + g.getResultSetMethod() + "(" + colName + "); // " + cn);
          w.println(indent + "      row." + setter + "(" + var + ");");
        }
      } else {
        w.println(indent + "      ", jc, " " + var + " = rs.getObject(" + colName + ", ", jc, ".class); // " + cn);
        w.println(indent + "      row." + setter + "(" + var + ");");
      }
    } else { // Converter specified
      String raw = "raw" + ordinal;
      String var = "col" + ordinal;
      String rawClass = ct.getRawClass();
      ExternalClass rc = ExternalClass.of(rawClass);
      JDBCGetter g = JDBC_GETTERS.get(rawClass);
      javaClass = ct.getDomainClass();
      ExternalClass mc = ExternalClass.of(javaClass);
      String property = this.converterProperties.get(ct.getName());
      if (g != null) {
        if (g.returnsPrimitiveType()) {
          w.println(indent + "      ", rc,
              " " + raw + " = rs." + g.getResultSetMethod() + "(" + colName + "); // " + cn);
          w.println(indent + "      if (rs.wasNull()) " + raw + " = null;");
          w.println(indent + "      ", mc, " " + var + " = " + property + ".decode(" + raw + ", conn);");
          w.println(indent + "      row." + setter + "(" + var + ");");
        } else {
          w.println(indent + "      ", rc,
              " " + raw + " = rs." + g.getResultSetMethod() + "(" + colName + "); // " + cn);
          w.println(indent + "      ", mc, " " + var + " = " + property + ".decode(" + raw + ", conn);");
          w.println(indent + "      row." + setter + "(" + var + ");");
        }
      } else {
        w.println(indent + "      ", rc, " " + raw + " = rs.getObject(" + colName + ", ", rc, ".class); // " + cn);
        w.println(indent + "      ", mc, " " + var + " = " + property + ".decode(" + raw + ", conn);");
        w.println(indent + "      row." + setter + "(" + var + ");");
      }
    }

    if (discoverable) {
      w.println("      }");
    }
  }

  private void writeSequenceRowReader() throws IOException {
    w.println();
    w.println("  // SEQUENCE ROW READER");
    w.println();
    w.println("  private final ", RowReader.class, "<Long> sequenceRowReader = new ", RowReader.class, "<Long>() {");
    w.println();
    w.println("    @", Override.class);
    w.println("    public Long readRowFrom(", ResultSet.class, " rs, ", Connection.class,
        " conn) throws SQLException {");
    w.println("      Long col1 = rs.getLong(1);");
    w.println("      if (rs.wasNull()) col1 = null;");
    w.println("      return col1;");
    w.println("    }");
    w.println();
    w.println("  };");
  }

  private void writeSelectSequence(final SequenceMethodTag tag, int n) throws IOException, ErrorMessageException {

    String sql;
    try {
      sql = this.adapter.renderSelectSequence(tag.getSequenceId());
    } catch (SequencesNotSupportedException e) {
      throw new ErrorMessageException(this.tag,
          "Could not generate method for <sequence> tag. Sequences are not supported in this database edition or version.");
    }

    String initializerMethod = "initializeSelectSequence" + n;
    this.initializersInPostConstruct.add(initializerMethod);

    w.println();
    w.println("  // SELECT SEQUENCE");
    w.println();
    w.println("  private ", DynamicSelectQuery.class, " selectSequence" + n + ";");
    w.println();
    w.println("  private void " + initializerMethod + "() {");
    w.println("    this.selectSequence" + n + " = dyn.literaln(\"" + sql + "\").endSelectQuery();");
    w.println("  }");
    w.println();
    w.println("  public long " + tag.getMethod() + "() {");
    w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
    w.println("    ", PreparedSelectQuery.class,
        "<Long> preparedQuery = " + "this.selectSequence" + n + ".prepare(params, this.sequenceRowReader);");
    w.println("    logQuery(preparedQuery);");
    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    w.println("      long value = preparedQuery.executeOne(conn);");
    w.println("      return value;");
    w.println("    } catch (", SQLException.class, " e) {");
    w.println("      throw new ", PersistenceException.class, "(e);");
    w.println("    }");
    w.println("  }");

  }

  private void writeNitroQuery(QueryMethodTag q, int n) throws ErrorMessageException {

    String queryName = "query" + n;
    String method = q.getMethod();

    w.println();
    w.println("  // NITRO QUERY: " + method);

    // 1. Query Definition

    String initializerName = "initialize" + SUtil.capitalize(queryName);
    this.initializersInPostConstruct.add(initializerName);

    w.println();
    w.println("  private ", DynamicModificationQuery.class, " " + queryName + ";");
    w.println();
    w.println("  private void " + initializerName + "() {");
    w.println("    this." + queryName + " = dyn");

    List<DynamicSQLPart> parts = q.getDynamicSQLParts();
    NitroRenderer r = new NitroRenderer();
    r.render(parts, w);

    w.println("      .endModificationQuery();");
    w.println("  }");

    // 2. Method

    w.println();
    w.print("  public int " + method + "(");
    Separator sep = new Separator(", ");
    for (ParameterTag p : q.getParameterDefinitions()) {
      ExternalClass pc = ExternalClass.of(p.getType());
      w.print(sep.render(), pc, " " + p.getName());
    }
    w.println(") {");
    w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
    for (ParameterTag p : q.getParameterDefinitions()) {
      w.println("    params.add(\"" + p.getName() + "\", " + p.getName() + ");");
    }
    w.println("    ", PreparedModificationQuery.class, " preparedQuery = this." + queryName + ".prepare(params);");

    fragmentLogging();
    fragmentExecuteModification();

    w.println("  }");

  }

  // TODO: Just a marker

  private void writeNitroEntitySelect(SelectMethodMetadata s, int sno) throws ErrorMessageException {
    this.writeNitroSelectBody(s, sno, this.metadata.getColumns());
  }

  private void writeNitroFreeSelect(SelectMethodMetadata s, int sno) throws ErrorMessageException {
    this.writeNitroSelectBody(s, sno, s.getColumns());
  }

  private void writeNitroSelectBody(SelectMethodMetadata s, int sno, List<ColumnMetadata> columns)
      throws ErrorMessageException {

    String queryName = "select" + sno;
    String method = s.getMethod();

    w.println();
    w.println("  // NITRO SELECT: " + method);

    // 1. Query Definition

    String initializerName = "initialize" + SUtil.capitalize(queryName);
    this.initializersInPostConstruct.add(initializerName);

    w.println();
    w.println("  private ", DynamicSelectQuery.class, " " + queryName + ";");
    w.println();
    w.println("  private void " + initializerName + "() {");
    w.println("    this." + queryName + " = dyn");

    List<EnhancedSQLPart> parts = s.getParts();

    NitroRenderer r = new NitroRenderer();
    r.renderSelect(parts, w);

    w.println("      .endSelectQuery();");
    w.println("  }");

    // 2. Row Reader

    String rowReaderClass = "RowReader" + sno;
    String rowReaderObject = "rowReader" + sno;
    this.writeNitroSelectRowReaderClass(s, rowReaderClass, rowReaderObject, columns);

    // 3. Method

    SelectMethodReturnType rt = s.getReturnType(this.classPackage);
    ExternalClass rc = ExternalClass.of(rt.getBaseReturnVOFullClassName());

    w.println();
    switch (rt.getMode()) {
    case CURSOR:
      w.print("  public ", Cursor.class, "<", rc, "> " + method + "(");
      break;
    case SINGLE_ROW:
      w.print("  public ", rc, " " + method + "(");
      break;
    default:
      w.print("  public ", List.class, "<", rc, "> " + method + "(");
      break;
    }

    Separator sep = new Separator(", ");
    for (SelectParameterMetadata sp : s.getParameters()) {
      ParameterTag p = sp.getParameter();
      ExternalClass pc = ExternalClass.of(p.getType());
      w.print(sep.render(), pc, " " + p.getName());
    }
    w.println(") {");

    w.println("    ", Parameters.class, " params = this.dyn.newParameters();");
    for (SelectParameterMetadata sp : s.getParameters()) {
      ParameterTag p = sp.getParameter();
      w.println("    params.add(\"" + p.getName() + "\", " + p.getName() + ");");
    }

    w.println("    " + rowReaderClass + " rr = new " + rowReaderClass + "();");
    w.print("    ", PreparedSelectQuery.class, "<", rc, "> preparedQuery = ");
    w.println("this." + queryName + ".prepare(params, rr);");

    fragmentLogging();

    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");

    switch (rt.getMode()) {
    case CURSOR:
      w.println("      ", Cursor.class, "<", rc, "> cursor = preparedQuery.executeCursor(conn);");
      w.println("      return cursor;");
      break;
    case SINGLE_ROW:
      w.println("      ", rc, " row = preparedQuery.executeOne(conn);");
      w.println("      return row;");
      break;
    default:
      w.println("      ", List.class, "<", rc, "> rows = preparedQuery.execute(conn);");
      w.println("      return rows;");
      break;
    }

    w.println("    } catch (", SQLException.class, " e) {");
    w.println("      throw new ", PersistenceException.class, "(e);");
    w.println("    }");

    w.println("  }");

  }

  // TODO: Just a marker

  private void writeNitroSelectRowReaderClass(SelectMethodMetadata s, String rowReaderClass, String rowReaderObject,
      List<ColumnMetadata> columns) {
    SelectMethodReturnType rt = s.getReturnType(this.classPackage);
    ExternalClass m = ExternalClass.of(rt.getBaseReturnVOFullClassName());
    rt.getSoloVO();

    w.println();
    w.println("  public final class " + rowReaderClass + " implements ", RowReader.class, "<", m, "> {");
    w.println();

    int ordinal = 1;
//    List<ColumnMetadata> columns = s.getColumns();

    for (@SuppressWarnings("unused")
    ColumnMetadata cm : columns) {
      w.println("    private boolean present" + ordinal + " = false;");
      ordinal++;
    }
    w.println();

    w.println("    @", Override.class);
    w.println("    public void discoverColumns(ResultSet rs) throws SQLException {");
    w.println("      ", ResultSetMetaData.class, " m = rs.getMetaData();");
    ordinal = 1;
    for (@SuppressWarnings("unused")
    ColumnMetadata cm : columns) {
      w.println("      present" + ordinal + " = false;");
      ordinal++;
    }
    w.println("      int n = m.getColumnCount();");
    w.println("      for (int i = 1; i <= n; i++) {");
    w.println("        String l = m.getColumnLabel(i);");
    ordinal = 1;
    for (ColumnMetadata cm : columns) {
      String label = SUtil.escapeJavaString(cm.getLabel());
      w.println("        if (\"" + label + "\".equals(l)) present" + ordinal + " = true;");
      ordinal++;
    }
    w.println("      }");
    w.println("    }");
    w.println();

    w.println("    @", Override.class);
    w.print("    public ", m, " readRowFrom(", ResultSet.class, " rs, ");
    w.print(Connection.class, " conn) ");
    w.println("throws ", SQLException.class, " {");
    w.println("      ", m, " row = applicationContext.getBean(", m, ".class);");

    ordinal = 1;
    for (ColumnMetadata cm : columns) {
      this.writeColumnReaderLogic(cm, ordinal, true);
      ordinal++;
    }

    w.println();
    w.println("      return row;");
    w.println("    }");
    w.println();
    w.println("  };");
  }

  public boolean isTable() {
    return this.daoType == DAOType.TABLE;
  }

  public boolean isView() {
    return this.daoType == DAOType.VIEW;
  }

  public boolean isExecutor() {
    return this.daoType == DAOType.EXECUTOR;
  }

  public String getClassName() {
    return this.jdbcTag.getDAOName(this.metadata.getId());
  }

  private String getOrderByClassName() {
    return this.metadata.getId().getJavaClassName() + "OrderBy";
  }

  public String getFullClassName() {
    return this.classPackage.getFullClassName(getClassName());
  }

  public String getMemberName() {
    return SUtil.lowerFirst(this.getClassName());
  }

}
