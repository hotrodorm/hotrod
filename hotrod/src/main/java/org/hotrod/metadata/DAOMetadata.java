package org.hotrod.metadata;

import java.sql.Connection;
import java.sql.SQLException;
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
import org.hotrod.config.structuredcolumns.ColumnsTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
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

  // Select Method meta data gathering

  public void gatherSelectsMetadataPhase1(final HotRodGenerator generator, final Connection conn1)
      throws ControlledException, UncontrolledException {
    for (SelectMethodTag selectTag : this.selects) {
      ColumnsTag ct = selectTag.getStructuredColumns();
      if (ct == null) {

        // Unstructured columns

        String tempViewName = this.config.getGenerators().getSelectedGeneratorTag().getSelectGeneration()
            .getNextTempViewName();
        SelectMethodMetadata sm = new SelectMethodMetadata(generator, selectTag, tempViewName,
            this.config);
        selectTag.setDataSetMetadata(sm);

        try {
          sm.prepareFlatColumnsRetrieval(conn1);
        } catch (SQLException e) {
          throw new UncontrolledException("Failed to retrieve metadata for <" + new SelectMethodTag().getTagName()
              + "> with method '" + selectTag.getMethod() + "'.", e);
        }

      } else {

        // Structured columns

        SelectGenerationTag selectGenerationTag = this.config.getGenerators().getSelectedGeneratorTag()
            .getSelectGeneration();
        ColumnsPrefixGenerator columnsPrefixGenerator = new ColumnsPrefixGenerator();

        try {
          log.info("ct.gatherMetadataPhase1()");
          ct.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, conn1);
        } catch (InvalidSQLException e) {
          throw new ControlledException("Could not retrieve metadata for <" + selectTag.getTagName() + "> on "
              + selectTag.getSourceLocation().render() + "; could not create temporary SQL view for it.\n" + "[ "
              + e.getMessage() + " ]\n" + "* Do all resulting columns have different and valid names?\n"
              + "* Is the create view SQL code below valid?\n" + "--- begin SQL ---\n" + e.getInvalidSQL()
              + "\n--- end SQL ---");
        }

      }
    }
  }

  public void gatherSelectsMetadataPhase2(final Connection conn2) throws ControlledException, UncontrolledException {
    this.selectsMetadata = new ArrayList<SelectMethodMetadata>();
    for (SelectMethodTag selectTag : this.selects) {
      SelectMethodMetadata sm = selectTag.getDataSetMetadata();
      this.selectsMetadata.add(sm);
      if (selectTag.getStructuredColumns() == null) {

        // Unstructured columns

        try {
          sm.retrieveFlatColumnsMetadata(conn2);
        } catch (SQLException e) {
          throw new UncontrolledException("Failed to retrieve metadata for <" + new SelectMethodTag().getTagName()
              + "> with method '" + selectTag.getMethod() + "'.", e);

        } catch (UnresolvableDataTypeException e) {
          throw new ControlledException("Failed to retrieve metadata for <" + new SelectMethodTag().getTagName()
              + "> with method '" + selectTag.getMethod() + "': could not find suitable Java property type for column '"
              + e.getColumnName() + "'.");
        }

      } else {

        // Structured columns

        try {
          log.info("ct.gatherMetadataPhase2()");
          selectTag.getStructuredColumns().gatherMetadataPhase2(conn2);
        } catch (InvalidSQLException e) {
          throw new ControlledException("Could not retrieve metadata for <" + selectTag.getTagName() + "> on "
              + selectTag.getSourceLocation().render() + ". Is the create view SQL code below valid?\n"
              + "--- begin SQL ---\n" + e.getInvalidSQL() + "\n--- end SQL ---");
        } catch (UnresolvableDataTypeException e) {
          throw new ControlledException("Failed to retrieve metadata for <" + new SelectMethodTag().getTagName()
              + "> with method '" + selectTag.getMethod() + "': could not find suitable Java property type for column '"
              + e.getColumnName() + "'.");
        }

      }
    }
  }

  // Getters

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
