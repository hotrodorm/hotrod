package org.hotrod.metadata;

import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.config.EnumTag;
import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.EnumTag.EnumProperty;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class EnumDataSetMetadata extends TableDataSetMetadata {

  private static final Logger log = Logger.getLogger(EnumDataSetMetadata.class);

  private EnumTag tag;

  // Constructor

  public EnumDataSetMetadata(final EnumTag tag, final JdbcTable t, final DatabaseAdapter adapter,
      final HotRodConfigTag config) throws UnresolvableDataTypeException, ControlledException {
    super(tag, t, adapter, config);
    log.debug("init");
    this.tag = tag;
  }

  // Indexing methods

  @Override
  public int hashCode() {
    return this.tag.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EnumDataSetMetadata other = (EnumDataSetMetadata) obj;
    return this.tag.equals(other);
  }

  // Getters

  public String getName() {
    return this.tag.getName();
  }

  public String getJdbcName() {
    return this.tag.getJdbcName();
  }

  public String getJavaClassName() {
    return this.tag.getJavaClassName();
  }

  public List<EnumConstant> getEnumConstants() {
    return this.tag.getEnumConstants();
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return this.tag.getFragmentConfig();
  }

  public List<EnumProperty> getProperties() {
    return this.tag.getProperties();
  }

}
