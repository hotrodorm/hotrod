package org.hotrod.metadata;

import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.EnumTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.SelectMetadataCache;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class DataSetMetadataFactory {

  private static final Logger log = Logger.getLogger(DataSetMetadataFactory.class);

  public static TableDataSetMetadata getMetadata(final JdbcTable t, final DatabaseAdapter adapter,
      final HotRodConfigTag config, final DataSetLayout layout, final CachedMetadata cachedMetadata)
      throws UnresolvableDataTypeException, InvalidConfigurationFileException {

    JdbcDatabase cachedDB = cachedMetadata.getCachedDatabase();
    HotRodConfigTag cachedConfig = cachedMetadata.getConfig();
    SelectMetadataCache selectMetadataCache = cachedMetadata.getSelectMetadataCache();

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
      TableDataSetMetadata tm = new TableDataSetMetadata(tableTag, t, adapter, config, layout, selectMetadataCache);
      log.debug("cachedConfig=" + cachedConfig);
      if (cachedConfig != null) {
        log.debug("cachedConfig.findEnum(tm, adapter)=" + cachedConfig.findEnum(tm, adapter));
        if (cachedConfig.findEnum(tm, adapter) != null) {
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
      EnumDataSetMetadata em = new EnumDataSetMetadata(enumTag, t, adapter, config, layout, selectMetadataCache);
      if (cachedConfig != null && cachedConfig.findEnum(em, adapter) != null) {
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
      return new TableDataSetMetadata(viewTag, t, adapter, config, layout, selectMetadataCache);
    }

    String msg = "Could not find table, enum, or view with name '" + t.getName() + "'.";
    throw new InvalidConfigurationFileException(config, msg, msg);

  }

  private static void markGenerateRelatedEntities(final DataSetMetadata tm) {
    for (ForeignKeyMetadata efk : tm.getExportedFKs()) {
      log.info(
          "...marking (using exported FK) remote=" + efk.getRemote().getTableMetadata().getId().getRenderedSQLName()
              + " local=" + efk.getLocal().getTableMetadata().getId().getRenderedSQLName());
      efk.getRemote().getTableMetadata().getDaoTag().markGenerate();
    }
    for (ForeignKeyMetadata ifk : tm.getImportedFKs()) {
      log.info(
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
