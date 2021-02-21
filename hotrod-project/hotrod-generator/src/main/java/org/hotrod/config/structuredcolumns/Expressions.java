package org.hotrod.config.structuredcolumns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter.UnescapedSQLCase;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.utils.ColumnsMetadataRetriever;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class Expressions implements ColumnsProvider, Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(Expressions.class);

  // Properties

  private TableDataSetMetadata tableMetadata;
  private List<ExpressionTag> expressions;

  protected transient ColumnsMetadataRetriever columnsRetriever;
  private transient Metadata metadata;

  // Constructor

  public Expressions() {
    this.expressions = new ArrayList<ExpressionTag>();
    this.columnsRetriever = null;
    this.metadata = null;
  }

  // Behavior

  public void addExpression(final ExpressionTag exp) {
    this.expressions.add(exp);
  }

  public boolean isEmpty() {
    return this.expressions.isEmpty();
  }

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean singleVOResult, final Set<String> idNames)
      throws InvalidConfigurationFileException {
    for (ExpressionTag tag : this.expressions) {
      tag.validate(daosTag, config, fragmentConfig, singleVOResult, idNames);
    }
  }

  public void validateAgainstDatabase(final Metadata metadata, final TableDataSetMetadata tableMetadata)
      throws InvalidConfigurationFileException {
    this.metadata = metadata;
    this.tableMetadata = tableMetadata;
  }

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final ColumnsRetriever cr)
      throws InvalidSQLException, InvalidConfigurationFileException {
    log.debug("this=" + this + " - this.expressions.isEmpty()=" + this.expressions.isEmpty());
    if (this.expressions.isEmpty()) {
      this.columnsRetriever = null;
    } else {
      this.columnsRetriever = new ColumnsMetadataRetriever(selectTag, this.metadata.getAdapter(),
          this.metadata.getJdbcDatabase(), this.metadata.getLoc(), selectGenerationTag, this, null,
          columnsPrefixGenerator, cr);
      this.columnsRetriever.prepareRetrieval();
    }
  }

  private Map<String, ExpressionTag> expressionsByAlias;

  @Override
  public String renderColumns() {
    this.expressionsByAlias = new HashMap<String, ExpressionTag>();
    String aliasPrefix = this.metadata.getAdapter().getUnescapedSQLCase() == UnescapedSQLCase.UPPER_CASE ? "EXPR_"
        : "expr_";
    ListWriter w = new ListWriter("      ", "", ",\n");
    int n = 0;
    for (ExpressionTag tag : this.expressions) {
      log.debug("this=" + this + " - expression 'property'=" + tag.getProperty());
      String alias = aliasPrefix + n;
      tag.setTempAlias(alias);
      this.expressionsByAlias.put(alias, tag);
      w.add(tag.getBody() + " as " + alias);
      n++;
    }
    String cols = w.toString();
    log.debug("this=" + this + " - cols=" + cols);
    return cols;
  }

  @Override
  public void gatherMetadataPhase2() throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException,
      InvalidConfigurationFileException {
    log.debug("this.columnsRetriever=" + this.columnsRetriever);
    if (this.columnsRetriever != null) {
      List<StructuredColumnMetadata> cms = this.columnsRetriever.retrieve();
      for (StructuredColumnMetadata cm : cms) {
        String alias = cm.getColumnName();
        ExpressionTag tag = this.expressionsByAlias.get(alias);

        ColumnTag ct = new ColumnTag();
        ct.setJavaName(tag.getProperty());
        if (tag.getClassName() != null) {
          ct.setJavaType(tag.getClassName());
        }
        if (tag.getConverterTag() != null) {
          ct.setConverterTag(tag.getConverterTag());
        }
        log.debug("******** java-name=" + ct.getJavaName() + " java-type=" + ct.getJavaType());
        try {
          cm = StructuredColumnMetadata.applyColumnTag(cm, ct, tag, this.metadata.getAdapter());
        } catch (InvalidIdentifierException e) {
          String msg = "Invalid name for column '" + cm.getColumnName() + tag.getClassName() + "': " + e.getMessage();
          throw new InvalidConfigurationFileException(tag, msg, msg);
        }

        boolean isId = false;
        for (ColumnMetadata tableCol : this.tableMetadata.getColumns()) {
          if (tableCol.getId().getJavaMemberName().equals(tag.getProperty()) && tableCol.belongsToPK()) {
            isId = true;
          }
          if (tableCol.getId().getJavaMemberName().equals(tag.getProperty())) {
            cm.setReusesMemberFromSuperClass(true);
          }
        }

        cm.setId(isId);
        cm.setFormula(tag.getBody());
        tag.setMetadata(cm);
      }
    }
  }

  public List<StructuredColumnMetadata> getMetadata() {
    List<StructuredColumnMetadata> cms = new ArrayList<StructuredColumnMetadata>();
    for (ExpressionTag tag : this.expressions) {
      cms.add(tag.getMetadata());
    }
    return cms;
  }

  public List<ExpressionTag> getExpressionTags() {
    return expressions;
  }

}
