package org.hotrod.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.ExecutorTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.generator.SelectMetadataCache;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.utils.ColumnsPrefixGenerator;

public class ExecutorDAOMetadata implements DataSetMetadata, Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(ExecutorDAOMetadata.class.getName());

  // Properties

  protected transient HotRodConfigTag config;
  protected transient DatabaseAdapter adapter;
  private ExecutorTag tag;

  private List<SequenceMethodTag> sequences = new ArrayList<SequenceMethodTag>();
  private List<QueryMethodTag> queries = new ArrayList<QueryMethodTag>();
  private List<SelectMethodTag> selects = new ArrayList<SelectMethodTag>();

  private SelectMetadataCache selectMetadataCache;
  private List<SelectMethodMetadata> selectsMetadata;

  private ObjectId id;

  private HotRodFragmentConfigTag fragmentConfig;

  // Constructor

  public ExecutorDAOMetadata(final ExecutorTag tag, final DatabaseAdapter adapter, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidIdentifierException {
    initialize(tag, adapter, config, fragmentConfig, new SelectMetadataCache());
  }

  public ExecutorDAOMetadata(final ExecutorTag tag, final DatabaseAdapter adapter, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final SelectMetadataCache selectMetadataCache)
      throws InvalidIdentifierException {
    initialize(tag, adapter, config, fragmentConfig, selectMetadataCache);
  }

  private void initialize(final ExecutorTag tag, final DatabaseAdapter adapter, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final SelectMetadataCache selectMetadataCache)
      throws InvalidIdentifierException {
    log.fine("init");
    this.tag = tag;
    this.config = config;
    this.adapter = adapter;
    this.fragmentConfig = fragmentConfig;

    this.selectMetadataCache = selectMetadataCache;

    this.sequences = this.tag.getSequences();
    this.queries = this.tag.getQueries();
    this.selects = this.tag.getSelects();

    this.id = new ObjectId(Id.fromJavaClass(tag.getJavaClassName()));

    this.selectsMetadata = null;
  }

  // Select Methods meta data gathering

  public boolean gatherSelectsMetadataPhase1(final Metadata metadata, final ColumnsRetriever cr, final JDBCTag jdbcTag)
      throws FaultException, ErrorMessageException {
    this.selectsMetadata = new ArrayList<SelectMethodMetadata>();
    boolean needsToRetrieveMetadata = false;
    for (SelectMethodTag selectTag : this.selects) {
      // SelectMethodMetadata cachedSm =
      // this.selectMetadataCache.get(this.getJavaClassName(),
      // selectTag.getMethod());
      SelectMethodMetadata cachedSm = null; // Do not use cache, for now.
      log.fine("[" + this.getId().getCanonicalSQLName() + "] " + selectTag.getMethod() + "() cache["
          + this.getJavaClassName() + "]=" + cachedSm + " cache[" + this.selectMetadataCache.size() + "]");

      // retrieve fresh metadata
      needsToRetrieveMetadata = true;
      ColumnsPrefixGenerator columnsPrefixGenerator = new ColumnsPrefixGenerator(this.adapter.getUnescapedSQLCase());
      SelectMethodMetadata sm;
      try {
        sm = new SelectMethodMetadata(metadata, cr, selectTag, this.config, null, columnsPrefixGenerator, jdbcTag, null,
            this);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid method name '" + selectTag.getMethod() + "': " + e.getMessage();
        throw new ErrorMessageException(selectTag, msg);
      } catch (InvalidConfigurationFileException e) {
        throw new ErrorMessageException(e.getTag(), e.getMessage());
      }
      this.selectsMetadata.add(sm);
      sm.gatherMetadataPhase1();
      log.fine(">>>   [Fresh] sm.metadataComplete()=" + sm.metadataComplete());

    }
    return needsToRetrieveMetadata;
  }

  public void gatherSelectsMetadataPhase2(final VORegistry voRegistry) throws FaultException, ErrorMessageException {
    for (SelectMethodMetadata sm : this.selectsMetadata) {
      if (!sm.metadataComplete()) {
        sm.gatherMetadataPhase2(voRegistry);
      }
    }
  }

  // Getters

  public String getJavaClassName() {
    return this.tag.getJavaClassName();
  }

  public List<SequenceMethodTag> getSequences() {
    return sequences;
  }

  public List<QueryMethodTag> getQueries() {
    return queries;
  }

  public boolean hasSelects() {
    return !this.selects.isEmpty();
  }

  // Overriden methods

  @Override
  public List<SelectMethodMetadata> getSelectsMetadata() {
    return selectsMetadata;
  }

  @Override
  public HotRodFragmentConfigTag getFragmentConfig() {
    return this.fragmentConfig;
  }

  @Override
  public List<ColumnMetadata> getColumns() {
    return new ArrayList<ColumnMetadata>();
  }

  @Override
  public List<ColumnMetadata> getNonPkColumns() {
    return new ArrayList<ColumnMetadata>();
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
    return new ArrayList<SelectParameterMetadata>();
  }

  @Override
  public List<SelectParameterMetadata> getParameterDefinitions() {
    return new ArrayList<SelectParameterMetadata>();
  }

  @Override
  public String renderSQLSentence(ParameterRenderer parameterRenderer) {
    return null;
  }

  @Override
  public String renderXML(ParameterRenderer parameterRenderer) {
    return null;
  }

  @Override
  public OptimisticLockingMetadata getOptimisticLocking() {
    return null;
  }

  @Override
  public AbstractDAOTag getDaoTag() {
    return this.tag;
  }

}
