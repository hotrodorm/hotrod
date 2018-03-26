package org.hotrod.generator;

import java.util.List;
import java.util.Map;

import org.hotrod.config.EnumTag.EnumConstant;
import org.hotrod.metadata.SelectMethodMetadata;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class CachedMetadata {

  private JdbcDatabase cachedDatabase;
  private Map<String, List<EnumConstant>> enumConstants;
  private Map<String, Map<String, SelectMethodMetadata>> selectMetadata; // dao-name,
                                                                         // method-name,
                                                                         // metadata

  // Getters

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
