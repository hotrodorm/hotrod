package org.hotrod.metadata;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.EnumTag;
import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.EnumTag.EnumProperty;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.SelectMetadataCache;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class EnumDataSetMetadata extends TableDataSetMetadata {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(EnumDataSetMetadata.class);

  private EnumTag tag;

  // Constructor

  public EnumDataSetMetadata(final EnumTag tag, final JdbcTable t, final DatabaseAdapter adapter,
      final HotRodConfigTag config, final DataSetLayout layout, final SelectMetadataCache selectMetadataCache)
      throws UnresolvableDataTypeException, InvalidConfigurationFileException {
    super(tag, t, adapter, config, layout, selectMetadataCache);
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
    return this.tag.equals(other.tag);
  }

  // Getters

  public String getName() {
    return this.tag.getId().getCanonicalSQLName();
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

  public EnumProperty getValueColumn() {
    return this.tag.getValueColumn();
  }

  public List<EnumProperty> getProperties() {
    return this.tag.getProperties();
  }

}
