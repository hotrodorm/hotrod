package org.hotrod.config.sql;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.config.AbstractSQLDAOTag;
import org.hotrod.config.tags.SelectTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

/**
 * <pre>
 * 
 * SQLSection
 *  +- SQLFoundationSection
 *       List<SQLChunk>
 *       List<SQLParameter>
 *  +- SQLComplementSection {    
 *       List<SQLChunk>
 *       List<SQLParameter>
 *     }
 * 
 * SQLChunk
 *  +- SQLLiteralChunk
 *  +- SQLParameter
 * 
 * </pre>
 */
public abstract class AbstractSQLSection {

  private static final String PREFIX = "#{";
  private static final String SUFFIX = "}";

  protected List<SQLChunk> chunks = new ArrayList<SQLChunk>();
  protected List<SQLParameter> params = new ArrayList<SQLParameter>();

  public AbstractSQLSection(final String txt, final AbstractSQLDAOTag tag, final String attName, final String name)
      throws InvalidConfigurationFileException {
    int pos = 0;
    int prefix;
    int suffix;
    boolean inTag = false;

    while (pos < txt.length() && (prefix = txt.indexOf(PREFIX, pos)) != -1) {

      if (prefix == -1) {
        throw new InvalidConfigurationFileException(getErrorMessage(txt, tag, attName, name));
      }
      String leadChunk = txt.substring(pos, prefix);
      inTag = decideIfInTag(inTag, leadChunk);

      SQLLiteralChunk l = new SQLLiteralChunk(leadChunk);
      this.chunks.add(l);

      suffix = txt.indexOf(SUFFIX, prefix + PREFIX.length());
      if (suffix == -1) {
        throw new InvalidConfigurationFileException(getErrorMessage(txt, tag, attName, name));
      }

      SQLParameter p = new SQLParameter(txt.substring(prefix + PREFIX.length(), suffix), tag, this, inTag, attName,
          name);
      this.chunks.add(p);
      this.params.add(p);

      pos = suffix + SUFFIX.length();
    }

    if (pos < txt.length()) {
      SQLLiteralChunk l = new SQLLiteralChunk(txt.substring(pos));
      this.chunks.add(l);
    }
  }

  private boolean decideIfInTag(final boolean previousInTag, final String leadChunk) {
    int lastLt = leadChunk.lastIndexOf('<');
    int lastGt = leadChunk.lastIndexOf('>');
    boolean inTag;
    if (lastLt == -1) {
      inTag = lastGt == -1 ? previousInTag : false;
    } else {
      inTag = lastGt == -1 ? true : (lastLt > lastGt);
    }
    return inTag;
  }

  String getErrorMessage(final String txt, final AbstractSQLDAOTag tag, final String attName, final String name) {
    return getErrorMessage(txt, tag, attName, name, null);
  }

  String getErrorMessage(final String txt, final AbstractSQLDAOTag tag, final String attName, final String name,
      final String extraMessage) {
    return "Invalid select SQL query as the body of the tag <" + new SelectTag().getTagName()+ "> with " + attName + " '" + name
        + "': invalid parameter section '" + txt + "'. Must containg one or more parameters with the form: " + PREFIX
        + "name,javaType=<JAVA-CLASS>,jdbcType=<JDBC-TYPE>" + SUFFIX
        + (extraMessage == null ? "" : " ; " + extraMessage);
  }

  // Getters

  public List<SQLParameter> getParameterOccurrences() {
    return this.params;
  }

  public List<SQLParameter> getParameterDefinitions() {
    List<SQLParameter> defs = new ArrayList<SQLParameter>();
    for (SQLParameter p : this.params) {
      if (p.isDefinition()) {
        defs.add(p);
      }
    }
    return defs;
  }

  public List<SQLChunk> getChunks() {
    return chunks;
  }

  public final String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder(" ");
    for (SQLChunk c : this.chunks) {
      sb.append(c.renderSQLSentence(parameterRenderer));
    }
    sb.append(" ");
    return sb.toString();
  }

  protected final String renderChunksSQL() {
    StringBuilder sb = new StringBuilder();
    for (SQLChunk c : this.chunks) {
      sb.append(c.renderAugmentedSQL());
    }
    sb.append(" ");
    return sb.toString();
  }

  // Abstract methods

  public abstract String getSQLFoundation(ParameterRenderer parameterRenderer);

  public abstract String renderAugmentedSQL();

}
