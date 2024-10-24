package org.hotrod.config.structuredcolumns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.DaosTag;
import org.hotrod.config.EnhancedSQLPart;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.Patterns;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.metadata.StructuredColumnsMetadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

@XmlRootElement(name = "columns")
public class ColumnsTag extends EnhancedSQLPart implements ColumnsProvider {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ColumnsTag.class);

  // Properties

  private DaosTag daosTag;
  private DataSetLayout layout;
  private HotRodFragmentConfigTag fragmentConfig;

  private String vo = null;
  private String id = null;

  private Set<String> idNames = new HashSet<String>();

  private List<VOTag> vos = new ArrayList<VOTag>();
  private Expressions expressions = new Expressions();

  @SuppressWarnings("unused")
  private transient Metadata metadatarep;

  private boolean connectedVOResult;

  private StructuredColumnsMetadata metadata;

  // Constructor

  public ColumnsTag() {
    super("columns");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "vo")
  public void setVoClass(final String vo) {
    this.vo = vo;
  }

  @XmlAttribute(name = "id")
  public void setId(final String id) {
    this.id = id;
  }

  @XmlElement(name = "vo")
  public void setVO(final VOTag vo) {
    this.vos.add(vo);
    super.addChild(vo);
  }

  @XmlElement(name = "expression")
  public void setExpressionsTag(final ExpressionTag exp) {
    this.expressions.addExpression(exp);
    super.addChild(exp);
  }

  // Behavior

  // ========================
  // EnhancedSQL sub-classing
  // ========================

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, ParameterDefinitions parameters, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    this.daosTag = daosTag;
    this.layout = new DataSetLayout(config);
    this.fragmentConfig = fragmentConfig;

    this.connectedVOResult = this.vo == null && this.vos.size() == 1 && this.expressions.isEmpty();
    this.validate(daosTag, config, fragmentConfig, this.connectedVOResult, adapter);

  }

  @Override
  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {

    log.debug("### metadata=" + metadata);

    this.metadatarep = metadata;

    // vos

    for (VOTag vo : this.vos) {
      vo.validateAgainstDatabase(metadata);
    }

    // expressions

    this.expressions.validateAgainstDatabase(metadata, null);

  }

  @Override
  public String renderSQLAngle(final DatabaseAdapter adapter, final ColumnsProvider cp) {
    return cp.renderColumns();
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return "... graph columns here...";
  }

  @Override
  public void renderXML(final SQLFormatter formatter, final ParameterRenderer parameterRenderer) {

    List<String> columns = new ArrayList<String>();
    for (VOTag vo : this.vos) {
      columns.addAll(vo.gelAliasedSQLColumns());
    }

    for (StructuredColumnMetadata m : this.expressions.getMetadata()) {
      String aliasedSQLColumn = m.renderAliasedSQLColumn();
      columns.add(aliasedSQLColumn);
    }

    String indent = SUtil.getFiller(' ', formatter.getCurrentIndent() + 2);
    formatter.add(ListWriter.render(columns, indent, "", ",\n"));
  }

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    // XXX: Pending. Develop only when/if it's needed.
    return null;
  }

  // ========================
  // ColumnProvider interface
  // ========================

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean connectedVOResult, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    // vo

    boolean includesSingleVO = this.vos.size() == 1 && this.expressions.isEmpty();

    if (this.vo != null) {
      if (includesSingleVO) {
        throw new InvalidConfigurationFileException(this, "Invalid 'vo' attribute on the <" + this.getTagName()
            + "> tag. " + "This attribute should not be specified "
            + "when the <columns> tag includes a single <vo> tag and no <expressions>, <association>, or <collection> tags (this case).");
      }
      if (!this.vo.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(this, "Invalid Java class '" + this.vo
            + "' specified in the 'vo' attribute. When specified, the vo-class must start with an upper case letter, "
            + "and continue with any combination of letters, digits, or underscores.");
      }
      if (!this.expressions.isEmpty()) {
        if (this.id == null) {
          throw new InvalidConfigurationFileException(this,
              "The 2 'id' attribute must be specified when the 'vo' attribute is specified and <expression> tags are included. "
                  + "It includes the comma-separated list of properties that identify a row.");
        }
        for (String id : this.id.split(",")) {
          if (!id.isEmpty()) {
            this.idNames.add(id);
          }
        }
        if (this.idNames.isEmpty()) {
          throw new InvalidConfigurationFileException(this,
              "The 4 'id' attribute should not be empty. "
                  + "It must be specified when the 'vo' attribute is specified; "
                  + "it includes the comma-separated list of properties that identify a row.");
        }
      }
    } else { // vo == null
      if (!includesSingleVO) {
        throw new InvalidConfigurationFileException(this, "Missing 'vo' attribute in the <" + this.getTagName()
            + "> tag. " + "This attribute can only be omitted when "
            + "the <columns> tag includes a single <vo> tag and no <expressions>, <association>, or <collection> tags.");
      }
      if (this.id != null) {
        throw new InvalidConfigurationFileException(this,
            "The 'id' attribute cannot be specified when the 'vo' attribute is not specified.");
      }
    }

    // vos

    for (VOTag vo : this.vos) {
      vo.validate(daosTag, config, fragmentConfig, connectedVOResult, adapter);
    }

    // expressions

    Set<String> ids = new HashSet<String>(this.idNames);

    this.expressions.validate(daosTag, config, fragmentConfig, connectedVOResult, ids);
    if (!ids.isEmpty()) {
      throw new InvalidConfigurationFileException(this, "Invalid id property '" + ids.iterator().next()
          + "'. Could not find any <expression> tag with this property name.");
    }

  }

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final ColumnsRetriever cr)
      throws InvalidSQLException, InvalidConfigurationFileException {

    for (VOTag vo : this.vos) {
      vo.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, cr);
    }
    log.debug("EXPRESSIONS from ColumnsTag... this=" + this);
    this.expressions.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, cr);

  }

  @Override
  public void gatherMetadataPhase2() throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException,
      InvalidConfigurationFileException {

    // Retrieve

    for (VOTag vo : this.vos) {
      vo.gatherMetadataPhase2();
    }
    this.expressions.gatherMetadataPhase2();

    // Assemble

    List<VOMetadata> vos = new ArrayList<VOMetadata>();
    for (VOTag t : this.vos) {
      vos.add(t.getMetadata(this.layout, this.fragmentConfig, this.daosTag));
    }

    ClassPackage classPackage = getVOClassPackage(this.layout, this.fragmentConfig);

    this.metadata = new StructuredColumnsMetadata(this, classPackage, !this.connectedVOResult, this.vo,
        this.expressions, vos);

  }

  public Set<TableDataSetMetadata> getReferencedEntities() {
    Set<TableDataSetMetadata> entities = new HashSet<TableDataSetMetadata>();
    for (VOTag a : this.vos) {
      entities.addAll(a.getReferencedEntities());
    }
    return entities;
  }

  // Utilities

  private ClassPackage getVOClassPackage(final DataSetLayout layout, final HotRodFragmentConfigTag fragmentConfig) {
    ClassPackage fragmentPackage = fragmentConfig != null && fragmentConfig.getFragmentPackage() != null
        ? fragmentConfig.getFragmentPackage()
        : null;
    return layout.getDAOPackage(fragmentPackage);
  }

  // Getters

  public List<VOTag> getVOs() {
    return vos;
  }

  public StructuredColumnsMetadata getMetadata() {
    return metadata;
  }

  @Override
  public String renderColumns() {
    return null;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
