package org.hotrod.config.structuredcolumns;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
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
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.metadata.ExpressionsMetadata;
import org.hotrod.metadata.StructuredColumnsMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;

@XmlRootElement(name = "columns")
public class ColumnsTag extends EnhancedSQLPart implements ColumnsProvider {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnsTag.class);

  // Properties

  private String vo = null;

  private List<VOTag> vos = new ArrayList<VOTag>();
  private List<ExpressionsTag> expressions = new ArrayList<ExpressionsTag>();
  private List<CollectionTag> collections = new ArrayList<CollectionTag>();

  private HotRodGenerator generator;

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

  @XmlElement(name = "vo")
  public void setVO(final VOTag vo) {
    this.vos.add(vo);
  }

  @XmlElement(name = "expressions")
  public void setExpressionsTag(final ExpressionsTag exps) {
    this.expressions.add(exps);
  }

  @XmlElement(name = "collection")
  public void setCollectionTag(final CollectionTag collection) {
    this.collections.add(collection);
  }

  // Behavior

  // ========================
  // EnhancedSQL sub-classing
  // ========================

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, ParameterDefinitions parameters)
      throws InvalidConfigurationFileException {
    boolean singleVOResult = this.vos.size() == 1 && this.expressions.isEmpty() && this.collections.isEmpty();
    this.validate(daosTag, config, fragmentConfig, singleVOResult);
  }

  @Override
  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {

    this.generator = generator;

    // vos

    for (VOTag vo : this.vos) {
      vo.validateAgainstDatabase(generator);
    }

    // expressions

    for (ExpressionsTag exp : this.expressions) {
      exp.validateAgainstDatabase(generator);
    }

    // expressions

    for (ExpressionsTag exp : this.expressions) {
      exp.validateAgainstDatabase(generator);
    }

  }

  @Override
  public String renderSQLAngle(final DatabaseAdapter adapter, final ColumnsProvider cp) {
    return cp.renderColumns();
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    // TODO Auto-generated method stub
    return null;
  }

  // ========================
  // ColumnProvider interface
  // ========================

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean singleVOResult)
      throws InvalidConfigurationFileException {

    // vo-class

    boolean includesSingleVO = this.vos.size() == 1 && this.expressions.isEmpty() && this.collections.isEmpty();

    if (this.vo != null) {
      if (includesSingleVO) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid 'vo' attribute on the <"
            + this.getTagName() + "> tag. " + "This attribute should not be specified "
            + "when the <columns> tag includes a single <vo> tag and no <expressions>, <association>, or <collection> tags (this case).");
      }
      if (!this.vo.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid Java class '" + this.vo
                + "' specified in the 'vo' attribute. When specified, the vo-class must start with an upper case letter, "
                + "and continue with any combination of letters, digits, or underscores.");
      }
    } else {
      if (!includesSingleVO) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Missing 'vo' attribute in the <"
            + this.getTagName() + "> tag. " + "This attribute can only be omitted when "
            + "the <columns> tag includes a single <vo> tag and no <expressions>, <association>, or <collection> tags.");
      }
    }

    // vos

    for (VOTag vo : this.vos) {
      vo.validate(daosTag, config, fragmentConfig, singleVOResult);
    }

    // expressions

    for (ExpressionsTag exp : this.expressions) {
      exp.validate(daosTag, config, fragmentConfig, singleVOResult);
    }

    // collections

    for (CollectionTag c : this.collections) {
      c.validate(daosTag, config, fragmentConfig, false);
    }

  }

  private boolean existingVO;
  private String computedVOClass;

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException {

    // if (this.vos.size() == 1 && this.collections.isEmpty() &&
    // this.expressions.isEmpty()) {
    //
    // VOTag singleVO = this.vos.get(0);
    // singleVO.gatherMetadataPhase1(selectTag, selectGenerationTag,
    // columnsPrefixGenerator, conn1);
    //
    // } else {

    for (VOTag vo : this.vos) {
      vo.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, conn1);
    }
    for (ExpressionsTag exp : this.expressions) {
      exp.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, conn1);
    }
    for (CollectionTag c : this.collections) {
      c.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, conn1);
    }

    // }

  }

  @Override
  public void gatherMetadataPhase2(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException, ControlledException {

    // if (this.vos.size() == 1 && this.collections.isEmpty() &&
    // this.expressions.isEmpty()) {
    //
    // VOTag singleVO = this.vos.get(0);
    // singleVO.gatherMetadataPhase2(conn2);
    //
    // } else {

    // Retrieve

    for (VOTag vo : this.vos) {
      vo.gatherMetadataPhase2(conn2);
    }
    for (ExpressionsTag exp : this.expressions) {
      exp.gatherMetadataPhase2(conn2);
    }
    for (CollectionTag c : this.collections) {
      c.gatherMetadataPhase2(conn2);
    }

    // Assemble

    List<ExpressionsMetadata> expressions = new ArrayList<ExpressionsMetadata>();
    for (ExpressionsTag t : this.expressions) {
      expressions.add(t.getExpressionsMetadata());
    }

    List<VOMetadata> collections = new ArrayList<VOMetadata>();
    for (VOTag t : this.collections) {
      collections.add(t.getMetadata());
    }

    List<VOMetadata> vos = new ArrayList<VOMetadata>();
    for (VOTag t : this.vos) {
      vos.add(t.getMetadata());
    }

    this.metadata = new StructuredColumnsMetadata(expressions, collections, vos);

    // }

  }

  // Getters

  public String getVoClass() {
    return vo;
  }

  public List<VOTag> getVos() {
    return vos;
  }

  public List<ExpressionsTag> getExpressions() {
    return expressions;
  }

  public StructuredColumnsMetadata getMetadata() {
    return metadata;
  }

  @Override
  public String renderColumns() {
    return null;
  }

}
