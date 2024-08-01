package org.hotrod.metadata;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.EnhancedSQLPart.SQLFormatter;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.SelectMethodTag.ResultSetMode;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.generator.mybatisspring.MyBatisSpringGenerator.EntityVOs;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.VOMetadata.DuplicatePropertyNameException;
import org.hotrod.metadata.VOMetadata.VOMember;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOProperty;
import org.hotrod.metadata.VORegistry.VOProperty.EnclosingTagType;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class SelectMethodMetadata implements DataSetMetadata, Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(SelectMethodMetadata.class);

  // Properties

  private boolean structuredSelect;

  @SuppressWarnings("unused")
  private transient Metadata metadata;
  private ColumnsRetriever cr;
  private transient DataSetLayout layout;
  private TableDataSetMetadata entityMetadata;

  private EntityVOs entityVOs;

  @SuppressWarnings("unused")
  private transient JdbcDatabase db;
  private HotRodConfigTag config;
  private transient DatabaseAdapter adapter;
  @SuppressWarnings("unused")
  private transient DatabaseLocation loc;
  private SelectMethodTag tag;
  private transient SelectGenerationTag selectGenerationTag;
  private transient ColumnsPrefixGenerator columnsPrefixGenerator;

  private transient HotRodFragmentConfigTag fragmentConfig;
  private List<ColumnMetadata> nonStructuredColumns;
  private StructuredColumnsMetadata structuredColumns;

  private transient String createView;

  private ObjectId id;

  private ClassPackage classPackage;

  private SelectMethodReturnType selectMethodReturnType;

  // Constructor

  public SelectMethodMetadata(final Metadata metadata, final ColumnsRetriever cr, final SelectMethodTag tag,
      final HotRodConfigTag config, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final DataSetLayout layout,
      final TableDataSetMetadata entityMetadata) throws InvalidIdentifierException, InvalidConfigurationFileException {
    this.metadata = metadata;
    this.cr = cr;
    this.layout = layout;
    this.entityMetadata = entityMetadata;
    this.entityVOs = null;
    this.db = metadata.getJdbcDatabase();
    this.config = config;
    this.adapter = metadata.getAdapter();
    this.loc = metadata.getLoc();
    this.tag = tag;
    this.id = new ObjectId(null, null, Id.fromJavaMember(tag.getMethod()), adapter);
    this.selectGenerationTag = selectGenerationTag;
    this.columnsPrefixGenerator = columnsPrefixGenerator;

    this.fragmentConfig = tag.getFragmentConfig();
    this.structuredSelect = this.tag.getStructuredColumns() != null;
    this.nonStructuredColumns = null;
    this.structuredColumns = null;

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;
    this.classPackage = layout.getDAOPackage(fragmentPackage);

    this.selectMethodReturnType = null;

  }

  public void setEntityVOs(final EntityVOs entityVOs) {
    this.entityVOs = entityVOs;
  }

  // TODO: Just a marker for phase 1

  public void gatherMetadataPhase1() throws InvalidConfigurationFileException {

    if (!this.structuredSelect) {

      // Flat columns

      try {
        this.cr.phase1Flat(getSelectKey(), this.tag, this);
      } catch (InvalidSQLException e) {
        throw new InvalidConfigurationFileException(this.tag,
            "Error in " + this.tag.getSourceLocation().render() + ":\n"
                + "Could not retrieve metadata for <select> tag while creating a temporary SQL view for it.\n" + "* "
                + e.getCause().getMessage() + "\n" + "* Is the create view SQL code below valid?\n"
                + "--- begin SQL ---\n" + e.getInvalidSQL() + "\n--- end SQL ---");
      }

    } else {

      // Graph columns

      try {
        log.debug("Phase 1 - method=" + this.getMethod());
        this.tag.getStructuredColumns().gatherMetadataPhase1(this.tag, this.selectGenerationTag,
            this.columnsPrefixGenerator, this.cr);
      } catch (InvalidSQLException e) {
        throw new InvalidConfigurationFileException(this.tag,
            "Error in " + this.tag.getSourceLocation().render() + ":\n" + "Could not retrieve metadata for <"
                + this.tag.getTagName() + "> tag while creating the temporary SQL view for it.\n" + "* "
                + e.getCause().getMessage() + "\n" + "* Is the create view SQL code below valid?\n"
                + "--- begin SQL ---\n" + e.getInvalidSQL() + "\n--- end SQL ---");
      }

    }

  }

  private String getSelectKey() {
    return "t" + System.identityHashCode(this.tag);
  }

  // TODO: Just a marker for phase 2

  public void gatherMetadataPhase2(final VORegistry voRegistry)
      throws UncontrolledException, InvalidConfigurationFileException {

    if (!this.structuredSelect) {

      // Flat columns

      try {
        this.nonStructuredColumns = this.cr.phase2Flat(getSelectKey());

      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve metadata for <" + new SelectMethodTag().getTagName()
            + "> at " + this.tag.getSourceLocation().render(), e);
      } catch (UnresolvableDataTypeException e) {
        String msg = "Could not retrieve metadata for <" + new SelectMethodTag().getTagName()
            + ">: could not find suitable Java type for column '" + e.getColumnName() + "' ";
        throw new InvalidConfigurationFileException(this.tag, msg);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid retrieved column name: " + e.getMessage();
        throw new InvalidConfigurationFileException(this.tag, msg);
      }

      List<VOProperty> properties = new ArrayList<VOProperty>();

      for (ColumnMetadata cm : this.nonStructuredColumns) {
        StructuredColumnMetadata m = new StructuredColumnMetadata(cm, "entityPrefix1", "columnAlias", false, this.tag);
        properties
            .add(new VOProperty(m.getId().getJavaMemberName(), m, EnclosingTagType.NON_STRUCTURED_SELECT, this.tag));
      }

      List<VOMember> associations = new ArrayList<VOMember>();
      List<VOMember> collections = new ArrayList<VOMember>();

      if (this.entityMetadata == null) { // does not belong to an entity (table or view)

        SelectVOClass vo = null;
        try {
          vo = new SelectVOClass(this.classPackage, this.tag.getVOClassName(), null, null, properties, associations,
              collections, this.tag);
          log.debug("--> Adding VO: " + vo);
          voRegistry.addVO(vo);
        } catch (VOAlreadyExistsException e) {
          throw new InvalidConfigurationFileException(this.tag,
              "Duplicate VO name '" + vo.getName() + "' in package '" + vo.getClassPackage().getPackage()
                  + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                  + ".");
        } catch (StructuredVOAlreadyExistsException e) {
          throw new InvalidConfigurationFileException(this.tag,
              "Duplicate VO name '" + vo.getName() + "' in package '" + vo.getClassPackage().getPackage()
                  + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                  + ".");
        } catch (DuplicatePropertyNameException e) {
          throw new InvalidConfigurationFileException(e.getInitial().getTag(), e.renderMessage());
        }

      }

    } else {

      // Graph columns

      try {
        log.debug("Graph columns - Phase 2");
        this.tag.getStructuredColumns().gatherMetadataPhase2();
        this.structuredColumns = this.tag.getStructuredColumns().getMetadata();
        this.structuredColumns.registerVOs(this.classPackage, voRegistry);

      } catch (InvalidSQLException e) {
        String msg = "Could not create temporary SQL view to retrieve metadata.\n" + "[ " + e.getMessage() + " ]\n"
            + "* Do all resulting columns have different and valid names?\n"
            + "* Is the create view SQL code below valid?\n" + "--- begin SQL ---\n" + e.getInvalidSQL()
            + "\n--- end SQL ---";
        throw new InvalidConfigurationFileException(this.tag, msg);
      } catch (UnresolvableDataTypeException e) {
        String msg = "Could not retrieve metadata: could not find suitable Java type for column '" + e.getColumnName()
            + "' ";
        throw new InvalidConfigurationFileException(this.tag, msg);
      } catch (VOAlreadyExistsException e) {
        throw new InvalidConfigurationFileException(e.getTag(),
            "Duplicate VO name '" + e.getThisName() + "' in package '" + e.getThisPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                + ".");
      } catch (StructuredVOAlreadyExistsException e) {
        throw new InvalidConfigurationFileException(e.getThisTag(),
            "Duplicate VO name '" + e.getThisName() + "' in package '" + e.getThisPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                + ".");
      } catch (DuplicatePropertyNameException e) {
        throw new InvalidConfigurationFileException(e.getDuplicate().getTag(), e.renderMessage());
      }

    }

    this.selectMethodReturnType = new SelectMethodReturnType(this, this.classPackage, this.tag, this.layout);

  }

  // TODO: Just a marker for end of phase 2

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tag == null) ? 0 : tag.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SelectMethodMetadata other = (SelectMethodMetadata) obj;
    if (tag == null) {
      if (other.tag != null)
        return false;
    } else if (!tag.equals(other.tag))
      return false;
    return true;
  }

  // Getters

  public boolean isStructured() {
    return structuredSelect;
  }

  public List<ColumnMetadata> getNonStructuredColumns() {
    return this.nonStructuredColumns;
  }

  public StructuredColumnsMetadata getStructuredColumns() {
    return this.tag.getStructuredColumns() == null ? null : this.tag.getStructuredColumns().getMetadata();
  }

  public String getVOClassName() {
    return this.tag.getVOClassName();
  }

  public String getAbstractVOClassName() {
    return this.tag.getAbstractVOClassName();
  }

  // Other getters

  public TableDataSetMetadata getEntityMetadata() {
    return entityMetadata;
  }

  public String getMethod() {
    return this.tag.getMethod();
  }

  public String getCreateView() {
    return createView;
  }

  @Override
  public List<ColumnMetadata> getColumns() {
    return this.nonStructuredColumns;
  }

  @Override
  public List<ColumnMetadata> getNonPkColumns() {
    return this.nonStructuredColumns;
  }

  @Override
  public KeyMetadata getPK() {
    return null;
  }

  @Override
  public ObjectId getId() {
    return this.id;
  }

  @Override
  public List<KeyMetadata> getUniqueIndexes() {
    return new ArrayList<KeyMetadata>();
  }

  @Override
  public List<ForeignKeyMetadata> getImportedFKs() {
    return new ArrayList<ForeignKeyMetadata>();
  }

  @Override
  public List<ForeignKeyMetadata> getExportedFKs() {
    return new ArrayList<ForeignKeyMetadata>();
  }

  @Override
  public List<SelectParameterMetadata> getParameters() {
    List<SelectParameterMetadata> pms = new ArrayList<SelectParameterMetadata>();
    for (ParameterTag p : this.tag.getParameterDefinitions()) {
      pms.add(new SelectParameterMetadata(p));
    }
    return pms;
  }

  @Override
  public List<SelectParameterMetadata> getParameterDefinitions() {
    List<SelectParameterMetadata> pms = new ArrayList<SelectParameterMetadata>();
    for (ParameterTag p : this.tag.getParameterDefinitions()) {
      pms.add(new SelectParameterMetadata(p));
    }
    return pms;
  }

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return this.tag.renderSQLSentence(parameterRenderer);
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    SQLFormatter formatter = new SQLFormatter();
    this.tag.renderXML(formatter, parameterRenderer);
    return formatter.toString();
  }

  @Override
  public VersionControlMetadata getVersionControlMetadata() {
    return null;
  }

  @Override
  public HotRodFragmentConfigTag getFragmentConfig() {
    return this.fragmentConfig;
  }

  public ResultSetMode getResultSetMode() {
    return this.tag.getResultSetMode();
  }

  @Override
  @Deprecated
  public List<SelectMethodMetadata> getSelectsMetadata() {
    throw new UnsupportedOperationException("A <select> tag should not implement this method.");
  }

  public SelectMethodReturnType getReturnType(final ClassPackage voClassPackage) {
    return this.selectMethodReturnType;
  }

  public boolean metadataComplete() {
    return this.selectMethodReturnType != null;
  }

  // Classes

  public static class SelectMethodReturnType implements Serializable {

    private static final long serialVersionUID = 1L;

    private SelectMethodMetadata sm;

    private SelectVOClass soloVO;
    private SelectVOClass abstractSoloVO;
    private transient VOMetadata connectedVO;

    private ResultSetMode mode;

    public SelectMethodReturnType(final SelectMethodMetadata sm, final ClassPackage voClassPackage,
        final AbstractConfigurationTag tag, final DataSetLayout layout) throws InvalidConfigurationFileException {

      this.sm = sm;

      if (sm.isStructured()) { // graph columns

        StructuredColumnsMetadata structCols = sm.getStructuredColumns();
        this.mode = sm.getResultSetMode();
        if (structCols.getSoloVOClass() == null) { // it's a connected VO
          log.trace(">>> it's a connected VO (1)");
          this.soloVO = null;
          this.abstractSoloVO = null;

          this.connectedVO = structCols.getVOs().get(0);

        } else { // solo VO from a <columns> tag
          log.info(">>> solo VO from a <columns> tag (2)");
          List<VOMember> associations = new ArrayList<VOMember>();
          for (VOMetadata vo : sm.getStructuredColumns().getVOs()) {
            VOMember m;
            try {
              m = new VOMember(vo.getProperty(), vo.getClassPackage(), vo.getName(), vo.getTag());
            } catch (InvalidIdentifierException e) {
              String msg = "Invalid property '" + vo.getProperty() + "':" + e.getMessage();
              throw new InvalidConfigurationFileException(tag, msg);
            }
            associations.add(m);
          }
          this.soloVO = structCols.getSoloVOClass();
          this.connectedVO = null;
        }

      } else { // solo VO from non-graph columns
        log.trace(">>> solo VO (3)");

        List<VOProperty> properties = new ArrayList<VOProperty>();
        for (ColumnMetadata cm : sm.getNonStructuredColumns()) {
          StructuredColumnMetadata m = new StructuredColumnMetadata(cm, "entityPrefix2", "columnAlias", false, null);
          VOProperty p = new VOProperty(cm.getId().getJavaMemberName(), m, EnclosingTagType.NON_STRUCTURED_SELECT,
              sm.tag);
          properties.add(p);
        }

        this.mode = sm.getResultSetMode();

        if (sm.tag.belongsToEntity()) {

          this.soloVO = null;
          this.abstractSoloVO = null;

        } else {

          List<VOMember> associations = new ArrayList<VOMember>();
          List<VOMember> collections = new ArrayList<VOMember>();
          try {
            this.soloVO = new SelectVOClass(voClassPackage, sm.getVOClassName(), null, sm.tag.getImplementsClasses(),
                properties, associations, collections, tag);
            this.abstractSoloVO = new SelectVOClass(voClassPackage, sm.getAbstractVOClassName(), null, null, properties,
                associations, collections, tag);
          } catch (DuplicatePropertyNameException e) {
            // swallow this exception
          }
          this.connectedVO = null;

          log.trace(">>> sm.getVOClassName()=" + sm.getVOClassName() + " sm.getAbstractVOClassName()="
              + sm.getAbstractVOClassName());
          log.trace("this.soloVO.getName()=" + (this.soloVO == null ? "null" : this.soloVO.getName())
              + " this.connectedVO.getName()=" + (this.connectedVO == null ? "null" : this.connectedVO.getName()));

        }

      }

    }

    public SelectVOClass getSoloVO() {
      return soloVO;
    }

    public SelectVOClass getAbstractSoloVO() {
      return this.abstractSoloVO;
    }

    public VOMetadata getConnectedVO() {
      return connectedVO;
    }

    public ResultSetMode getMode() {
      return this.mode;
    }

    // Simpler methods

    private ClassPackage getReturnVOPackage() { // primitives.accounting
      return this.soloVO != null ? this.soloVO.getClassPackage() : this.connectedVO.getClassPackage();
    }

    public String getBaseReturnVOType() { // AccountPersonVO
      if (this.sm.entityVOs != null) {
        return this.sm.entityVOs.getVo().getClassName();
      }
      return this.soloVO != null ? this.soloVO.getName() : this.connectedVO.getName();
    }

    public String getReturnType() { // AccountPersonVO, List<AccountPersonVO>, Cursor<AccountPersonVO>
      switch (this.mode) {
      case LIST:
        return "List<" + getBaseReturnVOType() + ">";
      case CURSOR:
        return "Cursor<" + getBaseReturnVOType() + ">";
      default:
        return getBaseReturnVOType(); // single-row
      }
    }

    public String getVOFullClassName() { // primitives.accounting.AccountPersonVO
      if (this.sm.entityVOs != null) {
        return this.sm.entityVOs.getVo().getFullClassName();
      } else {
        return this.getReturnVOPackage().getFullClassName(getBaseReturnVOType());
      }
    }

  }

  @Override
  public AbstractDAOTag getDaoTag() {
    throw new UnsupportedOperationException("This operation is not supported ina select method tag.");
  }

}
