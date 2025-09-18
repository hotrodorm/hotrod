package org.hotrod.metadata;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.EnhancedSQLPart;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.SelectMethodTag.ResultSetMode;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.generator.jdbc.EntityDTOs;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.VOMetadata.DuplicatePropertyNameException;
import org.hotrod.metadata.VOMetadata.VOMember;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOProperty;
import org.hotrod.metadata.VORegistry.VOProperty.EnclosingTagType;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class SelectMethodMetadata implements DataSetMetadata, Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(SelectMethodMetadata.class.getName());

  // Properties

  private boolean structuredSelect;

  @SuppressWarnings("unused")
  private Metadata metadata;
  private ColumnsRetriever cr;
  private JDBCTag jdbcTag;

  private TableDataSetMetadata entityMetaData;
  private ExecutorDAOMetadata executorMetaData;

  private EntityDTOs entityVOs;

  @SuppressWarnings("unused")
  private JdbcDatabase db;
  @SuppressWarnings("unused")
  private HotRodConfigTag config;
  private DatabaseAdapter adapter;
  @SuppressWarnings("unused")
  private DatabaseLocation loc;
  private SelectMethodTag tag;
  private ColumnsPrefixGenerator columnsPrefixGenerator;

  private HotRodFragmentConfigTag fragmentConfig;
  private List<ColumnMetadata> nonStructuredColumns;
  private StructuredColumnsMetadata structuredColumns;

  private String createView;

  private ObjectId id;

  private ClassPackage fragmentPackage;
  @SuppressWarnings("unused")
  private ClassPackage layoutPackage;
  private ClassPackage modelPackage;

  private SelectMethodReturnType selectMethodReturnType;

  // Constructor

  public SelectMethodMetadata(final Metadata metadata, final ColumnsRetriever cr, final SelectMethodTag tag,
      final HotRodConfigTag config, final ColumnsPrefixGenerator columnsPrefixGenerator, final JDBCTag jdbcTag,
      final TableDataSetMetadata entityMetaData, ExecutorDAOMetadata executorMetaData)
      throws InvalidIdentifierException, InvalidConfigurationFileException, ErrorMessageException {
    this.metadata = metadata;
    this.cr = cr;
    this.jdbcTag = jdbcTag;

    if (entityMetaData == null) {
      if (executorMetaData == null) {
        throw new ErrorMessageException(tag,
            "The Nitro select tag must belong to an entity or to a DAO but does not belong to any of them.");
      }
    } else {
      if (executorMetaData != null) {
        throw new ErrorMessageException(tag,
            "The Nitro select tag must belong to an entity or to a DAO and cannot belong to both of them.");
      }
    }
    this.entityMetaData = entityMetaData;
    this.executorMetaData = executorMetaData;

    this.entityVOs = null;
    this.db = metadata.getJdbcDatabase();
    this.config = config;
    this.adapter = metadata.getAdapter();
    this.loc = metadata.getLoc();
    this.tag = tag;
    this.id = new ObjectId(null, null, Id.fromJavaMember(tag.getMethod()), adapter);
    this.columnsPrefixGenerator = columnsPrefixGenerator;

    this.fragmentConfig = tag.getFragmentConfig();
    this.structuredSelect = this.tag.getStructuredColumns() != null;
    this.nonStructuredColumns = null;
    this.structuredColumns = null;

    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;
    this.layoutPackage = this.jdbcTag.getLayoutPackage(this.fragmentPackage);
    this.modelPackage = this.jdbcTag.getModelPackage(this.fragmentPackage);

    this.selectMethodReturnType = null;

  }

  public void setEntityVOs(final EntityDTOs entityVOs) {
    this.entityVOs = entityVOs;
  }

  // TODO: Just a marker for phase 1

  public void gatherMetadataPhase1() throws FaultException, ErrorMessageException {

    if (!this.structuredSelect) {

      // Flat columns

      try {
        this.cr.phase1Flat(getSelectKey(), this.tag, this);
      } catch (InvalidSQLException e) {
        throw new ErrorMessageException(this.tag,
            "Could not retrieve metadata for <select>\n" + "* " + e.getCause().getMessage() + "\n"
                + "* Is the SQL query below valid?\n" + "--- begin SQL ---\n" + e.getInvalidSQL()
                + "\n--- end SQL ---");
      } catch (InvalidConfigurationFileException e) {
        throw new ErrorMessageException(e.getTag(), e.getMessage());
      }

    } else {

      // Graph columns

      log.fine("Phase 1 - method=" + this.getMethod());
      this.tag.getStructuredColumns().gatherMetadataPhase1(this.tag, this.columnsPrefixGenerator, this.cr);

    }

  }

  private String getSelectKey() {
    return "t" + System.identityHashCode(this.tag);
  }

  // TODO: Just a marker for phase 2

  public void gatherMetadataPhase2(final VORegistry voRegistry) throws FaultException, ErrorMessageException {

    if (!this.structuredSelect) {

      // Flat columns

      try {
        this.nonStructuredColumns = this.cr.phase2Flat(getSelectKey());

      } catch (SQLException e) {
        throw new FaultException(this.tag,
            "Could not retrieve metadata for <" + new SelectMethodTag().getTagName() + ">", e);
      } catch (UnresolvableDataTypeException e) {
        throw new ErrorMessageException(this.tag,
            "Could not find suitable Java type for column '" + e.getColumnMetadata().getName() + "'");
      } catch (InvalidIdentifierException e) {
        throw new ErrorMessageException(this.tag, "Invalid retrieved column name: " + e.getMessage());
      }

      List<VOProperty> properties = new ArrayList<VOProperty>();

      for (ColumnMetadata cm : this.nonStructuredColumns) {
        StructuredColumnMetadata m = new StructuredColumnMetadata(cm, "entityPrefix1", "columnAlias", false, this.tag);
        properties
            .add(new VOProperty(m.getId().getJavaMemberName(), m, EnclosingTagType.NON_STRUCTURED_SELECT, this.tag));
      }

      List<VOMember> associations = new ArrayList<VOMember>();
      List<VOMember> collections = new ArrayList<VOMember>();

      if (this.entityMetaData == null) { // does not belong to an entity (table or view)

        SelectVOClass vo = null;
        try {
          vo = new SelectVOClass(this.fragmentPackage, this.modelPackage, this.tag.getVOClassName(), null, null,
              properties, associations, collections, this.tag);
          log.fine("--> Adding VO: " + vo);
          voRegistry.addVO(vo);
        } catch (VOAlreadyExistsException e) {
          throw new ErrorMessageException(this.tag,
              "Duplicate VO name '" + vo.getName() + "' in package '" + vo.getClassPackage().getPackage()
                  + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                  + ".");
        } catch (StructuredVOAlreadyExistsException e) {
          throw new ErrorMessageException(this.tag,
              "Duplicate VO name '" + vo.getName() + "' in package '" + vo.getClassPackage().getPackage()
                  + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                  + ".");
        } catch (DuplicatePropertyNameException e) {
          throw new ErrorMessageException(e.getInitial().getTag(), e.renderMessage());
        }

      }

    } else {

      // Graph columns

      try {
        log.fine("Graph columns - Phase 2");
        this.tag.getStructuredColumns().gatherMetadataPhase2();
        this.structuredColumns = this.tag.getStructuredColumns().getMetadata();
        this.structuredColumns.registerVOs(this.fragmentPackage, this.modelPackage, voRegistry);

      } catch (VOAlreadyExistsException e) {
        throw new ErrorMessageException(e.getTag(),
            "Duplicate VO name '" + e.getThisName() + "' in package '" + modelPackage.getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                + ".");
      } catch (StructuredVOAlreadyExistsException e) {
        throw new ErrorMessageException(e.getThisTag(),
            "Duplicate VO name '" + e.getThisName() + "' in package '" + modelPackage.getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                + ".");
      } catch (DuplicatePropertyNameException e) {
        throw new ErrorMessageException(e.getDuplicate().getTag(), e.renderMessage());
      } catch (InvalidConfigurationFileException e) {
        throw new ErrorMessageException(e.getTag(), e.getMessage());
      }

    }

    this.selectMethodReturnType = new SelectMethodReturnType(this, this.fragmentPackage, this.tag, this.jdbcTag);

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

  public String getSelectMethodNamespace() {
    return this.entityMetaData != null ? this.entityMetaData.getId().getCanonicalSQLName()
        : this.executorMetaData.getJavaClassName();
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

  public List<EnhancedSQLPart> getParts() {
    return this.tag.getParts();
  }

  @Override
  public OptimisticLockingMetadata getOptimisticLocking() {
    return null;
  }

  @Override
  public HotRodFragmentConfigTag getFragmentConfig() {
    return this.fragmentConfig;
  }

  public ResultSetMode getResultSetMode() {
    return this.tag.getResultSetMode();
  }

  public SelectMethodTag getTag() {
    return this.tag;
  }

  public EntityDTOs getEntityVOs() {
    return entityVOs;
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

  @Override
  public AbstractDAOTag getDaoTag() {
    throw new UnsupportedOperationException("This operation is not supported ina select method tag.");
  }

}
