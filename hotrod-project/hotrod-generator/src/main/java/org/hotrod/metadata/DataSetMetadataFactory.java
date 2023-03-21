package org.hotrod.metadata;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.EnumTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.SelectMetadataCache;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class DataSetMetadataFactory {

  private static final Logger log = LogManager.getLogger(DataSetMetadataFactory.class);

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
      if (cachedDB == null) {
        log.debug("##### Table '" + t.getName() + "' equivalent= NOT IN CACHE");
        tableTag.markGenerate();
      } else {
        JdbcTable o = findJdbcTable(cachedDB.getTables(), t.getName(), adapter);
        log.debug("##### Table '" + t.getName() + "' equivalent=" + t.isEquivalentTo(o));
        if (!t.isEquivalentTo(o)) {
          tableTag.markGenerate();
        }
      }
      TableDataSetMetadata tm = new TableDataSetMetadata(tableTag, t, tableTag.getExtendsTag(),
          tableTag.getExtendsJdbcTable(), adapter, config, layout, selectMetadataCache, isFromCurrentCatalog,
          isFromCurrentSchema);
      log.debug("cachedConfig=" + cachedConfig);
      if (cachedConfig != null) {
        log.debug("cachedConfig.findEnum(tm, adapter)=" + cachedConfig.findFacetEnum(tm, adapter));
        if (cachedConfig.findFacetEnum(tm, adapter) != null) {
          // changed from enum to table - generate related tables
          markGenerateRelatedEntities(tm);
        }
      }
      return tm;
    }

    // Enum

    EnumTag enumTag = config.getEnumTag(t);
    if (enumTag != null) {
      if (cachedDB == null) {
        enumTag.markGenerate();
      } else {
        JdbcTable o = findJdbcTable(cachedDB.getTables(), t.getName(), adapter);
        if (!t.isEquivalentTo(o)) {
          enumTag.markGenerate();
        }
      }
      EnumDataSetMetadata em = new EnumDataSetMetadata(enumTag, t, adapter, config, layout, selectMetadataCache,
          isFromCurrentCatalog, isFromCurrentSchema);
      if (cachedConfig != null && cachedConfig.findFacetEnum(em, adapter) != null) {
        // changed from table to enum - generate related tables
        markGenerateRelatedEntities(em);
      }
      return em;
    }

    // View

    ViewTag viewTag = config.getViewTag(t);
    if (viewTag != null) {
      if (cachedDB == null) {
        viewTag.markGenerate();
      } else {
        JdbcTable o = findJdbcTable(cachedDB.getViews(), t.getName(), adapter);
        if (!t.isEquivalentTo(o)) {
          viewTag.markGenerate();
        }
      }
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
        tableTag.validate(null, config, null, adapter);
        return new TableDataSetMetadata(tableTag, t, tableTag.getExtendsTag(), tableTag.getExtendsJdbcTable(), adapter,
            config, layout, selectMetadataCache, isFromCurrentCatalog, isFromCurrentSchema);
      } else {
        viewTag = new ViewTag();
        viewTag.setCatalog(t.getCatalog());
        viewTag.setSchema(t.getSchema());
        viewTag.setName(t.getName());
        viewTag.validate(null, config, null, adapter);
        return new TableDataSetMetadata(viewTag, t, adapter, config, layout, selectMetadataCache, isFromCurrentCatalog,
            isFromCurrentSchema);
      }

    } else {
      String msg = "Could not find table, enum, or view with name '" + t.getName() + "'.";
      throw new InvalidConfigurationFileException(config, msg);
    }

  }

  private static void markGenerateRelatedEntities(final DataSetMetadata tm) {
    for (ForeignKeyMetadata efk : tm.getExportedFKs()) {
      log.debug(
          "...marking (using exported FK) remote=" + efk.getRemote().getTableMetadata().getId().getRenderedSQLName()
              + " local=" + efk.getLocal().getTableMetadata().getId().getRenderedSQLName());
      efk.getRemote().getTableMetadata().getDaoTag().markGenerate();
    }
    for (ForeignKeyMetadata ifk : tm.getImportedFKs()) {
      log.debug(
          "...marking (using imported FK) remote=" + ifk.getRemote().getTableMetadata().getId().getRenderedSQLName()
              + " local=" + ifk.getLocal().getTableMetadata().getId().getRenderedSQLName());
      ifk.getRemote().getTableMetadata().getDaoTag().markGenerate();
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
