package org.hotrod.config;

import java.util.Map;

import org.hotrod.metadata.ColumnMetadata;
import org.nocrala.tools.database.tartarus.connectors.DatabaseConnector.ColumnNature;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class RetrievedColumn implements Comparable<RetrievedColumn> {

  private String catalog;
  private String schema;
  private String objectName;
  private String name;
  private String typeName;
  private Integer dataType;
  private Integer size;
  private Integer scale;
  private Object columnDefault;
  private AutogenerationType autogenerationType;
  private Boolean belongsToPK;
  private Boolean isVersionControlColumn;
  private ColumnNature nature;
  private Integer ordinal;
  private Boolean nullable;
  private Map<String, Object> nativeProperties;

  // Constructor

  public RetrievedColumn(final ColumnMetadata cm, final JdbcColumn c) {
    this.objectName = cm.getTable();
    this.name = cm.getName();
    this.typeName = cm.getTypeName();
    this.dataType = cm.getDataType();
    this.size = cm.getPrecision();
    this.scale = cm.getScale();
    this.columnDefault = cm.getColumnDefault();
    this.autogenerationType = cm.getAutogenerationType();
    this.belongsToPK = cm.belongsToPK();
    this.isVersionControlColumn = cm.isVersionControlColumn();
    if (c != null) {
      JdbcTable t = c.getTable();
      this.catalog = t == null ? null : t.getCatalog();
      this.schema = t == null ? null : t.getSchema();
      this.nature = c.getNature();
      this.ordinal = c.getOrdinalPosition();
      this.nullable = c.isNullable();
      this.nativeProperties = c.getNative();
    } else {
      this.catalog = null;
      this.schema = null;
      this.nature = null;
      this.ordinal = null;
      this.nullable = null;
      this.nativeProperties = null;
    }
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
    result = prime * result + ((ordinal == null) ? 0 : ordinal.hashCode());
    result = prime * result + ((schema == null) ? 0 : schema.hashCode());
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
    RetrievedColumn other = (RetrievedColumn) obj;
    if (catalog == null) {
      if (other.catalog != null)
        return false;
    } else if (!catalog.equals(other.catalog))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (objectName == null) {
      if (other.objectName != null)
        return false;
    } else if (!objectName.equals(other.objectName))
      return false;
    if (ordinal == null) {
      if (other.ordinal != null)
        return false;
    } else if (!ordinal.equals(other.ordinal))
      return false;
    if (schema == null) {
      if (other.schema != null)
        return false;
    } else if (!schema.equals(other.schema))
      return false;
    return true;
  }

  // Comparable

  @Override
  public int compareTo(final RetrievedColumn o) {
    int c;

    c = compareTo(this.catalog, o.catalog);
    if (c != 0) {
      return c;
    }

    c = compareTo(this.schema, o.schema);
    if (c != 0) {
      return c;
    }

    c = compareTo(this.objectName, o.objectName);
    if (c != 0) {
      return c;
    }

    c = compareTo(this.ordinal, o.ordinal);
    if (c != 0) {
      return c;
    }

    c = compareTo(this.name, o.name);
    return c;
  }

  private <T> int compareTo(final Comparable<T> a, final T b) {
    if (a == null) {
      return b == null ? 0 : -1;
    }
    if (b == null) {
      return 1;
    }
    return a.compareTo(b);
  }

  // Getters

  public String getCatalog() {
    return catalog;
  }

  public String getSchema() {
    return schema;
  }

  public String getObjectName() {
    return objectName;
  }

  public String getName() {
    return name;
  }

  public String getTypeName() {
    return typeName;
  }

  public Integer getDataType() {
    return dataType;
  }

  public Integer getSize() {
    return size;
  }

  public Integer getScale() {
    return scale;
  }

  public Object getDefault() {
    return columnDefault;
  }

  public String getAutogeneration() {
    return autogenerationType == null ? null : autogenerationType.name();
  }

  public Boolean getBelongsToPK() {
    return belongsToPK;
  }

  public Boolean getIsVersionControlColumn() {
    return isVersionControlColumn;
  }

  public ColumnNature getNature() {
    return nature;
  }

  public Integer getOrdinal() {
    return ordinal;
  }

  public Boolean getNullable() {
    return nullable;
  }

  public Map<String, Object> getNative() {
    return nativeProperties;
  }

}
