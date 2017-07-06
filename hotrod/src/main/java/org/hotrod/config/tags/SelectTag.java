package org.hotrod.config.tags;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractSQLDAOTag;
import org.hotrod.config.sql.AbstractSQLSection;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtils;

@XmlRootElement(name = "select")
public class SelectTag extends AbstractSQLDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(SelectTag.class);

  // Properties

  protected String javaClassName = null;
  protected List<ColumnTag> columns = new ArrayList<ColumnTag>();
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private DaosTag daosTag;

  // Constructor

  public SelectTag() {
    super("select");
  }

  // JAXB Setters

  @XmlAttribute(name = "java-class-name")
  public void setJavaClassName(final String javaClassName) {
    this.javaClassName = javaClassName;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    // name

    if (SUtils.isEmpty(this.javaClassName)) {
      throw new InvalidConfigurationFileException("Attribute 'java-class-name' of tag <" + getTagName()
          + "> cannot be empty. " + "Must specify a unique query name, " + "different from table and view names.");
    }

    super.validateCore("java-class-name", this.javaClassName);

    // columns

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(super.getTagName(), this.javaClassName, config);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException("Multiple <" + new ColumnTag().getTagName()
            + "> tags with the same name on tag <" + getTagName() + "> for query '" + this.javaClassName
            + "'. You cannot specify the same column name " + "multiple times on the same query.");
      }
      cols.add(c);
    }

    this.declaredMethodNames.add("select");

    log.debug("columns=" + this.columns.size());
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
    for (AbstractSQLSection s : this.sections) {
      String b = s.getSQLFoundation(parameterRenderer);
      if (b != null) {
        sb.append(unescapeXml(b));
      }
    }
    return sb.toString();
  }

  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (AbstractSQLSection s : this.sections) {
      sb.append(s.renderSQLSentence(parameterRenderer));
    }
    return sb.toString();
  }

  // DAO Tag implementation

  @Override
  public ClassPackage getPackage() {
    return this.daosTag.getDaoPackage(this.fragmentPackage);
  }

  @Override
  public String getJavaClassName() {
    return this.javaClassName;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

}
