package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.DAOType;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.GeneratableObject;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.KeyMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodMetadata.SelectMethodReturnType;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.metadata.StructuredColumnsMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.metadata.VersionControlMetadata;
import org.hotrod.utils.ClassPackage;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;
import org.nocrala.tools.database.tartarus.exception.ReaderException;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class Mapper extends GeneratableObject {

  private static final Logger log = LogManager.getLogger(Mapper.class);

  private DataSetMetadata metadata;
  private DataSetLayout layout;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private AbstractDAOTag compositeTag;

  private MyBatisSpringGenerator generator;

  private DAOType type;

  private DatabaseAdapter adapter;

  private String namespace;

  private ObjectVO vo;
  private ObjectDAO dao;

  private EntityDAORegistry entityDAORegistry;

  private TextWriter w;

  public Mapper(final AbstractDAOTag compositeTag, final DataSetMetadata metadata, final DataSetLayout layout,
      final MyBatisSpringGenerator generator, final DAOType type, final DatabaseAdapter adapter, final ObjectVO vo,
      final EntityDAORegistry daoRegistry) {
    super();
    log.debug("init");
    this.compositeTag = compositeTag;
    this.entityDAORegistry = daoRegistry;
    initialize(metadata, layout, generator, type, adapter, vo);
  }

  // TODO: Clean up
//  public Mapper(final SelectClassTag selectTag, final DataSetMetadata metadata, final DataSetLayout layout,
//      final MyBatisSpringGenerator generator, final DAOType type, final DatabaseAdapter adapter, final ObjectVO vo) {
//    super();
//    log.debug("init");
//    this.compositeTag = null;
//    this.selectTag = selectTag;
//    this.entityDAORegistry = null;
//    initialize(metadata, layout, generator, type, adapter, vo);
//  }

  private void initialize(final DataSetMetadata metadata, final DataSetLayout layout,
      final MyBatisSpringGenerator generator, final DAOType type, final DatabaseAdapter adapter, final ObjectVO vo) {
    this.metadata = metadata;

    this.fragmentConfig = this.metadata.getFragmentConfig();
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    this.layout = layout;
    this.generator = generator;

    metadata.getDaoTag().addGeneratableObject(this);

    if (type == null) {
      throw new RuntimeException("DAOType cannot be null.");
    }
    this.type = type;
    this.adapter = adapter;
    this.vo = vo;

    this.namespace = this.layout.getMapperNamespace()
        + (this.fragmentPackage == null ? "" : "." + this.fragmentPackage.getPackage()) + "."
        + this.metadata.getId().getJavaMemberName();
  }

  public void setDao(ObjectDAO dao) {
    this.dao = dao;
  }

  private boolean isTable() {
    return this.type == DAOType.TABLE;
  }

  private boolean isView() {
    return this.type == DAOType.VIEW;
  }

  private boolean isPlain() {
    return this.type == DAOType.EXECUTOR;
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException, ControlledException {
    String sourceFileName = this.getSourceFileName();

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    File mapper = new File(this.layout.getMapperPrimitiveDir(fragmentPackage), sourceFileName);
    this.w = null;

    try {
      this.w = fileGenerator.createWriter(mapper);

      writeHeader();

      writeColumns();

      if (!this.isPlain()) {

        writeResultMap();

        if (this.isTable()) {
          writeSelectByPK();
          writeSelectByUI();
        }

        writeSelectByExample();
        writeSelectByCriteria();

        if (this.isTable()) {
          writeInsert();
          writeUpdateByPK();
          writeDeleteByPK();
        }

        if (this.isView()) {
          writeInsertByExample();
        }
        writeUpdateByExample();
        writeUpdateByCriteria();

        writeDeleteByExample();
        writeDeleteByCriteria();

      }

      if (this.compositeTag != null) {

        for (SequenceMethodTag s : this.compositeTag.getSequences()) {
          try {
            writeSelectSequence(s);
          } catch (SequencesNotSupportedException e) {
            throw new ControlledException("Could not generate mapper for sequence "
                + s.getSequenceId().getRenderedSQLName() + " onto file: " + e.getMessage());
          }
        }

        for (QueryMethodTag q : this.compositeTag.getQueries()) {
          try {
            writeQuery(q);
          } catch (SequencesNotSupportedException e) {
            throw new ControlledException(
                "Could not generate mapper for query '" + q.getMethod() + "' onto file: " + e.getMessage());
          }
        }

        for (SelectMethodMetadata sm : this.metadata.getSelectsMetadata()) {
          log.debug("Generating method: " + sm.getMethod());
          writeSelectMethod(sm);
        }

      }

      writeFooter();

      super.markGenerated();

    } catch (IOException e) {
      throw new UncontrolledException(
          "Could not generate mapper file: could not write to file '" + mapper.getName() + "'.", e);
    } catch (UnresolvableDataTypeException e) {
      throw new ControlledException(
          "Could not generate mapper file: " + "could not resolve the database column type: " + e.getMessage());
    } catch (SequencesNotSupportedException e) {
      throw new ControlledException(
          "Could not generate mapper file: " + "this database does not support sequences: " + e.getMessage());
    } catch (IdentitiesPostFetchNotSupportedException e) {
      throw new ControlledException("Could not generate mapper file: "
          + "this database does not support retrieving identity values after an insert: " + e.getMessage());
    } finally {
      if (this.w != null) {
        try {
          this.w.close();
        } catch (IOException e) {
          throw new UncontrolledException(
              "Could not generate mapper file: could not close file '" + mapper.getName() + "'.", e);
        }
      }
    }
  }

  /**
   * <pre>
   * <?xml version="1.0" encoding="UTF-8" ?>
   * <!DOCTYPE mapper
   *   PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
   *   "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   * <mapper namespace="simpletests.dao">
   * </pre>
   * 
   * @throws IOException
   */

  private void writeHeader() throws IOException {
    println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    println("<!DOCTYPE mapper");
    println("  PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
    println("  \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
    println("<mapper namespace=\"" + this.namespace + "\">");
    println();
  }

  /**
   * <pre>
   *   <sql id="columns">
   *     ${prefix}ID as id,
   *     ${prefix}CURRENT_BALANCE as currentBalance,
   *     ${prefix}STARTED_ON as startedOn
   *   </sql>
   * </pre>
   * 
   * @throws IOException
   */
  private void writeColumns() throws IOException {

    // Without prefix

    println("  <!-- columns -->");
    println();
    println("  <sql id=\"columns\">");

    StringBuilder sb = new StringBuilder("    ");
    ListWriter lw = new ListWriter(",\n    ");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      lw.add(SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()));
    }
    sb.append(lw.toString());
    println(sb.toString());

    println("  </sql>");
    println();

    // With prefix

    println("  <sql id=\"columnsWithAlias\">");

    sb = new StringBuilder("    ");
    lw = new ListWriter(",\n    ");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      lw.add("${alias}." + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()));
    }
    sb.append(lw.toString());
    println(sb.toString());

    println("  </sql>");
    println();

  }

  private static final String RESULT_MAP_NAME = "allColumns";

  /**
   * <pre>
   * 
   * <resultMap id="resultMap" type="hotrod.test.generation.QuadrantDAO">
   *   <id property="region" column="region" />
   *   <id property="area" column="area" />
   *   <result property="caption" column="caption" />
   *   <result property="active" column="active" typeHandler=
  "tests.typehandler.BooleanShortTypeHandler" />
   * </resultMap>
   * 
   * </pre>
   * 
   * @throws IOException
   */

  private void writeResultMap() throws IOException {
    println("  <resultMap id=\"" + RESULT_MAP_NAME + "\" type=\"" + this.vo.getFullClassName() + "\">");

    // First, only the ids

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.belongsToPK()) {
        renderResultMapColumn(cm, "id");
      }
    }

    // Then, the non-ids

    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (!cm.belongsToPK()) {
        renderResultMapColumn(cm, "result");
      }
    }

    println("  </resultMap>");
    println();
  }

  private void renderResultMapColumn(final ColumnMetadata cm, final String tagName) throws IOException {

    String typeHandler = "";
    if (cm.getConverter() != null) {
      typeHandler = "typeHandler=\"" + this.dao.getTypeHandlerFullClassName(cm) + "\" ";
    } else {
      EnumDataSetMetadata ds = cm.getEnumMetadata();
      EnumClass ec = this.generator.getEnum(ds);
      if (ec != null) {
        typeHandler = "typeHandler=\"" + this.dao.getTypeHandlerFullClassName(cm) + "\" ";
      }
    }

    String indent = SUtil.getFiller(' ', 4);
    println(indent + "<" + tagName + " property=\"" + cm.getId().getJavaMemberName() + "\" column=\""
        + SUtil.escapeXmlAttribute(cm.getName()) + "\" " + typeHandler + "/>");

  }

  private void renderResultMapColumn(final SelectMethodMetadata sm, final ColumnMetadata cm, final String tagName)
      throws IOException {

    String typeHandler = "";
    if (cm.getConverter() != null) {
      typeHandler = "typeHandler=\"" + this.dao.getTypeHandlerFullClassName(sm, cm) + "\" ";
    } else {
      EnumDataSetMetadata ds = cm.getEnumMetadata();
      EnumClass ec = this.generator.getEnum(ds);
      if (ec != null) {
        typeHandler = "typeHandler=\"" + this.dao.getTypeHandlerFullClassName(sm, cm) + "\" ";
      }
    }

    String indent = SUtil.getFiller(' ', 4);
    println(indent + "<" + tagName + " property=\"" + cm.getId().getJavaMemberName() + "\" column=\""
        + SUtil.escapeXmlAttribute(cm.getName()) + "\" " + typeHandler + "/>");

  }

  private void writeSelectByPK() throws IOException {
    if (this.metadata.getPK() == null) {
      println("  <!-- select by PK: no mapping generated, since the table does not have a PK -->");
      println();
    } else {
      println("  <!-- select by PK -->");
      println();
      println("  <select id=\"" + this.getMapperIdSelectByPK() + "\" resultMap=\"" + RESULT_MAP_NAME + "\">");
      println("    select");
      println("      <include refid=\"columns\" />");
      println("     from " + SUtil.escapeXmlBody(this.metadata.getId().getRenderedSQLName()));
      println("     where");
      println(getWhereByIndex(this.metadata.getPK()));
      println("  </select>");
      println();
    }
  }

  private void writeSelectByUI() throws IOException {
    if (this.metadata.getUniqueIndexes().isEmpty()) {
      println("  <!-- select by unique indexes: no mappings generated, " + "since the table does not have UIs -->");
      println();
    } else {
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
            println("  <!-- select by unique indexes -->");
            println();
          }
          println("  <select id=\"" + this.getMapperIdSelectByUI(ui) + "\" resultMap=\"" + RESULT_MAP_NAME + "\">");
          println("    select");
          println("      <include refid=\"columns\" />");
          println("     from " + SUtil.escapeXmlBody(this.metadata.getId().getRenderedSQLName()));
          println("     where");
          println(getWhereByIndex(ui));
          println("  </select>");
          println();
        }
      }
      if (first) {
        println("  <!-- no select by unique indexes since there are no UI "
            + (this.metadata.getPK() == null ? "" : "(besides the PK) ") + "-->");
        println();
      }
    }
  }

  /**
   * <pre>
   *   <select id="selectAbc" resultType="dao.AbcDAO">
   *     select * from abc 
   *     <where>
   *       <if test="state != null">
   *         and state = #{state}
   *       </if>
   *     </where>
   *     <if test="o != null">
   *       order by ${o}
   *     </if>
   *   </select>
   * </pre>
   * 
   * @throws IOException
   */
  private void writeSelectByExample() throws IOException {
    println("  <!-- select by example -->");
    println();
    println("  <select id=\"" + this.getMapperIdSelectByExample() + "\" resultMap=\"" + RESULT_MAP_NAME + "\">");
    println("    select");
    println("      <include refid=\"columns\" />");
    println("     from " + SUtil.escapeXmlBody(this.metadata.getId().getRenderedSQLName()));
    print(getWhereByExample("p"));
    println("    <if test=\"o != null\">");
    println("      order by ${o}");
    println("    </if>");
    println("  </select>");
    println();
  }

  // select by criteria

  private void writeSelectByCriteria() throws IOException {
    println("  <!-- select by criteria -->");
    println();
    println("  <select id=\"selectByCriteria\" resultMap=\"allColumns\">");
    println("    ${sql}");
    println("  </select>");
    println();
  }

  // === Insert (only tables) ===

  private void writeInsert() throws ControlledException, IOException, SequencesNotSupportedException,
      IdentitiesPostFetchNotSupportedException {

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

    if (identities == 0) {
      if (sequences == 0) { // no sequences, no identities
        writeInsertWithoutAutoGeneratedColumn(false);
      } else { // sequences only
        if (integratesSequences) {
          writeInsertIntegrated(true, false, false);
          if (integratesDefaults && defaults != 0) {
            writeInsertIntegrated(true, false, true);
          }
        } else {
          writeInsertUsingSequencesPreFetch();
        }
      }
    } else {
      if (sequences == 0) { // identities only
        if (integratesIdentities) {
          writeInsertIntegrated(false, true, false);
        } else {
          writeInsertUsingIdentities(true);
        }
      } else { // sequences & identities
        if (integratesSequences && integratesIdentities) {
          writeInsertIntegrated(true, true, false);
          if (integratesDefaults && defaults != 0) {
            writeInsertIntegrated(true, true, true);
          }
        } else if (integratesIdentities) {
          writeSequencesPreFetch();
          writeInsertIntegrated(false, true, false);
        } else if (integratesSequences) {
          writeInsertIntegrated(true, false, false);
          writeIdentitiesPostFetch();
        } else {
          writeSequencesPreFetch();
          writeInsertUsingIdentities(false);
          writeIdentitiesPostFetch();
        }

      }
    }
  }

  private void appendColumn(final ListWriter columns, final ListWriter values, final ColumnMetadata cm) {
    appendColumn(false, columns, values, cm);
  }

  private void appendColumn(final boolean byExample, final ListWriter columns, final ListWriter values,
      final ColumnMetadata cm) {
    if (byExample || cm.getColumnDefault() != null) {
      String prop = cm.getId().getJavaMemberName();
      columns
          .add("<if test=\"" + prop + " != null\">, " + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + "</if>");
      values.add("<if test=\"" + prop + " != null\">, " + renderParameterColumn(cm) + "</if>");
    } else {
      columns.add("<if test=\"true\">, " + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + "</if>");
      values.add("<if test=\"true\">, " + renderParameterColumn(cm) + "</if>");
    }
  }

  private void appendSequenceColumn(final ListWriter columns, final ListWriter values, final ColumnMetadata cm)
      throws SequencesNotSupportedException {
    columns.add("<if test=\"true\">, " + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + "</if>");
    values.add("<if test=\"true\">, " + SUtil.escapeXmlBody(this.adapter.renderInlineSequenceOnInsert(cm)) + "</if>");
  }

  private void renderInsert(final ListWriter columns, final ListWriter values, final ListWriter queryColumns)
      throws IOException {
    println("    insert into " + SUtil.escapeXmlBody(this.metadata.getId().getRenderedSQLName()) + " (");
    print("      <trim prefixOverrides=\", \">\n" + columns.toString() + "      </trim>\n");
    print("      ) ");
    if (queryColumns.getCount() != 0) {
      print("output " + queryColumns.toString() + "\n        ");
    }
    println("values (");
    println("      <trim prefixOverrides=\", \">\n" + values.toString() + "      </trim>");
    println("      )");
  }

  private void writeInsertWithoutAutoGeneratedColumn(final boolean byExample) throws IOException {
    println("  <!-- insert " + (byExample ? "by example " : "") + "(no auto-generated columns) -->");
    println();

    String id = byExample ? this.getMapperIdInsertByExample() : this.getMapperIdInsert();

    println("  <insert id=\"" + id + "\">");
    ListWriter columns = new ListWriter("        ", "\n", "");
    ListWriter values = new ListWriter("        ", "\n", "");
    ListWriter queryColumns = new ListWriter(", ");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      appendColumn(byExample, columns, values, cm);
    }

    renderInsert(columns, values, queryColumns);

    println("  </insert>");
    println();
  }

  private void writeInsertIntegrated(final boolean retrieveSequences, final boolean retrieveIdentities,
      final boolean retrieveDefaults) throws IOException, SequencesNotSupportedException {

    ListWriter msg = new ListWriter(", ", ", and ");
    msg.add((retrieveSequences ? "integrates" : "does not integrate") + " sequences");
    msg.add((retrieveIdentities ? "integrates" : "does not integrate") + " identities");
    if (retrieveDefaults) {
      msg.add("integrates defaults");
    }
    println("  <!-- insert (" + msg.toString() + ") -->");
    println();

    ListWriter keyProperties = new ListWriter(",");
    ListWriter keyColumns = new ListWriter(",");

    ListWriter columns = new ListWriter("        ", "\n", "");
    ListWriter values = new ListWriter("        ", "\n", "");
    ListWriter queryColumns = new ListWriter(", ");

    for (ColumnMetadata cm : this.metadata.getColumns()) {

      String mapperColName = cm.getId().getRenderedSQLName();
      if (mapperColName != null && mapperColName.length() > 1 && mapperColName.startsWith("\"")
          && mapperColName.endsWith("\"")) {
        mapperColName = mapperColName.substring(1, mapperColName.length() - 1);
      }
      log.debug("mapperColName=" + mapperColName);

      if (retrieveSequences && cm.getSequenceId() != null) {
        keyProperties.add(cm.getId().getJavaMemberName());
        keyColumns.add(mapperColName);
        if (this.adapter.integratesUsingQuery()) {
          queryColumns.add(this.adapter.renderInsertQueryColumn(cm));
        }
        appendSequenceColumn(columns, values, cm);

      } else if (retrieveIdentities && cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity()) {
        keyProperties.add(cm.getId().getJavaMemberName());
        keyColumns.add(mapperColName);
        if (this.adapter.integratesUsingQuery()) {
          queryColumns.add(this.adapter.renderInsertQueryColumn(cm));
        }
        if (cm.getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT) {
          String prop = cm.getId().getJavaMemberName();
          columns.add(
              "<if test=\"" + prop + " != null\">, " + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + "</if>");
          values.add("<if test=\"" + prop + " != null\">, " + renderParameterColumn(cm) + "</if>");
        }

      } else if (retrieveDefaults && cm.getColumnDefault() != null) {
        keyProperties.add(cm.getId().getJavaMemberName());
        keyColumns.add(mapperColName);
        if (this.adapter.integratesUsingQuery()) {
          queryColumns.add(this.adapter.renderInsertQueryColumn(cm));
        }
        appendColumn(columns, values, cm);

      } else {
        appendColumn(columns, values, cm);

      }
    }

    log.debug("keyColumns.toString()=" + keyColumns.toString());

    String id = retrieveDefaults ? this.getMapperIdInsertRetrievingDefaults() : this.getMapperIdInsert();

    if (this.adapter.integratesUsingQuery()) {
      println("  <select id=\"" + id + "\" resultType=\"" + this.vo.getFullClassName() + "\">");
      renderInsert(columns, values, queryColumns);
      println("  </select>");
    } else {
      println("  <insert id=\"" + id + "\" useGeneratedKeys=\"true\" keyProperty=\"" + keyProperties.toString()
          + "\" keyColumn=\"" + keyColumns.toString() + "\">");
      renderInsert(columns, values, queryColumns);
      println("  </insert>");
    }

    println();

  }

  private void writeInsertUsingSequencesPreFetch() throws IOException, SequencesNotSupportedException {
    println("  <!-- insert (using sequences prefetch) -->");
    println();

    List<ColumnMetadata> sequences = new ArrayList<ColumnMetadata>();
    ListWriter keyProperties = new ListWriter(",");
    ListWriter columns = new ListWriter("        ", "\n", "");
    ListWriter values = new ListWriter("        ", "\n", "");
    ListWriter queryColumns = new ListWriter(", ");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getSequenceId() != null) {
        sequences.add(cm);
        keyProperties.add(cm.getId().getJavaMemberName());
      }
      appendColumn(columns, values, cm);
    }

    String id = this.getMapperIdInsert();

    println("  <insert id=\"" + id + "\">");
    println("    <selectKey keyProperty=\"" + keyProperties.toString() + "\" resultType=\"" + this.vo.getFullClassName()
        + "\" order=\"BEFORE\">");
    println("      " + SUtil.escapeXmlBody(this.adapter.renderSequencesPrefetch(sequences)));
    println("    </selectKey>");

    renderInsert(columns, values, queryColumns);

    println("  </insert>");
    println();
  }

  private void writeInsertUsingIdentities(final boolean postFetch)
      throws IOException, IdentitiesPostFetchNotSupportedException {
    if (postFetch) {
      println("  <!-- insert (using identities post fetch) -->");
    } else {
      println("  <!-- insert -->");
    }
    println();

    List<ColumnMetadata> identities = new ArrayList<ColumnMetadata>();
    ListWriter keyProperties = new ListWriter(",");
    ListWriter columns = new ListWriter("        ", "\n", "");
    ListWriter values = new ListWriter("        ", "\n", "");
    ListWriter queryColumns = new ListWriter(", ");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity()) {
        identities.add(cm);
        keyProperties.add(cm.getId().getJavaMemberName());
        if (cm.getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT) {
          String prop = cm.getId().getJavaMemberName();
          columns.add(
              "<if test=\"" + prop + " != null\">, " + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + "</if>");
          values.add("<if test=\"" + prop + " != null\">, " + renderParameterColumn(cm) + "</if>");
        }
      } else {
        appendColumn(columns, values, cm);
      }
    }

    String id = this.getMapperIdInsert();

    println("  <insert id=\"" + id + "\">");
    if (postFetch) {
      println("    <selectKey keyProperty=\"" + keyProperties.toString() + "\" resultType=\""
          + this.vo.getFullClassName() + "\" order=\"AFTER\">");
      println("      " + SUtil.escapeXmlBody(this.adapter.renderIdentitiesPostfetch(identities)));
      println("    </selectKey>");
    }

    renderInsert(columns, values, queryColumns);

    println("  </insert>");
    println();
  }

  private boolean sequencesPreFetchRendered = false;

  private void writeSequencesPreFetch() throws IOException, SequencesNotSupportedException {

    if (!this.sequencesPreFetchRendered) {

      println("  <!-- select sequences (prefetch) -->");
      println();

      List<ColumnMetadata> sequences = new ArrayList<ColumnMetadata>();
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        if (cm.getSequenceId() != null) {
          sequences.add(cm);
        }
      }

      println("  <select id=\"" + this.getMapperIdSequencesPreFetch() + "\" resultType=\"" + this.vo.getFullClassName()
          + "\">");
      println("    " + SUtil.escapeXmlBody(this.adapter.renderSequencesPrefetch(sequences)));
      println("  </select>");
      println();

      this.sequencesPreFetchRendered = true;

    }

  }

  private boolean identitiesPostFetchRendered = false;

  private void writeIdentitiesPostFetch() throws IOException, IdentitiesPostFetchNotSupportedException {

    if (!this.identitiesPostFetchRendered) {

      println("  <!-- select identities just generated (post-fetch) -->");
      println();

      List<ColumnMetadata> identities = new ArrayList<ColumnMetadata>();
      for (ColumnMetadata cm : this.metadata.getColumns()) {
        if (cm.getAutogenerationType() != null && cm.getAutogenerationType().isIdentity()) {
          identities.add(cm);
        }
      }
      println("  <select id=\"" + this.getMapperIdIdentitiesPostFetch() + "\" resultType=\""
          + this.vo.getFullClassName() + "\">");
      println("    " + SUtil.escapeXmlBody(this.adapter.renderIdentitiesPostfetch(identities)));
      println("  </select>");
      println();

      this.identitiesPostFetchRendered = true;

    }

  }

  // === Insert by Example (only views) ===

  private void writeInsertByExample() throws ControlledException, IOException, SequencesNotSupportedException,
      IdentitiesPostFetchNotSupportedException {
    writeInsertWithoutAutoGeneratedColumn(true);
  }

  private String renderParameterColumn(final ColumnMetadata cm) {
    return renderParameterColumn(cm, null);
  }

  private String renderParameterColumn(final ColumnMetadata cm, final String prefix) {
    String name = (prefix != null ? (prefix + ".") : "") + cm.getId().getJavaMemberName();
    String jdbcType = "jdbcType=" + cm.getType().getJDBCShortType();
    String typeHandler = null;
    if (cm.getConverter() != null) {
      typeHandler = "typeHandler=" + this.dao.getTypeHandlerFullClassName(cm);
    } else {
      EnumDataSetMetadata ds = cm.getEnumMetadata();
      EnumClass ec = this.generator.getEnum(ds);
      if (ec != null) {
        typeHandler = "typeHandler=" + this.dao.getTypeHandlerFullClassName(cm);
      }
    }

    return "#{" + name + "," + jdbcType + (typeHandler != null ? ("," + typeHandler) : "") + "}";
  }

  // Update

  /**
   * <pre>
   *   <update id="updateAbc">
   *     update Author set
   *     username = #{username},
   *     password =
   *     #{password},
   *     email =
   *     #{email},
   *     bio = #{bio}
   *     where id = #{id}
   *   </update>
   * </pre>
   * 
   * @throws IOException
   * @throws UnresolvableDataTypeException
   * @throws ReaderException
   */

  private void writeUpdateByPK() throws IOException, UnresolvableDataTypeException {
    if (this.metadata.getPK() == null) {
      println("  <!-- update by PK: no update mapping generated, since the table does not have a PK -->");
      println();
    } else if (this.metadata.getNonPkColumns().isEmpty()) {
      println("  <!-- update by PK: no update mapping generated, since there aren't any columns besides the PK -->");
      println();
    } else {

      VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();
      boolean useVersionControl = vcm != null;

      println("  <!-- update by PK -->");
      println();
      println("  <update id=\"" + this.getMapperIdUpdateByPK() + "\">");
      println("    update " + SUtil.escapeXmlBody(this.metadata.getId().getRenderedSQLName()) + " set");

      ListWriter lw = new ListWriter(",\n");
      for (ColumnMetadata cm : this.metadata.getNonPkColumns()) {
        String sqlColumn = cm.getId().getRenderedSQLName();
        if (useVersionControl) {
          if (useVersionControl && vcm.getColumnMetadata().equals(cm)) {
            lw.add("      " + SUtil.escapeXmlBody(sqlColumn) + " = #{nextVersionValue}");
          } else {
            lw.add("      " + SUtil.escapeXmlBody(sqlColumn) + " = " + renderParameterColumn(cm, "p"));
          }
        } else {
          lw.add("      " + SUtil.escapeXmlBody(sqlColumn) + " = " + renderParameterColumn(cm));
        }
      }
      println(lw.toString());

      println("     where");
      if (useVersionControl) {
        println(getWhereByIndex(this.metadata.getPK(), "p"));
      } else {
        println(getWhereByIndex(this.metadata.getPK()));
      }

      if (useVersionControl) {
        ColumnMetadata cm = vcm.getColumnMetadata();
        String sqlColumn = cm.getId().getRenderedSQLName();

        println("     and");
        println("      " + SUtil.escapeXmlBody(sqlColumn) + " " + "= " + renderParameterColumn(cm));

      }

      println("  </update>");
      println();
    }
  }

  private String getWhereByIndex(final KeyMetadata km) throws IOException {
    return getWhereByIndex(km, null);
  }

  private String getWhereByIndex(final KeyMetadata km, final String prefix) throws IOException {
    ListWriter lw;
    lw = new ListWriter("\n      and ");
    for (ColumnMetadata cm : km.getColumns()) {
      lw.add(SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + " = " + renderParameterColumn(cm, prefix));
    }
    return "      " + lw.toString();
  }

  private String getWhereByExample() throws IOException {
    return getWhereByExample(null);
  }

  private String getWhereByExample(final String prefix) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("    <trim prefix=\"where&#xA;\" prefixOverrides=\"and |or \">\n");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      String prompt = prefix != null ? (prefix + ".") : "";
      String prop = prompt + cm.getId().getJavaMemberName();

      sb.append("      <if test=\"" + prop + " != null\">\n");
      sb.append("        and " + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + " = "
          + renderParameterColumn(cm, prefix) + "\n");
      sb.append("      </if>\n");
    }
    sb.append("    </trim>\n");
    return sb.toString();
  }

  /**
   * <pre>
   *   <update id="updateByExample">
   *     update Author
   *     <set>
   *       <if test="values.username != null">username=#{values.username},</if>
   *       <if test="values.password != null">password=#{values.password},</if>
   *     </set>    
   *     <where>
   *       <if test="filter.state != null">
   *         and state = #{filter.state}
   *       </if>
   *       <if test="filter.title != null">
   *         and title like #{filter.title}
   *       </if>
   *     </where>
   *   </update>
   * </pre>
   * 
   * @throws UnresolvableDataTypeException
   */

  private void writeUpdateByExample() throws IOException, UnresolvableDataTypeException {
    println("  <!-- update by example -->");
    println();
    println("  <update id=\"" + this.getMapperIdUpdateByExample() + "\">");
    println("    update " + SUtil.escapeXmlBody(this.metadata.getId().getRenderedSQLName()));

    println("    <set>");
    for (ColumnMetadata cm : this.metadata.getColumns()) {
      if (cm.isVersionControlColumn()) {
        println("      " + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + " = "
            + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + " + 1,");
      } else {
        println("      <if test=\"values." + cm.getId().getJavaMemberName() + " != null\">"
            + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + " = " + renderParameterColumn(cm, "values")
            + ",</if>");
      }
    }
    println("    </set>");

    print(getWhereByExample("filter"));

    println("  </update>");
    println();
  }

  // update by criteria

  private void writeUpdateByCriteria() throws IOException {
    println("  <!-- update by criteria -->");
    println();
    println("  <select id=\"updateByCriteria\">");
    println("    ${sql}");
    println("  </select>");
    println();
  }

  /**
   * <pre>
   *   <delete id="deleteAbc">
   *     delete from Author where id = #{id}
   *   </delete>
   * </pre>
   * 
   * @throws IOException
   * @throws UnresolvableDataTypeException
   */
  private void writeDeleteByPK() throws IOException, UnresolvableDataTypeException {
    if (this.metadata.getPK() == null) {
      println("  <!-- delete by PK: no delete mapping generated, since the table does not have a PK -->");
      println();
    } else {
      println("  <!-- delete by PK -->");
      println();
      println("  <delete id=\"" + this.getMapperIdDeleteByPK() + "\">");
      println("    delete from " + SUtil.escapeXmlBody(this.metadata.getId().getRenderedSQLName()));
      println("     where");
      println(getWhereByIndex(this.metadata.getPK()));

      VersionControlMetadata vcm = this.metadata.getVersionControlMetadata();
      boolean useVersionControl = vcm != null;

      if (useVersionControl) {
        ColumnMetadata cm = vcm.getColumnMetadata();
        println("     and");
        println(
            "      " + SUtil.escapeXmlBody(cm.getId().getRenderedSQLName()) + " " + "= " + renderParameterColumn(cm));
      }

      println("  </delete>");
      println();
    }
  }

  private void writeDeleteByExample() throws IOException {
    println("  <!-- delete by example -->");
    println();
    println("  <delete id=\"" + this.getMapperIdDeleteByExample() + "\">");
    println("    delete from " + SUtil.escapeXmlBody(this.metadata.getId().getRenderedSQLName()));
    print(getWhereByExample());
    println("  </delete>");
    println();
  }

  // delete by criteria

  private void writeDeleteByCriteria() throws IOException {
    println("  <!-- delete by criteria -->");
    println();
    println("  <select id=\"deleteByCriteria\">");
    println("    ${sql}");
    println("  </select>");
    println();
  }

  /**
   * <pre>
   * 
   *   <!-- select sequence <seq-name> -->
   * 
   *   <select id="selectSequence<seq-name>" resultType="java.lang.Long">
   *     ...sql...
   *   </select>
   * 
   * </pre>
   * 
   * @throws SequencesNotSupportedException
   */

  private void writeSelectSequence(final SequenceMethodTag seq) throws IOException, SequencesNotSupportedException {
    println("  <!-- select sequence " + seq.getSequenceId().getRenderedSQLName() + " -->");
    println();
    println("  <select id=\"" + this.getMapperSelectSequence(seq) + "\" " + "resultType=\"java.lang.Long\">");
    String sentence = this.adapter.renderSelectSequence(seq.getSequenceId());
    println("    " + SUtil.escapeXmlBody(sentence));
    println("  </select>");
    println();
  }

  /**
   * <pre>
   * 
   *   <!-- update <update-name> -->
   * 
   *   <update id="<update-name>">
   *     ...sql...
   *   </update>
   * 
   * </pre>
   * 
   */

  private void writeQuery(final QueryMethodTag u) throws IOException, SequencesNotSupportedException {
    println("  <!-- query " + u.getMethod() + " -->");
    println();

    println("  <update id=\"" + u.getId().getJavaMemberName() + "\">");
    String sentence = u.renderXML(new MyBatisParameterRenderer());
    println("    " + sentence);
    println("  </update>");
    println();

  }

  private void writeSelectMethod(final SelectMethodMetadata sm) throws IOException {
    println("  <!-- select method: " + sm.getMethod() + " -->");
    println();

    if (sm.getEntityMetadata() == null) {

      // result map

      String resultMapName = this.getResultMapName(sm);

      SelectMethodReturnType rt = sm.getReturnType(this.layout.getDAOPackage(this.fragmentPackage));

      println("  <resultMap id=\"" + resultMapName + "\" type=\"" + rt.getVOFullClassName() + "\">");

      if (!sm.isStructured()) {

        for (ColumnMetadata cm : sm.getNonStructuredColumns()) {
          renderResultMapColumn(sm, cm, "result");
        }

      } else {

        StructuredColumnsMetadata scm = sm.getStructuredColumns();

        boolean soloVO = scm.getExpressions().isEmpty() && scm.getVOs().size() == 1;
        if (soloVO) {
          VOMetadata vo = scm.getVOs().get(0);

          String entityFullClassName = vo.getSuperClass() != null ? vo.getSuperClass().getFullClassName()
              : vo.getFullClassName();

          ObjectDAO dao = this.entityDAORegistry.findEntityDAO(entityFullClassName);
          renderResultMapLevel(sm, vo.getInheritedColumns(), vo.getDeclaredColumns(), vo.getAssociations(),
              vo.getCollections(), dao, 0);
        } else {
          renderResultMapLevel(sm, null, scm.getColumnsMetadata(), scm.getVOs(), null, null, 0);
        }

      }

      println("  </resultMap>");
      println();

      // statement

      String statementId = this.getSelectMethodStatementId(sm);

      println("  <select id=\"" + statementId + "\" resultMap=\"" + resultMapName + "\">");
      log.debug("sm.getMethod()=" + sm.getMethod());
      println(sm.renderXML(new MyBatisParameterRenderer()));
      println("  </select>");
      println();

    } else {

      String statementId = this.getSelectMethodStatementId(sm);

      println("  <select id=\"" + statementId + "\" resultMap=\"" + RESULT_MAP_NAME + "\">");
      log.debug("sm.getMethod()=" + sm.getMethod());
      println(sm.renderXML(new MyBatisParameterRenderer()));
      println("  </select>");
      println();

    }

  }

  private void renderResultMapLevel(final SelectMethodMetadata sm,
      final List<StructuredColumnMetadata> inheritedColumns, final List<StructuredColumnMetadata> declaredColumns,
      final List<VOMetadata> associations, final List<VOMetadata> collections, final ObjectDAO dao, final int level)
      throws IOException {

    // Main VO columns

    if (inheritedColumns != null) {
      for (StructuredColumnMetadata m : inheritedColumns) {
        if (m.isId()) {
          renderSelectResultMapColumn(sm, m, "id", dao, level);
        }
      }
      for (StructuredColumnMetadata m : inheritedColumns) {
        if (!m.isId()) {
          renderSelectResultMapColumn(sm, m, "result", dao, level);
        }
      }
    }

    // Expression columns

    if (declaredColumns != null) {
      for (StructuredColumnMetadata m : declaredColumns) {
        renderSelectResultMapColumn(sm, m, m.isId() ? "id" : "result", dao, level);
      }
    }

    // Associations

    String indent = SUtil.getFiller(' ', 4 + (level * 2));

    for (VOMetadata a : associations) {

      println(indent + "<association property=\"" + a.getProperty() + "\" javaType=\"" + a.getFullClassName() + "\">");
      String entityFullClassName = a.getSuperClass() != null ? a.getSuperClass().getFullClassName()
          : a.getFullClassName();
      ObjectDAO aDAO = entityDAORegistry.findEntityDAO(entityFullClassName);
      renderResultMapLevel(sm, a.getInheritedColumns(), a.getDeclaredColumns(), a.getAssociations(), a.getCollections(),
          aDAO, level + 1);
      println(indent + "</association>");
    }

    // Collections

    if (collections != null) {
      for (VOMetadata c : collections) {
        println(indent + "<collection property=\"" + c.getProperty() + "\" ofType=\"" + c.getFullClassName() + "\">");
        String entityFullClassName = c.getSuperClass() != null ? c.getSuperClass().getFullClassName()
            : c.getFullClassName();
        ObjectDAO cDAO = entityDAORegistry.findEntityDAO(entityFullClassName);
        renderResultMapLevel(sm, c.getInheritedColumns(), c.getDeclaredColumns(), c.getAssociations(),
            c.getCollections(), cDAO, level + 1);
        println(indent + "</collection>");
      }
    }

  }

  private void renderSelectResultMapColumn(final SelectMethodMetadata sm, final StructuredColumnMetadata cm,
      final String tagName, final ObjectDAO dao, final int level) throws IOException {

    String typeHandler = "";
    if (cm.getConverter() != null) {
      log.debug("converter=" + cm.getConverter().getName() + " cm=" + cm.getName());
      typeHandler = "typeHandler=\"" + dao.getTypeHandlerFullClassName(sm, cm) + "\" ";
    } else {
      EnumDataSetMetadata ds = cm.getEnumMetadata();
      EnumClass ec = this.generator.getEnum(ds);
      if (ec != null) {
        typeHandler = "typeHandler=\"" + dao.getTypeHandlerFullClassName(cm) + "\" ";
      }
    }

    String indent = SUtil.getFiller(' ', 4 + (level * 2));
    println(indent + "<" + tagName + " property=\"" + cm.getId().getJavaMemberName() + "\" column=\""
        + SUtil.escapeXmlAttribute(cm.getColumnAlias()) + "\" " + typeHandler + "/>");

  }

  private void writeFooter() throws IOException {
    println("</mapper>");
  }

  // Info

  public String getRuntimeSourceFileName() {
    File dir = this.layout.getMapperRuntimeDir(this.fragmentPackage);
    File source = new File(dir, this.getSourceFileName());
    return source.getPath();
  }

  public static String assembleSourceFileName(final DataSetLayout layout, final ClassPackage fragmentPackage,
      final ObjectId id) {
    File dir = layout.getMapperRuntimeDir(fragmentPackage);
    String sourceFile = "primitives-" + id.getDashedName() + ".xml";
    File source = new File(dir, sourceFile);
    return source.getPath();
  }

  public String getSourceFileName() {
    ObjectId id = this.metadata.getId();
    return getSourceFile(id);
  }

  public static String getSourceFile(final ObjectId id) {
    return id.getDashedName() + ".xml";
  }

  public String getMapperIdSelectByPK() {
    return "selectByPK"
    // + this.tid.getMapperIdentifier()
    ;
  }

  public String getFullMapperIdSelectSequence(final SequenceMethodTag s) {
    return this.namespace + "." + getMapperSelectSequence(s);
  }

  public String getMapperSelectSequence(final SequenceMethodTag s) {
    return "selectSequence" + s.getId().getJavaClassName();
  }

  public String getFullMapperIdSelectByPK() {
    return this.namespace + "." + getMapperIdSelectByPK();
  }

  public String getMapperIdSelectByUI(final KeyMetadata ui) {
    return "selectByUI" + ui.toCamelCase(this.layout.getColumnSeam());
  }

  public String getFullMapperIdSelectByUI(final KeyMetadata ui) {
    return this.namespace + "." + getMapperIdSelectByUI(ui);
  }

  public String getMapperIdSelectByExample() {
    return "selectByExample";
  }

  public String getFullMapperIdSelectByExample() {
    return this.namespace + "." + getMapperIdSelectByExample();
  }

  public String getMapperIdSelectByCriteria() {
    return "selectByCriteria";
  }

  public String getFullMapperIdSelectByCriteria() {
    return this.namespace + "." + getMapperIdSelectByCriteria();
  }

  public String getMapperIdDeleteByCriteria() {
    return "deleteByCriteria";
  }

  public String getFullMapperIdDeleteByCriteria() {
    return this.namespace + "." + getMapperIdDeleteByCriteria();
  }

  public String getMapperIdUpdateByCriteria() {
    return "updateByCriteria";
  }

  public String getFullMapperIdUpdateByCriteria() {
    return this.namespace + "." + getMapperIdUpdateByCriteria();
  }

  public String getMapperIdSelectParameterized() {
    return "selectParameterized";
  }

  public String getFullMapperIdSelectParameterized() {
    return this.namespace + "." + getMapperIdSelectParameterized();
  }

  // Insert

  public String getMapperIdInsert() {
    return "insert";
  }

  public String getFullMapperIdInsert() {
    return this.namespace + "." + getMapperIdInsert();
  }

  public String getMapperIdInsertRetrievingDefaults() {
    return "insertRetrievingDefaults";
  }

  public String getFullMapperIdInsertRetrievingDefaults() {
    return this.namespace + "." + getMapperIdInsertRetrievingDefaults();
  }

  public String getMapperIdSequencesPreFetch() {
    return "sequencesPreFetch";
  }

  public String getFullMapperIdSequencesPreFetch() {
    return this.namespace + "." + getMapperIdSequencesPreFetch();
  }

  public String getMapperIdIdentitiesPostFetch() {
    return "identityPostFetch";
  }

  public String getFullMapperIdIdentitiesPostFetch() {
    return this.namespace + "." + getMapperIdIdentitiesPostFetch();
  }

  // Insert by Example

  public String getMapperIdInsertByExample() {
    return "insertByExample";
  }

  public String getFullMapperIdInsertByExample() {
    return this.namespace + "." + getMapperIdInsertByExample();
  }

  // Update

  public String getMapperIdUpdateByPK() {
    return "updateByPK";
  }

  public String getFullMapperIdUpdateByPK() {
    return this.namespace + "." + getMapperIdUpdateByPK();
  }

  public String getMapperIdUpdateByExample() {
    return "updateByExample";
  }

  public String getFullMapperIdUpdateByExample() {
    return this.namespace + "." + getMapperIdUpdateByExample();
  }

  public String getMapperIdDeleteByPK() {
    return "deleteByPK";
  }

  public String getFullMapperIdDeleteByPK() {
    return this.namespace + "." + getMapperIdDeleteByPK();
  }

  public String getMapperIdDeleteByExample() {
    return "deleteByExample";
  }

  public String getFullMapperIdDeleteByExample() {
    return this.namespace + "." + getMapperIdDeleteByExample();
  }

  public String getNamespace() {
    return this.namespace;
  }

  public String getFullMapperIdUpdate(final QueryMethodTag u) {
    return this.namespace + "." + getMapperSelectSequence(u);
  }

  public String getMapperSelectSequence(final QueryMethodTag u) {
    return u.getId().getJavaMemberName();
  }

  public String getSelectMethodStatementId(final SelectMethodMetadata sm) {
    return "select_" + sm.getMethod();
  }

  public String getFullSelectMethodStatementId(final SelectMethodMetadata sm) {
    return this.namespace + "." + this.getSelectMethodStatementId(sm);
  }

  private String getResultMapName(final SelectMethodMetadata sm) {
    return "result_map_select_" + sm.getMethod();
  }

  // Helpers

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
