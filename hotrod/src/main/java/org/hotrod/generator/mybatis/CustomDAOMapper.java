package org.hotrod.generator.mybatis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.tags.CustomDAOTag;
import org.hotrod.config.tags.HotRodFragmentConfigTag;
import org.hotrod.config.tags.QueryTag;
import org.hotrod.config.tags.SequenceTag;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.Identifier;

public class CustomDAOMapper {

  private CustomDAOTag tag;
  private Identifier identifier;

  private DataSetLayout layout;

  private HotRodGenerator generator;

  private ClassPackage fragmentPackage;

  private String namespace;

  @SuppressWarnings("unused")
  private CustomDAO dao;

  private Writer w;

  public CustomDAOMapper(final CustomDAOTag tag, final DataSetLayout layout, final HotRodGenerator generator) {
    this.tag = tag;
    this.identifier = new DataSetIdentifier("fake_sql_name", tag.getJavaClassName());

    this.layout = layout;
    this.generator = generator;

    HotRodFragmentConfigTag fragmentConfig = tag.getFragmentConfig();
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

      for (SequenceTag s : this.tag.getSequences()) {
        try {
          writeSelectSequence(s);
        } catch (SequencesNotSupportedException e) {
          throw new ControlledException(
              "Could not generate mapper for sequence '" + s.getName() + "' onto file: " + e.getMessage());
        }
      }

      for (QueryTag q : this.tag.getQueries()) {
        try {
          writeQuery(q);
        } catch (SequencesNotSupportedException e) {
          throw new ControlledException(
              "Could not generate mapper for query '" + q.getJavaMethodName() + "' onto file: " + e.getMessage());
        }
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

//  private void beginCData() throws IOException {
//    println("   <![CDATA[");
//  }
//
//  private void endCData() throws IOException {
//    println("   ]]>");
//  }

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

  private void writeSelectSequence(final SequenceTag seq) throws IOException, SequencesNotSupportedException {
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

  private void writeQuery(final QueryTag u) throws IOException, SequencesNotSupportedException {
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

  private void writeFooter() throws IOException {
    println("</mapper>");
  }

  // Info

  public String getSourceFileName() {
    return "primitives-" + this.identifier.getMapperFileIdentifier() + ".xml";
  }

  public String getFullMapperIdSelectSequence(final SequenceTag s) {
    return this.namespace + "." + getMapperSelectSequence(s);
  }

  public String getFullMapperIdQuery(final QueryTag u) {
    return this.namespace + "." + getMapperSelectSequence(u);
  }

  public String getMapperSelectSequence(final SequenceTag s) {
    return "selectSequence" + s.getIdentifier().getJavaClassIdentifier();
  }

  public String getMapperSelectSequence(final QueryTag u) {
    return u.getIdentifier().getJavaMemberIdentifier();
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
