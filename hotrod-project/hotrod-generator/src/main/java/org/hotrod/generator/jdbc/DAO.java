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

import javax.sql.DataSource;

import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.Constants;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.EnhancedSQLPart;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.JDBCTag;
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
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.interfaces.OrderBy;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodMetadata.SelectMethodReturnType;
import org.hotrod.metadata.SelectParameterMetadata;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;
import org.hotrod.runtime.livesql.util.QueryAssemblerBean;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.GenUtils;
import org.hotrod.utils.SQLUtil;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.Separator;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;

public class DAO {

  private static final Logger log = Logger.getLogger(DAO.class.getName());

  // Properties

  private AbstractDAOTag tag;

  private DataSetMetadata metadata;
  private DataSetLayout layout;
  private JDBCGenerator generator;
  private DAOType daoType;
  private JDBCTag myBatisTag;
  private DatabaseAdapter adapter;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  private Entity entity = null;
  private Model model = null;

  private String metadataClassName;

  private ClassWriter w;

  private List<String> initializersInPostConstruct = new ArrayList<>();

  // Constructors

  public DAO(final AbstractDAOTag tag, final DataSetMetadata metadata, final DataSetLayout layout,
      final JDBCGenerator generator, final DAOType type, final JDBCTag myBatisTag, final DatabaseAdapter adapter,
      final Entity entity, final Model model) {
    super();
    this.tag = tag;
    this.metadata = metadata;
    this.layout = layout;
    this.generator = generator;
    if (type == null) {
      throw new RuntimeException("DAOType cannot be null.");
    }
    this.daoType = type;
    this.myBatisTag = myBatisTag;
    this.adapter = adapter;

    this.entity = entity;
    this.model = model;

    this.fragmentConfig = metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    this.classPackage = this.layout.getDAOPrimitivePackage(this.fragmentPackage);
    this.metadataClassName = this.metadata.getId().getJavaClassName() + (this.isTable() ? "Table" : "View");

  }

  public void generate(final FileGenerator fileGenerator, final JDBCGenerator mg)
      throws UncontrolledException, ControlledException {

    String className = this.getClassName() + ".java";

    File dir = this.layout.getDaoPrimitivePackageDir(this.fragmentPackage);
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

      if (this.isTable()) {
        writeSelect(false); // by PK
//        writeSelectByUI(mg);
      }
//
      writeSelect(true); // by example
//      writeSelectByCriteria();
//
//      if (this.isTable()) {
//        if (this.generator.isClassicFKNavigationEnabled() || this.isClassicFKNavigationEnabled()) {
//          log.fine("FK navigation");
//          writeSelectParentByFK();
//          writeSelectChildrenByFK();
//        }
//
      writeInsert(false); // standard INSERT
      writeInsert(true); // INSERT by example

      writeUpdateByPK();
      writeUpdateByExample();
//
      writeDeleteByPK();
      writeDeleteByExample();
//      }
//
//      if (this.isView()) {
//      }
//
//      if (this.isTable() || this.isView()) {
//        writeUpdateByCriteria();
//
//        writeDeleteByCriteria();
//      }
//
//      writeEnumTypeHandlers();
//
//      writeMetadata();
      writeOrderBy();
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
      log.info("q.getJavaMethodName()=" + q.getMethod());
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

//    Map<String, String> daoMembers = new HashMap<String, String>();

    w.println("  @", Const.AUTOWIRED);
    if (!SUtil.isEmpty(this.layout.getLiveSQLDialectBeanQualifier())) {
      w.println("  @", Const.QUALIFIER, "(\"" + this.layout.getLiveSQLDialectBeanQualifier() + "\")");
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
//    w.println("    this.sqlSession.getConfiguration().setObjectFactory(this.springBeanObjectFactory);");
    w.println("  }");
    w.println();

    w.println("  @SuppressWarnings(\"unused\")");
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

    InsertMechanics mechanics = computeInsertMechanics(sequences, identities, defaults);

    log.info("mechanics: " + mechanics);

    // Query

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
        w.println("      .if_(\"m." + SUtil.escapeJavaString(memId) + " != null\", assembler.literal(\""
            + SUtil.escapeJavaString(sqlId) + (n < coln ? "," : "") + "\\n\").end())");
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
        w.println("      .if_(\"m." + SUtil.escapeJavaString(memId) + " != null\", assembler.parameter(\"m."
            + SUtil.escapeJavaString(memId) + "\", Types." + jdbcType + ")" + (n < coln ? ".literal(\", \")" : "")
            + ".end())");
      } else {

        // Always include column
        if (!cm.belongsToPK()) {
          w.println("      .literal(\"  \").parameter(\"m." + SUtil.escapeJavaString(memId) + "\", ", Types.class,
              "." + jdbcType + ")" + (n < coln ? ".literaln(\",\")" : ""));
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
    w.print("  public void " + methodName + "(", em, " m");
    w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

    w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
    w.println("    context.add(\"m\", m);");
    w.println("    ", PreparedInsertQuery.class, " preparedQuery = this." + queryName + ".prepare(context);");

    fragmentLogging();

    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    if (mechanics.getMode() == PrimaryKeyRetrievalMode.NO_RETRIEVAL) {
      w.println("      preparedQuery.execute(conn);");
    } else {
      String targetClass = pk.getColumns().get(0).getType().getJavaClassName();
      String pkCast = GenUtils.convertPropertyType(Long.class.getName(), targetClass, "pk");
      w.println("      Long pk = preparedQuery.execute(conn);");
      w.println("      m.setId(" + pkCast + ");");
    }
    w.println("    }");

    w.println("  }");

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

  private void writeUpdateByPK() {

    KeyMetadata pk = this.metadata.getPK();

    if (pk == null) {
      w.println();
      w.println("  // UPDATE BY PRIMARY KEY -- Not available since the table does not have a primary key.");
    } else {

      w.println();
      w.println("  // UPDATE BY PRIMARY KEY");

      // Query

      String queryName = "updateByPK";
      String initializerName = "initialize" + SUtil.capitalize(queryName);
      this.initializersInPostConstruct.add(initializerName);

      w.println();
      w.println("  private ", DynamicModificationQuery.class, " " + queryName + ";");
      w.println();
      w.println("  private void " + initializerName + "() {");
      w.println("    this." + queryName + " = assembler");
      w.println("      .literal(\"UPDATE " + this.metadata.getId().getRenderedSQLName() + "\")");
      fragmentSet();
      fragmentWherePK("m");
      w.println("    .endModificationQuery();");
      w.println("  }");

      // Method

      ExternalClass em = ExternalClass.of(this.model.getFullClassName());
      w.println();
      w.print("  public int update(", em, " m");
      w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

      for (ColumnMetadata cm : pk.getColumns()) {
        String getter = cm.getId().getJavaGetter();
        w.println("    if (m." + getter + "() == null) return 0;");
      }

      w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
      w.println("    context.add(\"m\", m);");
      w.println("    ", PreparedModificationQuery.class, " preparedQuery = this.updateByPK.prepare(context);");

      fragmentLogging();
      fragmentExecuteModification();

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
    w.println("      .literal(\"UPDATE FROM " + this.metadata.getId().getRenderedSQLName() + "\")");
    fragmentSet();
    fragmentWhereExample("f");
    w.println("      .endModificationQuery();");
    w.println("  }");

    // Method

    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    w.println();
    w.print("  public int update(", em, " filter, ", em, " updateValues");
    w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

    w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
    w.println("    context.add(\"f\", filter);");
    w.println("    context.add(\"u\", updateValues);");
    w.println("    ", PreparedModificationQuery.class, " preparedQuery = this.updateByExample.prepare(context);");

    fragmentLogging();
    fragmentExecuteModification();

    w.println("  }");

  }

  private void writeDeleteByPK() {

    KeyMetadata pk = this.metadata.getPK();

    if (pk == null) {
      w.println();
      w.println("  // DELETE BY PRIMARY KEY -- Not available since the table does not have a primary key.");
    } else {

      w.println();
      w.println("  // DELETE BY PRIMARY KEY");

      // Query

      String queryName = "deleteByPK";
      String initializerName = "initialize" + SUtil.capitalize(queryName);
      this.initializersInPostConstruct.add(initializerName);

      w.println();
      w.println("  private ", DynamicModificationQuery.class, " " + queryName + ";");
      w.println();
      w.println("  private void " + initializerName + "() {");
      w.println("    this." + queryName + " = assembler");
      w.println("      .literaln(\"DELETE FROM " + this.metadata.getId().getRenderedSQLName() + "\")");
      fragmentWherePK("f");
      w.println("      .endModificationQuery();");
      w.println("  }");

      // Method

      w.println();
      w.print("  public int delete(");
      fragmentPKParameters(pk);
      w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

      for (ColumnMetadata cm : pk.getColumns()) {
        String m = cm.getId().getJavaMemberName();
        w.println("    if (" + m + " == null) return 0;");
      }

      ExternalClass em = ExternalClass.of(this.model.getFullClassName());
      w.println("    ", em, " filter = new ", em, "();");
      for (ColumnMetadata cm : pk.getColumns()) {
        String m = cm.getId().getJavaMemberName();
        String setter = cm.getId().getJavaSetter();
        w.println("    filter." + setter + "(" + m + ");");
      }

      w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
      w.println("    context.add(\"f\", filter);");
      w.println("    ", PreparedModificationQuery.class, " preparedQuery = this.deleteByPK.prepare(context);");

      fragmentLogging();
      fragmentExecuteModification();

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
    fragmentWhereExample("f");
    w.println("      .endModificationQuery();");
    w.println("  }");

    // Method

    ExternalClass em = ExternalClass.of(this.model.getFullClassName());
    w.println();
    w.print("  public int delete(", em, " filter");
    w.println(") throws ", DynamicExpressionException.class, ", ", SQLException.class, " {");

    w.println("    ", ParameterContext.class, " context = this.assembler.newParameterContext();");
    w.println("    context.add(\"f\", filter);");
    w.println("    ", PreparedModificationQuery.class, " preparedQuery = this.deleteByExample.prepare(context);");

    fragmentLogging();
    fragmentExecuteModification();

    w.println("  }");

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

  private void fragmentSet() {
    w.println("      .set(assembler.ifs()");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String jdbcType = cm.getType().getJDBCShortType();
      w.println("        .if_(\"m." + SUtil.escapeJavaString(memId) + " != null\", assembler.literal(\""
          + SUtil.escapeJavaString(sqlId) + " = \").parameter(\"m." + SUtil.escapeJavaString(memId) + "\", Types."
          + jdbcType + ").end())");
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

  private void fragmentWherePK(String objname) {
    KeyMetadata pk = this.metadata.getPK();
    Separator sep = Separator.of("WHERE ", "  AND ");
    for (ColumnMetadata cm : pk.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String jdbcType = cm.getType().getJDBCShortType();
      w.println(
          "      .literal(\"" + SUtil.escapeJavaString(sep.render()) + "\" + \"" + SUtil.escapeJavaString(sqlId)
              + " = \").parameter(\"" + objname + "." + SUtil.escapeJavaString(memId) + "\", ",
          Types.class, "." + jdbcType + ")");
    }
  }

  private void fragmentWhereExample(String objname) {
    w.println("      .where(\"AND\", assembler.ifs()");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String memId = cm.getId().getJavaMemberName();
      String sqlId = cm.getId().getRenderedSQLName();
      String jdbcType = cm.getType().getJDBCShortType();
      w.println("        .if_(\"" + objname + "." + memId + " != null\", assembler.literal(\""
          + SUtil.escapeJavaString(sqlId) + " = \").parameter(\"" + objname + "." + memId + "\", ", Types.class,
          "." + jdbcType + ").end())");
    }
    w.println("        .end())");
  }

  private void fragmentLogging() {
    w.println("    logQuery(preparedQuery);");
  }

  private void fragmentExecuteModification() {
    w.println("    try (", Connection.class, " conn = this.dataSource.getConnection()) {");
    w.println("      int rows = preparedQuery.execute(conn);");
    w.println("      return rows;");
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
      log.info(">> parameter '" + p.getName() + "'");
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

    log.info("Nitro SELECT 1");

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
      log.info(">> parameter '" + p.getName() + "'");
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

  public boolean isExecutor() {
    return this.daoType == DAOType.EXECUTOR;
  }

  public String getClassName() {
    return this.myBatisTag.getDaos().generateDAOName(this.metadata.getId());
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
