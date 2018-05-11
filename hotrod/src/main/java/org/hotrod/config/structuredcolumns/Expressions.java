package org.hotrod.config.structuredcolumns;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter.UnescapedSQLCase;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.ColumnsMetadataRetriever;
import org.hotrod.utils.ColumnsPrefixGenerator;

public class Expressions implements ColumnsProvider, Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(Expressions.class);

  // Properties

  private List<ExpressionTag> expressions;

  protected transient ColumnsMetadataRetriever columnsRetriever;
  private transient HotRodGenerator generator;

  // Constructor

  public Expressions() {
    this.expressions = new ArrayList<ExpressionTag>();
    this.columnsRetriever = null;
    this.generator = null;
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

  @Override
  public void validateAgainstDatabase(HotRodGenerator generator) throws InvalidConfigurationFileException {
    this.generator = generator;
  }

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException {
    log.debug("this=" + this + " - this.expressions.isEmpty()=" + this.expressions.isEmpty());
    if (this.expressions.isEmpty()) {
      this.columnsRetriever = null;
    } else {
      this.columnsRetriever = new ColumnsMetadataRetriever(selectTag, this.generator.getAdapter(),
          this.generator.getJdbcDatabase(), this.generator.getLoc(), selectGenerationTag, this, null,
          columnsPrefixGenerator);
      this.columnsRetriever.prepareRetrieval(conn1);
    }
  }

  private Map<String, ExpressionTag> expressionsByAlias;

  @Override
  public String renderColumns() {
    this.expressionsByAlias = new HashMap<String, ExpressionTag>();
    String aliasPrefix = this.generator.getAdapter().getUnescapedSQLCase() == UnescapedSQLCase.UPPER_CASE ? "EXPR_"
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
  public void gatherMetadataPhase2(final Connection conn2) throws InvalidSQLException, UncontrolledException,
      UnresolvableDataTypeException, ControlledException, InvalidConfigurationFileException {
    log.debug("this.columnsRetriever=" + this.columnsRetriever);
    if (this.columnsRetriever != null) {
      List<StructuredColumnMetadata> cms = this.columnsRetriever.retrieve(conn2);
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
        cm = StructuredColumnMetadata.applyColumnTag(cm, ct, tag);
        cm.setId(tag.isId());
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
