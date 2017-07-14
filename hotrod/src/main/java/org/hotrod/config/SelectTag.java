package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.config.dynamicsql.ComplementTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.config.dynamicsql.LiteralSQLPart;
import org.hotrod.config.sql.SQLSection;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;

@XmlRootElement(name = "select")
public class SelectTag extends AbstractSQLDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(SelectTag.class);

  // Properties

  protected String javaClassName = null;

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = ColumnTag.class), //
      @XmlElementRef(type = ComplementTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  // Properties - Parsed

  protected List<ColumnTag> columns = new ArrayList<ColumnTag>();
  protected List<DynamicSQLPart> parts = new ArrayList<DynamicSQLPart>();

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

    // java-class-name

    if (SUtils.isEmpty(this.javaClassName)) {
      throw new InvalidConfigurationFileException("Attribute 'java-class-name' of tag <" + getTagName()
          + "> cannot be empty. " + "Must specify a unique query name, " + "different from table and view names.");
    }

    // content text, columns, complement

    this.columns = new ArrayList<ColumnTag>();
    this.parts = new ArrayList<DynamicSQLPart>();
    for (Object obj : this.content) {
      try {
        String s = (String) obj;
        this.parts.add(new LiteralSQLPart(s));
      } catch (ClassCastException e1) {
        try {
          ColumnTag col = (ColumnTag) obj;
          this.columns.add(col);
        } catch (ClassCastException e2) {
          try {
            ComplementTag p = (ComplementTag) obj;
            this.parts.add(p);
          } catch (ClassCastException e3) {
            throw new InvalidConfigurationFileException(
                "The body of the tag <" + super.getTagName() + "> with " + "java-class-name '" + this.javaClassName
                    + "' has an invalid tag (of class '" + obj.getClass().getName() + "').");
          }
        }
      }
    }

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

    // content text and complement

    for (DynamicSQLPart p : this.parts) {
      p.validate("<select> with java-class-name '" + this.javaClassName + "'");
    }

    // all validations cleared

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
