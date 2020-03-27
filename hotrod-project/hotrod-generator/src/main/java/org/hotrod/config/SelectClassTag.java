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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.dynamicsql.LiteralTextPart;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtils;

@XmlRootElement(name = "select")
public class SelectClassTag extends AbstractDAOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(SelectClassTag.class);

  // Properties

  protected String javaClassName = null;

  // Properties - Primitive content parsing by JAXB

  // This property cannot be transient. JAXB fails when doing so with the
  // message: Transient field "content" cannot have any JAXB annotations.

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = ParameterTag.class), //
      @XmlElementRef(type = ColumnTag.class), //
      @XmlElementRef(type = ComplementTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  // Properties - Parsed

  protected ParameterDefinitions parameterDefinitions = null;
  protected List<ColumnTag> columns = null;
  protected List<DynamicSQLPart> parts = null;
  private List<LiteralTextPart> foundationParts = null;

  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  private DaosTag daosTag;

  // Constructor

  public SelectClassTag() {
    super("select");
  }

  // JAXB Setters

  @XmlAttribute(name = "java-class-name")
  public void setJavaClassName(final String javaClassName) {
    this.javaClassName = javaClassName;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    // java-class-name

    if (SUtils.isEmpty(this.javaClassName)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'java-class-name' cannot be empty", //
          "Attribute 'java-class-name' of tag <" + getTagName() + "> cannot be empty. "
              + "Must specify a unique query name, " + "different from table and view names.");
    }

    // content text, parameters, columns, complement

    this.parameterDefinitions = new ParameterDefinitions();
    this.columns = new ArrayList<ColumnTag>();
    this.parts = new ArrayList<DynamicSQLPart>();

    this.foundationParts = new ArrayList<LiteralTextPart>();

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content text
        LiteralTextPart p = new LiteralTextPart(super.getSourceLocation(), s);
        this.parts.add(p);
        this.foundationParts.add(p);
      } catch (ClassCastException e1) {
        try {
          ParameterTag param = (ParameterTag) obj; // parameter
          param.validate();
          this.parameterDefinitions.add(param);
        } catch (ClassCastException e2) {
          try {
            ColumnTag col = (ColumnTag) obj; // column
            this.columns.add(col);
          } catch (ClassCastException e3) {
            try {
              ComplementDAOTag p = (ComplementDAOTag) obj; // complement
              this.parts.add(p);
            } catch (ClassCastException e4) {
              throw new InvalidConfigurationFileException(this, //
                  "The body of the tag <" + super.getTagName() + "> includes an invalid tag: "
                      + obj.getClass().getName() + "", //
                  "The body of the tag <" + super.getTagName() + "> includes an invalid tag (of class '"
                      + obj.getClass().getName() + "').");
            }
          }
        }
      }
    }

    // columns

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(config, adapter);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException(this, //
            "Multiple <" + new ColumnTag().getTagName() + "> tags for the same column '" + c.getName() + "'", //
            "Multiple <" + new ColumnTag().getTagName() + "> tags with the same name on tag <" + getTagName()
                + "> for query '" + this.javaClassName + "'. You cannot specify the same column name "
                + "multiple times on the same query.");
      }
      cols.add(c);
    }

    // content text and complement

    for (DynamicSQLPart p : this.parts) {
      p.validate(daosTag, config, fragmentConfig, this.parameterDefinitions);
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
    for (LiteralTextPart tp : this.foundationParts) {
      sb.append(tp.renderStatic(null));
    }
    String literal = sb.toString();
    return literal;
  }

  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      String st = p.renderStatic(parameterRenderer);
      sb.append(st);
    }
    return sb.toString();
  }

  public String renderXML(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      sb.append(p.renderXML(parameterRenderer));
    }
    return sb.toString();
  }

  // public String renderJavaExpression(final int margin, final
  // ParameterRenderer parameterRenderer)
  // throws InvalidJavaExpressionException {
  // // return this.aggregatedPart.renderJavaExpression(margin,
  // // parameterRenderer);
  // }

  public List<ParameterTag> getParameterDefinitions() {
    return this.parameterDefinitions.getDefinitions();
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      SelectClassTag f = (SelectClassTag) fresh;
      return this.javaClassName.equals(f.javaClassName);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      SelectClassTag f = (SelectClassTag) fresh;
      boolean different = !same(fresh);

      this.parameterDefinitions = f.parameterDefinitions;
      this.columns = f.columns;
      this.parts = f.parts;
      this.foundationParts = f.foundationParts;
      this.fragmentConfig = f.fragmentConfig;
      this.fragmentPackage = f.fragmentPackage;
      this.daosTag = f.daosTag;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      SelectClassTag f = (SelectClassTag) fresh;
      return //
      Compare.same(this.javaClassName, f.javaClassName) && //
          Compare.same(this.parameterDefinitions, f.parameterDefinitions) && //
          Compare.same(this.columns, f.columns) && //
          Compare.same(this.parts, f.parts) && //
          Compare.same(this.foundationParts, f.foundationParts);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.javaClassName;
  }

}
