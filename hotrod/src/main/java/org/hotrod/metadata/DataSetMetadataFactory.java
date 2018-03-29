package org.hotrod.metadata;

import java.util.List;
import java.util.Map;

import org.hotrod.ant.ControlledException;
import org.hotrod.config.EnumTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class DataSetMetadataFactory {

  public static TableDataSetMetadata getMetadata(final JdbcTable t, final DatabaseAdapter adapter,
      final HotRodConfigTag config, final DataSetLayout layout, final CachedMetadata cachedMetadata)
      throws UnresolvableDataTypeException, ControlledException {

    JdbcDatabase cachedDB = cachedMetadata == null ? null : cachedMetadata.getCachedDatabase();
    Map<String, Map<String, SelectMethodMetadata>> allDAOsSelectMetadata = cachedMetadata == null ? null
        : cachedMetadata.getSelectMetadata();

    TableTag tableTag = config.getTableTag(t);
    if (tableTag != null) {
      if (cachedDB == null) {
        tableTag.markGenerate(true);
      } else {
        JdbcTable o = findJdbcTable(cachedDB.getTables(), t.getName(), adapter);
        if (!t.isEquivalentTo(o)) {
          tableTag.markGenerate(true);
        }
      }
      Map<String, SelectMethodMetadata> selectsMetadata = allDAOsSelectMetadata == null ? null
          : allDAOsSelectMetadata.get(tableTag.getName());
      return new TableDataSetMetadata(tableTag, t, adapter, config, layout, selectsMetadata);
    }

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
      Map<String, SelectMethodMetadata> selectsMetadata = allDAOsSelectMetadata == null ? null
          : allDAOsSelectMetadata.get(enumTag.getName());
      return new EnumDataSetMetadata(enumTag, t, adapter, config, layout, selectsMetadata);
    }

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
      Map<String, SelectMethodMetadata> selectsMetadata = allDAOsSelectMetadata == null ? null
          : allDAOsSelectMetadata.get(viewTag.getName());
      return new TableDataSetMetadata(viewTag, t, adapter, config, layout, selectsMetadata);
    }

    throw new ControlledException("Could not find table, enum, or view with name '" + t.getName() + "'.");

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
