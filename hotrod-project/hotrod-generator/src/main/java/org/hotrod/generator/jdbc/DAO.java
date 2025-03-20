package org.hotrod.generator.jdbc;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
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
import javax.swing.text.View;

import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.Constants;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.EnhancedSQLPart;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.OptimisticLockingTag.OptimisticLockingStrategy;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.ParameterContext;
import org.hotrod.dynamicsql.PreparedModificationQuery;
import org.hotrod.dynamicsql.PreparedQuery;
import org.hotrod.dynamicsql.PreparedSelectQuery;
import org.hotrod.dynamicsql.PreparedSelectQuery.RowReader;
import org.hotrod.dynamicsql.assembler.QueryAssembler;
import org.hotrod.dynamicsql.insert.PreparedInsertQuery;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.StaleDataException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.identifiers.Id;
import org.hotrod.interfaces.OrderBy;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.metadata.OptimisticLockingMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodReturnType;
import org.hotrod.metadata.SelectParameterMetadata;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.metadata.AllColumns;
import org.hotrod.runtime.livesql.metadata.BooleanEntityColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayEntityColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.metadata.NumberEntityColumn;
import org.hotrod.runtime.livesql.metadata.ObjectEntityColumn;
import org.hotrod.runtime.livesql.metadata.StringEntityColumn;
import org.hotrod.runtime.livesql.metadata.Table;
import org.hotrod.runtime.livesql.queries.DeleteWherePhase;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.UpdateSetCompletePhase;
import org.hotrod.runtime.livesql.queries.UpdateSetCompletePhase.Setter;
import org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.TypeSource;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;
import org.hotrod.runtime.livesql.util.QueryAssemblerBean;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.GenUtils;
import org.hotrod.utils.JUtils;
import org.hotrod.utils.SQLUtil;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.Separator;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;

public class DAO {

  private static final Logger log = Logger.getLogger(DAO.class.getName());

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

  private Layout entity = null;
  private Model model = null;

  private String metadataClassName;

  private ClassWriter w;

  private List<String> initializersInPostConstruct = new ArrayList<>();

  // Constructors

  public DAO(final AbstractDAOTag tag, final DataSetMetadata metadata, final JDBCGenerator generator,
      final DAOType type, final JDBCTag myBatisTag, final DatabaseAdapter adapter, final Layout entity,
      final Model model) {
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

    this.entity = entity;
    this.model = model;

    this.fragmentConfig = metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    this.classPackage = this.jdbcTag.getDAOPackage(this.fragmentPackage);
    this.metadataClassName = this.metadata.getId().getJavaClassName() + (this.isTable() ? "Table" : "View");

  }

  public void generate(final FileGenerator fileGenerator, final JDBCGenerator mg)
      throws UncontrolledException, ControlledException {

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

      throw new UncontrolledException(
          "Could not generate DAO primitives class for DAO defined in the <" + this.tag.getTagName() + "> tag in "
              + this.tag.getSourceLocation().render() + ":\n" + "could not write to file '" + f.getName() + "'.",
          e);
    } catch (UnresolvableDataTypeException e) {
      throw new ControlledException(
          "Could not generate DAO primitives class for DAO defined in the <" + this.tag.getTagName() + "> tag in "
              + this.tag.getSourceLocation().render() + ":\n" + "'could not handle columns '"
              + e.getColumnMetadata().getName() + "' type: " + e.getColumnMetadata().getTypeName());
    } catch (SequencesNotSupportedException e) {
      throw new ControlledException("Could not generate DAO primitives class for DAO defined in the <"
          + this.tag.getTagName() + "> tag in " + this.tag.getSourceLocation().render() + ":\n" + e.getMessage());
    }

  }

  private void writeBody(final JDBCGenerator g)
      throws IOException, UnresolvableDataTypeException, ControlledException, SequencesNotSupportedException {

    writeClassHeader();

    if (!this.isExecutor()) {
//
//      writeRowParser();

      writeConverterProperties();

      writeRowReaderProperty();

      writeBaseline();

      if (this.isTable()) {
        writeSelect(false); // by PK
//        writeSelectByUI(mg);
      }
//
      writeSelect(true); // by example
      writeSelectByCriteria();

//      if (this.isTable()) {
//        if (this.generator.isClassicFKNavigationEnabled() || this.isClassicFKNavigationEnabled()) {
//          log.fine("FK navigation");
//          writeSelectParentByFK();
//          writeSelectChildrenByFK();
//        }
//
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

//      }
//
//      if (this.isView()) {
//      }
//
//      if (this.isTable() || this.isView()) {
//        writeDeleteByCriteria();
//      }

//      writeEnumTypeHandlers();
//
      writeOrderBy();

      writeMetadata();

      //
//      if (this.getBundle().getParent() != null) {
//        writeAOPAspect();
//      }
//
    }

//    if (this.tag != null) {

//      log.fine("SQL NAME=" + this.metadata.getId().getCanonicalSQLName() + " this.tag=" + this.tag);
//      for (SequenceMethodTag s : this.tag.getSequences()) {
//        log.fine("s.getName()=" + s.getSequenceId().getRenderedSQLName());
//        writeSelectSequence(s);
//      }

    int i = 0;
    for (QueryMethodTag q : this.tag.getQueries()) {
//      log.info("q.getJavaMethodName()=" + q.getMethod());
      writeNitroQuery(q, i++);
    }

    i = 0;
    for (SelectMethodMetadata s : this.metadata.getSelectsMetadata()) {
      writeNitroSelect(s, i++);
    }

//    }

    writePostConstruct();

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

    w.println("  private static final ", Logger.class, " log = ", Logger.class,
        ".getLogger(" + this.getClassName() + ".class.getName());");
    w.println();

    // Spring properties

    w.println("  @", Const.AUTOWIRED);
    if (!SUtil.isEmpty(this.jdbcTag.getQualifier())) {
      w.println("  @", Const.QUALIFIER, "(\"" + this.jdbcTag.getQualifier() + "\")");
    }
    w.println("  private ", LiveSQLDialect.class, " liveSQLDialect;");
    w.println();

    w.println("  @", Const.AUTOWIRED);
    w.println("  private ", QueryAssemblerBean.class, " assemblerBean;");
    w.println();

    w.println("  private ", QueryAssembler.class, " assembler;");
    w.println();

    w.println("  @", Const.AUTOWIRED);
    w.println("  private ", DataSource.class, " dataSource;");
    w.println();

    w.println("  private ", Const.APPLICATION_CONTEXT, " applicationContext;");
    w.println();

    w.println("  @Override");
    w.println("  public void setApplicationContext(final ", Const.APPLICATION_CONTEXT, " applicationContext) throws ",
        Const.BEANS_EXCEPTION, " {");
    w.println("    this.applicationContext = applicationContext;");
    w.println("  }");
    w.println();

    w.println("  @", Const.AUTOWIRED);
    w.println("  private ", LiveSQL.class, " sql;");
    w.println();

    w.println("  private ", LiveSQLContext.class, " context;");

  }

  private void writePostConstruct() {
    w.println();
    w.println("  @", Const.POST_CONSTRUCT);
    w.println("  public void initializeContext() {");
    w.println("    this.context = new ", LiveSQLContext.class, "(this.liveSQLDialect, this.dataSource, new ",
        TypeSolver.class, "(null, this.liveSQLDialect));");
    w.println("    this.assembler = this.assemblerBean.getAssembler();");
    for (String ini : this.initializersInPostConstruct) {
      w.println("    this." + ini + "();");
    }
    w.println("  }");
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
      this.writeReaderLogic(cm, ordinal);
      ordinal++;
    }

    w.println();
    w.println("      return row;");
    w.println("    }");
    w.println();
    w.println("  };");
  }

  // TODO: Just a marker

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
      w.println("    this." + queryName + " = assembler");

      w.println("      .literaln(\"SELECT\")");
      int coln = this.metadata.getColumns().size();
      int n = 1;
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        String sqlId = cm.getId().getRenderedSQLName();
        w.println("      .literaln(\"  " + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\")");
        n++;
      }

      w.println("      .literaln(\"FROM " + this.metadata.getId().getRenderedSQLName() + "\")");
      if (byExample) {
        fragmentWhereExample("f");
        w.println("      .parameterInjection(\"ordering\")");
      } else {
        fragmentWherePK("f");
      }
      w.println("      .endSelectQuery();");
      w.println("  }");
      w.println();

      // Method

      ExternalClass em = ExternalClass.of(this.model.getFullClassName());
      if (byExample) {
        w.print("  public ", List.class, "<", em, "> select(");
        ExternalClass ob = ExternalClass.of(this.getOrderByClassName());
        w.print(em, " filter, ", ob, "... orderBies");
      } else {
        w.print("  public ", em, " select(");
        fragmentPKParameters(pk);
      }
      w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

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

      w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
      w.println("    context.add(\"f\", filter);");
      if (byExample) {
        w.println("    String ordering = ", SQLUtil.class, ".render(orderBies);");
        w.println("    context.add(\"ordering\", ordering);");
      }
      w.print("    ", PreparedSelectQuery.class, "<", em, "> preparedQuery = ");
      w.println("this." + queryName + ".prepare(context, ", em, ".class);");

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
    w.println("select(final ", ec, " from, final ", GeneralBooleanExpression.class, " predicate) {");
    w.println("    return new ", CriteriaWherePhase.class, "<", em,
        ">(this.context, from, predicate, this.rowReader);");
    w.println("  }");
  }

  private void writeInsert(boolean byExample) throws ControlledException {

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
    w.println("    this." + queryName + " = assembler");

    w.println("      .literaln(\"INSERT INTO ", SUtil.escapeJavaString(this.metadata.getId().getRenderedSQLName()),
        " (\")");
    int coln = this.metadata.getColumns().size();
    int n = 1;
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();

      if (byExample) {
        if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.TIMESTAMP && cm.isOLTimestampColumn()) {
          w.println("      .literaln(\"  " + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\")");
        } else {
          w.println("      .if_(\"m." + SUtil.escapeJavaString(memId) + " != null\", assembler.literal(\""
              + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\\n\").end())");
        }
      } else {

        // Always include column
        if (!cm.belongsToPK()) {
          w.println("      .literaln(\"  " + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\")");
        }
        if (cm.belongsToPK() && cm.getSequenceId() != null) {
          w.println("      .literaln(\"  " + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\")");
        }
        if (cm.belongsToPK() && cm.getSequenceId() == null && cm.getAutogenerationType() == null) {
          w.println("      .literaln(\"  " + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\")");
        }

        // Never include column
        if (cm.belongsToPK() && cm.getAutogenerationType() == AutogenerationType.IDENTITY_ALWAYS) {
          // nothing to do
        }

        // Conditionally include column
        if (cm.belongsToPK() && cm.getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT) {
          w.println("      .if_(\"m." + SUtil.escapeJavaString(memId) + " != null\", assembler.literal(\""
              + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\\n\").end())");
        }

      }

      n++;
    }
    w.println("      .literaln(\")\")");

    if (mechanics.getOutputClause() != null && mechanics.getGeneratedKeysNames() != null
        && mechanics.getGeneratedKeysNames().length > 0) {
      String gkn = mechanics.getGeneratedKeysNames()[0];
      w.println("      .literaln(\"" + mechanics.getOutputClause() + SUtil.escapeJavaString(gkn) + "\")");
    }

    w.println("      .literaln(\"VALUES(\")");

    n = 1;
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String jdbcType = cm.getType().getJDBCShortType();

      if (byExample) {
        if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.TIMESTAMP && cm.isOLTimestampColumn()) {
          w.println("      .literal(\"  " + SUtil.escapeJavaString(this.adapter.currentTimestampSQLExpression()) + "\")"
              + (n < coln ? ".literaln(\",\")" : ""));
        } else {
          w.println("      .if_(\"m." + SUtil.escapeJavaString(memId) + " != null\", assembler.parameter(\"m."
              + SUtil.escapeJavaString(memId) + "\", Types." + jdbcType + ")" + (n < coln ? ".literal(\", \")" : "")
              + ".end())");
        }
      } else {

        // Always include column
        if (!cm.belongsToPK()) {
          if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.TIMESTAMP && cm.isOLTimestampColumn()) {
            w.println("      .literal(\"  " + SUtil.escapeJavaString(this.adapter.currentTimestampSQLExpression())
                + "\")" + (n < coln ? ".literaln(\",\")" : ""));
          } else {
            w.println("      .literal(\"  \").parameter(\"m." + SUtil.escapeJavaString(memId) + "\", ", Types.class,
                "." + jdbcType + ")" + (n < coln ? ".literaln(\",\")" : ""));
          }
        }
        if (cm.belongsToPK() && cm.getSequenceId() != null) {
          if (mechanics.getMode() == PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH) {
            w.println("      .literal(\"  \").parameter(\"m." + SUtil.escapeJavaString(memId) + "\", ", Types.class,
                "." + jdbcType + ")" + (n < coln ? ".literaln(\",\")" : ""));
          } else {
            String si = mechanics.getSequenceInlineSQL();
            w.println(
                "      .literal(\"  " + SUtil.escapeJavaString(si) + "\")" + (n < coln ? ".literaln(\",\")" : ""));
          }
        }
        if (cm.belongsToPK() && cm.getSequenceId() == null && cm.getAutogenerationType() == null) {
          w.println("      .literal(\"  \").parameter(\"m." + SUtil.escapeJavaString(memId) + "\", ", Types.class,
              "." + jdbcType + ")" + (n < coln ? ".literaln(\",\")" : ""));
        }

        // Never include column
        if (cm.belongsToPK() && cm.getAutogenerationType() == AutogenerationType.IDENTITY_ALWAYS) {
          // nothing to do
        }

        // Conditionally include column
        if (cm.belongsToPK() && cm.getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT) {
          w.println("      .if_(\"m." + SUtil.escapeJavaString(memId) + " != null\", assembler.parameter(\"m."
              + SUtil.escapeJavaString(memId) + "\", Types." + jdbcType + ")" + (n < coln ? ".literal(\", \")" : "")
              + ".end())");
        }

      }

      n++;
    }
    w.println("      .literal(\")\")");

    if (mechanics.getMode() == PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH) {
      w.print("      .endInsertQuery(", PrimaryKeyRetrievalMode.class,
          "." + mechanics.getMode() + ", \"" + SUtil.escapeJavaString(mechanics.getSequencePreFetchSQL()) + "\", \"m." //
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
    w.println();
    w.print("  public void " + methodName + "(", em, " model");
    w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

    w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
    w.println("    context.add(\"m\", model);");

    if (ol != null) {
      switch (ol.getStrategy()) {
      case VERSION_NUMBER:
        ColumnMetadata cm = ol.getColumnMetadata();
        String setter = cm.getId().getJavaSetter();
        String zero = renderNumericLiteral(0, cm.getType().getJavaClassName());
        w.println("    model." + setter + "(" + zero + ");");
        break;
      case TIMESTAMP:
        cm = ol.getColumnMetadata();
        setter = cm.getId().getJavaSetter();
        w.println("    model." + setter + "(null);");
        break;
      default: // do not add setter
      }
    }

    w.println("    ", PreparedInsertQuery.class, " preparedQuery = this." + queryName + ".prepare(context);");

    fragmentLogging();

    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    if (mechanics.getMode() == PrimaryKeyRetrievalMode.NO_RETRIEVAL) {
      w.println("      preparedQuery.execute(conn);");
    } else {
      String targetClass = pk.getColumns().get(0).getType().getJavaClassName();
      String pkCast = GenUtils.convertPropertyType(Long.class.getName(), targetClass, "pk");
      w.println("      Long pk = preparedQuery.execute(conn);");
      w.println("      model.setId(" + pkCast + ");");
    }
    w.println("    }");

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
      List<ColumnMetadata> defaults) throws ControlledException {

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
        throw new ControlledException(
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
          throw new ControlledException(e.getMessage());
        }
        if (this.adapter.getInsertIntegration().integratesSequencesKeysResultSet()) {
          if (this.adapter.getInsertIntegration().identitiesMustDeclarePKColumns()) {
            String[] pkcols = this.metadata.getPK().getColumns().stream().map(c -> c.getId().getRenderedSQLName())
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
        throw new ControlledException("HotRod does not support multiple columns generated using sequences: table '"
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
      w.println("    this." + queryName + " = assembler");
      w.println("      .literaln(\"UPDATE " + this.metadata.getId().getRenderedSQLName() + "\")");
      w.println("      .literaln(\"SET\")");

      int coln = this.metadata.getColumns().size();
      int n = 1;
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        String sqlId = cm.getId().getRenderedSQLName();
        if (ol != null && cm.isOLVersionNumberColumn()) {
          w.println("      .literal(\"  " + SUtil.escapeJavaString(sqlId) + " = " + SUtil.escapeJavaString(sqlId)
              + " + 1\")" + (n < coln ? ".literaln(\",\")" : ""));
        } else if (ol != null && cm.isOLTimestampColumn()) {
          w.println("      .literal(\"  " + SUtil.escapeJavaString(sqlId) + " = "
              + SUtil.escapeJavaString(this.adapter.currentTimestampSQLExpression()) + "\")"
              + (n < coln ? ".literaln(\",\")" : ""));
        } else {
          String memId = cm.getId().getJavaMemberName();
          String jdbcType = cm.getType().getJDBCShortType();
          w.println("      .literal(\"  " + SUtil.escapeJavaString(sqlId) + " = \").parameter(\"m."
              + SUtil.escapeJavaString(memId) + "\", Types." + jdbcType + ")" + (n < coln ? ".literaln(\",\")" : ""));
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
      w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

      for (ColumnMetadata cm : pk.getColumns()) {
        String getter = cm.getId().getJavaGetter();
        w.println("    if (model." + getter + "() == null) return 0;");
      }

      w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
      w.println("    context.add(\"m\", model);");
      if (ol != null && ol.getStrategy() == OptimisticLockingStrategy.FULL_ROW_CHECK) {
        w.println("    context.add(\"b\", baseline);");
      }
      w.println("    ", PreparedModificationQuery.class, " preparedQuery = this." + queryName + ".prepare(context);");

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
    w.println("    this." + queryName + " = assembler");
    w.println("      .literal(\"UPDATE " + this.metadata.getId().getRenderedSQLName() + "\")");
    fragmentSet("v");
    fragmentWhereExample("e");
    w.println("      .endModificationQuery();");
    w.println("  }");

    // Method

    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    w.println();
    w.print("  public int update(", em, " example, ", em, " values");
    w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

    w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
    w.println("    context.add(\"e\", example);");
    w.println("    context.add(\"v\", values);");
    w.println("    ", PreparedModificationQuery.class, " preparedQuery = this.updateByExample.prepare(context);");

    fragmentLogging();
    fragmentExecuteModification();

    w.println("  }");

  }

  private void writeUpdateByCriteria() {
    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    ExternalClass ec = ExternalClass.of(this.metadataClassName);
    w.println();
    w.println("  // UPDATE BY CRITERIA");
    w.println();
    w.print("  public ", UpdateSetCompletePhase.class, " update(final ", em, " values, ");
    w.println("final ", ec, " tableOrView,");
    w.println("      final ", GeneralBooleanExpression.class, " predicate) {");
    w.print("    ", List.class, "<", Setter.class, "> setters");
    w.println(" = new ", ArrayList.class, "<>();");

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String getter = cm.getId().getJavaGetter();
      String memId = cm.getId().getJavaMemberName();
      w.println("    if (values." + getter + "() != null) setters.add(new ", Setter.class,
          "(tableOrView." + memId + ", sql.val(values." + getter + "())));");
    }

    w.println("    return new ", UpdateSetCompletePhase.class, "(this.context, tableOrView, setters, predicate);");
    w.println("  }");
  }

  // TODO: Just a marker

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
      w.println("    this." + queryName + " = assembler");
      w.println("      .literaln(\"DELETE FROM " + this.metadata.getId().getRenderedSQLName() + "\")");

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

      // TODO
      ExternalClass em = ExternalClass.of(this.model.getFullClassName());

      w.println();
      w.print("  public int delete(");
      if (optimisticLocking) {
        w.print(this.getBaselineClass(), " baseline");
        w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");
        for (ColumnMetadata cm : pk.getColumns()) {
          String getter = cm.getId().getJavaGetter();
          w.println("    if (baseline." + getter + "() == null) return 0;");
        }
        if (!ol.getStrategy().usesAllColumns()) {
          ColumnMetadata cm = ol.getColumnMetadata();
          String getter = cm.getId().getJavaGetter();
          w.println("    if (baseline." + getter + "() == null) return 0;");
        }
        w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
        w.println("    context.add(\"b\", baseline);");
      } else {
        fragmentPKParameters(pk);
        w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");
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
        w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
        w.println("    context.add(\"f\", filter);");
      }

      w.println("    ", PreparedModificationQuery.class, " preparedQuery = this." + queryName + ".prepare(context);");

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
    w.println("    this." + queryName + " = assembler");
    w.println("      .literal(\"DELETE FROM " + this.metadata.getId().getRenderedSQLName() + "\")");
    fragmentWhereExample("e");
    w.println("      .endModificationQuery();");
    w.println("  }");

    // Method

    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    w.println();
    w.print("  public int delete(", em, " example");
    w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

    w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
    w.println("    context.add(\"e\", example);");
    w.println("    ", PreparedModificationQuery.class, " preparedQuery = this.deleteByExample.prepare(context);");

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
    w.println(" delete(final ", ec, " from, final ", GeneralBooleanExpression.class, " predicate) {");
    w.println("    return new ", DeleteWherePhase.class, "(this.context, from, predicate);");
    w.println("  }");
  }

  private void writeMetadata() throws IOException {

    Class<?> type = this.isTable() ? Table.class : View.class;
    String typeName = type.getSimpleName();

    Id catalog = this.metadata.getId().getCatalog();
    Id schema = this.metadata.getId().getSchema();
    Id name = this.metadata.getId().getObject();

    ExternalClass pc = ExternalClass.of(type);
//    log.info(">>> this.metadataClassName=" + this.metadataClassName);
    ExternalClass ec = ExternalClass.of(this.metadataClassName);

    w.println();
    w.println("  // Database " + type + " metadata");
    w.println();
    w.println("  public ", ec, " new", pc, "() {");
    w.println("    return new ", ec, "();");
    w.println("  }");
    w.println();
    w.println("  public ", ec, " new", pc, "(final String alias) {");
    w.println("    return new ", ec, "(alias);");
    w.println("  }");
    w.println();
    w.println("  public static class ", ec, " extends ", pc, " {");

    w.println();
    w.println("    // Properties");
    w.println();
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      Class<?> liveSQLColumnType = toLiveSQLType(javaType);
      String javaMembername = cm.getId().getJavaMemberName();
      String colName = cm.getId().getCanonicalSQLName();
      String property = cm.getId().getJavaMemberName();
      String javaConverterClass = null;
      String rawClass = null;
      if (cm.getConverter() != null) {
        javaConverterClass = cm.getConverter().getJavaClass();
        rawClass = cm.getConverter().getJavaRawType();
      }

      ExternalClass jt = ExternalClass.of(javaType);
      ExternalClass lt = ExternalClass.of(liveSQLColumnType);

      w.println("    public final ", lt, " " + javaMembername + " = new ", lt, "(this,");
      w.print("      " //
          + "\"" + JUtils.escapeJavaString(colName) + "\"" //
          + ", \"" + JUtils.escapeJavaString(property) + "\"" //
          + ", \"" + JUtils.escapeJavaString(cm.getTypeName()) + "\"" //
          + ", " + cm.getPrecision() //
          + ", " + cm.getScale() //
          + ", ");

      ExternalClass th = ExternalClass.of(TypeHandler.class);
      if (rawClass != null && javaConverterClass != null) {
        ExternalClass cvt = ExternalClass.of(javaConverterClass);
        w.print(th, ".of(", cvt, ".class, ");
        w.print(TypeSource.class, ".ENTITY_COLUMN)");
      } else {
        w.print(th, ".of(", jt, ".class, ");
        w.print(TypeSource.class, ".ENTITY_COLUMN)");
      }
      w.println(");");

    }
    w.println();

    w.println("    // Getters");
    w.println();

    ExternalClass ac = ExternalClass.of(AllColumns.class);

    w.println("    public ", ac, " star() {");
    w.println("      return new ", ac, "(" + this.metadata.getColumns().stream()
        .map(c -> "this." + c.getId().getJavaMemberName()).collect(Collectors.joining(", ")) + ");");
    w.println("    }");
    w.println();

    ExternalClass nm = ExternalClass.of(Name.class);
    w.println("    // Constructors");
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
    w.println(", \"" + typeName + "\", null);");
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
    w.println(", \"" + typeName + "\", alias);");
    w.println("      initialize();");
    w.println("    }");
    w.println();

    w.println("    // Initialization");
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
    if ("java.lang.Byte".equals(javaType) //
        || "java.lang.Short".equals(javaType) //
        || "java.lang.Integer".equals(javaType) //
        || "java.lang.Long".equals(javaType) //
        || "java.lang.Float".equals(javaType) //
        || "java.lang.Double".equals(javaType) //
        || "java.math.BigInteger".equals(javaType) //
        || "java.math.BigDecimal".equals(javaType) //
    ) {
      return NumberEntityColumn.class;
    } else if ("java.lang.String".equals(javaType)) {
      return StringEntityColumn.class;
    } else if ("java.util.Date".equals(javaType) //
        || "java.sql.Date".equals(javaType) //
        || "java.sql.Timestamp".equals(javaType) //
        || "java.sql.Time".equals(javaType) //
        || "java.time.LocalDateTime".equals(javaType) //
        || "java.sql.LocalDate".equals(javaType) //
        || "java.sql.LocalTime".equals(javaType) //
        || "java.time.ZonedDateTime".equals(javaType) //
        || "java.time.OffsetDateTime".equals(javaType) //
        || "java.time.OffsetTime".equals(javaType) //
        || "java.time.Instant".equals(javaType) //
    ) {
      return DateTimeEntityColumn.class;
    } else if ("java.lang.Boolean".equals(javaType)) {
      return BooleanEntityColumn.class;
    } else if ("byte[]".equals(javaType)) {
      return ByteArrayEntityColumn.class;
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
    w.println("    public String getSQLColumnName() {");
    w.println("      return this.sqlColumnName;");
    w.println("    }");
    w.println();
    w.println("    public boolean isAscending() {");
    w.println("      return this.ascending;");
    w.println("    }");
    w.println();
    w.println("  }");

  }

  private void fragmentSet(String ns) {
    OptimisticLockingMetadata ol = this.metadata.getOptimisticLocking();
    w.println("      .set(assembler.ifs()");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String sqlId = cm.getId().getRenderedSQLName();
      if (ol != null && cm.isOLVersionNumberColumn()) {
        w.println("        .if_(\"true\", assembler.literal(\"" + ns + "." + SUtil.escapeJavaString(sqlId) + " = "
            + SUtil.escapeJavaString(sqlId) + " + 1\").end())");
      } else if (ol != null && cm.isOLTimestampColumn()) {
        w.println("        .if_(\"true\", assembler.literal(\"" + ns + "." + SUtil.escapeJavaString(sqlId) + " = "
            + SUtil.escapeJavaString(this.adapter.currentTimestampSQLExpression()) + "\").end())");
      } else {
        String memId = cm.getId().getJavaMemberName();
        String jdbcType = cm.getType().getJDBCShortType();
        w.println("        .if_(\"" + ns + "." + SUtil.escapeJavaString(memId) + " != null\", assembler.literal(\""
            + SUtil.escapeJavaString(sqlId) + " = \").parameter(\"" + ns + "." + SUtil.escapeJavaString(memId)
            + "\", Types." + jdbcType + ").end())");
      }
    }
    w.println("        .end())");

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
    Separator sep = Separator.of("\nWHERE ", "  AND ");
    for (ColumnMetadata cm : pk.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String jdbcType = cm.getType().getJDBCShortType();
      w.println(
          "      .literaln(\"" + SUtil.escapeJavaString(sep.render()) + "\" + \"" + SUtil.escapeJavaString(sqlId)
              + " = \").parameter(\"" + ns + "." + SUtil.escapeJavaString(memId) + "\", ",
          Types.class, "." + jdbcType + ")");
    }
  }

  private void fragmentWhereFullRow(String ns) {
    Separator sep = Separator.of("WHERE ", "  AND ");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String jdbcType = cm.getType().getJDBCShortType();
      w.println(
          "      .literaln(\"" + SUtil.escapeJavaString(sep.render()) + "\" + \"" + SUtil.escapeJavaString(sqlId)
              + " = \").parameter(\"" + ns + "." + SUtil.escapeJavaString(memId) + "\", ",
          Types.class, "." + jdbcType + ")");
    }
  }

  private void fragmentWhereExample(String ns) {
    w.println("      .where(\"AND\", assembler.ifs()");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String jdbcType = cm.getType().getJDBCShortType();
      w.println("        .if_(\"" + ns + "." + memId + " != null\", assembler.literal(\""
          + SUtil.escapeJavaString(sqlId) + " = \").parameter(\"" + ns + "." + memId + "\", ", Types.class,
          "." + jdbcType + ").end())");
    }
    w.println("        .end())");
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
    w.println("    }");
  }

  private void fragmentExecuteSelect(ExternalClass m, boolean singleRow) {
    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    w.println("      ", List.class, "<", m, "> rows = preparedQuery.execute(conn, this.rowReader);");

    if (singleRow) {
      w.println("      if (rows.size() == 0) return null;");
      w.println("      if (rows.size() == 1) return rows.get(0);");
      w.println(
          "      throw new RuntimeException(\"A single row at most was expected but received \" + rows.size() + \" rows.\");");
    } else {
      w.println("      return rows;");
    }
    w.println("    }");
  }

  private void writeClassFooter() throws IOException {
    w.println();
    w.println("  private void logQuery(", PreparedQuery.class, " preparedQuery) {");
    w.println("    if (log.isLoggable(", Level.class, ".FINER)) {");
    w.println("      log.finer(\"SQL: \" + preparedQuery.getPreview(true));");
    w.println("    } else if (log.isLoggable(", Level.class, ".FINE)) {");
    w.println("      log.fine(\"SQL: \" + preparedQuery.getPreview());");
    w.println("    }");
    w.println("  }");
    w.println();
    w.println("}");
  }

  private LinkedHashMap<String, String> converterProperties = new LinkedHashMap<>();

  private void writeConverterProperties() {
    int n = 0;
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      ConverterTag ct = cm.getConverter();
      if (ct != null) {
        boolean found = this.converterProperties.containsKey(ct.getName());
        if (!found) {
          if (n == 0) {
            w.println();
            w.println("  // CONVERTERS");
            w.println();
          }
          String property = "converter" + (n++);
          this.converterProperties.put(ct.getName(), property);
          ExternalClass cc = ExternalClass.of(ct.getJavaClass());
          w.println("  private final ", cc, " " + property + " = new ", cc, "();");
        }
      }
    }
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
    JDBC_GETTERS.put("java.lang.Short", JDBCGetter.prim("getShort"));
    JDBC_GETTERS.put("java.sql.SQLXML", JDBCGetter.obj("getSQLXML"));
    JDBC_GETTERS.put("java.lang.String", JDBCGetter.obj("getString"));
    JDBC_GETTERS.put("java.sql.Time", JDBCGetter.obj("getTime"));
    JDBC_GETTERS.put("java.sql.Timestamp", JDBCGetter.obj("getTimestamp"));
  }

  private void writeReaderLogic(ColumnMetadata cm, int ordinal) {
    w.println();
    String setter = cm.getId().getJavaSetter();
    String javaClass = cm.getType().getJavaClassName();
    ConverterTag ct = cm.getConverter();
    String cn = cm.getId().getCanonicalSQLName();

    if (ct == null) { // No converter
      JDBCGetter g = JDBC_GETTERS.get(javaClass);
      String var = "col" + ordinal;
      ExternalClass jc = ExternalClass.of(javaClass);
      if (g != null) {
        if (g.returnsPrimitiveType()) {
          w.println("      ", jc, " " + var + " = rs." + g.getResultSetMethod() + "(" + ordinal + "); // " + cn);
          w.println("      if (rs.wasNull()) " + var + " = null;");
          w.println("      row." + setter + "(" + var + ");");
        } else {
          w.println("      ", jc, " " + var + " = rs." + g.getResultSetMethod() + "(" + ordinal + "); // " + cn);
          w.println("      row." + setter + "(" + var + ");");
        }
      } else {
        w.println("      ", jc, " " + var + " = rs.getObject(" + ordinal + ", ", javaClass, ".class); // " + cn);
        w.println("      row." + setter + "(" + var + ");");
      }
    } else { // Converter specified
      String raw = "raw" + ordinal;
      String var = "col" + ordinal;
      String rawClass = ct.getJavaRawType();
      ExternalClass rc = ExternalClass.of(rawClass);
      JDBCGetter g = JDBC_GETTERS.get(rawClass);
      javaClass = ct.getJavaType();
      ExternalClass mc = ExternalClass.of(javaClass);
      String property = this.converterProperties.get(ct.getName());
      if (g != null) {
        if (g.returnsPrimitiveType()) {
          w.println("      ", rc, " " + raw + " = rs." + g.getResultSetMethod() + "(" + ordinal + "); // " + cn);
          w.println("      if (rs.wasNull()) " + raw + " = null;");
          w.println("      ", mc, " " + var + " = " + property + ".decode(" + raw + ", conn);");
          w.println("      row." + setter + "(" + var + ");");
        } else {
          w.println("      ", rc, " " + raw + " = rs." + g.getResultSetMethod() + "(" + ordinal + "); // " + cn);
          w.println("      ", mc, " " + var + " = " + property + ".decode(" + raw + ", conn);");
          w.println("      row." + setter + "(" + var + ");");
        }
      } else {
        w.println("      ", rc, " " + raw + " = rs.getObject(" + ordinal + ", ", rc, ".class); // " + cn);
        w.println("      ", mc, " " + var + " = " + property + ".decode(" + raw + ", conn);");
        w.println("      row." + setter + "(" + var + ");");
      }
    }

  }

  private void writeNitroQuery(QueryMethodTag q, int n) throws ControlledException {

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
    w.println("    this." + queryName + " = assembler");

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
//      log.info(">> parameter '" + p.getName() + "'");
      ExternalClass pc = ExternalClass.of(p.getJavaType());
      w.print(sep.render(), pc, " " + p.getName());
    }
    w.println(")");
    w.println("      throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");
    w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
    for (ParameterTag p : q.getParameterDefinitions()) {
      w.println("    context.add(\"" + p.getName() + "\", " + p.getName() + ");");
    }
    w.println("    ", PreparedModificationQuery.class, " preparedQuery = this." + queryName + ".prepare(context);");

    fragmentLogging();
    fragmentExecuteModification();

    w.println("  }");

  }

  private void writeNitroSelect(SelectMethodMetadata s, int sno) throws ControlledException {

//    log.info("Nitro SELECT 1");

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
    w.println("    this." + queryName + " = assembler");

    List<EnhancedSQLPart> parts = s.getParts();
    NitroRenderer r = new NitroRenderer();
    r.renderSelect(parts, w);

    w.println("      .endSelectQuery();");
    w.println("  }");
    w.println();

    // 2. Row Reader

    String rowReaderName;
    if (this.isExecutor()) {
      rowReaderName = "rowReader" + sno;
      this.writeNitroSelectRowReaderProperty(s, rowReaderName);
    } else {
      rowReaderName = "rowReader";
    }

    // 3. Method

    SelectMethodReturnType rt = s.getReturnType(this.classPackage);
    ExternalClass rc = ExternalClass.of(rt.getBaseReturnVOFullClassName());

    w.print("  public ", List.class, "<", rc, "> " + method + "(");
    Separator sep = new Separator(", ");
    for (SelectParameterMetadata sp : s.getParameters()) {
      ParameterTag p = sp.getParameter();
      ExternalClass pc = ExternalClass.of(p.getJavaType());
      w.print(sep.render(), pc, " " + p.getName());
    }
    w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

    w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
    for (SelectParameterMetadata sp : s.getParameters()) {
      ParameterTag p = sp.getParameter();
      w.println("    context.add(\"" + p.getName() + "\", " + p.getName() + ");");
    }

    w.print("    ", PreparedSelectQuery.class, "<", rc, "> preparedQuery = ");
    w.println("this." + queryName + ".prepare(context, ", rc, ".class);");

    fragmentLogging();

    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    w.println("      ", List.class, "<", rc, "> rows = preparedQuery.execute(conn, this." + rowReaderName + ");");
    w.println("      return rows;");
    w.println("    }");

    w.println("  }");

  }

  private void writeNitroSelectRowReaderProperty(SelectMethodMetadata s, String rowReaderName) {
    SelectMethodReturnType rt = s.getReturnType(this.classPackage);
    ExternalClass m = ExternalClass.of(rt.getBaseReturnVOFullClassName());
    rt.getSoloVO();

    w.println();
    w.print("  private final ", RowReader.class, "<", m, "> " + rowReaderName + " = new ");
    w.println(RowReader.class, "<", m, ">() {");
    w.println();
    w.println("    @", Override.class);
    w.print("    public ", m, " readRowFrom(", ResultSet.class, " rs, ");
    w.print(Connection.class, " conn) ");
    w.println("throws ", SQLException.class, " {");
    w.println("      ", m, " row = applicationContext.getBean(", m, ".class);");

    int ordinal = 1;
    for (ColumnMetadata cm : s.getColumns()) {
      this.writeReaderLogic(cm, ordinal);
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
