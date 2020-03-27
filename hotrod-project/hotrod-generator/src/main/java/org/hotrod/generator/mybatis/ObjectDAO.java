package org.hotrod.generator.mybatis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.Constants;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.config.TableTag;
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
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.ForeignKeyMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodMetadata.SelectMethodReturnType;
import org.hotrod.metadata.SelectParameterMetadata;
import org.hotrod.metadata.VersionControlMetadata;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.StaleDataException;
import org.hotrod.runtime.interfaces.DaoForUpdate;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.OrderBy;
import org.hotrod.runtime.interfaces.Selectable;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.tx.TxManager;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.GenUtils;
import org.hotrod.utils.ImportsRenderer;
import org.hotrod.utils.JUtils;
import org.hotrod.utils.ValueTypeFactory;
import org.hotrod.utils.ValueTypeFactory.ValueTypeManager;
import org.hotrodorm.hotrod.utils.ListWriter;
import org.hotrodorm.hotrod.utils.SUtils;
import org.nocrala.tools.database.tartarus.core.JdbcForeignKey;
import org.nocrala.tools.database.tartarus.core.JdbcKey;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;

public class ObjectDAO extends GeneratableObject {

  // Constants

  private static final Logger log = LogManager.getLogger(ObjectDAO.class);

  // Properties

  private AbstractDAOTag tag;

  private DataSetMetadata metadata;
  private DataSetLayout layout;
  private MyBatisGenerator generator;
  private DAOType daoType;
  private MyBatisTag myBatisTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private ClassPackage classPackage;

  private ObjectVO vo = null;
  private Mapper mapper = null;

  private TextWriter w;

  // Constructors

  public ObjectDAO(final AbstractDAOTag tag, final DataSetMetadata metadata, final DataSetLayout layout,
      final MyBatisGenerator generator, final DAOType type, final MyBatisTag myBatisTag, final ObjectVO vo,
      final Mapper mapper) {
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
    this.vo = vo;
    this.mapper = mapper;

    this.fragmentConfig = metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    this.classPackage = this.layout.getDAOPrimitivePackage(this.fragmentPackage);
  }

  // Behavior

  public boolean isTable() {
    return this.daoType == DAOType.TABLE;
  }

  public boolean isView() {
    return this.daoType == DAOType.VIEW;
  }

  public boolean isPlain() {
    return this.daoType == DAOType.EXECUTOR;
  }

  public void generate(final FileGenerator fileGenerator, final MyBatisGenerator mg)
      throws UncontrolledException, ControlledException {

    String className = this.getClassName() + ".java";

    File dir = this.layout.getDaoPrimitivePackageDir(this.fragmentPackage);
    File f = new File(dir, className);

    this.w = null;

    try {
      this.w = fileGenerator.createWriter(f);

      writeClassHeader();

      if (!this.isPlain()) {

        if (this.isTable()) {
          writeSelectByPK(mg);
          writeSelectByUI(mg);
        }

        writeSelectByExampleAndOrder();

        if (this.isTable()) {
          writeSelectParentByFK();
          writeSelectChildrenByFK();

          writeInsert();

          writeUpdateByPK();
          writeDeleteByPK();
        }

        if (this.isView()) {
          writeInsertByExample();
        }

        if (this.isTable() || this.isView()) {
          writeUpdateByExample();
          writeDeleteByExample();
        }

        writeEnumTypeHandlers();

        writeConverters();

        writeOrderingEnum();

      }

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
          // writeSelectExpression(); // remove once tested
          writeSelect(s);
        }

      }

      writeTxManager();

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
    if (isCheckedPersistenceException()) {
      imports.add("java.sql.SQLException");
    }
    imports.add("java.util.List");
    imports.newLine();
    imports.add("org.apache.ibatis.session.SqlSession");
    imports.add("org.apache.ibatis.session.SqlSessionFactory");
    imports.newLine();
    imports.add(TxManager.class);

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

    // imports.comment("[ selects done. ]");
    imports.newLine();

    if (this.usesConverters() || hasFKPointingToEnum()) {

      imports.add("java.sql.CallableStatement");
      imports.add("java.sql.PreparedStatement");
      imports.add("java.sql.ResultSet");
      imports.add("org.apache.ibatis.type.JdbcType");
      imports.add("org.apache.ibatis.type.TypeHandler");
      imports.add("org.hotrod.runtime.converter.TypeConverter");

      imports.newLine();

    }

    this.w.write(imports.render());

    // Signature

    println("public abstract class " + this.getClassName() + " implements Serializable {");
    println();

    // Serial Version UID

    println("  private static final long serialVersionUID = 1L;");
    println();

  }

  /**
   * <pre>
   * 
   * public static AbcDAO selectByPK(final Integer id) throws SQLException {
   *   SqlSession sqlSession = null;
   *   try {
   *     sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *     return selectByPK(sqlSession, id);
   *   } finally {
   *     if (sqlSession != null) {
   *       sqlSession.close();
   *     }
   *   }
   * }
   * 
   * public static AbcDAO selectByPK(final SqlSession sqlSession, final Integer id) throws SQLException {
   *   AbcDAO pk = new AbcDAO();
   *   pk.setId(id);
   *   return sqlSession.selectOne(&quot;simpletests.dao.selectAbc&quot;, pk);
   * }
   * 
   * </pre>
   * 
   */

  private static final String SELECT_BY_PK_METHOD = "select";

  private void writeSelectByPK(final MyBatisGenerator mg) throws IOException, UnresolvableDataTypeException {
    if (this.metadata.getPK() == null) {
      println("  // no select by PK generated, since the table does not have a PK.");
      println();
      return;
    }

    String paramsSignature = toParametersSignature(this.metadata.getPK(), mg);
    String paramsCall = toParametersCall(this.metadata.getPK());

    println("  // select by primary key");
    println();

    print("  public static " + this.vo.getClassName() + " " + SELECT_BY_PK_METHOD + "(");
    print(paramsSignature);
    print(") ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    println("      return " + SELECT_BY_PK_METHOD + "(sqlSession, " + paramsCall + ");");
    releaseSqlSession();
    println("  }");
    println();

    println("  public static " + this.vo.getClassName() + " " + SELECT_BY_PK_METHOD + "(final SqlSession sqlSession, "
        + paramsSignature + ")");
    print("      ");
    this.throwsCheckedException();
    println("{");
    for (ColumnMetadata cm : this.metadata.getPK().getColumns()) {
      println("    if (" + cm.getId().getJavaMemberName() + " == null) return null;");
    }
    println("    " + this.vo.getClassName() + " pk = new " + this.vo.getClassName() + "();");
    for (ColumnMetadata cm : this.metadata.getPK().getColumns()) {
      println("    pk." + cm.getId().getJavaSetter() + "(" + cm.getId().getJavaMemberName() + ");");
    }

    preCheckedException();
    println("    return sqlSession.selectOne(\"" + this.mapper.getFullMapperIdSelectByPK() + "\", pk);");
    postCheckedException();

    println("  }");
    println();
  }

  private void throwsCheckedException() throws IOException {
    if (isCheckedPersistenceException()) {
      print("throws SQLException ");
    }
  }

  private boolean isCheckedPersistenceException() {
    return this.myBatisTag.getProperties().isCheckedPersistenceException();
  }

  private void addCheckedException() throws IOException {
    if (isCheckedPersistenceException()) {
      print("SQLException");
    }
  }

  private void preCheckedException() throws IOException {
    if (isCheckedPersistenceException()) {
      println("    try {");
    }
  }

  private void postCheckedException() throws IOException {
    if (isCheckedPersistenceException()) {
      println("    } catch (RuntimeException e) {");
      println("      throw new SQLException(e);");
      println("    }");
    }
  }

  private void retrieveSqlSession() throws IOException {
    retrieveSqlSession(0);
  }

  /**
   * <pre>
   *     TxManager txm = null;
   *     try {
   *       txm = getTxManager();
   * </pre>
   */
  private void retrieveSqlSession(final int indent) throws IOException {
    String f = SUtils.getFiller(' ', indent * 2);
    println(f + "    TxManager txm = null;");
    println(f + "    try {");
    println(f + "      txm = getTxManager();");
    println(f + "      SqlSession sqlSession = txm.getSqlSession();");
  }

  /**
   * <pre>
   * if (!txm.isTransactionOngoing()) {
   *   txm.commit();
   * }
   * </pre>
   */
  private void commitSqlSession(final int indent) throws IOException {
    String f = SUtils.getFiller(' ', indent * 2);
    println(f + "    if (!txm.isTransactionOngoing()) {");
    println(f + "      txm.commit();");
    println(f + "    }");
  }

  private void releaseSqlSession() throws IOException {
    releaseSqlSession(0);
  }

  /**
   * <pre>
   *     } finally {
   *       if (txm != null && !txm.isTransactionOngoing()) {
   *         txm.close();
   *       }
   *     }
   * </pre>
   */

  private void releaseSqlSession(final int indent) throws IOException {
    String f = SUtils.getFiller(' ', indent * 2);

    if (this.isCheckedPersistenceException()) {
      println(f + "    } catch (SQLException e) {");
      println(f + "      throw e;");
      println(f + "    } catch (RuntimeException e) {");
      println(f + "      throw new SQLException(e);");
    }

    println(f + "    } finally {");

    if (this.isCheckedPersistenceException()) {
      println(f + "      try {");
      f = SUtils.getFiller(' ', (indent + 1) * 2);
    }

    println(f + "      if (txm != null && !txm.isTransactionOngoing()) {");
    println(f + "        txm.close();");
    println(f + "      }");
    f = SUtils.getFiller(' ', indent * 2);

    if (this.isCheckedPersistenceException()) {
      println(f + "      } catch (RuntimeException e) {");
      println(f + "        throw new SQLException(e);");
      println(f + "      }");
    }

    println(f + "    }");
  }

  /**
   * <pre>
   * 
   * // Select by Unique Indexes
   * 
   * public static List&lt;AbcDAO&gt; selectByUISectionPage(final Integer section, final Integer page) throws SQLException {
   *   SqlSession sqlSession = null;
   *   try {
   *     sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *     return selectByUISectionPage(sqlSession, section, page);
   *   } finally {
   *     if (sqlSession != null) {
   *       sqlSession.close();
   *     }
   *   }
   * }
   * 
   * public static List&lt;AbcDAO&gt; selectByUISectionPage(final SqlSession sqlSession, final Integer section,
   *     final Integer page) throws SQLException {
   *   AbcDAO ui = new AbcDAO();
   *   ui.setSection(section);
   *   ui.setPage(page);
   *   return sqlSession.selectList(&quot;simpletests.dao.selectAbc&quot;, ui);
   * }
   * 
   * </pre>
   * 
   * @throws IOException
   * @throws UnresolvableDataTypeException
   */

  private void writeSelectByUI(final MyBatisGenerator mg) throws IOException, UnresolvableDataTypeException {
    boolean first = true;

    // Remove duplicated unique indexes/constraints that may be registered in
    // the database. This behavior/bug has been observed in PostgreSQL.

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

        String paramsSignature = toParametersSignature(ui, mg);
        String paramsCall = toParametersCall(ui);
        String camelCase = ui.toCamelCase(this.layout.getColumnSeam());

        print("  public static " + this.vo.getClassName() + " selectByUI" + camelCase + "(" + paramsSignature + ") ");
        this.throwsCheckedException();
        println("{");
        retrieveSqlSession();
        println("      return selectByUI" + camelCase + "(sqlSession, " + paramsCall + ");");
        releaseSqlSession();
        println("  }");
        println();

        println("  public static " + this.vo.getClassName() + " selectByUI" + camelCase
            + "(final SqlSession sqlSession, " + paramsSignature + ") ");
        this.throwsCheckedException();
        println("{");
        for (ColumnMetadata cm : ui.getColumns()) {
          println("    if (" + cm.getId().getJavaMemberName() + " == null) return null;");
        }
        println("    " + this.vo.getClassName() + " ui = new " + this.vo.getClassName() + "();");
        for (ColumnMetadata cm : ui.getColumns()) {
          println("    ui." + cm.getId().getJavaSetter() + "(" + cm.getId().getJavaMemberName() + ");");
        }

        preCheckedException();
        println("    return sqlSession.selectOne(\"" + this.mapper.getFullMapperIdSelectByUI(ui) + "\", ui);");
        postCheckedException();

        println("  }");
        println();
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

  /**
   * 
   * <pre>
   * 
   * // Select with filter and order
   * 
   * public static List<AccountDAO> selectByExample(final AccountDAO example, final AccountOrderBy... orderBies)
   *     throws SQLException {
   *   TxManager txm = null;
   *   try {
   *     txm = getTxManager();
   *     SqlSession sqlSession = txm.getSqlSession();
   *     return selectByExample(sqlSession, example, orderBies);
   *   } finally {
   *     if (txm != null && !txm.isTransactionOngoing()) {
   *       txm.close();
   *     }
   *   }
   * }
   * 
   * public static List<AccountDAO> selectByExample(final SqlSession sqlSession, final AccountDAO example,
   *     final AccountOrderBy... orderBies) throws SQLException {
   *   DaoWithOrder<AccountDAOPrimitives, AccountOrderBy> dwo = //
   *       new DaoWithOrder<AccountDAOPrimitives, AccountOrderBy>(example, orderBies);
   *   return sqlSession.selectList("hotrod.test.generation.primitives.account.selectByExample", dwo);
   * }
   * 
   * </pre>
   * 
   * @throws IOException
   */

  private void writeSelectByExampleAndOrder() throws IOException {
    println("  // select by example (with ordering)");
    println();

    println("  public static List<" + this.vo.getClassName() + "> selectByExample(final " + this.vo.getClassName()
        + " example, final " + this.getOrderByClassName() + "... orderBies)");
    print("      ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    println("      return selectByExample(sqlSession, example, orderBies);");
    releaseSqlSession();
    println("  }");
    println();

    println("  public static List<" + this.vo.getClassName() + "> selectByExample(final SqlSession sqlSession, final "
        + this.vo.getClassName() + " example, final " + this.getOrderByClassName() + "... orderBies)");
    print("      ");
    this.throwsCheckedException();
    println("{");
    println("    DaoWithOrder<" + this.vo.getClassName() + ", " + this.getOrderByClassName() + "> dwo = //");
    println("    new DaoWithOrder<" + this.vo.getClassName() + ", " + this.getOrderByClassName()
        + ">(example, orderBies);");
    preCheckedException();
    println("    return sqlSession.selectList(\"" + this.mapper.getFullMapperIdSelectByExample() + "\", dwo);");
    postCheckedException();
    println("  }");
    println();
  }

  // XXX: This method is only used for internal testing

  @SuppressWarnings("unused")
  private void writeSelectExpression() throws IOException, ControlledException {
    println(" public static final " + DynamicExpression.class.getName() + " JAVA_EXPRESSION =\n");

    ParameterRenderer parameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "#" + parameter.getName() + "#";
      }
    };

    // try {
    // println(this.selectTag.renderJavaExpression(4, parameterRenderer) + ";");
    // } catch (InvalidJavaExpressionException e) {
    // throw new ControlledException("Invalid configuration file: " +
    // e.getMessage());
    // }

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

  /**
   * <pre>
   * // Select parents by FKs
   * 
   * public class DefParentSelector {
   * 
   *   public DefDAO byFkId() throws SQLException {
   *     SqlSession sqlSession = null;
   *     try {
   *       sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *       return byFkId(sqlSession);
   *     } finally {
   *       if (sqlSession != null) {
   *         sqlSession.close();
   *       }
   *     }
   *   }
   * 
   *   public DefDAO byFkId(final SqlSession sqlSession) throws SQLException {
   *     return DefDAOPrimitives.selectByUIAbcId(sqlSession, id);
   *   }
   * 
   * }
   * 
   * public static DefParentSelector selectParentDef() {
   *   return new DefParentSelector();
   * }
   * </pre>
   * 
   * @throws IOException
   * @throws ControlledException
   */
  private void writeSelectParentByFK() throws IOException, ControlledException {

    List<ForeignKeyMetadata> fks = this.metadata.getImportedFKs();
    if (fks.isEmpty()) {

      println("  // select parents by imported FKs: no imported keys found -- skipped");
      println();

    } else {

      println("  // select parents by imported FKs");
      println();

      // Sort by remote table.

      // Also, get distinct foreign keys only, since multiple identical foreign
      // keys can be registered in the database. This behavior/bug has been
      // observed in PostgreSQL.

      Map<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>> fkSelectors = compileDistinctFKs(
          this.metadata.getImportedFKs());

      for (DataSetMetadata ds : fkSelectors.keySet()) {
        ObjectVO vo = this.generator.getVO(ds);
        if (vo != null) { // points to a table, not an enum
          ObjectDAO dao = this.generator.getDAO(ds);

          String parentSelectorClassName = vo.getJavaClassIdentifier() + "ParentSelector";
          println("  public static class " + parentSelectorClassName + " {");
          println();

          for (ForeignKeyMetadata fkm : fkSelectors.get(ds)) {
            String selectByCols = this.getSelectByColumns(fkm.getLocal());
            print(
                "    public " + vo.getClassName() + " " + selectByCols + "(final " + this.vo.getClassName() + " vo) ");
            this.throwsCheckedException();
            println("{");
            retrieveSqlSession(1);
            println("        return " + selectByCols + "(sqlSession, vo);");
            releaseSqlSession(1);
            println("    }");
            println();

            String callParameters = renderCallParameters(fkm);

            String selectMethod = fkm.pointsToPK() ? SELECT_BY_PK_METHOD : dao.getSelectByUI(fkm.getRemote());
            print("    public " + vo.getClassName() + " " + selectByCols + "(final SqlSession sqlSession, final "
                + this.vo.getClassName() + " vo) ");
            this.throwsCheckedException();
            println("{");
            println(
                "      return " + dao.getClassName() + "." + selectMethod + "(sqlSession, " + callParameters + ");");
            println("    }");
            println();

          }

          println("  }");
          println();

          println(
              "  public static " + parentSelectorClassName + " selectParent" + vo.getJavaClassIdentifier() + "() {");
          println("    return new " + parentSelectorClassName + "();");
          println("  }");
          println();

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

  private Map<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>> compileDistinctFKs(
      final List<ForeignKeyMetadata> fks) {
    Map<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>> fkSelectors = new HashMap<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>>();
    for (ForeignKeyMetadata fk : fks) {
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

  private String renderCallParameters(final ForeignKeyMetadata fk) throws ControlledException {
    ListWriter lw = new ListWriter(", ");
    for (int i = 0; i < fk.getLocal().getColumns().size(); i++) {
      ColumnMetadata loCol = fk.getLocal().getColumns().get(i);
      ColumnMetadata reCol = fk.getRemote().getColumns().get(i);
      PropertyType loType = loCol.getType();
      PropertyType reType = reCol.getType();
      String param = GenUtils.convertPropertyType(loType.getJavaClassName(), reType.getJavaClassName(),
          "vo." + loCol.getId().getJavaMemberName());
      lw.add(param);
    }
    return lw.toString();
  }

  /**
   * <pre>
   * // Select children by exported FKs
   * 
   * public static class DefChildrenSelector {
   * 
   *   public List&lt;DefDAO&gt; byAbcSectionAbcPage(final DefDAOOrderBy... orderBies) throws SQLException {
   *     SqlSession sqlSession = null;
   *     try {
   *       sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *       return byAbcSectionAbcPage(sqlSession, orderBies);
   *     } finally {
   *       if (sqlSession != null) {
   *         sqlSession.close();
   *       }
   *     }
   *   }
   * 
   *   public List&lt;DefDAO&gt; byAbcSectionAbcPage(final SqlSession sqlSession, final DefDAOOrderBy... orderBies)
   *       throws SQLException {
   *     DefDAO example = new DefDAO();
   *     example.setAbcSection(section);
   *     example.setAbcPage(page);
   *     return DefDAO.selectByExample(sqlSession, example, orderBies);
   *   }
   * 
   * }
   * 
   * public static DefChildrenSelector selectChildrenDef() {
   *   return new DefChildrenSelector();
   * }
   * </pre>
   * 
   * @throws IOException
   * @throws ControlledException
   */

  private void writeSelectChildrenByFK() throws IOException, ControlledException {

    if (this.metadata.getExportedFKs().isEmpty()) {

      println("  // select children by exported FKs: no exported keys found -- skipped");
      println();

    } else {

      println("  // select children by exported FKs");
      println();

      // Sort by remote table.

      // Also, get distinct foreign keys only, since multiple identical foreign
      // keys can be registered in the database. This behavior has been observed
      // in PostgreSQL.

      Map<DataSetMetadata, LinkedHashSet<ForeignKeyMetadata>> efkSelectors = compileDistinctFKs(
          this.metadata.getExportedFKs());

      for (DataSetMetadata ds : efkSelectors.keySet()) {

        ObjectVO vo = this.generator.getVO(ds);
        ObjectDAO dao = this.generator.getDAO(ds);

        String selectorName = vo.getJavaClassIdentifier() + "ChildrenSelector";
        println("  public static class " + selectorName + " {");
        println();

        for (ForeignKeyMetadata tfk : efkSelectors.get(ds)) {

          String selectByCols = dao.getSelectByColumns(tfk.getRemote());

          println("    public List<" + vo.getClassName() + "> " + selectByCols + "(final " + this.vo.getClassName()
              + " vo, final " + dao.getOrderByClassName() + "... orderBies)");
          print("        ");
          this.throwsCheckedException();
          println("{");
          retrieveSqlSession(1);
          println("        return " + selectByCols + "(sqlSession, vo, orderBies);");
          releaseSqlSession(1);
          println("    }");
          println();

          println("    public List<" + vo.getClassName() + "> " + selectByCols + "(final SqlSession sqlSession, final "
              + this.vo.getClassName() + " vo, final " + dao.getOrderByClassName() + "... orderBies)");
          print("        ");
          this.throwsCheckedException();
          println("{");

          println("      " + vo.getClassName() + " example = new " + vo.getClassName() + "();");

          for (int i = 0; i < tfk.getLocal().getColumns().size(); i++) {
            ColumnMetadata loCol = tfk.getLocal().getColumns().get(i);
            ColumnMetadata reCol = tfk.getRemote().getColumns().get(i);
            PropertyType loType = loCol.getType();
            PropertyType reType = reCol.getType();
            String param = GenUtils.convertPropertyType(loType.getJavaClassName(), reType.getJavaClassName(),
                "vo." + loCol.getId().getJavaMemberName());
            println("      example.set" + reCol.getId().getJavaClassName() + "(" + param + ");");
          }

          println("      return " + dao.getClassName() + ".selectByExample(sqlSession, example, orderBies);");
          println("    }");
          println();

        }

        println("  }");
        println();

        println("  public static " + getChildrenSelectorClass(vo) + " selectChildren" + vo.getJavaClassIdentifier()
            + "() {");
        println("    return new " + getChildrenSelectorClass(vo) + "();");
        println("  }");
        println();
      }

    }

  }

  private String getChildrenSelectorClass(final ObjectVO dao) {
    return dao.getJavaClassIdentifier() + "ChildrenSelector";
  }

  /**
   * <pre>
   * 
   * // Insert
   * 
   * public int insert() throws SQLException {
   *   SqlSession sqlSession = null;
   *   try {
   *     sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *     int rows = insert(sqlSession);
   *     sqlSession.commit();
   *     return rows;
   *   } finally {
   *     if (sqlSession != null) {
   *       sqlSession.close();
   *     }
   *   }
   * }
   * 
   * public int insert(final SqlSession sqlSession) throws SQLException {
   *   this.versionControl = N; // only when there's version control defined
   *   return sqlSession.insert(&quot;simpletests.dao.insertAbc&quot;, this);
   * }
   * 
   * </pre>
   * 
   * @throws IOException
   * @throws UnresolvableDataTypeException
   * @throws ControlledException
   */

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

    boolean integratesSequences = this.generator.getAdapter().getInsertIntegration().integratesSequences();
    boolean integratesIdentities = this.generator.getAdapter().getInsertIntegration().integratesIdentities();
    boolean integratesDefaults = this.generator.getAdapter().getInsertIntegration().integratesDefaults();

    boolean extraInsert = integratesSequences && integratesDefaults && defaults != 0;

    // | integrates identities
    // | false | true
    // -------------------------+------------+-------------
    // has identities : false | T | T
    // : true | F | T
    //
    // ! has identities || integrates identities

    println("  // insert");
    println();

    if (extraInsert) {

      print("  public static int insert(final " + this.vo.getClassName() + " vo) ");
      this.throwsCheckedException();
      println("{");
      println("    return insert(vo, false);");
      println("  }");
      println();

      print("  public static int insert(final SqlSession sqlSession, final " + this.vo.getClassName() + " vo) ");
      this.throwsCheckedException();
      println("{");
      println("    return insert(sqlSession, vo, false);");
      println("  }");
      println();

    }

    print("  public static int insert(final " + this.vo.getClassName() + " vo");
    if (extraInsert) {
      print(", final boolean retrieveDefaults");
    }
    print(") ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    if (extraInsert) {
      println("      int rows = insert(sqlSession, vo, retrieveDefaults);");
    } else {
      println("      int rows = insert(sqlSession, vo);");
    }
    commitSqlSession(1);
    println("      return rows;");
    releaseSqlSession();
    println("  }");
    println();

    print("  public static int insert(final SqlSession sqlSession, final " + this.vo.getClassName() + " vo");
    if (extraInsert) {
      print(", final boolean retrieveDefaults");
    }
    print(") ");
    this.throwsCheckedException();
    println("{");

    VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();

    if (vcm != null) {
      ColumnMetadata cm = vcm.getColumnMetadata();
      String literalValue = renderNumericLiteral(cm.getType().getValueRange().getInitialValue(),
          cm.getType().getJavaClassName());
      println("    vo." + cm.getId().getJavaMemberName() + " = " + literalValue + ";");
    }

    preCheckedException();

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
        println("    return sqlSession.insert(id, vo);");
      } else { // sequences only
        if (integratesSequences) {
          writeInsertIntegrated(true, false, extraInsert);
          println("    return rows;");
        } else {
          println("    return sqlSession.insert(id, vo);");
        }
      }
    } else {
      if (sequences == 0) { // identities only
        if (integratesIdentities) {
          writeInsertIntegrated(false, true, extraInsert);
          println("    return rows;");
        } else {
          println("    return sqlSession.insert(id, vo);");
        }
      } else { // sequences & identities
        if (integratesSequences && integratesIdentities) {
          writeInsertIntegrated(true, true, extraInsert);
          println("    return rows;");
        } else if (integratesIdentities) {
          writeSequencesPreFetch();
          writeInsertIntegrated(false, true, extraInsert);
          println("    return rows;");
        } else if (integratesSequences) {
          writeInsertIntegrated(true, false, extraInsert);
          writeIdentitiesPostFetch();
          println("    return rows;");
        } else {
          writeSequencesPreFetch();
          println("    int rows = sqlSession.insert(id, vo);");
          writeIdentitiesPostFetch();
          println("    return rows;");
        }

      }
    }

    postCheckedException();

    println("  }");
    println();

  }

  private void writeInsertIntegrated(final boolean integratesSequences, final boolean integratesIdentities,
      final boolean extraInsert) throws IOException {
    if (this.generator.getAdapter().integratesUsingQuery()) {
      println("    " + this.vo.getClassName() + " values = sqlSession.selectOne(id, vo);");
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
      println("    int rows = sqlSession.insert(id, vo);");
    }
  }

  private void writeSequencesPreFetch() throws IOException {
    println("    " + this.vo.getClassName() + " sequences = sqlSession.selectOne(\""
        + this.mapper.getFullMapperIdSequencesPreFetch() + "\");");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getSequenceId() != null) {
        String prop = cm.getId().getJavaMemberName();
        println("    vo." + prop + " = sequences." + prop + ";");
      }
    }
  }

  private void writeIdentitiesPostFetch() throws IOException {
    println("    " + this.vo.getClassName() + " identities = sqlSession.selectOne(\""
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

  /**
   * <pre>
   * public int updateByPK() throws SQLException {
   *   SqlSession sqlSession = null;
   *   try {
   *     sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *     int rows = updateByPK(sqlSession);
   *     sqlSession.commit();
   *     return rows;
   *   } finally {
   *     if (sqlSession != null) {
   *       sqlSession.close();
   *     }
   *   }
   * }
   * 
   * public int updateByPK(final SqlSession sqlSession) throws SQLException {
   *   return sqlSession.update(&quot;simpletests.dao.insertAbc&quot;, this);
   * }
   * 
   * </pre>
   */

  /**
   * <pre>
   * public int update() throws SQLException, StaleDataException {
   *   TxManager txm = null;
   *   try {
   *     txm = getTxManager();
   *     SqlSession sqlSession = txm.getSqlSession();
   *     int rows = update(sqlSession);
   *     if (!txm.isTransactionOngoing()) {
   *       txm.commit();
   *     }
   *     return rows;
   *   } finally {
   *     if (txm != null && !txm.isTransactionOngoing()) {
   *       txm.close();
   *     }
   *   }
   * }
   * 
   * public int update(final SqlSession sqlSession) throws SQLException, StaleDataException {
   *   long currentVersion = this.versionControl;
   *   DaoForUpdate<VehicleDAOPrimitives> u = new DaoForUpdate<VehicleDAOPrimitives>(this, currentVersion, -256, 255);
   *   int rows = sqlSession.update("hotrod.test.generation.primitives.vehicle.updateByPK", u);
   *   if (rows != 1) {
   *     throw new StaleDataException("Could not update row on table VEHICLE with version " + currentVersion
   *         + " since it had already been updated by other process.");
   *   }
   *   return rows;
   * }
   * </pre>
   */

  private static final String UPDATE_BY_PK_METHOD = "update";

  private void writeUpdateByPK() throws IOException, UnresolvableDataTypeException {
    if (this.metadata.getPK() == null) {
      println("  // no update by PK generated, since the table does not have a PK.");
      println();
    } else {

      boolean useVersionControl = this.metadata.getVersionControlMetadata() != null;

      println("  // update by PK");
      println();

      print("  public static int " + UPDATE_BY_PK_METHOD + "(final " + this.vo.getClassName() + " vo) ");
      printExceptions(useVersionControl);
      println("{");
      retrieveSqlSession();
      println("      int rows = " + UPDATE_BY_PK_METHOD + "(sqlSession, vo);");
      commitSqlSession(1);
      println("      return rows;");
      releaseSqlSession();
      println("  }");
      println();

      if (useVersionControl) {
        VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();
        ColumnMetadata cm = vcm.getColumnMetadata();
        PropertyType pt = cm.getType();
        ValueRange range = pt.getValueRange();
        print("  public static int " + UPDATE_BY_PK_METHOD + "(final SqlSession sqlSession, final "
            + this.vo.getClassName() + " vo) ");
        printExceptions(useVersionControl);
        println("{");
        println("    long currentVersion = vo." + cm.getId().getJavaMemberName() + ";");

        String minValue = renderNumericLiteral(range.getMinValue(), cm.getType().getJavaClassName());
        String maxValue = renderNumericLiteral(range.getMaxValue(), cm.getType().getJavaClassName());

        println("    DaoForUpdate<" + this.vo.getClassName() + "> u = new DaoForUpdate<" + this.vo.getClassName()
            + ">(vo, currentVersion, " + minValue + ", " + maxValue + ");");
        println("    int rows = sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByPK() + "\", u);");
        println("    if (rows != 1) {");
        println("      throw new StaleDataException(\"Could not update row on table "
            + this.metadata.getId().getCanonicalSQLName() + " with version \" + currentVersion");
        println("          + \" since it had already been updated by another process.\");");
        println("    }");
        println("    vo." + cm.getId().getJavaMemberName() + " = (" + pt.getPrimitiveClassJavaType()
            + ") u.getNextVersionValue();");
        println("    return rows;");
      } else {
        print("  public static int " + UPDATE_BY_PK_METHOD + "(final SqlSession sqlSession, final "
            + this.vo.getClassName() + " vo) ");
        this.throwsCheckedException();
        println("{");

        for (ColumnMetadata cm : this.metadata.getPK().getColumns()) {
          println("    if (vo." + cm.getId().getJavaMemberName() + " == null) return 0;");
        }

        preCheckedException();
        println("    return sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByPK() + "\", vo);");
        postCheckedException();
      }

      println("  }");
      println();
    }

  }

  private void writeInsertByExample() throws IOException {
    println("  // insert by example");
    println();

    print("  public static int insertByExample(final " + this.vo.getClassName() + " example) ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    println("      int rows = " + this.getClassName() + ".insertByExample(sqlSession, example);");
    commitSqlSession(1);
    println("      return rows;");
    releaseSqlSession();
    println("  }");
    println();

    print("  public static int insertByExample(final SqlSession sqlSession, final " + this.vo.getClassName()
        + " example) ");
    this.throwsCheckedException();
    println("{");

    preCheckedException();

    // Choose insert variant

    println("    return sqlSession.insert(\"" + this.mapper.getFullMapperIdInsertByExample() + "\", example);");

    postCheckedException();

    println("  }");
    println();

  }

  /**
   * <pre>
   * public static int updateByExample(final AbcDAOPrimitives example, final AbcDAOPrimitives values)
   *     throws SQLException {
   *   SqlSession sqlSession = null;
   *   try {
   *     sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *     int rows = AbcDAOPrimitives.updateByExample(sqlSession, example, values);
   *     sqlSession.commit();
   *     return rows;
   *   } finally {
   *     if (sqlSession != null) {
   *       sqlSession.close();
   *     }
   *   }
   * }
   * 
   * public static int updateByExample(final SqlSession sqlSession, final AbcDAOPrimitives example,
   *     final AbcDAOPrimitives values) throws SQLException {
   *   UpdateByExampleDao&lt;AbcDAOPrimitives&gt; fvd = //
   *       new UpdateByExampleDao&lt;AbcDAOPrimitives&gt;(example, values);
   *   return sqlSession.update(&quot;simpletests.dao.insertAbc&quot;, fvd);
   * }
   * 
   * </pre>
   * 
   * @throws IOException
   */
  private void writeUpdateByExample() throws IOException {
    println("  // update by example");
    println();

    print("  public static int updateByExample(final " + this.vo.getClassName() + " example, final "
        + this.vo.getClassName() + " updateValues) ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    println("      int rows = " + this.getClassName() + ".updateByExample(sqlSession, example, updateValues);");
    commitSqlSession(1);
    println("      return rows;");
    releaseSqlSession();
    println("  }");
    println();

    print("  public static int updateByExample(final SqlSession sqlSession, final " + this.vo.getClassName()
        + " example, final " + this.vo.getClassName() + " updateValues) ");
    this.throwsCheckedException();
    println("{");
    println("    UpdateByExampleDao<" + this.vo.getClassName() + "> fvd = //");
    println("      new UpdateByExampleDao<" + this.vo.getClassName() + ">(example, updateValues);");

    preCheckedException();
    println("    return sqlSession.update(\"" + this.mapper.getFullMapperIdUpdateByExample() + "\", fvd);");
    postCheckedException();

    println("  }");
    println();

  }

  /**
   * <pre>
   * public int deleteByPK() throws SQLException {
   *   SqlSession sqlSession = null;
   *   try {
   *     sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *     int rows = deleteByPK(sqlSession);
   *     sqlSession.commit();
   *     return rows;
   *   } finally {
   *     if (sqlSession != null) {
   *       sqlSession.close();
   *     }
   *   }
   * }
   * 
   * public int deleteByPK(final SqlSession sqlSession) throws SQLException {
   *   return sqlSession.delete(&quot;simpletests.dao.insertAbc&quot;, this);
   * }
   * 
   * public int delete(final SqlSession sqlSession) throws SQLException, StaleDataException {
   *   int rows = sqlSession.delete("hotrod.test.generation.primitives.vehicle.deleteByPK", this);
   *   if (rows != 1) {
   *     throw new StaleDataException("Could not delete row on table VEHICLE with version " + this.versionControl
   *         + " since it had already been updated or deleted by another process.");
   *   }
   *   return rows;
   * }
   * </pre>
   * 
   * @throws IOException
   */

  private static final String DELETE_BY_PK_METHOD = "delete";

  private void writeDeleteByPK() throws IOException, UnresolvableDataTypeException {
    if (this.metadata.getPK() == null) {
      println("  // no delete by PK generated, since the table does not have a PK.");
      println();
    } else {

      boolean useVersionControl = this.metadata.getVersionControlMetadata() != null;

      println("  // delete by PK");
      println();

      print("  public static int " + DELETE_BY_PK_METHOD + "(final " + this.vo.getClassName() + " vo) ");
      printExceptions(useVersionControl);
      println("{");
      retrieveSqlSession();
      println("      int rows = " + DELETE_BY_PK_METHOD + "(sqlSession, vo);");
      commitSqlSession(1);
      println("      return rows;");
      releaseSqlSession();
      println("  }");
      println();

      if (useVersionControl) {
        VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();
        ColumnMetadata cm = vcm.getColumnMetadata();
        print("  public static int " + DELETE_BY_PK_METHOD + "(final SqlSession sqlSession, final "
            + this.vo.getClassName() + " vo) ");
        printExceptions(useVersionControl);
        println("{");
        println("    int rows = sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByPK() + "\", vo);");
        println("    if (rows != 1) {");
        println("      throw new StaleDataException(\"Could not delete row on table "
            + this.metadata.getId().getCanonicalSQLName() + " with version \" + vo." + cm.getId().getJavaMemberName());
        println("          + \" since it had already been updated or deleted by another process.\");");
        println("    }");
        println("    return rows;");
        println("  }");
      } else {
        print("  public static int " + DELETE_BY_PK_METHOD + "(final SqlSession sqlSession, final "
            + this.vo.getClassName() + " vo) ");
        this.throwsCheckedException();
        println("{");

        for (ColumnMetadata cm : this.metadata.getPK().getColumns()) {
          println("    if (vo." + cm.getId().getJavaMemberName() + " == null) return 0;");
        }

        preCheckedException();
        println("    return sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByPK() + "\", vo);");
        postCheckedException();

        println("  }");
      }
      println();
    }
  }

  private void printExceptions(boolean useVersionControl) throws IOException {
    if (this.isCheckedPersistenceException() || useVersionControl) {
      print("throws ");
    }
    this.addCheckedException();

    if (useVersionControl) {
      if (this.isCheckedPersistenceException()) {
        print(", ");
      }
      print("StaleDataException");
    }
    if (this.isCheckedPersistenceException() || useVersionControl) {
      print(" ");
    }
  }

  /**
   * <pre>
   * public static int deleteByExample(final AccountDAO example) throws SQLException {
   *   SqlSession sqlSession = null;
   *   try {
   *     sqlSession = Database1SessionFactory.getInstance().getSqlSessionFactory().openSession();
   *     int rows = AccountDAO.deleteByExample(sqlSession, example);
   *     sqlSession.commit();
   *     return rows;
   *   } finally {
   *     if (sqlSession != null) {
   *       sqlSession.close();
   *     }
   *   }
   * }
   * 
   * public static int deleteByExample(final SqlSession sqlSession, final AccountDAO example) throws SQLException {
   *   return sqlSession.delete(&quot;simpletests.dao.insertAbc&quot;, example);
   * }
   * </pre>
   * 
   * @throws IOException
   */
  private void writeDeleteByExample() throws IOException {
    println("  // delete by example");
    println();

    print("  public static int deleteByExample(final " + this.vo.getClassName() + " example) ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    println("      int rows = " + this.getClassName() + ".deleteByExample(sqlSession, example);");
    commitSqlSession(1);
    println("      return rows;");
    releaseSqlSession();
    println("  }");
    println();

    print("  public static int deleteByExample(final SqlSession sqlSession, final " + this.vo.getClassName()
        + " example) ");
    this.throwsCheckedException();
    println("{");
    println("    return sqlSession.delete(\"" + this.mapper.getFullMapperIdDeleteByExample() + "\", example);");
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

  /**
   * <pre>
   * 
   * public class ActiveTypeHandler implements TypeHandler<Boolean> {
   * 
   *   private TypeConverter<Short, Boolean> converter = new BooleanShortConverter();
   * 
   *   &#64;Override
   *   public Boolean getResult(final ResultSet rs, final String columnName) throws SQLException {
   *     Short value = rs.getShort(columnName);
   *     if (rs.wasNull()) {
   *       value = null;
   *     }
   *     return this.converter.get(value);
   *   }
   * 
   *   &#64;Override
   *   public Boolean getResult(final ResultSet rs, final int columnIndex) throws SQLException {
   *     Short value = rs.getShort(columnIndex);
   *     if (rs.wasNull()) {
   *       value = null;
   *     }
   *     return this.converter.get(value);
   *   }
   * 
   *   &#64;Override
   *   public Boolean getResult(final CallableStatement cs, final int columnIndex) throws SQLException {
   *     Short value = cs.getShort(columnIndex);
   *     if (cs.wasNull()) {
   *       value = null;
   *     }
   *     return this.converter.get(value);
   *   }
   * 
   *   &#64;Override
   *   public void setParameter(final PreparedStatement ps, final int columnIndex, final Boolean v,
   *       final JdbcType jdbcType) throws SQLException {
   *     Short value = this.converter.set(v);
   *     if (value == null) {
   *       ps.setNull(columnIndex, jdbcType.TYPE_CODE);
   *     } else {
   *       ps.setShort(columnIndex, value);
   *     }
   *   }
   * 
   * }
   * 
   * </pre>
   * 
   * @throws IOException
   */

  private void writeConverters() throws IOException {

    // Entity columns converters

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getConverter() != null) {
        String typeHandlerClassName = getTypeHandlerClassName(cm);
        writeTypeHandler(null, cm, typeHandlerClassName);
      }
    }

    // Select columns converters

    log.debug("DAO=" + this.getClassName() + " select converters=" + this.selectTypeHandlerNames.size());

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
    String interType = cm.getConverter().getJavaIntermediateType();
    String type = cm.getConverter().getJavaType();
    String setter = cm.getConverter().getJdbcSetterMethod();
    String getter = cm.getConverter().getJdbcGetterMethod();
    String converter = cm.getConverter().getJavaClass();

    println("  // TypeHandler for " + (property != null ? "property " + property : "column " + cm.getColumnName())
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

  /**
   * <pre>
    NAME$CASEINSENSITIVE("\"ACCOUNT\"", "lower(\"NAME\")", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("\"ACCOUNT\"", "lower(\"NAME\"), \"NAME\"", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("\"ACCOUNT\"", "lower(\"NAME\"), \"NAME\"", false), //
  
    NAME$DESC_CASEINSENSITIVE("\"ACCOUNT\"", "lower(\"NAME\")", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("\"ACCOUNT\"", "lower(\"NAME\") DESC, \"NAME\"", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("\"ACCOUNT\"", "lower(\"NAME\") DESC, \"NAME\"", true), //
   * </pre>
   */

  /**
   * <pre>
   * // DAO Ordering
   * 
   * public enum AbcDAOOrderBy implements OrderBy {
   * 
   *   IDN(&quot;ABC&quot;, &quot;IDN&quot;, true), //
   *   IDN$DESC(&quot;ABC&quot;, &quot;IDN&quot;, false), //
   *   ID(&quot;ABC&quot;, &quot;ID&quot;, true), //
   *   ID$DESC(&quot;ABC&quot;, &quot;ID&quot;, false), //
   *   VOLUME(&quot;ABC&quot;, &quot;VOLUME&quot;, true), //
   *   VOLUME$DESC(&quot;ABC&quot;, &quot;VOLUME&quot;, false), //
   *   CHAPTER(&quot;ABC&quot;, &quot;CHAPTER&quot;, true), //
   *   CHAPTER$DESC(&quot;ABC&quot;, &quot;CHAPTER&quot;, false), //
   *   SECTION(&quot;ABC&quot;, &quot;SECTION&quot;, true), //
   *   SECTION$DESC(&quot;ABC&quot;, &quot;SECTION&quot;, false), //
   *   PAGE(&quot;ABC&quot;, &quot;PAGE&quot;, true), //
   *   PAGE$DESC(&quot;ABC&quot;, &quot;PAGE&quot;, false), //
   *   DESCRIPTION(&quot;ABC&quot;, &quot;DESCRIPTION&quot;, true), //
   *   DESCRIPTION$DESC(&quot;ABC&quot;, &quot;DESCRIPTION&quot;, false);
   * 
   *   private AbcDAOOrderBy(final String tableName, final String columnName, boolean ascending) {
   *     this.tableName = tableName;
   *     this.columnName = columnName;
   *     this.ascending = ascending;
   *   }
   * 
   *   private String tableName;
   *   private String columnName;
   *   private boolean ascending;
   * 
   *   public String getTableName() {
   *     return this.tableName;
   *   }
   * 
   *   public String getColumnName() {
   *     return this.columnName;
   *   }
   * 
   *   public boolean isAscending() {
   *     return this.ascending;
   *   }
   * }
   * </pre>
   * 
   * @throws IOException
   */

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

  /**
   * <pre>
   * public static long selectSequenceSeqCodes() throws SQLException {
   *   TxManager txm = null;
   *   try {
   *     txm = getTxManager();
   *     SqlSession sqlSession = txm.getSqlSession();
   *     return selectSequenceSeqCodes(sqlSession);
   *   } finally {
   *     if (txm != null && !txm.isTransactionOngoing()) {
   *       txm.close();
   *     }
   *   }
   * }
   * 
   * public static long selectSequenceSeqCodes(final SqlSession sqlSession) {
   *   return sqlSession.selectOne("hotrod.test.generation.primitives.account.sequenceSeqCodes");
   * }
   * </pre>
   * 
   * @throws IOException
   * @throws SequencesNotSupportedException
   */

  private void writeSelectSequence(final SequenceMethodTag tag) throws IOException, SequencesNotSupportedException {

    println("  // sequence " + tag.getSequenceId().getRenderedSQLName());
    println();
    println(ObjectDAO.renderJavaComment(this.generator.getAdapter().renderSelectSequence(tag.getSequenceId())));
    println();

    println("  public static long " + tag.getMethod() + "()");
    print("      ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    println("      return " + tag.getMethod() + "(sqlSession);");
    releaseSqlSession();
    println("  }");
    println();

    print("  public static long " + tag.getMethod() + "(final SqlSession sqlSession) ");
    this.throwsCheckedException();
    println("{");
    preCheckedException();
    println("    return (Long) sqlSession.selectOne(");
    println("      \"" + this.mapper.getFullMapperIdSelectSequence(tag) + "\");");
    postCheckedException();
    println("  }");
    println();

  }

  /**
   * <pre>
   * 
   * public static int update-name() throws SQLException {
   *   TxManager txm = null;
   *   try {
   *     txm = getTxManager();
   *     SqlSession sqlSession = txm.getSqlSession();
   *     return update-name(sqlSession);
   *   } finally {
   *     if (txm != null && !txm.isTransactionOngoing()) {
   *       txm.close();
   *     }
   *   }
   * }
   * 
   * public static int update-name(final SqlSession sqlSession) throws SQLException {
   *   return sqlSession.update("hotrod.test.generation.primitives.account.sequenceSeqCodes");
   * }
   * 
   * </pre>
   * 
   * @throws IOException
   */

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
    String paramCall = pcall.toString();

    // parameter class

    if (!tag.getParameterDefinitions().isEmpty()) {
      println("  public static class " + this.getParamClassName(tag) + " {");
      for (ParameterTag p : tag.getParameterDefinitions()) {
        println("    " + p.getJavaType() + " " + p.getName() + ";");
      }
      println("  }");
      println();
    }

    // main method

    print("  public static int " + methodName + "(");
    print(paramDef);
    print(") ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    print("      int rows = " + methodName + "(sqlSession");
    if (!tag.getParameterDefinitions().isEmpty()) {
      print(", " + paramCall);
    }
    println(");");
    println("      if (!txm.isTransactionOngoing()) {");
    println("        txm.commit();");
    println("      }");
    println("      return rows;");
    releaseSqlSession();
    println("  }");
    println();

    // core method

    print("  public static int " + methodName + "(final SqlSession sqlSession");
    if (!tag.getParameterDefinitions().isEmpty()) {
      print(", " + paramDef);
    }
    print(") ");
    this.throwsCheckedException();
    println("{");
    String objName = null;
    if (!tag.getParameterDefinitions().isEmpty()) {
      objName = provideObjectName(tag.getParameterDefinitions());
      println("    " + this.getParamClassName(tag) + " " + objName + " = new " + this.getParamClassName(tag) + "();");
      for (ParameterTag p : tag.getParameterDefinitions()) {
        println("    " + objName + "." + p.getName() + " = " + p.getName() + ";");
      }
    }
    preCheckedException();
    println("    return sqlSession.update(");
    print("      \"" + this.mapper.getFullMapperIdUpdate(tag) + "\"");
    if (!tag.getParameterDefinitions().isEmpty()) {
      print(", " + objName);
    }
    println(");");
    postCheckedException();
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

  /**
   * <pre>
   * 
   * public static int update-name() throws SQLException {
   *   TxManager txm = null;
   *   try {
   *     txm = getTxManager();
   *     SqlSession sqlSession = txm.getSqlSession();
   *     return update-name(sqlSession);
   *   } finally {
   *     if (txm != null && !txm.isTransactionOngoing()) {
   *       txm.close();
   *     }
   *   }
   * }
   * 
   * public static int update-name(final SqlSession sqlSession) throws SQLException {
   *   return sqlSession.update("hotrod.test.generation.primitives.account.sequenceSeqCodes");
   * }
   * 
   * </pre>
   * 
   * @throws IOException
   */

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
    ListWriter pcall = new ListWriter(", ");
    for (SelectParameterMetadata p : sm.getParameterDefinitions()) {
      String name = p.getParameter().getName();
      pdef.add("final " + p.getParameter().getJavaType() + " " + name);
      pcall.add(name);
    }
    String paramDef = pdef.toString();
    String paramCall = pcall.toString();

    // parameter class

    if (!sm.getParameterDefinitions().isEmpty()) {
      println("  public static class " + this.getParamClassName(sm) + " {");
      for (SelectParameterMetadata p : sm.getParameterDefinitions()) {
        println("    " + p.getParameter().getJavaType() + " " + p.getParameter().getName() + ";");
      }
      println("  }");
      println();
    }

    // main method

    print("  public static " + rt.getReturnType() + " " + methodName + "(");
    print(paramDef);
    print(") ");
    this.throwsCheckedException();
    println("{");
    retrieveSqlSession();
    print("      " + rt.getReturnType() + " result = " + methodName + "(sqlSession");
    if (!sm.getParameterDefinitions().isEmpty()) {
      print(", " + paramCall);
    }
    println(");");
    println("      if (!txm.isTransactionOngoing()) {");
    println("        txm.commit();");
    println("      }");
    println("      return result;");
    releaseSqlSession();
    println("  }");
    println();

    // core method

    print("  public static " + rt.getReturnType() + " " + methodName + "(final SqlSession sqlSession");
    if (!sm.getParameterDefinitions().isEmpty()) {
      print(", " + paramDef);
    }
    print(") ");
    this.throwsCheckedException();
    println("{");
    String objName = null;
    if (!sm.getParameterDefinitions().isEmpty()) {
      objName = provideParamObjectName(sm.getParameterDefinitions());
      println("    " + this.getParamClassName(sm) + " " + objName + " = new " + this.getParamClassName(sm) + "();");
      for (SelectParameterMetadata p : sm.getParameterDefinitions()) {
        println("    " + objName + "." + p.getParameter().getName() + " = " + p.getParameter().getName() + ";");
      }
    }
    preCheckedException();

    String myBatisSelectMethod = sm.isMultipleRows() ? "selectList" : "selectOne";
    print(
        "    return sqlSession." + myBatisSelectMethod + "(\"" + this.mapper.getFullSelectMethodStatementId(sm) + "\"");
    if (!sm.getParameterDefinitions().isEmpty()) {
      print(", " + objName);
    }
    println(");");
    postCheckedException();
    println("  }");
    println();

  }

  /**
   * <pre>
   * // Transaction demarcation
   * 
   * private static TxManager txManager = null;
   * 
   * public static TxManager getTxManager() throws SQLException {
   *   if (txManager == null) {
   *     synchronized (AccountPrimitives.class) {
   *       if (txManager == null) {
   *         txManager = new TxManager(getSqlSessionFactory());
   *       }
   *     }
   *   }
   *   return txManager;
   * }
   * 
   * public static SqlSessionFactory getSqlSessionFactory() throws SQLException {
   *   return fulltest.FullTestDatabaseSessionFactory.getInstance().getSqlSessionFactory();
   * }
   * 
   * public static SqlSession getSqlSession() throws SQLException {
   *   return getSqlSessionFactory().openSession();
   * }
   * </pre>
   */
  private void writeTxManager() throws IOException {
    println("  // Transaction demarcation");
    println();
    println("  private static TxManager txManager = null;");
    println();
    print("  public static TxManager getTxManager() ");
    this.throwsCheckedException();
    println("{");
    println("    if (txManager == null) {");
    println("      synchronized (" + this.getClassName() + ".class) {");
    println("        if (txManager == null) {");
    println("          txManager = new TxManager(getSqlSessionFactory());");
    println("        }");
    println("      }");
    println("    }");
    println("    return txManager;");
    println("  }");
    println();

    print("  public static SqlSessionFactory getSqlSessionFactory() ");
    this.throwsCheckedException();
    println("{");

    preCheckedException();
    println("    return " + this.layout.getSessionFactoryGetter() + ";");
    postCheckedException();

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

  public static String toParametersSignature(final KeyMetadata km, final MyBatisGenerator mg)
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
