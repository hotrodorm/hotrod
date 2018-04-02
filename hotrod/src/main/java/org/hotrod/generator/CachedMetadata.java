package org.hotrod.generator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.metadata.SelectMethodMetadata;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class CachedMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  private HotRodConfigTag config;
  private JdbcDatabase cachedDatabase;
  private Map<String, List<EnumConstant>> enumConstants;
  private Map<String, Map<String, SelectMethodMetadata>> selectMetadata; // dao-name,
                                                                         // method-name,
                                                                         // metadata

  // Constructor

  public CachedMetadata() {
    this.config = null;
    this.cachedDatabase = null;
    this.enumConstants = null;
    this.selectMetadata = null;
  }

  public CachedMetadata(final HotRodConfigTag config, final JdbcDatabase cachedDatabase,
      final Map<String, List<EnumConstant>> enumConstants,
      final Map<String, Map<String, SelectMethodMetadata>> selectMetadata) {
    this.config = config;
    this.cachedDatabase = cachedDatabase;
    this.enumConstants = enumConstants;
    this.selectMetadata = selectMetadata;
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

  public Map<String, Map<String, SelectMethodMetadata>> getSelectMetadata() {
    return selectMetadata;
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

  public void setSelectMetadata(final Map<String, Map<String, SelectMethodMetadata>> selectMetadata) {
    this.selectMetadata = selectMetadata;
  }

}
