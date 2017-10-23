package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.structuredcolumns.AssociationTag;
import org.hotrod.config.structuredcolumns.CollectionTag;
import org.hotrod.config.structuredcolumns.ExpressionsTag;
import org.hotrod.config.structuredcolumns.VOTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.metadata.VORegistry.VOClass;
import org.hotrod.utils.ClassPackage;

public class VOMetadata {

  // Constants

  private static final Logger log = Logger.getLogger(VOMetadata.class);

  // Properties

  private ClassPackage classPackage;
  private String name;
  private VOClass superClass;

  private List<ExpressionsMetadata> expressions;
  private List<VOMetadata> associations;
  private List<VOMetadata> collections;

  private TableDataSetMetadata tableMetadata;
  private TableDataSetMetadata viewMetadata;
  private List<StructuredColumnMetadata> columns;
  private String alias;
  private String property;

  // Constructors

  public VOMetadata(final VOTag tag, final DataSetLayout layout, final HotRodFragmentConfigTag fragmentConfig,
      final DaosTag daosTag) throws InvalidConfigurationFileException {
    log.debug("init");

    this.tableMetadata = tag.getTableMetadata();
    this.viewMetadata = tag.getViewMetadata();
    this.columns = tag.getColumns();
    this.alias = tag.getAlias();
    this.property = tag.getProperty();

    this.expressions = new ArrayList<ExpressionsMetadata>();
    for (ExpressionsTag e : tag.getExpressions()) {
      this.expressions.add(e.getExpressionsMetadata());
    }

    this.associations = new ArrayList<VOMetadata>();
    for (AssociationTag a : tag.getAssociations()) {
      this.associations.add(a.getMetadata(layout, fragmentConfig, daosTag));
    }

    this.collections = new ArrayList<VOMetadata>();
    for (CollectionTag c : tag.getCollections()) {
      this.collections.add(c.getMetadata(layout, fragmentConfig, daosTag));
    }

    // package & class name

    if (tag.getExtendedVO() != null) { // extended VO from a table or view
      this.classPackage = getClassPackage(layout, fragmentConfig);
      this.name = tag.getExtendedVO();
      this.superClass = tag.getGenerator().getVORegistry()
          .findVOClass(this.tableMetadata != null ? this.tableMetadata : this.viewMetadata);
      if (this.superClass == null) {
        throw new InvalidConfigurationFileException(tag.getSourceLocation(),
            "Invalid 'extended-vo' attribute with value '" + tag.getExtendedVO()
                + "'. There's no table or view VO with that name.");
      }
    } else { // it's a table or view VO
      this.superClass = null;
      if (this.tableMetadata != null) {
        this.classPackage = getClassPackage(layout, this.tableMetadata.getFragmentConfig());
        this.name = daosTag.generateVOName(this.tableMetadata.getIdentifier());
      } else {
        this.classPackage = getClassPackage(layout, this.viewMetadata.getFragmentConfig());
        this.name = daosTag.generateVOName(this.viewMetadata.getIdentifier());
      }
    }

  }

  // Utilities

  private ClassPackage getClassPackage(final DataSetLayout layout, final HotRodFragmentConfigTag fragmentConfig) {
    ClassPackage fragmentPackage = fragmentConfig != null && fragmentConfig.getFragmentPackage() != null
        ? fragmentConfig.getFragmentPackage() : null;
    return layout.getDAOPrimitivePackage(fragmentPackage);
  }

  // Getters

  public List<ExpressionsMetadata> getExpressions() {
    return expressions;
  }

  public List<VOMetadata> getAssociations() {
    return associations;
  }

  public List<VOMetadata> getCollections() {
    return collections;
  }

  public TableDataSetMetadata getTableMetadata() {
    return tableMetadata;
  }

  public TableDataSetMetadata getViewMetadata() {
    return viewMetadata;
  }

  public List<StructuredColumnMetadata> getColumns() {
    return columns;
  }

  public String getAlias() {
    return alias;
  }

  public String getProperty() {
    return this.property;
  }

  public VOClass getSuperClass() {
    return this.superClass;
  }

  public ClassPackage getClassPackage() {
    return classPackage;
  }

  public String getName() {
    return name;
  }

}
