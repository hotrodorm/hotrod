package org.hotrod.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.DaosSpringMyBatisTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.structuredcolumns.AssociationTag;
import org.hotrod.config.structuredcolumns.CollectionTag;
import org.hotrod.config.structuredcolumns.Expressions;
import org.hotrod.config.structuredcolumns.VOTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.identifiers.Id;
import org.hotrod.metadata.VORegistry.EntityVOClass;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOProperty;
import org.hotrod.metadata.VORegistry.VOProperty.EnclosingTagType;
import org.hotrod.utils.ClassPackage;

public class VOMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(VOMetadata.class);

  // Properties

  private VOTag tag;

  private ClassPackage classPackage;

  private String name;
  private String abstractName;

  private EntityVOClass entityVOSuperClass;

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
      final DaosSpringMyBatisTag daosTag) throws InvalidConfigurationFileException {
    log.debug("init");

    this.tag = tag;

    this.tableMetadata = tag.getTableMetadata();
    this.viewMetadata = tag.getViewMetadata();
    this.inheritedColumns = tag.getInheritedColumns();
    this.declaredColumns = tag.getDeclaredColumns();
    log.debug("DEC COLUMNS=" + this.declaredColumns.size());
    this.alias = tag.getAlias();
    this.property = tag.getProperty();

    this.associations = new ArrayList<VOMetadata>();
    this.associationMembers = new ArrayList<VOMember>();
    for (AssociationTag a : tag.getAssociations()) {
      VOMetadata am = a.getMetadata(layout, fragmentConfig, daosTag);
      this.associations.add(am);
      VOMember m;
      try {
        m = new VOMember(a.getProperty(), am.getClassPackage(), am.getName(), a);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid property name '" + a.getProperty() + "': " + e.getMessage();
        throw new InvalidConfigurationFileException(tag, msg);
      }
      this.associationMembers.add(m);
    }

    this.collections = new ArrayList<VOMetadata>();
    this.collectionMembers = new ArrayList<VOMember>();
    for (CollectionTag c : tag.getCollections()) {
      VOMetadata com = c.getMetadata(layout, fragmentConfig, daosTag);
      this.collections.add(com);
      VOMember m;
      try {
        m = new VOMember(c.getProperty(), com.getClassPackage(), com.getName(), c);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid property name '" + c.getProperty() + "': " + e.getMessage();
        throw new InvalidConfigurationFileException(tag, msg);
      }
      this.collectionMembers.add(m);
    }

    // package & class name

    if (tag.getExtendedVO() != null) { // extended VO from a table or view
      this.classPackage = getVOClassPackage(layout, fragmentConfig);
      this.name = daosTag.generateNitroVOName(tag.getExtendedVO());
      this.abstractName = daosTag.generateNitroAbstractVOName(tag.getExtendedVO());
      this.entityVOSuperClass = tag.getVORegistry()
          .findEntityVOClass(this.tableMetadata != null ? this.tableMetadata : this.viewMetadata);
      if (this.entityVOSuperClass == null) {
        throw new InvalidConfigurationFileException(tag, "Invalid 'extended-vo' attribute with value '"
            + tag.getExtendedVO() + "'. There's no table or view VO with that name.");
      }
    } else { // it's a table or view VO
      this.entityVOSuperClass = null;
      if (this.tableMetadata != null) {
        this.classPackage = getVOClassPackage(layout, this.tableMetadata.getFragmentConfig());
        this.name = daosTag.generateVOName(this.tableMetadata.getId());
        this.abstractName = daosTag.generateAbstractVOName(this.tableMetadata.getId());
      } else {
        this.classPackage = getVOClassPackage(layout, this.viewMetadata.getFragmentConfig());
        this.name = daosTag.generateVOName(this.viewMetadata.getId());
        this.abstractName = daosTag.generateAbstractVOName(this.viewMetadata.getId());
      }
    }

  }

  // Registration

  public EntityVOClass getEntityVOSuperClass() {
    return entityVOSuperClass;
  }

  public void registerSubTreeVOs(final ClassPackage classPackage, final VORegistry voRegistry)
      throws VOAlreadyExistsException, StructuredVOAlreadyExistsException, DuplicatePropertyNameException,
      InvalidConfigurationFileException {

    if (this.entityVOSuperClass != null) { // new VO (extends an entity VO)

      List<VOProperty> properties = new ArrayList<VOProperty>();

      // Entity VO properties

      for (ColumnMetadata cm : this.entityVOSuperClass.getColumnsByName().values()) {
        StructuredColumnMetadata m = new StructuredColumnMetadata(cm, "entityPrefix3", "columnAlias", false, this.tag);
        if (this.tag.getAlias() != null) {
          properties.add(new VOProperty(cm.getId().getJavaMemberName(), m, EnclosingTagType.ENTITY_VO,
              this.entityVOSuperClass.getTag()));
        }
      }

      // Expressions properties

      for (StructuredColumnMetadata cm : this.tag.getExpressions().getMetadata()) {
        properties.add(new VOProperty(cm.getId().getJavaMemberName(), cm, EnclosingTagType.EXPRESSIONS, cm.getTag()));
      }

      // Association properties

      List<VOMember> associationMembers = new ArrayList<VOMember>();
      for (VOMetadata vo : this.associations) {
        try {
          associationMembers.add(new VOMember(vo.getProperty(), classPackage, vo.getProperty(), vo.getTag()));
        } catch (InvalidIdentifierException e) {
          throw new InvalidConfigurationFileException(this.getTag(),
              "Invalid property '" + vo.getProperty() + "': " + e.getMessage());
        }
      }

      // Collection properties

      List<VOMember> collectionMembers = new ArrayList<VOMember>();
      for (VOMetadata vo : this.collections) {
        try {
          collectionMembers.add(new VOMember(vo.getProperty(), classPackage, vo.getProperty(), vo.getTag()));
        } catch (InvalidIdentifierException e) {
          throw new InvalidConfigurationFileException(this.getTag(),
              "Invalid property '" + vo.getProperty() + "': " + e.getMessage());
        }
      }

      SelectVOClass voClass = new SelectVOClass(classPackage, this.name, this.entityVOSuperClass, null, properties,
          associationMembers, collectionMembers, this.tag);
      voRegistry.addVO(voClass);

    }

    // Register sub tree

    for (VOMetadata vo : this.associations) {
      vo.registerSubTreeVOs(classPackage, voRegistry);
    }
    for (VOMetadata vo : this.collections) {
      vo.registerSubTreeVOs(classPackage, voRegistry);
    }

  }

  public static class DuplicatePropertyNameException extends Exception {

    private static final long serialVersionUID = 1L;

    private VOProperty duplicate;
    private VOProperty initial;

    public DuplicatePropertyNameException(final VOProperty duplicate, final VOProperty initial) {
      this.duplicate = duplicate;
      this.initial = initial;
    }

    public static long getSerialversionuid() {
      return serialVersionUID;
    }

    public VOProperty getDuplicate() {
      return duplicate;
    }

    public VOProperty getInitial() {
      return initial;
    }

    public String renderMessage() {
      String existing;
      switch (this.initial.getSourceTagType()) {
      case NON_STRUCTURED_SELECT:
        existing = "This property name is already being used by a property (related to a database column) of the non-graph select in "
            + this.initial.getTag().getSourceLocation().render();
        break;
      case ENTITY_VO:
        existing = "This property name is already being used by a property (column) of the entity VO it is subclassing defined in "
            + this.initial.getTag().getSourceLocation().render();
        break;
      case EXPRESSIONS:
        existing = "This property name is also being used by a property of an expression defined in "
            + this.initial.getTag().getSourceLocation().render();
        break;
      case ASSOCIATION:
        existing = "This property name is also being used by the property name of an association defined in "
            + this.initial.getTag().getSourceLocation().render();
        break;
      case COLLECTION:
        existing = "This property name is also being used by the property name of a collection defined in "
            + this.initial.getTag().getSourceLocation().render();
        break;
      default:
        existing = "<undefined>";
        break;
      }
      return "Duplicate VO property '" + this.duplicate.getName() + "'. " + existing + ".";
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
        ? fragmentConfig.getFragmentPackage()
        : null;
    return layout.getDAOPackage(fragmentPackage);
  }

  // toString

  public String toString() {
    return "{VOMetadata: " + (this.classPackage == null ? "<no-package>" : this.classPackage.getPackage()) + " / "
        + this.name + "}";
  }

  public AbstractConfigurationTag getTag() {
    return this.tag;
  }

  // Getters

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

  public EntityVOClass getSuperClass() {
    return this.entityVOSuperClass;
  }

  public ClassPackage getClassPackage() {
    return classPackage;
  }

  public String getName() {
    return name;
  }

  public String getAbstractName() {
    return abstractName;
  }

  public Expressions getExpressions() {
    return this.tag.getExpressions();
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

  public static class VOMember implements Serializable {

    private static final long serialVersionUID = 1L;

    private String property;
    private ClassPackage classPackage;
    private String name;
    private AbstractConfigurationTag tag;

    private Id id;

    public VOMember(final String property, final ClassPackage classPackage, final String name,
        final AbstractConfigurationTag tag) throws InvalidIdentifierException {
      this.property = property;
      this.classPackage = classPackage;
      this.name = name;
      this.tag = tag;
      this.id = Id.fromJavaMember(this.property);
    }

    public String getProperty() {
      return property;
    }

    public Id getId() {
      return id;
    }

    public ClassPackage getClassPackage() {
      return classPackage;
    }

    public String getName() {
      return name;
    }

    public AbstractConfigurationTag getTag() {
      return this.tag;
    }

    public String getFullClassName() {
      return this.classPackage.getFullClassName(this.name);
    }

    public String renderFullClassName() {
      return this.classPackage.getFullClassName(this.name);
    }

  }

}
