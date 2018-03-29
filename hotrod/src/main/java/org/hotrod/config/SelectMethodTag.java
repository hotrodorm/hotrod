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
import org.hotrod.config.EnhancedSQLPart.SQLFormatter;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.config.structuredcolumns.ColumnsTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "select")
public class SelectMethodTag extends AbstractMethodTag {

  // Constants

  private static final Logger log = Logger.getLogger(SelectMethodTag.class);

  private static final String MULTIPLE_ROWS_FALSE = "false";
  private static final String MULTIPLE_ROWS_TRUE = "true";
  private static final boolean MULTIPLE_ROWS_DEFAULT = true;

  // Properties

  private String method = null;
  private String vo = null;
  private String sMultipleRows = null;

  private SelectMethodMetadata metadata = null;

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = ParameterTag.class), //
      @XmlElementRef(type = ColumnTag.class), //
      @XmlElementRef(type = ComplementTag.class), //
      @XmlElementRef(type = ColumnsTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  // Properties - Parsed

  private boolean multipleRows;
  protected ParameterDefinitions parameters = null;
  protected List<ColumnTag> columns = null;
  protected List<EnhancedSQLPart> parts = null;
  private EnhancedSQLPart aggregatedPart = null;
  private ColumnsTag structuredColumns = null;

  private HotRodFragmentConfigTag fragmentConfig = null;

  // Constructor

  public SelectMethodTag() {
    super("select");
  }

  // JAXB Setters

  @XmlAttribute
  public void setMethod(final String method) {
    this.method = method;
  }

  @XmlAttribute(name = "vo")
  public void setVO(final String vo) {
    this.vo = vo;
  }

  @XmlAttribute(name = "multiple-rows")
  public void setHasMultipleRows(final String mr) {
    this.sMultipleRows = mr;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    this.fragmentConfig = fragmentConfig;

    // Sort: content parts, columns, parameters, complements

    this.parts = new ArrayList<EnhancedSQLPart>();

    this.columns = new ArrayList<ColumnTag>();
    this.parameters = new ParameterDefinitions();

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        VerbatimTextPart p = new VerbatimTextPart(s);
        this.parts.add(p);
      } catch (ClassCastException e1) {
        try {
          ParameterTag param = (ParameterTag) obj; // parameter
          this.parameters.add(param);
        } catch (ClassCastException e2) {
          try {
            ColumnTag col = (ColumnTag) obj; // column
            this.columns.add(col);
          } catch (ClassCastException e3) {
            try {
              ComplementTag p = (ComplementTag) obj; // complement
              this.parts.add(p);
            } catch (ClassCastException e4) {
              try {
                ColumnsTag p = (ColumnsTag) obj; // columns
                this.structuredColumns = p;
                this.parts.add(p);
              } catch (ClassCastException e5) {
                throw new InvalidConfigurationFileException(super.getSourceLocation(), "The body of the tag <"
                    + super.getTagName() + "> has an invalid tag (of class '" + obj.getClass().getName() + "').");
              }
            }
          }
        }
      }
    }

    if (this.parts.size() != 1) {
      this.aggregatedPart = new SequenceOfParts(this.parts);
    } else {
      this.aggregatedPart = this.parts.get(0);
    }

    // method

    if (SUtils.isEmpty(this.method)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'method' of tag <"
          + getTagName() + "> cannot be empty. " + "A unique Java method name was expected for the DAO class.");
    }
    if (!this.method.matches(Patterns.VALID_JAVA_METHOD)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid method name '" + this.method + "' on tag <" + super.getTagName()
              + ">. A Java method name must start with a lower case letter, "
              + "and continue with letters, digits, and/or underscores.");
    }

    // vo

    if (this.vo == null) {
      if (this.structuredColumns == null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Missing 'vo' attribute in the <" + this.getTagName()
                + "> tag. The 'vo' attribute must be specified when no inner <columns> tag is present.");
      }
    } else {
      if (this.structuredColumns != null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid 'vo' attribute. "
            + "When the 'vo' attribute is specified no inner <columns> tag can be used. Use one or the other but not both.");
      }
      if (SUtils.isEmpty(this.vo)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "When specified, the 'vo' attribute cannot be empty.");
      }
      if (!this.vo.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid 'vo' attribute with value '" + this.vo + "' in the tag <" + super.getTagName()
                + ">. A Java class name must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // multiple-rows

    if (this.sMultipleRows == null) {
      this.multipleRows = MULTIPLE_ROWS_DEFAULT;
    } else {
      if (this.sMultipleRows.equals(MULTIPLE_ROWS_TRUE)) {
        this.multipleRows = true;
      } else if (this.sMultipleRows.equals(MULTIPLE_ROWS_FALSE)) {
        this.multipleRows = false;
      } else {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid 'multiple-rows' attribute with value '" + this.sMultipleRows
                + "'. When specified, the 'multiple-rows' attribute must either be 'true' or 'false'.");
      }
    }

    // <column> tags

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(config);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Multiple <" + new ColumnTag().getTagName() + "> tags for the same column '" + c.getName() + "' on tag <"
                + getTagName() + ">.");
      }
      cols.add(c);
    }

    // Literal SQL, <columns>, <complement> tags

    for (EnhancedSQLPart p : this.parts) {
      // log.info("VAL p: " + p.getClass().getName());
      p.validate(daosTag, config, fragmentConfig, this.parameters);
    }

    // all validations cleared

    log.debug("columns=" + this.columns.size());
  }

  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {
    for (EnhancedSQLPart p : this.parts) {
      p.validateAgainstDatabase(generator);
    }
  }

  @Deprecated
  public void setDataSetMetadata(final SelectMethodMetadata dataSetMetadata) {
    this.metadata = dataSetMetadata;
  }

  // Getters

  public String getMethod() {
    return method;
  }

  public String getVO() {
    return vo;
  }

  public boolean isMultipleRows() {
    return multipleRows;
  }

  public ParameterDefinitions getParameters() {
    return parameters;
  }

  public List<EnhancedSQLPart> getParts() {
    return parts;
  }

  public ColumnsTag getStructuredColumns() {
    return structuredColumns;
  }

  @Deprecated
  public SelectMethodMetadata getDataSetMetadata() {
    return metadata;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

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

  public String renderSQLAngle(final ParameterRenderer parameterRenderer, final ColumnsProvider cp,
      final DatabaseAdapter adapter) {
    StringBuilder sb = new StringBuilder();
    for (EnhancedSQLPart p : this.parts) {
      sb.append(p.renderSQLAngle(adapter, cp));
    }
    String literal = sb.toString();
    SQLFormatter formatter = new SQLFormatter();
    formatter.add(literal);
    return formatter.toString();
  }

  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (EnhancedSQLPart p : this.parts) {
      String st = p.renderStatic(parameterRenderer);
      sb.append(st);
    }
    return sb.toString();
  }

  public void renderXML(final SQLFormatter formatter, final ParameterRenderer parameterRenderer) {
    for (EnhancedSQLPart p : this.parts) {
      p.renderXML(formatter, parameterRenderer);
    }
  }

  public String renderJavaExpression(final int margin, final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    return this.aggregatedPart.renderJavaExpression(margin, parameterRenderer);
  }

  public List<ParameterTag> getParameterDefinitions() {
    return this.parameters.getDefinitions();
  }

  public Set<TableDataSetMetadata> getReferencedEntities() {
    if (this.structuredColumns == null) {
      return new HashSet<TableDataSetMetadata>();
    } else {
      return this.structuredColumns.getReferencedEntities();
    }
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      SelectMethodTag f = (SelectMethodTag) fresh;
      return this.method.equals(f.method);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      SelectMethodTag f = (SelectMethodTag) fresh;
      boolean different = !same(fresh);

      this.vo = f.vo;
      this.sMultipleRows = f.sMultipleRows;
      this.metadata = f.metadata;
      this.multipleRows = f.multipleRows;
      this.parameters = f.parameters;
      this.columns = f.columns;
      this.parts = f.parts;
      this.aggregatedPart = f.aggregatedPart;
      this.structuredColumns = f.structuredColumns;
      this.fragmentConfig = f.fragmentConfig;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      SelectMethodTag f = (SelectMethodTag) fresh;
      return //
      Compare.same(this.method, f.method) && //
          Compare.same(this.vo, f.vo) && //
          Compare.same(this.sMultipleRows, f.sMultipleRows) && //
          Compare.same(this.parameters, f.parameters) && //
          Compare.same(this.columns, f.columns) && //
          Compare.same(this.parts, f.parts) && //
          Compare.same(this.aggregatedPart, f.aggregatedPart) && //
          Compare.same(this.structuredColumns, f.structuredColumns) && //
          Compare.same(this.fragmentConfig, f.fragmentConfig);
    } catch (ClassCastException e) {
      return false;
    }
  }

}
