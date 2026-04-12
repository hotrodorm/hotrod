package org.hotrod.config.structuredcolumns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.EnhancedSQLPart;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.Patterns;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.identifiers.TypedSQLName;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.StructuredColumnsMetadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ColumnsPrefixGenerator;

@XmlRootElement(name = "columns")
public class ColumnsTag extends EnhancedSQLPart implements ColumnsProvider {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnsTag.class.getName());

  // Properties

  private JDBCTag jdbcTag;
  private HotRodFragmentConfigTag fragmentConfig;

  private String vo = null;
  private String id = null;

  private Set<TypedSQLName> idNames = new HashSet<>();

  private List<VOTag> vos = new ArrayList<>();
  private Expressions expressions = new Expressions();

  @SuppressWarnings("unused")
  private Metadata metadatarep;

  private boolean connectedVOResult;

  private StructuredColumnsMetadata metadata;

  // Constructor

  public ColumnsTag() {
    super("columns");
    log.fine("init");
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
  }

  @XmlElement(name = "expression")
  public void setExpressionsTag(final ExpressionTag exp) {
    this.expressions.addExpression(exp);
  }

  // Behavior

  // ========================
  // EnhancedSQL sub-classing
  // ========================

  @Override
  public void validate(final JDBCTag jdbcTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, ParameterDefinitions parameters, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    this.jdbcTag = jdbcTag;
    this.fragmentConfig = fragmentConfig;

    this.connectedVOResult = this.vo == null && this.vos.size() == 1 && this.expressions.isEmpty();
    this.validate(jdbcTag, config, fragmentConfig, this.connectedVOResult, adapter);

  }

  @Override
  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {

    log.fine("### metadata=" + metadata);

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
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    return "";
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return "... graph columns here...";
  }

  // ========================
  // ColumnProvider interface
  // ========================

  public void validate(final JDBCTag jdbcTag, final HotRodConfigTag config,
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
            this.idNames.add(new TypedSQLName(id));
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
      vo.validate(jdbcTag, config, fragmentConfig, connectedVOResult, adapter);
    }

    // expressions

    Set<TypedSQLName> ids = new HashSet<>(this.idNames);

    this.expressions.validate(jdbcTag, config, fragmentConfig, connectedVOResult, ids);
    if (!ids.isEmpty()) {
      throw new InvalidConfigurationFileException(this, "Invalid id property '" + ids.iterator().next()
          + "'. Could not find any <expression> tag with this property name.");
    }

  }

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final ColumnsPrefixGenerator columnsPrefixGenerator,
      final ColumnsRetriever cr) throws ErrorMessageException, FaultException {

    for (VOTag vo : this.vos) {
      vo.gatherMetadataPhase1(selectTag, columnsPrefixGenerator, cr);
    }
    log.fine("EXPRESSIONS from ColumnsTag... this=" + this);
    this.expressions.gatherMetadataPhase1(selectTag, columnsPrefixGenerator, cr);

  }

  @Override
  public void gatherMetadataPhase2() throws ErrorMessageException, FaultException {

    // Retrieve

    for (VOTag vo : this.vos) {
      vo.gatherMetadataPhase2();
    }
    this.expressions.gatherMetadataPhase2();

    // Assemble

    List<VOMetadata> vos = new ArrayList<VOMetadata>();
    for (VOTag t : this.vos) {
      try {
        vos.add(t.getMetadata(this.fragmentConfig, this.jdbcTag));
      } catch (InvalidConfigurationFileException e) {
        throw new ErrorMessageException(this.jdbcTag, e.getMessage());
      }
    }

    ClassPackage classPackage = getVOClassPackage(this.jdbcTag, this.fragmentConfig);

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

  private ClassPackage getVOClassPackage(final JDBCTag jdbcTag, final HotRodFragmentConfigTag fragmentConfig) {
    ClassPackage fragmentPackage = fragmentConfig != null && fragmentConfig.getFragmentPackage() != null
        ? fragmentConfig.getFragmentPackage()
        : null;
    return jdbcTag.getDAOPackage(fragmentPackage);
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
