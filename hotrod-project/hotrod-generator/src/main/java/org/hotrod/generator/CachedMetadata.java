package org.hotrod.generator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.HotRodConfigTag;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class CachedMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  private HotRodConfigTag config;
  private JdbcDatabase cachedDatabase;
  private Map<String, List<EnumConstant>> enumConstants;
  private SelectMetadataCache selectMetadataCache;

  // Constructor

  public CachedMetadata() {
    this.config = null;
    this.cachedDatabase = null;
    this.enumConstants = null;
    this.selectMetadataCache = new SelectMetadataCache();
  }

  public CachedMetadata(final HotRodConfigTag config, final JdbcDatabase cachedDatabase,
      final Map<String, List<EnumConstant>> enumConstants, final SelectMetadataCache selectMetadataCache) {
    this.config = config;
    this.cachedDatabase = cachedDatabase;
    this.enumConstants = enumConstants;
    this.setSelectMetadataCache(selectMetadataCache);
  }

  // Getters

  public HotRodConfigTag getConfig() {
    return config;
  }

  public JdbcDatabase getCachedDatabase() {
    return cachedDatabase;
  }

  public Map<String, List<EnumConstant>> getEnumConstants() {
    return enumConstants;
  }

  public SelectMetadataCache getSelectMetadataCache() {
    return selectMetadataCache;
  }

  public void setConfig(final HotRodConfigTag config) {
    this.config = config;
  }

  public void setCachedDatabase(final JdbcDatabase cachedDatabase) {
    this.cachedDatabase = cachedDatabase;
  }

  public void setEnumConstants(final Map<String, List<EnumConstant>> enumConstants) {
    this.enumConstants = enumConstants;
  }

  public void setSelectMetadataCache(final SelectMetadataCache selectMetadataCache) {
    if (selectMetadataCache == null) {
      this.selectMetadataCache = new SelectMetadataCache();
    } else {
      this.selectMetadataCache = selectMetadataCache;
    }
  }

}
