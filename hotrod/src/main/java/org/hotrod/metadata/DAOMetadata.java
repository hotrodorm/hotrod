package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.CustomDAOTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.database.DatabaseAdapter;

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

  // Getters

  public List<SequenceMethodTag> getSequences() {
    return sequences;
  }

  public List<QueryMethodTag> getQueries() {
    return queries;
  }

  public List<SelectMethodTag> getSelects() {
    return selects;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return this.fragmentConfig;
  }

}
