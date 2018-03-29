package org.hotrod.metadata;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.ExecutorTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.Identifier;

public class ExecutorDAOMetadata implements DataSetMetadata {

  // Constants

  private static final Logger log = Logger.getLogger(ExecutorDAOMetadata.class);

  // Properties

  protected HotRodConfigTag config;
  protected DatabaseAdapter adapter;
  private ExecutorTag tag;

  private List<SequenceMethodTag> sequences = new ArrayList<SequenceMethodTag>();
  private List<QueryMethodTag> queries = new ArrayList<QueryMethodTag>();
  private List<SelectMethodTag> selects = new ArrayList<SelectMethodTag>();

  private Map<String, SelectMethodMetadata> cachedSelectsMetadata;
  private List<SelectMethodMetadata> selectsMetadata;

  private HotRodFragmentConfigTag fragmentConfig;

  // Constructor

  public ExecutorDAOMetadata(final ExecutorTag tag, final DatabaseAdapter adapter, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final Map<String, SelectMethodMetadata> cachedSelectsMetadata) {
    log.debug("init");
    this.tag = tag;
    this.config = config;
    this.adapter = adapter;
    this.fragmentConfig = fragmentConfig;

    this.cachedSelectsMetadata = cachedSelectsMetadata;

    this.sequences = this.tag.getSequences();
    this.queries = this.tag.getQueries();
    this.selects = this.tag.getSelects();

    this.selectsMetadata = null;
  }

  // Select Methods meta data gathering

  public boolean gatherSelectsMetadataPhase1(final HotRodGenerator generator, final Connection conn1,
      final DataSetLayout layout) throws ControlledException, UncontrolledException {
    this.selectsMetadata = new ArrayList<SelectMethodMetadata>();
    boolean needsToRetrieveMetadata = false;
    for (SelectMethodTag selectTag : this.selects) {

      SelectMethodMetadata cachedSm = this.cachedSelectsMetadata == null ? null
          : this.cachedSelectsMetadata.get(selectTag.getMethod());

      if (referencesAMarkedEntity(selectTag.getReferencedEntities())) {
        selectTag.markGenerate(true);
      }

      if (cachedSm != null && !selectTag.getGenerateMark()) {

        // use the cached metadata
        this.selectsMetadata.add(cachedSm);

      } else {

        // retrieve fresh metadata
        needsToRetrieveMetadata = true;
        SelectGenerationTag selectGenerationTag = this.config.getGenerators().getSelectedGeneratorTag()
            .getSelectGeneration();
        ColumnsPrefixGenerator columnsPrefixGenerator = new ColumnsPrefixGenerator(this.adapter.getUnescapedSQLCase());
        SelectMethodMetadata sm = new SelectMethodMetadata(generator, selectTag, this.config, selectGenerationTag,
            columnsPrefixGenerator, layout);
        this.selectsMetadata.add(sm);
        sm.gatherMetadataPhase1(conn1);

      }
    }
    return needsToRetrieveMetadata;
  }

  private boolean referencesAMarkedEntity(final Set<TableDataSetMetadata> referencedEntities) {
    for (TableDataSetMetadata referencedEntity : referencedEntities) {
      if (referencedEntity.getDaoTag().getGenerateMark()) {
        return true;
      }
    }
    return false;
  }

  public void gatherSelectsMetadataPhase2(final Connection conn2, final VORegistry voRegistry)
      throws ControlledException, UncontrolledException, InvalidConfigurationFileException {
    for (SelectMethodMetadata sm : this.selectsMetadata) {
      if (!sm.metadataComplete()) {
        sm.gatherMetadataPhase2(conn2, voRegistry);
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
  public String generateDAOName(Identifier identifier) {
    return null;
  }

  @Override
  public String generatePrimitivesName(Identifier identifier) {
    return null;
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
  public DataSetIdentifier getIdentifier() {
    return new DataSetIdentifier("no-sql-name", this.tag.getJavaClassName());
  }

  @Override
  public String renderSQLIdentifier() {
    return null;
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
  public AutoGeneratedColumnMetadata getAutoGeneratedColumnMetadata() {
    return null;
  }

  @Override
  public VersionControlMetadata getVersionControlMetadata() {
    return null;
  }

  @Override
  public AbstractDAOTag getDaoTag() {
    return this.tag;
  }

}
