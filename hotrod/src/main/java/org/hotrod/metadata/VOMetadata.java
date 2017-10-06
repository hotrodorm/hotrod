package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.config.sqlcolumns.ExpressionsTag;
import org.hotrod.config.sqlcolumns.VOTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.utils.identifiers.Identifier;

public class VOMetadata {

  private PrefixGenerator prefixGenerator;
  private DatabaseAdapter adapter;

  private Identifier identifier;
  private String extendedVoClass;

  private List<StructuredColumnMetadata> columns;
  private List<ExpressionsMetadata> expressions;
  private List<VOMetadata> associations;
  private List<VOMetadata> collections;

  // public VOMetadata(final TableDataSetMetadata tm, final String voClass,
  // final PrefixGenerator prefixGenerator,
  // final DatabaseAdapter adapter) {

  public VOMetadata(final VOTag tag, final HotRodGenerator generator, final PrefixGenerator prefixGenerator,
      final DatabaseAdapter adapter) {

    TableDataSetMetadata dm = tag.getTable() != null ? generator.findTableMetadata(tag.getTable())
        : generator.findViewMetadata(tag.getView());

    this.prefixGenerator = prefixGenerator;
    this.adapter = adapter;

    this.identifier = dm.getIdentifier();
    this.extendedVoClass = tag.getExtendedVOClass();

    String prefix = prefixGenerator.next();
    this.columns = new ArrayList<StructuredColumnMetadata>();
    for (ColumnMetadata cm : dm.getColumns()) {
      String alias = prefix + cm.getIdentifier().getSQLIdentifier();
      this.columns.add(new StructuredColumnMetadata(cm, alias));
    }

    this.expressions = new ArrayList<ExpressionsMetadata>();
    for (ExpressionsTag et : tag.getExpressions()) {
      ExpressionsMetadata em = new ExpressionsMetadata(et);
      this.expressions.add(em);
    }

    // TODO associations & collections

  }

  public String renderSQLColumns() {
    // TODO:
    return null;
  }

  public List<StructuredColumnMetadata> getColumns() {
    return this.columns;
  }

}
