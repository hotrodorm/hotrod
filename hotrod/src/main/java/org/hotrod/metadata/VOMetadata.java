package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.MyBatisTag;
import org.hotrod.config.structuredcolumns.AssociationTag;
import org.hotrod.config.structuredcolumns.CollectionTag;
import org.hotrod.config.structuredcolumns.ExpressionsTag;
import org.hotrod.config.structuredcolumns.VOTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.generator.mybatis.SelectAbstractVO;
import org.hotrod.generator.mybatis.SelectVO;
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
  private String alias;
  private String property;

  private List<StructuredColumnMetadata> inheritedColumns;
  private List<StructuredColumnMetadata> declaredColumns;
  private List<VOMember> associationMembers;
  private List<VOMember> collectionMembers;

  // Constructors

  public VOMetadata(final VOTag tag, final DataSetLayout layout, final HotRodFragmentConfigTag fragmentConfig,
      final DaosTag daosTag) throws InvalidConfigurationFileException {
    log.debug("init");

    this.tableMetadata = tag.getTableMetadata();
    this.viewMetadata = tag.getViewMetadata();
    this.inheritedColumns = tag.getInheritedColumns();
    this.declaredColumns = tag.getDeclaredColumns();
    this.alias = tag.getAlias();
    this.property = tag.getProperty();

    this.expressions = new ArrayList<ExpressionsMetadata>();
    for (ExpressionsTag e : tag.getExpressions()) {
      this.expressions.add(e.getExpressionsMetadata());
    }

    this.associations = new ArrayList<VOMetadata>();
    this.associationMembers = new ArrayList<VOMember>();
    for (AssociationTag a : tag.getAssociations()) {
      VOMetadata am = a.getMetadata(layout, fragmentConfig, daosTag);
      this.associations.add(am);
      VOMember m = new VOMember(a.getProperty(), am.getClassPackage(), am.getName());
      this.associationMembers.add(m);
    }

    this.collections = new ArrayList<VOMetadata>();
    this.collectionMembers = new ArrayList<VOMember>();
    for (CollectionTag c : tag.getCollections()) {
      VOMetadata com = c.getMetadata(layout, fragmentConfig, daosTag);
      this.collections.add(com);
      VOMember m = new VOMember(c.getProperty(), com.getClassPackage(), com.getName());
      this.collectionMembers.add(m);
    }

    // package & class name

    if (tag.getExtendedVO() != null) { // extended VO from a table or view
      this.classPackage = getVOClassPackage(layout, fragmentConfig);
      this.name = tag.getExtendedVO();
      // log.info("[EXTENDED VO]: " +
      // this.classPackage.getFullClassName(this.name));
      this.superClass = tag.getGenerator().getVORegistry()
          .findVOClass(this.tableMetadata != null ? this.tableMetadata : this.viewMetadata);

      // log.info("vo=" + this.name + " this.superClass=" + this.superClass);

      if (this.superClass == null) {
        throw new InvalidConfigurationFileException(tag.getSourceLocation(),
            "Invalid 'extended-vo' attribute with value '" + tag.getExtendedVO()
                + "'. There's no table or view VO with that name.");
      }
    } else { // it's a table or view VO
      this.superClass = null;
      if (this.tableMetadata != null) {
        this.classPackage = getVOClassPackage(layout, this.tableMetadata.getFragmentConfig());
        this.name = daosTag.generateVOName(this.tableMetadata.getIdentifier());
        // log.info("[TABLE VO]: " +
        // this.classPackage.getFullClassName(this.name));
      } else {
        this.classPackage = getVOClassPackage(layout, this.viewMetadata.getFragmentConfig());
        this.name = daosTag.generateVOName(this.viewMetadata.getIdentifier());
        // log.info("[VIEW VO]: " +
        // this.classPackage.getFullClassName(this.name));
      }
    }

  }

  // Registration

  public void register(final Set<SelectAbstractVO> abstractSelectVOs, final Set<SelectVO> selectVOs,
      final DataSetLayout layout, final MyBatisTag myBatisTag) {
    if (this.superClass != null) {
      SelectAbstractVO abstractVO = new SelectAbstractVO(this, layout, myBatisTag);
      abstractSelectVOs.add(abstractVO);
      selectVOs.add(new SelectVO(this, abstractVO, layout));
    }
    for (VOMetadata vo : this.associations) {
      vo.register(abstractSelectVOs, selectVOs, layout, myBatisTag);
    }
    for (VOMetadata vo : this.collections) {
      vo.register(abstractSelectVOs, selectVOs, layout, myBatisTag);
    }
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((classPackage == null) ? 0 : classPackage.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    VOMetadata other = (VOMetadata) obj;
    if (classPackage == null) {
      if (other.classPackage != null)
        return false;
    } else if (!classPackage.equals(other.classPackage))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  // Utilities

  private ClassPackage getVOClassPackage(final DataSetLayout layout, final HotRodFragmentConfigTag fragmentConfig) {
    ClassPackage fragmentPackage = fragmentConfig != null && fragmentConfig.getFragmentPackage() != null
        ? fragmentConfig.getFragmentPackage() : null;
    return layout.getDAOPackage(fragmentPackage);
  }

  // toString

  public String toString() {
    return "{VOMetadata: " + (this.classPackage == null ? "<no-package>" : this.classPackage.getPackage()) + " / "
        + this.name + "}";
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

  public List<StructuredColumnMetadata> getInheritedColumns() {
    return inheritedColumns;
  }

  public List<StructuredColumnMetadata> getDeclaredColumns() {
    return declaredColumns;
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

  public List<VOMember> getAssociationMembers() {
    return associationMembers;
  }

  public List<VOMember> getCollectionMembers() {
    return collectionMembers;
  }

  public String getFullClassName() {
    return this.classPackage.getFullClassName(this.name);
  }

  // Classes

  public static class VOMember {

    private String property;
    private ClassPackage classPackage;
    private String name;

    public VOMember(final String property, final ClassPackage classPackage, final String name) {
      this.property = property;
      this.classPackage = classPackage;
      this.name = name;
    }

    public String getProperty() {
      return property;
    }

    public ClassPackage getClassPackage() {
      return classPackage;
    }

    public String getName() {
      return name;
    }

    public String getFullClassName() {
      return this.classPackage.getFullClassName(this.name);
    }

    public String renderFullClassName() {
      return this.classPackage.getFullClassName(this.name);
    }

  }

}
