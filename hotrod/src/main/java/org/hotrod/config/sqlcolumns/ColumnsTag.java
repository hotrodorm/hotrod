package org.hotrod.config.sqlcolumns;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.Patterns;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

@XmlRootElement(name = "columns")
public class ColumnsTag extends ColumnsProducerTag {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnsTag.class);

  // Properties

  private String voClass = null;

  private List<VOTag> vos = new ArrayList<VOTag>();
  private List<ExpressionsTag> expressions = new ArrayList<ExpressionsTag>();
  private List<CollectionTag> collections = new ArrayList<CollectionTag>();
  private List<AssociationTag> associations = new ArrayList<AssociationTag>();

  // Constructor

  public ColumnsTag() {
    super("columns");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "vo-class")
  public void setVoClass(final String voClass) {
    this.voClass = voClass;
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

  @XmlElement(name = "association")
  public void setAssociationTag(final AssociationTag association) {
    this.associations.add(association);
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    boolean singleVO = this.vos.size() == 1 && this.expressions.isEmpty();

    // vo-class

    if (this.voClass != null) {
      if (singleVO) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid 'vo-class' attribute on the <" + this.getTagName() + "> tag. "
                + "This attribute must not be specified "
                + "when the <columns> tag includes a single <vo> tag and no <expressions> tags.");
      }
      if (!this.voClass.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid Java class '" + this.voClass
                + "' specified in the 'vo-class' attribute. When specified, the vo-class must start with an upper case letter, "
                + "and continue with any combination of letters, digits, or underscores.");
      }
    } else {
      if (!singleVO) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Missing 'vo-class' attribute on the <" + this.getTagName() + "> tag. "
                + "This attribute can be ommitted when "
                + "there's an inner <columns> tag that includes a single <vo> tag and no <expressions> tags. "
                + "The 'vo-class' attribute must be specified otherwise.");
      }
    }

    // vos

    for (VOTag vo : this.vos) {
      vo.validate(daosTag, config, fragmentConfig, singleVO);
    }

    // expressions

    for (ExpressionsTag exp : this.expressions) {
      exp.validate(config);
    }

  }

  // Meta data gathering

  private boolean existingVO;
  private String computedVOClass;

  public void prepareRetrieval(final SelectMethodTag selectTag, final DatabaseAdapter adapter, final JdbcDatabase db,
      final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag,
      final ColumnsProducerTag columnsProducerTag, final ColumnsPrefixGenerator columnsPrefixGenerator,
      final Connection conn1) throws InvalidSQLException {

    if (this.vos.size() == 1 && this.collections.isEmpty() && this.associations.isEmpty()
        && this.expressions.isEmpty()) {

      VOTag singleVO = this.vos.get(0);
      singleVO.prepareRetrieval(selectTag, adapter, db, loc, selectGenerationTag, columnsProducerTag,
          columnsPrefixGenerator, conn1);

      // this.existingVO = singleVO.getExistingVO();
      // this.computedVOClass = singleVO.getComputedVOClass();

    } else {

      for (ExpressionsTag exp : this.expressions) {
        exp.prepareRetrieval(selectTag, adapter, db, loc, selectGenerationTag, columnsProducerTag,
            columnsPrefixGenerator, conn1);
      }

      for (CollectionTag c : this.collections) {
        c.prepareRetrieval(selectTag, adapter, db, loc, selectGenerationTag, columnsProducerTag, columnsPrefixGenerator,
            conn1);

      }

      for (AssociationTag a : this.associations) {
        a.prepareRetrieval(selectTag, adapter, db, loc, selectGenerationTag, columnsProducerTag, columnsPrefixGenerator,
            conn1);

      }

    }

  }

  @Override
  public void retrieve() {

  }

  // Getters

  public String getVoClass() {
    return voClass;
  }

  public List<VOTag> getVos() {
    return vos;
  }

  public List<ExpressionsTag> getExpressions() {
    return expressions;
  }

}
