package org.hotrod.config;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.hotrod.config.EnhancedSQLPart.SQLFormatter;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.config.structuredcolumns.ColumnsTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.lang.collector.listcollector.ListCollector;

@XmlRootElement(name = "select")
public class SelectMethodTag extends AbstractMethodTag<SelectMethodTag> {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(SelectMethodTag.class);

  public enum ResultSetMode {

    LIST("list"), //
    CURSOR("cursor"), //
    SINGLE_ROW("single-row");

    private String caption;

    private ResultSetMode(final String caption) {
      this.caption = caption;
    }

    public String getCaption() {
      return caption;
    }

    public static ResultSetMode parse(final String s) {
      for (ResultSetMode m : ResultSetMode.values()) {
        if (m.caption.equals(s)) {
          return m;
        }
      }
      return null;
    }

  }

  // Properties

  private String vo = null;
  private String sMode = null;

  private SelectMethodMetadata metadata = null;

  // Properties - Primitive content parsing by JAXB

  // This property cannot be transient. JAXB fails when doing so with the
  // message: Transient field "content" cannot have any JAXB annotations.

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = ParameterTag.class), //
      @XmlElementRef(type = ColumnTag.class), //
      @XmlElementRef(type = ComplementTag.class), //
      @XmlElementRef(type = ColumnsTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  // Properties - Parsed

  private String voClassName;
  private String abstractVoClassName;

  private ResultSetMode mode;
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

  // Duplicate

  @Override
  public SelectMethodTag duplicate() {
    SelectMethodTag d = new SelectMethodTag();

    d.copyCommon(this);

    d.vo = this.vo;
    d.sMode = this.sMode;
    d.metadata = this.metadata;

    d.content = this.content;

    d.voClassName = this.voClassName;
    d.mode = this.mode;
    d.parameters = this.parameters;
    d.columns = this.columns;
    d.parts = this.parts;
    d.aggregatedPart = this.aggregatedPart;
    d.structuredColumns = this.structuredColumns;

    d.fragmentConfig = this.fragmentConfig;

    return d;
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

  @XmlAttribute(name = "mode")
  public void setMode(final String m) {
    this.sMode = m;
  }

  // Behavior

  public void validate(final DaosSpringMyBatisTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    this.fragmentConfig = fragmentConfig;

    // Sort: content parts, columns, parameters, complements

    this.parts = new ArrayList<EnhancedSQLPart>();

    this.columns = new ArrayList<ColumnTag>();
    this.parameters = new ParameterDefinitions();

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // literal [parameterisable] text content
        TextContent p = new TextContent(s);
        this.parts.add(p);
        super.addChild(p);
      } catch (ClassCastException e1) {
        try {
          ParameterTag param = (ParameterTag) obj; // parameter definition
          this.parameters.add(param);
          super.addChild(param);
        } catch (ClassCastException e2) {
          try {
            ColumnTag col = (ColumnTag) obj; // column
            this.columns.add(col);
            super.addChild(col);
          } catch (ClassCastException e3) {
            try {
              ComplementTag p = (ComplementTag) obj; // complement
              this.parts.add(p);
              super.addChild(p);
            } catch (ClassCastException e4) {
              try {
                ColumnsTag p = (ColumnsTag) obj; // columns
                this.structuredColumns = p;
                this.parts.add(p);
                super.addChild(p);
              } catch (ClassCastException e5) {
                throw new InvalidConfigurationFileException(this, //
                    "Invalid tag (" + obj.getClass().getName() + ") in body of <" + super.getTagName() + "> tag", //
                    "The body of the tag <" + super.getTagName() + "> has an invalid tag (of class '"
                        + obj.getClass().getName() + "').");
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

    if (SUtil.isEmpty(this.method)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'method cannot be empty", //
          "Attribute 'method' of tag <" + getTagName() + "> cannot be empty. "
              + "A unique Java method name was expected for the DAO class.");
    }
    if (!this.method.matches(Patterns.VALID_JAVA_METHOD)) {
      throw new InvalidConfigurationFileException(this, //
          "Invalid method name '" + this.method + "'. " + "A Java method name must start with a lower case letter, "
              + "and continue with letters, digits, and/or underscores", //
          "Invalid method name '" + this.method + "' on tag <" + super.getTagName()
              + ">. A Java method name must start with a lower case letter, "
              + "and continue with letters, digits, and/or underscores.");
    }

    // vo

    if (this.vo == null) {
      if (this.structuredColumns == null) {
        throw new InvalidConfigurationFileException(this, //
            "Missing 'vo' attribute in the <" + this.getTagName() + "> tag", //
            "Missing 'vo' attribute in the <" + this.getTagName()
                + "> tag. The 'vo' attribute must be specified when no inner <columns> tag is present.");
      }
    } else {
      if (this.structuredColumns != null) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'vo' attribute. " + "When the 'vo' attribute is specified no inner <columns> tag can be used", //
            "Invalid 'vo' attribute. "
                + "When the 'vo' attribute is specified no inner <columns> tag can be used. Use one or the other but not both.");
      }
      if (SUtil.isEmpty(this.vo)) {
        throw new InvalidConfigurationFileException(this, //
            "When specified, the 'vo' attribute cannot be empty", //
            "When specified, the 'vo' attribute cannot be empty.");
      }
      if (!this.vo.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'vo' attribute with value '" + this.vo + "'. "
                + "A Java class name must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores", //
            "Invalid 'vo' attribute with value '" + this.vo + "' in the tag <" + super.getTagName()
                + ">. A Java class name must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }

      this.voClassName = daosTag.generateNitroVOName(this.vo);
      this.abstractVoClassName = daosTag.generateNitroAbstractVOName(this.vo);

    }

    // mode

    if (this.sMode == null) {
      this.mode = ResultSetMode.LIST;
    } else {
      this.mode = ResultSetMode.parse(this.sMode);
      if (this.mode == null) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'mode' attribute in <select> tag; when specified it must have one of the values: "
                + Arrays.stream(ResultSetMode.values()).map(c -> ("'" + c.getCaption() + "'"))
                    .collect(ListCollector.joining(", ", ", or ")));
      }
    }

    log.debug("sMode=" + this.sMode + " this.structuredColumns=" + this.structuredColumns + " mode=" + this.mode);

    if (this.structuredColumns != null) { // structured <select> can only use mode LIST
      if (this.mode != ResultSetMode.LIST) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid mode '" + this.mode.getCaption() + "' in <select> tag; structured selects can only use mode '"
                + ResultSetMode.LIST.getCaption() + "'.");
      }
    }

    // <column> tags

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(config, adapter);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException(this, //
            "Duplicate <" + new ColumnTag().getTagName() + "> tag for column '" + c.getName() + "'", //
            "Multiple <" + new ColumnTag().getTagName() + "> tags for the same column '" + c.getName() + "' on tag <"
                + getTagName() + ">.");
      }
      cols.add(c);
    }

    // Literal SQL, <columns>, <complement> tags

    for (EnhancedSQLPart p : this.parts) {
      // log.info("VAL p: " + p.getClass().getName());
      p.validate(daosTag, config, fragmentConfig, this.parameters, adapter);
    }

    // all validations cleared

    log.debug("columns=" + this.columns.size());
  }

  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {
    for (EnhancedSQLPart p : this.parts) {
      p.validateAgainstDatabase(metadata);
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

  public String getVOClassName() {
    return this.voClassName;
  }

  public String getAbstractVOClassName() {
    return this.abstractVoClassName;
  }

  public ResultSetMode getResultSetMode() {
    return this.mode;
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
      this.sMode = f.sMode;
      this.metadata = f.metadata;
      this.voClassName = f.voClassName;
      this.mode = f.mode;
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

      log.debug("--------------- Will compare -----------------------");
      boolean comp = Compare.same(this.parts, f.parts);
      log.debug("--------------- Compared same= " + comp + " ------------------");

      boolean same = Compare.same(this.method, f.method) && //
          Compare.same(this.vo, f.vo) && //
          Compare.same(this.sMode, f.sMode) && //
          Compare.same(this.parameters, f.parameters) && //
          Compare.same(this.columns, f.columns) && //
          Compare.same(this.parts, f.parts) && //
          Compare.same(this.aggregatedPart, f.aggregatedPart) && //
          Compare.same(this.structuredColumns, f.structuredColumns) && //
          Compare.same(this.fragmentConfig, f.fragmentConfig);
      log.debug("<select> '" + this.method + "' same=" + same);
      return same;
    } catch (ClassCastException e) {
      log.debug("<select> '" + this.method + "' same=" + "false");
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.method;
  }

}
