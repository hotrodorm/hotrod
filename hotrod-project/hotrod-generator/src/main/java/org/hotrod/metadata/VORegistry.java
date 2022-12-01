package org.hotrod.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.metadata.VOMetadata.DuplicatePropertyNameException;
import org.hotrod.metadata.VOMetadata.VOMember;
import org.hotrod.metadata.VORegistry.VOProperty.EnclosingTagType;
import org.hotrod.utils.ClassPackage;

/*
 * <pre>
 * 
 * - table                                        -->  EntityVOClass
 * - view                                         -->  EntityVOClass
 * - select - non-graph vo      ::= solo VO       -->  SelectVOClass
 * - select - columns vo        ::= solo VO       -->  SelectVOClass
 * - select - inner vo          ::= connected VO  -->  SelectVOClass
 * 
 * </pre>
 */
public class VORegistry {

  // Constants

  private static final Logger log = LogManager.getLogger(VORegistry.class);

  // Properties

  private LinkedHashMap<ClassPackage, FragmentRegistry> fragmentsByPackage = new LinkedHashMap<ClassPackage, FragmentRegistry>();

  // Behavior

  // From <table> and <view>
  public void addVO(final EntityVOClass voClass) throws VOAlreadyExistsException, StructuredVOAlreadyExistsException {
    log.debug("add");
    ClassPackage classPackage = voClass.getClassPackage();
    FragmentRegistry f = this.fragmentsByPackage.get(classPackage);
    if (f == null) {
      f = new FragmentRegistry(classPackage);
      this.fragmentsByPackage.put(classPackage, f);
    }
    f.addVO(voClass);
  }

  // From <select> tags
  public void addVO(final SelectVOClass structuredVOClass)
      throws VOAlreadyExistsException, StructuredVOAlreadyExistsException {
    ClassPackage classPackage = structuredVOClass.getClassPackage();
    FragmentRegistry f = this.fragmentsByPackage.get(classPackage);
    if (f == null) {
      f = new FragmentRegistry(classPackage);
      this.fragmentsByPackage.put(classPackage, f);
    }
    f.addVO(structuredVOClass);
  }

  public EntityVOClass findEntityVOClass(final TableDataSetMetadata name) {
    for (FragmentRegistry f : this.fragmentsByPackage.values()) {
      EntityVOClass vo = f.findEntityVOClass(name);
      if (vo != null) {
        return vo;
      }
    }
    return null;
  }

  // Getters

  public List<FragmentRegistry> getFragments() {
    return new ArrayList<FragmentRegistry>(this.fragmentsByPackage.values());
  }

  // Classes

  public static class FragmentRegistry {

    // Properties

    private ClassPackage classPackage;
    private LinkedHashMap<DataSetMetadata, EntityVOClass> vosByMetadata;
    private LinkedHashMap<String, EntityVOClass> vosByName;
    private LinkedHashMap<String, SelectVOClass> structuredVOsByName;

    // Constructor

    public FragmentRegistry(final ClassPackage classPackage) {
      this.classPackage = classPackage;
      this.vosByMetadata = new LinkedHashMap<DataSetMetadata, EntityVOClass>();
      this.vosByName = new LinkedHashMap<String, EntityVOClass>();
      this.structuredVOsByName = new LinkedHashMap<String, SelectVOClass>();
    }

    // Behavior

    public void addVO(final EntityVOClass voClass) throws VOAlreadyExistsException, StructuredVOAlreadyExistsException {
      {
        EntityVOClass other = this.vosByName.get(voClass.getName());
        if (other != null) {
          throw new VOAlreadyExistsException(voClass, other);
        }

        if (voClass.getMetadata() != null) {
          other = this.vosByMetadata.get(voClass.getMetadata());
          if (other != null) {
            throw new VOAlreadyExistsException(voClass, other);
          }
        }
      }

      {
        SelectVOClass other = this.structuredVOsByName.get(voClass.getName());
        if (other != null) {
          throw new StructuredVOAlreadyExistsException(voClass, other);
        }
        if (voClass.getMetadata() != null) {
          this.vosByMetadata.put(voClass.getMetadata(), voClass);
        }
        this.vosByName.put(voClass.getName(), voClass);
      }
    }

    public void addVO(final SelectVOClass voClass) throws VOAlreadyExistsException, StructuredVOAlreadyExistsException {
      log.debug("[graph] fragment=" + this.classPackage.getPackage() + " voClass="
          + voClass.getClassPackage().getPackage() + " / " + voClass.getName() + " "
          + this.vosByName.containsKey(voClass.getName()) + "/"
          + this.structuredVOsByName.containsKey(voClass.getName()));

      {
        EntityVOClass other = this.vosByName.get(voClass.getName());
        if (other != null) {
          throw new VOAlreadyExistsException(voClass, other);
        }
      }

      {
        if (this.structuredVOsByName.containsKey(voClass.getName())) {
          SelectVOClass other = this.structuredVOsByName.get(voClass.getName());
          throw new StructuredVOAlreadyExistsException(voClass, other);
        }
        this.structuredVOsByName.put(voClass.getName(), voClass);
      }
    }

    public EntityVOClass findEntityVOClass(final TableDataSetMetadata name) {
      return this.vosByMetadata.get(name);
    }

    // Getters

    public ClassPackage getClassPackage() {
      return classPackage;
    }

    public List<EntityVOClass> getVOs() {
      return new ArrayList<EntityVOClass>(this.vosByName.values());
    }

    public List<SelectVOClass> getStructuredVOs() {
      return new ArrayList<SelectVOClass>(this.structuredVOsByName.values());
    }

  }

  public static class EntityVOClass implements Serializable {

    private static final long serialVersionUID = 1L;

    // Properties

    private DataSetMetadata metadata;
    private ClassPackage classPackage;
    private String name;
    private LinkedHashMap<String, ColumnMetadata> columnsByName;
    private AbstractConfigurationTag tag;

    // Constructor

    public EntityVOClass(final DataSetMetadata metadata, final ClassPackage classPackage, final String name,
        final List<ColumnMetadata> columns, final AbstractConfigurationTag tag) {
      this.tag = tag;
      if (this.tag == null) {
        throw new IllegalArgumentException("location cannot be null");
      }

      this.metadata = metadata;
      this.classPackage = classPackage;
      this.name = name;
      this.columnsByName = new LinkedHashMap<String, ColumnMetadata>();
      for (ColumnMetadata c : columns) {
        this.columnsByName.put(c.getColumnName(), c);
      }
    }

    // Behavior

    public boolean equals(final EntityVOClass o) {
      if (o == null) {
        return false;
      }
      EntityVOClass other;
      try {
        other = (EntityVOClass) o;
      } catch (ClassCastException e) {
        return false;
      }
      if (!this.classPackage.equals(other.classPackage)) {
        return false;
      }
      if (!this.name.equals(other.name)) {
        return false;
      }

      // compare columns

      for (String name : this.columnsByName.keySet()) {
        ColumnMetadata c1 = this.columnsByName.get(name);
        ColumnMetadata c2 = other.columnsByName.get(name);
        if (c2 == null) {
          return false;
        }
        if (!equivalentColumns(c1, c2)) {
          return false;
        }
      }

      for (String name : other.columnsByName.keySet()) {
        ColumnMetadata c1 = other.columnsByName.get(name);
        ColumnMetadata c2 = this.columnsByName.get(name);
        if (c2 == null) {
          return false;
        }
        if (!equivalentColumns(c1, c2)) {
          return false;
        }
      }

      // all comparisons cleared

      return true;
    }

    // toString

    public String toString() {
      return "{VOClass: " + (this.classPackage == null ? "<no-package>" : this.classPackage.getPackage()) + " / "
          + this.name + "}";
    }

    // Getters

    public DataSetMetadata getMetadata() {
      return metadata;
    }

    public ClassPackage getClassPackage() {
      return classPackage;
    }

    public String getName() {
      return name;
    }

    public LinkedHashMap<String, ColumnMetadata> getColumnsByName() {
      return columnsByName;
    }

    public String getFullClassName() {
      return this.classPackage.getFullClassName(this.name);
    }

    public AbstractConfigurationTag getTag() {
      return this.tag;
    }

  }

  public static class SelectVOClass implements Serializable {

    private static final long serialVersionUID = 1L;

    // Properties

    private ClassPackage classPackage;
    private String name;
    private EntityVOClass extendsEntityVO;
    private LinkedHashMap<String, StructuredColumnMetadata> columnsByName;
    private List<VOMember> associations;
    private List<VOMember> collections;
    private AbstractConfigurationTag tag;

    // Constructor

    public SelectVOClass(final ClassPackage classPackage, final String name, final EntityVOClass extendsEntityVO,
        final List<VOProperty> properties, final List<VOMember> associations, final List<VOMember> collections,
        final AbstractConfigurationTag tag) throws DuplicatePropertyNameException {

      this.tag = tag;
      if (this.tag == null) {
        throw new IllegalArgumentException("tag cannot be null");
      }

      VOPropertiesRegistry reg = new VOPropertiesRegistry(name);

      this.classPackage = classPackage;
      this.name = name;
      this.extendsEntityVO = extendsEntityVO;

      this.columnsByName = new LinkedHashMap<String, StructuredColumnMetadata>();
      for (VOProperty p : properties) {
        reg.add(p);
        StructuredColumnMetadata cm = p.getMetadata();
        if (cm != null) {
          this.columnsByName.put(cm.getColumnName(), cm);
        }
      }

      this.associations = associations;
      for (VOMember a : this.associations) {
        reg.add(new VOProperty(a.getProperty(), null, EnclosingTagType.ASSOCIATION, a.getTag()));
      }

      this.collections = collections;
      for (VOMember c : this.collections) {
        reg.add(new VOProperty(c.getProperty(), null, EnclosingTagType.COLLECTION, c.getTag()));
      }

    }

    // Behavior

    public boolean equals(final Object o) {
      if (o == null) {
        return false;
      }
      SelectVOClass other;
      try {
        other = (SelectVOClass) o;
      } catch (ClassCastException e) {
        return false;
      }
      if (!this.classPackage.equals(other.classPackage)) {
        return false;
      }
      if (!this.name.equals(other.name)) {
        return false;
      }
      if (this.extendsEntityVO == null) {
        if (other.extendsEntityVO != null) {
          return false;
        }
      } else {
        if (!this.extendsEntityVO.equals(other.extendsEntityVO)) {
          return false;
        }
      }

      // compare columns

      for (String name : this.columnsByName.keySet()) {
        StructuredColumnMetadata c1 = this.columnsByName.get(name);
        StructuredColumnMetadata c2 = other.columnsByName.get(name);
        if (c2 == null) {
          return false;
        }
        if (!equivalentColumns(c1, c2)) {
          return false;
        }
      }

      for (String name : other.columnsByName.keySet()) {
        StructuredColumnMetadata c1 = other.columnsByName.get(name);
        StructuredColumnMetadata c2 = this.columnsByName.get(name);
        if (c2 == null) {
          return false;
        }
        if (!equivalentColumns(c1, c2)) {
          return false;
        }
      }

      // all comparisons cleared

      return true;
    }

    // Getters

    public ClassPackage getClassPackage() {
      return classPackage;
    }

    public String getName() {
      return name;
    }

    public EntityVOClass getExtendsEntityVO() {
      return extendsEntityVO;
    }

    public LinkedHashMap<String, StructuredColumnMetadata> getColumnsByName() {
      return columnsByName;
    }

    public List<VOMember> getAssociations() {
      return associations;
    }

    public AbstractConfigurationTag getTag() {
      return this.tag;
    }

  }

  public static class VOPropertiesRegistry {

    private String name;
    private List<VOProperty> properties = new ArrayList<VOProperty>();

    public VOPropertiesRegistry(final String name) {
      this.name = name;
      log.debug("--- Properties '" + this.name + "' ---");
    }

    public void add(final VOProperty property) throws DuplicatePropertyNameException {
      log.debug(" " + this.name + " [" + this.properties.size() + "] + " + property);
      for (VOProperty existing : this.properties) {
        if (existing.getName().equals(property.getName())) {
          throw new DuplicatePropertyNameException(property, existing);
        }
      }
      this.properties.add(property);
    }

  }

  public static class VOProperty {

    public static enum EnclosingTagType {
      NON_STRUCTURED_SELECT, ENTITY_VO, EXPRESSIONS, ASSOCIATION, COLLECTION
    };

    private String name;
    private StructuredColumnMetadata cm;
    private EnclosingTagType sourceTagType;
    private AbstractConfigurationTag tag;

    public VOProperty(final String name, final StructuredColumnMetadata cm, final EnclosingTagType sourceTagType,
        final AbstractConfigurationTag tag) {
      this.name = name;
      this.cm = cm;
      this.sourceTagType = sourceTagType;
      this.tag = tag;
    }

    public String getName() {
      return name;
    }

    public StructuredColumnMetadata getMetadata() {
      return cm;
    }

    public EnclosingTagType getSourceTagType() {
      return sourceTagType;
    }

    public AbstractConfigurationTag getTag() {
      return this.tag;
    }

    public String toString() {
      return "{name=" + this.name + ", SQL-name=" + (this.cm == null ? "null" : cm.getColumnName()) + ", tag-type="
          + this.sourceTagType + "}";
    }

  }

  // Utilities

  public static boolean equivalentColumns(final ColumnMetadata c1, final ColumnMetadata c2) {
    if (!c1.getColumnName().equals(c2.getColumnName())) {
      return false;
    }
    if (c1.getDataType() != c2.getDataType()) {
      return false;
    }
    if (!c1.getType().getJavaClassName().equals(c2.getType().getJavaClassName())) {
      return false;
    }
    return true;
  }

  // Exceptions

  public static class VOAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    private EntityVOClass thisOne;
    private SelectVOClass thisOneSt;
    private EntityVOClass otherOne;

    public VOAlreadyExistsException(EntityVOClass thisOne, final EntityVOClass otherOne) {
      super();
      this.thisOne = thisOne;
      this.thisOneSt = null;
      this.otherOne = otherOne;
    }

    public VOAlreadyExistsException(SelectVOClass thisOneSt, final EntityVOClass otherOne) {
      super();
      this.thisOne = null;
      this.thisOneSt = thisOneSt;
      this.otherOne = otherOne;
    }

    public EntityVOClass getOtherOne() {
      return otherOne;
    }

    public AbstractConfigurationTag getTag() {
      return this.thisOne != null ? this.thisOne.getTag() : this.thisOneSt.getTag();
    }

    public String getThisName() {
      return this.thisOne != null ? this.thisOne.getName() : this.thisOneSt.getName();
    }

    public ClassPackage getThisPackage() {
      return this.thisOne != null ? this.thisOne.getClassPackage() : this.thisOneSt.getClassPackage();
    }

  }

  public static class StructuredVOAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    private EntityVOClass thisOne;
    private SelectVOClass thisOneSt;
    private SelectVOClass otherOne;

    public StructuredVOAlreadyExistsException(final EntityVOClass thisOne, final SelectVOClass otherOne) {
      super();
      this.thisOne = thisOne;
      this.thisOneSt = null;
      this.otherOne = otherOne;
    }

    public StructuredVOAlreadyExistsException(final SelectVOClass thisOneSt, final SelectVOClass otherOne) {
      super();
      this.thisOne = null;
      this.thisOneSt = thisOneSt;
      this.otherOne = otherOne;
    }

    public SelectVOClass getOtherOne() {
      return otherOne;
    }

    public AbstractConfigurationTag getThisTag() {
      return this.thisOne != null ? this.thisOne.getTag() : this.thisOneSt.getTag();
    }

    public String getThisName() {
      return this.thisOne != null ? this.thisOne.getName() : this.thisOneSt.getName();
    }

    public ClassPackage getThisPackage() {
      return this.thisOne != null ? this.thisOne.getClassPackage() : this.thisOneSt.getClassPackage();
    }

  }

}
