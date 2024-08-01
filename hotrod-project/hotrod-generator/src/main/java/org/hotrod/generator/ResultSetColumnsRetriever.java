package org.hotrod.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.SQLUtil;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class ResultSetColumnsRetriever implements ColumnsRetriever {

  private static final Logger log = LogManager.getLogger(ResultSetColumnsRetriever.class);

  private HotRodConfigTag config;
  @SuppressWarnings("unused")
  private DatabaseLocation dloc;
  @SuppressWarnings("unused")
  private SelectGenerationTag selectGenerationTag;
  private DatabaseAdapter adapter;
  @SuppressWarnings("unused")
  private JdbcDatabase db;
  private Connection conn;
  private Map<String, RetrievalContext> contexts;

  public ResultSetColumnsRetriever(final HotRodConfigTag config, final DatabaseLocation dloc,
      final SelectGenerationTag selectGenerationTag, final DatabaseAdapter adapter, final JdbcDatabase db,
      final Connection conn) throws SQLException {
    this.config = config;
    this.dloc = dloc;
    this.selectGenerationTag = selectGenerationTag;
    this.adapter = adapter;
    this.db = db;
    this.conn = conn;
    this.contexts = new HashMap<String, RetrievalContext>();
  }

  private class ResultSetParameterRenderer implements ParameterRenderer {

    private List<JDBCType> parameterJDBCTypes = new ArrayList<JDBCType>();

    @Override
    public String render(final SQLParameter parameter) {
      log.debug("prepare view 0.1 -- parameter=" + parameter.getDefinition());
      JDBCType jdbcType = parameter.getDefinition().getJDBCType();
      parameterJDBCTypes.add(jdbcType);
      String parameterSampleValue = parameter.getDefinition().getSampleSQLValue();
      String adapterSampleValue = adapter.provideSampleValueFor(jdbcType);
      adapterSampleValue = adapterSampleValue != null ? ("(" + adapterSampleValue + ")") : null;
      String sample = parameterSampleValue != null ? parameterSampleValue : adapterSampleValue;
      return sample != null ? sample : "?";
    }

  }

  // TODO: Flat <select> (just a marker)

  @Override
  public void phase1Flat(final String key, final SelectMethodTag tag, final SelectMethodMetadata sm)
      throws InvalidSQLException, InvalidConfigurationFileException {

    log.debug("prepare view 0");

    RetrievalContext ctx = new RetrievalContext(tag, sm);
    this.contexts.put(key, ctx);

    log.debug("flat 1 -- this.conn=" + this.conn);

    ResultSetParameterRenderer pr = new ResultSetParameterRenderer();
    String foundation = SQLUtil.cleanUpSQL(ctx.getTag().renderSQLSentence(pr));

    List<ColumnMetadata> flatColumns = new ArrayList<ColumnMetadata>();
    ctx.setFlatColumnsMetadata(flatColumns);

    log.debug("flat 2 -- method=" + sm.getMethod() + " sql=" + foundation);

    try (PreparedStatement ps = this.conn.prepareStatement(foundation)) {

      log.debug("flat 2.1");

      ResultSetMetaData rm = ps.getMetaData();
      int columns = rm.getColumnCount();
      for (int i = 1; i <= columns; i++) {
        String label = rm.getColumnLabel(i);
        ColumnTag columnTag = ctx.getTag().findColumnTag(label, this.adapter);
        log.debug("prepare view 3 -- column=" + label);
        ColumnMetadata cm;
        try {
          cm = new ColumnMetadata(ctx.getSm(), rm, i, ctx.getTag().getMethod(), this.adapter, columnTag, false, false,
              this.config.getTypeSolverTag());
        } catch (UnresolvableDataTypeException e) {
          String msg = "Could not retrieve metadata for <" + new SelectMethodTag().getTagName()
              + ">: could not find suitable Java type for column '" + e.getColumnName() + "' ";
          throw new InvalidConfigurationFileException(ctx.getTag(), msg);
        } catch (InvalidIdentifierException e) {
          String msg = "Invalid retrieved column name: " + e.getMessage();
          throw new InvalidConfigurationFileException(ctx.getTag(), msg);
        }
        flatColumns.add(cm);
      }
    } catch (SQLException e) {
      String em = e.getMessage() == null ? "" : e.getMessage().trim();
      String msg = "Could not retrieve metadata for <" + new SelectMethodTag().getTagName()
          + "> tag: invalid SQL SELECT statement: " + em + "\n---\n" + foundation + "\n---";
      throw new InvalidConfigurationFileException(ctx.getTag(), msg);
    }

  }

  @Override
  public List<ColumnMetadata> phase2Flat(final String key) {
    log.debug("flat 4 -- columns retrieved.");
    RetrievalContext ctx = this.contexts.get(key);
    return ctx.getColumnsMetadata();
  }

  // TODO: Graph <select> (just a marker)

  @Override
  public void phase1Structured(final String key, final SelectMethodTag selectTag, final String aliasPrefix,
      final String entityPrefix, final ColumnsProvider columnsProvider, final SelectMethodMetadata sm)
      throws InvalidSQLException, InvalidConfigurationFileException {

    log.debug("prepare view 0");

    RetrievalContext ctx = new RetrievalContext(selectTag, sm);
    this.contexts.put(key, ctx);

    log.debug("prepare view 1");
    ResultSetParameterRenderer pr = new ResultSetParameterRenderer();
    String foundation = SQLUtil.cleanUpSQL(selectTag.renderSQLAngle(pr, columnsProvider, this.adapter));

    List<StructuredColumnMetadata> structuredColumnMetadata = new ArrayList<StructuredColumnMetadata>();
    ctx.setStructuredColumnMetadata(structuredColumnMetadata);

    try (PreparedStatement ps = this.conn.prepareStatement(foundation)) {

      log.debug("flat 2.1");

      ResultSetMetaData rm = ps.getMetaData();
      int columns = rm.getColumnCount();
      for (int i = 1; i <= columns; i++) {
        String label = rm.getColumnLabel(i);
        ColumnTag columnTag = ctx.getTag().findColumnTag(label, this.adapter);
        log.debug("prepare view 3 -- column=" + label);
        ColumnMetadata cm;
        try {
          cm = new ColumnMetadata(ctx.getSm(), rm, i, ctx.getTag().getMethod(), this.adapter, columnTag, false, false,
              this.config.getTypeSolverTag());

          String alias = aliasPrefix + cm.getName();
          StructuredColumnMetadata scm = new StructuredColumnMetadata(cm, entityPrefix, alias, false, null,
              columnsProvider);
          structuredColumnMetadata.add(scm);

        } catch (UnresolvableDataTypeException e) {
          String msg = "Could not retrieve metadata for <" + new SelectMethodTag().getTagName()
              + ">: could not find suitable Java type for column '" + e.getColumnName() + "' ";
          throw new InvalidConfigurationFileException(ctx.getTag(), msg);
        } catch (InvalidIdentifierException e) {
          String msg = "Invalid retrieved column name: " + e.getMessage();
          throw new InvalidConfigurationFileException(ctx.getTag(), msg);
        }
      }
    } catch (SQLException e) {
      throw new InvalidSQLException("could not retrieve metadata.", e);
    }

  }

  @Override
  public List<StructuredColumnMetadata> phase2Structured(final String key, final SelectMethodTag selectTag,
      final String aliasPrefix, final String entityPrefix, final ColumnsProvider columnsProvider)
      throws UnresolvableDataTypeException, InvalidConfigurationFileException, UncontrolledException {
    log.debug("flat 4 -- columns retrieved.");
    RetrievalContext ctx = this.contexts.get(key);
    return ctx.getStructuredColumnMetadata();
  }

  // TODO: End of graph <select> (just a marker)

  @Override
  public void close() throws Exception {
  }

  // Classes

  public static class RetrievalContext {

    private SelectMethodTag tag;
    private SelectMethodMetadata sm;
    private String cleanedUpFoundation;
    private String tempViewName;
    private List<ColumnMetadata> flatColumnsMetadata;
    private List<StructuredColumnMetadata> structuredColumnMetadata;

    public RetrievalContext(final SelectMethodTag tag, final SelectMethodMetadata sm) {
      this.tag = tag;
      this.sm = sm;
    }

    public String getCleanedUpFoundation() {
      return cleanedUpFoundation;
    }

    public void setCleanedUpFoundation(String cleanedUpFoundation) {
      this.cleanedUpFoundation = cleanedUpFoundation;
    }

    public String getTempViewName() {
      return tempViewName;
    }

    public void setTempViewName(String tempViewName) {
      this.tempViewName = tempViewName;
    }

    public SelectMethodTag getTag() {
      return tag;
    }

    public SelectMethodMetadata getSm() {
      return sm;
    }

    public List<ColumnMetadata> getColumnsMetadata() {
      return flatColumnsMetadata;
    }

    public void setFlatColumnsMetadata(final List<ColumnMetadata> flatColumnsMetadata) {
      this.flatColumnsMetadata = flatColumnsMetadata;
    }

    public List<StructuredColumnMetadata> getStructuredColumnMetadata() {
      return structuredColumnMetadata;
    }

    public void setStructuredColumnMetadata(final List<StructuredColumnMetadata> structuredColumnMetadata) {
      this.structuredColumnMetadata = structuredColumnMetadata;
    }

  }

}
