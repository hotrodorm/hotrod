package org.hotrod.metadata;

import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.config.EnumTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.SelectMetadataCache;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class DataSetMetadataFactory {

  private static final Logger log = Logger.getLogger(DataSetMetadataFactory.class);

  public static TableDataSetMetadata getMetadata(final JdbcTable t, final DatabaseAdapter adapter,
      final HotRodConfigTag config, final DataSetLayout layout, final CachedMetadata cachedMetadata)
      throws UnresolvableDataTypeException, ControlledException {

    JdbcDatabase cachedDB = cachedMetadata.getCachedDatabase();
    HotRodConfigTag cachedConfig = cachedMetadata.getConfig();
    SelectMetadataCache selectMetadataCache = cachedMetadata.getSelectMetadataCache();

    // Table

    TableTag tableTag = config.getTableTag(t);
    if (tableTag != null) {
      if (cachedDB == null) {
        log.info(">>> Table '" + t.getName() + "' equivalent= NOT IN CACHE");
        tableTag.markGenerate(true);
      } else {
        JdbcTable o = findJdbcTable(cachedDB.getTables(), t.getName(), adapter);
        log.info(">>> Table '" + t.getName() + "' equivalent=" + t.isEquivalentTo(o));
        if (!t.isEquivalentTo(o)) {
          tableTag.markGenerate(true);
        }
      }
      TableDataSetMetadata tm = new TableDataSetMetadata(tableTag, t, adapter, config, layout, selectMetadataCache);
      if (cachedConfig != null && cachedConfig.findEnum(tm, adapter) != null) {
        // changed from enum to table - generate related tables
        markGenerateRelatedEntities(tm);
      }
      return tm;
    }

    // Enum

    EnumTag enumTag = config.getEnumTag(t);
    if (enumTag != null) {
      if (cachedDB == null) {
        enumTag.markGenerate(true);
      } else {
        JdbcTable o = findJdbcTable(cachedDB.getTables(), t.getName(), adapter);
        if (!t.isEquivalentTo(o)) {
          enumTag.markGenerate(true);
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
        viewTag.markGenerate(true);
      } else {
        JdbcTable o = findJdbcTable(cachedDB.getViews(), t.getName(), adapter);
        if (!t.isEquivalentTo(o)) {
          viewTag.markGenerate(true);
        }
      }
      return new TableDataSetMetadata(viewTag, t, adapter, config, layout, selectMetadataCache);
    }

    throw new ControlledException("Could not find table, enum, or view with name '" + t.getName() + "'.");

  }

  private static void markGenerateRelatedEntities(final DataSetMetadata tm) {
    for (ForeignKeyMetadata efk : tm.getExportedFKs()) {
      efk.getRemote().getTableMetadata().getDaoTag().markGenerate(true);
    }
    for (ForeignKeyMetadata ifk : tm.getImportedFKs()) {
      ifk.getRemote().getTableMetadata().getDaoTag().markGenerate(true);
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
