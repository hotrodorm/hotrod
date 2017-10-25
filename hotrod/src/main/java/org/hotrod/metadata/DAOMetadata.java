package org.hotrod.metadata;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.CustomDAOTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.utils.ColumnsPrefixGenerator;

public class DAOMetadata {

  // Constants

  private static final Logger log = Logger.getLogger(DAOMetadata.class);

  // Properties

  protected HotRodConfigTag config;
  protected DatabaseAdapter adapter;
  private CustomDAOTag tag;

  private List<SequenceMethodTag> sequences = new ArrayList<SequenceMethodTag>();
  private List<QueryMethodTag> queries = new ArrayList<QueryMethodTag>();
  private List<SelectMethodTag> selects = new ArrayList<SelectMethodTag>();

  private List<SelectMethodMetadata> selectsMetadata;

  private HotRodFragmentConfigTag fragmentConfig;

  // Constructor

  public DAOMetadata(final CustomDAOTag tag, final DatabaseAdapter adapter, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) {
    log.debug("init");
    this.tag = tag;
    this.config = config;
    this.adapter = adapter;
    this.fragmentConfig = fragmentConfig;

    this.sequences = this.tag.getSequences();
    this.queries = this.tag.getQueries();
    this.selects = this.tag.getSelects();

  }

  // Select Methods meta data gathering

  public void gatherSelectsMetadataPhase1(final HotRodGenerator generator, final Connection conn1,
      final DataSetLayout layout) throws ControlledException, UncontrolledException {
    this.selectsMetadata = new ArrayList<SelectMethodMetadata>();
    for (SelectMethodTag selectTag : this.selects) {
      SelectGenerationTag selectGenerationTag = this.config.getGenerators().getSelectedGeneratorTag()
          .getSelectGeneration();
      ColumnsPrefixGenerator columnsPrefixGenerator = new ColumnsPrefixGenerator(this.adapter.getUnescapedSQLCase());
      SelectMethodMetadata sm = new SelectMethodMetadata(generator, selectTag, this.config, selectGenerationTag,
          columnsPrefixGenerator, layout);
      this.selectsMetadata.add(sm);
      sm.gatherMetadataPhase1(conn1);
    }
  }

  public void gatherSelectsMetadataPhase2(final Connection conn2, final VORegistry voRegistry)
      throws ControlledException, UncontrolledException, InvalidConfigurationFileException {
    for (SelectMethodMetadata sm : this.selectsMetadata) {
      sm.gatherMetadataPhase2(conn2, voRegistry);
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

  public List<SelectMethodMetadata> getSelectsMetadata() {
    return selectsMetadata;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return this.fragmentConfig;
  }

}
