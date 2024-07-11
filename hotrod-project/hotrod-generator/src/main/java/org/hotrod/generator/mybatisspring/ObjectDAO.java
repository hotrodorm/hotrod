package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.hotrod.config.TableTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.GeneratableObject;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.identifiers.Id;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.ForeignKeyMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodMetadata.SelectMethodReturnType;
import org.hotrod.metadata.SelectParameterMetadata;
import org.hotrod.metadata.VersionControlMetadata;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.exceptions.StaleDataException;
import org.hotrod.runtime.interfaces.DaoForUpdate;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.OrderBy;
import org.hotrod.runtime.interfaces.Selectable;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.metadata.AllColumns;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;
import org.hotrod.runtime.livesql.util.CastUtil;
import org.hotrod.runtime.spring.LazyParentClassLoading;
import org.hotrod.runtime.typesolver.TypeHandler;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.GenUtils;
import org.hotrod.utils.ImportsRenderer;
import org.hotrod.utils.JUtils;
import org.hotrod.utils.ValueTypeFactory;
import org.hotrod.utils.ValueTypeFactory.ValueTypeManager;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.JdbcForeignKey;
import org.nocrala.tools.database.tartarus.core.JdbcKey;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class ObjectDAO extends GeneratableObject {

  // Constants

  private static final Logger log = LogManager.getLogger(ObjectDAO.class);

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

  private TextWriter w;

  // Constructors

  public ObjectDAO(final AbstractDAOTag tag, final DataSetMetadata metadata, final DataSetLayout layout,
      final MyBatisSpringGenerator generator, final DAOType type, final MyBatisSpringTag myBatisTag,
      final DatabaseAdapter adapter, final ObjectAbstractVO avo, final ObjectVO vo, final Mapper mapper) {
    super();
    log.debug("init");
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
    log.debug("f=" + f);

    this.w = null;

    try {
      this.w = fileGenerator.createWriter(f);

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
            log.debug("FK navigation");
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

        log.debug("SQL NAME=" + this.metadata.getId().getCanonicalSQLName() + " this.tag=" + this.tag);
        for (SequenceMethodTag s : this.tag.getSequences()) {
          log.debug("s.getName()=" + s.getSequenceId().getRenderedSQLName());
          writeSelectSequence(s);
        }

        for (QueryMethodTag q : this.tag.getQueries()) {
          log.debug("q.getJavaMethodName()=" + q.getMethod());
          writeQuery(q);
        }

        for (SelectMethodMetadata s : this.metadata.getSelectsMetadata()) {
          writeSelect(s);
        }

      }

      writeClassFooter();

      super.markGenerated();

    } catch (IOException e) {

      throw new UncontrolledException(
          "Could not generate DAO primitives class for DAO defined in the <" + this.tag.getTagName() + "> tag in "
              + this.tag.getSourceLocation().render() + ":\n" + "could not write to file '" + f.getName() + "'.",
          e);
    } catch (UnresolvableDataTypeException e) {
      throw new ControlledException("Could not generate DAO primitives class for DAO defined in the <"
          + this.tag.getTagName() + "> tag in " + this.tag.getSourceLocation().render() + ":\n"
          + "'could not handle columns '" + e.getColumnName() + "' type: " + e.getTypeName());
    } catch (SequencesNotSupportedException e) {
      throw new ControlledException("Could not generate DAO primitives class for DAO defined in the <"
          + this.tag.getTagName() + "> tag in " + this.tag.getSourceLocation().render() + ":\n" + e.getMessage());
    } finally {
      if (this.w != null) {
        try {
          this.w.close();
        } catch (IOException e) {
          throw new UncontrolledException(
              "Could not generate DAO primitives class: could not close file '" + f.getName() + "'.", e);
        }
      }
    }

  }

  private void writeClassHeader() throws IOException {

    // Comment

    println("// Autogenerated by " + Constants.TOOL_NAME + " -- Do not edit.");
    println();

    // Package

    println("package " + this.classPackage.getPackage() + ";");
    println();

    // Imports

    ImportsRenderer imports = new ImportsRenderer();

    imports.add("java.io.Serializable");
    imports.add("java.util.List");
    imports.newLine();
    imports.add("org.apache.ibatis.session.SqlSession");
    imports.add(Cursor.class);
    imports.add(MyBatisCursor.class.getName());
    imports.newLine();

    if (this.metadata.getVersionControlMetadata() != null) {
      imports.add(DaoForUpdate.class);
      imports.add(StaleDataException.class);
    }
    imports.add(DaoWithOrder.class);
    if (this.isTable() || this.isView()) {
      imports.add(UpdateByExampleDao.class);
    }
    imports.add(OrderBy.class);
    if (!this.isTable()) {
      imports.add(Selectable.class);
    }

    imports.newLine();

    if (this.avo != null) {
      imports.add(this.avo.getFullClassName());
    }
    if (this.vo != null) {
      imports.add(this.vo.getFullClassName());
    }

    for (ForeignKeyMetadata ik : this.metadata.getImportedFKs()) {

      String fkc;
      ObjectVO rvo = this.generator.getVO(ik.getRemote().getTableMetadata());
      if (rvo != null) {
        fkc = rvo.getFullClassName();
        imports.add(fkc);
        ObjectDAO dao = this.generator.getDAO(ik.getRemote().getTableMetadata());
        String daoc = dao.getFullClassName();
        imports.add(daoc);
      } else {
        EnumClass ec = this.generator.getEnum(ik.getRemote().getTableMetadata());
        fkc = ec.getFullClassName();
        imports.add(fkc);
      }

    }

    for (ForeignKeyMetadata ek : this.metadata.getExportedFKs()) {

      // log.info(" DAO=" + metadata.getIdentifier().getSQLIdentifier() + " ek="
      // +
      // ek.getRemote().getTableMetadata().getIdentifier().getSQLIdentifier());

      try {
        @SuppressWarnings("unused")
        TableTag tag = (TableTag) ek.getRemote().getTableMetadata().getDaoTag();

        ObjectVO rvo = this.generator.getVO(ek.getRemote().getTableMetadata());
        imports.add(rvo.getFullClassName());

        ObjectDAO dao = this.generator.getDAO(ek.getRemote().getTableMetadata());
        imports.add(dao.getOrderByFullClassName());
        imports.add(dao.getFullClassName());

      } catch (ClassCastException e) {
        // points to an enum - nothing to do.
      }

    }

    imports.newLine();
    // imports.comment("[ now, for the selects... ]");

    for (SelectMethodMetadata sm : this.metadata.getSelectsMetadata()) {
      ClassPackage voPackage = this.myBatisTag.getDaos().getDaoPackage(this.fragmentPackage);
      SelectMethodReturnType rt = sm.getReturnType(voPackage);
      imports.add(rt.getVOFullClassName());
    }
    if (!this.metadata.getSelectsMetadata().isEmpty()) {
      imports.newLine();
    }
    // imports.comment("[ selects done. ]");

    if (this.usesConverters() || hasFKPointingToEnum()) {
      imports.add("java.sql.SQLException");
      imports.add("java.sql.CallableStatement");
      imports.add("java.sql.PreparedStatement");
      imports.add("java.sql.ResultSet");
      imports.add("org.apache.ibatis.type.JdbcType");
      imports.add("org.apache.ibatis.type.TypeHandler");
      imports.add("org.hotrod.runtime.converter.TypeConverter");
      imports.newLine();
    }

    imports.add(Override.class);
    imports.add(Map.class);
    imports.add(ArrayList.class);
    imports.add(HashMap.class);
    imports.newLine();

    imports.add("org.hotrod.runtime.livesql.expressions.ResultSetColumn");
    imports.add("org.hotrod.runtime.spring.SpringBeanObjectFactory");

    imports.add("org.hotrod.runtime.livesql.dialects.LiveSQLDialect");
    imports.add(LiveSQLMapper.class);
    imports.add(CastUtil.class);
    imports.add("javax.annotation.PostConstruct");
    imports.add(DataSource.class);
    imports.add(Column.class);
    imports.add("org.hotrod.runtime.livesql.metadata.NumberColumn");
    imports.add("org.hotrod.runtime.livesql.metadata.StringColumn");
    imports.add("org.hotrod.runtime.livesql.metadata.DateTimeColumn");
    imports.add("org.hotrod.runtime.livesql.metadata.BooleanColumn");
    imports.add("org.hotrod.runtime.livesql.metadata.ByteArrayColumn");
    imports.add("org.hotrod.runtime.livesql.metadata.ObjectColumn");
    imports.add("org.hotrod.runtime.livesql.metadata.Table");
    imports.add("org.hotrod.runtime.livesql.expressions.predicates.Predicate");
    imports.add(AllColumns.class);
    imports.add("org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase");
    imports.add("org.hotrod.runtime.livesql.queries.DeleteWherePhase");
    imports.add("org.hotrod.runtime.livesql.queries.UpdateSetCompletePhase");
    imports.add(Name.class);

    imports.add("org.hotrod.runtime.livesql.metadata.View");
    imports.newLine();

    imports.add(LiveSQLContext.class);

    imports.add("org.springframework.stereotype.Component");
    imports.add("org.springframework.beans.BeansException");
    imports.add("org.springframework.context.annotation.Lazy");
    imports.add("org.springframework.beans.factory.annotation.Autowired");
    imports.add("org.springframework.beans.factory.annotation.Value");
    imports.add("org.springframework.context.ApplicationContext");
    imports.add("org.springframework.context.ApplicationContextAware");
    if (!SUtil.isEmpty(this.layout.getSqlSessionBeanQualifier())) {
      imports.add("org.springframework.beans.factory.annotation.Qualifier");
    }

    imports.newLine();

    if (this.getBundle().getParent() != null) {
      imports.add("org.aspectj.lang.JoinPoint");
      imports.add("org.aspectj.lang.annotation.Aspect");
      imports.add("org.aspectj.lang.annotation.Before");
      imports.add("org.springframework.context.annotation.Configuration");
      imports.add("org.springframework.core.annotation.Order");
      imports.add(LazyParentClassLoading.class);
      imports.newLine();
    }

    this.w.write(imports.render());

    // Signature

    println("@Component");
    println("public class " + this.getClassName() + " implements Serializable, ApplicationContextAware {");
    println();

    // Serial Version UID

    println("  private static final long serialVersionUID = 1L;");
    println();

    // Spring properties

    println("  @Autowired");
    if (!SUtil.isEmpty(this.layout.getSqlSessionBeanQualifier())) {
      println("  @Qualifier(\"" + this.layout.getSqlSessionBeanQualifier() + "\")");
    }
    println("  private SqlSession sqlSession;");
    println();

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
        println("  @Lazy");
        println("  @Autowired");
        println("  private " + className + " " + memberName + ";");
        println();
      }
    }

    println("  @Autowired");
    if (!SUtil.isEmpty(this.layout.getLiveSQLDialectBeanQualifier())) {
      println("  @Qualifier(\"" + this.layout.getLiveSQLDialectBeanQualifier() + "\")");
    }
    println("  private LiveSQLDialect liveSQLDialect;");
    println();

    println("  @Autowired");
    println("  private LiveSQLMapper liveSQLMapper;");
    println();

    println("  @Autowired");
    println("  private SpringBeanObjectFactory springBeanObjectFactory;");
    println();

    println("  @Autowired");
    println("  private DataSource dataSource;");
    println();

    println("  private ApplicationContext applicationContext;");
    println();

    println("  @Override");
    println("  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {");
    println("    this.applicationContext = applicationContext;");
    println("    this.sqlSession.getConfiguration().setObjectFactory(this.springBeanObjectFactory);");
    println("  }");
    println();

    println("  private LiveSQLContext context;");
    println();

    println("  @Value(\"${use.plain.jdbc:false}\")");
    println("  private boolean usePlainJDBC;");
    println();

    println("  @PostConstruct");
    println("  public void initializeContext() {");
    println(
        "    this.context = new LiveSQLContext(this.liveSQLDialect, this.sqlSession, this.liveSQLMapper, this.usePlainJDBC, this.dataSource);");
    println("  }");
    println();

  }

  private static final String SELECT_BY_PK_METHOD = "select";

  private void writeSelectByPK(final MyBatisSpringGenerator mg) throws IOException, UnresolvableDataTypeException {
    if (this.metadata.getPK() == null) {
      println("  // no select by PK generated, since the table does not have a PK.");
      println();
      return;
    }

    println("  // select by primary key");
    println();
    selectByUniqueKey(mg, this.metadata.getPK(), SELECT_BY_PK_METHOD, this.mapper.getFullMapperIdSelectByPK());
  }

  private void selectByUniqueKey(final MyBatisSpringGenerator mg, final KeyMetadata key, final String method,
      final String mapperQuery) throws UnresolvableDataTypeException, IOException {
    String paramsSignature = toParametersSignature(key, mg);
    String avoc = this.avo.getFullClassName();
    String voc = this.vo.getFullClassName();

    print("  public " + voc + " " + method + "(");
    print(paramsSignature);
    print(") ");
    println("{");

    for (ColumnMetadata cm : key.getColumns()) {
      String m = cm.getId().getJavaMemberName();
      println("    if (" + m + " == null)");
      println("      return null;");
    }
    println("    " + voc + " vo = new " + voc + "();");
    for (ColumnMetadata cm : key.getColumns()) {
      String m = cm.getId().getJavaMemberName();
      String setter = cm.getId().getJavaSetter();
      println("    vo." + setter + "(" + m + ");");
    }

    println("    return this.sqlSession.selectOne(\"" + mapperQuery + "\", vo);");
    println("  }");
    println();
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
          println("  // select by unique indexes");
          println();
        }

        String camelCase = ui.toCamelCase(this.layout.getColumnSeam());
        String method = "selectByUI" + camelCase;
        selectByUniqueKey(mg, ui, method, this.mapper.getFullMapperIdSelectByUI(ui));

      }
    }

    if (first) {
      println("  // select by unique indexes: no unique indexes found"
          + (this.metadata.getPK() != null ? " (besides the PK)" : "") + " -- skipped");
      println();
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
    println("  // Row Parser");
    println();
    println("  public " + voClassName + " parseRow(Map<String, Object> m) {");
    println("    return parseRow(m, null, null);");
    println("  }");
    println();
    println("  public " + voClassName + " parseRow(Map<String, Object> m, String prefix) {");
    println("    return parseRow(m, prefix, null);");
    println("  }");
    println();
    println("  public " + voClassName + " parseRow(Map<String, Object> m, String prefix, String suffix) {");
    println("    " + voClassName + " mo = this.applicationContext.getBean(" + voClassName + ".class);");
    println("    String p = prefix == null ? \"\": prefix;");
    println("    String s = suffix == null ? \"\": suffix;");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      String property = cm.getId().getJavaMemberName();

      if (cm.getConverter() != null) {
        ConverterTag ct = cm.getConverter();

        println(
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
        println("    mo." + cm.getId().getJavaSetter() + "(CastUtil.to" + st + "((Number) m.get(p + \""
            + JUtils.escapeJavaString(property) + "\" + s)));");
      } else if ("java.lang.Object".equals(javaType)) {
        println(
            "    mo." + cm.getId().getJavaSetter() + "(m.get(p + \"" + JUtils.escapeJavaString(property) + "\" + s));");
      } else {
        println("    mo." + cm.getId().getJavaSetter() + "((" + javaType + ") m.get(p + \""
            + JUtils.escapeJavaString(property) + "\" + s));");
      }

    }

    println("    return mo;");
    println("  }");
    println();

  }

  private void writeSelectByExample() throws IOException {
    println("  // select by example");
    println();

    String avoClassName = this.avo.getFullClassName();
    String voClassName = this.vo.getFullClassName();

    println("  public List<" + voClassName + "> select(final " + avoClassName + " example, final "
        + this.getOrderByClassName() + "... orderBies)");
    print("      ");
    println("{");
    println("    DaoWithOrder<" + avoClassName + ", " + this.getOrderByClassName() + "> dwo = //");
    println("        new DaoWithOrder<>(example, orderBies);");
    println("    return this.sqlSession.selectList(\"" + this.mapper.getFullMapperIdSelectByExample() + "\", dwo);");
    println("  }");
    println();

    println("  public Cursor<" + voClassName + "> selectCursor(final " + avoClassName + " example, final "
        + this.getOrderByClassName() + "... orderBies)");
    print("      ");
    println("{");
    println("    DaoWithOrder<" + avoClassName + ", " + this.getOrderByClassName() + "> dwo = //");
    println("        new DaoWithOrder<>(example, orderBies);");
    println("    return new " + MyBatisCursor.class.getSimpleName() + "<" + voClassName
        + ">(this.sqlSession.selectCursor(\"" + this.mapper.getFullMapperIdSelectByExample() + "\", dwo));");
    println("  }");
    println();
  }

  private void writeSelectByCriteria() throws IOException {
    println("  // select by criteria");
    println();

    String daoClassName = this.getClassName();
    String voFullClassName = this.vo.getFullClassName();
    String mapperName = this.mapper.getFullMapperIdSelectByCriteria();

    println("  public CriteriaWherePhase<" + voFullClassName + "> select(final " + daoClassName + "."
        + this.metadataClassName + " from,");
    println("      final Predicate predicate) {");
    println("    return new CriteriaWherePhase<" + voFullClassName + ">(this.context, \"" + mapperName + "\",");
    println("        from, predicate);");
    println("  }");

    println();
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

      println("  // select parent(s) by FKs: no imported keys found -- skipped");
      println();

    } else {

      println("  // select parent(s) by FKs");
      println();

      // Group by remote table.

      // Also, get distinct foreign keys only, since multiple identical foreign
      // keys can be registered in the database. This behavior has been
      // observed in PostgreSQL.

      log.debug("DAO: " + this.getClassName() + " -- this.metadata.getImportedFKs().size()="
          + this.metadata.getImportedFKs().size() + " --  fkSelectors.size()=" + fkSelectors.size());

      for (DataSetMetadata ds : fkSelectors.keySet()) {
        ObjectVO vo = this.generator.getVO(ds);
        if (vo != null) { // points to a table, not an enum
          ObjectDAO dao = this.generator.getDAO(ds);

          String selectParentPhaseClassName = "SelectParent" + vo.getJavaClassIdentifier() + "Phase";
          String voClassName = this.vo.getClassName();

          println("  public " + selectParentPhaseClassName + " selectParent" + vo.getJavaClassIdentifier() + "Of(final "
              + voClassName + " vo) {");
          println("    return new " + selectParentPhaseClassName + "(vo);");
          println("  }");
          println();

          println("  public class " + selectParentPhaseClassName + " {");
          println();
          println("    private " + voClassName + " vo;");
          println();
          println("    " + selectParentPhaseClassName + "(final " + voClassName + " vo) {");
          println("      this.vo = vo;");
          println("    }");
          println();

          Set<KeyMetadata> fromKeys = new HashSet<KeyMetadata>();

          for (ForeignKeyMetadata fkm : fkSelectors.get(ds)) {

            if (!fromKeys.contains(fkm.getLocal())) {
              fromKeys.add(fkm.getLocal());
              String fromKey = fkm.getLocal().toCamelCase(this.layout.getColumnSeam());
              String fromPhaseClassName = "SelectParent" + vo.getJavaClassIdentifier() + "From" + fromKey + "Phase";
              String fromMethod = "from" + fromKey;

              println("    public " + fromPhaseClassName + " " + fromMethod + "() {");
              println("      return new " + fromPhaseClassName + "(this.vo);");
              println("    }");
              println();
            }

          }

          println("  }");
          println();

          fromKeys.clear();

          for (ForeignKeyMetadata fkm : fkSelectors.get(ds)) {

            if (!fromKeys.contains(fkm.getLocal())) {
              fromKeys.add(fkm.getLocal());
              String fromKey = fkm.getLocal().toCamelCase(this.layout.getColumnSeam());
              String fromPhaseClassName = "SelectParent" + vo.getJavaClassIdentifier() + "From" + fromKey + "Phase";

              println("  public class " + fromPhaseClassName + " {");
              println();
              println("    private " + voClassName + " vo;");
              println();
              println("    " + fromPhaseClassName + "(final " + voClassName + " vo) {");
              println("      this.vo = vo;");
              println("    }");
              println();

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

                  println("    public " + vo.getClassName() + " " + toMethod + "() {");
                  String memberPrefix = dao.getClassName().equals(currentDAO.getClassName()) ? ""
                      : (dao.getMemberName() + ".");
                  println("      return " + memberPrefix + selectMethod + "(" + params + ");");
                  println("    }");
                  println();
                }
              }

              println("  }");
              println();

            }
          }

        } else {
          EnumClass ec = this.generator.getEnum(ds);
          if (ec != null) {
            for (ForeignKeyMetadata fkm : fkSelectors.get(ds)) {
              ListWriter lw = new ListWriter(", ");
              for (ColumnMetadata cm : fkm.getLocal().getColumns()) {
                lw.add(cm.getColumnName());
              }
              println("  // --- no select parent for FK column" + (fkm.getLocal().getColumns().size() > 1 ? "s" : "")
                  + " (" + lw.toString() + ") since it points to the enum table "
                  + fkm.getRemote().getTableMetadata().getId().getRenderedSQLName());
              println();
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
      throw new CannotConvertTypeException("Cannot navigate foreign key relationship from column "
          + fromColumn.getTableName() + "." + fromColumn.getColumnName() + " to column " + toColumn.getTableName() + "."
          + toColumn.getColumnName() + ": " + e.getMessage());
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

      println("  // select children by FKs: no exported FKs found -- skipped");
      println();

    } else {

      println("  // select children by FKs");
      println();

      // Group by remote table.

      // Also, get distinct foreign keys only, since multiple identical foreign
      // keys can be registered in the database. This behavior has been observed
      // in PostgreSQL.

      for (DataSetMetadata ds : this.efkSelectors.keySet()) {

        ObjectVO vo = this.generator.getVO(ds);
        ObjectDAO dao = this.generator.getDAO(ds);

        String selectChildrenPhaseClassName = "SelectChildren" + vo.getJavaClassIdentifier() + "Phase";

        println("  public " + selectChildrenPhaseClassName + " selectChildren" + vo.getJavaClassIdentifier()
            + "Of(final " + this.vo.getClassName() + " vo) {");
        println("    return new " + selectChildrenPhaseClassName + "(vo);");
        println("  }");
        println();

        println("  public class " + selectChildrenPhaseClassName + " {");
        println();
        println("    private " + this.vo.getClassName() + " vo;");
        println();
        println("    " + selectChildrenPhaseClassName + "(final " + this.vo.getClassName() + " vo) {");
        println("      this.vo = vo;");
        println("    }");
        println();

        Set<KeyMetadata> fromKeys = new HashSet<KeyMetadata>();

        for (ForeignKeyMetadata tfk : this.efkSelectors.get(ds)) {
          if (!fromKeys.contains(tfk.getLocal())) {
            fromKeys.add(tfk.getLocal());
            String fromKey = tfk.getLocal().toCamelCase(this.layout.getColumnSeam());
            String fromPhaseClassName = "SelectChildren" + vo.getJavaClassIdentifier() + "From" + fromKey + "Phase";
            String fromMethod = "from" + fromKey;

            println("    public " + fromPhaseClassName + " " + fromMethod + "() {");
            println("      return new " + fromPhaseClassName + "(this.vo);");
            println("    }");
            println();
          }
        }

        println("  }");
        println();

        fromKeys.clear();

        for (ForeignKeyMetadata tfk : this.efkSelectors.get(ds)) {
          if (!fromKeys.contains(tfk.getLocal())) {
            fromKeys.add(tfk.getLocal());

            String fromKey = tfk.getLocal().toCamelCase(this.layout.getColumnSeam());
            String fromPhaseClassName = "SelectChildren" + vo.getJavaClassIdentifier() + "From" + fromKey + "Phase";

            println("  public class " + fromPhaseClassName + " {");
            println();
            println("    private " + this.vo.getClassName() + " vo;");
            println();
            println("    " + fromPhaseClassName + "(final " + this.vo.getClassName() + " vo) {");
            println("      this.vo = vo;");
            println("    }");
            println();

            for (ForeignKeyMetadata fkm2 : this.efkSelectors.get(ds)) {
              if (fkm2.getLocal().equals(tfk.getLocal())) {

                // TODO: fix remote seam

                fkm2.getRemote().getTableMetadata();
                writeFKChildrenToMethod(currentDAO, vo, dao, fkm2, false);
                writeFKChildrenToMethod(currentDAO, vo, dao, fkm2, true);

              }
            }

            println("  }");
            println();
          }
        }

      }

    }

  }

  private void writeFKChildrenToMethod(final ObjectDAO currentDAO, final ObjectVO vo, final ObjectDAO dao,
      final ForeignKeyMetadata fkm2, final boolean useCursor) throws IOException, ControlledException {
    String toMethod = (useCursor ? "cursorTo" : "to") + fkm2.getRemote().toCamelCase(dao.layout.getColumnSeam());

    String returnType = (useCursor ? "Cursor" : "List") + "<" + vo.getClassName() + ">";
    println("    public " + returnType + " " + toMethod + "(final " + vo.getJavaClassIdentifier()
        + "OrderBy... orderBies) {");
    println("      " + vo.getClassName() + " example = new " + vo.getClassName() + "();");

    Iterator<ColumnMetadata> lit = fkm2.getLocal().getColumns().iterator();
    Iterator<ColumnMetadata> rit = fkm2.getRemote().getColumns().iterator();

    while (lit.hasNext() && rit.hasNext()) {
      ColumnMetadata lcm = lit.next();
      ColumnMetadata rcm = rit.next();
      try {
        println("      example.set" + rcm.getId().getJavaClassName() + "("
            + renderCast("this.vo.get" + lcm.getId().getJavaClassName() + "()", lcm, rcm) + ");");
      } catch (CannotConvertTypeException e) {
        throw new ControlledException(e.getMessage());
      }
    }
    String memberPrefix = dao.getClassName().equals(currentDAO.getClassName()) ? "" : (dao.getMemberName() + ".");
    String selectMethod = useCursor ? "selectCursor" : "select";
    println("      return " + memberPrefix + selectMethod + "(example, orderBies);");
    println("    }");
    println();
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

    println("  // insert");
    println();

    String voClassName = this.avo.getFullClassName();
    String moClassName = this.vo.getFullClassName();
    if (extraInsert) {
      print("  public " + moClassName + " insert(final " + voClassName + " vo) ");
      println("{");
      println("    return insert(vo, false);");
      println("  }");
      println();
    }

    print("  public " + moClassName + " insert(final " + voClassName + " vo");
    if (extraInsert) {
      print(", final boolean retrieveDefaults");
    }
    print(") ");
    println("{");

    VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();

    if (vcm != null) {
      ColumnMetadata cm = vcm.getColumnMetadata();
      String literalValue = renderNumericLiteral(cm.getType().getValueRange().getInitialValue(),
          cm.getType().getJavaClassName());
      println("    vo." + cm.getId().getJavaMemberName() + " = " + literalValue + ";");
    }

    // Decide on the mapper id

    if (extraInsert) {
      println("    String id = retrieveDefaults ? \"" + this.mapper.getFullMapperIdInsertRetrievingDefaults()
          + "\" : \"" + this.mapper.getFullMapperIdInsert() + "\";");
    } else {
      println("    String id = \"" + this.mapper.getFullMapperIdInsert() + "\";");
    }

    // Choose insert variant

    if (identities == 0) {
      if (sequences == 0) { // no sequences, no identities
        println("    this.sqlSession.insert(id, vo);");
        this.writeVOToModel();
        println("    return mo;");
      } else { // sequences only
        if (integratesSequences) {
          writeInsertIntegrated(true, false, extraInsert);
          this.writeVOToModel();
          println("    return mo;");
        } else {
          println("    this.sqlSession.insert(id, vo);");
          this.writeVOToModel();
          println("    return mo;");
        }
      }
    } else {
      if (sequences == 0) { // identities only
        if (integratesIdentities) {
          writeInsertIntegrated(false, true, extraInsert);
          this.writeVOToModel();
          println("    return mo;");
        } else {
          println("    this.sqlSession.insert(id, vo);");
          this.writeVOToModel();
          println("    return mo;");
        }
      } else { // sequences & identities
        if (integratesSequences && integratesIdentities) {
          writeInsertIntegrated(true, true, extraInsert);
          this.writeVOToModel();
          println("    return mo;");
        } else if (integratesIdentities) {
          writeSequencesPreFetch();
          writeInsertIntegrated(false, true, extraInsert);
          this.writeVOToModel();
          println("    return mo;");
        } else if (integratesSequences) {
          writeInsertIntegrated(true, false, extraInsert);
          writeIdentitiesPostFetch();
          this.writeVOToModel();
          println("    return mo;");
        } else {
          writeSequencesPreFetch();
          println("    int rows = this.sqlSession.insert(id, vo);");
          writeIdentitiesPostFetch();
          this.writeVOToModel();
          println("    return mo;");
        }

      }
    }

    println("  }");
    println();

  }

  private void writeVOToModel() throws IOException {
    String moClassName = this.vo.getFullClassName();
//    println("    " + moClassName + " mo = new " + moClassName + "();");
    println("    " + moClassName + " mo = springBeanObjectFactory.create(" + moClassName + ".class);"); // Spring bean
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      println("    mo." + cm.getId().getJavaSetter() + "(vo." + cm.getId().getJavaGetter() + "());");
    }
  }

  private void writeInsertIntegrated(final boolean integratesSequences, final boolean integratesIdentities,
      final boolean extraInsert) throws IOException {
    if (this.adapter.integratesUsingQuery()) {
      String voClassName = this.vo.getFullClassName();
      println("    " + voClassName + " values = this.sqlSession.selectOne(id, vo);");
      println("    int rows = 1;");
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        String prop = cm.getId().getJavaMemberName();
        if (cm.getSequenceId() != null && integratesSequences
            || cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity() && integratesIdentities) {
          println("    vo." + prop + " = values." + prop + ";");
        } else if (extraInsert) {
          println("    if (retrieveDefaults) {");
          println("      vo." + prop + " = values." + prop + ";");
          println("    }");
        }
      }
    } else {
      println("    int rows = this.sqlSession.insert(id, vo);");
    }
  }

  private void writeSequencesPreFetch() throws IOException {
    String voClassName = this.vo.getFullClassName();
    println("    " + voClassName + " sequences = this.sqlSession.selectOne(\""
        + this.mapper.getFullMapperIdSequencesPreFetch() + "\");");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getSequenceId() != null) {
        String prop = cm.getId().getJavaMemberName();
        println("    vo." + prop + " = sequences." + prop + ";");
      }
    }
  }

  private void writeIdentitiesPostFetch() throws IOException {
    String voClassName = this.vo.getFullClassName();
    println("    " + voClassName + " identities = this.sqlSession.selectOne(\""
        + this.mapper.getFullMapperIdIdentitiesPostFetch() + "\");");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity()) {
        String prop = cm.getId().getJavaMemberName();
        println("    vo." + prop + " = identities." + prop + ";");
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
      println("  // no update by PK generated, since the table does not have a PK.");
      println();
    } else {

      boolean useVersionControl = this.metadata.getVersionControlMetadata() != null;

      println("  // update by PK");
      println();

      String voClassName = this.vo.getFullClassName();
      if (useVersionControl) {
        VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();
        ColumnMetadata cm = vcm.getColumnMetadata();
        PropertyType pt = cm.getType();
        ValueRange range = pt.getValueRange();
        print("  public int " + UPDATE_BY_PK_METHOD + "(final " + voClassName + " vo) ");
        println("{");
        println("    long currentVersion = vo." + cm.getId().getJavaGetter() + "();");

        String minValue = renderNumericLiteral(range.getMinValue(), cm.getType().getJavaClassName());
        String maxValue = renderNumericLiteral(range.getMaxValue(), cm.getType().getJavaClassName());

        println("    DaoForUpdate<" + voClassName + "> u = new DaoForUpdate<" + voClassName + ">(vo, currentVersion, "
            + minValue + ", " + maxValue + ");");
        println("    int rows = this.sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByPK() + "\", u);");
        println("    if (rows != 1) {");
        println("      throw new StaleDataException(\"Could not update row on table "
            + this.metadata.getId().getCanonicalSQLName() + " with version \" + currentVersion");
        println("          + \" since it had already been updated by another process.\");");
        println("    }");
        println("    vo." + cm.getId().getJavaGetter() + "() = (" + pt.getPrimitiveClassJavaType()
            + ") u.getNextVersionValue();");
        println("    return rows;");
      } else {
        print("  public int " + UPDATE_BY_PK_METHOD + "(final " + voClassName + " vo) ");
        println("{");
        for (ColumnMetadata cm : this.metadata.getPK().getColumns()) {
          println("    if (vo." + cm.getId().getJavaGetter() + "() == null) return 0;");
        }
        println("    return this.sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByPK() + "\", vo);");
      }

      println("  }");
      println();
    }

  }

  private void writeInsertByExample() throws IOException {
    println("  // insert by example");
    println();
    String voClassName = this.avo.getFullClassName();
    print("  public int insertByExample(final " + voClassName + " example) ");
    println("{");
    println("    return sqlSession.insert(\"" + this.mapper.getFullMapperIdInsertByExample() + "\", example);");
    println("  }");
    println();

  }

  private void writeUpdateByExample() throws IOException {
    println("  // update by example");
    println();
    String voClassName = this.avo.getFullClassName();
    println("  public int update(final " + voClassName + " example, final " + voClassName + " updateValues) {");
    println("    UpdateByExampleDao<" + voClassName + "> fvd = //");
    println("      new UpdateByExampleDao<" + voClassName + ">(example, updateValues);");
    println("    return this.sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByExample() + "\", fvd);");
    println("  }");
    println();
  }

  private void writeUpdateByCriteria() throws IOException {
    println("  // update by criteria");
    println();
    String voClassName = this.avo.getFullClassName();
    String mapperName = this.mapper.getFullMapperIdUpdateByCriteria();

    // writeUpdateByCriteriaVariation(false, voClassName, mapperName);
    writeUpdateByCriteriaVariation(true, voClassName, mapperName);

    println();
  }

  private void writeUpdateByCriteriaVariation(final boolean useEntity, final String voClassName,
      final String mapperName) throws IOException {
    print("  public UpdateSetCompletePhase update(final " + voClassName + " updateValues, ");
    if (useEntity) {
      print("final " + this.getClassName() + "." + this.metadataClassName + " tableOrView, ");
    }
    println("final Predicate predicate) {");

    println("    Map<String, Object> values = new HashMap<>();");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String colName = cm.getId().getRenderedSQLName();
      if (colName.startsWith("\"") && colName.endsWith("\"")) {
        colName = "\\\"" + colName.substring(1, colName.length() - 1) + "\\\"";
      }
      println("    if (updateValues." + cm.getId().getJavaGetter() + "() != null) values.put(\"" + colName
          + "\", updateValues." + cm.getId().getJavaGetter() + "());");
    }

    print("    return new UpdateSetCompletePhase(");
    print("this.context, ");
    print("\"" + mapperName + "\", ");
    if (useEntity) {
      print("tableOrView, ");
    } else {
      print(this.getClassName() + (this.isTable() ? ".newTable()" : ".newView()") + ", ");
    }
    println(" predicate, values);");

    println("  }");
    println();
  }

  private static final String DELETE_BY_PK_METHOD = "delete";

  private void writeDeleteByPK(final MyBatisSpringGenerator mg) throws IOException, UnresolvableDataTypeException {
    KeyMetadata pk = this.metadata.getPK();
    if (pk == null) {
      println("  // no delete by PK generated, since the table does not have a PK.");
      println();
    } else {

      boolean useVersionControl = this.metadata.getVersionControlMetadata() != null;

      println("  // delete by PK");
      println();

      String voClassName = this.vo.getFullClassName();

      String paramsSignature = toParametersSignature(pk, mg);
      print("  public int " + DELETE_BY_PK_METHOD + "(" + paramsSignature + ") ");
      println("{");

      String voc = this.vo.getFullClassName();
      for (ColumnMetadata cm : pk.getColumns()) {
        String m = cm.getId().getJavaMemberName();
        println("    if (" + m + " == null) return 0;");
      }
      println("    " + voc + " vo = new " + voc + "();");
      for (ColumnMetadata cm : pk.getColumns()) {
        String m = cm.getId().getJavaMemberName();
        String setter = cm.getId().getJavaSetter();
        println("    vo." + setter + "(" + m + ");");
      }

      if (useVersionControl) {
        VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();
        ColumnMetadata vccm = vcm.getColumnMetadata();
        println("    int rows = this.sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByPK() + "\", vo);");
        println("    if (rows != 1) {");
        println("      throw new StaleDataException(\"Could not delete row on table "
            + this.metadata.getId().getCanonicalSQLName() + " with version \" + vo."
            + vccm.getId().getJavaMemberName());
        println("          + \" since it had already been updated or deleted by another process.\");");
        println("    }");
        println("    return rows;");
        println("  }");
      } else {
        for (ColumnMetadata cm : pk.getColumns()) {
          println("    if (vo." + cm.getId().getJavaGetter() + "() == null) return 0;");
        }
        println("    return this.sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByPK() + "\", vo);");
        println("  }");
      }

      println();
    }
  }

  private void writeDeleteByExample() throws IOException {
    println("  // delete by example");
    println();
    String voClassName = this.avo.getFullClassName();
    println("  public int delete(final " + voClassName + " example) {");
    println("    return this.sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByExample() + "\", example);");
    println("  }");
    println();
  }

  private void writeDeleteByCriteria() throws IOException {
    println("  // delete by criteria");
    println();

    String daoClassName = this.getClassName();
    String mapperName = this.mapper.getFullMapperIdDeleteByCriteria();

    // writeDeleteByCriteriaVariation(false, daoClassName, mapperName);
    writeDeleteByCriteriaVariation(true, daoClassName, mapperName);
  }

  private void writeDeleteByCriteriaVariation(final boolean useFrom, final String daoClassName, final String mapperName)
      throws IOException {
    print("  public DeleteWherePhase delete(");
    if (useFrom) {
      print("final " + daoClassName + "." + this.metadataClassName + " from, ");
    }
    println("final Predicate predicate) {");

    print("    return new DeleteWherePhase(");
    print("this.context, ");
    print("\"" + mapperName + "\", ");
    if (useFrom) {
      print("from, ");
    } else {
      print(daoClassName + (this.isTable() ? ".newTable()" : ".newView()") + ", ");
    }
    println("predicate);");

    println("  }");
    println();
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
                + this.metadata.getId().getCanonicalSQLName() + "'. Foreign key column '" + cm.getColumnName()
                + "' point to an enum type and must be of one of the following simple types:\n"
                + ListWriter.render(ValueTypeFactory.getSupportedTypes(), " - ", "", "\n"));
          }

          println("  // TypeHandler for enum-FK column " + cm.getColumnName() + ".");
          println();
          println("  public static class " + typeHandlerClassName + " implements TypeHandler<" + type + "> {");
          println();
          println("    @Override");
          println(
              "    public " + type + " getResult(final ResultSet rs, final String columnName) throws SQLException {");
          println("      " + interType + " value = " + tm.renderJdbcGetter("rs", "columnName") + ";");
          println("      if (rs.wasNull()) {");
          println("        value = null;");
          println("      }");
          println("      return " + type + ".decode("
              + GenUtils.convertPropertyType(interType, ec.getValueColumn().getClassName(), "value") + ");");

          println("    }");
          println();
          println("    @Override");
          println("    public " + type + " getResult(final ResultSet rs, final int columnIndex) throws SQLException {");
          println("      " + interType + " value = " + tm.renderJdbcGetter("rs", "columnIndex") + ";");
          println("      if (rs.wasNull()) {");
          println("        value = null;");
          println("      }");
          println("      return " + type + ".decode("
              + GenUtils.convertPropertyType(interType, ec.getValueColumn().getClassName(), "value") + ");");
          println("    }");
          println();
          println("    @Override");
          println("    public " + type
              + " getResult(final CallableStatement cs, final int columnIndex) throws SQLException {");
          println("      " + interType + " value = " + tm.renderJdbcGetter("cs", "columnIndex") + ";");
          println("      if (cs.wasNull()) {");
          println("        value = null;");
          println("      }");
          println("      return " + type + ".decode("
              + GenUtils.convertPropertyType(interType, ec.getValueColumn().getClassName(), "value") + ");");
          println("    }");
          println();
          println("    @Override");
          println("    public void setParameter(final PreparedStatement ps, final int columnIndex, final " + type
              + " v, final JdbcType jdbcType)");
          println("        throws SQLException {");
          println("      " + ec.getValueColumn().getClassName() + " importedValue = " + type + ".encode(v);");
          println("      " + interType + " localValue = "
              + GenUtils.convertPropertyType(ec.getValueColumn().getClassName(), interType, "importedValue") + ";");

          println("      if (localValue == null) {");
          println("        ps.setNull(columnIndex, jdbcType.TYPE_CODE);");
          println("      } else {");
          println("        " + tm.renderJdbcSetter("ps", "columnIndex", "localValue", "param"));
          println("      }");
          println("    }");
          println();
          println("  }");
          println();

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
        log.debug("WRITING TYPEHANDLER '" + thName + "'");
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

    println("  // TypeHandler for " + (property != null ? "property " + property : "column " + cm.getColumnName())
        + " using Converter " + converter + ".");
    println();
    println("  public static class " + typeHandlerClassName + " implements TypeHandler<" + type + "> {");
    println();
    println(
        "    private static final TypeConverter<" + interType + ", " + type + "> CONVERTER = new " + converter + "();");
    println();
    println("    @Override");
    println("    public " + type + " getResult(final ResultSet rs, final String columnName) throws SQLException {");
    println("      " + interType + " raw = rs." + getter + "(columnName);");
    println("      if (rs.wasNull()) {");
    println("        raw = null;");
    println("      }");
    println("      return CONVERTER.decode(raw, rs.getStatement().getConnection());");
    println("    }");
    println();
    println("    @Override");
    println("    public " + type + " getResult(final ResultSet rs, final int columnIndex) throws SQLException {");
    println("      " + interType + " raw = rs." + getter + "(columnIndex);");
    println("      if (rs.wasNull()) {");
    println("        raw = null;");
    println("      }");
    println("      return CONVERTER.decode(raw, rs.getStatement().getConnection());");
    println("    }");
    println();
    println("    @Override");
    println(
        "    public " + type + " getResult(final CallableStatement cs, final int columnIndex) throws SQLException {");
    println("      " + interType + " raw = cs." + getter + "(columnIndex);");
    println("      if (cs.wasNull()) {");
    println("        raw = null;");
    println("      }");
    println("      return CONVERTER.decode(raw, cs.getConnection());");
    println("    }");
    println();
    println("    @Override");
    println("    public void setParameter(final PreparedStatement ps, final int columnIndex, final " + type
        + " value, final JdbcType jdbcType)");
    println("        throws SQLException {");
    println("      " + interType + " raw = CONVERTER.encode(value, ps.getConnection());");
    println("      if (raw == null) {");
    println("        ps.setNull(columnIndex, jdbcType.TYPE_CODE);");
    println("      } else {");
    println("        ps." + setter + "(columnIndex, raw);");
    println("      }");
    println("    }");
    println();
    println("  }");
    println();
  }

  private void writeOrderingEnum() throws IOException {
    println("  // DAO ordering");
    println();

    println("  public enum " + this.getOrderByClassName() + " implements OrderBy {");
    println();

    ListWriter lw = new ListWriter(", //\n");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String constantBase = cm.getId().getJavaConstantName();
      String ti = JUtils.escapeJavaString(this.metadata.getId().getRenderedSQLName());
      String ci = JUtils.escapeJavaString(cm.getId().getRenderedSQLName());
      lw.add("    " + constantBase + "(\"" + ti + "\", \"" + ci + "\", true)");
      lw.add("    " + constantBase + "$DESC(\"" + ti + "\", \"" + ci + "\", false)");
      log.debug(
          "*** " + cm.getColumnName() + " -> cm.isCaseSensitiveStringSortable()=" + cm.isCaseSensitiveStringSortable());
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
    println(lw.toString() + ";");
    println();

    println("    private " + this.getOrderByClassName() + "(final String tableName, final String columnName,");
    println("        boolean ascending) {");
    println("      this.tableName = tableName;");
    println("      this.columnName = columnName;");
    println("      this.ascending = ascending;");
    println("    }");
    println();
    println("    private String tableName;");
    println("    private String columnName;");
    println("    private boolean ascending;");
    println();
    println("    public String getTableName() {");
    println("      return this.tableName;");
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

  // TODO: Nothing to do. Just a marker

  private void writeMetadata() throws IOException {

    String type = this.isTable() ? "Table" : "View";

    Id catalog = this.metadata.getId().getCatalog();
    Id schema = this.metadata.getId().getSchema();
    Id name = this.metadata.getId().getObject();

//    String name = this.metadata.getId().getCanonicalSQLName();

    println("  // Database " + type + " metadata");
    println();
    println("  public static " + this.metadataClassName + " new" + type + "() {");
    println("    return new " + this.metadataClassName + "();");
    println("  }");
    println();
    println("  public static " + this.metadataClassName + " new" + type + "(final String alias) {");
    println("    return new " + this.metadataClassName + "(alias);");
    println("  }");
    println();

    println("  public static class " + this.metadataClassName + " extends " + type + " {");
    println();

    println("    // Properties");
    println();
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String javaType = resolveType(cm);
      String liveSQLColumnType = toLiveSQLType(javaType);
      String javaMembername = cm.getId().getJavaMemberName();
      String colName = cm.getId().getCanonicalSQLName();
      String property = cm.getId().getJavaMemberName();
      String javaConverterClass = null;
      String rawClass = null;
      if (cm.getConverter() != null) {
        javaConverterClass = cm.getConverter().getJavaClass();
        rawClass = cm.getConverter().getJavaRawType();
      }

      String th = TypeHandler.class.getName() + ".of(" + javaType + ".class"
          + (javaConverterClass != null ? ", " + rawClass + ".class" + ", " + javaConverterClass + ".class" : "") + ")";

      println("    public final " + liveSQLColumnType + " " + javaMembername + " = new " + liveSQLColumnType + "(this" //
          + ", \"" + JUtils.escapeJavaString(colName) + "\"" //
          + ", \"" + JUtils.escapeJavaString(property) + "\"" //
          + ", \"" + JUtils.escapeJavaString(cm.getTypeName()) + "\"" //
          + ", " + cm.getColumnSize() + "" //
          + ", " + cm.getDecimalDigits() + "" //
          + ", " + th + ");");
    }
    println();

    println("    // Getters");
    println();

    println("    public AllColumns star() {");
    println("      return new AllColumns(" + this.metadata.getColumns().stream()
        .map(c -> "this." + c.getId().getJavaMemberName()).collect(Collectors.joining(", ")) + ");");
    println("    }");
    println();

    String c = catalog == null ? "null"
        : "Name.of(\"" + JUtils.escapeJavaString(catalog.getCanonicalSQLName()) + "\", " + catalog.isQuoted() + ")";
    String s = schema == null ? "null"
        : "Name.of(\"" + JUtils.escapeJavaString(schema.getCanonicalSQLName()) + "\", " + schema.isQuoted() + ")";
    String n = "Name.of(\"" + JUtils.escapeJavaString(name.getCanonicalSQLName()) + "\", " + name.isQuoted() + ")";

    println("    // Constructors");
    println();
    println("    " + this.metadataClassName + "() {");
    println("      super(" + c + ", " + s + ", " + n + ", \"" + type + "\", null);");
    println("      initializeColumns();");
    println("    }");
    println();
    println("    " + this.metadataClassName + "(final String alias) {");
    println("      super(" + c + ", " + s + ", " + n + ", \"" + type + "\", alias);");
    println("      initializeColumns();");
    println("    }");
    println();

    println("    // Initialization");
    println();
    println("    private void initializeColumns() {");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      println("      super.add(this." + cm.getId().getJavaMemberName() + ");");
    }

    println("    }");
    println();

    println("  }");
    println();
  }

  private void writeAOPAspect() throws IOException {

    println("  // AOP Lazy Loading of Parent Class Aspect");
    println();

    println("  @Aspect");
    println("  @Configuration");
    println("  @Order(150)");
    println("  public class LazyLoadingAspect {");
    println();
    println("    @Before(\"execution(* " + this.getFullClassName() + ".get*(..))\")");
    println("    public void superclassLoader(JoinPoint joinPoint) {");
    println("      System.out.println(\"> SuperclassLoader Aspect triggered. joinpoint: \" + joinPoint);");
    println("      try {");
    println("        " + LazyParentClassLoading.class.getSimpleName() + " target = ("
        + LazyParentClassLoading.class.getSimpleName() + ") joinPoint.getTarget();");
    println("        target.loadSuperclass();");
    println("      } catch (Exception e) {");
    println("        // do nothing");
    println("      }");
    println("    }");
    println();
    println("  }");
    println();

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
      return "NumberColumn";
    } else if ("java.lang.String".equals(javaType)) {
      return "StringColumn";
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
      return "DateTimeColumn";
    } else if ("java.lang.Boolean".equals(javaType)) {
      return "BooleanColumn";
    } else if ("byte[]".equals(javaType)) {
      return "ByteArrayColumn";
    }

    // byte[]
    // java.lang.Object
    // <Custom Converter>
    return "ObjectColumn";
  }

  private void writeSelectSequence(final SequenceMethodTag tag) throws IOException, SequencesNotSupportedException {

    println("  // sequence " + tag.getSequenceId().getRenderedSQLName());
    println();
    println(ObjectDAO.renderJavaComment(this.adapter.renderSelectSequence(tag.getSequenceId())));
    println();

    println("  public long " + tag.getMethod() + "() {");
    println("    return (Long) sqlSession.selectOne(");
    println("      \"" + this.mapper.getFullMapperIdSelectSequence(tag) + "\");");
    println("  }");
    println();

  }

  private void writeQuery(final QueryMethodTag tag) throws IOException {

    println("  // update " + tag.getMethod());
    println();

    ParameterRenderer parameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "#{" + parameter.getName() + "}";
      }
    };
    String sentence = tag.renderSQLSentence(parameterRenderer);
    println(renderJavaComment(sentence));

    println();

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
      println("  public static class " + this.getParamClassName(tag) + " {");
      for (ParameterTag p : tag.getParameterDefinitions()) {
        println("    " + p.getJavaType() + " " + p.getName() + ";");
      }
      println("  }");
      println();
    }

    // method

    print("  public int " + methodName + "(");
    if (!tag.getParameterDefinitions().isEmpty()) {
      print(paramDef);
    }
    println(") {");
    String objName = null;
    if (!tag.getParameterDefinitions().isEmpty()) {
      objName = provideObjectName(tag.getParameterDefinitions());
      println("    " + this.getParamClassName(tag) + " " + objName + " = new " + this.getParamClassName(tag) + "();");
      for (ParameterTag p : tag.getParameterDefinitions()) {
        println("    " + objName + "." + p.getName() + " = " + p.getName() + ";");
      }
    }
    println("    return this.sqlSession.update(");
    print("      \"" + this.mapper.getFullMapperIdUpdate(tag) + "\"");
    if (!tag.getParameterDefinitions().isEmpty()) {
      print(", " + objName);
    }
    println(");");
    println("  }");
    println();

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

    println("  // select method: " + sm.getMethod());
    println();

    SelectMethodReturnType rt = sm.getReturnType(this.classPackage);

    // render comment

    ParameterRenderer parameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "#{" + parameter.getName() + "}";
      }
    };
    String sentence = sm.renderSQLSentence(parameterRenderer);
    println(renderJavaComment(sentence));

    println();

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
      println("  public static class " + this.getParamClassName(sm) + " {");
      for (SelectParameterMetadata p : sm.getParameterDefinitions()) {
        if (!p.getParameter().isInternal()) {
          println("    " + p.getParameter().getJavaType() + " " + p.getParameter().getName() + ";");
        }
      }
      println("  }");
      println();
    }

    // method

    print("  public " + rt.getReturnType() + " " + methodName + "(");
    if (!sm.getParameterDefinitions().isEmpty()) {
      print(paramDef);
    }
    println(") {");
    String objName = null;
    if (!sm.getParameterDefinitions().isEmpty()) {
      objName = provideParamObjectName(sm.getParameterDefinitions());
      println("    " + this.getParamClassName(sm) + " " + objName + " = new " + this.getParamClassName(sm) + "();");
      for (SelectParameterMetadata p : sm.getParameterDefinitions()) {
        if (!p.getParameter().isInternal()) {
          println("    " + objName + "." + p.getParameter().getName() + " = " + p.getParameter().getName() + ";");
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

    log.debug("--> mode=" + sm.getResultSetMode() + ", method=" + myBatisSelectMethod);

    print("    return ");

    if (sm.getResultSetMode() == ResultSetMode.CURSOR) {
      print("    new MyBatisCursor<" + rt.getBaseReturnVOType() + ">(");
    }

    print("this.sqlSession." + myBatisSelectMethod + "(\"" + this.mapper.getFullSelectMethodStatementId(sm) + "\"");
    if (!sm.getParameterDefinitions().isEmpty()) {
      print(", " + objName);
    }
    print(")");

    if (sm.getResultSetMode() == ResultSetMode.CURSOR) {
      print(")");
    }

    println(";");
    println("  }");
    println();

  }

  private void writeClassFooter() throws IOException {
    println("}");
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
    log.debug("sm=" + sm.getMethod() + " # " + cm.getColumnName());
    String thName = null;
    Map<ColumnMetadata, String> typeHandlers = this.selectTypeHandlers.get(sm);
    if (typeHandlers != null) {
      thName = typeHandlers.get(cm);
    }
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
    log.debug(this.getClassName() + " / " + cm.getColumnName() + " - TypeHandler=" + thName + " added=" + added
        + " total=" + this.selectTypeHandlerNames.size());
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
      log.debug(cm.getColumnName() + " cm.getEnumMetadata()=" + em);
      String javaClassName;
      if (em != null) {
        EnumClass ec = mg.getEnum(em);
        javaClassName = ec.getFullClassName();
        log.debug(" >> enumclass=" + javaClassName);
      } else {
        javaClassName = cm.getType().getJavaClassName();
        log.debug(" >> simpleclass=" + javaClassName);
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
