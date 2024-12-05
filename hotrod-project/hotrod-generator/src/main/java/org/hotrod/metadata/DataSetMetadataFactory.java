package org.hotrod.metadata;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.EnumTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.SelectMetadataCache;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class DataSetMetadataFactory {

  private static final Logger log = Logger.getLogger(DataSetMetadataFactory.class.getName());

  public static TableDataSetMetadata getMetadata(final JdbcTable t, final boolean isTable, final boolean autoDiscovery,
      final DatabaseAdapter adapter, final HotRodConfigTag config, final DataSetLayout layout,
      final boolean isFromCurrentCatalog, final boolean isFromCurrentSchema)
      throws UnresolvableDataTypeException, InvalidConfigurationFileException {
    return getMetadata(t, isTable, autoDiscovery, adapter, config, layout, null, isFromCurrentCatalog,
        isFromCurrentSchema);
  }

  private static TableDataSetMetadata getMetadata(final JdbcTable t, final boolean isTable, final boolean autoDiscovery,
      final DatabaseAdapter adapter, final HotRodConfigTag config, final DataSetLayout layout,
      final CachedMetadata cachedMetadata, final boolean isFromCurrentCatalog, final boolean isFromCurrentSchema)
      throws UnresolvableDataTypeException, InvalidConfigurationFileException {

    JdbcDatabase cachedDB = null;
    HotRodConfigTag cachedConfig = null;
    SelectMetadataCache selectMetadataCache = null;
    if (cachedMetadata != null) {
      cachedDB = cachedMetadata.getCachedDatabase();
      cachedConfig = cachedMetadata.getConfig();
      selectMetadataCache = cachedMetadata.getSelectMetadataCache();
    }

    // Table

    TableTag tableTag = config.getTableTag(t);
    if (tableTag != null) {
      TableDataSetMetadata tm = new TableDataSetMetadata(tableTag, t, tableTag.getExtendsTag(),
          tableTag.getExtendsJdbcTable(), adapter, config, layout, selectMetadataCache, isFromCurrentCatalog,
          isFromCurrentSchema);
      log.fine("cachedConfig=" + cachedConfig);
      return tm;
    }

    // Enum

    EnumTag enumTag = config.getEnumTag(t);
    if (enumTag != null) {
      EnumDataSetMetadata em = new EnumDataSetMetadata(enumTag, t, adapter, config, layout, selectMetadataCache,
          isFromCurrentCatalog, isFromCurrentSchema);
      return em;
    }

    // View

    ViewTag viewTag = config.getViewTag(t);
    if (viewTag != null) {
      return new TableDataSetMetadata(viewTag, t, adapter, config, layout, selectMetadataCache, isFromCurrentCatalog,
          isFromCurrentSchema);
    }

    // Not a declared table or view

    if (autoDiscovery) {

      if (isTable) {
        tableTag = new TableTag();
        tableTag.setCatalog(t.getCatalog());
        tableTag.setSchema(t.getSchema());
        tableTag.setName(t.getName());
        tableTag.validate(null, config, null, adapter, null);
        return new TableDataSetMetadata(tableTag, t, tableTag.getExtendsTag(), tableTag.getExtendsJdbcTable(), adapter,
            config, layout, selectMetadataCache, isFromCurrentCatalog, isFromCurrentSchema);
      } else {
        viewTag = new ViewTag();
        viewTag.setCatalog(t.getCatalog());
        viewTag.setSchema(t.getSchema());
        viewTag.setName(t.getName());
        viewTag.validate(null, config, null, adapter, null);
        return new TableDataSetMetadata(viewTag, t, adapter, config, layout, selectMetadataCache, isFromCurrentCatalog,
            isFromCurrentSchema);
      }

    } else {
      String msg = "Could not find table, enum, or view with name '" + t.getName() + "'.";
      throw new InvalidConfigurationFileException(config, msg);
    }

  }

  // Utilities

  public static JdbcTable findJdbcTable(final List<JdbcTable> tables, final String name,
      final DatabaseAdapter adapter) {
    for (JdbcTable t : tables) {
      if (adapter.isTableIdentifier(t.getName(), name)) {
        return t;
      }
    }
    return null;
  }

}
