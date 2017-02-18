package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtils;

public class SelectTag extends AbstractSQLDAOTag {

  private static final Logger log = Logger.getLogger(SelectTag.class);

  public static final String TAG_NAME = "select";

  protected String javaClassName = null;
  protected List<ColumnTag> columns = new ArrayList<ColumnTag>();

  private DaosTag daosTag;

  public void validate(final DaosTag daosTag) throws InvalidConfigurationFileException {

    this.daosTag = daosTag;

    // name

    if (SUtils.isEmpty(this.javaClassName)) {
      throw new InvalidConfigurationFileException("Attribute 'java-class-name' of tag <" + getTagName()
          + "> cannot be empty. " + "Must specify a unique query name, " + "different from table and view names.");
    }

    super.validateCore("java-class-name", this.javaClassName);

    // columns

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(this.javaClassName);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException("Multiple <" + ColumnTag.TAG_NAME
            + "> tags with the same name on tag <" + getTagName() + "> for query '" + this.javaClassName
            + "'. You cannot specify the same column name " + "multiple times on the same query.");
      }
      cols.add(c);
    }

    this.declaredMethodNames.add("select");

    log.debug("columns=" + this.columns.size() + " SQL=" + super.body);
  }

  @Override
  protected String getTagName() {
    return TAG_NAME;
  }

  // Setters

  public void setJavaClassName(String javaClassName) {
    this.javaClassName = javaClassName;
  }

  // Getters

  // Columns

  public ColumnTag findColumnTag(final String columnName, final DatabaseAdapter adapter) {
    for (ColumnTag ct : this.columns) {
      if (ct.isName(columnName, adapter)) {
        return ct;
      }
    }
    return null;
  }

  public void addColumn(final ColumnTag c) {
    this.columns.add(c);
  }

  // Rendering

  public List<ColumnTag> getColumns() {
    return columns;
  }

  public String getSQLFoundation(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLSection s : this.sections) {
      String b = s.getSQLFoundation(parameterRenderer);
      if (b != null) {
        sb.append(unescapeXml(b));
      }
    }
    return sb.toString();
  }

  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLSection s : this.sections) {
      sb.append(s.renderSQLSentence(parameterRenderer));
    }
    return sb.toString();
  }

  // DAO Tag implementation

  @Override
  public ClassPackage getPackage() {
    return this.daosTag.getDaoPackage();
  }

  @Override
  public String getJavaClassName() {
    return this.javaClassName;
  }

}
