package org.hotrod.generator.mybatisspring;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.Constants;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.EnumTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisSpringTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectMethodTag.ResultSetMode;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.StaleDataException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.GeneratableObject;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.identifiers.Id;
import org.hotrod.interfaces.DaoForUpdate;
import org.hotrod.interfaces.DaoWithOrder;
import org.hotrod.interfaces.OrderBy;
import org.hotrod.interfaces.UpdateByExampleDao;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.ForeignKeyMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodMetadata.SelectMethodReturnType;
import org.hotrod.metadata.SelectParameterMetadata;
import org.hotrod.metadata.VersionControlMetadata;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.metadata.AllColumns;
import org.hotrod.runtime.livesql.metadata.BooleanEntityColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayEntityColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.runtime.livesql.metadata.NumberEntityColumn;
import org.hotrod.runtime.livesql.metadata.ObjectEntityColumn;
import org.hotrod.runtime.livesql.metadata.StringEntityColumn;
import org.hotrod.runtime.livesql.queries.DeleteWherePhase;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.UpdateSetCompletePhase;
import org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;
import org.hotrod.runtime.livesql.util.CastUtil;
import org.hotrod.spring.LazyParentClassLoading;
import org.hotrod.spring.SpringBeanObjectFactory;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.AbstractClassWriter.ExternalClass;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.GenUtils;
import org.hotrod.utils.JUtils;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.ValueTypeFactory;
import org.hotrod.utils.ValueTypeFactory.ValueTypeManager;
import org.nocrala.tools.database.tartarus.core.JdbcForeignKey;
import org.nocrala.tools.database.tartarus.core.JdbcKey;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

import ognl.TypeConverter;

public class ObjectDAO extends GeneratableObject {

  // Constants

  private static final Logger log = Logger.getLogger(ObjectDAO.class.getName());

  // Properties

  private AbstractDAOTag tag;

  private DataSetMetadata metadata;
  private DataSetLayout layout;
  private MyBatisSpringGenerator generator;
  private DAOType daoType;
  private MyBatisSpringTag myBatisTag;
  private DatabaseAdapter adapter;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  private ObjectAbstractVO avo = null;
  private ObjectVO vo = null;
  private Mapper mapper = null;

  private String metadataClassName;

  private Map<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>> fkSelectors;
  private Map<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>> efkSelectors;

  private ClassWriter w;

  // Constructors

  public ObjectDAO(final AbstractDAOTag tag, final DataSetMetadata metadata, final DataSetLayout layout,
      final MyBatisSpringGenerator generator, final DAOType type, final MyBatisSpringTag myBatisTag,
      final DatabaseAdapter adapter, final ObjectAbstractVO avo, final ObjectVO vo, final Mapper mapper) {
    super();
    log.fine("init");
    this.tag = tag;
    this.metadata = metadata;
    this.layout = layout;
    this.generator = generator;
    if (type == null) {
      throw new RuntimeException("DAOType cannot be null.");
    }
    metadata.getDaoTag().addGeneratableObject(this);
    this.daoType = type;
    this.myBatisTag = myBatisTag;
    this.adapter = adapter;

    this.avo = avo;
    this.vo = vo;
    this.mapper = mapper;

    this.fragmentConfig = metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    this.classPackage = this.layout.getDAOPrimitivePackage(this.fragmentPackage);
    this.metadataClassName = this.metadata.getId().getJavaClassName() + (this.isTable() ? "Table" : "View");

    this.fkSelectors = compileDistinctFKs(this.metadata.getImportedFKs());
    this.efkSelectors = compileDistinctFKs(this.metadata.getExportedFKs());
  }

  // Behavior

  public boolean isTable() {
    return this.daoType == DAOType.TABLE;
  }

  public boolean isView() {
    return this.daoType == DAOType.VIEW;
  }

  public boolean isExecutor() {
    return this.daoType == DAOType.EXECUTOR;
  }

  public boolean isClassicFKNavigationEnabled() {
    return this.metadata.getClassicFKNavigation() != null;
  }

  public void generate(final FileGenerator fileGenerator, final MyBatisSpringGenerator mg)
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

      super.markGenerated();

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

  private void writeBody(final MyBatisSpringGenerator mg)
      throws IOException, UnresolvableDataTypeException, ControlledException, SequencesNotSupportedException {

    writeClassHeader();

    if (!this.isExecutor()) {

      writeRowParser();

      if (this.isTable()) {
        writeSelectByPK(mg);
        writeSelectByUI(mg);
      }

      writeSelectByExample();
      writeSelectByCriteria();

      if (this.isTable()) {
        if (this.generator.isClassicFKNavigationEnabled() || this.isClassicFKNavigationEnabled()) {
          log.fine("FK navigation");
          writeSelectParentByFK();
          writeSelectChildrenByFK();
        }

        writeInsert();

        writeUpdateByPK(mg);

        writeDeleteByPK(mg);
      }

      if (this.isView()) {
        writeInsertByExample();
      }

      if (this.isTable() || this.isView()) {
        writeUpdateByExample();
        writeUpdateByCriteria();

        writeDeleteByExample();
        writeDeleteByCriteria();
      }

      writeEnumTypeHandlers();

      writeOrderingEnum();

      writeMetadata();

      if (this.getBundle().getParent() != null) {
        writeAOPAspect();
      }

    }

    writeConverters();

    if (this.tag != null) {

      log.fine("SQL NAME=" + this.metadata.getId().getCanonicalSQLName() + " this.tag=" + this.tag);
      for (SequenceMethodTag s : this.tag.getSequences()) {
        log.fine("s.getName()=" + s.getSequenceId().getRenderedSQLName());
        writeSelectSequence(s);
      }

      for (QueryMethodTag q : this.tag.getQueries()) {
        log.fine("q.getJavaMethodName()=" + q.getMethod());
        writeQuery(q);
      }

      for (SelectMethodMetadata s : this.metadata.getSelectsMetadata()) {
        writeSelect(s);
      }

    }

    writeClassFooter();
  }

  private void writeClassHeader() throws IOException {

//    // Imports
//
//    ImportsRenderer imports = new ImportsRenderer();
//
//    imports.add("java.io.Serializable");
//    imports.add("java.util.List");
//    imports.newLine();
//    imports.add("org.apache.ibatis.session.SqlSession");
//    imports.add(Cursor.class);
//    imports.add(MyBatisCursor.class.getName());
//    imports.newLine();
//
//    if (this.metadata.getVersionControlMetadata() != null) {
//      imports.add(DaoForUpdate.class);
//      imports.add(StaleDataException.class);
//    }
//    imports.add(DaoWithOrder.class);
//    if (this.isTable() || this.isView()) {
//      imports.add(UpdateByExampleDao.class);
//    }
//    imports.add(OrderBy.class);
//    if (!this.isTable()) {
//      imports.add(Selectable.class);
//    }
//
//    imports.newLine();
//
//    if (this.avo != null) {
//      imports.add(this.avo.getFullClassName());
//    }
//    if (this.vo != null) {
//      imports.add(this.vo.getFullClassName());
//    }
//
//    for (ForeignKeyMetadata ik : this.metadata.getImportedFKs()) {
//
//      String fkc;
//      ObjectVO rvo = this.generator.getVO(ik.getRemote().getTableMetadata());
//      if (rvo != null) {
//        fkc = rvo.getFullClassName();
//        imports.add(fkc);
//        ObjectDAO dao = this.generator.getDAO(ik.getRemote().getTableMetadata());
//        String daoc = dao.getFullClassName();
//        imports.add(daoc);
//      } else {
//        EnumClass ec = this.generator.getEnum(ik.getRemote().getTableMetadata());
//        fkc = ec.getFullClassName();
//        imports.add(fkc);
//      }
//
//    }
//
//    for (ForeignKeyMetadata ek : this.metadata.getExportedFKs()) {
//
//      // log.info(" DAO=" + metadata.getIdentifier().getSQLIdentifier() + " ek="
//      // +
//      // ek.getRemote().getTableMetadata().getIdentifier().getSQLIdentifier());
//
//      try {
//        @SuppressWarnings("unused")
//        TableTag tag = (TableTag) ek.getRemote().getTableMetadata().getDaoTag();
//
//        ObjectVO rvo = this.generator.getVO(ek.getRemote().getTableMetadata());
//        imports.add(rvo.getFullClassName());
//
//        ObjectDAO dao = this.generator.getDAO(ek.getRemote().getTableMetadata());
//        imports.add(dao.getOrderByFullClassName());
//        imports.add(dao.getFullClassName());
//
//      } catch (ClassCastException e) {
//        // points to an enum - nothing to do.
//      }
//
//    }
//
//    imports.newLine();
//    // imports.comment("[ now, for the selects... ]");
//
//    for (SelectMethodMetadata sm : this.metadata.getSelectsMetadata()) {
//      ClassPackage voPackage = this.myBatisTag.getDaos().getDaoPackage(this.fragmentPackage);
//      SelectMethodReturnType rt = sm.getReturnType(voPackage);
//      imports.add(rt.getVOFullClassName());
//    }
//    if (!this.metadata.getSelectsMetadata().isEmpty()) {
//      imports.newLine();
//    }
//    // imports.comment("[ selects done. ]");
//
//    if (this.usesConverters() || hasFKPointingToEnum()) {
//      imports.add("java.sql.SQLException");
//      imports.add("java.sql.CallableStatement");
//      imports.add("java.sql.PreparedStatement");
//      imports.add("java.sql.ResultSet");
//      imports.add("org.apache.ibatis.type.JdbcType");
//      imports.add("org.apache.ibatis.type.TypeHandler");
//      imports.add(TypeConverter.class);
//      imports.newLine();
//    }
//
//    imports.add(Override.class);
//    imports.add(Map.class);
//    imports.add(ArrayList.class);
//    imports.add(HashMap.class);
//    imports.newLine();
//
//    imports.add(ResultSetColumn.class);
//    imports.add("org.hotrod.spring.SpringBeanObjectFactory");
//
//    imports.add(LiveSQLDialect.class);
//    imports.add("org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.TypeSource");
//    imports.add(LiveSQLMapper.class);
//    imports.add(CastUtil.class);
//    imports.add("javax.annotation.PostConstruct");
//    imports.add(DataSource.class);
//    imports.add(Column.class);
//    imports.add(TypeSolver.class);
//
//    imports.newLine();
//
//    imports.add(NumberEntityColumn.class);
//    imports.add(StringEntityColumn.class);
//    imports.add(DateTimeEntityColumn.class);
//    imports.add(BooleanEntityColumn.class);
//    imports.add(ByteArrayEntityColumn.class);
//    imports.add(ObjectEntityColumn.class);
//
//    imports.newLine();
//
//    imports.add(Table.class);
//
//    imports.add(GeneralBooleanExpression.class);
//    imports.add(AllColumns.class);
//    imports.add(CriteriaWherePhase.class);
//    imports.add(DeleteWherePhase.class);
//    imports.add(UpdateSetCompletePhase.class);
//    imports.add(Name.class);
//
//    imports.add(View.class);
//    imports.newLine();
//
//    imports.add(LiveSQLContext.class);
//
//    imports.add("org.springframework.stereotype.Component");
//    imports.add("org.springframework.beans.BeansException");
//    imports.add("org.springframework.context.annotation.Lazy");
//    imports.add("org.springframework.beans.factory.annotation.Autowired");
//    imports.add("org.springframework.beans.factory.annotation.Value");
//    imports.add("org.springframework.context.ApplicationContext");
//    imports.add("org.springframework.context.ApplicationContextAware");
//    if (!SUtil.isEmpty(this.layout.getSqlSessionBeanQualifier())) {
//      imports.add("org.springframework.beans.factory.annotation.Qualifier");
//    }
//
//    imports.newLine();
//
//    if (this.getBundle().getParent() != null) {
//      imports.add("org.aspectj.lang.JoinPoint");
//      imports.add("org.aspectj.lang.annotation.Aspect");
//      imports.add("org.aspectj.lang.annotation.Before");
//      imports.add("org.springframework.context.annotation.Configuration");
//      imports.add("org.springframework.core.annotation.Order");
//      imports.add(LazyParentClassLoading.class);
//      imports.newLine();
//    }
//
//    this.w.write(imports.render());

    // Signature

    w.println("@", Const.COMPONENT);
    w.println("public class " + this.getClassName() + " implements ", Serializable.class, ", ",
        Const.APPLICATION_CONTEXT_AWARE, " {");
    w.println();

    // Serial Version UID

    w.println("  private static final long serialVersionUID = 1L;");
    w.println();

    // Spring properties

    w.println("  @", Const.AUTOWIRED);
    if (!SUtil.isEmpty(this.layout.getSqlSessionBeanQualifier())) {
      w.println("  @", Const.QUALIFIER, "(\"" + this.layout.getSqlSessionBeanQualifier() + "\")");
    }
    w.println("  private ", Const.SQL_SESSION, " sqlSession;");
    w.println();

    Map<String, String> daoMembers = new HashMap<String, String>();

    for (DataSetMetadata ds : this.fkSelectors.keySet()) {
      if (!(ds.getDaoTag() instanceof EnumTag)) {
        ObjectDAO dao = this.generator.getDAO(ds);
        daoMembers.put(dao.getClassName(), dao.getMemberName());
      }
    }

    for (DataSetMetadata ds : this.efkSelectors.keySet()) {
      if (!(ds.getDaoTag() instanceof EnumTag)) {
        ObjectDAO dao = this.generator.getDAO(ds);
        daoMembers.put(dao.getClassName(), dao.getMemberName());
      }
    }

    for (String className : daoMembers.keySet()) {
      String memberName = daoMembers.get(className);
      if (!className.equals(this.getClassName())) {
        w.println("  @", Const.LAZY);
        w.println("  @", Const.AUTOWIRED);
        w.println("  private " + className + " " + memberName + ";");
        w.println();
      }
    }

    w.println("  @", Const.AUTOWIRED);
    if (!SUtil.isEmpty(this.layout.getLiveSQLDialectBeanQualifier())) {
      w.println("  @", Const.QUALIFIER, "(\"" + this.layout.getLiveSQLDialectBeanQualifier() + "\")");
    }
    w.println("  private ", LiveSQLDialect.class, " liveSQLDialect;");
    w.println();

    w.println("  @", Const.AUTOWIRED);
    w.println("  private ", LiveSQLMapper.class, " liveSQLMapper;");
    w.println();

    w.println("  @", Const.AUTOWIRED);
    w.println("  private ", SpringBeanObjectFactory.class, " springBeanObjectFactory;");
    w.println();

    w.println("  @", Const.AUTOWIRED);
    w.println("  private DataSource dataSource;");
    w.println();

    w.println("  private ", Const.APPLICATION_CONTEXT, " applicationContext;");
    w.println();

    w.println("  @Override");
    w.println("  public void setApplicationContext(final ", Const.APPLICATION_CONTEXT, " applicationContext) throws ",
        Const.BEANS_EXCEPTION, " {");
    w.println("    this.applicationContext = applicationContext;");
    w.println("    this.sqlSession.getConfiguration().setObjectFactory(this.springBeanObjectFactory);");
    w.println("  }");
    w.println();

    w.println("  private ", LiveSQLContext.class, " context;");
    w.println();

    w.println("  @", Const.POST_CONSTRUCT);
    w.println("  public void initializeContext() {");
    w.println("    this.context = new ", LiveSQLContext.class,
        "(this.liveSQLDialect, this.sqlSession, this.liveSQLMapper, this.dataSource, new ", TypeSolver.class,
        "(null, this.liveSQLDialect));");
    w.println("  }");
    w.println();

  }

  private static final String SELECT_BY_PK_METHOD = "select";

  private void writeSelectByPK(final MyBatisSpringGenerator mg) throws IOException, UnresolvableDataTypeException {
    if (this.metadata.getPK() == null) {
      w.println("  // no select by PK generated, since the table does not have a PK.");
      w.println();
      return;
    }

    w.println("  // select by primary key");
    w.println();
    selectByUniqueKey(mg, this.metadata.getPK(), SELECT_BY_PK_METHOD, this.mapper.getFullMapperIdSelectByPK());
  }

  private void selectByUniqueKey(final MyBatisSpringGenerator mg, final KeyMetadata key, final String method,
      final String mapperQuery) throws UnresolvableDataTypeException, IOException {
    String paramsSignature = toParametersSignature(key, mg);
    @SuppressWarnings("unused")
    String avoc = this.avo.getFullClassName();
    String voc = this.vo.getFullClassName();

    w.print("  public " + voc + " " + method + "(");
    w.print(paramsSignature);
    w.print(") ");
    w.println("{");

    for (ColumnMetadata cm : key.getColumns()) {
      String m = cm.getId().getJavaMemberName();
      w.println("    if (" + m + " == null)");
      w.println("      return null;");
    }
    w.println("    " + voc + " vo = new " + voc + "();");
    for (ColumnMetadata cm : key.getColumns()) {
      String m = cm.getId().getJavaMemberName();
      String setter = cm.getId().getJavaSetter();
      w.println("    vo." + setter + "(" + m + ");");
    }

    w.println("    return this.sqlSession.selectOne(\"" + mapperQuery + "\", vo);");
    w.println("  }");
    w.println();
  }

  private void writeSelectByUI(final MyBatisSpringGenerator mg) throws IOException, UnresolvableDataTypeException {
    boolean first = true;

    // Remove duplicated unique indexes/constraints that may be registered in
    // the database. This behavior has been observed in PostgreSQL.

    Set<KeyMetadata> distinctConstraints = new LinkedHashSet<KeyMetadata>();
    for (KeyMetadata ui : this.metadata.getUniqueIndexes()) {
      distinctConstraints.add(ui);
    }

    // Generate the primitive method.

    for (KeyMetadata ui : distinctConstraints) {
      if (this.metadata.getPK() == null || !ui.equals(this.metadata.getPK())) {

        if (first) {
          first = false;
          w.println("  // select by unique indexes");
          w.println();
        }

        String camelCase = ui.toCamelCase(this.layout.getColumnSeam());
        String method = "selectByUI" + camelCase;
        selectByUniqueKey(mg, ui, method, this.mapper.getFullMapperIdSelectByUI(ui));

      }
    }

    if (first) {
      w.println("  // select by unique indexes: no unique indexes found"
          + (this.metadata.getPK() != null ? " (besides the PK)" : "") + " -- skipped");
      w.println();
    }
  }

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

  private void writeRowParser() throws IOException {
    String voClassName = this.vo.getFullClassName();
    w.println("  // Row Parser");
    w.println();
    w.println("  public " + voClassName + " parseRow(", Map.class, "<String, Object> m) {");
    w.println("    return parseRow(m, null, null);");
    w.println("  }");
    w.println();
    w.println("  public " + voClassName + " parseRow(", Map.class, "<String, Object> m, String prefix) {");
    w.println("    return parseRow(m, prefix, null);");
    w.println("  }");
    w.println();
    w.println("  public " + voClassName + " parseRow(", Map.class,
        "<String, Object> m, String prefix, String suffix) {");
    w.println("    " + voClassName + " mo = this.applicationContext.getBean(" + voClassName + ".class);");
    w.println("    String p = prefix == null ? \"\": prefix;");
    w.println("    String s = suffix == null ? \"\": suffix;");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      String property = cm.getId().getJavaMemberName();

      if (cm.getConverter() != null) {
        ConverterTag ct = cm.getConverter();

        w.println(
            "    mo." + cm.getId().getJavaSetter() + "(new " + ct.getJavaClass() + "().decode((" + ct.getJavaRawType()
                + ") m.get(p + \"" + JUtils.escapeJavaString(property) + "\" + s), this.sqlSession.getConnection()));");

      } else if ("java.lang.Byte".equals(javaType) || //
          "java.lang.Short".equals(javaType) || //
          "java.lang.Integer".equals(javaType) || //
          "java.lang.Long".equals(javaType) || //
          "java.lang.Float".equals(javaType) || //
          "java.lang.Double".equals(javaType) || //
          "java.math.BigInteger".equals(javaType) || //
          "java.math.BigDecimal".equals(javaType)) {
        int idx = javaType.lastIndexOf(".");
        String st = idx == -1 ? javaType : javaType.substring(idx + 1);
        w.println("    mo." + cm.getId().getJavaSetter() + "(", CastUtil.class,
            ".to" + st + "((Number) m.get(p + \"" + JUtils.escapeJavaString(property) + "\" + s)));");
      } else if ("java.lang.Object".equals(javaType)) {
        w.println(
            "    mo." + cm.getId().getJavaSetter() + "(m.get(p + \"" + JUtils.escapeJavaString(property) + "\" + s));");
      } else {
        w.println("    mo." + cm.getId().getJavaSetter() + "((" + javaType + ") m.get(p + \""
            + JUtils.escapeJavaString(property) + "\" + s));");
      }

    }

    w.println("    return mo;");
    w.println("  }");
    w.println();

  }

  private void writeSelectByExample() throws IOException {
    w.println("  // select by example");
    w.println();

    String avoClassName = this.avo.getFullClassName();
    String voClassName = this.vo.getFullClassName();

    w.println("  public ", List.class, "<" + voClassName + "> select(final " + avoClassName + " example, final ",
        ExternalClass.of(this.getOrderByClassName()), "... orderBies)");
    w.print("      ");
    w.println("{");
    w.println("    ", DaoWithOrder.class, "<" + avoClassName + ", " + this.getOrderByClassName() + "> dwo = //");
    w.println("        new ", DaoWithOrder.class, "<>(example, orderBies);");
    w.println("    return this.sqlSession.selectList(\"" + this.mapper.getFullMapperIdSelectByExample() + "\", dwo);");
    w.println("  }");
    w.println();

    w.println("  public ", Cursor.class, "<" + voClassName + "> selectCursor(final " + avoClassName + " example, final "
        + this.getOrderByClassName() + "... orderBies)");
    w.print("      ");
    w.println("{");
    w.println("    ", DaoWithOrder.class, "<" + avoClassName + ", " + this.getOrderByClassName() + "> dwo = //");
    w.println("        new ", DaoWithOrder.class, "<>(example, orderBies);");
    w.println("    return new ", MyBatisCursor.class, "<" + voClassName + ">(this.sqlSession.selectCursor(\""
        + this.mapper.getFullMapperIdSelectByExample() + "\", dwo));");
    w.println("  }");
    w.println();
  }

  private void writeSelectByCriteria() throws IOException {
    w.println("  // select by criteria");
    w.println();

    String daoClassName = this.getClassName();
    String voFullClassName = this.vo.getFullClassName();
    String mapperName = this.mapper.getFullMapperIdSelectByCriteria();

    w.println("  public ", CriteriaWherePhase.class,
        "<" + voFullClassName + "> select(final " + daoClassName + "." + this.metadataClassName + " from,");
    w.println("      final ", GeneralBooleanExpression.class, " predicate) {");
    w.println("    return new ", CriteriaWherePhase.class,
        "<" + voFullClassName + ">(this.context, \"" + mapperName + "\",");
    w.println("        from, predicate);");
    w.println("  }");

    w.println();
  }

  public static String renderJavaComment(final String sentence) {

    StringBuilder sb = new StringBuilder();
    sb.append("  /*\n");
    sb.append("  * The SQL statement for this method is:\n");
    sb.append("\n");

    String rendered = sentence.replaceAll("\\*/", "\\*\\\\/");
    if (!sentence.equals(rendered)) {
      sb.append("Note: The string sequence star-slash has been replaced by *\\/ in this comment.\n\n");
    }
    sb.append(rendered);
    sb.append("\n");
    sb.append("\n");
    sb.append("  */\n");

    return sb.toString();
  }

  private void writeSelectParentByFK() throws IOException, ControlledException {

    ObjectDAO currentDAO = this;

    List<ForeignKeyMetadata> fks = this.metadata.getImportedFKs();
    if (fks.isEmpty()) {

      w.println("  // select parent(s) by FKs: no imported keys found -- skipped");
      w.println();

    } else {

      w.println("  // select parent(s) by FKs");
      w.println();

      // Group by remote table.

      // Also, get distinct foreign keys only, since multiple identical foreign
      // keys can be registered in the database. This behavior has been
      // observed in PostgreSQL.

      log.fine("DAO: " + this.getClassName() + " -- this.metadata.getImportedFKs().size()="
          + this.metadata.getImportedFKs().size() + " --  fkSelectors.size()=" + fkSelectors.size());

      for (DataSetMetadata ds : fkSelectors.keySet()) {
        ObjectVO vo = this.generator.getVO(ds);
        if (vo != null) { // points to a table, not an enum
          ObjectDAO dao = this.generator.getDAO(ds);

          String selectParentPhaseClassName = "SelectParent" + vo.getJavaClassIdentifier() + "Phase";
          String voClassName = this.vo.getClassName();

          w.println("  public " + selectParentPhaseClassName + " selectParent" + vo.getJavaClassIdentifier()
              + "Of(final " + voClassName + " vo) {");
          w.println("    return new " + selectParentPhaseClassName + "(vo);");
          w.println("  }");
          w.println();

          w.println("  public class " + selectParentPhaseClassName + " {");
          w.println();
          w.println("    private " + voClassName + " vo;");
          w.println();
          w.println("    " + selectParentPhaseClassName + "(final " + voClassName + " vo) {");
          w.println("      this.vo = vo;");
          w.println("    }");
          w.println();

          Set<KeyMetadata> fromKeys = new HashSet<KeyMetadata>();

          for (ForeignKeyMetadata fkm : fkSelectors.get(ds)) {

            if (!fromKeys.contains(fkm.getLocal())) {
              fromKeys.add(fkm.getLocal());
              String fromKey = fkm.getLocal().toCamelCase(this.layout.getColumnSeam());
              String fromPhaseClassName = "SelectParent" + vo.getJavaClassIdentifier() + "From" + fromKey + "Phase";
              String fromMethod = "from" + fromKey;

              w.println("    public " + fromPhaseClassName + " " + fromMethod + "() {");
              w.println("      return new " + fromPhaseClassName + "(this.vo);");
              w.println("    }");
              w.println();
            }

          }

          w.println("  }");
          w.println();

          fromKeys.clear();

          for (ForeignKeyMetadata fkm : fkSelectors.get(ds)) {

            if (!fromKeys.contains(fkm.getLocal())) {
              fromKeys.add(fkm.getLocal());
              String fromKey = fkm.getLocal().toCamelCase(this.layout.getColumnSeam());
              String fromPhaseClassName = "SelectParent" + vo.getJavaClassIdentifier() + "From" + fromKey + "Phase";

              w.println("  public class " + fromPhaseClassName + " {");
              w.println();
              w.println("    private " + voClassName + " vo;");
              w.println();
              w.println("    " + fromPhaseClassName + "(final " + voClassName + " vo) {");
              w.println("      this.vo = vo;");
              w.println("    }");
              w.println();

              for (ForeignKeyMetadata fkm2 : fkSelectors.get(ds)) {
                if (fkm2.getLocal().equals(fkm.getLocal())) {
                  String toMethod = "to" + fkm2.getRemote().toCamelCase(dao.layout.getColumnSeam());
                  String params = renderParams(fkm);
                  String selectMethod = "";
                  if (fkm2.getRemote().equals(fkm2.getRemote().getTableMetadata().getPK())) {
                    selectMethod = "select";
                  } else {
                    selectMethod = "selectByUI" + fkm2.getRemote().toCamelCase(this.layout.getColumnSeam());
                  }

                  w.println("    public " + vo.getClassName() + " " + toMethod + "() {");
                  String memberPrefix = dao.getClassName().equals(currentDAO.getClassName()) ? ""
                      : (dao.getMemberName() + ".");
                  w.println("      return " + memberPrefix + selectMethod + "(" + params + ");");
                  w.println("    }");
                  w.println();
                }
              }

              w.println("  }");
              w.println();

            }
          }

        } else {
          EnumClass ec = this.generator.getEnum(ds);
          if (ec != null) {
            for (ForeignKeyMetadata fkm : fkSelectors.get(ds)) {
              ListWriter lw = new ListWriter(", ");
              for (ColumnMetadata cm : fkm.getLocal().getColumns()) {
                lw.add(cm.getName());
              }
              w.println("  // --- no select parent for FK column" + (fkm.getLocal().getColumns().size() > 1 ? "s" : "")
                  + " (" + lw.toString() + ") since it points to the enum table "
                  + fkm.getRemote().getTableMetadata().getId().getRenderedSQLName());
              w.println();
            }
          }
        }
      }

    }

  }

  private String renderParams(final ForeignKeyMetadata fkm) throws ControlledException {
    Iterator<ColumnMetadata> lit = fkm.getLocal().getColumns().iterator();
    Iterator<ColumnMetadata> rit = fkm.getRemote().getColumns().iterator();
    ListWriter lw = new ListWriter(", ");
    while (lit.hasNext() && rit.hasNext()) {
      ColumnMetadata local = lit.next();
      ColumnMetadata remote = rit.next();
      try {
        lw.add(renderCast("this.vo." + local.getId().getJavaMemberName(), local, remote));
      } catch (CannotConvertTypeException e) {
        throw new ControlledException(e.getMessage());
      }
    }
    return lw.toString();
  }

  private String renderCast(final String expression, final ColumnMetadata fromColumn, final ColumnMetadata toColumn)
      throws CannotConvertTypeException {
    if (fromColumn.getType().getJavaClassName().equals(toColumn.getType().getJavaClassName())) {
      return expression;
    }
    try {
      return GenUtils.convertPropertyType(fromColumn.getType().getJavaClassName(),
          toColumn.getType().getJavaClassName(), expression);
    } catch (ControlledException e) {
      throw new CannotConvertTypeException(
          "Cannot navigate foreign key relationship from column " + fromColumn.getTable() + "." + fromColumn.getName()
              + " to column " + toColumn.getTable() + "." + toColumn.getName() + ": " + e.getMessage());
    }
  }

  public class CannotConvertTypeException extends Exception {

    private static final long serialVersionUID = 1L;

    public CannotConvertTypeException(final String message) {
      super(message);
    }

  }

  private Map<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>> compileDistinctFKs(
      final List<ForeignKeyMetadata> fks) {

    List<ForeignKeyMetadata> sortedFKs = new ArrayList<ForeignKeyMetadata>(fks);
    sortedFKs.sort((a, b) -> {
      int c = a.getRemote().toCamelCase(".").compareTo(b.getRemote().toCamelCase("."));
      if (c != 0) {
        return c;
      }
      return a.getLocal().toCamelCase(".").compareTo(b.getLocal().toCamelCase("."));
    });

    Map<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>> fkSelectors = new LinkedHashMap<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>>();
    for (ForeignKeyMetadata fk : sortedFKs) {
      DataSetMetadata ds = fk.getRemote().getTableMetadata();
      LinkedHashSet<ForeignKeyMetadata> fkSelector = fkSelectors.get(ds);
      if (fkSelector == null) {
        fkSelector = new LinkedHashSet<ForeignKeyMetadata>();
        fkSelectors.put(ds, fkSelector);
      }
      fkSelector.add(fk);
    }

    return fkSelectors;
  }

  public static class ForeignKey {

    private JdbcForeignKey fk;

    public ForeignKey(JdbcForeignKey fk) {
      this.fk = fk;
    }

    public JdbcForeignKey getFk() {
      return fk;
    }

    @Override
    public int hashCode() {
      return 1;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ForeignKey other = (ForeignKey) obj;
      if (fk == null) {
        if (other.fk != null)
          return false;
      } else {
        TableKey tlk = new TableKey(fk.getLocalKey());
        TableKey olk = new TableKey(other.fk.getLocalKey());
        if (!tlk.equals(olk))
          return false;
        if (!fk.getRemoteTable().getName().equals(other.fk.getRemoteTable().getName()))
          return false;
        TableKey trk = new TableKey(fk.getRemoteKey());
        TableKey ork = new TableKey(other.fk.getRemoteKey());
        if (!trk.equals(ork))
          return false;
      }
      return true;
    }

  }

  private void writeSelectChildrenByFK() throws IOException, ControlledException {

    ObjectDAO currentDAO = this;

    if (this.metadata.getExportedFKs().isEmpty()) {

      w.println("  // select children by FKs: no exported FKs found -- skipped");
      w.println();

    } else {

      w.println("  // select children by FKs");
      w.println();

      // Group by remote table.

      // Also, get distinct foreign keys only, since multiple identical foreign
      // keys can be registered in the database. This behavior has been observed
      // in PostgreSQL.

      for (DataSetMetadata ds : this.efkSelectors.keySet()) {

        ObjectVO vo = this.generator.getVO(ds);
        ObjectDAO dao = this.generator.getDAO(ds);

        String selectChildrenPhaseClassName = "SelectChildren" + vo.getJavaClassIdentifier() + "Phase";

        w.println("  public " + selectChildrenPhaseClassName + " selectChildren" + vo.getJavaClassIdentifier()
            + "Of(final " + this.vo.getClassName() + " vo) {");
        w.println("    return new " + selectChildrenPhaseClassName + "(vo);");
        w.println("  }");
        w.println();

        w.println("  public class " + selectChildrenPhaseClassName + " {");
        w.println();
        w.println("    private " + this.vo.getClassName() + " vo;");
        w.println();
        w.println("    " + selectChildrenPhaseClassName + "(final " + this.vo.getClassName() + " vo) {");
        w.println("      this.vo = vo;");
        w.println("    }");
        w.println();

        Set<KeyMetadata> fromKeys = new HashSet<KeyMetadata>();

        for (ForeignKeyMetadata tfk : this.efkSelectors.get(ds)) {
          if (!fromKeys.contains(tfk.getLocal())) {
            fromKeys.add(tfk.getLocal());
            String fromKey = tfk.getLocal().toCamelCase(this.layout.getColumnSeam());
            String fromPhaseClassName = "SelectChildren" + vo.getJavaClassIdentifier() + "From" + fromKey + "Phase";
            String fromMethod = "from" + fromKey;

            w.println("    public " + fromPhaseClassName + " " + fromMethod + "() {");
            w.println("      return new " + fromPhaseClassName + "(this.vo);");
            w.println("    }");
            w.println();
          }
        }

        w.println("  }");
        w.println();

        fromKeys.clear();

        for (ForeignKeyMetadata tfk : this.efkSelectors.get(ds)) {
          if (!fromKeys.contains(tfk.getLocal())) {
            fromKeys.add(tfk.getLocal());

            String fromKey = tfk.getLocal().toCamelCase(this.layout.getColumnSeam());
            String fromPhaseClassName = "SelectChildren" + vo.getJavaClassIdentifier() + "From" + fromKey + "Phase";

            w.println("  public class " + fromPhaseClassName + " {");
            w.println();
            w.println("    private " + this.vo.getClassName() + " vo;");
            w.println();
            w.println("    " + fromPhaseClassName + "(final " + this.vo.getClassName() + " vo) {");
            w.println("      this.vo = vo;");
            w.println("    }");
            w.println();

            for (ForeignKeyMetadata fkm2 : this.efkSelectors.get(ds)) {
              if (fkm2.getLocal().equals(tfk.getLocal())) {

                // TODO: fix remote seam

                fkm2.getRemote().getTableMetadata();
                writeFKChildrenToMethod(currentDAO, vo, dao, fkm2, false);
                writeFKChildrenToMethod(currentDAO, vo, dao, fkm2, true);

              }
            }

            w.println("  }");
            w.println();
          }
        }

      }

    }

  }

  private void writeFKChildrenToMethod(final ObjectDAO currentDAO, final ObjectVO vo, final ObjectDAO dao,
      final ForeignKeyMetadata fkm2, final boolean useCursor) throws IOException, ControlledException {
    String toMethod = (useCursor ? "cursorTo" : "to") + fkm2.getRemote().toCamelCase(dao.layout.getColumnSeam());

    Class<?> rt = useCursor ? Cursor.class : List.class;
    w.println("    public ", rt, "<" + vo.getClassName() + "> " + toMethod + "(final " + vo.getJavaClassIdentifier()
        + "OrderBy... orderBies) {");
    w.println("      " + vo.getClassName() + " example = new " + vo.getClassName() + "();");

    Iterator<ColumnMetadata> lit = fkm2.getLocal().getColumns().iterator();
    Iterator<ColumnMetadata> rit = fkm2.getRemote().getColumns().iterator();

    while (lit.hasNext() && rit.hasNext()) {
      ColumnMetadata lcm = lit.next();
      ColumnMetadata rcm = rit.next();
      try {
        w.println("      example.set" + rcm.getId().getJavaClassName() + "("
            + renderCast("this.vo.get" + lcm.getId().getJavaClassName() + "()", lcm, rcm) + ");");
      } catch (CannotConvertTypeException e) {
        throw new ControlledException(e.getMessage());
      }
    }
    String memberPrefix = dao.getClassName().equals(currentDAO.getClassName()) ? "" : (dao.getMemberName() + ".");
    String selectMethod = useCursor ? "selectCursor" : "select";
    w.println("      return " + memberPrefix + selectMethod + "(example, orderBies);");
    w.println("    }");
    w.println();
  }

  // TODO: Clean up
//  private String getChildrenSelectorClass(final ObjectVO dao) {
//    return dao.getJavaClassIdentifier() + "ChildrenSelector";
//  }

  private void writeInsert() throws IOException, UnresolvableDataTypeException {

    // Count auto-generated columns

    int sequences = 0;
    int identities = 0;
    int defaults = 0;
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getSequenceId() != null) {
        sequences++;
      }
      if (cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity()) {
        identities++;
      }
      if (cm.getColumnDefault() != null) {
        defaults++;
      }
    }

    boolean integratesSequences = this.adapter.getInsertIntegration().integratesSequences();
    boolean integratesIdentities = this.adapter.getInsertIntegration().integratesIdentities();
    boolean integratesDefaults = this.adapter.getInsertIntegration().integratesDefaults();

    boolean extraInsert = integratesSequences && integratesDefaults && defaults != 0;

    /**
     * <pre>
     * | integrates identities  | false      | true
     * |------------------------+------------+-------------
     * | has identities : false | T          | T
     * |                : true  | F          | T
     *                
     * ! has identities || integrates identities
     * </pre>
     */

    w.println("  // insert");
    w.println();

    String voClassName = this.avo.getFullClassName();
    String moClassName = this.vo.getFullClassName();
    if (extraInsert) {
      w.print("  public " + moClassName + " insert(final " + voClassName + " vo) ");
      w.println("{");
      w.println("    return insert(vo, false);");
      w.println("  }");
      w.println();
    }

    w.print("  public " + moClassName + " insert(final " + voClassName + " vo");
    if (extraInsert) {
      w.print(", final boolean retrieveDefaults");
    }
    w.print(") ");
    w.println("{");

    VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();

    if (vcm != null) {
      ColumnMetadata cm = vcm.getColumnMetadata();
      String literalValue = renderNumericLiteral(cm.getType().getValueRange().getInitialValue(),
          cm.getType().getJavaClassName());
      w.println("    vo." + cm.getId().getJavaMemberName() + " = " + literalValue + ";");
    }

    // Decide on the mapper id

    if (extraInsert) {
      w.println("    String id = retrieveDefaults ? \"" + this.mapper.getFullMapperIdInsertRetrievingDefaults()
          + "\" : \"" + this.mapper.getFullMapperIdInsert() + "\";");
    } else {
      w.println("    String id = \"" + this.mapper.getFullMapperIdInsert() + "\";");
    }

    // Choose insert variant

    if (identities == 0) {
      if (sequences == 0) { // no sequences, no identities
        w.println("    this.sqlSession.insert(id, vo);");
        this.writeVOToModel();
        w.println("    return mo;");
      } else { // sequences only
        if (integratesSequences) {
          writeInsertIntegrated(true, false, extraInsert);
          this.writeVOToModel();
          w.println("    return mo;");
        } else {
          w.println("    this.sqlSession.insert(id, vo);");
          this.writeVOToModel();
          w.println("    return mo;");
        }
      }
    } else {
      if (sequences == 0) { // identities only
        if (integratesIdentities) {
          writeInsertIntegrated(false, true, extraInsert);
          this.writeVOToModel();
          w.println("    return mo;");
        } else {
          w.println("    this.sqlSession.insert(id, vo);");
          this.writeVOToModel();
          w.println("    return mo;");
        }
      } else { // sequences & identities
        if (integratesSequences && integratesIdentities) {
          writeInsertIntegrated(true, true, extraInsert);
          this.writeVOToModel();
          w.println("    return mo;");
        } else if (integratesIdentities) {
          writeSequencesPreFetch();
          writeInsertIntegrated(false, true, extraInsert);
          this.writeVOToModel();
          w.println("    return mo;");
        } else if (integratesSequences) {
          writeInsertIntegrated(true, false, extraInsert);
          writeIdentitiesPostFetch();
          this.writeVOToModel();
          w.println("    return mo;");
        } else {
          writeSequencesPreFetch();
          w.println("    int rows = this.sqlSession.insert(id, vo);");
          writeIdentitiesPostFetch();
          this.writeVOToModel();
          w.println("    return mo;");
        }

      }
    }

    w.println("  }");
    w.println();

  }

  private void writeVOToModel() throws IOException {
    String moClassName = this.vo.getFullClassName();
//    w.println("    " + moClassName + " mo = new " + moClassName + "();");
    w.println("    " + moClassName + " mo = springBeanObjectFactory.create(" + moClassName + ".class);"); // Spring bean
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      w.println("    mo." + cm.getId().getJavaSetter() + "(vo." + cm.getId().getJavaGetter() + "());");
    }
  }

  private void writeInsertIntegrated(final boolean integratesSequences, final boolean integratesIdentities,
      final boolean extraInsert) throws IOException {
    if (this.adapter.integratesUsingQuery()) {
      String voClassName = this.vo.getFullClassName();
      w.println("    " + voClassName + " values = this.sqlSession.selectOne(id, vo);");
      w.println("    int rows = 1;");
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        String prop = cm.getId().getJavaMemberName();
        if (cm.getSequenceId() != null && integratesSequences
            || cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity() && integratesIdentities) {
          w.println("    vo." + prop + " = values." + prop + ";");
        } else if (extraInsert) {
          w.println("    if (retrieveDefaults) {");
          w.println("      vo." + prop + " = values." + prop + ";");
          w.println("    }");
        }
      }
    } else {
      w.println("    int rows = this.sqlSession.insert(id, vo);");
    }
  }

  private void writeSequencesPreFetch() throws IOException {
    String voClassName = this.vo.getFullClassName();
    w.println("    " + voClassName + " sequences = this.sqlSession.selectOne(\""
        + this.mapper.getFullMapperIdSequencesPreFetch() + "\");");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getSequenceId() != null) {
        String prop = cm.getId().getJavaMemberName();
        w.println("    vo." + prop + " = sequences." + prop + ";");
      }
    }
  }

  private void writeIdentitiesPostFetch() throws IOException {
    String voClassName = this.vo.getFullClassName();
    w.println("    " + voClassName + " identities = this.sqlSession.selectOne(\""
        + this.mapper.getFullMapperIdIdentitiesPostFetch() + "\");");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity()) {
        String prop = cm.getId().getJavaMemberName();
        w.println("    vo." + prop + " = identities." + prop + ";");
      }
    }
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

  private static final String UPDATE_BY_PK_METHOD = "update";

  private void writeUpdateByPK(final MyBatisSpringGenerator mg) throws IOException, UnresolvableDataTypeException {
    if (this.metadata.getPK() == null) {
      w.println("  // no update by PK generated, since the table does not have a PK.");
      w.println();
    } else {

      boolean useVersionControl = this.metadata.getVersionControlMetadata() != null;

      w.println("  // update by PK");
      w.println();

      String voClassName = this.vo.getFullClassName();
      if (useVersionControl) {
        VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();
        ColumnMetadata cm = vcm.getColumnMetadata();
        PropertyType pt = cm.getType();
        ValueRange range = pt.getValueRange();
        w.print("  public int " + UPDATE_BY_PK_METHOD + "(final " + voClassName + " vo) ");
        w.println("{");
        w.println("    long currentVersion = vo." + cm.getId().getJavaGetter() + "();");

        String minValue = renderNumericLiteral(range.getMinValue(), cm.getType().getJavaClassName());
        String maxValue = renderNumericLiteral(range.getMaxValue(), cm.getType().getJavaClassName());

        w.println("    ", DaoForUpdate.class, "<" + voClassName + "> u = new ", DaoForUpdate.class,
            "<" + voClassName + ">(vo, currentVersion, " + minValue + ", " + maxValue + ");");
        w.println("    int rows = this.sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByPK() + "\", u);");
        w.println("    if (rows != 1) {");
        w.println("      throw new StaleDataException(\"Could not update row on table "
            + this.metadata.getId().getCanonicalSQLName() + " with version \" + currentVersion");
        w.println("          + \" since it had already been updated by another process.\");");
        w.println("    }");
        w.println("    vo." + cm.getId().getJavaGetter() + "() = (" + pt.getPrimitiveClassJavaType()
            + ") u.getNextVersionValue();");
        w.println("    return rows;");
      } else {
        w.print("  public int " + UPDATE_BY_PK_METHOD + "(final " + voClassName + " vo) ");
        w.println("{");
        for (ColumnMetadata cm : this.metadata.getPK().getColumns()) {
          w.println("    if (vo." + cm.getId().getJavaGetter() + "() == null) return 0;");
        }
        w.println("    return this.sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByPK() + "\", vo);");
      }

      w.println("  }");
      w.println();
    }

  }

  private void writeInsertByExample() throws IOException {
    w.println("  // insert by example");
    w.println();
    String voClassName = this.avo.getFullClassName();
    w.print("  public int insertByExample(final " + voClassName + " example) ");
    w.println("{");
    w.println("    return sqlSession.insert(\"" + this.mapper.getFullMapperIdInsertByExample() + "\", example);");
    w.println("  }");
    w.println();

  }

  private void writeUpdateByExample() throws IOException {
    w.println("  // update by example");
    w.println();
    String voClassName = this.avo.getFullClassName();
    w.println("  public int update(final " + voClassName + " example, final " + voClassName + " updateValues) {");
    w.println("    ", UpdateByExampleDao.class, "<" + voClassName + "> fvd = //");
    w.println("      new ", UpdateByExampleDao.class, "<" + voClassName + ">(example, updateValues);");
    w.println("    return this.sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByExample() + "\", fvd);");
    w.println("  }");
    w.println();
  }

  private void writeUpdateByCriteria() throws IOException {
    w.println("  // update by criteria");
    w.println();
    String voClassName = this.avo.getFullClassName();
    String mapperName = this.mapper.getFullMapperIdUpdateByCriteria();

    // writeUpdateByCriteriaVariation(false, voClassName, mapperName);
    writeUpdateByCriteriaVariation(true, voClassName, mapperName);

    w.println();
  }

  private void writeUpdateByCriteriaVariation(final boolean useEntity, final String voClassName,
      final String mapperName) throws IOException {
    w.print("  public ", UpdateSetCompletePhase.class, " update(final " + voClassName + " updateValues, ");
    if (useEntity) {
      w.print("final " + this.getClassName() + "." + this.metadataClassName + " tableOrView, ");
    }
    w.println("final " + GeneralBooleanExpression.class.getSimpleName() + " predicate) {");

    w.println("    ", Map.class, "<String, Object> values = new ", HashMap.class, "<>();");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String colName = cm.getId().getRenderedSQLName();
      if (colName.startsWith("\"") && colName.endsWith("\"")) {
        colName = "\\\"" + colName.substring(1, colName.length() - 1) + "\\\"";
      }
      w.println("    if (updateValues." + cm.getId().getJavaGetter() + "() != null) values.put(\"" + colName
          + "\", updateValues." + cm.getId().getJavaGetter() + "());");
    }

    w.print("    return new ", UpdateSetCompletePhase.class, "(");
    w.print("this.context, ");
    w.print("\"" + mapperName + "\", ");
    if (useEntity) {
      w.print("tableOrView, ");
    } else {
      w.print(this.getClassName() + (this.isTable() ? ".newTable()" : ".newView()") + ", ");
    }
    w.println(" predicate, values);");

    w.println("  }");
    w.println();
  }

  private static final String DELETE_BY_PK_METHOD = "delete";

  private void writeDeleteByPK(final MyBatisSpringGenerator mg) throws IOException, UnresolvableDataTypeException {
    KeyMetadata pk = this.metadata.getPK();
    if (pk == null) {
      w.println("  // no delete by PK generated, since the table does not have a PK.");
      w.println();
    } else {

      boolean useVersionControl = this.metadata.getVersionControlMetadata() != null;

      w.println("  // delete by PK");
      w.println();

      @SuppressWarnings("unused")
      String voClassName = this.vo.getFullClassName();

      String paramsSignature = toParametersSignature(pk, mg);
      w.print("  public int " + DELETE_BY_PK_METHOD + "(" + paramsSignature + ") ");
      w.println("{");

      String voc = this.vo.getFullClassName();
      for (ColumnMetadata cm : pk.getColumns()) {
        String m = cm.getId().getJavaMemberName();
        w.println("    if (" + m + " == null) return 0;");
      }
      w.println("    " + voc + " vo = new " + voc + "();");
      for (ColumnMetadata cm : pk.getColumns()) {
        String m = cm.getId().getJavaMemberName();
        String setter = cm.getId().getJavaSetter();
        w.println("    vo." + setter + "(" + m + ");");
      }

      if (useVersionControl) {
        VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();
        ColumnMetadata vccm = vcm.getColumnMetadata();
        w.println("    int rows = this.sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByPK() + "\", vo);");
        w.println("    if (rows != 1) {");
        w.println("      throw new ", StaleDataException.class,
            "(\"Could not delete row on table " + this.metadata.getId().getCanonicalSQLName() + " with version \" + vo."
                + vccm.getId().getJavaMemberName());
        w.println("          + \" since it had already been updated or deleted by another process.\");");
        w.println("    }");
        w.println("    return rows;");
        w.println("  }");
      } else {
        for (ColumnMetadata cm : pk.getColumns()) {
          w.println("    if (vo." + cm.getId().getJavaGetter() + "() == null) return 0;");
        }
        w.println("    return this.sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByPK() + "\", vo);");
        w.println("  }");
      }

      w.println();
    }
  }

  private void writeDeleteByExample() throws IOException {
    w.println("  // delete by example");
    w.println();
    String voClassName = this.avo.getFullClassName();
    w.println("  public int delete(final " + voClassName + " example) {");
    w.println("    return this.sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByExample() + "\", example);");
    w.println("  }");
    w.println();
  }

  private void writeDeleteByCriteria() throws IOException {
    w.println("  // delete by criteria");
    w.println();

    String daoClassName = this.getClassName();
    String mapperName = this.mapper.getFullMapperIdDeleteByCriteria();

    // writeDeleteByCriteriaVariation(false, daoClassName, mapperName);
    writeDeleteByCriteriaVariation(true, daoClassName, mapperName);
  }

  private void writeDeleteByCriteriaVariation(final boolean useFrom, final String daoClassName, final String mapperName)
      throws IOException {
    w.print("  public ", DeleteWherePhase.class, " delete(");
    if (useFrom) {
      w.print("final " + daoClassName + "." + this.metadataClassName + " from, ");
    }
    w.println("final ", GeneralBooleanExpression.class, " predicate) {");

    w.print("    return new ", DeleteWherePhase.class, "(");
    w.print("this.context, ");
    w.print("\"" + mapperName + "\", ");
    if (useFrom) {
      w.print("from, ");
    } else {
      w.print(daoClassName + (this.isTable() ? ".newTable()" : ".newView()") + ", ");
    }
    w.println("predicate);");

    w.println("  }");
    w.println();
  }

  private boolean usesConverters() throws IOException {
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getConverter() != null) {
        return true;
      }
    }
    if (!this.selectTypeHandlerNames.isEmpty()) {
      return true;
    }
    return false;
  }

  private boolean hasFKPointingToEnum() throws IOException {
    for (ForeignKeyMetadata fk : this.metadata.getImportedFKs()) {
      DataSetMetadata ds = fk.getRemote().getTableMetadata();
      EnumClass ec = this.generator.getEnum(ds);
      if (ec != null) {
        return true;
      }
    }
    return false;
  }

  private void writeEnumTypeHandlers() throws ControlledException, IOException {

    for (ForeignKeyMetadata fkm : this.metadata.getImportedFKs()) {
      DataSetMetadata ds = fkm.getRemote().getTableMetadata();
      EnumClass ec = this.generator.getEnum(ds);

      if (ec != null) { // FKs point to an enum

        for (ColumnMetadata cm : fkm.getLocal().getColumns()) {

          String typeHandlerClassName = getTypeHandlerClassName(cm);
          String interType = cm.getType().getJavaClassName();
          String type = ec.getFullClassName();

          ValueTypeManager<?> tm = ValueTypeFactory.getValueManager(interType);
          if (tm == null) {
            throw new ControlledException("Could not generate DAO primitives for table '"
                + this.metadata.getId().getCanonicalSQLName() + "'. Foreign key column '" + cm.getName()
                + "' point to an enum type and must be of one of the following simple types:\n"
                + ListWriter.render(ValueTypeFactory.getSupportedTypes(), " - ", "", "\n"));
          }

          w.println("  // TypeHandler for enum-FK column " + cm.getName() + ".");
          w.println();
          w.println("  public static class " + typeHandlerClassName + " implements ", TypeHandler.class,
              "<" + type + "> {");
          w.println();
          w.println("    @", Override.class);
          w.println("    public " + type + " getResult(final ", ResultSet.class,
              " rs, final String columnName) throws ", SQLException.class, " {");
          w.println("      " + interType + " value = " + tm.renderJdbcGetter("rs", "columnName") + ";");
          w.println("      if (rs.wasNull()) {");
          w.println("        value = null;");
          w.println("      }");
          w.println("      return " + type + ".decode("
              + GenUtils.convertPropertyType(interType, ec.getValueColumn().getClassName(), "value") + ");");

          w.println("    }");
          w.println();
          w.println("    @Override");
          w.println("    public " + type + " getResult(final ", ResultSet.class, " rs, final int columnIndex) throws ",
              SQLException.class, " {");
          w.println("      " + interType + " value = " + tm.renderJdbcGetter("rs", "columnIndex") + ";");
          w.println("      if (rs.wasNull()) {");
          w.println("        value = null;");
          w.println("      }");
          w.println("      return " + type + ".decode("
              + GenUtils.convertPropertyType(interType, ec.getValueColumn().getClassName(), "value") + ");");
          w.println("    }");
          w.println();
          w.println("    @", Override.class);
          w.println("    public " + type + " getResult(final ", CallableStatement.class,
              " cs, final int columnIndex) throws ", SQLException.class, " {");
          w.println("      " + interType + " value = " + tm.renderJdbcGetter("cs", "columnIndex") + ";");
          w.println("      if (cs.wasNull()) {");
          w.println("        value = null;");
          w.println("      }");
          w.println("      return " + type + ".decode("
              + GenUtils.convertPropertyType(interType, ec.getValueColumn().getClassName(), "value") + ");");
          w.println("    }");
          w.println();
          w.println("    @", Override.class);
          w.println("    public void setParameter(final ", PreparedStatement.class,
              " ps, final int columnIndex, final " + type + " v, final ", JDBCType.class, " jdbcType)");
          w.println("        throws ", SQLException.class, " {");
          w.println("      " + ec.getValueColumn().getClassName() + " importedValue = " + type + ".encode(v);");
          w.println("      " + interType + " localValue = "
              + GenUtils.convertPropertyType(ec.getValueColumn().getClassName(), interType, "importedValue") + ";");

          w.println("      if (localValue == null) {");
          w.println("        ps.setNull(columnIndex, jdbcType.TYPE_CODE);");
          w.println("      } else {");
          w.println("        " + tm.renderJdbcSetter("ps", "columnIndex", "localValue", "param"));
          w.println("      }");
          w.println("    }");
          w.println();
          w.println("  }");
          w.println();

        }
      }

    }

  }

  private void writeConverters() throws IOException {

    // Entity columns converters

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getConverter() != null) {
        String typeHandlerClassName = getTypeHandlerClassName(cm);
        writeTypeHandler(null, cm, typeHandlerClassName);
      }
    }

    // Select columns converters

    for (Map<ColumnMetadata, String> selectTypeHandlers : this.selectTypeHandlers.values()) {
      for (ColumnMetadata cm : selectTypeHandlers.keySet()) {
        String thName = selectTypeHandlers.get(cm);
        log.fine("WRITING TYPEHANDLER '" + thName + "'");
        writeTypeHandler("", cm, thName);
      }
    }

  }

  private void writeTypeHandler(final String property, final ColumnMetadata cm, final String typeHandlerClassName)
      throws IOException {
    String interType = cm.getConverter().getJavaRawType();
    String type = cm.getConverter().getJavaType();
    String setter = cm.getConverter().getJdbcSetterMethod();
    String getter = cm.getConverter().getJdbcGetterMethod();
    String converter = cm.getConverter().getJavaClass();

    w.println("  // TypeHandler for " + (property != null ? "property " + property : "column " + cm.getName())
        + " using Converter " + converter + ".");
    w.println();
    w.println("  public static class " + typeHandlerClassName + " implements ", TypeHandler.class, "<" + type + "> {");
    w.println();
    w.println("    private static final ", TypeConverter.class,
        "<" + interType + ", " + type + "> CONVERTER = new " + converter + "();");
    w.println();
    w.println("    @", Override.class);
    w.println("    public " + type + " getResult(final ", ResultSet.class, " rs, final String columnName) throws ",
        SQLException.class, " {");
    w.println("      " + interType + " raw = rs." + getter + "(columnName);");
    w.println("      if (rs.wasNull()) {");
    w.println("        raw = null;");
    w.println("      }");
    w.println("      return CONVERTER.decode(raw, rs.getStatement().getConnection());");
    w.println("    }");
    w.println();
    w.println("    @", Override.class);
    w.println("    public " + type + " getResult(final ", ResultSet.class, " rs, final int columnIndex) throws ",
        SQLException.class, " {");
    w.println("      " + interType + " raw = rs." + getter + "(columnIndex);");
    w.println("      if (rs.wasNull()) {");
    w.println("        raw = null;");
    w.println("      }");
    w.println("      return CONVERTER.decode(raw, rs.getStatement().getConnection());");
    w.println("    }");
    w.println();
    w.println("    @Override");
    w.println("    public " + type + " getResult(final ", CallableStatement.class,
        " cs, final int columnIndex) throws ", SQLException.class, " {");
    w.println("      " + interType + " raw = cs." + getter + "(columnIndex);");
    w.println("      if (cs.wasNull()) {");
    w.println("        raw = null;");
    w.println("      }");
    w.println("      return CONVERTER.decode(raw, cs.getConnection());");
    w.println("    }");
    w.println();
    w.println("    @", Override.class);
    w.println("    public void setParameter(final ", PreparedStatement.class,
        " ps, final int columnIndex, final " + type + " value, final ", JDBCType.class, " jdbcType)");
    w.println("        throws ", SQLException.class, " {");
    w.println("      " + interType + " raw = CONVERTER.encode(value, ps.getConnection());");
    w.println("      if (raw == null) {");
    w.println("        ps.setNull(columnIndex, jdbcType.TYPE_CODE);");
    w.println("      } else {");
    w.println("        ps." + setter + "(columnIndex, raw);");
    w.println("      }");
    w.println("    }");
    w.println();
    w.println("  }");
    w.println();
  }

  private void writeOrderingEnum() throws IOException {
    w.println("  // DAO ordering");
    w.println();

    w.println("  public enum " + this.getOrderByClassName() + " implements ", OrderBy.class, " {");
    w.println();

    ListWriter lw = new ListWriter(", //\n");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String constantBase = cm.getId().getJavaConstantName();
      String ti = JUtils.escapeJavaString(this.metadata.getId().getRenderedSQLName());
      String ci = JUtils.escapeJavaString(cm.getId().getRenderedSQLName());
      lw.add("    " + constantBase + "(\"" + ti + "\", \"" + ci + "\", true)");
      lw.add("    " + constantBase + "$DESC(\"" + ti + "\", \"" + ci + "\", false)");
      log.fine("*** " + cm.getName() + " -> cm.isCaseSensitiveStringSortable()=" + cm.isCaseSensitiveStringSortable());
      if (cm.isCaseSensitiveStringSortable()) {
        String cici = JUtils.escapeJavaString(cm.renderForCaseInsensitiveOrderBy());

        lw.add("    " + constantBase + "$CASEINSENSITIVE(\"" + ti + "\", \"" + cici + "\", true)");
        lw.add("    " + constantBase + "$CASEINSENSITIVE_STABLE_FORWARD(\"" + ti + "\", \"" + cici + ", " + ci
            + "\", true)");
        lw.add("    " + constantBase + "$CASEINSENSITIVE_STABLE_REVERSE(\"" + ti + "\", \"" + cici + ", " + ci
            + "\", false)");

        lw.add("    " + constantBase + "$DESC_CASEINSENSITIVE(\"" + ti + "\", \"" + cici + "\", false)");
        lw.add("    " + constantBase + "$DESC_CASEINSENSITIVE_STABLE_FORWARD(\"" + ti + "\", \"" + cici + ", " + ci
            + "\", false)");
        lw.add("    " + constantBase + "$DESC_CASEINSENSITIVE_STABLE_REVERSE(\"" + ti + "\", \"" + cici + ", " + ci
            + "\", true)");

      }
    }
    w.println(lw.toString() + ";");
    w.println();

    w.println("    private " + this.getOrderByClassName() + "(final String tableName, final String columnName,");
    w.println("        boolean ascending) {");
    w.println("      this.tableName = tableName;");
    w.println("      this.columnName = columnName;");
    w.println("      this.ascending = ascending;");
    w.println("    }");
    w.println();
    w.println("    private String tableName;");
    w.println("    private String columnName;");
    w.println("    private boolean ascending;");
    w.println();
    w.println("    public String getTableName() {");
    w.println("      return this.tableName;");
    w.println("    }");
    w.println();
    w.println("    public String getColumnName() {");
    w.println("      return this.columnName;");
    w.println("    }");
    w.println();
    w.println("    public boolean isAscending() {");
    w.println("      return this.ascending;");
    w.println("    }");
    w.println();
    w.println("  }");
    w.println();
  }

  // TODO: Nothing to do. Just a marker

  private void writeMetadata() throws IOException {

    String type = this.isTable() ? "Table" : "View";

    Id catalog = this.metadata.getId().getCatalog();
    Id schema = this.metadata.getId().getSchema();
    Id name = this.metadata.getId().getObject();

//    String name = this.metadata.getId().getCanonicalSQLName();

    w.println("  // Database " + type + " metadata");
    w.println();
    w.println("  public static " + this.metadataClassName + " new" + type + "() {");
    w.println("    return new " + this.metadataClassName + "();");
    w.println("  }");
    w.println();
    w.println("  public static " + this.metadataClassName + " new" + type + "(final String alias) {");
    w.println("    return new " + this.metadataClassName + "(alias);");
    w.println("  }");
    w.println();

    w.println("  public static class " + this.metadataClassName + " extends " + type + " {");
    w.println();

    w.println("    // Properties");
    w.println();
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      String liveSQLColumnType = toLiveSQLType(javaType);
      String javaMembername = cm.getId().getJavaMemberName();
      String colName = cm.getId().getCanonicalSQLName();
      String property = cm.getId().getJavaMemberName();
      String javaConverterClass = null;

      String th;
      if (cm.getConverter() != null) {
        javaConverterClass = cm.getConverter().getJavaClass();
        th = TypeHandler.class.getName() + ".of(" + javaConverterClass + ".class, TypeSource.ENTITY_COLUMN)";
      } else {
        th = TypeHandler.class.getName() + ".of(" + javaType + ".class, TypeSource.ENTITY_COLUMN)";
      }

      w.println("    public final " + liveSQLColumnType + " " + javaMembername + " = new " + liveSQLColumnType + "(this" //
          + ", \"" + JUtils.escapeJavaString(colName) + "\"" //
          + ", \"" + JUtils.escapeJavaString(property) + "\"" //
          + ", \"" + JUtils.escapeJavaString(cm.getTypeName()) + "\"" //
          + ", " + cm.getPrecision() + "" //
          + ", " + cm.getScale() + "" //
          + ", " + th + ");");
    }
    w.println();

    w.println("    // Getters");
    w.println();

    w.println("    public ", AllColumns.class, " star() {");
    w.println("      return new ", AllColumns.class, "(" + this.metadata.getColumns().stream()
        .map(c -> "this." + c.getId().getJavaMemberName()).collect(Collectors.joining(", ")) + ");");
    w.println("    }");
    w.println();

    String c = catalog == null ? "null"
        : "Name.of(\"" + JUtils.escapeJavaString(catalog.getCanonicalSQLName()) + "\", " + catalog.isQuoted() + ")";
    String s = schema == null ? "null"
        : "Name.of(\"" + JUtils.escapeJavaString(schema.getCanonicalSQLName()) + "\", " + schema.isQuoted() + ")";
    String n = "Name.of(\"" + JUtils.escapeJavaString(name.getCanonicalSQLName()) + "\", " + name.isQuoted() + ")";

    w.println("    // Constructors");
    w.println();
    w.println("    " + this.metadataClassName + "() {");
    w.println("      super(" + c + ", " + s + ", " + n + ", \"" + type + "\", null);");
    w.println("      initializeColumns();");
    w.println("    }");
    w.println();
    w.println("    " + this.metadataClassName + "(final String alias) {");
    w.println("      super(" + c + ", " + s + ", " + n + ", \"" + type + "\", alias);");
    w.println("      initializeColumns();");
    w.println("    }");
    w.println();

    w.println("    // Initialization");
    w.println();
    w.println("    private void initializeColumns() {");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      w.println("      super.add(this." + cm.getId().getJavaMemberName() + ");");
    }

    w.println("    }");
    w.println();

    w.println("  }");
    w.println();
  }

  private void writeAOPAspect() throws IOException {

    w.println("  // AOP Lazy Loading of Parent Class Aspect");
    w.println();

    w.println("  @", Const.ASPECT);
    w.println("  @", Const.CONFIGURATION);
    w.println("  @", Const.ORDER, "(150)");
    w.println("  public class LazyLoadingAspect {");
    w.println();
    w.println("    @", Const.BEFORE, "(\"execution(* " + this.getFullClassName() + ".get*(..))\")");
    w.println("    public void superclassLoader(", Const.JOIN_POINT, " joinPoint) {");
    w.println("      System.out.println(\"> SuperclassLoader Aspect triggered. joinpoint: \" + joinPoint);");
    w.println("      try {");
    w.println("        " + LazyParentClassLoading.class.getSimpleName() + " target = ("
        + LazyParentClassLoading.class.getSimpleName() + ") joinPoint.getTarget();");
    w.println("        target.loadSuperclass();");
    w.println("      } catch (Exception e) {");
    w.println("        // do nothing");
    w.println("      }");
    w.println("    }");
    w.println();
    w.println("  }");
    w.println();

  }

  // Helpers

  private String resolveType(final ColumnMetadata cm) {
    EnumClass ec = this.generator.getEnum(cm.getEnumMetadata());
    return ec != null ? ec.getFullClassName() : cm.getType().getJavaClassName();
  }

  private String toLiveSQLType(final String javaType) {
    if ("java.lang.Byte".equals(javaType) //
        || "java.lang.Short".equals(javaType) //
        || "java.lang.Integer".equals(javaType) //
        || "java.lang.Long".equals(javaType) //
        || "java.lang.Float".equals(javaType) //
        || "java.lang.Double".equals(javaType) //
        || "java.math.BigInteger".equals(javaType) //
        || "java.math.BigDecimal".equals(javaType) //
    ) {
      return NumberEntityColumn.class.getSimpleName();
    } else if ("java.lang.String".equals(javaType)) {
      return StringEntityColumn.class.getSimpleName();
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
      return DateTimeEntityColumn.class.getSimpleName();
    } else if ("java.lang.Boolean".equals(javaType)) {
      return BooleanEntityColumn.class.getSimpleName();
    } else if ("byte[]".equals(javaType)) {
      return ByteArrayEntityColumn.class.getSimpleName();
    }

    // byte[]
    // java.lang.Object
    // <Custom Converter>
    return ObjectEntityColumn.class.getSimpleName();
  }

  private void writeSelectSequence(final SequenceMethodTag tag) throws IOException, SequencesNotSupportedException {

    w.println("  // sequence " + tag.getSequenceId().getRenderedSQLName());
    w.println();
    w.println(ObjectDAO.renderJavaComment(this.adapter.renderSelectSequence(tag.getSequenceId())));
    w.println();

    w.println("  public long " + tag.getMethod() + "() {");
    w.println("    return (Long) sqlSession.selectOne(");
    w.println("      \"" + this.mapper.getFullMapperIdSelectSequence(tag) + "\");");
    w.println("  }");
    w.println();

  }

  private void writeQuery(final QueryMethodTag tag) throws IOException {

    w.println("  // update " + tag.getMethod());
    w.println();

    ParameterRenderer parameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "#{" + parameter.getName() + "}";
      }
    };
    String sentence = tag.renderSQLSentence(parameterRenderer);
    w.println(renderJavaComment(sentence));

    w.println();

    String methodName = tag.getId().getJavaMemberName();

    ListWriter pdef = new ListWriter(", ");
    ListWriter pcall = new ListWriter(", ");
    for (ParameterTag p : tag.getParameterDefinitions()) {
      pdef.add("final " + p.getJavaType() + " " + p.getName());
      pcall.add(p.getName());
    }
    String paramDef = pdef.toString();

    // parameter class

    if (!tag.getParameterDefinitions().isEmpty()) {
      w.println("  public static class " + this.getParamClassName(tag) + " {");
      for (ParameterTag p : tag.getParameterDefinitions()) {
        w.println("    " + p.getJavaType() + " " + p.getName() + ";");
      }
      w.println("  }");
      w.println();
    }

    // method

    w.print("  public int " + methodName + "(");
    if (!tag.getParameterDefinitions().isEmpty()) {
      w.print(paramDef);
    }
    w.println(") {");
    String objName = null;
    if (!tag.getParameterDefinitions().isEmpty()) {
      objName = provideObjectName(tag.getParameterDefinitions());
      w.println("    " + this.getParamClassName(tag) + " " + objName + " = new " + this.getParamClassName(tag) + "();");
      for (ParameterTag p : tag.getParameterDefinitions()) {
        w.println("    " + objName + "." + p.getName() + " = " + p.getName() + ";");
      }
    }
    w.println("    return this.sqlSession.update(");
    w.print("      \"" + this.mapper.getFullMapperIdUpdate(tag) + "\"");
    if (!tag.getParameterDefinitions().isEmpty()) {
      w.print(", " + objName);
    }
    w.println(");");
    w.println("  }");
    w.println();

  }

  private String provideObjectName(final List<ParameterTag> definitions) {

    Set<String> existing = new HashSet<String>();
    for (ParameterTag p : definitions) {
      existing.add(p.getName().toLowerCase());
    }

    int i = 0;
    while (true) {
      String candidate = "param" + i;
      if (!existing.contains(candidate.toLowerCase())) {
        return candidate;
      }
      i++;
    }

  }

  private String provideParamObjectName(final List<SelectParameterMetadata> definitions) {

    Set<String> existing = new HashSet<String>();
    for (SelectParameterMetadata p : definitions) {
      existing.add(p.getParameter().getName().toLowerCase());
    }

    int i = 0;
    while (true) {
      String candidate = "param" + i;
      if (!existing.contains(candidate.toLowerCase())) {
        return candidate;
      }
      i++;
    }

  }

  // Select Method Tag

  private void writeSelect(final SelectMethodMetadata sm) throws IOException {

    w.println("  // select method: " + sm.getMethod());
    w.println();

    SelectMethodReturnType rt = sm.getReturnType(this.classPackage);

    // render comment

    ParameterRenderer parameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "#{" + parameter.getName() + "}";
      }
    };
    String sentence = sm.renderSQLSentence(parameterRenderer);
    w.println(renderJavaComment(sentence));

    w.println();

    String methodName = sm.getMethod();

    ListWriter pdef = new ListWriter(", ");
//    ListWriter pcall = new ListWriter(", ");
    for (SelectParameterMetadata p : sm.getParameterDefinitions()) {
      String name = p.getParameter().getName();
      if (!p.getParameter().isInternal()) {
        pdef.add("final " + p.getParameter().getJavaType() + " " + name);
//        pcall.add(name);
      }
    }
    String paramDef = pdef.toString();

    // parameter class

    if (!sm.getParameterDefinitions().isEmpty()) {
      w.println("  public static class " + this.getParamClassName(sm) + " {");
      for (SelectParameterMetadata p : sm.getParameterDefinitions()) {
        if (!p.getParameter().isInternal()) {
          w.println("    " + p.getParameter().getJavaType() + " " + p.getParameter().getName() + ";");
        }
      }
      w.println("  }");
      w.println();
    }

    // method

    w.print("  public " + rt.getReturnType() + " " + methodName + "(");
    if (!sm.getParameterDefinitions().isEmpty()) {
      w.print(paramDef);
    }
    w.println(") {");
    String objName = null;
    if (!sm.getParameterDefinitions().isEmpty()) {
      objName = provideParamObjectName(sm.getParameterDefinitions());
      w.println("    " + this.getParamClassName(sm) + " " + objName + " = new " + this.getParamClassName(sm) + "();");
      for (SelectParameterMetadata p : sm.getParameterDefinitions()) {
        if (!p.getParameter().isInternal()) {
          w.println("    " + objName + "." + p.getParameter().getName() + " = " + p.getParameter().getName() + ";");
        }
      }
    }

    String myBatisSelectMethod;
    if (sm.getResultSetMode() == ResultSetMode.LIST) {
      myBatisSelectMethod = "selectList";
    } else if (sm.getResultSetMode() == ResultSetMode.CURSOR) {
      myBatisSelectMethod = "selectCursor";
    } else {
      myBatisSelectMethod = "selectOne";
    }

    log.fine("--> mode=" + sm.getResultSetMode() + ", method=" + myBatisSelectMethod);

    w.print("    return ");

    if (sm.getResultSetMode() == ResultSetMode.CURSOR) {
      w.print("    new "
          + MyBatisCursor.class
          + "<" + rt.getBaseReturnVOType() + ">(");
    }

    w.print("this.sqlSession." + myBatisSelectMethod + "(\"" + this.mapper.getFullSelectMethodStatementId(sm) + "\"");
    if (!sm.getParameterDefinitions().isEmpty()) {
      w.print(", " + objName);
    }
    w.print(")");

    if (sm.getResultSetMode() == ResultSetMode.CURSOR) {
      w.print(")");
    }

    w.println(";");
    w.println("  }");
    w.println();

  }

  private void writeClassFooter() throws IOException {
    w.println("}");
  }

  // Identifiers

  public String getFullClassName() {
    return this.classPackage.getFullClassName(getClassName());
  }

  public String getOrderByFullClassName() {
    return getFullClassName() + "." + getOrderByClassName();
  }

  private String getOrderByClassName() {
    return this.metadata.getId().getJavaClassName() + "OrderBy";
  }

  public String getClassName() {
    return this.myBatisTag.getDaos().generateDAOName(this.metadata.getId());
  }

  public String getMemberName() {
    return SUtil.lowerFirst(this.getClassName());
  }

  public String getParameterClassName() {
    return this.getClassName() + "Parameter";
  }

  public String getSelectByUI(final KeyMetadata ui) {
    return "selectByUI" + ui.toCamelCase(this.layout.getColumnSeam());
  }

  public String getSelectByColumns(final KeyMetadata ui) {
    return "by" + ui.toCamelCase(this.layout.getColumnSeam());
  }

  public String getParamClassName(final QueryMethodTag u) {
    return "Param" + u.getId().getJavaClassName();
  }

  public String getParamClassName(final SelectMethodMetadata sm) {
    return "Param" + sm.getId().getJavaClassName();
  }

  private String getTypeHandlerClassName(final ColumnMetadata cm) {
    return cm.getId().getJavaClassName() + "TypeHandler";
  }

  public String getTypeHandlerFullClassName(final ColumnMetadata cm) {
    return this.getFullClassName() + "$" + getTypeHandlerClassName(cm);
  }

  private Map<SelectMethodMetadata, Map<ColumnMetadata, String>> selectTypeHandlers = new HashMap<SelectMethodMetadata, Map<ColumnMetadata, String>>();
  private Set<String> selectTypeHandlerNames = new HashSet<String>();

  private String getTypeHandlerClassName(final SelectMethodMetadata sm, final ColumnMetadata cm) {
    log.fine("sm=" + sm.getMethod() + " # " + cm.getName());
    String thName = null;
    Map<ColumnMetadata, String> typeHandlers = this.selectTypeHandlers.get(sm);
    if (typeHandlers != null) {
      thName = typeHandlers.get(cm);
    }
    @SuppressWarnings("unused")
    boolean added = false;
    if (thName == null) {
      String base = sm.getMethod() + "_" + cm.getId().getJavaClassName() + "TypeHandler";
      thName = findNextAvailableThName(base);
      if (typeHandlers == null) {
        typeHandlers = new HashMap<ColumnMetadata, String>();
        this.selectTypeHandlers.put(sm, typeHandlers);
      }
      typeHandlers.put(cm, thName);
      this.selectTypeHandlerNames.add(thName);
      added = true;
    }
//    log.fine(this.getClassName() + " / " + cm.getName() + " - TypeHandler=" + thName + " added=" + added
//        + " total=" + this.selectTypeHandlerNames.size());
    return thName;
  }

  private String findNextAvailableThName(final String baseName) {
    if (!this.selectTypeHandlerNames.contains(baseName)) {
      return baseName;
    }
    for (int i = 2; i < Integer.MAX_VALUE; i++) {
      String candidate = baseName + i;
      if (!this.selectTypeHandlerNames.contains(candidate)) {
        return candidate;
      }
    }
    return null;
  }

  public String getTypeHandlerFullClassName(final SelectMethodMetadata sm, final ColumnMetadata cm) {
    return this.getFullClassName() + "$" + getTypeHandlerClassName(sm, cm);
  }

  // Helpers

  public static String toParametersSignature(final KeyMetadata km, final MyBatisSpringGenerator mg)
      throws UnresolvableDataTypeException {
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : km.getColumns()) {
      EnumDataSetMetadata em = cm.getEnumMetadata();
//      log.fine(cm.getName() + " cm.getEnumMetadata()=" + em);
      String javaClassName;
      if (em != null) {
        EnumClass ec = mg.getEnum(em);
        javaClassName = ec.getFullClassName();
        log.fine(" >> enumclass=" + javaClassName);
      } else {
        javaClassName = cm.getType().getJavaClassName();
        log.fine(" >> simpleclass=" + javaClassName);
      }
      lw.add("final " + javaClassName + " " + cm.getId().getJavaMemberName());
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

}
