package org.hotrod.metadata;

import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.EnumTag;
import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.EnumTag.ValueTypeManager;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.database.DatabaseAdapter;

public class EnumMetadata {

  private static final Logger log = Logger.getLogger(EnumMetadata.class);

  private EnumTag tag;
  @SuppressWarnings("unused")
  private HotRodConfigTag config;
  @SuppressWarnings("unused")
  private DatabaseAdapter adapter;

  // Constructor

  public EnumMetadata(final EnumTag tag, final DatabaseAdapter adapter, final HotRodConfigTag config) {

    log.debug("init");
    this.tag = tag;
    this.config = config;
    this.adapter = adapter;

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
    EnumMetadata other = (EnumMetadata) obj;
    return this.tag.equals(other);
  }

  // Getters

  public String getName() {
    return this.tag.getName();
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

  public ValueTypeManager<?> getValueTypeManager() {
    return this.tag.getValueTypeManager();
  }

}
