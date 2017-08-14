package org.hotrod.metadata;

import org.hotrod.ant.ControlledException;
import org.hotrod.config.EnumTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class DataSetMetadataFactory {

  public static TableDataSetMetadata getMetadata(final JdbcTable t, final DatabaseAdapter adapter,
      final HotRodConfigTag config) throws UnresolvableDataTypeException, ControlledException {
    TableTag tableTag = config.getTableTag(t);
    if (tableTag != null) {
      return new TableDataSetMetadata(tableTag, t, adapter, config);
    }

    EnumTag enumTag = config.getEnumTag(t);
    if (enumTag != null) {
      return new EnumDataSetMetadata(enumTag, t, adapter, config);
    }

    ViewTag viewTag = config.getViewTag(t);
    if (viewTag != null) {
      return new TableDataSetMetadata(viewTag, t, adapter, config);
    }

    throw new ControlledException("Could not find table, enum, or view with name '" + t.getName() + "'.");

  }

}
