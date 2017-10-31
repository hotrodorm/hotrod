package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.metadata.VOMetadata.VOMember;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.utils.ClassPackage;

public class VORegistry {

  // Constants

  private static final Logger log = Logger.getLogger(VORegistry.class);

  // Properties

  private LinkedHashMap<ClassPackage, FragmentRegistry> fragmentsByPackage = new LinkedHashMap<ClassPackage, FragmentRegistry>();

  // Behavior

  public void addVO(final VOClass voClass) throws VOAlreadyExistsException, StructuredVOAlreadyExistsException {
    log.debug("add");
    ClassPackage classPackage = voClass.getClassPackage();
    FragmentRegistry f = this.fragmentsByPackage.get(classPackage);
    if (f == null) {
      f = new FragmentRegistry(classPackage);
      this.fragmentsByPackage.put(classPackage, f);
    }
    f.addVO(voClass);
  }

  public void addVO(final StructuredVOClass structuredVOClass)
      throws VOAlreadyExistsException, StructuredVOAlreadyExistsException {
    ClassPackage classPackage = structuredVOClass.getClassPackage();
    FragmentRegistry f = this.fragmentsByPackage.get(classPackage);
    if (f == null) {
      f = new FragmentRegistry(classPackage);
      this.fragmentsByPackage.put(classPackage, f);
    }
    f.addVO(structuredVOClass);
  }

  public VOClass findVOClass(final TableDataSetMetadata name) {
    for (FragmentRegistry f : this.fragmentsByPackage.values()) {
      VOClass vo = f.findVOClass(name);
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
    private LinkedHashMap<DataSetMetadata, VOClass> vosByMetadata;
    private LinkedHashMap<String, VOClass> vosByName;
    private LinkedHashMap<String, StructuredVOClass> structuredVOsByName;

    // Constructor

    public FragmentRegistry(final ClassPackage classPackage) {
      this.classPackage = classPackage;
      this.vosByMetadata = new LinkedHashMap<DataSetMetadata, VOClass>();
      this.vosByName = new LinkedHashMap<String, VOClass>();
      this.structuredVOsByName = new LinkedHashMap<String, StructuredVOClass>();
    }

    // Behavior

    public void addVO(final VOClass voClass) throws VOAlreadyExistsException, StructuredVOAlreadyExistsException {
      {
        VOClass other = this.vosByName.get(voClass.getName());
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
        StructuredVOClass other = this.structuredVOsByName.get(voClass.getName());
        if (other != null) {
          throw new StructuredVOAlreadyExistsException(voClass, other);
        }
        if (voClass.getMetadata() != null) {
          this.vosByMetadata.put(voClass.getMetadata(), voClass);
        }
        this.vosByName.put(voClass.getName(), voClass);
      }
    }

    public void addVO(final StructuredVOClass voClass)
        throws VOAlreadyExistsException, StructuredVOAlreadyExistsException {
      log.info("[structured] fragment=" + this.classPackage.getPackage() + " voClass="
          + voClass.getClassPackage().getPackage() + " / " + voClass.getName() + " "
          + this.vosByName.containsKey(voClass.getName()) + "/"
          + this.structuredVOsByName.containsKey(voClass.getName()));

      {
        VOClass other = this.vosByName.get(voClass.getName());
        if (other != null) {
          throw new VOAlreadyExistsException(voClass, other);
        }
      }

      {
        if (this.structuredVOsByName.containsKey(voClass.getName())) {
          StructuredVOClass other = this.structuredVOsByName.get(voClass.getName());
          throw new StructuredVOAlreadyExistsException(voClass, other);
        }
        this.structuredVOsByName.put(voClass.getName(), voClass);
      }
    }

    public VOClass findVOClass(final TableDataSetMetadata name) {
      return this.vosByMetadata.get(name);
    }

    // Getters

    public ClassPackage getClassPackage() {
      return classPackage;
    }

    public List<VOClass> getVOs() {
      return new ArrayList<VOClass>(this.vosByName.values());
    }

    public List<StructuredVOClass> getStructuredVOs() {
      return new ArrayList<StructuredVOClass>(this.structuredVOsByName.values());
    }

  }

  public static class VOClass {

    // Properties

    private DataSetMetadata metadata;
    private ClassPackage classPackage;
    private String name;
    private LinkedHashMap<String, ColumnMetadata> columnsByName;
    private List<VOMember> associations;
    private SourceLocation location;

    // Constructor

    public VOClass(final DataSetMetadata metadata, final ClassPackage classPackage, final String name,
        final List<ColumnMetadata> columns, final SourceLocation location) {
      initialize(metadata, classPackage, name, columns, new ArrayList<VOMember>(), location);
    }

    public VOClass(final DataSetMetadata metadata, final ClassPackage classPackage, final String name,
        final List<ColumnMetadata> columns, final List<VOMember> associations, final SourceLocation location) {
      initialize(metadata, classPackage, name, columns, associations, location);
    }

    private void initialize(final DataSetMetadata metadata, final ClassPackage classPackage, final String name,
        final List<ColumnMetadata> columns, final List<VOMember> associations, SourceLocation location) {
      this.metadata = metadata;
      this.classPackage = classPackage;
      this.name = name;
      this.columnsByName = new LinkedHashMap<String, ColumnMetadata>();
      for (ColumnMetadata c : columns) {
        this.columnsByName.put(c.getColumnName(), c);
      }
      this.associations = associations;
      this.location = location;
      if (this.location == null) {
        throw new IllegalArgumentException("location cannot be null");
      }
    }

    // Behavior

    public boolean equals(final VOClass o) {
      if (o == null) {
        return false;
      }
      VOClass other;
      try {
        other = (VOClass) o;
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

    public List<VOMember> getAssociations() {
      return associations;
    }

    public String getFullClassName() {
      return this.classPackage.getFullClassName(this.name);
    }

    public SourceLocation getLocation() {
      return location;
    }

  }

  public static class StructuredVOClass {

    // Properties

    private ClassPackage classPackage;
    private String name;
    private VOClass extendsVO;
    private LinkedHashMap<String, StructuredColumnMetadata> columnsByName;
    private SourceLocation location;

    // Constructor

    public StructuredVOClass(final ClassPackage classPackage, final String name, final VOClass extendsVO,
        final List<StructuredColumnMetadata> columns, final SourceLocation location) {
      this.classPackage = classPackage;
      this.name = name;
      this.extendsVO = extendsVO;
      this.columnsByName = new LinkedHashMap<String, StructuredColumnMetadata>();
      for (StructuredColumnMetadata c : columns) {
        this.columnsByName.put(c.getColumnName(), c);
      }
      this.location = location;
      if (this.location == null) {
        throw new IllegalArgumentException("location cannot be null");
      }
    }

    // Behavior

    public boolean equals(final Object o) {
      if (o == null) {
        return false;
      }
      StructuredVOClass other;
      try {
        other = (StructuredVOClass) o;
      } catch (ClassCastException e) {
        return false;
      }
      if (!this.classPackage.equals(other.classPackage)) {
        return false;
      }
      if (!this.name.equals(other.name)) {
        return false;
      }
      if (this.extendsVO == null) {
        if (other.extendsVO != null) {
          return false;
        }
      } else {
        if (!this.extendsVO.equals(other.extendsVO)) {
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

    public VOClass getExtendsVO() {
      return extendsVO;
    }

    public LinkedHashMap<String, StructuredColumnMetadata> getColumnsByName() {
      return columnsByName;
    }

    public SourceLocation getLocation() {
      return location;
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

    private VOClass thisOne;
    private StructuredVOClass thisOneSt;
    private VOClass otherOne;

    public VOAlreadyExistsException(VOClass thisOne, final VOClass otherOne) {
      super();
      this.thisOne = thisOne;
      this.thisOneSt = null;
      this.otherOne = otherOne;
    }

    public VOAlreadyExistsException(StructuredVOClass thisOneSt, final VOClass otherOne) {
      super();
      this.thisOne = null;
      this.thisOneSt = thisOneSt;
      this.otherOne = otherOne;
    }

    public VOClass getOtherOne() {
      return otherOne;
    }

    public SourceLocation getThisLocation() {
      return this.thisOne != null ? this.thisOne.getLocation() : this.thisOneSt.getLocation();
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

    private VOClass thisOne;
    private StructuredVOClass thisOneSt;
    private StructuredVOClass otherOne;

    public StructuredVOAlreadyExistsException(final VOClass thisOne, final StructuredVOClass otherOne) {
      super();
      this.thisOne = thisOne;
      this.thisOneSt = null;
      this.otherOne = otherOne;
    }

    public StructuredVOAlreadyExistsException(final StructuredVOClass thisOneSt, final StructuredVOClass otherOne) {
      super();
      this.thisOne = null;
      this.thisOneSt = thisOneSt;
      this.otherOne = otherOne;
    }

    public StructuredVOClass getOtherOne() {
      return otherOne;
    }

    public SourceLocation getThisLocation() {
      return this.thisOne != null ? this.thisOne.getLocation() : this.thisOneSt.getLocation();
    }

    public String getThisName() {
      return this.thisOne != null ? this.thisOne.getName() : this.thisOneSt.getName();
    }

    public ClassPackage getThisPackage() {
      return this.thisOne != null ? this.thisOne.getClassPackage() : this.thisOneSt.getClassPackage();
    }

  }

}
