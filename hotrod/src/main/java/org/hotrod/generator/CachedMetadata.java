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

  public CachedMetadata(final HotRodConfigTag config, final JdbcDatabase cachedDatabase,
      final Map<String, List<EnumConstant>> enumConstants,
      final Map<String, Map<String, SelectMethodMetadata>> selectMetadata) {
    super();
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

}
