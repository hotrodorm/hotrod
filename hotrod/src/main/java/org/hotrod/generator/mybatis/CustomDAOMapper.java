package org.hotrod.generator.mybatis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.DAOMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.SelectMethodMetadata.SelectMethodReturnType;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.metadata.StructuredColumnsMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.Identifier;

public class CustomDAOMapper {

  private static final Logger log = Logger.getLogger(CustomDAOMapper.class);

  private DAOMetadata dm;
  private Identifier identifier;
  private DataSetLayout layout;
  private EntityDAORegistry entityDAORegistry;

  private MyBatisGenerator generator;

  private ClassPackage fragmentPackage;

  private String namespace;

  private CustomDAO dao;

  private Writer w;

  public CustomDAOMapper(final DAOMetadata dm, final DataSetLayout layout, final MyBatisGenerator generator,
      final EntityDAORegistry entityDAORegistry) {
    this.dm = dm;
    this.identifier = new DataSetIdentifier("fake_sql_name", this.dm.getJavaClassName());

    this.layout = layout;
    this.generator = generator;
    this.entityDAORegistry = entityDAORegistry;

    HotRodFragmentConfigTag fragmentConfig = this.dm.getFragmentConfig();
    this.fragmentPackage = fragmentConfig != null && fragmentConfig.getFragmentPackage() != null
        ? fragmentConfig.getFragmentPackage() : null;

    this.namespace = this.layout.getDAOPrimitivePackage(this.fragmentPackage).getPackage() + "."
        + this.identifier.getJavaMemberIdentifier();
  }

  public void generate() throws UncontrolledException, ControlledException {
    String sourceFileName = this.getSourceFileName();
    File mapper = new File(this.layout.getMapperPrimitiveDir(this.fragmentPackage), sourceFileName);
    this.w = null;

    try {
      this.w = new BufferedWriter(new FileWriter(mapper));

      writeHeader();

      for (SequenceMethodTag s : this.dm.getSequences()) {
        try {
          writeSelectSequence(s);
        } catch (SequencesNotSupportedException e) {
          throw new ControlledException(
              "Could not generate mapper for sequence '" + s.getName() + "' onto file: " + e.getMessage());
        }
      }

      for (QueryMethodTag q : this.dm.getQueries()) {
        try {
          writeQuery(q);
        } catch (SequencesNotSupportedException e) {
          throw new ControlledException(
              "Could not generate mapper for query '" + q.getJavaMethodName() + "' onto file: " + e.getMessage());
        }
      }

      for (SelectMethodMetadata sm : this.dm.getSelectsMetadata()) {
        // TODO: <dao> tags do not yet implement <select> correctly.
        // writeSelectMethod(sm);
      }

      writeFooter();

    } catch (IOException e) {
      throw new UncontrolledException(
          "Could not generate mapper file: could not write to file '" + mapper.getName() + "'.", e);
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

  // private void beginCData() throws IOException {
  // println(" <![CDATA[");
  // }
  //
  // private void endCData() throws IOException {
  // println(" ]]>");
  // }

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
    println("  <!-- select sequence " + seq.getName() + " -->");
    println();
    println("  <select id=\"" + this.getMapperSelectSequence(seq) + "\" " + "resultType=\"java.lang.Long\">");

    String sentence = this.generator.getAdapter().renderSelectSequence(seq.getIdentifier());
    println("    " + sentence);
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
    println("  <!-- query " + u.getJavaMethodName() + " -->");
    println();

    println("  <update id=\"" + u.getIdentifier().getJavaMemberIdentifier() + "\">");

    // this.beginCData();
    String sentence = u.renderSQLSentence(new MyBatisParameterRenderer());
    println("    " + sentence);
    // this.endCData();
    println("  </update>");
    println();

  }

  private void writeSelectMethod(final SelectMethodMetadata sm) throws IOException {
    println("  <!-- select method: " + sm.getMethod() + " -->");
    println();

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
    println(sm.renderXML(new MyBatisParameterRenderer()));
    println("  </select>");
    println();
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

    String indent = SUtils.getFiller(' ', 4 + (level * 2));

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
      log.debug("converter=" + cm.getConverter().getName() + " cm=" + cm.getColumnName());
      typeHandler = "typeHandler=\"" + dao.getTypeHandlerFullClassName(sm, cm) + "\" ";
    } else {
      EnumDataSetMetadata ds = cm.getEnumMetadata();
      EnumClass ec = this.generator.getEnum(ds);
      if (ec != null) {
        typeHandler = "typeHandler=\"" + dao.getTypeHandlerFullClassName(cm) + "\" ";
      }
    }

    String indent = SUtils.getFiller(' ', 4 + (level * 2));
    println(indent + "<" + tagName + " property=\"" + cm.getIdentifier().getJavaMemberIdentifier() + "\" column=\""
        + SUtils.escapeXmlAttribute(cm.getColumnAlias()) + "\" " + typeHandler + "/>");

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

    String indent = SUtils.getFiller(' ', 4);
    println(indent + "<" + tagName + " property=\"" + cm.getIdentifier().getJavaMemberIdentifier() + "\" column=\""
        + SUtils.escapeXmlAttribute(cm.getColumnName()) + "\" " + typeHandler + "/>");

  }

  private void writeFooter() throws IOException {
    println("</mapper>");
  }

  // Info

  public String getSourceFileName() {
    return "primitives-" + this.identifier.getMapperFileIdentifier() + ".xml";
  }

  public String getFullMapperIdSelectSequence(final SequenceMethodTag s) {
    return this.namespace + "." + getMapperSelectSequence(s);
  }

  public String getFullMapperIdQuery(final QueryMethodTag u) {
    return this.namespace + "." + getMapperSelectSequence(u);
  }

  public String getMapperSelectSequence(final SequenceMethodTag s) {
    return "selectSequence" + s.getIdentifier().getJavaClassIdentifier();
  }

  public String getMapperSelectSequence(final QueryMethodTag u) {
    return u.getIdentifier().getJavaMemberIdentifier();
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

  @SuppressWarnings("unused")
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

  // Setters

  public void setDao(final CustomDAO dao) {
    this.dao = dao;
  }

}
