package org.hotrod.metadata;

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
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class DataSetMetadataFactory {

  public static TableDataSetMetadata getMetadata(final JdbcTable t, final DatabaseAdapter adapter,
      final HotRodConfigTag config, final DataSetLayout layout, final CachedMetadata cachedMetadata)
      throws UnresolvableDataTypeException, ControlledException {

    TableTag tableTag = config.getTableTag(t);

    Map<String, Map<String, SelectMethodMetadata>> allDAOsSelectMetadata = cachedMetadata.getSelectMetadata();
    Map<String, SelectMethodMetadata> selectsMetadata = allDAOsSelectMetadata == null ? null
        : allDAOsSelectMetadata.get(tableTag.getName());

    if (tableTag != null) {
      return new TableDataSetMetadata(tableTag, t, adapter, config, layout, selectsMetadata);
    }

    EnumTag enumTag = config.getEnumTag(t);
    if (enumTag != null) {
      return new EnumDataSetMetadata(enumTag, t, adapter, config, layout, selectsMetadata);
    }

    ViewTag viewTag = config.getViewTag(t);
    if (viewTag != null) {
      return new TableDataSetMetadata(viewTag, t, adapter, config, layout, selectsMetadata);
    }

    throw new ControlledException("Could not find table, enum, or view with name '" + t.getName() + "'.");

  }

}
